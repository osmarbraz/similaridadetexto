
import java.io.BufferedReader;
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
     * Calcula o tamanho do gram considerando o tamanho da palavra.
     *
     * @param w Tamanho de uma palavra.
     * @return o tamanho do gram a ser considerado para uma palavra de tamanho
     * w.
     */
    public static int formula(int w) {
        //return (10 + w) / 6;
        return (10 + w) / 6;
    }

    /**
     * Executa a comparação de duas Strings.
     *
     * @param ref1 String 1 a ser comparada.
     * @param ref2 String 2 a ser comparada.
     * @return O percentual de semelhança das Strings.
     */
    public static double similaridadeAO(String ref1, String ref2) {
        //Cria o tokenizador de String com o separador por espaços em branco
        Tokenizer tokenizador1 = Tokenizers.whitespace();

        //Gera um List dos grams da String 1
        List<String> refs1 = tokenizador1.tokenizeToList(ref1);

        //Cria o tokenizador de String com o separador por espaços em branco
        Tokenizer tokenizador2 = Tokenizers.whitespace();
        //Gera um List dos grams da String 1
        List<String> refs2 = tokenizador2.tokenizeToList(ref2);

        //Acumula a quantidade de Strings semelhantes entre as Listas
        int comum = 0;
        //Acumula a quantidade de Strings difeentes entre as Listas
        int diferencaA = 0;
        //Faz a comparação das Strings da lista 1 com a lista 2
        for (String a : refs1) {
            if (refs2.contains(a)) {
                comum = comum + 1;
            } else {
                diferencaA = diferencaA + 1;
            }
        }

        //Calcula a diferença para a lista b
        int diferencaB = Math.abs(refs2.size() - comum);

        //Calcula o total de elementos distintos
        double total = comum + diferencaA + diferencaB;

        //Calcula o percentual de semelhança das listas
        double medida = comum / total;

        medida = medida * diferencaAbsoluta(ref1, ref2);
        //Retorna o percentual
        return medida;
    }

    /**
     * Gera uma String de grams apartir de uma String.
     *
     * @param ref String a ser gerada os grams.
     * @return String de Grams.
     */
    static int ultima;

    public static String geraGram(String ref, int xgram) {
        //Converte a String para minusculo
        Simplifier simplificador = Simplifiers.toLowerCase();
        ref = simplificador.simplify(ref);

        //Remove as não palavras
        simplificador = Simplifiers.removeNonWord("");
        //ref = simplificador.simplify(ref);

        //Cria o tokenizador de String com o separador por espaços em branco
        Tokenizer tokenizador1 = Tokenizers.whitespace();
        //Gera um List dos grams da String
        List<String> refs1 = tokenizador1.tokenizeToList(ref);

        //String de retorno dos grams
        String rgrams = "";
        for (String a : refs1) {
            a = simplificador.simplify(a);
            ///Tokens com 2 ou mais letras
            if (a.length() > 1) {
                //System.out.println("\ntoken = " + a );
                int ngram = 0;

                if (xgram == 0) {
                    ngram = formula(a.length());
                    ultima = ngram;
                } else {
                    ngram = xgram;
                }

                Tokenizer tokenizador2 = Tokenizers.qGram(ngram);
                List<String> refs2 = tokenizador2.tokenizeToList(a);
                for (String b : refs2) {
                    rgrams = rgrams + " " + b;
                }
            }
        }
        //Remove os espaços em branco das extremidade
        rgrams = rgrams.trim();
        return rgrams;
    }

    /**
     * Compara as Strings com o método de Jaccard padrão sem pre processamento.
     *
     * @param ref1 String 1 a ser comparada.
     * @param ref2 String 2 a ser comparada.
     * @param ngram Tamanho dos grams a ser gerado.
     * @return
     */
    public static float jaccardModificado(String ref1, String ref2, int ngram) {
        //Converte a String para minusculo
        Simplifier simplificador2 = Simplifiers.toLowerCase();
        ref1 = simplificador2.simplify(ref1);
        ref2 = simplificador2.simplify(ref2);

        //Remove não palavras
        simplificador2 = Simplifiers.replaceNonWord(" ");
        ref1 = simplificador2.simplify(ref1);
        ref2 = simplificador2.simplify(ref2);

        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();
        return metric.compare(ref1, ref2);
    }

    /**
     * Compara as Strings com o método de Jaccard padrão sem pre processamento.
     *
     * @param ref1 String 1 a ser comparada.
     * @param ref2 String 2 a ser comparada.
     * @param ngram Tamanho dos grams a ser gerado.
     * @return
     */
    public static float jaccardPadrao(String ref1, String ref2, int ngram) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();
        return metric.compare(ref1, ref2);
    }

    /**
     * Suponha as strings x=arara e y=araxá. Declare uma matriz de 256 posições
     * tipo inteiro para cada string. Para cada caractere, incremente a posição
     * correspondente ao seu char code. Após processar as duas strings, calcule
     * a diferença absoluta (módulo) de cada índice, some os resultados e divida
     * pela média de tamanho das das strings. No caso: arara: a=3 r=2 (TODAS as
     * outras posições valerão 0! araxá: a=2 r=1 x=1 á=1 Similaridade:
     * (|3-2|+|2-1|+|0-1|+|0-1|)/((5+5)/2)= 4/5=0.8
     *
     * @param ref1
     * @param ref2
     * @return
     */
    public static double diferencaAbsoluta(String ref1, String ref2) {
        int[] vref1 = new int[256];
        for (int i = 0; i < ref1.length(); i++) {
            vref1[ref1.charAt(i)]++;
        }

        int[] vref2 = new int[256];
        for (int i = 0; i < ref2.length(); i++) {
            vref2[ref2.charAt(i)]++;
        }

        int soma = 0;
        for (int i = 0; i < 256; i++) {
            soma = soma + Math.abs(vref1[i] - vref2[i]);
        }

        double media = soma / ((double) (ref1.length() + ref2.length()) / 2.0);
        return 1 - media;
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
            if (correta.equals(lista.get(i).getPalavraErrada())) {
                return i;
            }
        }
        return melhor;
    }

    /**
     * Imprime na tela os dados da lista.
     *
     * @param lista Lista a ser impressa.
     * @param metodo Método a ser analisado.
     */
    public static void imprime(ArrayList<Palavra> lista, String correta, int metodo) {
        for (int i = 0; i < 10; i++) {
            System.out.println(" top(" + (i + 1) + ") gram(" + metodo + ")= " + lista.get(i).getPalavraErrada() + " = " + lista.get(i).getResultado());
        }
        System.out.println("  > " + correta + " na posição = " + (posicaoMelhor(correta, lista)+1));
        System.out.println("");
    }

    /**
     * Retorna a data e hora concatenados.
     *
     * @return String com data e hora concatenado.
     */
    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // Base de dados
        // https://www.dcs.bbk.ac.uk/~ROGER/corpora.html
        
        //Controla a saída dos dados
        boolean imprimir = true;

        PrintWriter out = null;
        if (imprimir) {
            String arquivo = "saida_" + getDateTime() + ".csv";
            out = new PrintWriter(new FileWriter(arquivo));
            System.out.println(">>>> Gerando saída no arquivo " + arquivo);

            //Cabeçalho do arquivo            
            out.println("palavra correta;palavra digitada;px;p1;p2;p3;p4;p1_lx;p1_l1;p1_l2;p1_l3;p1_l4");
            out.flush();
        } else {
            System.out.println(">>>> Gerando saída em tela ");
        }

        //Abre o arquivo do dicionário
        java.io.Reader input = new FileReader("base_teste_en.txt");
        BufferedReader reader = new BufferedReader(input);
        String palavra = reader.readLine();

        //Leitura palavras corretas do dicionário
        while (palavra != null) {
            
            //Verifica se é uma palavra correta
            if (palavra.charAt(0) == '$') {
                
                //Retira o $ do início da palavra
                String correta = palavra.substring(1, palavra.length()).toLowerCase();
                
                //Leitura das palavras digitadas incorretamente
                palavra = reader.readLine().toLowerCase();
                
                //Leitura  das palavras incorretas.
                while ((palavra != null) && (palavra.charAt(0) != '$')) {
                    
                    //verifica o tamanho da palavra
                    int sub = correta.length() - palavra.length();
                    double x = Math.abs(sub) / (double) palavra.length();
                    if (x < 0.5) {
                        //Saída em tela
                        if (imprimir == false) {
                            System.out.println("correta = " + correta + "(" + correta.length() + ") / analisada =" + palavra + "(" + palavra.length() + ")\n SAO=" + similaridadeAO(geraGram(correta, 0), geraGram(palavra, 0)) + " / difAbs=" + diferencaAbsoluta(correta, palavra) + "\n");
                        }

                        //Listas para armazenar os resultados da comparação
                        ArrayList<Palavra> melhoresX = new ArrayList();
                        ArrayList<Palavra> melhores1 = new ArrayList();
                        ArrayList<Palavra> melhores2 = new ArrayList();
                        ArrayList<Palavra> melhores3 = new ArrayList();
                        ArrayList<Palavra> melhores4 = new ArrayList();
                                                
                        //Procura a palavra digitada errada no dicionario
                        java.io.Reader input1 = new FileReader("dicionario_en.txt");
                        BufferedReader reader1 = new BufferedReader(input1);
                
                        //Leitura da primeira palavra do dicionario em english
                        String palavraDicionario = reader1.readLine();

                        //Leitura palavras corretas
                        while (palavraDicionario != null) {
                            //System.out.println("Lendo dicionario");

                            //Gera o gram da palavra
                            String xgram = geraGram(palavra, 0);

                            //Executa as similaridades
                            double resultado = similaridadeAO(xgram, geraGram(palavraDicionario, ultima));
                            melhoresX.add(new Palavra(palavra, palavraDicionario, resultado, ultima));

                            resultado = jaccardModificado(palavra, palavraDicionario, 1);
                            melhores1.add(new Palavra(palavra, palavraDicionario, resultado, 1));

                            resultado = jaccardModificado(palavra, palavraDicionario, 2);
                            melhores2.add(new Palavra(palavra, palavraDicionario, resultado, 2));

                            resultado = jaccardModificado(palavra, palavraDicionario, 3);
                            melhores3.add(new Palavra(palavra, palavraDicionario, resultado, 3));

                            resultado = jaccardModificado(palavra, palavraDicionario, 4);
                            melhores4.add(new Palavra(palavra, palavraDicionario, resultado, 4));

                            //Leitura próxima palavra do dicionario
                            palavraDicionario = reader1.readLine();
                        }
                        
                        //Ordena as listas dos resultados
                        Collections.sort(melhoresX);
                        Collections.sort(melhores1);
                        Collections.sort(melhores2);
                        Collections.sort(melhores3);
                        Collections.sort(melhores4);

                        if (imprimir == false) {
                            imprime(melhoresX, correta, 0);
                            imprime(melhores1, correta, 1);
                            imprime(melhores2, correta, 2);
                            imprime(melhores3, correta, 3);
                            imprime(melhores4, correta, 4);
                        } else {
                            out.println(correta + ";" + palavra
                                    //O primeiro de cada lista
                                    + ";" + (posicaoMelhor(correta, melhoresX) + 1)
                                    + ";" + (posicaoMelhor(correta, melhores1) + 1)
                                    + ";" + (posicaoMelhor(correta, melhores2) + 1)
                                    + ";" + (posicaoMelhor(correta, melhores3) + 1)
                                    + ";" + (posicaoMelhor(correta, melhores4) + 1)
                                    + ";" + melhoresX.get(0).getPalavraErrada()
                                    + ";" + melhores1.get(0).getPalavraErrada()
                                    + ";" + melhores2.get(0).getPalavraErrada()
                                    + ";" + melhores3.get(0).getPalavraErrada()
                                    + ";" + melhores4.get(0).getPalavraErrada());
                        }
                    }// if tamanho palavra
                    palavra = reader.readLine();
                }//while palavras incorretas
            }//if $
            //Envia buffer para o arquivo
            if (imprimir){
                out.flush();
            }
        }//while dicionario        
    }
}