package suffix_tree_ukkonen;

public class Index {
    public int index;

    public Index(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return Integer.toString(index);
    }
}
