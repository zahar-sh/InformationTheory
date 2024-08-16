package sample.logic;

public class Cipher3 extends Cipher3Base0 {
    public final String alphabet;
    public final int matLen;

    public Cipher3(int len, String alphabet, char empty) {
        super(empty);

        if (alphabet == null)
            throw new NullPointerException();

        if (alphabet.indexOf(empty) < 0)
            throw new IllegalArgumentException();

        if (len * len < alphabet.length())
            throw new IllegalArgumentException();

        this.matLen = len;
        this.alphabet = alphabet;
    }

    public Matrix createMatrix(String key) {
        if (key == null)
            throw new NullPointerException();
        char[] data = new char[matLen * matLen];

        int size = 0;
        int i;
        char c;
        for (i = 0; i < key.length() && size < data.length; i++) {
            c = key.charAt(i);
            if (alphabet.indexOf(c) >= 0 && indexOf(data, size, c) < 0)
                data[size++] = c;
        }
        for (i = 0; i < alphabet.length() && size < data.length; i++) {
            c = alphabet.charAt(i);
            if (indexOf(data, size, c) < 0)
                data[size++] = c;
        }

        return new Matrix() {
            public int len() {
                return matLen;
            }

            public Point getPos(char c) {
                c = chPosApply(c);
                for (int i = 0; i < data.length; i++) {
                    if (c == data[i]) {
                        int y = i / matLen; //i / matLen
                        int x = i - (y * matLen); //i % matLen
                        return new Point(x, y);
                    }
                }
                return null;
            }

            public char get(int x, int y) {
                if (x < 0 || x >= matLen || y < 0 || y >= matLen)
                    throw new IndexOutOfBoundsException();
                return data[y * matLen + x];
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append(data);
                for (int j = 0, k = matLen; k < sb.length(); k += matLen, j++)
                    sb.insert(j + k, '\n');
                return sb.toString();
            }
        };
    }

    protected char chPosApply(char c) {
        return c;
    }

    public static int indexOf(char[] data, int size, char c) {
        for (int i = 0; i < size; i++)
            if (data[i] == c)
                return i;
        return -1;
    }

    public static void main(String[] args) {
        Cipher3 c = new Cipher3(5, "ABCDEFGHIKLMNOPQRSTUVWXYZ", 'X');
        String s = "Cryptography and data secutrity".toUpperCase();
        String key = "code".toUpperCase();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (Character.isLetter(s.charAt(i)))
                sb.append(s.charAt(i));
        s = sb.toString();

        Matrix mat = c.createMatrix(key);
        System.out.println(mat.toString());
        System.out.println(s);
        String encode = c.encode(s, mat);
        System.out.println(encode);
        String decode = c.decode(encode, mat);
        System.out.println(decode);
        System.out.println(s.equals(decode));
    }
}