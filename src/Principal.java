
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import static com.google.common.base.Predicates.in;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.simmetrics.StringDistance;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringDistanceBuilder;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.metrics.EuclideanDistance;
import org.simmetrics.metrics.GeneralizedJaccard;
import org.simmetrics.metrics.Jaccard;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.metrics.StringDistances;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifier;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizer;
import org.simmetrics.tokenizers.Tokenizers;

public class Principal {

    public static int formula(int w) {
        return (10 + Math.abs(w)) / 6;
    }

    public static String metodoX(String ref1) {
        Simplifier simplificador1 = Simplifiers.replaceNonWord(" ");
        ref1 = simplificador1.simplify(ref1);
        // System.out.println("simplificador1:"+ref1);

        Simplifier simplificador2 = Simplifiers.toLowerCase();
        ref1 = simplificador2.simplify(ref1);
        //System.out.println("simplificador2:"+ref1);

        Tokenizer tokenizador1 = Tokenizers.whitespace();
        List<String> refs1 = tokenizador1.tokenizeToList(ref1);

        String fref1 = "";
        for (String a : refs1) {
            //System.out.println("token = " + a );
            if (a.length() > 1) {
                int ngram = formula(a.length());
                Tokenizer tokenizador2 = Tokenizers.qGram(ngram);
                List<String> refs2 = tokenizador2.tokenizeToList(a);
                //System.out.print("      gram["+ngram+"]=");
                for (String b : refs2) {
                    // System.out.print(b+",");
                    fref1 = fref1 + " " + b;
                }
            }
        }
        return fref1;
    }

    public static float exemplo01(String str1, String str2) {
        StringMetric metric = StringMetrics.cosineSimilarity();
        float result = metric.compare(str1, str2);
        return result;
    }

    public static float exemplo02(String str1, String str2) {
        StringMetric metric = StringMetricBuilder
                .with(new CosineSimilarity<String>())
                .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
                .simplify(Simplifiers.replaceNonWord())
                .tokenize(Tokenizers.whitespace()).build();
        float result = metric.compare(str1, str2);
        return result;
    }

    public static float exemplo03(String str1, String str2) {
        StringDistance metric = StringDistanceBuilder
                .with(new EuclideanDistance<String>())
                .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
                .simplify(Simplifiers.replaceNonWord())
                .tokenize(Tokenizers.whitespace()).build();
        float result = metric.distance(str1, str2);
        return result;
    }

    public static float exemplo04(String a, String b) {
        StringDistance metric = new Levenshtein();
        return metric.distance(a, b);
    }

    public static float exemplo05(String a, String b) {
        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new Levenshtein())
                        .simplify(Simplifiers.removeDiacritics())
                        .build();

