package sample.logic;

public interface Cipher {
    String encode(String s, String key);
    String decode(String s, String key);
}
