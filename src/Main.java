
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.GeneralizedJaccard;
import org.simmetrics.tokenizers.Tokenizers;

public class Main {

    /**
     * It returns a gram size to be adopted.
     *
     * The values were calculated based on the media of the English word (8)
     * with a standard deviation of 2 between * grams
     *
     * @param wordLength Word's length.
     * @return The gram's size to be adopted by the word w.
     */
    public static int getGramSize(int wordLength) {
        //return (10 + w) / 6;
        // [1-5]
        if (wordLength < 6) {
            return 1;
        }
        // [6-10]
        if (wordLength < 11) {
            return 2;
        }
        // [11-15]
        if (wordLength < 16) {
            return 3;
        }
        // [15+]
        return 4;
    }

    /**
     * Compares the Strings with the standard Jaccard method without
     * pre-processing.
     *
     * @param ref1 String 1 to be compared.
     * @param ref2 String 2 to be compared.
     * @param ngram Size of the grams to be generated.
     * @return The measure of similarity.
     */
    public static float jaccard(String ref1, String ref2, int ngram) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();
        return metric.compare(ref1, ref2);
    }

    /**
     * It is the percentual difference of letters between the compared words.
     *
     * @param ref1 Word 1
     * @param ref2 Word 2
     * @return Absolute difference between the letters of the words.
     */
    public static double absoluteDifference(String ref1, String ref2) {
        //Lowercase ref1 and ref2
        ref1 = ref1.toLowerCase();
        ref2 = ref2.toLowerCase();

        //Calculates the amount of occurrences of each letter in ref1
        int[] vref1 = new int[256];
        for (int i = 0; i < ref1.length(); i++) {
            vref1[ref1.charAt(i)]++;
        }

        //Calculates the amount of occurrences of each letter in ref2
        int[] vref2 = new int[256];
        for (int i = 0; i < ref2.length(); i++) {
            vref2[ref2.charAt(i)]++;
        }
        //Make up the difference in modulus of each letter of the words
        int sum = 0;
        for (int i = 0; i < 256; i++) {
            sum = sum + Math.abs(vref1[i] - vref2[i]);
        }
        //Calculates the mean of the difference by the mean size
        double partial = sum / ((double) (ref1.length() + ref2.length()));

        return 1 - partial;
    }

    /**
     * Returns the position of the correct word in the list.
     *
     * @param rightWord Word to be verified.
     * @param list List with words.
     *
     * @return Position the correct word in the list or -1.
     */
    public static int bestPosition(String rightWord, ArrayList<Word> list) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (rightWord.equalsIgnoreCase(list.get(i).getWrongWord())) {
                return i;
            }
        }
        return position;
    }

    /**
     * Prints the list data on the screen.
     *
     * @param list List to be displayed.
     * @param rightWord String of the correct word.
     * @param method Method to be used in the analysis.
     */
    public static void imprime(ArrayList<Word> list, String rightWord, int method) {
        for (int i = 0; i < 10; i++) {
            System.out.println(" top(" + (i + 1) + ") gram(" + method + ")= " + list.get(i).getWrongWord() + " = " + list.get(i).getResult());
        }
        System.out.println("  > " + rightWord + " at position = " + (bestPosition(rightWord, list) + 1));
        System.out.println("");
    }

    /**
     * Datetime formated.
     *
     * @return String with date and time in a specific format to be used in the
     * output filename.
     */
    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Loads of dictionary word in the memory to accelerate the test execution
     *
     * @return List of dicionary.
     */
    public static List<String> loadDictionary() {
        //Defines the String string of the dictionary
        List<String> list = new ArrayList<>(194433);
        java.io.Reader input;
        try {
            input = new FileReader("dicionario_en.txt");
            BufferedReader reader = new BufferedReader(input);
            String wordDicitionary = reader.readLine();
            while (wordDicitionary != null) {
                list.add(wordDicitionary);
                wordDicitionary = reader.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return list;
    }

    /**
     * The method is used to calculate the relative difference of length between
     * two words.
     *
     * @param rightWord String of the correct word.
     * @param word Another word.
     * @return The value of the relative difference.
     */
    private static double lengthDifference(String rightWord, String word) {
        return Math.abs(rightWord.length() - word.length()) / (double) rightWord.length();
    }

    /**
     * Main method.
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // ----------------------------------------------------------------------------------------------------
        // SET PRINT METHOD
        // ----------------------------------------------------------------------------------------------------
        boolean print = true;

        //output file
        PrintWriter out = null;
        //treats output on screen or file
        if (print) {
            String file = "output_" + getDateTime() + ".csv";
            out = new PrintWriter(new FileWriter(file));
            System.out.println(">>>> Generating Output in File " + file);

            //HEADER OF THE OUTPUT FILE
            out.println("right Word;type word;px;p1;p2;p3;p4;p1_lx;p1_l1;p1_l2;p1_l3;p1_l4");
            out.flush();
        } else {
            System.out.println(">>>> OUTPUT ON THE SCREEN");
        }

        // ----------------------------------------------------------------------------------------------------
        // Dataset downloaded at https://www.dcs.bbk.ac.uk/~ROGER/corpora.html
        // ---------------------------------------------------------------------------------------------------- 
        java.io.Reader input = new FileReader("base_teste_en.txt");
        BufferedReader reader = new BufferedReader(input);
        String word = reader.readLine();

        List<String> dictionary = loadDictionary();

        while (word != null) {

            // If it is a target word
            if (word.charAt(0) == '$') {

                //remove the $ symbol
                String rightWord = word.substring(1, word.length());

                //Incorrect word reading
                word = reader.readLine();

                // While there is a wrong word
                while ((word != null) && (word.charAt(0) != '$')) {

                    // If two words are much different, so the combination wouldn't be tested.
                    if (lengthDifference(rightWord, word) < 0.5) {

                        if (print == false) {
                            System.out.println("right = " + rightWord + "(" + rightWord.length() + ") / analyzed =" + word + "(" + word.length() + ") / difAbs=" + absoluteDifference(rightWord, word) + "\n");
                        }

                        //Used as a similarity ranking for each method
                        ArrayList<Word> bestList1 = new ArrayList<>();
                        ArrayList<Word> bestList2 = new ArrayList<>();
                        ArrayList<Word> bestList3 = new ArrayList<>();
                        ArrayList<Word> bestList4 = new ArrayList<>();
                        ArrayList<Word> bestListX = new ArrayList<>();

                        //Scrolls through the dictionary list.
                        for (int i = 0; i < dictionary.size(); i++) {

                            //take the word i from the dictionary list
                            String dictionaryWord = dictionary.get(i);

                            // Below, the calculation of similarities between the wrong word and the current dictionary word
                            double result1 = jaccard(word, dictionaryWord, 1);
                            bestList1.add(new Word(word, dictionaryWord, result1, 1));

                            double result2 = jaccard(word, dictionaryWord, 2);
                            bestList2.add(new Word(word, dictionaryWord, result2, 2));

                            double result3 = jaccard(word, dictionaryWord, 3);
                            bestList3.add(new Word(word, dictionaryWord, result3, 3));

                            double result4 = jaccard(word, dictionaryWord, 4);
                            bestList4.add(new Word(word, dictionaryWord, result4, 4));

                            // --------------------------------------------------------------------------
                            // X-GRAM Execution
                            // --------------------------------------------------------------------------
                            Double absoluteDifference = absoluteDifference(word, dictionaryWord);
                            int gramSize = getGramSize(word.length());

                            if (absoluteDifference > 0.6) {
                                switch (gramSize) {
                                    case 1:
                                        bestListX.add(new Word(word, dictionaryWord, result1 * absoluteDifference, 1));
                                        break;
                                    case 2:
                                        bestListX.add(new Word(word, dictionaryWord, result2 * absoluteDifference, 2));
                                        break;
                                    case 3:
                                        bestListX.add(new Word(word, dictionaryWord, result3 * absoluteDifference, 3));
                                        break;
                                    case 4:
                                        bestListX.add(new Word(word, dictionaryWord, result4 * absoluteDifference, 4));
                                        break;
                                }
                            } else {
                                bestListX.add(new Word(word, dictionaryWord, 0, gramSize));
                            }

                            //System.out.println("WORD " + word + " DICTIONARY " + dictionaryWord + " GRAM: " + gramSize + " AD: " + absoluteDifference);
                            // --------------------------------------------------------------------------
                        }

                        //Sorts lists of results
                        Collections.sort(bestListX);
                        Collections.sort(bestList1);
                        Collections.sort(bestList2);
                        Collections.sort(bestList3);
                        Collections.sort(bestList4);

                        //treats output on screen or file
                        if (print == false) {
                            imprime(bestListX, rightWord, 0);
                            imprime(bestList1, rightWord, 1);
                            imprime(bestList2, rightWord, 2);
                            imprime(bestList3, rightWord, 3);
                            imprime(bestList4, rightWord, 4);
                        } else {
                            out.println(rightWord + ";" + word
                                    //The first of each list
                                    + ";" + (bestPosition(rightWord, bestListX) + 1)
                                    + ";" + (bestPosition(rightWord, bestList1) + 1)
                                    + ";" + (bestPosition(rightWord, bestList2) + 1)
                                    + ";" + (bestPosition(rightWord, bestList3) + 1)
                                    + ";" + (bestPosition(rightWord, bestList4) + 1)
                                    + ";" + bestListX.get(0).getWrongWord()
                                    + ";" + bestList1.get(0).getWrongWord()
                                    + ";" + bestList2.get(0).getWrongWord()
                                    + ";" + bestList3.get(0).getWrongWord()
                                    + ";" + bestList4.get(0).getWrongWord());
                        }
                    }// if word size
                    word = reader.readLine();
                }//while misspelled words
            }//if $
            //Send buffer to file
            if (print) {
                out.flush();
            }
        }//END while dictionary
        System.out.println(">>>> END OF EXECUTION.");
    }
}
