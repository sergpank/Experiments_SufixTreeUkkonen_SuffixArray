package suffix_tree_ukkonen;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Edge {
    private Map<Integer, String> wordMap;
    private Set<Integer> wordIndexes;
    private Index startIndex;
    private Index endIndex;
    private int wordIndex;
    private Vertex startVertex;
    private Vertex endVertex;

    public Edge(Map<Integer, String> wordMap, final int wordIndex, Index startIndex, Index endIndex,
                Vertex startVertex, Vertex endVertex) {
        this.wordMap = wordMap;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.wordIndex = wordIndex;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        wordIndexes = new HashSet<Integer>() {{
            add(wordIndex);
        }};
    }

    public Set<Integer> getWordIndexes() {
        return wordIndexes;
    }

    public void addWordIndex(Integer wordIndex) {
        wordIndexes.add(wordIndex);
    }

    public void addWordIndexes(Set<Integer> wordIndexes) {
        this.wordIndexes.addAll(wordIndexes);
    }

    public Index getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Index endIndex){
        this.endIndex = endIndex;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public char getFirstChar() {
        return wordMap.get(wordIndex).charAt(startIndex.index);
    }

    public char getCharAt(int pos) {
        return wordMap.get(wordIndex).charAt(startIndex.index + pos);
    }

    @Override
    public String toString() {
        return wordMap.get(wordIndex).substring(startIndex.index, endIndex.index) + " " + wordIndexes.toString();
    }

    public String getString(){
        return wordMap.get(wordIndex).substring(startIndex.index, endIndex.index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (endIndex != edge.endIndex) return false;
        if (startIndex != edge.startIndex) return false;
        if (wordIndex != edge.wordIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startIndex.index;
        result = 31 * result + endIndex.index;
        result = 31 * result + wordIndex;
        return result;
    }

    public int length() {
        return endIndex.index - startIndex.index;
    }

    public Edge[] split(int activeLength, Vertex newVertex) {
        Edge[] splitEdges = new Edge[2];
        splitEdges[0] = new Edge(wordMap, wordIndex, startIndex, new Index(startIndex.index + activeLength), startVertex, newVertex);
        splitEdges[1] = new Edge(wordMap, wordIndex, new Index(startIndex.index + activeLength), endIndex, newVertex, endVertex);

        splitEdges[0].addWordIndexes(wordIndexes);
        splitEdges[1].addWordIndexes(wordIndexes);

        return splitEdges;
    }

    public void setWordIndex(int wordIndex) {
        this.wordIndex = wordIndex;
    }
}
