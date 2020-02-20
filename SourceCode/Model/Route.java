/******************************************************************
*Name:Oliver Pope 18344822
*Completion Date: 22/5/2019
*ASSERTION: Container class for a route
******************************************************************/

package Model;

import java.util.*;
public class Route extends RouteComponent
{
    private String name;
    private String description;
    private WayPoint startWayPoint;
    private WayPoint endWayPoint;
    private ArrayList<RouteComponent> componentList;
    private ArrayList<RouteComponent> mergedList;

    private double totalHDistance;
    private double climbingDistance;
    private double decentDistance;

    public Route()
    {
        this.name = "";
        this.description = "";
        this.startWayPoint = null;
        this.endWayPoint = null;

        componentList = new ArrayList<RouteComponent>();
        mergedList = new ArrayList<RouteComponent>();
    }


    public Route(String inName, String inDescription)
    {
        this.name = inName;
        this.description = inDescription;
        this.startWayPoint = null;
        this.endWayPoint = null;
        componentList = new ArrayList<RouteComponent>();
        mergedList = new ArrayList<RouteComponent>();

    }

    public ArrayList<RouteComponent> getCompleteRoute()
    {
        return mergedList;
    }

    public void setCompleteRoute(ArrayList<RouteComponent> mergedList)
    {
        this.mergedList = mergedList;
    }

    /******************************************************************
    *SUBMODULE: add
    *IMPORTS: RouteComponent
    *EXPORTS: NONE
    *ASSERTION: adds a routecomponent to the routes list
    ******************************************************************/
    public void add(RouteComponent routeComponent)
    {
        componentList.add(routeComponent);
    }

    public void setHorizontalDistance(double indistance)
    {
        this.totalHDistance = indistance;
    }

    public void setClimbingDistance(double indistance)
    {
        this.climbingDistance = indistance;
    }

    public void setDecentDistance(double indistance)
    {
        this.decentDistance = indistance;
    }

    public double getHorizontalDistance()
    {
        return totalHDistance;
    }

    public double getClimbDistance()
    {
        return climbingDistance;
    }

    public double getDescentDistance()
    {
        return decentDistance;
    }

    public String getName()
    {
        return name;
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

    public ArrayList<RouteComponent> getRouteList()
    {
        return componentList;
    }

    public void setStart(WayPoint inPoint)
    {
        this.startWayPoint = inPoint;
    }

    public void setEnd(WayPoint inPoint)
    {
        this.endWayPoint = inPoint;
    }

    public void setName(String inName)
    {
        name = inName;
    }

    public void setDescription(String inDesc)
    {
        description = inDesc;
    }

    /******************************************************************
    *SUBMODULE: routeSummary
    *IMPORTS: NONE
    *EXPORTS: String
    *ASSERTION: builds a summary of the route and returns
    ******************************************************************/
    public String routeSummary()
    {
        String str = "\n--------------------------------------------------";

        str += "\nRoute Name: " + name;
        str += "\n     Start Point: " + startWayPoint.toString();
        str += "\n     End Point : " + endWayPoint.toString();
        str += "\n";
        return str;
    }

    /******************************************************************
    *SUBMODULE: printAllData
    *IMPORTS: NONE
    *EXPORTS: String
    *ASSERTION: builds a summary of all waypoints and descriptions
    ******************************************************************/
    public String printAllData()
    {
        String str = routeSummary();

        for(RouteComponent rc: componentList)
        {
            str += rc.printAllData();
        }
        str += "\nWayPoint: " + endWayPoint.toString();

        return str;
    }
}
