
/**
 * 
 */

import gui.EADesktop;
import data.DataModel;
import io.Load;
import io.Save;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import events.DataChangeListener;
import events.DataChangeEvent;
import com.developer.data.devDataHelper;

public class ExecAssistDesktop
{
    static public DataModel dm = new DataModel();
    final static public devDataHelper dataHelper = devDataHelper.getDataHelper();

    /**
     *
     */
    private static void createAndShowGUI()
    {
        final Save save = new Save();
        final Load load = new Load();
        dm = devDataHelper.getDataHelper().getDataModel();
        dm = (DataModel) load.load();
        EADesktop ead = new EADesktop();
        ead.setVisible(true);

        ead.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                save.objectToSave(dm);
                save.save();
                System.exit(0);
            }
        });
    }

    /**
     * @param s
     */
    public static void main(String s [])
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });

    }
}
