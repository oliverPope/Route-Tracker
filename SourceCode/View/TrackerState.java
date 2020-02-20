/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Concrete state class representing the tracker menu
******************************************************************/
package View;

import Controller.*;
import java.util.*;

public class TrackerState implements UIState
{
    UserInterfaceController ui;
    private UserIO uIO;
    private RouteDriver driver;
    private GeoUtils geo;
    private LocationDriver locationDriver;
    private TrackerDriver tracker;
    private UIState menuState;

    public TrackerState()
    {
        this.ui = null;
        this.uIO = null;
        this.geo = null;
        this.locationDriver = null;
        this.tracker = null;
        this.menuState = null;
    }

    public TrackerState(UserInterfaceController ui, UserIO inIO, GeoUtils inGeo, LocationDriver inLD, TrackerDriver inTracker, UIState inMenu)
    {
        this.ui = ui;
        this.uIO = inIO;
        this.geo = inGeo;
        this.locationDriver = inLD;
        this.tracker = inTracker;
        this.menuState = inMenu;
    }

    public void setDriver(RouteDriver inDriver)
    {
        this.driver = inDriver;
    }

    public void setTrackerDriver(TrackerDriver trackerDriver)
    {
        this.tracker = trackerDriver;
    }

    public void setState(UIState menuState)
    {
        this.menuState = menuState;
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
    *ASSERTION: Provides menu functionality for the tracking part of the
    *program. Allows user to select a route to track or quit back to the
    * previous menu
    ******************************************************************/
    public int displayData(UserInterfaceController inUI)
    {
        //updates the ui controller
        this.ui = inUI;
        System.out.println("entered tracking mode");
        int selection = -1;
        String routeName = "";


        while(selection != 2)
        {
            System.out.println("\n\n\n_____________________________________________________________________");
            System.out.println("Please select one of the following options:");
            String trackerOptions =      "(1)Select Route and Go"
                                     + "\n(2)Quit";
            System.out.println(trackerOptions);
            System.out.println("_____________________________________________________________________");
            selection = uIO.validInput(1,2);

            if(selection == 1)
            {
                //gets the route name
                routeName = retrieveRouteName();
                System.out.println("\n");
                //updates the value within the tracker instance
                tracker.setLocationDriver(locationDriver);
                tracker.setRouteMap(driver.getRouteMap());
                tracker.setUserIO(uIO);
                tracker.setGeoUtils(geo);
                tracker.setUIController(ui);
                //runs the tracking method
                tracker.go(routeName);
            }
            else if(selection == 2)
            {

                //update the menuState instance
                menuState.setUserIO(uIO);
                menuState.setUIController(ui);
                menuState.setLocationDriver(locationDriver);
                menuState.setTrackerDriver(tracker);
                menuState.setRouteDriver(driver);
                menuState.setState(this);
                menuState.setGeoUtils(geo);
                //set the ui current state to the menustate
                ui.setUIState(menuState);
                System.out.println("quit tracking mode");
            }
            else
            {
                System.out.println("Invalid entry");
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
    public void outputString(String input)
    {
        System.out.println(input);
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
