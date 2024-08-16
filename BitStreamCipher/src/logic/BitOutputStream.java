package logic;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class BitOutputStream extends OutputStream {
    private final OutputStream out;

    public BitOutputStream(OutputStream out) {
        this.out = Objects.requireNonNull(out);
    }

    public void write(int b) throws IOException {
        for (int i = 7; i != 0; i--)
            out.write(((b >>> i) & 1) + '0');
        out.write((b & 1) + '0');
    }

    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }
    public void close() throws IOException {
        out.close();
    }
}
