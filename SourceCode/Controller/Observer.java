/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Observer interface
******************************************************************/
package Controller;

import Model.WayPoint;
public interface Observer
{
    public void update(double longitude, double latitude, double altitude);
    public void initialiseData(double longitude, double latitude, double altitude);
}
