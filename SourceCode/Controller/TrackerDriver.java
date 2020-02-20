/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Handles all the tracking functionality and observes
* on the users location values
******************************************************************/

package Controller;

import Model.*;
import java.util.*;

public class TrackerDriver implements Observer
{
    private double latitude;
    private double longitude;
    private double altitude;

    private LocationDriver locationDriver;
    private static Map<String, RouteComponent> routeMap;
    private GeoUtils geo;
    private UserIO userIO;
    private UserInterfaceController uiController;

    private ArrayList<RouteComponent> entireRoute;
    private RouteComponent selectedRoute;

    private WayPoint currentWayPoint;
    private WayPoint expectedWayPoint;
    private RouteComponent currentSegment;


    public TrackerDriver(LocationDriver inLocationDriver,Map<String, RouteComponent> inRouteMap, UserIO inIO, GeoUtils inGeo, UserInterfaceController uiController)
    {
        this.locationDriver = inLocationDriver;
        this.routeMap = inRouteMap;
        locationDriver.registerObserver(this);
        this.uiController = uiController;

        geo = inGeo;
        userIO = inIO;
    }

    public TrackerDriver()
    {
        latitude = 0.0;
        longitude = 0.0;
        altitude = 0.0;
        locationDriver = null;
        routeMap = null;
        geo = null;
        userIO = null;
    }

    /******************************************************************
    *SUBMODULE: go
    *IMPORTS: String routeName
    *EXPORTS: NONE
    *ASSERTION: initialises and runs the tracking functionality.
    ******************************************************************/
    public void go(String routeName)
    {
        //gets the chosen route
        selectedRoute = routeMap.get(routeName);
        ArrayList<RouteComponent> routeList = selectedRoute.getRouteList();
        //merges the route and its subroutes if any into one list
        entireRoute = selectedRoute.getCompleteRoute();
        entireRoute = updateWayPointDistances(entireRoute);
        //sets the current waypoint to the starting point
        currentWayPoint = entireRoute.get(0).getStartPoint();

        locationDriver.setTrackerDriver(this);
        locationDriver.setRoute(entireRoute);
        //registers tracker as an observer in locationDriver
        locationDriver.registerObserver(this);
        //initialises the simulation
        locationDriver.initialiseSimulation(routeName);

        displayStartLocation();
        locationDriver.simulateUserMoving();
        locationDriver.removeObserver(this);
    }


    public void initialiseData(double inLatitude, double inLongitude, double inAltitude)
    {
        this.latitude = inLatitude;
        this.longitude = inLongitude;
        this.altitude = inAltitude;
    }

