/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Represents the concrete subject. Generates the user data
* and notifies all observers of new data
******************************************************************/

package Controller;

import Model.*;
import Controller.*;
import java.util.*;

public class LocationDriver extends GpsLocator implements Subject
{
    private double latitude;
    private double longitude;
    private double altitude;

    private ArrayList<Observer> observerList;
    private ArrayList<WayPoint> theClimbData;
    private ArrayList<WayPoint> mainRouteData;
    private ArrayList<WayPoint> theStrollData;
    private ArrayList<WayPoint> userData;

    private int selectedIndex;
    private int segmentCounter;
    private int maxIndex;
    private boolean reachedEnd;
    private WayPoint currentWayPoint;
    private WayPoint expectedWayPoint;
    private RouteComponent currentSegment;
    private ArrayList<RouteComponent> entireRoute;
    private TrackerDriver tracker;

    public LocationDriver()
    {
        observerList = new ArrayList<Observer>();
        theClimbData = new ArrayList<WayPoint>();
        mainRouteData = new ArrayList<WayPoint>();
        theStrollData = new ArrayList<WayPoint>();
        userData = new ArrayList<WayPoint>();
        this.tracker = null;

        selectedIndex = 1;
        segmentCounter = 0;
        maxIndex = 0;
        reachedEnd = false;
    }

    public LocationDriver(TrackerDriver tracker)
    {
        observerList = new ArrayList<Observer>();
        theClimbData = new ArrayList<WayPoint>();
        mainRouteData = new ArrayList<WayPoint>();
        theStrollData = new ArrayList<WayPoint>();
        userData = new ArrayList<WayPoint>();

        this.tracker = tracker;

        selectedIndex = 1;
        segmentCounter = 0;
        maxIndex = 0;
        reachedEnd = false;
    }

    public void setTrackerDriver(TrackerDriver inTracker)
    {
        this.tracker = inTracker;
    }

    public void setRoute(ArrayList<RouteComponent> entireRoute)
    {
        this.entireRoute = entireRoute;
    }

    //sets the index value and updates values
    public void setIndex(int inIndex)
    {
        if(inIndex < maxIndex)
        {
            this.selectedIndex = inIndex;
            this.segmentCounter = inIndex;
        }

        currentSegment = entireRoute.get(selectedIndex);
        currentWayPoint = currentSegment.getEndPoint();
        selectedIndex = syncIndex(currentWayPoint);
    }

    public void setEnd(boolean inEnd)
    {
        this.reachedEnd = inEnd;
    }

    public void setCurrentWayPoint(WayPoint inPoint)
    {
        this.currentWayPoint = inPoint;
    }

    public int getSegmentIndex()
    {
        return segmentCounter;
    }

    //increments the user data index ensureing its still within bounds
    public void incrementIndex()
    {
        if(selectedIndex < maxIndex -1)
        {
            this.selectedIndex++;

        }
    }

    //increments the segment counter
    public void incrementSegmentCount()
    {
        this.segmentCounter++;
    }

