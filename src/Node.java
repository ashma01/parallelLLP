
public class Node {
    public int value;
    public int parent;  // index of parent in the list
    public int dist = 1;
    public int next;

    public Node(int value, int parent) {
        this.value = value;
        this.parent = parent;
        this.next = parent;
    }
}


