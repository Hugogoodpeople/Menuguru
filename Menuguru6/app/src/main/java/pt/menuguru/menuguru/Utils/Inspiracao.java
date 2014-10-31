package pt.menuguru.menuguru.Utils;

/**
 * Created by hugocosta on 14/08/14.
 */
public class Inspiracao
{
    public String db_id;
    public String nome;
    public InspiracaoItem[] inspiracaoItems;
    public String urlImagem;


    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public InspiracaoItem[] getInspiracaoItems() {
        return inspiracaoItems;
    }

    public void setInspiracaoItems(InspiracaoItem[] inspiracaoItems) {
        this.inspiracaoItems = inspiracaoItems;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
