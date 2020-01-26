package io;

import java.io.IOException;
import java.io.DataInputStream;

// ---------------------------------------------------------------------------
// Data class that contains all datebook entries
// ---------------------------------------------------------------------------
final class Data
{
    private boolean m_verbose = false;
    private static final int DATEBOOKVERSION = 0x44420100;
    private final int m_version;
    private final String m_origName;
    private final String m_showHeader;
    private final int m_nextFreeCat;
    private final int m_CategoryCount;
    private final int m_schemaResourceID;
    private final int m_fieldsPerRow;
    private final int m_recordIdPos;
    private final int m_recordStatusPos;
    private final int m_recordPlacementPos;
    private final int m_schemaFieldCount;
    private final short[] m_fieldEntrys;
    private final int m_numEntries;
    private final DateBookEntry[] m_entries;

    public Data(DataInputStream is, boolean verbose)
        throws IOException
    {
        m_verbose = verbose;
        print("** Data **");

        if (is == null)
            throw new NullPointerException("Input stream was null");

        m_version = StreamUtil.readInt(is);
        print("Version: " + m_version);
        if (m_version != DATEBOOKVERSION)
        {
            throw new IllegalArgumentException("Illegal file format: " + m_version);
        }

        m_origName = StreamUtil.readCString(is);
        print("Orig File: " + m_origName);

        m_showHeader = StreamUtil.readCString(is);
        print("Show Header: " + m_showHeader);

        m_nextFreeCat = StreamUtil.readInt(is);
        print("Next Free Cat: " + m_nextFreeCat);

        m_CategoryCount = is.readInt();
        print("Category Count: " + m_CategoryCount);

        // If there were Category Entries we would decode them here.
        if (m_CategoryCount != 0x00)
            throw new IllegalArgumentException("Decoder does not support Category Entries");

        m_schemaResourceID = StreamUtil.readInt(is);
        print("Schema ResId: " + m_schemaResourceID);

        m_fieldsPerRow = StreamUtil.readInt(is);
        print("Fields per Row (15): " + m_fieldsPerRow);

        if (m_fieldsPerRow != 0x0f)
            throw new IllegalArgumentException("Illegal Field Count");

        m_recordIdPos = StreamUtil.readInt(is);
        print("Record Pos: " + m_recordIdPos);

        m_recordStatusPos = StreamUtil.readInt(is);
        print("Record Status Pos: " + m_recordStatusPos);

        m_recordPlacementPos = StreamUtil.readInt(is);
        print("Record Placement Pos: " + m_recordPlacementPos);

        m_schemaFieldCount = StreamUtil.readShort(is);
        print("Schema Field Count: " + m_schemaFieldCount);

        m_fieldEntrys = new short[m_schemaFieldCount];
        for (int i = 0; i < m_schemaFieldCount; i++)
        {
            m_fieldEntrys[i] = StreamUtil.readShort(is);
            print("\tEntry " + i + " is " + m_fieldEntrys[i]);
        }

        m_numEntries = StreamUtil.readInt(is);
        print("Num Entries: " + m_numEntries + " \n\t actual: " + m_numEntries/15);
        m_entries = new DateBookEntry[m_numEntries/15];

        print("Header read successfully");

        parseEntries(is);
    }

    private void parseEntries(DataInputStream is)
        throws IOException
    {
        long start = System.currentTimeMillis();
        for(int i = 0; i < m_entries.length; i++)
        {
            print("Entry: " + i + " of " + m_entries.length);
            m_entries[i] = new DateBookEntry(is, m_verbose);

            // StreamUtil.dumpBytes(is, 20);
        }
        long end = System.currentTimeMillis();

        System.err.println("Parsed " + m_entries.length + " in " + (end-start)
                + " mSec");
    }

    private void print(String str)
    {
        if (!m_verbose) return;

        System.out.println(str);
    }
}
