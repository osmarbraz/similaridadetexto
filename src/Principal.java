
import java.util.List;
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
     * @return o tamanho do gram a ser considerado para uma palavra de tamanho w.
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
        //Retorna o percentual        
        return medida;
    }

    /**
     * Gera uma String de grams apartir de uma String.
     *
     * @param ref String a ser gerada os grams.
     * @return String de Grams.
     */
    public static String geraGram(String ref) {
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
                int ngram = formula(a.length());
                Tokenizer tokenizador2 = Tokenizers.qGram(ngram);
                List<String> refs2 = tokenizador2.tokenizeToList(a);
                //System.out.print("      gram["+ngram+"]=");
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
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        String ref1 = "H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 = "H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 2013.";
                
//        String ref1 = "xx xy yxy";
//        String ref2 = "xy xx zz yy";

//        String ref1 = "aba bab abca";
//        String ref2 = "ba bb bc";

//        String ref1 = "Maria gosta de ouvir música";
//        String ref2 = "Pedro gosta de tocar música";

//        String ref1 = "Maria Eduarda";
//        String ref2 = "Eduarda Maria";

        System.out.println(">>> Strings originais:");
        System.out.println("Antes ref1 = " + ref1);
        System.out.println("Antes ref2 = " + ref2);
        System.out.println(">>> Strings pré-processadas:");
        System.out.println("Depois ref1p = " + geraGram(ref1));
        System.out.println("Depois ref2p = " + geraGram(ref2));
        

        System.out.println("\nTestes com Jaccard:");
        System.out.println("ngram=2 Padrão = " + jaccardPadrao(ref1, ref2, 2) + "\t Modificado = " + jaccardModificado(ref1, ref2, 2));
        System.out.println("ngram=3 Padrão = " + jaccardPadrao(ref1, ref2, 3) + "\t Modificado = " + jaccardModificado(ref1, ref2, 3));
        System.out.println("ngram=4 Padrão = " + jaccardPadrao(ref1, ref2, 4) + "\t Modificado = " + jaccardModificado(ref1, ref2, 4));
        System.out.println("ngram=5 Padrão = " + jaccardPadrao(ref1, ref2, 5) + "\t Modificado = " + jaccardModificado(ref1, ref2, 5));
        System.out.println("ngram=6 Padrão = " + jaccardPadrao(ref1, ref2, 6) + "\t Modificado = " + jaccardModificado(ref1, ref2, 6));
        System.out.println("ngram=7 Padrão = " + jaccardPadrao(ref1, ref2, 7) + "\t Modificado = " + jaccardModificado(ref1, ref2, 7));
        
        System.out.println("\nTestes com SimilaridadeAO:");       
        System.out.println("Sem pre-processamento = " + similaridadeAO(ref1, ref2));
        System.out.println("Com pre-processamento = " + similaridadeAO(geraGram(ref1), geraGram(ref2)));
        
    }
}
