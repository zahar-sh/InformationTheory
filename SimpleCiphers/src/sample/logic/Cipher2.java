package sample.logic;

public class Cipher2 implements Cipher { //алгоритм Виженера, самогенерирующийся ключ
    private final String alphabet;

    public Cipher2(String alphabet) {
        if (alphabet == null)
            throw new NullPointerException();
        this.alphabet = alphabet;
    }

    protected int indexOf(char c) {
        for (int i = 0, l = alphabet.length(); i < l; i++)
            if (c == alphabet.charAt(i))
                return i;
        return -1;
    }
    private char encodeChar(char c, char key) {
        int i = indexOf(c);
        if (i < 0)
            throw new IllegalArgumentException("Illegal character: " + c);
        int k = indexOf(key);
        if (k < 0)
            throw new IllegalArgumentException("Illegal key character: " + key);

        return alphabet.charAt((i + k) % alphabet.length());
    }
    private char decodeChar(char c, char key) {
        int i = indexOf(c);
        if (i < 0)
            throw new IllegalArgumentException("Illegal character: " + c);
        int k = indexOf(key);
        if (k < 0)
            throw new IllegalArgumentException("Illegal key character: " + key);
        return alphabet.charAt((i + alphabet.length() - k) % alphabet.length());
    }

    public String encode(String s, String key) {
        if (s == null || key == null)
            throw new NullPointerException();
        int l = s.length();
        if (l == 0)
            return "";

        int kl = key.length();
        if (kl == 0)
            throw new IllegalArgumentException("Empty key");

        int i = 0;
        StringBuilder sb = new StringBuilder(l);
        if (l > kl) {
            while (i < kl) {
                sb.append(encodeChar(s.charAt(i), key.charAt(i)));
                i++;
            }
            while (i < l) {
                sb.append(encodeChar(s.charAt(i), s.charAt((i - kl))));
                i++;
            }
        } else {
            while (i < l) {
                sb.append(encodeChar(s.charAt(i), key.charAt(i)));
                i++;
            }
        }
        return sb.toString();
    }
    public String decode(String s, String key) {
        if (s == null || key == null)
            throw new NullPointerException();
        int l = s.length();
        if (l == 0)
            return "";

        int kl = key.length();
        if (kl == 0)
            throw new IllegalArgumentException("Empty key");

        StringBuilder sb = new StringBuilder(l);

        int min = Math.min(l, key.length());
        int i = 0;
        while (i < min) {
            sb.append(decodeChar(s.charAt(i), key.charAt(i)));
            i++;
        }
        while (i < l) {
            sb.append(decodeChar(s.charAt(i), sb.charAt((i - min))));
            i++;
        }
        return sb.toString();
    }

    public String printTable() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, l = alphabet.length(); i < l; i++)
            sb.append(alphabet, i, l).append(alphabet, 0, i).append('\n');
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toUpperCase();

        Cipher2 cipher = new Cipher2(s);
        String input = "Ёлки зелёные в лесу".toUpperCase();
        String key = "Ёлки".toUpperCase();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++)
            if (s.indexOf(input.charAt(i)) >= 0)
                sb.append(input.charAt(i));
        input = sb.toString();

        System.out.println(cipher.printTable());
        System.out.println(input);
        String encode = cipher.encode(input, key);
        System.out.println(encode);
        System.out.println(cipher.decode(encode, key));
    }
}