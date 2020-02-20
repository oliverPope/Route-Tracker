/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Factory for the route data, determins if a line is a
route, waypoint or segment object and returns
******************************************************************/
package Controller;

import Model.*;
import java.util.*;
import java.io.*;

public class RouteDataFactory
{
    /******************************************************************
    *SUBMODULE: makeRoute
    *IMPORTS: String line, ROuteCOmponent inROute in Segment, WayPoint
    *EXPORTS: Object
    *ASSERTION: takes in a line from the route driver and determines
    * its type, storing its information then returns
    ******************************************************************/
    public static Object makeRoute(String line,
                                   RouteComponent inRoute,
                                   RouteComponent inSegment,
                                   WayPoint inWayPoint)throws InvalidFileFormatException
    {
        Object lineType = null;
        String wayPoint[], routeDetails[] = null;
        String routeName = "", routeDescription = "",
               segmentDescription = "", subRoute = "";

        String [] routeLine;

        //initial split on empty space for determining if a route line
        routeLine = line.split(" ");
        //checks the first element of the resulting split
        if(checkFirstLine(routeLine[0]))
        {
            //if its validated as an initial route line then store the data
            routeName =  routeLine[0];
            //the remaining elements of the line array make up the description
            for(int j = 1; j < routeLine.length; j++)
            {
                routeDescription += routeLine[j] + " ";
            }
            //store the name as already validated
            inRoute.setName(routeName);
            //checks the descritpion is valid before storing
            if(validateDescription(routeDescription))
            {
                inRoute.setDescription(routeDescription);
                //sets the object to a route object
                lineType = inRoute;
            }
            else
            {
                throw new InvalidFileFormatException("Invalid Description");
            }

        }
        else
        {
             //rebuilds the line to undo the line split on " "
             line = reverseSplit(routeLine);
             //splits on a comma maintaing the description if its empty
             wayPoint = line.split(",",-1);
             //if the lenght is greater than three than its a waypoint coordinate
             //as well as a segment description
             if(wayPoint.length > 3)
             {
                 /*not the end waypoint so will contain lat, long, altitude
                 and either a subroute name or a segment description*/
                 inWayPoint = setWayPointValues(wayPoint[0], wayPoint[1], wayPoint[2], inWayPoint);
                 //handles if the description contained a comma
                 String desc = "";
                 //builds the description with whats left
                 for(int j = 3; j < wayPoint.length; j++)
                 {
                     desc += wayPoint[j];
                 }
                 //stores the starting waypoint
                 inSegment.setStart(inWayPoint);
                 //validates the sgement description before storing
                 if(validateSegDescription(desc))
                 {
                     inSegment.setDescription(desc);
                     lineType = inSegment;
                 }
                 else
                 {
                     throw new InvalidFileFormatException("Invalid Segment Description" + desc);
                 }

             }
             else if(wayPoint.length == 3)
             {
                 //final waypoint i.e no subroute or segment description
                 //creates a new waypoint object that represents the final waypoint
                 //in the route
                 inWayPoint = setWayPointValues(wayPoint[0], wayPoint[1], wayPoint[2], inWayPoint);
                 lineType = inWayPoint;
             }
             else
             {
                 throw new InvalidFileFormatException("Invalid Route Name");
             }
        }

        return lineType;
    }

    /******************************************************************
    *SUBMODULE: setWayPointValues
    *IMPORTS:
    *EXPORTS:
    *ASSERTION: validates the waypoint values before storing into a waypoint
    ******************************************************************/
    public static WayPoint setWayPointValues(String inLat, String inLong, String inAlt, WayPoint inWayPoint)throws InvalidFileFormatException
    {
        double latitude, longitude, altitude;

        try
        {
            latitude = Double.parseDouble(inLat);
            longitude = Double.parseDouble(inLong);
            altitude = Double.parseDouble(inAlt);
            //if not a vlaide double then exception is thrown and handled
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("Invalid coordinate values", e);
        }

        //ensures the latitude and longitues values are within the bounds
        if(validateCoordinate(latitude, 90))
        {
            inWayPoint.setLatitude(latitude);
            if(validateCoordinate(longitude, 180))
            {
                inWayPoint.setLongitude(longitude);
            }
            else
            {
                throw new InvalidFileFormatException("Invalid Longitude");
            }
        }
        else
        {
            throw new InvalidFileFormatException("Invalid Latitude");
        }

        inWayPoint.setAltitude(altitude);

        return inWayPoint;
    }

