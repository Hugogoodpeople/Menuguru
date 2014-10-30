package pt.menuguru.menuguru6.Restaurante.Comentarios;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import pt.menuguru.menuguru6.Utils.Comentario;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;

/**
 * Created by hugocosta on 29/09/14.
 */
public class Lista_comentarios extends Activity
{
    private String rest_id;
    private static AdapterComentarios mAdapter;
    private ArrayList<Comentario> some_list;
    private ListView listView;

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
        actionBar.hide();

        setContentView(R.layout.activity_lista_comentarios);
        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante_id");


        //fechar_comentarios

        ImageButton fechar = (ImageButton) findViewById(R.id.fechar_comentarios);
        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( R.anim.abc_fade_in , R.anim.abc_slide_out_bottom);
            }
        });


        new AsyncTaskParseJsonAvaliar(this).execute();

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
    public class AsyncTaskParseJsonAvaliar extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listar_comentarios.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Lista_comentarios delegate;

        public AsyncTaskParseJsonAvaliar (Lista_comentarios delegate)
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



                dict.put("id_rest",rest_id);
                dict.put("lang", Globals.getInstance().getLingua());


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

                dataJsonArr = jsonObj.getJSONArray("res");



                some_list = new ArrayList<Comentario>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Comentario comentario = new Comentario();
                    comentario.setId_com(c.getString("id_com"));
                    comentario.setComentario(c.getString("comentario"));
                    comentario.setResp_com(c.getString("resp_com"));
                    comentario.setData_com(c.getString("data_com"));
                    comentario.setData_respest(c.getString("data_respest"));
                    comentario.setNome_user_com(c.getString("nome_user_com"));
                    comentario.setNome_rest_com(c.getString("nome_rest_com"));

                    some_list.add(comentario);
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
        listView = (ListView) findViewById(R.id.lista_de_comentarios);

        AdapterComentarios mAdapter = new AdapterComentarios(this, R.layout.row_comentario, some_list);
        listView.setAdapter(mAdapter);

    }

    public class AdapterComentarios extends ArrayAdapter<Comentario> {

        Context myContext;
        public ImageLoader imageLoader;

        public AdapterComentarios(Context context, int textViewResourceId, ArrayList<Comentario> objects) {
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
                row=inflater.inflate(R.layout.row_comentario, parent, false);

            // para o comentario do utilizador
            TextView label1=(TextView)row.findViewById(R.id.textView_coment_user);
            label1.setText(some_list.get(position).getNome_user_com());

            TextView label2=(TextView)row.findViewById(R.id.textView_coment_data);
            label2.setText(some_list.get(position).getData_com());

            TextView label3=(TextView)row.findViewById(R.id.textView_coment_coment);
            label3.setText(some_list.get(position).getComentario());


            LinearLayout resposta = (LinearLayout) row.findViewById(R.id.resposta_restaurante);

            // para a resposta do restaurante
            // tenho de fazer uma verificaçao para remover a parte de baixo quando o restaurante não responde
            if (some_list.get(position).getResp_com().length() != 0) {

                TextView label4 = (TextView) row.findViewById(R.id.textView_resp_rest);
                label4.setText(some_list.get(position).getNome_rest_com());

                TextView label5 = (TextView) row.findViewById(R.id.textView_resp_data);
                label5.setText(some_list.get(position).getData_respest());

                TextView label6 = (TextView) row.findViewById(R.id.textView_resp_coment);
                label6.setText(some_list.get(position).getResp_com());

                resposta.setVisibility(View.VISIBLE);
            }else
            {

                resposta.setVisibility(View.GONE);
            }


            return row;
        }

    }


}
