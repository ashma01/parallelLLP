package Boruvka;

public class LLPNode {
    int id;
    int parent;

    public LLPNode(int id) {
        this.id = id;
        this.parent = -1;  // Initialize parent to -1 (undefined)
    }
}
