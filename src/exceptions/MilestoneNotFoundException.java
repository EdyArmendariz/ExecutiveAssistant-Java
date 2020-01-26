package exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: Feb 27, 2007
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MilestoneNotFoundException
        extends java.lang.Exception
{
    private String defaultMessage = "MilestoneNotFoundException";
    private int errorNumber = -1;
    private String strAppend = new String( "");
    private String message = new String(defaultMessage );

    public MilestoneNotFoundException()
    {

    }

    public String getMessage()
    {
        return message;
    }

    public int getErrorNumber()
    {
        return errorNumber;
    }

    public void appendToMessage( String s )
    {
        message += ": " + s;
    }

    public void resetMessage()
    {
        message = defaultMessage;
    }
}
