/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Validates the user input for int selection
******************************************************************/

package Controller;

import java.util.*;
import java.io.*;
public class UserIO
{
    /******************************************************************
    *SUBMODULE: validateInput
    *IMPORTS: lower (integer), upper(integer)
    *EXPORTS: conv (integer)
    *ASSERTION: takes in the upper and lower bounds and returns a
    *           validated input
    ******************************************************************/
    public int validInput(int lower, int upper)
    {
        Scanner sc = new Scanner(System.in);
        boolean valid = false;
        String choice = null;
         int conv = 0;

        if(lower > upper) //if the lower value is greater than the upper then switch the values
        {
            int temp = lower;
            lower = upper;
            upper = temp;
        }
        while(!valid) //will loop until user inputs a valid input
        {
            try
            {
                choice = sc.nextLine();
                conv = Integer.parseInt(choice);
                if(conv <= upper && conv >= lower) //validates input againts number range
                {
                    valid = true;
                }
                else
                {
                    throw new IllegalArgumentException("Invalid entry, please select again");
                }
            }
            catch(InputMismatchException | NumberFormatException e)
            {
                System.out.println("Invalid entry, please select again");
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Invalid entry, please select again");
            }
        }
        return conv;
    }

}
