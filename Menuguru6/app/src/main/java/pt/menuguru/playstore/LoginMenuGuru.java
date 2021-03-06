package pt.menuguru.playstore;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
*/
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.menuguru.playstore.Json_parser.JSONParser;
import pt.menuguru.menuguru.R;
import pt.menuguru.playstore.Utils.Globals;
import pt.menuguru.playstore.Utils.User;



/**
 * Created by hugocosta on 08/09/14.
 */

public class LoginMenuGuru extends Activity
{
    Button LoginB;
    Button Registo;

    EditText edit_email;
    EditText edit_pass;

    EditText recuperar_edit_pass;

    TextView recuperar_pass;

    User[] user = null;

    String aux_user = "0";
    String id_face = "0";
    String email_recup = "";

    String aux_recup_email;

    private ProgressDialog progressDialog;

    private LoginButton loginBtn;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions","email");

    private static String message = "Sample status posted from android app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activitylogin);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_close_b);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);


        actionBar.setTitle("Login");


        LoginB = (Button)findViewById(R.id.bt_login);
        Registo = (Button)findViewById(R.id.registo);

        loginBtn = (LoginButton) findViewById(R.id.bt_loginface);

        edit_email   = (EditText)findViewById(R.id.edit_email);
        edit_pass   = (EditText)findViewById(R.id.edit_pass);

        recuperar_pass = (TextView)findViewById(R.id.text_oferta);

