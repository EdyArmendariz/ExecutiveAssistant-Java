package io;

import java.io.IOException;
import java.io.DataInputStream;
import java.util.Date;

// ---------------------------------------------------------------------------
// RepeatEvent
// ---------------------------------------------------------------------------
final class RepeatEvent
{
    public static final int DAILY = 1;
    public static final int WEEKLY = 2;
    public static final int MONTHLYBYDAY = 3;
    public static final int MONTHLYBYDATE = 4;
    public static final int YEARLYBYDATE = 5;
    public static final int YEARLYBYDAY = 6; // Never used

    private boolean m_verbose;

    private short m_dateException;
    private int m_exceptionEntry;
    private short m_repeatEventFlag;
    private final ClassEntry m_classEntry;
    private int m_brand;
    private int m_interval;
    private long m_endDate;
    private int m_firstDayWk;
    private BrandData m_brandData;

    RepeatEvent(DataInputStream is, boolean verbose)
        throws IOException
    {
        m_verbose = verbose;
        print("\n** repeat event **");

        m_dateException = StreamUtil.readShort(is);
        print("dateException: " +  m_dateException);

        if (m_dateException != 0)
        {
            print("\n** Found date exceptions, index is " + m_dateException);
            for (int i = 0; i < m_dateException; i++)
            {
                /**
                 * This was unclear in the decode document.
                 * Followed perl scripts implementation here.
                **/
                m_exceptionEntry = StreamUtil.readInt(is);
                print("\texceptionEntry: " + m_exceptionEntry);
            }
        }
        else m_exceptionEntry = 0;

        m_repeatEventFlag = StreamUtil.readShort(is);
        print("\trepeatEventFlag: " + m_repeatEventFlag);

        // The rest of these members could (should?) be pushed down into repeat
        // event decoder. They are here because this is where they exist in
        // the decode document.
        switch(m_repeatEventFlag)
        {
            case 0x0000:
            {
                m_classEntry = null;
                return;
            }
            case ((short)0xFFFF):
            {
                print("** We have a class entry **");
                m_classEntry = new ClassEntry(is, m_verbose);
                break;
            }
            default:
            {
                /**
                 * This this seems to be the right way to do this. It was
                 * not clear in the decode document
                **/
                //m_classEntry = new ClassEntry(is, m_verbose);
                m_classEntry = null;
            }
        }

        m_brand = StreamUtil.readInt(is);
        switch (m_brand)
        {
            case 1: print("\tDaily (1)"); break;
            case 2: print("\tWeekly (2)"); break;
            case 3: print("\tMonthlyByDay (3)"); break;
            case 4: print("\tMonthlyByDate (4)"); break;
            case 5: print("\tYearlyByDate(5)"); break;
            case 6: print("\tYearlyByDay (6)"); break;
            default: print("\tUnknown Repeat type (" + (m_brand&0xff) + ")" ); break;
        }


        m_interval = StreamUtil.readInt(is);
        print("Interval: " + m_interval);

        // Convert Seconds to milli seconds
        m_endDate = (long)StreamUtil.readInt(is) * 1000;
        Date date = new Date(m_endDate);
        print("Repeat EndDate: " + date.toString() + "(" + m_endDate + ")");

        m_firstDayWk = (StreamUtil.readInt(is)&0xff);

        if (m_firstDayWk < 0 || m_firstDayWk > 6)
            throw new IllegalStateException("Illegal day of week epected 1-6 got " + m_firstDayWk);

        print("first dow: " + m_firstDayWk);

        m_brandData = new BrandData(is, m_brand, m_verbose);

    }

    private void print(String str)
    {
        if (!m_verbose) return;

        System.out.println("\t\t\t" + str);
    }
}