    /******************************************************************
    *SUBMODULE: initialiseSimulation
    *IMPORTS: String routeName
    *EXPORTS: NONE
    *ASSERTION: Initialises the data and observers prior to the commencent
    * of tracking
    ******************************************************************/
    public void initialiseSimulation(String routeName)
    {
        //initialise to start point
        currentSegment = entireRoute.get(0);
        currentWayPoint = currentSegment.getStartPoint();
        latitude = currentWayPoint.getLatitude();
        longitude = currentWayPoint.getLongitude();
        altitude = currentWayPoint.getAltitude();

        reachedEnd = false;
        selectedIndex = 0;
        segmentCounter = 0;

        //provde the observers with the start point data, allowing them to display
        for(int i = 0; i < observerList.size(); i++)
        {
            Observer observer = (Observer)observerList.get(i);
            observer.initialiseData(latitude, longitude, altitude);
        }

        //create user data for the route theStroll
        WayPoint theStroll1 = new WayPoint(-31.95, 115.77, 44.8);
        WayPoint theStroll2 = new WayPoint(-31.93, 115.76, 23.0);
        WayPoint theStroll3 = new WayPoint(-31.93, 115.76, 43.0);
        WayPoint theStroll4 = new WayPoint(-31.93, 115.76, 13.0);
        WayPoint theStroll5 = new WayPoint(-31.94, 115.75, 47.1);

        theStrollData.add(theStroll1);
        theStrollData.add(theStroll2);
        theStrollData.add(theStroll3);
        theStrollData.add(theStroll4);
        theStrollData.add(theStroll5);

        //create user data for the route mainRoute
        WayPoint main1 = new WayPoint(-31.96,115.80,63.0);
        WayPoint main2 = new WayPoint(-31.95,115.78,45.3);
        WayPoint main3 = new WayPoint(-31.95,115.78,5.3);
        WayPoint main4 = new WayPoint(-31.95,115.77,44.8); //the stroll
        WayPoint main5 = new WayPoint(-31.93,115.76,43.0);
        WayPoint main6 = new WayPoint(-31.94,115.75,47.1);
        WayPoint main7 = new WayPoint(-31.95,115.78,25.3);
        WayPoint main8 = new WayPoint(-31.93,115.72,40.1);
        WayPoint main9 = new WayPoint(-31.94,115.75,47.1); //theclimb
        WayPoint main10 = new WayPoint(-31.94,115.75,55.3);
        WayPoint main11 = new WayPoint(-31.94,115.75,71.0);
        WayPoint main12 = new WayPoint(-31.95,115.78,45.3);
        WayPoint main13 = new WayPoint(-31.93,115.75,108.0);
        WayPoint main14 = new WayPoint(-31.93,115.75,131.9);
        WayPoint main15 = new WayPoint(-31.92,115.74,128.1);

        mainRouteData.add(main1);
        mainRouteData.add(main2);
        mainRouteData.add(main3);
        mainRouteData.add(main4);
        mainRouteData.add(main5);
        mainRouteData.add(main6);
        mainRouteData.add(main7);
        mainRouteData.add(main8);
        mainRouteData.add(main9);
        mainRouteData.add(main10);
        mainRouteData.add(main11);
        mainRouteData.add(main12);
        mainRouteData.add(main13);
        mainRouteData.add(main14);
        mainRouteData.add(main15);

        //create userdata for the route theCLimb
        WayPoint climb1 = new WayPoint(-31.94,115.75,47.1);
        WayPoint climb2 = new WayPoint(-31.94,115.75,55.3);
        WayPoint climb3 = new WayPoint(-31.94,115.75,21.0);
        WayPoint climb4 = new WayPoint(-31.94,115.75,71.0);
        WayPoint climb5 = new WayPoint(-31.93,115.75,10.0);
        WayPoint climb6 = new WayPoint(-31.93,115.75,108.0);
        WayPoint climb7 = new WayPoint(-31.93,115.75,40.9);
        WayPoint climb8 = new WayPoint(-31.93,115.75,131.9);

        theClimbData.add(climb1);
        theClimbData.add(climb2);
        theClimbData.add(climb3);
        theClimbData.add(climb4);
        theClimbData.add(climb5);
        theClimbData.add(climb6);
        theClimbData.add(climb7);
        theClimbData.add(climb8);

        //set the userData based on the given route name
        if(routeName.equals("mainRoute"))
        {
            userData = mainRouteData;
        }
        else if(routeName.equals("theClimb"))
        {
            userData = theClimbData;
        }
        else if(routeName.equals("theStroll"))
        {
            userData = theStrollData;
        }

        //adjust the max index to the appropriate size value of the user data
        maxIndex = userData.size();
    }

