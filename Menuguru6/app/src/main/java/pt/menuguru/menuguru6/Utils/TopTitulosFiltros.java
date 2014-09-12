package pt.menuguru.menuguru6.Utils;

/**
 * Created by hugocosta on 12/09/14.
 */
public class TopTitulosFiltros
{
    // o que tinha no iOS
    /*
    @property NSMutableArray * arrayObjectos;
    @property NSString * titulo;
    @property NSString * multiSelection;
    @property NSString * id_titulo;
    */

    private AvancadosObject[] arrayObjectos;
    private String titulo;
    private String multiSelection;
    private String id_titulo;

    public AvancadosObject[] getArrayObjectos() {
        return arrayObjectos;
    }

    public void setArrayObjectos(AvancadosObject[] arrayObjectos) {
        this.arrayObjectos = arrayObjectos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMultiSelection() {
        return multiSelection;
    }

    public void setMultiSelection(String multiSelection) {
        this.multiSelection = multiSelection;
    }

    public String getId_titulo() {
        return id_titulo;
    }

    public void setId_titulo(String id_titulo) {
        this.id_titulo = id_titulo;
    }
}
