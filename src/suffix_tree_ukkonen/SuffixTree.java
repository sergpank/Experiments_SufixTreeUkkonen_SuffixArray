package suffix_tree_ukkonen;

import java.util.*;

public class SuffixTree {

    private Vertex rootVertex;
    private Map<Integer, String> wordMap = new HashMap<Integer, String>();
    private ActivePoint activePoint;
    private Index currentPosition = new Index(0);
    private Integer remainder = 0;
    private Vertex candidateForSuffixLink;

    public SuffixTree() {
        rootVertex = new Vertex();
    }

    public void addWord(String word) {
        activePoint = new ActivePoint(rootVertex, null, 0, this);
        wordMap.put(wordMap.size(), word);
        int wordIndex = wordMap.size() - 1;
        currentPosition = new Index(0);
        remainder = 1;
        while (currentPosition.index < word.length()) {
            candidateForSuffixLink = null;
            ++currentPosition.index;
            String subsuffix = getSubsuffix(word);
            addSuffix(subsuffix, wordIndex);
//            System.out.println(this);
            ++remainder;
        }
    }

    public void addSuffix(String subsuffix, int wordIndex) {
        if (remainder == 1) {
            Edge edge = activePoint.getActiveNode().findEdgeStartingWith(subsuffix.charAt(0));
            if (edge == null) {
                Index startIndex = new Index(currentPosition.index - remainder);
                Edge newEdge = new Edge(wordMap, wordIndex, startIndex, currentPosition, rootVertex, null);
                rootVertex.addEdge(newEdge);
                --remainder;
                return;
            } else {
                activePoint.setActiveEdge(edge);
                activePoint.setActiveNode(edge.getStartVertex());
                activePoint.incrementActiveLength(subsuffix.charAt(0), wordIndex, subsuffix);
            }
        } else {
            if(activePoint.getActiveLength() == activePoint.getActiveEdge().length() && activePoint.getActiveEdge().getEndVertex() == null){
                activePoint.getActiveEdge().setEndIndex(currentPosition);
                activePoint.getActiveEdge().addWordIndex(wordIndex);
                activePoint.getActiveEdge().setWordIndex(wordIndex);
            }
            else if (activePoint.isNeedToInsert(subsuffix.charAt(subsuffix.length() - 1), subsuffix, wordIndex)) {
                splitEdgeAndInsertNode(wordIndex, subsuffix);
            } else {
                activePoint.incrementActiveLength(subsuffix.charAt(remainder - 1), wordIndex, subsuffix);
//            } else if (activePoint.getActiveLength() == activePoint.getActiveEdge().length() &&
//                    activePoint.getActiveEdge().getEndVertex().findEdgeStartingWith(charToInsert) == null) {
//                activePoint.getActiveEdge().setEndIndex(currentPosition);
            }
        }
    }

    private void splitEdgeAndInsertNode(final int wordIndex, String suffix) {
        Vertex newVertex = new Vertex();
        Edge newEdge = new Edge(wordMap, wordIndex, new Index(currentPosition.index - 1), currentPosition, newVertex, null);

        if (activePoint.getActiveEdge().length() == activePoint.getActiveLength()) {
            if (activePoint.getActiveEdge().getEndIndex() == currentPosition) {
                activePoint.getActiveEdge().setEndIndex(new Index(currentPosition.index));
            }
            activePoint.getActiveEdge().getEndVertex().addEdge(newEdge);
            newVertex = activePoint.getActiveEdge().getStartVertex();
            activePoint.getActiveEdge().addWordIndex(wordIndex);
        } else {
            Edge[] splitEdges = activePoint.getActiveEdge().split(activePoint.getActiveLength(), newVertex);
            newVertex.addEdge(newEdge);
            newVertex.addEdge(splitEdges[1]);

            activePoint.getActiveNode().getEdges().remove(activePoint.getActiveEdge());
            activePoint.getActiveNode().addEdge(splitEdges[0]);
            splitEdges[0].addWordIndex(wordIndex);
        }

        --remainder;

        if (activePoint.getActiveNode() == rootVertex) {
            activePoint.setActiveEdge(rootVertex.findEdgeStartingWith(suffix.charAt(1)));
            activePoint.decrementActiveLength();
        } else {
            Vertex suffixLink = activePoint.getActiveNode().getSuffixLink();
            if (suffixLink != null) {
                activePoint.setActiveNode(suffixLink);
            } else {
                activePoint.setActiveNode(rootVertex);
            }
            Edge newActiveEdge = activePoint.getActiveNode().findEdgeStartingWith(activePoint.getActiveEdge().getFirstChar());
            activePoint.setActiveEdge(newActiveEdge);
//            activePoint.verifyAndFixLength(suffix);
        }

        if (candidateForSuffixLink != null) {
            candidateForSuffixLink.setSuffixLink(newVertex);
            candidateForSuffixLink = newVertex;
        } else {
            candidateForSuffixLink = newVertex;
        }

        addSuffix(suffix.substring(1, suffix.length()), wordIndex);
    }

    private String getSubsuffix(String suffix) {
        return suffix.substring(currentPosition.index - remainder, currentPosition.index);
    }

    public String getLongestSubstring() {
        Set<String> subSet = Collections.synchronizedSet(new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String string1, String string2) {
                return string2.length() - string1.length();
            }
        }));
        getSubstrings(rootVertex, subSet, "");

        return subSet.iterator().next();
    }

    private void getSubstrings(Vertex node, Set<String> strings, String substring) {
        if (node != null) {
            for (Edge edge : node.getEdges()) {
                if (edge.getWordIndexes().size() == wordMap.size()) {
                    String edgeString = edge.getString();
                    strings.add(substring + edgeString);
                    Vertex nextVertex = edge.getEndVertex();
                    getSubstrings(nextVertex, strings, substring + edgeString);
                }
            }
        }
    }

    @Override
    public String toString() {
        return printTree(rootVertex, 0);
    }

    private String printTree(Vertex vertex, int level) {
        String string = new String();

        for (Edge e : vertex.getEdges()) {
            string += printIndent(level);
            string += e + "\n";
            if (e.getEndVertex() != null) {
                string += printTree(e.getEndVertex(), level + 1);
            }
        }

        return string;
    }

    private String printIndent(int level) {
        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "\t";
        }
        return indent;
    }
}
