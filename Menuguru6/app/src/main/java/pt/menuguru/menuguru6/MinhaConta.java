package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.User;

/**
 * Created by hugocosta on 06/08/14.
 */
public class MinhaConta extends Activity {

    EditText edit_pnome;
    EditText edit_snome;
    EditText edit_data_nascimento;
    EditText edit_cidade;
    EditText edit_telefone;
    EditText edit_email;
    EditText edit_pass;
    Switch edit_news;

    String pnome;
    String snome;
    String data_nascimento;
    String cidade;
    String telefone;
    String email;
    String pass;
    Boolean news;

    String g_pnome;
    String g_snome;
    String g_data_nascimento;
    String g_cidade;
    String g_telefone;
    String g_email;
    String g_pass;
    Boolean g_news;

    String aux_news;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Minha conta");
        actionBar.setDisplayHomeAsUpEnabled(true);

        pnome = Globals.get_instance().getUser().getPnome();
        snome = Globals.get_instance().getUser().getSnome();
        data_nascimento = Globals.get_instance().getUser().getData_nasc();
        cidade = Globals.get_instance().getUser().getCidade();
        telefone = Globals.get_instance().getUser().getTelefone_user();
        email = Globals.get_instance().getUser().getEmail();
        pass = Globals.get_instance().getUser().getPass();
        news = Globals.get_instance().getUser().getNews();


        edit_pnome   = (EditText)findViewById(R.id.edit_pnome);
        edit_snome   = (EditText)findViewById(R.id.edit_snome);
        edit_data_nascimento   = (EditText)findViewById(R.id.edit_data_nascimento);
        edit_cidade   = (EditText)findViewById(R.id.edit_cidade);
        edit_telefone   = (EditText)findViewById(R.id.edit_telefone);
        edit_email   = (EditText)findViewById(R.id.edit_email);
        edit_pass   = (EditText)findViewById(R.id.edit_pass);
        edit_news   = (Switch)findViewById(R.id.news);

        edit_pnome.setText(pnome);
        edit_snome.setText(snome);
        edit_data_nascimento.setText(data_nascimento);
        edit_cidade.setText(cidade);
        edit_telefone.setText(telefone);
        edit_email.setText(email);
        edit_pass.setText(pass);
        edit_news.setChecked(news);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guardar_perfil_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            case R.id.action_guardar:
                g_pnome = edit_pnome.getText().toString().trim();
                g_snome = edit_snome.getText().toString().trim();
                g_data_nascimento = edit_data_nascimento.getText().toString().trim();
                g_cidade = edit_cidade.getText().toString().trim();
                g_telefone = edit_telefone.getText().toString().trim();
                g_email = edit_email.getText().toString().trim();
                g_pass = edit_pass.getText().toString().trim();
                g_news = edit_news.isChecked();

                if(g_news.equals("true")){aux_news="1";}else{aux_news="0";}
                Log.v("Guardar","Nome:"+g_pnome+" Sobrenome:"+g_snome+" Data:"+g_data_nascimento+" Cidade:"+g_cidade+" Telefone:"+g_telefone+" Email:"+g_email+" Pass:"+g_pass+" News:"+aux_news);
                new AsyncTaskParseJson(MinhaConta.this).execute();
                return false;
            default:
                break;
        }

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_login_update_guru.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private MinhaConta delegate;

        public AsyncTaskParseJson (MinhaConta delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MinhaConta.this);
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

                dict.put("id",Globals.get_instance().getUser().getUserid());
                dict.put("pass",g_pass);
                dict.put("email",g_email);
                dict.put("cidade",g_cidade);
                dict.put("data_nasc",g_data_nascimento);
                dict.put("primeironome",g_pnome);
                dict.put("segundonome",g_snome);
                dict.put("telefone",g_telefone);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String resp = jsonObj.getString("resp");

                Log.v("resp","objecto = "+ jsonObj.getString("resp"));
                Log.v("msg","objecto = "+ jsonObj.getString("msg"));
                Log.v("titulo","objecto = "+ jsonObj.getString("titulo"));




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){
        Globals.get_instance().getUser().setPnome(g_pnome);
        Globals.get_instance().getUser().setSnome(g_snome);
        Globals.get_instance().getUser().setData_nasc(g_data_nascimento);
        Globals.get_instance().getUser().setCidade(g_cidade);
        Globals.get_instance().getUser().setTelefone_user(g_telefone);
        Globals.get_instance().getUser().setEmail(g_email);
        Globals.get_instance().getUser().setPass(g_pass);
        Globals.get_instance().getUser().setNews(g_news);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.guardar_perfil)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
