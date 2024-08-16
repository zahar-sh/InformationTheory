package sample.logic;

class Node {
    int key, index;

    Node(int key, int index) {
        this.key = key;
        this.index = index;
    }

    public String toString() {
        return (char) (key) + "->" + index;
    }
}
