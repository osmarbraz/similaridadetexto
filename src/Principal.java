
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

    public static double compararXX(String ref1, String ref2) {
        Tokenizer tokenizador1 = Tokenizers.whitespace();
        List<String> refs1 = tokenizador1.tokenizeToList(ref1);

        Tokenizer tokenizador2 = Tokenizers.whitespace();
        List<String> refs2 = tokenizador2.tokenizeToList(ref2);

        int comum = 0;
        int diferencaA = 0;
        for (String a : refs1) {
            if (refs2.contains(a)) {                
                comum = comum + 1;
            } else {
                diferencaA = diferencaA + 1;
            }
        }

        int diferencaB = Math.abs(refs2.size() - comum);
        double total = comum + diferencaA + diferencaB;
        double medida = comum / total;
        return medida;
    }

    public static String geraGram(String ref1) {
        Simplifier simplificador1 = Simplifiers.replaceNonWord(" ");
        ref1 = simplificador1.simplify(ref1);
        //System.out.println("simplificador1:"+ref1);

        Simplifier simplificador2 = Simplifiers.toLowerCase();
        ref1 = simplificador2.simplify(ref1);
        //System.out.println("simplificador2:"+ref1);

        Tokenizer tokenizador1 = Tokenizers.whitespace();
        List<String> refs1 = tokenizador1.tokenizeToList(ref1);

        String fref1 = "";
        for (String a : refs1) {
            ///Tokens com 2 ou mais letras
            if (a.length() > 1) {
                //System.out.println("\ntoken = " + a );
                int ngram = formula(a.length());
                Tokenizer tokenizador2 = Tokenizers.qGram(ngram);
                List<String> refs2 = tokenizador2.tokenizeToList(a);
                //System.out.print("      gram["+ngram+"]=");
                for (String b : refs2) {
                    //System.out.print(b+",");
                    fref1 = fref1 + " " + b;
                }
            }
        }
        return fref1;
    }

    public static float jaccard(String str1, String str2, int ngram) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.qGram(ngram))
                        .build();

        return metric.compare(str1, str2);
    }
    
    public static void main(String[] args) {
        String ref1 = "H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 = "H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 20113.";
        
//        String ref1 = "xx xy yxy";
//        String ref2 = "xy xx zz yy";
        
//        String ref1 = "aba bab abca";
//        String ref2 = "ba bb bc";

//        String ref1 = "Maria gosta de ouvir música";
//        String ref2 = "Pedro gosta de tocar música";


        System.out.println("jaccard(ngram=2)=" + jaccard(ref1, ref2, 2));
        System.out.println("jaccard(ngram=3)=" + jaccard(ref1, ref2, 3));
        System.out.println("jaccard(ngram=4)=" + jaccard(ref1, ref2, 4));
        System.out.println("jaccard(ngram=5)=" + jaccard(ref1, ref2, 5)); 
        System.out.println("jaccard(ngram=6)=" + jaccard(ref1, ref2, 6)); 
        System.out.println("jaccard(ngram=7)=" + jaccard(ref1, ref2, 7)); 

        System.out.println("\nNosso cálculo");
        String x1 = geraGram(ref1);
        String x2 = geraGram(ref2);

        System.out.println("depois ref1 =" + x1);
        System.out.println("depois ref2 =" + x2);
       
        System.out.println("comparar=" + compararXX(x1, x2));
    }
}
