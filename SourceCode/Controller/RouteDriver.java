/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Drives the functionality for loading and extracting the
*route data
******************************************************************/

package Controller;

import Model.*;
import java.util.*;
import java.io.*;


public class RouteDriver
{
    private static Map<String, RouteComponent> routeMap;
    private static GeoUtils geo;

    /******************************************************************
    *SUBMODULE: loadData
    *IMPORTS: NONE
    *EXPORTS: int
    *ASSERTION: loads the data into the program
    ******************************************************************/
    public static int loadData()
    {
        String data = "";
        routeMap  = new HashMap<String, RouteComponent>();
        int valid = -1;
        try
        {
            //gets the data from the database
            data = geo.retrieveRouteData();
            //calls for the data to be extracted into its components
            extractRoute(data);
            //generates a merged list of all segments including any subroutes
            //within each route
            generateCompleteRoutes();

        } //handles any io exceptions from the retrieving data and any invalid data
        catch(InvalidFileFormatException | IOException  e)
        {
            System.out.println("\n\n********************");
            System.out.println("Exception Occurred: " + e);
            System.out.println("********************\n\n");
            valid = 0;
        }

        return valid;

    }

    public void setGeoUtils(GeoUtils geo)
    {
        this.geo = geo;
    }

    /******************************************************************
    *SUBMODULE: getRouteMap
    *IMPORTS: NONE
    *EXPORTS: Map<String, RouteComponent>
    *ASSERTION: getter for the route map
    ******************************************************************/
    public Map<String, RouteComponent> getRouteMap()
    {
        return routeMap;
    }

    //for each route generates a merge segment list to include any subroutes
    public static void generateCompleteRoutes() throws InvalidFileFormatException
    {
        for(RouteComponent route: routeMap.values())
        {
            route.setCompleteRoute(mergeRoutes(route));
        }
    }

    /******************************************************************
    *SUBMODULE: mergeRoutes
    *IMPORTS: RouteComponent route
    *EXPORTS: ArrayList<RouteComponent>
    *ASSERTION: takes in a route and recursivley creates an entire list
    * of segments in one list, including all subroutes. Also validates
    * that a subroute start and end coordinates are close enough to
    * the respective main route waypoints
    ******************************************************************/
    public static ArrayList<RouteComponent> mergeRoutes(RouteComponent route)throws InvalidFileFormatException
    {
        ArrayList<RouteComponent> segmentList = new ArrayList<RouteComponent>();
        ArrayList<RouteComponent> subRouteList = new ArrayList<RouteComponent>();
        WayPoint currentWayPoint = route.getStartPoint();
        WayPoint expectedWayPoint = null;


        for(RouteComponent segment: route.getRouteList())
        {
            //if there is something in the descritpion and the description starts with an *
            //this means the descritpion is a sub route, signaling the start of
            //another recursion level

            //maintains a current and expected way point that represent the start
            //and end point of a subroute within the mainroute
            currentWayPoint = segment.getStartPoint();
            expectedWayPoint = segment.getEndPoint();

            if(segment.getDescription().isEmpty() != true && segment.getDescription().charAt(0) == '*')
            {
                //substrings to remove the * from the name
                String subRouteName = segment.getDescription().substring(1);
                //retrieves the route
                RouteComponent subRoute = routeMap.get(subRouteName);
                //validates the start of the subroute is close enough to the mainroute waypoint
                if(validateSubRoute(currentWayPoint, subRoute.getStartPoint()))
                {
                    //generates the subroute list
                    subRouteList = mergeRoutes(subRoute);

                    //validates the end of the subroute is close enough to the mainroute waypoint
                    if(validateSubRoute(expectedWayPoint, subRoute.getEndPoint()))
                    {
                        //if valid stores the subroute data into the mainlist
                        segmentList.addAll(subRouteList);
                    }
                    else
                    {
                        throw new InvalidFileFormatException("Invalid End to a SubRoute");
                    }
                }
                else
                {
                    throw new InvalidFileFormatException("Invalid Start to a SubRoute");
                }
            }
            else
            {
                //otherwise add the segment to the list
                segmentList.add(segment);
            }
        }

        return segmentList;
    }

