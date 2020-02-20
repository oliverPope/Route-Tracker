/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Main class for the program. Creates all objects recquried
* for the program and initiliases respective objects. This allows for
* dependency injection and better testability.
******************************************************************/
import Controller.*;
import Model.*;
import View.*;

import java.util.*;
import java.io.*;

public class Main
{

    public static void main(String [] args)
    {
        RouteDriver driver = new RouteDriver();
        GeoUtils geo = new GeoUtils();
        UserIO inIO = new UserIO();
        TrackerDriver tracker = new TrackerDriver();
        LocationDriver lDriver = new LocationDriver(tracker);
        UserInterfaceController ui = new UserInterfaceController(driver);

        UIState inMenu = new MenuState(ui, inIO, geo, lDriver, tracker, new TrackerState());
        UIState inTracker = new TrackerState(ui, inIO, geo, lDriver, tracker, new MenuState());

        ui.setMenu(inMenu);
        ui.setTracker(inTracker);
        //initiliases the state to the menustate
        ui.setUIState(inMenu);
        //runs the program
        ui.displayData();
    }
}
