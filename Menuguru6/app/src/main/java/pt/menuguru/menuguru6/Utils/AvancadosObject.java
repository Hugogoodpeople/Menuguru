package pt.menuguru.menuguru6.Utils;

/**
 * Created by hugocosta on 12/09/14.
 */
public class AvancadosObject {

    // na versao ios tem
    /*
    @property NSString * id_sub_titulo;
    @property NSString * sub_titulo;
    @property BOOL selecionado;
    */

    private String id_sub_titulo;
    private String sub_titulo;
    private Boolean selecionado = false;

    public String getId_sub_titulo() {
        return id_sub_titulo;
    }

    public void setId_sub_titulo(String id_sub_titulo) {
        this.id_sub_titulo = id_sub_titulo;
    }

    public String getSub_titulo() {
        return sub_titulo;
    }

    public void setSub_titulo(String sub_titulo) {
        this.sub_titulo = sub_titulo;
    }

    public Boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(Boolean selecionado) {
        this.selecionado = selecionado;
    }
}
