package pt.menuguru.menuguru6.ResultadosProcurarMesa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;
import pt.menuguru.menuguru6.Utils.Utils;

/**
 * Created by hugocosta on 09/09/14.
 */
public class Resultados extends Activity
{
    String data;
    String hora;
    String pessoas;

    Restaurante[] some_array = null;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapter mAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_inspiracao);
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        hora = intent.getStringExtra("hora");
        pessoas = intent.getStringExtra("pessoas");


        //setTitle(value);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setCustomView(R.layout.tab_header);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView t =(TextView) findViewById(R.id.mytext);
        t.setText(Globals.get_instance().getCidadeÇ_nome());

        Intent id_item = getIntent();


        new AsyncTaskParseJson(this).execute();
    }

    public void asyncComplete(boolean success){


        // mCallbacks.onButtonClicked();

        mListView = (ListView) findViewById(R.id.listViewResultadoInspiras);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);


        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_restlistagem_listanomefav.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Resultados delegate;

        public AsyncTaskParseJson (Resultados delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {

            try {

                // instantiate our json parser
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("lang","pt");
                dict.put("lat", Globals.getInstance().getLatitude());
                dict.put("long", Globals.getInstance().getLongitude());
                dict.put("hora", hora);
                dict.put("data", data);
                dict.put("cidade", Globals.getInstance().getCidedade_id());
                dict.put("numpessoas", pessoas);


                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("sdffgddvsdsv","objecto especial = "+ jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }

    public class MyListAdapter extends ArrayAdapter<Restaurante> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             Restaurante[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(context);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = null;


            if (some_array[position].tipo.equalsIgnoreCase("restaurante")) {

                row=inflater.inflate(R.layout.fragment_inicio, parent, false);
                TextView label2=(TextView)row.findViewById(R.id.nomeMenu);
                label2.setText(some_array[position].cosinhas);

                TextView label3=(TextView)row.findViewById(R.id.desconto);
                label3.setText(some_array[position].precoMedio);

                TextView label=(TextView)row.findViewById(R.id.nomeRestaurante);
                label.setText(some_array[position].nome);

                ImageView icon=(ImageView)row.findViewById(R.id.capa);

                RatingBar rating = (RatingBar)row.findViewById(R.id.ratingBar);

                rating.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                rating.setFocusable(false);

                rating.setRating( Float.parseFloat(some_array[position].mediarating));

                TextView label4 = (TextView) row.findViewById(R.id.distancia);
                label4.setText("ND");

                Location locationRest = new Location("");
                locationRest.setLatitude(Double.parseDouble(some_array[position].latitude));
                locationRest.setLongitude(Double.parseDouble(some_array[position].longitude));

                Location locationPhone = new Location("");
                locationPhone.setLatitude(Double.parseDouble(Globals.getInstance().getLatitude()));
                locationPhone.setLongitude(Double.parseDouble(Globals.getInstance().getLongitude()));

                label4.setText(Utils.getDistance(locationPhone, locationRest));

                imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), icon);

            }else
            {
                row=inflater.inflate(R.layout.fragment_row_colecao, parent, false);
                ImageView icon=(ImageView)row.findViewById(R.id.imagemColecao);
                imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), icon);

            }
            return row;
        }

    }



}
