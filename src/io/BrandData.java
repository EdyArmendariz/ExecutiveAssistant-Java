package io;

import java.io.DataInputStream;
import java.io.IOException;

// ---------------------------------------------------------------------------
// BrandData
// ---------------------------------------------------------------------------
final class BrandData
{
    private boolean m_verbose;

    private int m_dayIndex;
    private int m_dayMask;
    private int m_weekIndex;
    private int m_dayNumber;
    private int m_monthIndex;

    public BrandData(DataInputStream is, int brand, boolean verbose)
        throws IOException
    {
        m_verbose = verbose;
        brand = (brand & 0xff);
        print("** brand data " + brand + " **");

        if (brand == RepeatEvent.DAILY || brand == RepeatEvent.WEEKLY ||
            brand == RepeatEvent.MONTHLYBYDAY)
        {
            m_dayIndex = StreamUtil.readInt(is);
            print("dayIndex: " + m_dayIndex);
        }

        if (brand == RepeatEvent.WEEKLY)
        {
            m_dayMask = is.readByte();
            print("dayMask: " + m_dayMask);
        }

        if (brand == RepeatEvent.MONTHLYBYDAY)
        {
            m_weekIndex = StreamUtil.readInt(is);
            print("weekIndex: " + m_weekIndex);
        }

        if (brand == RepeatEvent.MONTHLYBYDATE ||
            brand == RepeatEvent.YEARLYBYDATE)
        {
            m_dayNumber = StreamUtil.readInt(is);
            print("dayNumber: " + m_dayNumber);
        }

        if (brand == RepeatEvent.YEARLYBYDATE)
        {
            m_monthIndex = StreamUtil.readInt(is);
            print("monthIndex: " + m_monthIndex);
        }
    }

    private void print(String str)
    {
        if (!m_verbose) return;

        System.out.println(str);
    }
}
