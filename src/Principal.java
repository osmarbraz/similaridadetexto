
import java.util.List;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;



public class Principal {
    
    public static int formula(int w){        
        return (10 + Math.abs(w))/6;
    }

    public static void main(String[] args) {
        String ref1 ="H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 ="H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 20113.";
        
       
        Simplifier simplificador1 = Simplifiers.replaceNonWord(" ");        
        ref1 = simplificador1.simplify(ref1);                        
        System.out.println("simplificador1:"+ref1);
        
        Simplifier simplificador2 = Simplifiers.toLowerCase();      
        ref1 = simplificador2.simplify(ref1);                        
        System.out.println("simplificador2:"+ref1);
        
        Tokenizer tokenizador1 = Tokenizers.whitespace();
        List<String> refs1 = tokenizador1.tokenizeToList(ref1);
        
        for(String a : refs1){
            System.out.println("\ntoken = " + a );
            if (a.length()>1){
                int ngram = formula(a.length());
                 Tokenizer tokenizador2 = Tokenizers.qGram(ngram);
                 List<String> refs2 = tokenizador2.tokenizeToList(a);
                 System.out.print("gram["+ngram+"]=");
                 for(String b : refs2){
                     System.out.print("," + b);
                 }                               
            }
	}        
    }
    
}
