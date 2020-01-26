package io;

import java.io.IOException;
import java.io.DataInputStream;

// ---------------------------------------------------------------------------
// Helper utility for reading Streams
// ---------------------------------------------------------------------------
final class StreamUtil
{
    // TODO: Figure out a better way to do this
    // This is lame way to do this but oh well; DataInputStrea.readInt() has
    // wrong byte order. ByteBuffer would be my choice but can't count on
    // Java 1.4 on all web servers out there :(
    public static int readInt(DataInputStream is)
        throws IOException
    {
        byte[] buf = new byte[4];
        buf[0] = is.readByte();
        buf[1] = is.readByte();
        buf[2] = is.readByte();
        buf[3] = is.readByte();

        int res = 0;
        int i, n;
        for (i = 0; i != 4; i++)
        {
            n = buf[i] & 0xFF;
            n <<= i * 8;
            res |=  n;
        }
        return res;
    }

    // TODO: Figure out a better way to do this
    // This is lame way to do this but oh well; DataInputStrea.readInt() has
    // wrong byte order. ByteBuffer would be my choice but can't count on
    // Java 1.4 on all web servers out there :(
    public static short readShort(DataInputStream is)
        throws IOException
    {
        byte[] buf = new byte[2];
        buf[0] = is.readByte();
        buf[1] = is.readByte();

        short res = 0;
        int i, n;
        for (i = 0; i != 2; i++)
        {
            n = buf[i] & 0xFF;
            n <<= i * 8;
            res |=  n;
        }
        return res;
    }

    /**
     * Cstrings are stored as folnnlows:
     * 1. Strings less than 255 bytes are stored with the length specified in
     * the first byte followed by the actual string.
     *
     * 2. Zero length strings are stored with a 0x00 byte.
     *
     * 3. Strings 255 bytes or longer are stored with a flag byte set to 0xFF
     * followed by a short (2*Byte) that specifies the length of the string,
     * followed by the actual string.
    **/
    public static String readCString(DataInputStream is)
        throws IOException
    {
        StringBuffer scratchfile = new StringBuffer();
        int len = is.readByte();

        if (len == (byte)0xFF) len = readShort(is);

        for (int i = 0; i != len; i++)
            scratchfile.append((char)is.readByte());

        return scratchfile.toString();
    }

    /**
     * This will dump the hex values of the next N bytes in stream.
     *
     * Warning: if you call this the decoder will not be able to parse
     * anymore of the file.
    **/
    public static void dumpBytes(DataInputStream is, int count)
        throws IOException
    {
        System.err.println(
            "\nStart stream dump: "
            + "\n----------------------------------------------");
        Integer val;
        for (int i = 0; i < count; i++)
        {
            if (i%16 == 0)
                System.err.print("\n");

            val = new Integer(is.readByte() & 0xFF );
            System.err.print(
                    ((val.intValue() < 0x10) ? "0x0" : "0x")
                    + Integer.toHexString(val.intValue()).toUpperCase() + " ");
        }
        System.err.println(
                "\n\nEnd of Stream Dump"
                + "\n----------------------------------------------"
                + "\n\n");

        // throw new IllegalStateException(count + " bytes dumped to standard err");
    }
}

