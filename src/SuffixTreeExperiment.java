import suffix_tree_ukkonen.SuffixTree;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

public class SuffixTreeExperiment {

    public static final int STRINGS_NR = 1;

    public static void main(String[] args) throws IOException {
        SuffixTree trie = new SuffixTree();

        String commonSubsequence = "uasivyoiuesbvryaosiudfhasldkjfnhcasdjhfasudvoaewiuryoaisudfyvudashfvioaushdf" +
                "oiuashdfoiuasdfhcnuscdhfoasuvhdfoasiudhfcnoasdfihvabsduhfcanosdufhvasodiufhnacsdufhvashodfuiacn" +
                "shdofuhfasodufhsadfiuhsuvhanvaosviduxlnvuretyqreqpweryxewmruvbszxcvhgasdfyqwerasdfljhcxvzxcvuaz" +
                "xcnvvberuqwoerbvasdfhavsjdkfhasdoufhasdoufsydfouawyeofuiavwyeoasiudhfasdhfaosdufhoaweiufadufhas" +
                "dfhasncaudsfhauertyabvrutyasodufhvahsdfuahsdiufhaeryaoiusydroavsudofhasdfjhaoseiuryabsdouraysue" +
                "ryaboseiuryaosdiurhasdjfahseuiryaoeiurayvsdr";

        long startTime = System.nanoTime();
        int length = 9500;
        String[] randomStrings = new String[STRINGS_NR];
        for(int i = 0; i < STRINGS_NR; i++){
            String word = generateRandomString(length);
            int splitPosition = new Random().nextInt(length);
            String half1 = word.substring(0, splitPosition);
            String half2 = word.substring(splitPosition, length);
            StringBuilder sb = new StringBuilder();
            sb.append(half1);
            sb.append(commonSubsequence);
            sb.append(half2);
            word = sb.toString();
            System.out.println(word);
            randomStrings[i] = word;
        }

        long stringGenerationTime = System.nanoTime();
        System.out.println("\nstrings generation: " + (nanoToSeconds(stringGenerationTime - startTime)) + "\n");

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < STRINGS_NR; i++){
            sb.append(randomStrings[i]);
            sb.append('$');
        }
        trie.addWord(sb.toString());

        long endInsertTime = System.nanoTime();
        System.out.println("insert: " + (nanoToSeconds(endInsertTime - stringGenerationTime)) + "\n");

        String longestSubstring = trie.getLongestSubstring();

        if(longestSubstring.charAt(longestSubstring.length() - 1) == '@'){
            longestSubstring = longestSubstring.substring(0, longestSubstring.length() - 1);
        }

        System.out.println("longest substring: " + longestSubstring);
        System.out.println("\nis right: " + (longestSubstring.equalsIgnoreCase(commonSubsequence) + "").toUpperCase());
        long endSearchTime = System.nanoTime();
        System.out.println("\nsearch: " + (nanoToSeconds(endSearchTime - endInsertTime)) + "\n");

        System.out.println();
    }

    private static String nanoToSeconds(long nano){
        long nanos  = nano % 1000;
        long micro  = (nano - nanos) / 1000;
        long micros = micro % 1000;
        long mili   = (micro - micros) / 1000;
        long milis  = mili % 1000;
        long second = (mili - milis) / 1000;

        return MessageFormat.format("{0}s {1}ms {2}us {3}ns", second, milis, micros, nanos);
    }

    private static String generateRandomString(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random(new Date().getTime());
        for (int i = 0; i < length; i++){
            sb.append((char)(97 + random.nextInt(25)));
        }
        return sb.toString();
    }
}