    /******************************************************************
    *SUBMODULE: validateSubRoute
    *IMPORTS: WayPoint 1 WayPoint 2
    *EXPORTS: boolean valid
    *ASSERTION: compares two waypoints and checks within 10m horizontal distance
    * and 2m vertical distance
    ******************************************************************/
    public static boolean validateSubRoute(WayPoint point1, WayPoint point2)
    {
        boolean valid = false;
        double horizontalDistance = geo.calcMetresDistance(
                                            point1.getLatitude(),
                                            point1.getLongitude(),
                                            point2.getLatitude(),
                                            point2.getLongitude());

        double verticalDistance = point1.getAltitude() - point2.getAltitude();
        if(verticalDistance < 0) // maintains the absolute value
        {
            verticalDistance = verticalDistance *-1;
        }

        int compareVal1 = Double.compare(horizontalDistance, 10.0);
        int compareVal2 = Double.compare(verticalDistance, 2.0);

        if(compareVal1 < 0 )
        {
            if(compareVal2 < 0)
            {
                valid = true;
            }
        }

        return valid;
    }

    /******************************************************************
    *SUBMODULE: getRouteSummary
    *IMPORTS: NONE
    *EXPORTS: String routeSummary
    *ASSERTION: builds a string for the initial startup
    ******************************************************************/
    public static String getRouteSummary()
    {
        String routeSummary = "";
        double [] distanceArray = new double[3];
        routeSummary += "\n\n\n=================== MAIN MENU ====================\n\n";
        String str = "";

        //for each route retrieves the name, start and end poitns then
        // also adds each ones distance values
        for(RouteComponent route : routeMap.values())
        {
            //sets the distance values for the routes
            setRouteDistance(route);

            routeSummary += route.routeSummary();
            routeSummary += "     Horizontal Distance: " + route.getHorizontalDistance();
            routeSummary += "\n     Vertical Climbing: " + route.getClimbDistance();
            routeSummary += "\n     Vertical Descent: " + route.getDescentDistance();
        }
        return routeSummary;
    }

    /******************************************************************
    *SUBMODULE: getRouteNames
    *IMPORTS: NONE
    *EXPORTS: String names
    *ASSERTION: generates a string of all the route names stored in the
    * route map
    ******************************************************************/
    public static String getRouteNames()
    {
        String names = "";

        for(RouteComponent route: routeMap.values())
        {
            names += "\nRoute name: " + route.getName();
        }

        return names;
    }


    /******************************************************************
    *SUBMODULE: validateRouteName
    *IMPORTS: String routeName
    *EXPORTS: boolean valid
    *ASSERTION: checks if a routename exists within the stored routes
    ******************************************************************/
    public static boolean validateRouteName(String routeName)
    {
        boolean valid = false;

        for(RouteComponent route: routeMap.values())
        {
            if(routeName.equals(route.getName()))
            {
                valid = true;
            }
        }
        return valid;
    }

    /******************************************************************
    *SUBMODULE: getRouteDetails
    *IMPORTS: String routeName
    *EXPORTS: String routeDetails
    *ASSERTION: builds a string of the more specific details for  a route
    ******************************************************************/
    public static String getRouteDetails(String routeName)
    {
        RouteComponent route = routeMap.get(routeName);
        String routeDetails = "";

        routeDetails += "\n---------------------------------";
        routeDetails += "\nDetails of the Route: " + route.getName();
        routeDetails += "\n---------------------------------";
        routeDetails += "\nRoute Description: " + route.getDescription();
        routeDetails += "\n-------\n";
        routeDetails += getDetailsRecursive(route);
        routeDetails += "\nWaypoint: " + route.getEndPoint().toString();

        return routeDetails;
    }

