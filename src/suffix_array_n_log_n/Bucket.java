package suffix_array_n_log_n;

public class Bucket {
    public int start;
    public int end;

    public Bucket() {
    }

    public int size(){
        return end - start;
    }

}
