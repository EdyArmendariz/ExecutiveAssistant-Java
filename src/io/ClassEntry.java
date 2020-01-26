package io;

import java.io.DataInputStream;
import java.io.IOException;

// ---------------------------------------------------------------------------
// ClassEntry
// ---------------------------------------------------------------------------
final class ClassEntry
{
    private boolean m_verbose;
    private short m_const;
    private byte m_classByte;
    private String m_className;

    public ClassEntry(DataInputStream is, boolean verbose)
        throws IOException
    {
        m_verbose = verbose;
        print("class entry");

        m_const = StreamUtil.readShort(is);
        print("const: " + m_const);

        int len = StreamUtil.readShort(is);
        m_classByte = is.readByte();

        // I am not sure why they just did not reuse CString format !?!
        StringBuffer str = new StringBuffer(len);
        for (int i = 1; i < len; i++)
            str.append(((char)is.readByte()));


        m_className = str.toString();
        print("className: " + m_className);
    }

    private void print(String str)
    {
        if (!m_verbose) return;

        System.out.println(str);
    }
}

