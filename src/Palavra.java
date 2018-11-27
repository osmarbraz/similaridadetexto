
public class Palavra implements Comparable {

    private String palavra;
    private String palavraErrada;
    private double resultado;
    private int gram;

    public Palavra(String palavra, String palavraErrada, double resultado, int gram) {
        this.palavra = palavra;
        this.palavraErrada = palavraErrada;
        this.resultado = resultado;
        this.gram = gram;
    }

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
