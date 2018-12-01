
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
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;

public class Principal {

    /**
     * It returns a gram size to be adopted. The values were calculated based on
     * the media of the English word (8) with a standard deviation of 2 between
     * grams
     *
     * @param wordLength word's length.
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
     * It calculates the similarity grade between two given strings.
     *
     * @param wrongWord
     * @param dictionaryWord
     * @return It is the grade of similarity between both words.
     *
     * public static double similaridadeAO(String wrongWord, String
     * dictionaryWord) {
     *
     * Double absoluteDifference = absoluteDifference(wrongWord,
     * dictionaryWord);
     *
     * if (absoluteDifference > 0.6) {
     *
     * String xgram1 = generatesGram(wrongWord, 0);
     *
     * String xgram2 = generatesGram(dictionaryWord, 0);
     *
     * Tokenizer tokenizador1 = Tokenizers.whitespace();
     *
     * List<String> refs1 = tokenizador1.tokenizeToList(xgram1);
     *
     * Tokenizer tokenizador2 = Tokenizers.whitespace();
     *
     * List<String> refs2 = tokenizador2.tokenizeToList(xgram2);
     *
     * int comum = 0;
     *
     * int diferencaA = 0;
     *
     * for (String a : refs1) { if (refs2.contains(a)) { comum = comum + 1; }
     * else { diferencaA = diferencaA + 1; } }
     *
     * int diferencaB = Math.abs(refs2.size() - comum);
     *
     * double total = comum + diferencaA + diferencaB;
     *
     * double medida = comum / total; medida = medida * absoluteDifference;
     *
     * return medida; } else { return 0; } }
     */
    /**
     * Gera uma String de grams apartir de uma String.
     *
     * @param ref String a ser gerada os grams.
     * @return String de Grams.
     *
     * static int ultima;
     *
     * public static String generatesGram(String ref, int xgram) {
     *
     * Simplifier simplificador = Simplifiers.toLowerCase();
     *
     * ref = simplificador.simplify(ref);
     *
     * simplificador = Simplifiers.removeNonWord(""); //Cria o tokenizador de
     * Strings com o separador por espacos em branco Tokenizer tokenizador1 =
     * Tokenizers.whitespace(); //Gera um List dos grams da String List<String>
     * refs1 = tokenizador1.tokenizeToList(ref);
     *
     * //String de retorno dos grams String rgrams = ""; for (String a : refs1)
     * { a = simplificador.simplify(a); ///Tokens com 2 ou mais letras if
     * (a.length() > 1) { int ngram = 0; if (xgram == 0) { ngram =
     * getGramSize(a.length()); ultima = ngram; } else { ngram = xgram; }
     * Tokenizer tokenizador2 = Tokenizers.qGram(ngram); List<String> refs2 =
     * tokenizador2.tokenizeToList(a); for (String b : refs2) { rgrams = rgrams
     * + " " + b; } } } //Remove os espacos em branco das extremidades rgrams =
     * rgrams.trim(); return rgrams; }
     */
    /**
     * Compara as Strings com o método de Jaccard padrão sem pre processamento.
     *
     * @param ref1 String 1 a ser comparada.
     * @param ref2 String 2 a ser comparada.
     * @param ngram Tamanho dos grams a ser gerado.
     * @return
     */
    public static float jaccardPadrao(String ref1, String ref2, int ngram) {
        StringMetric metrica
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();
        return metrica.compare(ref1, ref2);
    }

