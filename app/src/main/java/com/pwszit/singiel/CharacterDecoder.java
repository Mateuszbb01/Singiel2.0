package com.pwszit.singiel;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;

// Referenced classes of package Decoder:
//            CEStreamExhausted

public abstract class CharacterDecoder
{

    public CharacterDecoder()
    {
    }

    protected abstract int bytesPerAtom();

    protected abstract int bytesPerLine();

    protected void decodeBufferPrefix(PushbackInputStream pushbackinputstream, OutputStream outputstream)
            throws IOException
    {
    }

    protected void decodeBufferSuffix(PushbackInputStream pushbackinputstream, OutputStream outputstream)
            throws IOException
    {
    }

    protected int decodeLinePrefix(PushbackInputStream aStream, OutputStream bStream)
            throws IOException
    {
        return bytesPerLine();
    }

    protected void decodeLineSuffix(PushbackInputStream pushbackinputstream, OutputStream outputstream)
            throws IOException
    {
    }

    protected void decodeAtom(PushbackInputStream aStream, OutputStream bStream, int l)
            throws IOException
    {
        throw new IOException();
    }

    protected int readFully(InputStream in, byte buffer[], int offset, int len)
            throws IOException
    {
        for(int i = 0; i < len; i++)
        {
            int q = in.read();
            if(q == -1)
            {
                return i != 0 ? i : -1;
            }
            buffer[i + offset] = (byte)q;
        }

        return len;
    }

    public void decodeBuffer(InputStream aStream, OutputStream bStream)
            throws IOException
    {
        int totalBytes = 0;
        PushbackInputStream ps = new PushbackInputStream(aStream);
        decodeBufferPrefix(ps, bStream);
        try
        {
            do
            {
                int length = decodeLinePrefix(ps, bStream);
                int i;
                for(i = 0; i + bytesPerAtom() < length; i += bytesPerAtom())
                {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                }

                if(i + bytesPerAtom() == length)
                {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                } else
                {
                    decodeAtom(ps, bStream, length - i);
                    totalBytes += length - i;
                }
                decodeLineSuffix(ps, bStream);
            } while(true);
        }
        catch(IOException cestreamexhausted)
        {
            decodeBufferSuffix(ps, bStream);
        }
    }

    public byte[] decodeBuffer(String inputString)
            throws IOException
    {
        byte inputBuffer[] = new byte[inputString.length()];
        inputString.getBytes(0, inputString.length(), inputBuffer, 0);
        ByteArrayInputStream inStream = new ByteArrayInputStream(inputBuffer);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        decodeBuffer(((InputStream) (inStream)), ((OutputStream) (outStream)));
        return outStream.toByteArray();
    }

    public byte[] decodeBuffer(InputStream in)
            throws IOException
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        decodeBuffer(in, ((OutputStream) (outStream)));
        return outStream.toByteArray();
    }

    public ByteBuffer decodeBufferToByteBuffer(String inputString)
            throws IOException
    {
        return ByteBuffer.wrap(decodeBuffer(inputString));
    }

    public ByteBuffer decodeBufferToByteBuffer(InputStream in)
            throws IOException
    {
        return ByteBuffer.wrap(decodeBuffer(in));
    }
}