        return metric.distance(a, b);
    }

    public static float exemplo06(String a, String b) {
        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new Levenshtein())
                        .simplify(Simplifiers.removeDiacritics())
                        .simplify(Simplifiers.toLowerCase())
                        .build();
        return metric.distance(a, b);
    }

    public static float exemplo07(String a, String b) {
        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .tokenize(Tokenizers.whitespace())
                        .build();
        return metric.distance(a, b);
    }

    public static float exemplo08(String a, String b) {
        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .tokenize(Tokenizers.whitespace())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.distance(a, b);
    }

    public static float exemplo09(String a, String b) {
        Set<String> commonWords = Sets.newHashSet("it", "is");
        Set<String> otherCommonWords = Sets.newHashSet("a");

        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .simplify(Simplifiers.toLowerCase())
                        .simplify(Simplifiers.removeNonWord())
                        .tokenize(Tokenizers.whitespace())
                        .filter(Predicates.not(in(commonWords)))
                        .filter(Predicates.not(in(otherCommonWords)))
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.distance(a, b);
    }

    public static float exemplo10(String a, String b) {
        Function<String, String> reverse = new Function<String, String>() {

            @Override
            public String apply(String input) {
                return new StringBuilder(input).reverse().toString();
            }

        };

        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .simplify(Simplifiers.toLowerCase())
                        .simplify(Simplifiers.removeNonWord())
                        .tokenize(Tokenizers.whitespace())
                        .transform(reverse)
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.distance(a, b);
    }

    public static float exemplo11(String a, String b) {
        Cache<String, String> stringCache
                = CacheBuilder.newBuilder()
                        .maximumSize(2)
                        .build();

        Cache<String, Multiset<String>> tokenCache
                = CacheBuilder.newBuilder()
                        .maximumSize(2)
                        .build();

        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .simplify(Simplifiers.toLowerCase())
                        .simplify(Simplifiers.removeNonWord())
                        .cacheStrings(stringCache)
                        .tokenize(Tokenizers.qGram(3))
                        .cacheTokens(tokenCache)
                        .build();

        return metric.distance(a, b);
    }

    public static float exemplo12(String str1, String str2) {
        StringDistance metric = StringDistances.levenshtein();
        return metric.distance(str1, str2);
    }

    public static float exemplo13(String str1, String str2) {
        StringDistance metric = StringDistances.euclideanDistance();
        return metric.distance(str1, str2);
    }

    public static float exemplo14(String str1, String str2) {
        StringDistance metric
                = org.simmetrics.builders.StringDistanceBuilder.with(new EuclideanDistance<String>())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.distance(str1, str2);
    }

    public static float exemplo15(String str1, String str2) {
        StringMetric metric = StringMetrics.jaro();
        return metric.compare(str1, str2);
    }

    public static float exemplo16(String str1, String str2) {
        StringMetric metric = StringMetrics.cosineSimilarity();
        return metric.compare(str1, str2);
    }

    public static float exemplo17(String str1, String str2) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new CosineSimilarity<String>())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.compare(str1, str2);
    }

    public static float exemplo18(String str1, String str2) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new Jaccard<String>())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.compare(str1, str2);
    }

    public static float exemplo19(String str1, String str2) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.compare(str1, str2);
    }
    
    public static float exemplo20(String str1, String str2) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.qGram(2))
                        .build();

        return metric.compare(str1, str2);
    }
    
    public static float exemplo99(String str1, String str2) {
        StringMetric metric
                = org.simmetrics.builders.StringMetricBuilder.with(new GeneralizedJaccard<String>())
                        .tokenize(Tokenizers.qGram(3))
                        .build();

        return metric.compare(str1, str2);
    }

    public static void main(String[] args) {
        String ref1 = "H. Wang, X. He, M.-W. Chang, Y. Song, R. W. White, and W. Chu. Personalized ranking model adaptation for web search. In Proceedings of the 36th international ACM SIGIR conference on Research and development in information retrieval, pages 323-332, 2013.";
        String ref2 = "H. Wang, et.al. Personalized ranking model adaptation for web search. In Proc. Intl. SIGIR; pg. 323 a 332, 20113.";
        System.out.println("ref1=" + ref1);
        System.out.println("ref2=" + ref2);
        System.out.println("medidas");
        //Teste 1

        System.out.println("examplo01=" + exemplo01(ref1, ref2));
        System.out.println("examplo02=" + exemplo02(ref1, ref2));
        System.out.println("examplo03=" + exemplo03(ref1, ref2));
        System.out.println("examplo04=" + exemplo04(ref1, ref2));
        System.out.println("examplo05=" + exemplo05(ref1, ref2));
        System.out.println("examplo06=" + exemplo06(ref1, ref2));
        System.out.println("examplo07=" + exemplo07(ref1, ref2));
        System.out.println("examplo08=" + exemplo08(ref1, ref2));
        System.out.println("examplo09=" + exemplo09(ref1, ref2));
        System.out.println("examplo10=" + exemplo10(ref1, ref2));
        System.out.println("examplo11=" + exemplo11(ref1, ref2));
        System.out.println("examplo12=" + exemplo12(ref1, ref2));
        System.out.println("examplo13=" + exemplo13(ref1, ref2));
        System.out.println("examplo14=" + exemplo14(ref1, ref2));
        System.out.println("examplo15=" + exemplo15(ref1, ref2));
        System.out.println("examplo16=" + exemplo16(ref1, ref2));
        System.out.println("examplo17=" + exemplo17(ref1, ref2));
        System.out.println("examplo18=" + exemplo18(ref1, ref2));
        System.out.println("examplo19=" + exemplo19(ref1, ref2));
        System.out.println("examplo20=" + exemplo20(ref1, ref2));

        String x1 = metodoX(ref1);
        String x2 = metodoX(ref2);
        System.out.println("examplo99=" + exemplo99(ref1, ref2));

        System.out.println("x1 =" + x1);
        System.out.println("x2 =" + x2);

    }

}
