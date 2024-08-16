package sample.logic;

public abstract class Cipher3Base0 implements Cipher { //Шифр Плейфейра
    public final char empty;

    protected Cipher3Base0(char c) {
        empty = c;
    }

    private String code(String in, Matrix m, Fun f) {
        if (in == null || m == null || f == null)
            throw new NullPointerException();

        int l = in.length();
        if (l == 0)
            return in;

        StringBuilder sb = new StringBuilder();
        if (l == 1) {
            append(m, f, sb, in.charAt(0), empty);
        } else {
            int i = 0;
            int save = l & (~1);
            char c1 = in.charAt(i++);
            char c2 = in.charAt(i++);
            while (true) {
                if (c1 == c2) {
                    append(m, f, sb, c1, empty);
                    c1 = c2;
                    save++;
                } else {
                    append(m, f, sb, c1, c2);
                    c1 = in.charAt(i++);
                }
                if (i >= save) {
                    append(m, f, sb, in.charAt(l - 1), empty);
                    break;
                }
                if (i < l) {
                    c2 = in.charAt(i++);
                }
                if (i >= l) {
                    append(m, f, sb, c1, c2);
                    break;
                }
            }
        }
        return sb.toString();
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
        if (c1 != empty)
            sb.append(c1);
        if (c2 != empty)
            sb.append(c2);
    }

    public String encode(String s, Matrix m) {
        return code(s, m, Cipher3Base0::next);
    }
    public String decode(String s, Matrix m) {
        return code(s, m, Cipher3Base0::prev);
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