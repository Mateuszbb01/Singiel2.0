package com.pwszit.singiel;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;

// Referenced classes of package Decoder:
//            CharacterDecoder, CEFormatException, CEStreamExhausted

public class BASE64Decoder extends CharacterDecoder
{

    public static final char pem_array[] = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '+', '/'
    };
    public static final byte pem_convert_array[];
    byte decode_buffer[];

    public BASE64Decoder()
    {
        decode_buffer = new byte[4];
    }

    public int bytesPerAtom()
    {
        return 4;
    }

    public int bytesPerLine()
    {
        return 72;
    }

    public void decodeAtom(PushbackInputStream inStream, OutputStream outStream, int rem)
            throws IOException
    {
        byte a = -1;
        byte b = -1;
        byte c = -1;
        byte d = -1;
        if(rem < 2)
        {
            throw new IOException("BASE64Decoder: Not enough bytes for an atom.");
        }
        int i;
        do
        {
            i = inStream.read();
            if(i == -1)
            {
                throw new IOException();
            }
        } while(i == 10 || i == 13);
        decode_buffer[0] = (byte)i;
        i = readFully(inStream, decode_buffer, 1, rem - 1);
        if(i == -1)
        {
            throw new IOException();
        }
        if(rem > 3 && decode_buffer[3] == 61)
        {
            rem = 3;
        }
        if(rem > 2 && decode_buffer[2] == 61)
        {
            rem = 2;
        }
        switch(rem)
        {
            case 4: // '\004'
                d = pem_convert_array[decode_buffer[3] & 0xff];
                // fall through

            case 3: // '\003'
                c = pem_convert_array[decode_buffer[2] & 0xff];
                // fall through

            case 2: // '\002'
                b = pem_convert_array[decode_buffer[1] & 0xff];
                a = pem_convert_array[decode_buffer[0] & 0xff];
                // fall through

            default:
                switch(rem)
                {
                    case 2: // '\002'
                        outStream.write((byte)(a << 2 & 0xfc | b >>> 4 & 3));
                        break;

                    case 3: // '\003'
                        outStream.write((byte)(a << 2 & 0xfc | b >>> 4 & 3));
                        outStream.write((byte)(b << 4 & 0xf0 | c >>> 2 & 0xf));
                        break;

                    case 4: // '\004'
                        outStream.write((byte)(a << 2 & 0xfc | b >>> 4 & 3));
                        outStream.write((byte)(b << 4 & 0xf0 | c >>> 2 & 0xf));
                        outStream.write((byte)(c << 6 & 0xc0 | d & 0x3f));
                        break;
                }
                break;
        }
    }

    static
    {
        pem_convert_array = new byte[256];
        for(int i = 0; i < 255; i++)
        {
            pem_convert_array[i] = -1;
        }

        for(int i = 0; i < pem_array.length; i++)
        {
            pem_convert_array[pem_array[i]] = (byte)i;
        }

    }
}
