package pt.menuguru.menuguru6.Utils;

import android.content.res.Resources;

import pt.menuguru.menuguru6.R;

/**
 * Created by hugocosta on 13/08/14.
 */
public class Globals
{
    private static Globals _instance;
    public String latitude = "0";
    public String longitude = "0";
    public String cidedade_id = "0";
    public String cidadeÇ_nome = "Perto de mim" ;
    public User user ;
    public String lingua = "pt";
    public String textoPesquisado = "";
    public String tipoRestPrat = "1";
    public TopTitulosFiltros[] filtros;
    public boolean hasFilters = false;
    public ComoFunc[] cfunc;
    public Festival[] festival;
    public String deviceID;

    public boolean isHasFilters() {
        return hasFilters;
    }

    public void setHasFilters(boolean hasFilters) {
        this.hasFilters = hasFilters;
    }

    public String getTextoPesquisado() {
        return textoPesquisado;
    }

    public void setTextoPesquisado(String textoPesquisado) {
        this.textoPesquisado = textoPesquisado;
    }

    public String getTipoRestPrat() {
        return tipoRestPrat;
    }

    public void setTipoRestPrat(String tipoRestPrat) {
        this.tipoRestPrat = tipoRestPrat;
    }

    public TopTitulosFiltros[] getFiltros() {
        return filtros;
    }

    public void setFiltros(TopTitulosFiltros[] filtros) {
        this.filtros = filtros;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCidadeÇ_nome() {
        return cidadeÇ_nome;
    }

    public void setCidadeÇ_nome(String cidadeÇ_nome) {
        this.cidadeÇ_nome = cidadeÇ_nome;
    }

    public static Globals get_instance() {
        return _instance;
    }

    public static void set_instance(Globals _instance) {
        Globals._instance = _instance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCidedade_id() {
        return cidedade_id;
    }

    public void setCidedade_id(String cidedade_id) {
        this.cidedade_id = cidedade_id;
    }

    public ComoFunc[] getCfunc() { return cfunc; }

    public void setCfunc(ComoFunc[] cfunc) {
        this.cfunc = cfunc;
    }

    public Festival[] getFestival() {return festival;}
    public void setFestival(Festival[] festival) { this.festival = festival;}

    private Globals()
    {

    }

    public static Globals getInstance()
    {
        if (_instance == null)
        {
            _instance = new Globals();
        }
        return _instance;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceID()
    {
        return this.deviceID;
    }
}