        recuperar_pass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginMenuGuru.this);
                dialog.setTitle(R.string.T_recuperar_pass);
                dialog.setContentView(R.layout.dialog_recuperar_pass);
                dialog.show();

                Button bt_cancelar = (Button) dialog.findViewById(R.id.bt_cancelar);
                Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);

                bt_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });

                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recuperar_edit_pass = (EditText) dialog.findViewById(R.id.rec_email);
                        String text = recuperar_edit_pass.getText().toString();
                        email_recup = text;
                        // Close dialog
                        Log.v("Email Recuperacao", email_recup);
                        if(!isEmailValid(email_recup)){
                            dialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginMenuGuru.this);
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(R.string.erro_email)
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

                        }else {
                            dialog.dismiss();
                            new AsyncTaskParseJsonRecuperar(LoginMenuGuru.this).execute();
                        }
                    }
                });
            }
        });


        LoginB.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        new AsyncTaskParseJson(LoginMenuGuru.this).execute();
                    }
                });

        Registo.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(LoginMenuGuru.this, CriarUser.class);
                        startActivity(intent);
                        LoginMenuGuru.this.overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                    }
                });
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    id_face = user.getId();
                    new AsyncTaskParseJsonFace(LoginMenuGuru.this).execute();
                } else {

                }
            }
        });

        /*

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "pt.menuguru.menuguru6", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_login_confirm.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private LoginMenuGuru delegate;

        public AsyncTaskParseJson (LoginMenuGuru delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginMenuGuru.this);
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

                dict.put("lang","pt");
                dict.put("email",edit_email.getText());
                dict.put("password",edit_pass.getText());

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users


                    User users = new User();

                    String resp = jsonObj.getString("resp");
                    if(resp.equals("email invalido") || resp.equals("ERRO") || resp.equals("Activar conta")){
                        if(resp.equals("Activar conta")){aux_user="3";}else{ aux_user = "0";}
                        Log.v("resp","objecto = "+ jsonObj.getString("resp"));
                        Log.v("msg","objecto = "+ jsonObj.getString("msg"));
                        Log.v("titulo","objecto = "+ jsonObj.getString("titulo"));
                    }else {
                        aux_user = "1";
                        users.userid = jsonObj.getString("userid");
                        users.email = jsonObj.getString("email");
                        users.pnome = jsonObj.getString("pnome");
                        users.snome = jsonObj.getString("snome");
                        users.cidade = jsonObj.getString("cidade");
                        users.telefone_user = jsonObj.getString("telefone_user");
                        users.data_nasc = jsonObj.getString("data_nasc");
                        users.pass = jsonObj.getString("pass");
                        users.tipoconta = jsonObj.getString("tipoconta");
                        String news = jsonObj.getString("news");
                        users.id_face = "0";
                        if (news.equals("1")) {
                            users.news = true;
                        } else {
                            users.news = false;
                        }
                        Globals.get_instance().setUser(users);

                        Log.v("Nome", "objecto = " + jsonObj.getString("pnome"));
                        Log.v("userid","objecto = "+ jsonObj.getString("userid"));
                        Log.v("email","objecto = "+ jsonObj.getString("email"));
                        Log.v("snome","objecto = "+ jsonObj.getString("snome"));
                        Log.v("cidade","objecto = "+ jsonObj.getString("cidade"));
                        Log.v("telefone_user","objecto = "+ jsonObj.getString("telefone_user"));
                        Log.v("data_nasc","objecto = "+ jsonObj.getString("data_nasc"));
                        Log.v("pass","objecto = "+ jsonObj.getString("pass"));
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){
        if(aux_user.equals("0")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.incorrecto)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }else if(aux_user.equals("3")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.conf_email)
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
        }else{
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
            editor.putString("id_face", "0");
            editor.commit();

            finish();
        }


    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonFace extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_login_confirm_face.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private LoginMenuGuru delegate;

        public AsyncTaskParseJsonFace (LoginMenuGuru delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginMenuGuru.this);
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

                dict.put("face_id",id_face);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e)
                {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users


                User users = new User();

                String resp = jsonObj.getString("resp");
                if(resp.equals("email invalido") || resp.equals("ERRO") || resp.equals("Activar conta")){

                    if(resp.equals("Activar conta")){aux_user="3";}else{ aux_user = "0";}
                    Log.v("AUX","objecto = "+ aux_user);
                    Log.v("resp","objecto = "+ jsonObj.getString("resp"));
                    Log.v("msg","objecto = "+ jsonObj.getString("msg"));
                    Log.v("titulo","objecto = "+ jsonObj.getString("titulo"));
                }else {
                    aux_user = "1";
                    users.userid = "0";
                    users.email = jsonObj.getString("email");
                    users.pnome = jsonObj.getString("pnome");
                    users.snome = jsonObj.getString("snome");
                    users.cidade = jsonObj.getString("cidade");
                    users.telefone_user = jsonObj.getString("telefone_user");
                    users.data_nasc = jsonObj.getString("data_nasc");
                    users.pass = jsonObj.getString("pass");
                    users.tipoconta = jsonObj.getString("tipoconta");
                    String news = jsonObj.getString("news");
                    users.id_face = jsonObj.getString("id_face");
                    if (news.equals("1")) {
                        users.news = true;
                    } else {
                        users.news = false;
                    }
                    Globals.get_instance().setUser(users);

                    Log.v("Nome", "objecto = " + jsonObj.getString("pnome"));
                    Log.v("userid","objecto = "+ jsonObj.getString("userid"));
                    Log.v("email","objecto = "+ jsonObj.getString("email"));
                    Log.v("snome","objecto = "+ jsonObj.getString("snome"));
                    Log.v("cidade","objecto = "+ jsonObj.getString("cidade"));
                    Log.v("telefone_user","objecto = "+ jsonObj.getString("telefone_user"));
                    Log.v("data_nasc","objecto = "+ jsonObj.getString("data_nasc"));
                    Log.v("pass","objecto = "+ jsonObj.getString("pass"));
                    Log.v("Face","objecto = "+ jsonObj.getString("id_face"));
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
            delegate.asyncCompleteFace(true);  }

    }


    public void asyncCompleteFace(boolean success){
        if(aux_user.equals("0")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.incorrecto)
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
        }else if(aux_user.equals("3")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.conf_email)
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
        }else{

            // para ir guardar as preferencias de utilizador
            SharedPreferences preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

            User utilizador = Globals.getInstance().getUser();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_name", utilizador.getPnome());
            editor.putString("user_last_name", utilizador.getSnome());
            editor.putString("user_id", "0");
            editor.putString("user_data", utilizador.getData_nasc());
            editor.putString("user_tel", utilizador.getTelefone_user());
            editor.putString("user_mail", utilizador.getEmail());
            editor.putString("user_tipo", utilizador.getTipoconta());
            editor.putString("user_pass", utilizador.getPass());
            editor.putString("user_cidade", utilizador.getCidade());
            editor.putBoolean("user_news", utilizador.getNews());
            editor.putString("id_face", utilizador.getId_face());

            editor.commit();

            finish();
        }

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonRecuperar extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_recuperar_pass2.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private LoginMenuGuru delegate;

        public AsyncTaskParseJsonRecuperar (LoginMenuGuru delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginMenuGuru.this);
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

                dict.put("email",email_recup);
                dict.put("lang", Globals.get_instance().getLingua());


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                String resp = jsonObj.getString("resp");
                if(resp.equals("Receba um email")){
                    aux_recup_email = "0";
                }else{
                    aux_recup_email = "1";
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncCompleteRecuperar(true);  }

    }


    public void asyncCompleteRecuperar(boolean success){
        if(aux_recup_email.equals("1")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setTitle(R.string.t_exite_email)
                    .setMessage(R.string.n_exite_email)
                    .setCancelable(false)
                    .setPositiveButton(R.string.sim,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(LoginMenuGuru.this, CriarUser.class);
                            startActivity(intent);
                            LoginMenuGuru.this.overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
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

        }else{
            finish();
        }


    }

}
