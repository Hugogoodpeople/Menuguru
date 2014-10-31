package pt.menuguru.playstore.Utils;

/**
 * Created by user on 22/09/14.
 */
public class Horario_Especial {
    private String id;
    private String hora_inicio;
    private String n_pessoas_h;
    private String dia_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDia_id() {
        return dia_id;
    }

    public void setDia_id(String dia_id) {
        this.dia_id = dia_id;
    }

    public String getN_pessoas_h() {
        return n_pessoas_h;
    }

    public void setN_pessoas_h(String n_pessoas_h) {
        this.n_pessoas_h = n_pessoas_h;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }
}
