package events;

import data.DataBean;

/**
 * 
 */
public interface ScreenChangeListener
        extends SimpleEventListener
{
    final public static String MAIN = "MAIN";
    final public static String EMPLOYEE = "EMPLOYEE";
    final public static String EMPLOYEE_LIST = "EMPLOYEE_LIST";
    final public static String PROJECT_LIST = "PROJECT_LIST";
    final public static String PROJECT_MENU = "PROJECT_MENU";
    final public static String PROJECT_EMPLOYEE = "PROJECT_EMPLOYEE";
    final public static String PROJECT_EMPLOYEES_LIST = "PROJECT_EMPLOYEES_LIST";
    final public static String MILESTONES_GRAPH = "MILESTONES_GRAPH";


    public static final int NONE = -1;
    public static final int SHOW_MAIN = 1;
    public static final int SHOW_EMPLOYEE_LIST = 2;
    public static final int SHOW_PROJECT_LIST = 3;
    public static final int SHOW_PROJECT_MENU = 4;
    public static final int SHOW_PROJECT_EMPLOYEES_LIST = 5;
    public static final int SHOW_MILESTONES_GRAPH = 6;
    public static final int SHOW_PROJECT_EMPLOYEE = 7;
    public static final int SHOW_EDIT_MILESTONE = 8;
    public static final int SHOW_EMPLOYEE = 9;
    public static final int SHOW_NEW_EMPLOYEE = 10;
    public static final int SHOW_MILESTONE = 11;


    public static final int SHOW_PREVIOUS = 12;



    /**
     *
     * @param screenID
     * @param sce
     */
    boolean goToScreen( int screenID , ScreenChangeEvent sce );

}
