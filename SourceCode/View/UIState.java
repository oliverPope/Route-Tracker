/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Interface for the concrete states
******************************************************************/
package View;

import Controller.*;

public interface UIState
{
    public int displayData(UserInterfaceController inUI);
    public void setDriver(RouteDriver inDriver);

    public void outputString(String inString);

    public void setTrackerDriver(TrackerDriver trackerDriver);
    public void setState(UIState trackerState);

    public void setLocationDriver(LocationDriver locationDriver);

    public void setRouteDriver(RouteDriver routeDriver);

    public void setUIController(UserInterfaceController ui);

    public void setGeoUtils(GeoUtils geo);

    public void setUserIO(UserIO uIO);
}
