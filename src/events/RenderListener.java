package events;

/**
 * Created by IntelliJ IDEA.
 * User: Edy
 * Date: May 7, 2007
 * Time: 10:51:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RenderListener
        extends SimpleEventListener
{
    public static final int STUFF = 0;
    public void reRender();
}
