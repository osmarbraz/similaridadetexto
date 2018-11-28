
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
     * Calcula o tamanho do gram considerando o tamanho da palavra.
     *
     * @param w Tamanho de uma palavra.
     * @return O tamanho do gram considerando w.
     */
    public static int formula(int w) {
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
        //Guarda da diferença absoluta
        double diferencaAbsoluta = diferencaAbsoluta(ref1, ref2);
        //Verifica a diferença absoluta
        if (diferencaAbsoluta > 0.6) {
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
            medida = medida * diferencaAbsoluta;
            //Retorna o percentual
            return medida;
        } else {
            return 0;
        }
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
        Simplifier simplificador = Simplifiers.toLowerCase();
        ref1 = simplificador.simplify(ref1);
        ref2 = simplificador.simplify(ref2);

        //Remove não palavras
        simplificador = Simplifiers.replaceNonWord(" ");
        ref1 = simplificador.simplify(ref1);
        ref2 = simplificador.simplify(ref2);

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
        StringMetric metrica
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();
        return metrica.compare(ref1, ref2);
    }

    /**
     * Calcula a diferença absoluta entre palavras.
     *
     * @param ref1 Palavra 1
     * @param ref2 Palavra 2
     * @return Diferença absoluta entre as letras das palavras.
     */
    public static double diferencaAbsoluta(String ref1, String ref2) {
        //Calcula o número de ocorrências de cada letra da palavra 1
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
        double parcial = soma / ((double) ((ref1.length() + ref2.length()) / 2.0));

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
            if (correta.equals(lista.get(i).getPalavraErrada())) {
                return i;
            }
        }
        return melhor;
    }

    /**
     * Imprime na tela os dados da lista.
     *
     * @param lista Lista a ser exibida.
     * @param palavraCorreta String da palavra correta.
     * @param metodo Método a ser utilizado na analise.
     */
    public static void imprime(ArrayList<Palavra> lista, String palavraCorreta, int metodo) {
        for (int i = 0; i < 10; i++) {
            System.out.println(" top(" + (i + 1) + ") gram(" + metodo + ")= " + lista.get(i).getPalavraErrada() + " = " + lista.get(i).getResultado());
        }
        System.out.println("  > " + palavraCorreta + " na posição = " + (posicaoMelhor(palavraCorreta, lista) + 1));
        System.out.println("");
    }

    /**
     * Retorna a data e hora concatenados.
     *
     * @return String com data e hora concatenados.
     */
    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static List carregarDicionario() throws FileNotFoundException, IOException {
        //Define a lista de String do dicionário
        List<String> lista = new ArrayList<>(194433);
        //Abre o arquivo do dicionário
        java.io.Reader input = new FileReader("dicionario_en.txt");
        //Instancia o leitor do arquivo do dicionário
        BufferedReader reader = new BufferedReader(input);
        //Leitura da primeira palavra do dicionário em inglês
        String palavraDicionario = reader.readLine();
        //Leitura palavras corretas
        while (palavraDicionario != null) {
            //Adiciona a String a lista
            lista.add(palavraDicionario);

            //Leitura próxima palavra do dicionário
            palavraDicionario = reader.readLine();
        }
        //Retorna a lista com o dicionário de palavras
        return lista;
    }

    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        
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

        
        // Base de dados de teste
        // https://www.dcs.bbk.ac.uk/~ROGER/corpora.html

        //Abre o arquivo a base de testes
        java.io.Reader input = new FileReader("base_teste_en.txt");
        BufferedReader reader = new BufferedReader(input);
        String palavra = reader.readLine();

        //Carrega o dicionário para uma lista
        List<String> dicionario = carregarDicionario();

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

                    //50% de diferença não testa                    
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
                        
                        //Gera o gram da palavra
                        String xgram = geraGram(palavra, 0);

                        //Percorre a lista de palavras do dicionário
                        for (int i = 0; i < dicionario.size(); i++) {
                            //Recupera a palavra do dicionário
                            String palavraDicionario = dicionario.get(i);

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
            if (imprimir) {
                out.flush();
            }
        }//while dicionario        
    }
}