    /******************************************************************
    *SUBMODULE: getDetailsRecursive
    *IMPORTS: RouteComponent route
    *EXPORTS: String details
    *ASSERTION: recursivley gets all of a routes details
    ******************************************************************/
    public static String getDetailsRecursive(RouteComponent route)
    {
        RouteComponent subRoute;
        String details = "";

        for(RouteComponent segment: route.getRouteList())
        {
            //if a subroute occurs recusrvily retrieve its details
            if(segment.getDescription().charAt(0) == '*')
            {
                details += "\nSUBROUTE: ";
                subRoute = routeMap.get(segment.getDescription().substring(1));
                details += getDetailsRecursive(subRoute);
            }
            else
            {
                details += "\n" + segment.printAllData();
            }
        }

        return details;
    }



    /******************************************************************
    *SUBMODULE: extractRoute
    *IMPORTS: String routeData
    *EXPORTS: NONE
    *ASSERTION: using the route factory stores routes into th eroute map
    * and then waypoints into segments into routes
    ******************************************************************/
    public static void extractRoute(String routeData)throws InvalidFileFormatException
    {
        String lines[], wayPoint[], routeDetails[];
        String routeName = "", routeDescription = "", latitude = "",
               longitude = "", altitude = "",
               segmentDescription = "", subRoute = "";

        int wayPointCount = 0;
        RouteDataFactory factory = new RouteDataFactory();
        Object obj = null;
        RouteComponent currRoute = null;
        boolean startWayPoint = true;

        RouteComponent routeIn;
        RouteComponent segmentIn;
        WayPoint wayPointIn;

        //newline characters for unix and windows
        //removes any empty lines
        lines = routeData.split("[\\r\\n]+");

        //iterates through each line
        for(int i = 0; i< lines.length; i++)
        {
            routeIn = new Route();
            segmentIn = new Segment();
            wayPointIn = new WayPoint();
            //removes the leading and trailing whitespace of the line
            //https://www.geeksforgeeks.org/trim-remove-leading-trailing-spaces-string-java/
            lines[i] = lines[i].trim();

            //checks if the line contains characters and not just whitespace
            if(lines[i].length() > 0)
            {
                //sends the line to the factory to determine what it is
                obj = factory.makeRoute(lines[i], routeIn, segmentIn, wayPointIn);

                if(obj instanceof Route)
                {
                    //if its a Route object then add it to the route map
                    RouteComponent tempRoute = (Route)obj;
                    routeMap.put(tempRoute.getName(), tempRoute);
                    currRoute = tempRoute;
                    //set the start point boolean to true so the next waypoint
                    //is set as the starting point
                    startWayPoint = true;
                }
                else if(obj instanceof Segment)
                {
                    RouteComponent tempSegment = (Segment)obj;
                    //if start point is true the start of the segment is the
                    //start of the route
                    if(startWayPoint)
                    {
                        currRoute.setStart(tempSegment.getStartPoint());
                    }
                    else
                    {
                        //otherwise extract the route list from the current route,
                        // and retrieve the last added segment
                        ArrayList<RouteComponent> tempList = currRoute.getRouteList();
                        RouteComponent prevSegment = tempList.get(tempList.size()-1);
                        //set the start of the new segment as the end of the
                        //previous segment
                        prevSegment.setEnd(tempSegment.getStartPoint());
                    }
                    //set that the startway point as been added
                    startWayPoint = false;
                    //add the segment to the current routes route list
                    currRoute.getRouteList().add(tempSegment);

                }
                else if(obj instanceof WayPoint)
                {
                    /*if a waypoint is returned then this means its the
                    last waypoint of a route. The last added sgement is retrieved
                    and the waypoint is set as its end and also the current routes
                    end point*/
                    WayPoint tempWayPoint = (WayPoint)obj;
                    ArrayList<RouteComponent> tempList = currRoute.getRouteList();
                    if(tempList.size() == 0)
                    {
                        throw new InvalidFileFormatException("Invalid number of waypoints");
                    }
                    else
                    {
                        RouteComponent prevSegment = tempList.get(tempList.size()-1);
                        prevSegment.setEnd(tempWayPoint);
                        currRoute.setEnd(tempWayPoint);
                    }
                }
            }
        }
    }



