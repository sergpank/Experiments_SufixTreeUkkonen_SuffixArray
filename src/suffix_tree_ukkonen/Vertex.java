package suffix_tree_ukkonen;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private Vertex suffixLink;
    private List<Edge> edges = new ArrayList<Edge>();

    public Vertex() {
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Edge findEdgeStartingWith(char startChar) {
        Edge result = null;
        for (Edge edge : edges) {
            if (edge.getFirstChar() == startChar) {
                result = edge;
                break;
            }
        }
        return result;
    }

    public Vertex getSuffixLink() {
        return suffixLink;
    }

    public void setSuffixLink(Vertex suffixLink) {
        this.suffixLink = suffixLink;
    }
}
