/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Provided class
******************************************************************/

package Controller;
import java.io.*;

public class GeoUtils
{
    /******************************************************************
    *SUBMODULE: calcMetresDistance
    *IMPORTS: double lat1, double lat2, double long 1, double long2
    *EXPORTS: double distance
    *ASSERTION: Takes in the horizontal coordinates of two points
    * and axxporimates the distance as per the assignment specification
    ******************************************************************/
    public double calcMetresDistance(double lat1, double long1,
                                     double lat2, double long2){

        double distance, x;

        x = Math.sin((Math.PI*lat1)/180)*Math.sin((Math.PI*lat2)/180)
            + Math.cos((Math.PI*lat1)/180)*Math.cos((Math.PI*lat2)/180)
            * Math.cos((Math.PI*(long1-long2))/180);
        distance = 6371000*Math.acos(x);

        return distance;
   }

   /******************************************************************
   *SUBMODULE: retrieveRouteData
   *IMPORTS: NONE
   *EXPORTS: String routeData
   *ASSERTION: Simulates the method connecting and retreiving the latest
   * version of the route data. Simulates by building a formatted string
   * representing some valid route data and returns.
   ******************************************************************/
   public String retrieveRouteData()throws IOException
   {
        String routeData = "";

        routeData ="theClimb Amazing views!\n"
         + "  -31.94,115.75,47.1,Easy start\n"
         + "  -31.94,115.75,55.3,Tricky, watch for drop bears.\n"
         + "  -31.94,115.75,71.0,I*feel,like.over-punctuating!@#$%^&*()[]{}<>.?_+\n"
         + "  -31.93,115.75,108.0,Getting there\n"
         + "           -31.93,115.75,131.9\n\n"
         + "       \n"
        +"mainRoute Since I was young\n"
          +"  -31.96,115.80,63.0,I knew\n"
         + "  -31.95,115.78,45.3,I'd find you\n"
          +"  -31.95,115.77,44.8,*theStroll\n"
         + "  -31.94,115.75,47.1,But our love\n"
          +"  -31.93,115.72,40.1,Was a song\n"
         + "  -31.94,115.75,47.1,*theClimb\n"
         + "  -31.93,115.75,131.9,Sung by a dying swan\n"
         + "  -31.92,115.74,128.1\n\n"
        +  "theStroll Breathe in the light\n"
         + "  -31.95,115.77,44.8,I'll stay here\n"
         + "  -31.93,115.76,43.0,In the shadow\n"
         + "  -31.94,115.75,47.1\n\n";

         if(routeData == null || routeData.equals(""))
         {
             throw new IOException("Failed to obtain route data");
         }

         return routeData;
   }
}