    /******************************************************************
    *SUBMODULE: validateCoordinate
    *IMPORTS: double latLong, double posRange
    *EXPORTS: boolean
    *ASSERTION: common method for comparing the latitude and longitude to the
    * set bounds
    ******************************************************************/
    public static boolean validateCoordinate(double latlong, double posRange)
    {
        boolean valid = false;
        int compareVal1 = 0;
        int compareVal2 = 0;

        double negRange = posRange*-1;

        compareVal1 = Double.compare(latlong, negRange);

        if(compareVal1 > 0)//lattitude value greater than -90.0
        {
            compareVal2 = Double.compare(latlong, posRange);
            if(compareVal2 < 0)//lattitude value less than 90
            {
                valid = true;
            }
        }
        return valid;
    }


    /******************************************************************
    *SUBMODULE: checkFirstLine
    *IMPORTS: String line
    *EXPORTS: boolean
    *ASSERTION: further breaks up the line in order to check if the first line
    * of a route block
    ******************************************************************/
    public static boolean checkFirstLine(String line) throws InvalidFileFormatException
    {
        boolean valid = false;
        String [] lineArray = line.split(",");
        if(lineArray.length == 1)
        {
            if(validateRouteName(line))
            {
                valid = true;
            }
            else
            {
                throw new InvalidFileFormatException("Invalid Route Name");
            }
        }
        return valid;
    }

    /******************************************************************
    *SUBMODULE: validateRouteName
    *IMPORTS: String routeName
    *EXPORTS: boolean
    *ASSERTION: determines if the route name is only made up of letters,
    digits and underscore
    ******************************************************************/
    public static boolean validateRouteName(String routeName)
    {
        boolean valid = false;
        if(routeName.matches("^[a-z-A-Z-0-9_]+"))
        {
            valid = true;
        }
        return valid;
    }

    /******************************************************************
    *SUBMODULE: validateDescription
    *IMPORTS: string descritpion
    *EXPORTS: boolean
    *ASSERTION: checks the description doesnt contain new line characters
    ******************************************************************/
    public static boolean validateDescription(String description)
    {
        boolean valid = false;
        if(description.matches("^[\n\r]+") != true)
        {
            if(description.isEmpty()!= true)
            {
                valid = true;
            }
        }

        return valid;
    }

    /******************************************************************
    *SUBMODULE: validateSegDescription
    *IMPORTS: string description
    *EXPORTS: boolean
    *ASSERTION: if the description is a subroute name, it validates the name
    * and if not it checks it doesnt contain new line character
    ******************************************************************/
    public static boolean validateSegDescription(String description)
    {
        boolean valid = false;
        if(description.charAt(0) == '*')
        {
            if(validateRouteName(description.substring(1)))
            {
                valid = true;
            }
        }
        else
        {
            if(description.matches("^[\n\r]+") != true)
            {
                valid = true;

            }
        }
        return valid;
    }

    /******************************************************************
    *SUBMODULE: reverseSplit
    *IMPORTS: String [] routeList
    *EXPORTS: string
    *ASSERTION: rebuilds the string from the split array
    ******************************************************************/
    public static String reverseSplit(String [] routeLine)
    {
        String originalLine = "";
        originalLine = routeLine[0];
        for(int i = 1; i < routeLine.length; i++)
        {
            originalLine += " " + routeLine[i];
        }
        return originalLine;
    }
}