    /**
     * It is the percentual difference of letters between the compared words.
     *
     * @param ref1 Palavra 1
     * @param ref2 Palavra 2
     * @return Diferença absoluta entre as letras das palavras.
     */
    public static double absoluteDifference(String ref1, String ref2) {
        //Calcula qtd ocorrencias de cada letra da palavra 1

        ref1 = ref1.toLowerCase();
        ref2 = ref2.toLowerCase();

        int[] vref1 = new int[256];
        for (int i = 0; i < ref1.length(); i++) {
            vref1[ref1.charAt(i)]++;
        }

        //Calcula o número de ocorrências de cada letra da palavra 2
        int[] vref2 = new int[256];
        for (int i = 0; i < ref2.length(); i++) {
            vref2[ref2.charAt(i)]++;
        }
        //Realiza soma a diferença em módulo de cada letra das palavras
        int soma = 0;
        for (int i = 0; i < 256; i++) {
            soma = soma + Math.abs(vref1[i] - vref2[i]);
        }
        //Calcula a média da diferença pelo tamanho médio.
        double parcial = soma / ((double) (ref1.length() + ref2.length()));

        return 1 - parcial;
    }

    /**
     * Retorna a posição da palavra correta na lista.
     *
     * @param correta Palavra a ser verificada.
     * @param lista Lista com as palavras.
     *
     * @return Possição da palavra correta na lista ou -1.
     */
    public static int posicaoMelhor(String correta, ArrayList<Palavra> lista) {
        int melhor = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (correta.equalsIgnoreCase(lista.get(i).getPalavraErrada())) {
                return i;
            }
        }
        return melhor;
    }

    /**
     * Imprime na tela os dados da lista.
     *
     * @param list Lista a ser exibida.
     * @param rightWord String da palavra correta.
     * @param method Método a ser utilizado na analise.
     */
    public static void imprime(ArrayList<Palavra> list, String rightWord, int method) {
        for (int i = 0; i < 10; i++) {
            System.out.println(" top(" + (i + 1) + ") gram(" + method + ")= " + list.get(i).getPalavraErrada() + " = " + list.get(i).getResultado());
        }
        System.out.println("  > " + rightWord + " na posição = " + (posicaoMelhor(rightWord, list) + 1));
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
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> carregarDicionario() throws FileNotFoundException, IOException {
        //Define a lista de String do dicionario
        List<String> lista = new ArrayList<>(194433);
        java.io.Reader input = new FileReader("dicionario_en.txt");
        BufferedReader reader = new BufferedReader(input);
        String palavraDicionario = reader.readLine();
        while (palavraDicionario != null) {
            lista.add(palavraDicionario);
            palavraDicionario = reader.readLine();
        }
        return lista;
    }

    /**
     * The method is used to calculate the relative difference of length between
     * two words.
     *
     */
    private static double lengthDifference(String rightWord, String word) {
        return Math.abs(rightWord.length() - word.length()) / (double) rightWord.length();
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // ----------------------------------------------------------------------------------------------------
        // SET PRINT METHOD
        // ----------------------------------------------------------------------------------------------------
        boolean imprimir = true;

        PrintWriter out = null;
        if (imprimir) {
            String arquivo = "saida_" + getDateTime() + ".csv";
            out = new PrintWriter(new FileWriter(arquivo));
            System.out.println(">>>> Gerando saida no arquivo " + arquivo);

            //HEADER OF THE OUTPUT FILE
            out.println("palavra correta;palavra digitada;px;p1;p2;p3;p4;p1_lx;p1_l1;p1_l2;p1_l3;p1_l4");
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

        List<String> dictionary = carregarDicionario();

        while (word != null) {

            // If it is a target word
            if (word.charAt(0) == '$') {

                String rightWord = word.substring(1, word.length());

                word = reader.readLine();

                // While there is a wrong word
                while ((word != null) && (word.charAt(0) != '$')) {

                    // If two words are much different, so the combination wouldn't be tested.
                    if (lengthDifference(rightWord, word) < 0.5) {

                        if (imprimir == false) {
                            System.out.println("correta = " + rightWord + "(" + rightWord.length() + ") / analisada =" + word + "(" + word.length() + ") / difAbs=" + absoluteDifference(rightWord, word) + "\n");
                        }

                        //Used as a similarity ranking for each method
                        ArrayList<Palavra> melhores1 = new ArrayList<Palavra>();
                        ArrayList<Palavra> melhores2 = new ArrayList<Palavra>();
                        ArrayList<Palavra> melhores3 = new ArrayList<Palavra>();
                        ArrayList<Palavra> melhores4 = new ArrayList<Palavra>();
                        ArrayList<Palavra> melhoresX = new ArrayList<Palavra>();

                        for (int i = 0; i < dictionary.size(); i++) {

                            String palavraDicionario = dictionary.get(i);

                            // Below, the calculation of similarities between the wrong word and the current dictionary word
                            double resultado1 = jaccardPadrao(word, palavraDicionario, 1);
                            melhores1.add(new Palavra(word, palavraDicionario, resultado1, 1));

                            double resultado2 = jaccardPadrao(word, palavraDicionario, 2);
                            melhores2.add(new Palavra(word, palavraDicionario, resultado2, 2));

                            double resultado3 = jaccardPadrao(word, palavraDicionario, 3);
                            melhores3.add(new Palavra(word, palavraDicionario, resultado3, 3));

                            double resultado4 = jaccardPadrao(word, palavraDicionario, 4);
                            melhores4.add(new Palavra(word, palavraDicionario, resultado4, 4));

                            // --------------------------------------------------------------------------
                            // X-GRAM Execution
                            // --------------------------------------------------------------------------
                            Double absoluteDifference = absoluteDifference(word, palavraDicionario);
                            int gramSize = getGramSize(word.length());

                            if (absoluteDifference > 0.6) {
                                switch (gramSize) {
                                    case 1:
                                        melhoresX.add(new Palavra(word, palavraDicionario, resultado1 * absoluteDifference, 1));
                                        break;
                                    case 2:
                                        melhoresX.add(new Palavra(word, palavraDicionario, resultado2 * absoluteDifference, 2));
                                        break;
                                    case 3:
                                        melhoresX.add(new Palavra(word, palavraDicionario, resultado3 * absoluteDifference, 3));
                                        break;
                                    case 4:
                                        melhoresX.add(new Palavra(word, palavraDicionario, resultado4 * absoluteDifference, 4));
                                        break;
                                }
                            } else {
                                melhoresX.add(new Palavra(word, palavraDicionario, 0, gramSize));
                            }

                            //System.out.println("WORD " + word + " DICTIONARY " + palavraDicionario + " GRAM: " + gramSize + " AD: " + absoluteDifference);
                            // --------------------------------------------------------------------------
                        }

                        //Ordena as listas dos resultados
                        Collections.sort(melhoresX);
                        Collections.sort(melhores1);
                        Collections.sort(melhores2);
                        Collections.sort(melhores3);
                        Collections.sort(melhores4);

                        if (imprimir == false) {
                            imprime(melhoresX, rightWord, 0);
                            imprime(melhores1, rightWord, 1);
                            imprime(melhores2, rightWord, 2);
                            imprime(melhores3, rightWord, 3);
                            imprime(melhores4, rightWord, 4);
                        } else {
                            out.println(rightWord + ";" + word
                                    //O primeiro de cada lista
                                    + ";" + (posicaoMelhor(rightWord, melhoresX) + 1)
                                    + ";" + (posicaoMelhor(rightWord, melhores1) + 1)
                                    + ";" + (posicaoMelhor(rightWord, melhores2) + 1)
                                    + ";" + (posicaoMelhor(rightWord, melhores3) + 1)
                                    + ";" + (posicaoMelhor(rightWord, melhores4) + 1)
                                    + ";" + melhoresX.get(0).getPalavraErrada()
                                    + ";" + melhores1.get(0).getPalavraErrada()
                                    + ";" + melhores2.get(0).getPalavraErrada()
                                    + ";" + melhores3.get(0).getPalavraErrada()
                                    + ";" + melhores4.get(0).getPalavraErrada());
                        }
                    }// if tamanho palavra
                    word = reader.readLine();
                }//while palavras incorretas
            }//if $
            //Envia buffer para o arquivo
            if (imprimir) {
                out.flush();
            }
        }//END while dictionary

        System.out.println(">>>> END OF EXECUTION.");
    }
}
