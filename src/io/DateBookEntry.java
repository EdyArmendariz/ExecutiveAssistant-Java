package io;

import java.util.Date;
import java.io.DataInputStream;
import java.io.IOException;

// ---------------------------------------------------------------------------
// Class that contains an entry from the datebook
// ---------------------------------------------------------------------------
final class DateBookEntry
{
    private boolean m_verbose;

    private final int m_fieldType1, m_recordId, m_fieldType2, m_statusField,
            m_fieldType3, m_pos, m_fieldType4;
    private final long m_startTime;
    private final int m_fieldType5;
    private final long m_endTime;
    private final int m_fieldType6, m_padding1;

    private final String m_description;

    private final int m_fiedlType7, m_duration, m_fieldType8, m_padding2;

    private final String m_note;

    private final int m_fieldType9, m_unTimed, m_fieldType10, m_private,
            m_fieldType11, m_category, m_fieldType12, m_alarmSet, m_fieldType13,
            m_alarmAdvUnits, m_fieldType14, m_alarmAdvType, m_fieldType15;

    private final RepeatEvent m_repeatEvent;

    public DateBookEntry(DataInputStream is, boolean verbose)
        throws IOException
    {
        m_verbose = verbose;
        print("** datebook entry **");

        Date date;

        m_fieldType1 = StreamUtil.readInt(is);
        print("\tfieldType1: " + m_fieldType1);

        m_recordId = StreamUtil.readInt(is);
        print("\trecordId: " + m_recordId);

        m_fieldType2 = StreamUtil.readInt(is);
        print("\tfieldType2: " + m_fieldType2);

        m_statusField = StreamUtil.readInt(is);
        print("\tstatusField: " + m_statusField);
        decodeStatus(m_statusField);


        m_fieldType3 = StreamUtil.readInt(is);
        print("\tfieldType3: " + m_fieldType3);

        m_pos = StreamUtil.readInt(is);
        print("\tpos: " + m_pos);

        m_fieldType4 = StreamUtil.readInt(is);
        print("\tfieldType4: " + m_fieldType4);

        // record is stored in sec we need milli seconds
        // Don't remove the cast. It will cause calcuation errors
        m_startTime = (long)StreamUtil.readInt(is) * 1000;
        date = new Date(m_startTime);
        print("\tstartTime: " + date.toString() + " (" + m_startTime + ")");

        m_fieldType5 = StreamUtil.readInt(is);
        print("\tfieldType5: " + m_fieldType5);

        // record is stored in sec we need milli seconds
        // Don't remove the cast. It will cause calcuation errors
        m_endTime = (long)StreamUtil.readInt(is) * 1000;
        date = new Date(m_endTime);
        print("\tendTime: " + date.toString());

        m_fieldType6 = StreamUtil.readInt(is);
        print("\tfieldType6: " + m_fieldType6);

        m_padding1 = StreamUtil.readInt(is);
        print("\tpadding1: " + m_padding1);

        m_description = StreamUtil.readCString(is);
        print("\tdescription: \"" + m_description + "\"");

        m_fiedlType7 = StreamUtil.readInt(is);
        print("\tfiedlType7: " + m_fiedlType7);

        m_duration = StreamUtil.readInt(is);
        print("\tduration: " + m_duration);

        m_fieldType8 = StreamUtil.readInt(is);
        print("\tfieldType8: " + m_fieldType8);

        m_padding2 = StreamUtil.readInt(is);
        print("\tpadding2: " + m_padding2);

        m_note = StreamUtil.readCString(is);
        print("\tnote: \"" + m_note + "\"");

        m_fieldType9 = StreamUtil.readInt(is);
        print("\tfieldType9: " + m_fieldType9);

        m_unTimed = StreamUtil.readInt(is);
        print("\tunTimed: " + m_unTimed);

        m_fieldType10 = StreamUtil.readInt(is);
        print("\tfieldType10: " + m_fieldType10);

        m_private = StreamUtil.readInt(is);
        print("\tprivate: " + m_private);

        m_fieldType11 = StreamUtil.readInt(is);
        print("\tfieldType11: " + m_fieldType11);

        m_category = StreamUtil.readInt(is);
        print("\tcategory: " + m_category);

        m_fieldType12 = StreamUtil.readInt(is);
        print("\tfieldType12: " + m_fieldType12);

        m_alarmSet = StreamUtil.readInt(is);
        print("\talarmSet: " + m_alarmSet);

        m_fieldType13 = StreamUtil.readInt(is);
        print("\tfieldType13: " + m_fieldType13);

        m_alarmAdvUnits = StreamUtil.readInt(is);
        print("\talarmAdvUnits: " + m_alarmAdvUnits);

        m_fieldType14 = StreamUtil.readInt(is);
        print("\tfieldType14: " + m_fieldType14);
        switch (m_fieldType14)
        {
            case 0: print("\t\tMinutes"); break;
            case 1: print("\t\tHours");   break;
            case 2: print("\t\tDays");    break;
        }


        m_alarmAdvType = StreamUtil.readInt(is);
        print("\talarmAdvType: " + m_alarmAdvType);

        m_fieldType15 = StreamUtil.readInt(is);
        print("\tfieldType15: " + m_fieldType15);

        m_repeatEvent = new RepeatEvent(is, m_verbose);

        print("Record parsed properly");
    }

    private void decodeStatus(int status)
    {
        if ((status & 0x08) != 0)
            print("\t\tPending (0x08)");

        if ((status & 0x01) != 0)
            print("\t\tAdd (0x01)");

        if ((status & 0x02) != 0)
            print("\t\tUpdate (0x02)");

        if ((status & 0x04) != 0)
            print("\t\tDelete (0x04)");

        if ((status & 0x80) != 0)
            print("\t\tArchive (0x80)");
    }

    private void print(String str)
    {
        if (!m_verbose) return;

        System.out.println(str);
    }
}

