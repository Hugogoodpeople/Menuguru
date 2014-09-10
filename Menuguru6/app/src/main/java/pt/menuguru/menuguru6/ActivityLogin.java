package pt.menuguru.menuguru6;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.Inspiracao;
import pt.menuguru.menuguru6.Utils.InspiracaoItem;
import pt.menuguru.menuguru6.Utils.Locais;
import pt.menuguru.menuguru6.Utils.User;

/**
 * Created by hugocosta on 08/09/14.
 */

public class ActivityLogin extends Activity
{
    Button LoginB;
    Button LoginBface;
    EditText edit_email;
    EditText edit_pass;

    User[] user = null;

    String aux_user = "0";

    public Defenicoes delegate;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogin);
        ActionBar ab = getActionBar();
        ab.setTitle("Login");

        LoginB = (Button)findViewById(R.id.bt_login);
        LoginBface = (Button)findViewById(R.id.bt_face);
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


            finish();

        }


    }

}
