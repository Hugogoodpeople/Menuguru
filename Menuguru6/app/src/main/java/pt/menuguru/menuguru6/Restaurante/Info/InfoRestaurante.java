package pt.menuguru.menuguru6.Restaurante.Info;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.MyApplication;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.Locais;

/**
 * Created by hugocosta on 25/09/14.
 */
public class InfoRestaurante extends Activity
{

    private String rest_id;
    private ArrayList<InfoRestObject> infos;
    private ListView listaInfo;
    private AdapterInfos mAdapter;

    private String nome_rest;
    private String cidade_nome;

    private ProgressDialog progressDialog;


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

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(getString(R.string.info_rest));

        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante_id");
        nome_rest = intent.getStringExtra("nome_rest");
        cidade_nome = intent.getStringExtra("cidade_nome");

        setContentView(R.layout.restaurante_info_list);

        new AsyncTaskParseJsonComentarios(this).execute();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition( R.anim.abc_fade_in , R.anim.out_from_top);
                return false;
            default:
                break;
        }

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonComentarios extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_info_restuarante_android.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private InfoRestaurante delegate;

        public AsyncTaskParseJsonComentarios (InfoRestaurante delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InfoRestaurante.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
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

                dict.put("id_rest",rest_id);


                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultadodo info do restaurante " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna estes infos"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("res");



                infos = new ArrayList<InfoRestObject>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    InfoRestObject info = new InfoRestObject();
                    info.setTitulo(c.getString("titulo"));
                    info.setConteudo(c.getString("conteudo"));
                    info.setSubconteudo(c.getString("subconteudo"));

                    infos.add(info);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();
            delegate.asyncCompleteInfo(true);
        }

    }

    public void asyncCompleteInfo(boolean success)
    {
        // aqui tenho de preencher a lista

        listaInfo = (ListView) findViewById(R.id.listView_info_rest);

        final LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_reportar_erro, listaInfo, false);

        RelativeLayout lay = (RelativeLayout) footer.findViewById(R.id.layout_footer_click);
        lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoRestaurante.this, InfoRestReportarErro.class);
                intent.putExtra("restaurante_id", rest_id);
                intent.putExtra("nome_rest", nome_rest);
                intent.putExtra("cidade_nome",cidade_nome);

                startActivity(intent);
                overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

            }
        });

        listaInfo.addFooterView(footer);

        mAdapter = new AdapterInfos(this, R.layout.row_info_rest, infos);
        listaInfo.setAdapter(mAdapter);



    }


    public class AdapterInfos extends ArrayAdapter<InfoRestObject> {

        Context myContext;

        public AdapterInfos(Context context, int textViewResourceId,
                             ArrayList<InfoRestObject> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_info_rest, parent, false);
            TextView label = (TextView) row.findViewById(R.id.textView_info_titulo);
            label.setText(infos.get(position).getTitulo());

            TextView label2 = (TextView) row.findViewById(R.id.textView_info_desc);
            label2.setText(infos.get(position).getConteudo());

            return row;
        }

    }



}