    /******************************************************************
    *SUBMODULE: syncIndex
    *IMPORTS: WayPoint selectedWayPoint
    *EXPORTS: int syncedIndex
    *ASSERTION: takes in a selected waypoint and finds it in the
    * generated user data. Its used to sync the index value for the user
    * data to make sure still in line with real waypoints from the route list
    ******************************************************************/
    public int syncIndex(WayPoint selectedWayPoint)
    {
        int syncedIndex = 0;
        int count = 0;
        boolean found = false;
        Iterator<WayPoint> iterator = userData.iterator();

        //iterates through the userData till it finds a match
        while(iterator.hasNext() && found != true)
        {
            WayPoint curPoint = iterator.next();
            if(curPoint.isEqual(curPoint, selectedWayPoint))
            {
                found = true;
                //set the index value to the count
                syncedIndex = count;
            }
            count++;
        }

        return syncedIndex;

    }

    /******************************************************************
    *SUBMODULE: simulateUSerMoving
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: loops until end of route reached (notified thorugh boolean)
    * each iteration is retrieves new location values from a set list
    * and notifies these new values to any obsevers
    ******************************************************************/
    public void simulateUserMoving()
    {
        while(reachedEnd != true)
        {
            //update the user location
            changeUserLocation(selectedIndex);
            //if user has hit the end of the route
            if(segmentCounter == entireRoute.size())
            {
                reachedEnd = true;
            }
        }
    }


    /******************************************************************
    *SUBMODULE: changeUserLocation
    *IMPORTS: int i
    *EXPORTS: NONE
    *ASSERTION: based on an index value it delays by 1 secong to provide
    * a simualtino of movement and then retrieves the respective user
    * data to be notified
    ******************************************************************/
    public void changeUserLocation(int i)
    {
        timeDelay(); //delays output for 1second
        locationReceived(userData.get(i).getLatitude(),
                         userData.get(i).getLongitude(),
                         userData.get(i).getAltitude());

         notifyObservers();
    }

    /******************************************************************
    *SUBMODULE: locationReceived
    *IMPORTS: double inLatitude, dobule inLongitude, dobule inAltitude
    *EXPORTS: NONE
    *ASSERTION: takes in three location values and updates current fields
    * then calls notifyObserver method to update all observers to the
    * data
    ******************************************************************/
    public void locationReceived(double inLatitude,
                                 double inLongitude,
                                 double inAltitude)
    {
        this.latitude = inLatitude;
        this.longitude = inLongitude;
        this.altitude = inAltitude;

    }




    /******************************************************************
    *SUBMODULE:removeObserver
    *IMPORTS: Observer observer
    *EXPORTS: NONE
    *ASSERTION: ALlows an observer to be removed from the current
    * observer list
    ******************************************************************/
    public void removeObserver(Observer observer)
    {
        int i = observerList.indexOf(observer);
        if(i >= 0)
        {
            observerList.remove(i);
        }
    }

    /******************************************************************
    *SUBMODULE: notifyObservers
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: iterates through the observer lits and calls update
    * to update the values they're observing on
    ******************************************************************/
    public void notifyObservers()
    {
        for(int i = 0; i < observerList.size(); i++)
        {
            Observer observer = (Observer)observerList.get(i);
            observer.update(latitude, longitude, altitude);
        }
    }


    /******************************************************************
    *SUBMODULE: registerObserver
    *IMPORTS: observer Observer
    *EXPORTS: NONE
    *ASSERTION: allows a valid observer to be added to the current
    * observer lisst. Important for when notifying each observer
    * of a data change
    ******************************************************************/
    public void registerObserver(Observer observer)
    {
        observerList.add(observer);
    }


    /******************************************************************
    *SUBMODULE: timeDelay
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: creates a delay of 1s and pauses the program
    ******************************************************************/
    public void timeDelay()
    {
        int timeDelay = 1000; //delay of 1 second (1000 milliseconds)

        try
        {
            Thread.sleep(timeDelay);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }


}
