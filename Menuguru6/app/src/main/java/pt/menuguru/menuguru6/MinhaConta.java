package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.User;

import static com.facebook.Session.StatusCallback;

/**
 * Created by hugocosta on 06/08/14.
 */
public class MinhaConta extends Activity {

    EditText edit_pnome;
    EditText edit_snome;
    Button edit_data_nascimento;
    EditText edit_cidade;
    EditText edit_telefone;
    EditText edit_email;
    EditText edit_pass;
    Switch edit_news;
    Switch edit_not;

    String pnome;
    String snome;
    String data_nascimento;
    String cidade;
    String telefone;
    String email;
    String pass;
    String tipo;
    Boolean news;

    String g_pnome;
    String g_snome;
    String g_data_nascimento;
    String g_cidade;
    String g_telefone;
    String g_email;
    String g_pass;
    Boolean g_news;

    String id_face;

    String yourJsonStringUrl;

    String aux_news;

    String resesc;
    String titulo;
    String msgbox;


    private Switch switchNotificacoes;


    private ProgressDialog progressDialog;

    private Calendar cal;

    private LoginButton loginBtn;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "email");

    private static String message = "Sample status posted from android app";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Minha conta");
        actionBar.setDisplayHomeAsUpEnabled(true);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);


        loginBtn = (LoginButton) findViewById(R.id.bt_transfFav);

        pnome = Globals.get_instance().getUser().getPnome();
        snome = Globals.get_instance().getUser().getSnome();
        data_nascimento = Globals.get_instance().getUser().getData_nasc();
        cidade = Globals.get_instance().getUser().getCidade();
        telefone = Globals.get_instance().getUser().getTelefone_user();
        email = Globals.get_instance().getUser().getEmail();
        pass = Globals.get_instance().getUser().getPass();
        news = Globals.get_instance().getUser().getNews();
        news = Globals.get_instance().getUser().getNews();
        tipo = Globals.get_instance().getUser().getTipoconta();
        Log.v("NEWS",news.toString());


        edit_pnome   = (EditText)findViewById(R.id.edit_pnome);
        edit_snome   = (EditText)findViewById(R.id.edit_snome);
        edit_data_nascimento   = (Button)findViewById(R.id.edit_data_nascimento);
        edit_cidade   = (EditText)findViewById(R.id.edit_cidade);
        edit_telefone   = (EditText)findViewById(R.id.edit_telefone);
        edit_email   = (EditText)findViewById(R.id.edit_email);
        edit_pass   = (EditText)findViewById(R.id.edit_data_nasc);
        edit_news   = (Switch)findViewById(R.id.news);
        edit_not   = (Switch)findViewById(R.id.not);

        if(tipo.equals("facebook")){
            yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_login_update_face.php";
            edit_pass.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
        }else{
            yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_editar_conta_android.php";
            loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                    if (user != null) {
                        id_face = user.getId();
                        Log.v("tem login",id_face);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MinhaConta.this);
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(R.string.tranf_favoritos)
                                .setCancelable(false)
                                .setPositiveButton(R.string.sim,new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_juntar_contas.php";
                                        new AsyncTaskParseJsonFavoritos(MinhaConta.this).execute();
                                    }
                                })
                                .setNegativeButton(R.string.nao,new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();


                    } else {
                        Log.v("NAO tem login","");
                    }
                }
            });

        }
        edit_pnome.setText(pnome);
        edit_snome.setText(snome);
        edit_data_nascimento.setText(data_nascimento);
        edit_cidade.setText(cidade);
        edit_telefone.setText(telefone);
        edit_email.setText(email);
        edit_pass.setText(pass);
        edit_news.setChecked(news);
        User utilizador = Globals.getInstance().getUser();
        id_face = utilizador.getId_face();
        Log.v("IDFACe",id_face);
        edit_data_nascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("coiso", "Procurar");

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(MinhaConta.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();

            }
        });

        switchNotificacoes = (Switch) findViewById(R.id.not);
        switchNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar_notificacoes();
            }
        });

        new AsyncTaskVerificaNotificacao(this).execute();


    }

    public void editar_notificacoes()
    {
        new AsyncTaskEditarNotificacoes(this).execute();
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("FacebookSampleActivity", "Facebook session opened");
            } else if (state.isClosed()) {
                Log.d("FacebookSampleActivity", "Facebook session closed");
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            String dataSelecionada= "Ano " + Integer.toString(mYear) +
                    " Mes " + Integer.toString(mMonth+1) +
                    " Dia " + Integer.toString(mDay);

            Log.v("selecionado",dataSelecionada);

            cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, mYear);
            cal.set(Calendar.MONTH, mMonth);
            cal.set(Calendar.DAY_OF_MONTH, mDay);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(cal.getTime());

            edit_data_nascimento.setText(formattedDate);


        }
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


                if(tipo.equals("facebook")) {
                    dict.put("id",Globals.get_instance().getUser().getId_face());
                    dict.put("pass", "" );
                }else{
                    dict.put("id",Globals.get_instance().getUser().getUserid());
                    dict.put("pass", g_pass);
                }
                dict.put("email",g_email);
                dict.put("cidade",g_cidade);
                dict.put("data_nasc",g_data_nascimento);
                dict.put("primeironome",g_pnome);
                dict.put("segundonome",g_snome);
                dict.put("telefone",g_telefone);
                if(g_news){
                    dict.put("news","1");
                }else{
                    dict.put("news","0");
                }


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

        // para ir guardar as preferencias de utilizador
        SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        User utilizador = Globals.getInstance().getUser();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_name", utilizador.getPnome());
        editor.putString("user_last_name", utilizador.getSnome());
        editor.putString("user_id", utilizador.getUserid());
        editor.putString("user_data", utilizador.getData_nasc());
        editor.putString("user_tel", utilizador.getTelefone_user());
        editor.putString("user_mail", utilizador.getEmail());
        editor.putString("user_tipo", utilizador.getTipoconta());
        editor.putString("user_pass", utilizador.getPass());
        editor.putString("user_cidade", utilizador.getCidade());
        editor.putBoolean("user_news", utilizador.getNews());
        editor.commit();

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

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonFavoritos extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";




        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private MinhaConta delegate;

        public AsyncTaskParseJsonFavoritos (MinhaConta delegate){
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



                dict.put("user_id",Globals.get_instance().getUser().getUserid());
                dict.put("face_id", id_face);
                dict.put("lang", Globals.get_instance().getLingua());


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users


                resesc = jsonObj.getString("resesc");;
                titulo = jsonObj.getString("titulo");;
                msgbox = jsonObj.getString("msgbox");;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncCompleteFav(true);  }

    }


    public void asyncCompleteFav(boolean success){
        callFacebookLogout(MinhaConta.this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setTitle(titulo+" "+getString(R.string.fav_tranf))
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

    public static void callFacebookLogout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {

            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            //clear your preferences if saved
        }

    }

    public class AsyncTaskVerificaNotificacao extends AsyncTask<String, String, String> {
        private MinhaConta delegate;


        // este webservice precisa de ser trocado por um novo destinado a android
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_divice_activo_android.php";

        public AsyncTaskVerificaNotificacao (MinhaConta delegate){
            if(delegate != null)
                this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                String id_dispositivo = Globals.getInstance().getDeviceID();

                dict.put("id_dispositivo", id_dispositivo);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+jsonString);
                }

                Log.v("JsonObject","resultado do device_id = "+ jsonObj);

                String activo = jsonObj.getString("activo");


                return activo;

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            if(delegate != null)
                delegate.asysnkCompleteDispositovoActivo(strFromDoInBg);
        }
    }

    public void asysnkCompleteDispositovoActivo(String activo)
    {
        // tenho de mudar o estado do botao aqui

        Switch sw =(Switch) findViewById(R.id.not);
        if(activo.equalsIgnoreCase("1"))
            sw.setChecked(true);
        else
            sw.setChecked(false);

    }


    public class AsyncTaskEditarNotificacoes extends AsyncTask<String, String, String> {
        private MinhaConta delegate;


        // este webservice precisa de ser trocado por um novo destinado a android
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_add_remove_android_device.php";

        public AsyncTaskEditarNotificacoes (MinhaConta delegate){
            if(delegate != null)
                this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                String id_dispositivo = Globals.getInstance().getDeviceID();

                String activar;
                if(switchNotificacoes.isChecked())
                    activar = "1";
                else
                    activar = "0";


                dict.put("id_dispositivo", id_dispositivo);
                dict.put("activar", activar);


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+jsonString);
                }

                Log.v("JsonObject","resultado do device_id = "+ jsonObj);

                //String activo = jsonObj.getString("activo");


                return null;

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            if(delegate != null)
                delegate.asysnkCompleteNotificacoesAlteradas();
        }
    }


    public void asysnkCompleteNotificacoesAlteradas()
    {
        // new AsyncTaskVerificaNotificacao(this).execute();
    }

}
