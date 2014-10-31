package pt.menuguru.playstore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pt.menuguru.playstore.Json_parser.JSONParser;
import pt.menuguru.playstore.Utils.Globals;
import pt.menuguru.menuguru.R;


public class Voucher extends Activity {

    String data;
    String hora;
    String nr_pessoa;
    String id_rest;
    String id_especial;
    String nome_rest;
    String cod_bloqueio;
    String id_pai;
    String desc;

    String codigo;

    String resposta;
    String usada;

    TextView tv_nomerest;
    TextView tv_desc;
    TextView tv_hora;
    TextView tv_data;
    TextView tv_codigo;

    EditText ed_cod;

    ImageView img;
    ImageView img2;

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
        setContentView(R.layout.activity_voucher);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        data = getIntent().getExtras().getString("data");
        hora = getIntent().getExtras().getString("hora");
        id_rest = getIntent().getExtras().getString("id_rest");
        id_especial = getIntent().getExtras().getString("id_especial");
        nome_rest = getIntent().getExtras().getString("nome_rest");
        id_pai = getIntent().getExtras().getString("id_pai");
        desc = getIntent().getExtras().getString("desc");

        tv_nomerest = (TextView) findViewById(R.id.tv_nomerest);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_hora = (TextView) findViewById(R.id.tv_hora);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_codigo = (TextView) findViewById(R.id.tv_codigo);
        img = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);

        tv_nomerest.setText(nome_rest);
        tv_desc.setText(desc);

        new AsyncTaskParseJson(this).execute();

        ed_cod = (EditText) findViewById(R.id.ed_cod);

        ed_cod.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.v("CARREGOU","CONCLUIDO");
                    codigo = ed_cod.getText().toString();
                    new AsyncTaskParseJsonCodigo(Voucher.this).execute();
                }
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
            }
        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_gerar_codigo_reserva3.php";


        private Voucher delegate;

        public AsyncTaskParseJson (Voucher delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Voucher.this);
            progressDialog.setCancelable(false);
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

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("id_especial_pai",id_pai);
                dict.put("id_especial",id_especial);

                if(Globals.getInstance().getUser().getTipoconta().equals("facebook")){
                    dict.put("face_id",Globals.get_instance().getUser().getId_face());
                    dict.put("user_id","0");
                }else if(Globals.getInstance().getUser().getTipoconta().equals("guru")){
                    dict.put("face_id","0");
                    dict.put("user_id",Globals.get_instance().getUser().getUserid());
                }

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ", "Ele retorna na reserva" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                data = jsonObj.getString("datareserva");
                cod_bloqueio = jsonObj.getString("codigo_bloqueio");
                nr_pessoa = jsonObj.getString("npessoasreserva");
                hora = jsonObj.getString("horareserva");
                usada = jsonObj.getString("usado");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss(); delegate.asyncComplete(true);  }

    }



    public void asyncComplete(boolean success) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy");

        String aux_data = df.format(date);

        String aux_pes;
        if(nr_pessoa.equals("1")){
            aux_pes = getString(R.string.pessoa);
        }else{
            aux_pes = getString(R.string.pessoas);
        }

        tv_codigo.setText(cod_bloqueio);
        tv_hora.setText(hora+" "+nr_pessoa+" "+aux_pes);
        tv_data.setText(aux_data);

        if(usada.equals("0")){
            ed_cod.setVisibility(View.GONE);
            img.setImageResource(R.drawable.cadeado_fechado);
            img2.setVisibility(View.VISIBLE);
        }
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonCodigo extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_valida_reserva.php";


        private Voucher delegate;

        public AsyncTaskParseJsonCodigo (Voucher delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Voucher.this);
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

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("id_especial_pai",id_pai);
                dict.put("id_especial",id_especial);
                dict.put("codigo",codigo);

                if(Globals.getInstance().getUser().getTipoconta().equals("facebook")){
                    dict.put("face_id",Globals.get_instance().getUser().getId_face());
                    dict.put("user_id","0");
                }else if(Globals.getInstance().getUser().getTipoconta().equals("guru")){
                    dict.put("face_id","0");
                    dict.put("user_id",Globals.get_instance().getUser().getUserid());
                }



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ", "Ele retorna na reserva" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                resposta = jsonObj.getString("resp");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss(); delegate.asyncCompleteCodigo(true);  }

    }



    public void asyncCompleteCodigo(boolean success) {
        Log.v("RESPOSTA",resposta);

        if(resposta.equals("feito")){
            ed_cod.setVisibility(View.GONE);
            img.setImageResource(R.drawable.cadeado_fechado);
            img2.setVisibility(View.VISIBLE);
        }else if(resposta.equals("codigo invalido")){
             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Voucher.this);
             alertDialogBuilder
                    .setMessage(R.string.cod_invalido)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
             AlertDialog alertDialog = alertDialogBuilder.create();
             // show it
             alertDialog.show();
            }
    }

}
