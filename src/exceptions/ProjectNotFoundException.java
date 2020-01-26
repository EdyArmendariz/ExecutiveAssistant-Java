package exceptions;

/**
 * A runtime exception that is thrown when a Screen requests information that doesn't exist.
 */
public class ProjectNotFoundException
        extends RuntimeException
{
    /**
     * Construct a runtime exception that is thrown when a Screen requests information that doesn't exist. 
     */
    public ProjectNotFoundException()
    {
        super();
    }
}
