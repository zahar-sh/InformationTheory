package logic;

import java.io.*;

public class Encoder {
    private static final int BUF_LEN = 8192;

    public static void encode(InputStream in, OutputStream out, BitGenerator g) throws IOException {
        int key, bitIndex, bufIndex, bufLen;
        byte[] buf = new byte[BUF_LEN - 5];
        while ((bufLen = in.read(buf, 0, buf.length)) > 0) {
            for (bufIndex = 0; bufIndex < bufLen; bufIndex++) {
                key = 0;
                for (bitIndex = 0; bitIndex < 8; bitIndex++) {
                    key <<= 1;
                    key |= g.nextBit();
                }
                buf[bufIndex] ^= key;
            }
            out.write(buf, 0, bufLen);
        }
    }
    public static void fileEncode(File in, File encode, BitGenerator g) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream os = new FileOutputStream(encode)) {
            encode(is, os, g);
        }
    }

    public static void encode(InputStream in, OutputStream keyBits, OutputStream out, BitGenerator g) throws IOException {
        int key, bitIndex, bufIndex, bufLen;
        byte[] buf = new byte[BUF_LEN - 5];
        while ((bufLen = in.read(buf, 0, buf.length)) > 0) {
            for (bufIndex = 0; bufIndex < bufLen; bufIndex++) {
                key = 0;
                for (bitIndex = 0; bitIndex < 8; bitIndex++) {
                    key <<= 1;
                    key |= g.nextBit();
                }
                buf[bufIndex] ^= key;
                keyBits.write(key);
            }
            out.write(buf, 0, bufLen);
        }
    }
    public static void fileEncode(File in, File keyBits, File out, BitGenerator g) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream ks = new FileOutputStream(keyBits);
             OutputStream os = new FileOutputStream(out)) {
            encode(is, ks, os, g);
        }
    }

    public static void printBits(File in, File bits, int limit) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream bs = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(bits), BUF_LEN))) {
            byte[] buf = new byte[BUF_LEN];
            int l;
            while (limit > 0 && (l = is.read(buf, 0,
                    Math.min(buf.length, limit))) > 0) {
                bs.write(buf, 0, l);
                limit -= l;
            }
        }
    }
    public static void fileEncode(File in, File out, BitGenerator g,
                                  File inBits, File keyBits, File outBits, int limit) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream ks = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(keyBits), BUF_LEN));
             OutputStream os = new FileOutputStream(out)) {
            encode(is, new OutputStream() {
                int all = 0;
                public void write(int b) throws IOException {
                    if (all < limit) {
                        ks.write(b);
                        all++;
                    }
                }
            }, os, g);
        }
        printBits(in, inBits, limit);
        printBits(out, outBits, limit);

    }

    public static void printBits(File in, File bits) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream bs = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(bits), BUF_LEN))) {
            byte[] buf = new byte[BUF_LEN];
            int l;
            while ((l = is.read(buf, 0, buf.length)) > 0)
                bs.write(buf, 0, l);
        }
    }
    public static void fileEncode(File in, File out, BitGenerator g,
                                  File inBits, File keyBits, File outBits) throws IOException {
        try (InputStream is = new FileInputStream(in);
             OutputStream ks = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(keyBits), BUF_LEN));
             OutputStream os = new FileOutputStream(out)) {
            encode(is, ks, os, g);
        }
        printBits(in, inBits);
        printBits(out, outBits);

    }

    public static String readAll(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            ByteArrayOutputStream b = new ByteArrayOutputStream(BUF_LEN);
            byte[] buf = new byte[BUF_LEN];
            int l;
            while ((l = in.read(buf, 0, buf.length)) > 0)
                b.write(buf, 0, l);
            return b.toString();
        }
    }
    public static String readNBytes(File f, int n) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            byte[] buf = new byte[n];
            int l = in.read(buf, 0, buf.length);
            return new String(buf, 0, l);
        }
    }
}
