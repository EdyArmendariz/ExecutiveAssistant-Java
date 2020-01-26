package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

/**
 * Opens the execasst.dat file
 */
public class OpenDat
{
    //private String DEFAULTPATH = "C:\\Program Files\\Palm\\ArmendE\\ExecAsst";
    private String DEFAULTPATH = "C:\\Program Files\\Palm\\ArmendE\\datebook";
    // private String DEFAULTFILE = "execasst2.dat";
    private String DEFAULTFILE = "datebook2.dat";
    String strContents = new String();

    /**
     *
     */
    public OpenDat()
    {

    }

    /**
     *
     * @return
     */
    public Object load()
    {
        Object obj = new Object();

        try
        {
            File f = new File(DEFAULTPATH + "\\" + DEFAULTFILE);
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataInputStream dis = new DataInputStream( fis );
            Data dat = new Data( dis, true );
            // StreamUtil.dumpBytes( dis, 9999 );

            byte[] bReadByte = new byte[1];
            int iReadByte = -1;
            iReadByte = fis.read(bReadByte);

            while (iReadByte != -1)
            {
                baos.write(bReadByte, 0, iReadByte);
                iReadByte = fis.read(bReadByte);
            }

           strContents = baos.toString();
        }
        catch (java.io.FileNotFoundException fnfe)
        {
             System.out.println( fnfe.getMessage() );
        }
        catch (java.io.IOException ioe)
        {
            System.out.println( ioe.getMessage() );
        }
        catch (Exception e )
        {
            System.out.println( e.getMessage() );
        }


        return obj;
    }

    public String getString()
    {
        return strContents;
    }
}
