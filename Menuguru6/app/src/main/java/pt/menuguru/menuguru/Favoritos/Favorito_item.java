package pt.menuguru.menuguru.Favoritos;

/**
 * Created by hugocosta on 29/09/14.
 */
public class Favorito_item
{
    private String fav_id;
    private String fav_name;
    private String fav_number;
    private String existe;

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getFav_name() {
        return fav_name;
    }

    public void setFav_name(String fav_name) {
        this.fav_name = fav_name;
    }

    public String getFav_number() {
        return fav_number;
    }

    public void setFav_number(String fav_number) {
        this.fav_number = fav_number;
    }

    public String getExiste() {
        return existe;
    }

    public void setExiste(String existe) {
        this.existe = existe;
    }
}