    /******************************************************************
    *SUBMODULE: update
    *IMPORTS: double inLatitude, dobule inLongitude, dobule inAltitude
    *EXPORTS: NONE
    *ASSERTION: receieves the new notified values for the user and processes.
    ******************************************************************/
    public void update(double inLatitude, double inLongitude, double inAltitude)
    {
        WayPoint[] pArray = new WayPoint[2];
        RouteComponent currentSegment;
        int index = 0;

        double userLat, userLong, userAlt, expectedLat, expectedLong, expectedAlt;
        this.latitude = inLatitude;
        this.longitude = inLongitude;
        this.altitude = inAltitude;

        //retrieves the segment currently up to
        currentSegment = entireRoute.get(locationDriver.getSegmentIndex());
        expectedWayPoint = currentSegment.getEndPoint();


        //set the user values
        userLat = latitude;
        userLong = longitude;
        userAlt = altitude;

        //update expected location values
        expectedLat = expectedWayPoint.getLatitude();
        expectedLong = expectedWayPoint.getLongitude();
        expectedAlt = expectedWayPoint.getAltitude();

        //calc distance summary to next wayPoint
        double horizontalDistance = geo.calcMetresDistance(
                                         userLat,
                                         userLong,
                                         expectedLat,
                                         expectedLong);

        //maintains absolute value
        if(horizontalDistance < 0)
        {
            horizontalDistance = horizontalDistance * -1;
        }

        //Determine the vetical distances
        double climbDistance = expectedAlt - userAlt;
        double decentDistance = userAlt - expectedAlt;


        if(climbDistance < 0)
        {
           climbDistance = climbDistance *-1;
        }

        if(decentDistance < 0)
        {
           decentDistance = decentDistance * -1;
        }

        //display the latest gps data
        displayGPSLocation();

        //check if the current user location is a waypoint
        boolean wayPointReached = reachedWayPoint(horizontalDistance, climbDistance, decentDistance);
        if(wayPointReached)
        {
            //update the current waypoint to the expected one
            currentWayPoint = expectedWayPoint;

            locationDriver.setCurrentWayPoint(currentWayPoint);
            //increment the segment count to proceed to target the next waypoint
            locationDriver.incrementSegmentCount();

        }

        //display the remaing distance to the next waypoint and rest of route
        displayRemainingDistances(currentWayPoint, selectedRoute,
                               horizontalDistance, climbDistance, decentDistance);

        //if the user had reached a waypoint then display
        if(wayPointReached)
        {
            displayCurrentWayPoint();
        }

        //if the current waypoint is the end of the list then complete the tracking
        if(currentWayPoint.isEqual(currentWayPoint, selectedRoute.getEndPoint()))
        {
            locationDriver.setEnd(true);
            uiController.outputString("\n\n\n=========== Route Completed ============");

        }
        else
        {
            //otherwise prompt the user if they would like to indicate a waypoint
            uiController.outputString("\n\nPlease select one of the following options:");
            uiController.outputString("\n(1)Manually indicate a waypoint location");
            uiController.outputString("(2)Continue Tracking");
            int selection = userIO.validInput(1,2);

            if(selection == 1)
            {
                //gets the start and end waypoint relative to the selected waypoint
                pArray = indicateWayPoint(entireRoute);
                //determines the index in the route to which the sleected waypoint occurs
                index = updateSegment(pArray, entireRoute);
                locationDriver.setIndex(index);
            }
            else
            {
                //validate user location againts expected waypoint location
                locationDriver.incrementIndex();
            }
        }
    }



    public void setLocationDriver(LocationDriver locationDriver)
    {
        this.locationDriver = locationDriver;
    }

    public void setRouteMap(Map<String, RouteComponent> inRouteMap)
    {
        this.routeMap = inRouteMap;
    }

    public void setUIController(UserInterfaceController ui)
    {
        this.uiController = ui;
    }

    public void setGeoUtils(GeoUtils geo)
    {
        this.geo = geo;
    }

    public void setUserIO(UserIO uIO)
    {
        this.userIO  = uIO;
    }

    /******************************************************************
    *SUBMODULE: displayRemainingDistances
    *IMPORTS: WayPoint currentWayPoint, RouteComponent, double, double, dobule
    *EXPORTS: NONE
    *ASSERTION: computes the running distances for displaying
    ******************************************************************/
    public void displayRemainingDistances(WayPoint currentWayPoint, RouteComponent selectedRoute,
                                       double horizontalDistance, double climbDistance, double descentDistance)
    {
        //determines the remaining distance in the route
        double distancetoUser = geo.calcMetresDistance(
                                            latitude,
                                            longitude,
                                            currentWayPoint.getLatitude(),
                                            currentWayPoint.getLongitude());
        //uses each waypoints running total to determine how far the user has gone
        double distanceSoFar = currentWayPoint.getHDistanceFromStart() + distancetoUser;
        double hDistanceRemaining = selectedRoute.getHorizontalDistance() - distanceSoFar;
        double climbRemaining = selectedRoute.getClimbDistance() - currentWayPoint.getClimbFromStart();
        double descentRemaining = selectedRoute.getDescentDistance() - currentWayPoint.getDecentFromStart();

        String distanceUpdate = "";

        distanceUpdate += "\n\n            Distance till next WayPoint";
        distanceUpdate += "\n                Horizontal: " + Math.round(horizontalDistance);
        distanceUpdate += "\n                Climbing: " + Math.round(climbDistance);
        distanceUpdate += "\n                Descent: " + Math.round(descentDistance);
        distanceUpdate += "\n\n            Distance till end of Route";
        distanceUpdate += "\n                Horizontal: " + Math.round(hDistanceRemaining);
        distanceUpdate += "\n                Climbing: " + Math.round(climbRemaining);
        distanceUpdate += "\n                Descent: " + Math.round(descentRemaining);

        uiController.outputString(distanceUpdate);

    }

