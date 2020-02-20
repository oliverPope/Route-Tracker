/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Container class for a WayPoint. Coordinate values as
* well as running distance totals for horizontal, climbing and descent
******************************************************************/
package Model;

public class WayPoint
{
    private double latitude;
    private double longitude;
    private double altitude;

    private double hDistanceFromStart;
    private double climbFromStart;
    private double decentFromStart;

    public WayPoint()
    {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0;
    }

    public WayPoint(double inLatitude, double inLongitude, double inAltitude)
    {
        this.latitude = inLatitude;
        this.longitude = inLongitude;
        this.altitude = inAltitude;
    }

    public void setDistancetFromStart(double indistance)
    {
        this.hDistanceFromStart = indistance;
    }

    public void setClimbFromStart(double indistance)
    {
        this.climbFromStart = indistance;
    }


    public void setDecentFromStart(double indistance)
    {
        this.decentFromStart = indistance;
    }

    public double getHDistanceFromStart()
    {
        return hDistanceFromStart;
    }

    public double getClimbFromStart()
    {
        return climbFromStart;
    }

    public double getDecentFromStart()
    {
        return decentFromStart;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public void setLatitude(double inLat)
    {
        latitude = inLat;
    }

    public void setLongitude(double inLong)
    {
        longitude = inLong;
    }

    public void setAltitude(double inAlt)
    {
        altitude = inAlt;
    }

    public String toString()
    {
        String str = Double.toString(latitude) + " "
                    + Double.toString(longitude) + " "
                    + Double.toString(altitude) + " ";

        return str;
    }

    /******************************************************************
    *SUBMODULE:isEqual
    *IMPORTS:
    *EXPORTS:
    *ASSERTION: Takes two waypoints and compares their values
    ******************************************************************/
    public boolean isEqual(WayPoint w1, WayPoint w2)
    {
        int compareVal1 = 1;
        int compareVal2 = 1;
        int compareVal3 = 1;

        boolean equal = false;

        compareVal1 = Double.compare(w1.getLatitude(), w2.getLatitude());
        if(compareVal1 == 0)
        {
            compareVal2 = Double.compare(w1.getLongitude(), w2.getLongitude());
            if(compareVal2 == 0)
            {
                compareVal3 = Double.compare(w1.getAltitude(), w2.getAltitude());
                if(compareVal3 == 0)
                {
                    equal = true;

                }
            }
        }

        return equal;
    }
}
