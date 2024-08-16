package sample.logic;

import sample.logic.Cipher;

import java.util.Objects;

public abstract class Filter implements Cipher {
    public final Cipher c;

    public Filter(Cipher c) {
        this.c = Objects.requireNonNull(c);
    }

    public String encode(String s, String key) {
        return c.encode(apply(s), apply(key));
    }
    public String decode(String s, String key) {
        return c.decode(apply(s), apply(key));
    }

    public abstract String apply(String s);
}