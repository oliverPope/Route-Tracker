/******************************************************************
*Name: Oliver Pope 18344822
*Completion Date: 21/5/2019
*ASSERTION: Exception class used to generalise any high level
* exceptions that may be thrown during the validation of route data
******************************************************************/
package Controller;

public class InvalidFileFormatException extends Exception
{
    /******************************************************************
    *Represents an exception of both the message and throwable cause
    ******************************************************************/
    public InvalidFileFormatException(String exceptionMessage, Throwable cause)
    {
        super(exceptionMessage, cause);
    }

    /******************************************************************
    *represents an exception with just the message and not throwable
    ******************************************************************/
    public InvalidFileFormatException(String exceptionMessage)
    {
        super(exceptionMessage);
    }
}
