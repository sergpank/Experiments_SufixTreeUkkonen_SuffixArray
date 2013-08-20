package suffix_array_n_log_n;

public class Suffix {
    int wordIndex;
    int shift;
    SuffixArray sa;

    public Suffix(SuffixArray sa) {
        this.sa = sa;
    }

    Suffix(int wordIndex, int shift, SuffixArray sa) {
        this.wordIndex = wordIndex;
        this.shift = shift;
        this.sa = sa;
    }

    public char getCharAt(int pos, String word) {
        final int truePos = getLevelShift(pos, word.length());
        return word.charAt(truePos);
    }

    private int getLevelShift(int level, int wordLength){
        int truePos = shift + level;
        return truePos >= wordLength ? truePos - wordLength : truePos;
    }

    public int getWordIndex() {
        return wordIndex;
    }

    public int getShift() {
        return shift;
    }


    @Override
    public String toString() {
        return sa.word.substring(shift);
    }
}