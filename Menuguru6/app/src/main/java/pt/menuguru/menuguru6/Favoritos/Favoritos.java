package pt.menuguru.menuguru6.Favoritos;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;

/**
 * Created by hugocosta on 29/09/14.
 */
public class Favoritos extends Activity
{
    private ArrayList<Favorito_item> some_list;

    private ListView listView;
    private String rest_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.favoritos);
        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante");



        new AsyncTaskParseJsonFavoritos(this).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition( R.anim.abc_fade_in , R.anim.abc_slide_out_bottom);
                return false;
            default:
                break;
        }

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonFavoritos extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listagem_listanomefav.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Favoritos delegate;

        public AsyncTaskParseJsonFavoritos (Favoritos delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();


                dict.put("lang", Globals.getInstance().getLingua());
                dict.put("id_rest", rest_id);
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());

                /*
                [dict setObject: [Globals user].faceId forKey:@"face_id"];
        [dict setObject: [NSString stringWithFormat:@"%d", 0] forKey:@"user_id"];
                 */


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado dos comentarios = " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna esto dos comentarios"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("list");



                /*
                list =     (
                {
                     count = 6;
                     existe = "<null>";
                     id = 1;
                    nomelista = favoritos;
                    tipo = 1;
                },
                 */

                some_list = new ArrayList<Favorito_item>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Favorito_item fav = new Favorito_item();
                    fav.setFav_id(c.getString("id"));
                    fav.setFav_name(c.getString("nomelista"));
                    fav.setFav_number(c.getString("count"));

                    some_list.add(fav);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteListaComentarios(true);
        }

    }

    private void asyncCompleteListaComentarios(boolean success)
    {
        // aqui tenhe de inicializar o novo adapter para ter a lista de comentarios
        listView = (ListView) findViewById(R.id.lista_favoritos);

        AdapterFavoritos mAdapter = new AdapterFavoritos(this, R.layout.row_favorito, some_list);
        listView.setAdapter(mAdapter);

    }

    public class AdapterFavoritos extends ArrayAdapter<Favorito_item> {

        Context myContext;
        public ImageLoader imageLoader;

        public AdapterFavoritos(Context context, int textViewResourceId, ArrayList<Favorito_item> objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.row_favorito, parent, false);

            // para o comentario do utilizador
            TextView label1=(TextView)row.findViewById(R.id.favoritos_number);
            label1.setText(some_list.get(position).getFav_number());

            TextView label2=(TextView)row.findViewById(R.id.texto_fav_nome);
            label2.setText(some_list.get(position).getFav_name());


            return row;
        }

    }

}
