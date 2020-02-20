/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Concrete state representing the initial prgoram menu
******************************************************************/

package View;

import Controller.*;
import java.util.*;
import java.io.*;



public class MenuState implements UIState
{
    private UserInterfaceController ui;
    private UserIO uIO;
    private RouteDriver driver;
    private GeoUtils geo;
    private LocationDriver locationDriver;
    private TrackerDriver tracker;
    private UIState trackerState;

    public MenuState()
    {
        this.ui = null;
        this.uIO = null;
        this.geo = null;
        this.locationDriver = null;
        this.tracker = null;
        this.trackerState = null;
    }

    public MenuState(UserInterfaceController ui, UserIO inIO, GeoUtils inGeo, LocationDriver inLD, TrackerDriver tracker, UIState inTrackerState)
    {
        this.ui = ui;
        this.uIO = inIO;
        this.geo = inGeo;
        this.locationDriver = inLD;
        this.tracker = tracker;
        this.trackerState = inTrackerState;
    }

    public void setDriver(RouteDriver inDriver)
    {
        this.driver = inDriver;
    }

    public void setTrackerDriver(TrackerDriver trackerDriver)
    {
        this.tracker = trackerDriver;
    }

    public void setState(UIState trackerState)
    {
        this.trackerState = trackerState;
    }

    public void setLocationDriver(LocationDriver locationDriver)
    {
        this.locationDriver = locationDriver;
    }

    public void setRouteDriver(RouteDriver routeDriver)
    {
        this.driver = routeDriver;
    }

    public void setUIController(UserInterfaceController ui)
    {
        this.ui = ui;
    }

    public void setGeoUtils(GeoUtils geo)
    {
        this.geo = geo;
    }

    public void setUserIO(UserIO uIO)
    {
        this.uIO  = uIO;
    }

    /******************************************************************
    *SUBMODULE: displayData
    *IMPORTS: UserInterfaceController
    *EXPORTS: int
    *ASSERTION: Provides menu functionality for the intial part of the program.
    * allows user to display a more detailed summary of a route or enter tracker mode.
    * also provides the option to reload the route data
    ******************************************************************/
    public int displayData(UserInterfaceController inUI)
    {

        Scanner sc = new Scanner(System.in);
        int selection = -1;
        String routeName = "";

        this.ui = inUI;

        driver.setGeoUtils(geo);
        selection = driver.loadData();


        while(selection != 0 && selection != 2)
        {
            System.out.println(driver.getRouteSummary());

            System.out.println("\n\n\n_____________________________________________________________________");
            String mainOptions = "Please select one of the following options:"
                                        +"\n(1)Display Route Data"
                                        + "\n(2)Enter Tracking Mode"
                                        + "\n(3)Load Location Data"
                                        + "\n(0)Quit Program";
            System.out.println(mainOptions);
            System.out.println("_____________________________________________________________________");
            selection = uIO.validInput(0,3);


            if(selection == 1)
            {
                //gets the chosen route name
                routeName = retrieveRouteName();
                System.out.println(driver.getRouteDetails(routeName));

            }
            else if(selection == 2)
            {
                //updates the trackerstate instance
                trackerState.setUserIO(uIO);
                trackerState.setUIController(ui);
                trackerState.setLocationDriver(locationDriver);
                trackerState.setTrackerDriver(tracker);
                trackerState.setRouteDriver(driver);
                trackerState.setState(this);
                trackerState.setGeoUtils(geo);
                //updates the current state instnace to the tracker state
                ui.setUIState(trackerState);
            }
            else if(selection == 3)
            {
                //reloads the reoute data
                selection = driver.loadData();
                System.out.println(driver.getRouteSummary());
            }
            else if(selection == 0)
            {
                System.out.println("\nYou have exited the program :)\n");
            }
            else
            {
                System.out.println("invalid entry");
            }

        }

        return selection;
    }

    /******************************************************************
    *SUBMODULE: outputString
    *IMPORTS:String
    *EXPORTS:void
    *ASSERTION:displays a imported string to teh user
    ******************************************************************/
    public void outputString(String inString)
    {
        System.out.println(inString);
    }



    /******************************************************************
    *SUBMODULE: retrieveRouteName
    *IMPORTS:NONE
    *EXPORTS:String
    *ASSERTION: displays routenames to the screen and retrieves a valid`
    * route name selection
    ******************************************************************/
    public String retrieveRouteName()
    {
        String routeName = "";
        Scanner sc = new Scanner(System.in);
        System.out.println(driver.getRouteNames());
        System.out.println("\nEnter a route name: ");
        routeName = sc.nextLine();

        while(driver.validateRouteName(routeName) != true)
        {
            System.out.println("\nInvalid name, Enter a route name: ");
            routeName = sc.nextLine();
        }

        return routeName;
    }
}
