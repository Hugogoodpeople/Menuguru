package pt.menuguru.menuguru.Utils;

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
    public String id_rest;
    public String destaque = "0";
    public String lat;
    public String lng;
    public String nome_rest;
    public String imagem_rest;
    public String morada;
    public String descricao;
    public String hora_minimo_antedencia_especial;


    public String getHora_minimo_antedencia_especial() {
        return hora_minimo_antedencia_especial;
    }

    public void setHora_minimo_antedencia_especial(String hora_minimo_antedencia_especial) {
        this.hora_minimo_antedencia_especial = hora_minimo_antedencia_especial;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDestaque() {
        return destaque;
    }

    public void setDestaque(String destaque) {
        this.destaque = destaque;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNome_rest() {
        return nome_rest;
    }

    public void setNome_rest(String nome_rest) {
        this.nome_rest = nome_rest;
    }

    public String getImagem_rest() {
        return imagem_rest;
    }

    public void setImagem_rest(String imagem_rest) {
        this.imagem_rest = imagem_rest;
    }

    public String getId_rest() {   return id_rest; }

    public void setId_rest(String id_rest) {  this.id_rest = id_rest;  }

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
