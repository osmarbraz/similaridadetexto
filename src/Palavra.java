
/**
 * Classe usada para armazenar o resultado da comparação das palavras e o seu resultado.
 */
public class Palavra implements Comparable {

    private String palavra;
    private String palavraErrada;
    private double resultado;
    private int gram;

    /**
     * Construtor com parâmetros.
     *
     * @param palavra Palavra correta.
     * @param palavraErrada Palavra a ser comparada.
     * @param resultado Resultado a verificação.
     * @param gram Tamanho do gram utilizado na comparação.
     */
    public Palavra(String palavra, String palavraErrada, double resultado, int gram) {
        this.palavra = palavra;
        this.palavraErrada = palavraErrada;
        this.resultado = resultado;
        this.gram = gram;
    }

    // gets´s e set´s
    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getPalavraErrada() {
        return palavraErrada;
    }

    public void setPalavraErrada(String palavraErrada) {
        this.palavraErrada = palavraErrada;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public int getGram() {
        return gram;
    }

    public void setGram(int gram) {
        this.gram = gram;
    }

    /**
     * Metodo que compara os resultados.
     *
     * Utilizado para ordenar a lista.
     *
     * @param o Objeto de comparação.
     *
     * @return Se é menor, maior ou igual.
     */
    @Override
    public int compareTo(Object o) {
        Palavra pa = (Palavra) o;
        if (this.resultado < pa.resultado) {
            return 1;
        }
        if (this.resultado > pa.resultado) {
            return -1;
        }
        if (this.resultado == pa.resultado) {
            return 0;
        }
        return -1;
    }
}