    /******************************************************************
    *SUBMODULE: updateSegment
    *IMPORTS: WayPoint[], ArrayList
    *EXPORTS: int
    *ASSERTION: Determines the segment to whcih the user selected waypoint
    * occurrs in the entire list
    ******************************************************************/
    public int updateSegment(WayPoint[] pArray, ArrayList<RouteComponent> entireRoute)
    {
        int index = 0;
        int count = 0;
        WayPoint tempStart = pArray[0];
        WayPoint tempEnd = pArray[1];
        boolean found = false;
        boolean end = false;
        Iterator<RouteComponent> iterator = entireRoute.iterator();

        //iterates throught the route list
        while(iterator.hasNext() && found != true)
        {
            RouteComponent segment = iterator.next();
            //compares the start and end points to each other
            if(segment.getStartPoint().isEqual(segment.getStartPoint(), tempStart))
            {
                if(segment.getEndPoint().isEqual(segment.getEndPoint(), tempEnd));
                {
                    //maintains th eindex position in the list
                    index = count;
                    found = true;
                }
            }
            count ++;
        }
        return index;
    }


    /******************************************************************
    *SUBMODULE: indicateWayPoint
    *IMPORTS: ArrayList entireROute
    *EXPORTS: WayPoint[]
    *ASSERTION: gets the user selected waypoint and maintains it as the start point
    *allowing the end point of the virtual segment to be maintained
    ******************************************************************/
    public WayPoint[] indicateWayPoint(ArrayList<RouteComponent> entireRoute)
    {

        ArrayList<WayPoint> wayPointList = new ArrayList<WayPoint>();
        int count = 1;
        WayPoint[] pArray = new WayPoint[2];

        //builds a list of all the waypoints
        wayPointList.add(entireRoute.get(0).getStartPoint());
        for(RouteComponent segment: entireRoute)
        {
            wayPointList.add(segment.getEndPoint());
        }

        //displays the list of waypoints and a attached number value
        for(WayPoint wayPoint:wayPointList)
        {
            uiController.outputString("\n(" + count + ")" + "WayPoint: ");
            uiController.outputString("\n    " + wayPoint.toString());
            count++;
        }

        //retrieves the chosen waypoint
        Scanner sc = new Scanner(System.in);
        uiController.outputString("\nEnter the number of the waypoint to indicate: ");
        int selection = sc.nextInt() - 1;

        WayPoint tempStart = null;
        WayPoint tempEnd = wayPointList.get(selection);

        //creates an array of a temp segment
        //if the selection is above the first one then use the previous
        if(selection > 0)
        {
            tempStart = wayPointList.get(selection-1);
        }
        else
        {
            tempStart = wayPointList.get(selection);
            tempEnd = wayPointList.get(selection + 1);

        }

        pArray[0] = tempStart;
        pArray[1] = tempEnd;

        return pArray;
    }