    /******************************************************************
    *SUBMODULE: setRouteDistance
    *IMPORTS: RouteComponent route
    *EXPORTS: NONE
    *ASSERTION: retreieves an array of doubles and stores into a given route
    ******************************************************************/
    public static void setRouteDistance(RouteComponent route)
    {
        double [] distanceArray = new double[3];

        distanceArray = recursiveDistance(route, distanceArray);

        route.setHorizontalDistance(distanceArray[0]);
        route.setClimbingDistance(distanceArray[1]);
        route.setDecentDistance(distanceArray[2]);
    }

    /******************************************************************
    *SUBMODULE: recursiveDistance
    *IMPORTS: RouteComponent route, double array
    *EXPORTS: double array
    *ASSERTION: recursivley goes within a route and populates its distance
    * values for each waypoint and segment
    ******************************************************************/
    public static double[] recursiveDistance(RouteComponent route, double [] distanceArray)
    {
        WayPoint routeStart = route.getStartPoint();
        double segmentDistance = 0;
        WayPoint segStart = null;
        WayPoint segEnd = null;
        RouteComponent subRoute;
        double totalRouteDistance = 0;
        double totalDistanceUp = 0;
        double totalDistanceDown = 0;
        double startAlt, endAlt;
        int compareVal;

        double []subDistanceArray = new double[3];

        for(RouteComponent segment: route.getRouteList())
        {
            segStart = segment.getStartPoint();
            segEnd = segment.getEndPoint();

            //if the current segment contains a subroute then retrieve its values
            if(segment.getDescription().isEmpty() != true && segment.getDescription().charAt(0) == '*')
            {
                 subRoute = routeMap.get(segment.getDescription().substring(1));
                 subDistanceArray = recursiveDistance(subRoute, subDistanceArray);

                totalDistanceUp += subDistanceArray[1];
                totalDistanceDown += subDistanceArray[2];
                //check this. is this including the segment that the sub route
                //shoud be replaciong?
                totalRouteDistance += subDistanceArray[0];

            }
            else
            {
                //calculate the horizontal distance of the segment
                segmentDistance = geo.calcMetresDistance(
                                        segStart.getLatitude(),
                                        segStart.getLongitude(),
                                        segEnd.getLatitude(),
                                        segEnd.getLongitude());
                segment.setHorizontalDistance(segmentDistance);

                //start of the segment is the current total route running distance
                segStart.setDistancetFromStart(totalRouteDistance);
                //update the total running distance with the segment distance
                totalRouteDistance += segmentDistance;
                //set the distance of the segment
                segEnd.setDistancetFromStart(totalRouteDistance);


                //Set vertical distances
                startAlt = segStart.getAltitude();
                endAlt = segEnd.getAltitude();

                segStart.setClimbFromStart(totalDistanceUp);
                segStart.setDecentFromStart(totalDistanceDown);

                compareVal = Double.compare(startAlt, endAlt);
                if(compareVal < 0) //start < end i.e increase in altitude
                {
                    totalDistanceUp += (endAlt - startAlt);
                }
                else if(compareVal > 0) //start > end i.e decrease in altitude
                {
                    totalDistanceDown += (startAlt - endAlt);
                }

                segEnd.setClimbFromStart(totalDistanceUp);
                segEnd.setDecentFromStart(totalDistanceDown);

            }
        }

        //rounded the values for nicer displaying
        distanceArray[0] = Math.round(totalRouteDistance);
        distanceArray[1] = Math.round(totalDistanceUp);
        distanceArray[2] = Math.round(totalDistanceDown);

        return distanceArray;
    }
}
