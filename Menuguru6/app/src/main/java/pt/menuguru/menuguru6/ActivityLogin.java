package pt.menuguru.menuguru6;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.User;

/**
 * Created by hugocosta on 08/09/14.
 */

public class ActivityLogin extends Activity
{
    Button LoginB;
    Button Registo;

    EditText edit_email;
    EditText edit_pass;

    User[] user = null;

    String aux_user = "0";
    String id_face = "0";

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
        ActionBar ab = getActionBar();
        ab.setTitle("Login");
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LoginB = (Button)findViewById(R.id.bt_login);
        Registo = (Button)findViewById(R.id.registo);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);

        edit_email   = (EditText)findViewById(R.id.edit_email);
        edit_pass   = (EditText)findViewById(R.id.edit_pass);

        LoginB.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        new AsyncTaskParseJson(ActivityLogin.this).execute();
                    }
                });

        Registo.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(ActivityLogin.this, CriarUser.class);
                        startActivity(intent);
                        ActivityLogin.this.overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                    }
                });
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    id_face = user.getId();
                    new AsyncTaskParseJsonFace(ActivityLogin.this).execute();
                } else {

                }
            }
        });


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

        private ActivityLogin delegate;

        public AsyncTaskParseJson (ActivityLogin delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogin.this);
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
                        aux_user = "0";
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

        private ActivityLogin delegate;

        public AsyncTaskParseJsonFace (ActivityLogin delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogin.this);
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
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users


                User users = new User();

                String resp = jsonObj.getString("resp");
                if(resp.equals("email invalido") || resp.equals("ERRO") || resp.equals("Activar conta")){
                    aux_user = "0";
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

            editor.commit();

            finish();
        }


    }

}
