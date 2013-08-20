import suffix_array_n_log_n.Suffix;
import suffix_array_n_log_n.SuffixArray;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;

public class SuffixArrayExperiment {

    public static final int STRINGS_NR = 10;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Test Suffix array");
        frame.setSize(400, 300);
        JButton button = new JButton("TEST SUFFIX ARRAY");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    test();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        frame.add(button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void test() throws IOException {
        SuffixArray suffixArray = new SuffixArray();

        String commonSubsequence = "uasivyoiuesbvryaosiudfhasldkjfnhcasdjhfasudvoaewiuryoaisudfyvudashfvioaushdf" +
                "oiuashdfoiuasdfhcnuscdhfoasuvhdfoasiudhfcnoasdfihvabsduhfcanosdufhvasodiufhnacsdufhvashodfuiacn" +
                "shdofuhfasodufhsadfiuhsuvhanvaosviduxlnvuretyqreqpweryxewmruvbszxcvhgasdfyqwerasdfljhcxvzxcvuaz" +
                "xcnvvberuqwoerbvasdfhavsjdkfhasdoufhasdoufsydfouawyeofuiavwyeoasiudhfasdhfaosdufhoaweiufadufhas" +
                "dfhasncaudsfhauertyabvrutyasodufhvahsdfuahsdiufhaeryaoiusydroavsudofhasdfjhaoseiuryabsdouraysue" +
                "ryaboseiuryaosdiurhasdjfahseuiryaoeiurayvsdr";

        long startTime = System.nanoTime();
        int length = 500;
        String[] randomStrings = new String[STRINGS_NR];
        for (int i = 0; i < STRINGS_NR; i++) {
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
        sb.append("ababaccabdca$");
        sb.append("cbdacbcbabdcdc$");
//        for (String word : randomStrings) {
//            sb.append(word);
//            sb.append('$');
//        }
        suffixArray.addWord(sb.toString());

        long endSuffixGenerationTime = System.nanoTime();
        System.out.println("Suffix generation time: " + (nanoToSeconds(endSuffixGenerationTime - stringGenerationTime)) + "\n");

        suffixArray.performPreparationSort(0);

        long endSortingTime = System.nanoTime();
        System.out.println("Preparation sort time " + nanoToSeconds(endSortingTime - endSuffixGenerationTime) + "\n");


        suffixArray.sortSuffixes(0, sb.length() - 1, 1);
        long endTrueSortTime = System.nanoTime();
        System.out.println("True sort time: " + (nanoToSeconds(endTrueSortTime - endSortingTime)) + "\n");

        BufferedWriter bw = new BufferedWriter(new FileWriter("sorted suffixes.txt"));
        for (Suffix suffix : suffixArray.getSuffixes()) {
            bw.write(sb.toString().substring(suffix.getShift()));
            bw.newLine();
        }
        bw.close();

        long endDumpTime = System.nanoTime();
        System.out.println("Dump time: " + (nanoToSeconds(endDumpTime - endTrueSortTime)) + "\n");

        System.out.println();
    }

    private static String nanoToSeconds(long nano) {
        long nanos = nano % 1000;
        long micro = (nano - nanos) / 1000;
        long micros = micro % 1000;
        long mili = (micro - micros) / 1000;
        long milis = mili % 1000;
        long second = (mili - milis) / 1000;

        return MessageFormat.format("{0}s {1}ms {2}us {3}ns", second, milis, micros, nanos);
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random(new Date().getTime());
        for (int i = 0; i < length; i++) {
            sb.append((char) (97 + random.nextInt(25)));
        }
        return sb.toString();
    }


}