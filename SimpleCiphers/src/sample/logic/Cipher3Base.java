package sample.logic;

public abstract class Cipher3Base implements Cipher { //Шифр Плейфейра
    public final char empty;

    protected Cipher3Base(char c) {
        empty = c;
    }

    private void append(Matrix m, Fun f, StringBuilder sb, char c1, char c2) {
        Point p1 = getOrThrow(m, c1);
        Point p2 = getOrThrow(m, c2);

        if (p1.x == p2.x) { //next Y
            c1 = m.get(p1.x, f.move(p1.y, m.len()));
            c2 = m.get(p2.x, f.move(p2.y, m.len()));
        } else if (p1.y == p2.y) { //next X
            c1 = m.get(f.move(p1.x, m.len()), p1.y);
            c2 = m.get(f.move(p2.x, m.len()), p2.y);
        } else { // по диагонали
            c1 = m.get(p2.x, p1.y);
            c2 = m.get(p1.x, p2.y);
        }
        sb.append(c1);
        sb.append(c2);
    }
    private String code(String in, Matrix m, Fun f) {
        if (in == null || m == null || f == null)
            throw new NullPointerException();

        int l = in.length();
        if (l == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        if (l == 1) {
            append(m, f, sb, in.charAt(0), empty);
        } else {
            int save = l & (~1);
            int i = 0;
            while (i < save) {
                char c1 = in.charAt(i++);
                char c2 = in.charAt(i++);
                if (c1 == c2) {
                    append(m, f, sb, c1, empty);
                    append(m, f, sb, c2, empty);
                } else {
                    append(m, f, sb, c1, c2);
                }
            }
            if (save != l)
                append(m, f, sb, in.charAt(l - 1), empty);
        }
        return sb.toString();
    }
    private String afterDecoding(String code) {
        if (code == null || code.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder(code.length());
        int i = 0;
        int l = code.length() - 4;
        while (i < l) {
            sb.append(code.charAt(i));
            if (code.charAt(i + 1) == empty && code.charAt(i + 3) == empty) {
                sb.append(code.charAt(i + 2));
                i += 3;
            }
            i++;
        }
        l = code.length() - 1;
        while (i < l)
            sb.append(code.charAt(i++));

        if (code.charAt(l) != empty)
            sb.append(code.charAt(l));
        return sb.toString();
    }

    public String encode(String s, Matrix m) {
        return code(s, m, Cipher3Base::next);
    }
    public String decode(String s, Matrix m) {
        return afterDecoding(code(s, m, Cipher3Base::prev));
    }

    public String encode(String s, String key) {
        return encode(s, createMatrix(key));
    }
    public String decode(String s, String key) {
        return decode(s, createMatrix(key));
    }

    public abstract Matrix createMatrix(String key);

    public static int prev(int i, int len) {
        return (i == 0) ? (len - 1) : (i - 1);
    }
    public static int next(int i, int len) {
        return (i == len - 1) ? 0 : (i + 1);
    }

    private static Point getOrThrow(Matrix m, char c) {
        if (m == null)
            throw new NullPointerException();
        Point pos = m.getPos(c);
        if (pos == null)
            throw new IllegalArgumentException("Illegal character: " + c);
        return pos;
    }
}