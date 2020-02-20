/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Provided abstract class
******************************************************************/
package Controller;

public abstract class GpsLocator
{
    protected abstract void locationReceived(double latitude,
                                             double longitude,
                                             double altitude);
}
