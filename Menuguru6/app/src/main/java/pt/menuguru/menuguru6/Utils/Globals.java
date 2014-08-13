package pt.menuguru.menuguru6.Utils;

/**
 * Created by hugocosta on 13/08/14.
 */
public class Globals
{
    private static Globals   _instance;
    public String latitude = "0";
    public String longitude = "0";
    public String cidedade_id = "0";

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
}