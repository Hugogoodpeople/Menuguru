package pt.menuguru.menuguru6.menus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.MyApplication;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Ementa_object;
import pt.menuguru.menuguru6.Utils.ImageLoader;

/**
 * Created by hugocosta on 17/10/14.
 */


public class Menu_diaria extends Activity
{

    private ProgressDialog progressDialog;

    private ArrayList<Ementa_object> ementa_objects;

    private String rest_id;
    private String nome_cat_em;
    private String url_imagem;
    private String descricao_ementa;

    @Override
    public void onStart()
    {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_ementa);
        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("rest_id");
        url_imagem = intent.getStringExtra("url_foto");

        actionBar.setTitle(nome_cat_em);


        new AsyncTaskParseJson(this).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition( R.anim.pop_view1 , R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }


    // preciso de instanciar um novo custon adapter
    public class MyListAdapter extends ArrayAdapter<Ementa_object> {


        ArrayList<Ementa_object> mObjects;

        Context myContext;

        public MyListAdapter(Context context, int textViewResourceId,
                             ArrayList<Ementa_object> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            mObjects = objects;
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_menu_diaria, parent, false);

            TextView label=(TextView)row.findViewById(R.id.textview_conteudo_diaria);
            label.setText(mObjects.get(position).getTexto());


            TextView label2 = (TextView)row.findViewById(R.id.titulo);
            label2.setText(mObjects.get(position).getPreco());

            return row;
        }
    }

    // preciso do webservice para ir buscar a ementa selecionada

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_menu_dia.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Menu_diaria delegate;

        public AsyncTaskParseJson (Menu_diaria delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Menu_diaria.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                // instantiate our json parser
                JSONParser jParser = new JSONParser();

                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("lang","");
                dict.put("dummy", "nao");
                dict.put("rest_id",rest_id);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);



                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage() + "] " + jsonString);
                }

                dataJsonArr = jsonObj.getJSONArray("resp");

                Log.v("JsonObject","objecto = "+ jsonObj);

                ementa_objects = new ArrayList<Ementa_object>();

                // percorrer o loop para preencher o array com os itens
                for (int i = 0; i < dataJsonArr.length(); i++)
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    Ementa_object ementa_object = new Ementa_object();

                     ementa_object.setTexto("");
                    // aqui tenho um array de strings ou seja não é directo
                    for (int z = 0; z < c.getJSONArray("nome_prato").length(); z++)
                    {
                        if (ementa_object.getTexto().length() == 0)
                        {
                            String cont = c.getJSONArray("nome_prato").get(z).toString();
                            ementa_object.setTexto(cont);
                        }else
                        {
                            String cont = c.getJSONArray("nome_prato").get(z).toString();
                            ementa_object.setTexto(ementa_object.getTexto() + "\n"  + cont);
                        }

                    }


                    ementa_object.setPreco(c.getString("nome_cat") + "  " );

                    ementa_objects.add(ementa_object);

                }

               JSONObject coiso = jsonObj.getJSONObject("res");

                Log.v("JsonObject","descricao cenas = "+ jsonObj);


                descricao_ementa = coiso.getString("descricao");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }

    public void asyncComplete(boolean success)
    {

        ListView listView = (ListView) findViewById(R.id.listView_menu_diaria_normal);

        MyListAdapter mAdapter = new MyListAdapter(this, R.layout.row_menu_diaria, ementa_objects);

        final LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_menu_diaria, listView, false);

        ImageView imagem = (ImageView)header.findViewById(R.id.image_capa);
        ImageLoader imageLoaderEspecial=new ImageLoader(getApplicationContext());
        imageLoaderEspecial.DisplayImage("http://menuguru.pt/"+ url_imagem, imagem);

        TextView descricao = (TextView)header.findViewById(R.id.textView11);
        descricao.setText(descricao_ementa);

        // para o cenas
        SimpleDateFormat input = new SimpleDateFormat("dd LLLL yy");

        TextView data = (TextView)header.findViewById(R.id.data_diaria);
        data.setText(input.format(new Date()));

        // para o coiso
        SimpleDateFormat input2 = new SimpleDateFormat("EEEE");

        TextView data2 = (TextView)header.findViewById(R.id.dia_semana_diaria);
        data2.setText(input2.format(new Date()));


        listView.addHeaderView(header);

        listView.setAdapter(mAdapter);


    }


}
