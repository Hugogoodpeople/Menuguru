package pt.menuguru.menuguru6.Utils;

/**
 * Created by hugocosta on 13/08/14.
 */
public class Menu_do_restaurante {


    public Restaurante restaurante;
    public String nome;
    public String db_id;
    public String urlImage;
    public String tipo;
    public String precoAntigo;
    public String precoNovo;
    public String desconto;
    public String especialFita;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrecoAntigo() {
        return precoAntigo;
    }

    public void setPrecoAntigo(String precoAntigo) {
        this.precoAntigo = precoAntigo;
    }

    public String getPrecoNovo() {
        return precoNovo;
    }

    public void setPrecoNovo(String precoNovo) {
        this.precoNovo = precoNovo;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public String getDesconto() {
        return desconto;
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public String getEspecialFita() {
        return especialFita;
    }

    public void setEspecialFita(String especialFita) {
        this.especialFita = especialFita;
    }
}
