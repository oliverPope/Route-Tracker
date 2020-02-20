/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Abstract class of the route and segment class,
* as part of the composite pattern
******************************************************************/

package Model;



import java.util.ArrayList;
public abstract class RouteComponent
{
    /*each method throws an UnsupportedOperationException so that in the future
    if another class is involved and doesnt implement such methods then the program
    will be notified*/
    public void setCompleteRoute(ArrayList<RouteComponent> list)
    {
        throw new UnsupportedOperationException();
    }

    public ArrayList<RouteComponent> getCompleteRoute()
    {
        throw new UnsupportedOperationException();
    }
    public String getName()
    {
        throw new UnsupportedOperationException();
    }

    public String getDescription()
    {
        throw new UnsupportedOperationException();
    }

    public WayPoint getStartPoint()
    {
        throw new UnsupportedOperationException();
    }

    public WayPoint getEndPoint()
    {
        throw new UnsupportedOperationException();
    }

    public void setStart(WayPoint inPoint)
    {
        throw new UnsupportedOperationException();
    }

    public void setEnd(WayPoint inPoint)
    {
        throw new UnsupportedOperationException();
    }

    public void setName(String inName)
    {
        throw new UnsupportedOperationException();
    }

    public void setDescription(String inDescription)
    {
        throw new UnsupportedOperationException();
    }

    public void add(RouteComponent routeComponent)
    {
        throw new UnsupportedOperationException();
    }

    public ArrayList<RouteComponent> getRouteList()
    {
        throw new UnsupportedOperationException();
    }

    public String routeSummary()
    {
        throw new UnsupportedOperationException();
    }

    public String printAllData()
    {
        throw new UnsupportedOperationException();
    }

    public void setHorizontalDistance(double indistance)
    {
        throw new UnsupportedOperationException();
    }

    public void setClimbingDistance(double indistance)
    {
        throw new UnsupportedOperationException();
    }

    public void setDecentDistance(double indistance)
    {
        throw new UnsupportedOperationException();
    }

    public double getHorizontalDistance()
    {
        throw new UnsupportedOperationException();
    }

    public double getClimbDistance()
    {
        throw new UnsupportedOperationException();
    }

    public double getDescentDistance()
    {
        throw new UnsupportedOperationException();
    }
}
