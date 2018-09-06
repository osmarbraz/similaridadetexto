
import java.text.Normalizer;
import java.util.List;
import org.simmetrics.StringMetric;
import static org.simmetrics.builders.StringDistanceBuilder.with;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;



public class Principal {
    
    public static int formula(int n){
        return n/2;
    }

    public static void main(String[] args) {
        String ref1 ="H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 ="H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 20113.";
        
       
        Simplifier simplificador1 = Simplifiers.replaceNonWord(" ");
        
        ref1 = simplificador1.simplify(ref1);                        
        System.out.println("resultado1:"+ref1);
        
        //Simplifier simplificador2 = Simplifiers.toLowerCase();      
        //ref1 = simplificador2.simplify(ref1);                        
        //System.out.println("resultado2:"+ref1);
        
        Tokenizer tokenizador = Tokenizers.whitespace();
        List<String> refs1 = tokenizador.tokenizeToList(ref1);
        
        for(String a : refs1){
            System.out.println("token = " + a);
            if (a.length()>1){
                int ngram = formula(a.length());
                
                
            }
	}
        
        
        
        
        
//        Simplifier simplificador3 = Simplifiers.normalize(Normalizer.Form.NFD);
//        ref1 = simplificador3.simplify(ref1);                        
//        System.out.println("resultado2:"+ref1);
        
        
        //String[] palavrasRef1 = ref1.split(" ");
        
        //for(int i=0;i<palavrasRef1.length;i++){
        //System.out.println("tokens:"+palavrasRef1[i] + " =  "+palavrasRef1[i].length());
        //}
        
        
        // StringMetric metric = StringMetrics.longestCommonSubsequence();
        //ystem.out.println("metrica = " + metric.compare(ref1, ref2));
        
    }
    
}
