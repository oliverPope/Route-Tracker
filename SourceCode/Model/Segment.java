/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Container class for a Segment
******************************************************************/
package Model;

public class Segment extends RouteComponent
{
    private String description;
    private WayPoint startWayPoint;
    private WayPoint endWayPoint;

    private double segmentDistance;


    public Segment()
    {
        this.description = "";

    }

    public Segment(String inDescription, WayPoint inStartPoint)
    {
        this.description = inDescription;
        this.startWayPoint = inStartPoint;
        this.endWayPoint = null;
    }

    public void setHorizontalDistance(double inDistance)
    {
        this.segmentDistance = inDistance;
    }

    public String getDescription()
    {
        return description;
    }

    public WayPoint getStartPoint()
    {
        return startWayPoint;
    }

    public WayPoint getEndPoint()
    {
        return endWayPoint;
    }

    public void setStart(WayPoint inPoint)
    {
        this.startWayPoint = inPoint;
    }

    public void setEnd(WayPoint inPoint)
    {
        this.endWayPoint = inPoint;
    }

    public void setDescription(String inDesc)
    {
        description = inDesc;
    }

    /******************************************************************
    *SUBMODULE: printAllData
    *IMPORTS: NONE
    *EXPORTS: String
    *ASSERTION: builds a string of the start point and description
    ******************************************************************/
    public String printAllData()
    {
        String str = "";

        str = "Waypoint: " + startWayPoint.toString()
              + "\n    Description: " + description;

        return str;
    }



}
