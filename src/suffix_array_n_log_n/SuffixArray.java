package suffix_array_n_log_n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SuffixArray {

    public static final char TERMINATING_CHAR = '$';
    public Index[] equivalency;
    public String word;
    private Suffix[] suffixArray;
    private Map<Integer, Integer> shiftSortedIndexMap = null;
    private Map<Integer, List<Bucket>> levelBucketMap = new TreeMap<Integer, List<Bucket>>();


    public void addWord(String word) {
        this.word = word;
        int size = word.length();
        suffixArray = new Suffix[size];
        equivalency = new Index[size];
        int wordIndex = 0;

        for (int shift = 0; shift < size; shift++) {
            suffixArray[shift] = new Suffix(wordIndex, shift, this);
            if (word.charAt(shift) == TERMINATING_CHAR) {
                wordIndex++;
            }
        }
    }


    public void sortSuffixes(int startIndex, int endIndex, int level) {
        final Bucket firstBucket = new Bucket();
        firstBucket.start = startIndex;
        firstBucket.end = endIndex;
        levelBucketMap.put(1, new ArrayList<Bucket>() {{
            add(firstBucket);
        }});
        while (level < word.length()) {
            if (levelBucketMap.get(level) != null) {
                for (Bucket bucket : levelBucketMap.get(level)) {
                    startIndex = findBucketsAndSort(bucket.start, bucket.end, level);
                }
            }
            level = level << 1;
        }
    }


    private int findBucketsAndSort(int startIndex, int endIndex, int level) {
        while (startIndex < endIndex) {
            final int pos = level - 1;
            if (pos < word.length() - suffixArray[startIndex].shift) {
                char currentChar = suffixArray[startIndex].getCharAt(pos, word);
                Bucket bucket = findBucket(startIndex, pos, currentChar);
                if (bucket.size() > 0) {
                    sortBucket(bucket, level);
                }
                startIndex = bucket.end + 1;
            } else {
                startIndex++;
            }
        }
        return startIndex;
    }


    private Bucket findBucket(int startIndex, int level, char currentChar) {
        Bucket bucket = new Bucket();
        bucket.start = startIndex;
        while ((startIndex < word.length() - 1) && (suffixArray[startIndex + 1].getCharAt(level, word) == currentChar)) {
            startIndex++;
        }
        bucket.end = startIndex;
        return bucket;
    }


    private void sortBucket(Bucket bucket, int level) {
        Suffix[] sortedBucket = mergeSort(Arrays.copyOfRange(suffixArray, bucket.start, bucket.end + 1), level);
        int pos = 0;
        for (int i = bucket.start; i <= bucket.end; i++) {
            suffixArray[i] = sortedBucket[pos++];
            shiftSortedIndexMap.put(suffixArray[i].shift, i);
        }
        List<Bucket> buckets = levelBucketMap.get(level << 1);
        if (buckets == null) {
            buckets = new ArrayList<Bucket>();
            levelBucketMap.put(level << 1, buckets);
        }
        buckets.add(bucket);
    }


    public void performPreparationSort(final int pos) {
        suffixArray = mergeSort(suffixArray, pos);

        shiftSortedIndexMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < equivalency.length; i++) {
            shiftSortedIndexMap.put(suffixArray[i].getShift(), i);
        }
    }


    private Suffix[] mergeSort(Suffix[] bucket, int level) {
        Suffix[] left;
        Suffix[] right;

        if (bucket.length <= 1) {
            return bucket;
        } else {
            left = Arrays.copyOfRange(bucket, 0, bucket.length / 2);
            right = Arrays.copyOfRange(bucket, bucket.length / 2, bucket.length);

            left = mergeSort(left, level);
            right = mergeSort(right, level);

            return merge(left, right, level);
        }
    }


    private Suffix[] merge(Suffix[] left, Suffix[] right, int level) {
        int length = left.length + right.length;
        Suffix[] result = new Suffix[length];
        int l = 0;
        int r = 0;

        for (int i = 0; i < length; i++) {
            if ((l < left.length)) {
                if (r < right.length) {
                    boolean isLeftLessThanRight;
                    if (shiftSortedIndexMap == null) {
                        isLeftLessThanRight = left[l].getCharAt(level, word) < right[r].getCharAt(level, word);
                    } else {
                        isLeftLessThanRight =
                                shiftSortedIndexMap.get(getLevelShift(left[l].shift, level, word.length()))
                                < shiftSortedIndexMap.get(getLevelShift(right[r].shift, level, word.length()));
                    }

                    if (isLeftLessThanRight) {
                        result[i] = left[l];
                        l++;
                    } else {
                        result[i] = right[r];
                        r++;
                    }
                } else {
                    result[i] = left[l];
                    l++;
                }
            } else {
                result[i] = right[r];
                r++;
            }
        }

        return result;
    }


    private int getLevelShift(int shift, int level, int wordLength) {
        int truePos = shift + level;
        return truePos >= wordLength ? truePos - wordLength : truePos;
    }


    public Suffix[] getSuffixes() {
        return suffixArray;
    }
}
