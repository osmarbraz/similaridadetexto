
/**
 * Class used to store the result of the comparison of the words and their result.
 */
public class Word implements Comparable {

    private String word;
    private String wrongWord;
    private double result;
    private int gram;

    /**
     * Constructor with parameters.
     *
     * @param word Correct word.
     * @param wrongWord Wrong word.
     * @param result Result of verification.
     * @param gram Gram size used in comparison.
     */
    public Word(String word, String wrongWord, double result, int gram) {
        this.word = word;
        this.wrongWord = wrongWord;
        this.result = result;
        this.gram = gram;
    }

    // gets´s e set´s
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWrongWord() {
        return wrongWord;
    }

    public void setWrongWord(String wrongWord) {
        this.wrongWord = wrongWord;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int getGram() {
        return gram;
    }

    public void setGram(int gram) {
        this.gram = gram;
    }

    /**
     * A method that compares the results.
     *
     * UUsed to sort list by collection.
     *
     * @param o Comparison object.
     *
     * @return If it is smaller, greater or equal.
     */
    @Override
    public int compareTo(Object o) {
        Word pa = (Word) o;
        if (this.result < pa.result) {
            return 1;
        }
        if (this.result > pa.result) {
            return -1;
        }
        if (this.result == pa.result) {
            return 0;
        }
        return -1;
    }
}