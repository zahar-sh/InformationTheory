package logic;

//x32 + x28 + x27 + x + 1
public class BitGenerator {
    private int bits;

    public BitGenerator(int bits) {
        this.bits = bits;
    }

    public int nextBit() {
        final int firstBit = bits >>> 31;
        int newBit = (firstBit ^ (bits >>> 27) ^ (bits >>> 26) ^ bits) & 1;
        bits = (bits << 1) | newBit;
        return (firstBit & 1);
    }
    public int getBits() {
        return bits;
    }
    /*public String bitsToStr() {
        String str = Integer.toBinaryString(getBits());
        int l = str.length();
        return l > 32 ? str.substring(l - 32) :
                ("0".repeat(32 - l) + str);
    }*/
    public String bitsToStr() {
        String str = Integer.toBinaryString(getBits());
        return "0".repeat(32 - str.length()) + str;
    }

    public static void main(String[] args) {
        BitGenerator g = new BitGenerator(-1);
        System.out.println(g.bitsToStr());
        for (int i = 0; i < 53; i++) {
            g.nextBit();
            System.out.println(g.bitsToStr());
        }
    }
}

