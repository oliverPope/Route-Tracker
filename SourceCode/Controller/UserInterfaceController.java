/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Controller class to run the state pattern
******************************************************************/

package Controller;

import View.*;
import java.util.*;

public class UserInterfaceController
{
    UIState menuUI;
    UIState trackerUI;
    UIState currState;

    RouteDriver driver;

    public UserInterfaceController(RouteDriver inDriver)
    {
        driver = inDriver;
    }

    public void setMenu(UIState inMenu)
    {
        menuUI = inMenu;
    }

    public void setTracker(UIState inTracker)
    {
        trackerUI = inTracker;
    }


    /******************************************************************
    *SUBMODULE: displayData
    *IMPORTS: NONE
    *EXPORTS: NONE
    *ASSERTION: loops the states until the user prompts to exit the program
    ******************************************************************/
    public void displayData()
    {
        int terminate = -1;
        while(terminate != 0)
        {
            //sets the Route Driver to the current state to maintain data
            currState.setDriver(driver);
            //displays the data on the current state and retrives a value
            terminate = currState.displayData(this);
        }
    }

    /******************************************************************
    *SUBMODULE: outputString
    *IMPORTS: String inString
    *EXPORTS: NONE
    *ASSERTION: takes in a string and passes it to the current state to display
    ******************************************************************/
    public void outputString(String inString)
    {
        currState.outputString(inString);
    }

    public void setUIState(UIState inState)
    {
        this.currState = inState;
    }

    public UIState getTrackingState()
    {
        return trackerUI;
    }

    public UIState getMenuState()
    {
        return menuUI;
    }

}
