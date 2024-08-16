package sample;

import java.io.*;

public class Files {
    public static String readAll(File file) throws IOException {
        return readAll(new FileInputStream(file));
    }
    public static String readAll(InputStream stream) throws IOException {
        try (Reader reader = new InputStreamReader(stream)) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int l;
            while ((l = reader.read(buf, 0, buf.length)) > 0)
                sb.append(buf, 0, l);
            return sb.toString();
        }
    }

    public static void writeAll(File file, String string) throws IOException {
        writeAll(new FileOutputStream(file), string);
    }
    public static void writeAll(OutputStream stream, String string) throws IOException {
        try (Writer writer = new OutputStreamWriter(stream)) {
            writer.write(string);
        }
    }
}