    /******************************************************************
    *SUBMODULE: reachedWayPoint
    *IMPORTS: dobule horizontalDistance, double climbDistance, double decentDistance
    *EXPORTS: boolean
    *ASSERTION: determines if the current user location is within the right
    * distances to be assume at the waypoint
    ******************************************************************/
    public boolean reachedWayPoint(double horizontalDistance, double climbDistance, double decentDistance)
    {
        boolean wayPointReached = false;
        int compareVal = Double.compare(horizontalDistance, 10.0);
        if(compareVal < 0) //difference less than 10m
        {
            compareVal = Double.compare(climbDistance, 2.0);
            int compareVal2 = Double.compare(decentDistance, 2.0);
            if(compareVal < 0 || compareVal2 < 0) //vertical distance less than 2m
            {
                wayPointReached = true;
            }

        }
        return wayPointReached;
    }




    /******************************************************************
    *SUBMODULE: displayGPSLocation
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: displayes the current user location each update
    ******************************************************************/
    public void displayGPSLocation()
    {
        String gpsLocation = "";
        gpsLocation += "\n\n       ======= GPS Location Updated ========";
        gpsLocation += "\n            Latitude: " + latitude;
        gpsLocation += "\n            Longitude: " + longitude;
        gpsLocation += "\n            Altitude: " + altitude;
        uiController.outputString(gpsLocation);
    }


    /******************************************************************
    *SUBMODULE: displayCurrentWayPoint
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: displays the informatino of the current way point once
    * reached by the user
    ******************************************************************/
    public void displayCurrentWayPoint()
    {
        String currWayPoint = "";
        currWayPoint += "\n\n<<<<<<<<< Reached WayPoint >>>>>>>>>>>";
        currWayPoint += "\n   Latitude: " + latitude;
        currWayPoint += "\n   Longitude: " + longitude;
        currWayPoint += "\n   Altitude: " + altitude;
        currWayPoint += "\n<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>";
        uiController.outputString(currWayPoint);
    }

    /******************************************************************
    *SUBMODULE: displayStartLocation
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: displays the initial start information
    ******************************************************************/
    public void displayStartLocation()
    {
        String startLocation = "";
        startLocation += "\n============== Start of Route ==============";
        startLocation += "\n   --- Current WayPoint/ User Location ---";
        startLocation += "\n     Latitude: " + latitude;
        startLocation += "\n      Longitude: " + longitude;
        startLocation += "\n      Altitude: " + altitude;
        uiController.outputString(startLocation);
    }

    /******************************************************************
    *SUBMODULE: updateWayPointDistances
    *IMPORTS: ArrayList segmneList
    *EXPORTS: ArrayList
    *ASSERTION: iterates through the entire list and maintains the running
    * distance totals
    ******************************************************************/
    public ArrayList<RouteComponent> updateWayPointDistances(ArrayList<RouteComponent> segmentList)
    {
        double runningTotal = 0.0;
        double horizontalDistance = 0.0;

        double climbingDistance = 0.0;
        double descentDistance = 0.0;

        segmentList.get(0).getStartPoint().setDistancetFromStart(0.0);
        segmentList.get(0).getStartPoint().setClimbFromStart(0.0);
        segmentList.get(0).getStartPoint().setDecentFromStart(0.0);
        for(RouteComponent segment: segmentList)
        {
            WayPoint start = segment.getStartPoint();
            WayPoint end = segment.getEndPoint();

            horizontalDistance = geo.calcMetresDistance(
                                    start.getLatitude(),
                                    start.getLongitude(),
                                    end.getLatitude(),
                                    end.getLongitude());

            runningTotal += horizontalDistance;
            end.setDistancetFromStart(runningTotal);
            double startAlt = start.getAltitude();
            double endAlt = end.getAltitude();

            int compareVal = Double.compare(startAlt, endAlt);
            if(compareVal < 0) //start < end i.e increase in altitude
            {
                climbingDistance += (endAlt - startAlt);
            }
            else if(compareVal > 0) //start > end i.e decrease in altitude
            {
                descentDistance += (startAlt - endAlt);
            }

            end.setClimbFromStart(climbingDistance);
            end.setDecentFromStart(descentDistance);

        }

        return segmentList;
    }
}
