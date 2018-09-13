
import java.util.List;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.GeneralizedJaccard;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;

public class Principal {

    public static int formula(int w) {
        return (10 + Math.abs(w)) / 6;
    }

    public static int formula1(int w) {
        int valor = (3*w + 10) / 8;
        return Math.min(valor, 5);
    }
    
    /**
     * Executa a comparação de duas Strings
     *
     * @param ref1 String 1 a ser comparada
     * @param ref2 String 2 a ser comparada
     * @return O percentual de semelhança das Strings
     */
    public static double comparadorXY(String ref1, String ref2) {
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
     * Gera uma String de grams apartir de uma String
     *
     * @param ref String a ser gerada os grams
     * @return String de Grams
     */
    public static String geraGram(String ref) {
        //Converte a String para minusculo
        Simplifier simplificador2 = Simplifiers.toLowerCase();
        ref = simplificador2.simplify(ref);

        //Cria o tokenizador de String com o separador por espaços em branco
        Tokenizer tokenizador1 = Tokenizers.whitespace();
        //Gera um List dos grams da String
        List<String> refs1 = tokenizador1.tokenizeToList(ref);
        
        //String de retorno dos grams
        String rgrams = "";
        for (String a : refs1) {            
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
        return rgrams;
    }

    /**
     * Compara as Strings com o método de Jaccard
     *
     * @param str1 String 1 a ser comparada
     * @param str2 String 2 a ser comparada
     * @param ngram Tamanho dos grams a ser gerado
     * @return
     */
    public static float jaccard(String str1, String str2, int ngram) {
        //Converte a String para minusculo
        Simplifier simplificador2 = Simplifiers.toLowerCase();
        str1 = simplificador2.simplify(str1);
        str2 = simplificador2.simplify(str2);
        
        
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())                        
                        .tokenize(Tokenizers.whitespace())                        
                        .tokenize(Tokenizers.qGram(ngram))                        
                        .build();        
        return metric.compare(str1, str2);
    }
    
    /**
     * Programa principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        String ref1 = "H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 = "H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 20113.";

//        String ref1 = "xx xy yxy";
//        String ref2 = "xy xx zz yy";

//        String ref1 = "aba bab abca";
//        String ref2 = "ba bb bc";

//        String ref1 = "Maria gosta de ouvir música";
//        String ref2 = "Pedro gosta de tocar música";

//        String ref1 = "Maria Eduarda";
//        String ref2 = "Eduarda Maria";

        System.out.println(">>> Strings originais");
        System.out.println("Antes ref1 =" + ref1);
        System.out.println("Antes ref2 =" + ref2);

        System.out.println("\njaccard(ngram=2) =" + jaccard(ref1, ref2, 2));
        System.out.println("jaccard(ngram=3) =" + jaccard(ref1, ref2, 3));
        System.out.println("jaccard(ngram=4) =" + jaccard(ref1, ref2, 4));
        System.out.println("jaccard(ngram=5) =" + jaccard(ref1, ref2, 5));
        System.out.println("jaccard(ngram=6) =" + jaccard(ref1, ref2, 6));
        System.out.println("jaccard(ngram=7) =" + jaccard(ref1, ref2, 7));

        System.out.println("\nNosso cálculo");
        String x1 = geraGram(ref1);
        String x2 = geraGram(ref2);

        System.out.println("Depois ref1 =" + x1);
        System.out.println("Depois ref2 =" + x2);

        System.out.println("Resultado da comparação =" + comparadorXY(x1, x2));
    }
}
