package sample.logic;

import java.util.Arrays;
import java.util.Comparator;

public class Cipher1 implements Cipher { //столбцовый метод
    public String encode(String s, String key) {
        return encode0(s, vectorFromKey(key));
    }
    public String decode(String s, String key) {
        return decode0(s, vectorFromKey(key));
    }

    protected String vectorFromKey(String key) {
        if (key == null)
            throw new NullPointerException();
        int l = key.length();
        if (l == 0)
            return "";
        char[] visited = new char[l];
        int size = 0;

        StringBuilder sb = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            char c = key.charAt(i);
            while (contains(visited, size, c))
                c++;
            visited[size++] = c;
            sb.append(c);
        }
        return sb.toString();
    }

    private static boolean contains(char[] visited, int size, char c) {
        for (int i = 0; i < size; i++)
            if (c == visited[i])
                return true;
        return false;
    }
    private static Node[] createNodes(String vector, int l) {
        Node[] nodes = new Node[l];
        for (int i = 0; i < l; i++)
            nodes[i] = new Node(vector.charAt(i), i);
        Arrays.sort(nodes, Comparator.comparingInt(o -> o.key));
        return nodes;
    }

    private String encode0(String s, String vector) {
        if (s == null || vector == null)
            throw new NullPointerException();
        int l = s.length();
        if (l == 0)
            return "";
        int vl = vector.length();
        if (vl == 0)
            throw new IllegalArgumentException("Empty key");

        Node[] nodes = createNodes(vector, vl);
        StringBuilder sb = new StringBuilder(l);
        for (int i = 0; i < vl; i++) {
            for (int j = nodes[i].index; j < l; j += vl) {
                sb.append(s.charAt(j));
                //System.out.append(s.charAt(j)).print(' ');
            }
            //System.out.println();
        }
        return sb.toString();
    }
    private String decode0(String s, String vector) {
        if (s == null || vector == null)
            throw new NullPointerException();
        int l = s.length();
        if (l == 0)
            return "";
        int vl = vector.length();
        if (vl == 0)
            throw new IllegalArgumentException("Empty key");

        Node[] nodes = createNodes(vector, vl);
        char[] cs = new char[l];
        int k = 0;
        for (int i = 0; i < vl; i++)
            for (int j = nodes[i].index; k < l && j < l; j += vl)
                cs[j] = s.charAt(k++);
        return new String(cs);
    }

    public static void main(String[] args) {
        String s = "Ёлки зелёные в лесу".toUpperCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (Character.isLetterOrDigit(s.charAt(i)))
                sb.append(s.charAt(i));
        s = sb.toString();

        String key = "ёлки";

        Cipher1 cipher = new Cipher1();

        String ci = cipher.encode(s, key);
        System.out.println(ci);

        String ret = cipher.decode(ci, key);
        System.out.println(ret);
    }
}