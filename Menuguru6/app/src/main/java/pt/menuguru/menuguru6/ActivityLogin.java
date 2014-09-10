package pt.menuguru.menuguru6;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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

/**
 * Created by hugocosta on 08/09/14.
 */

public class ActivityLogin extends Activity
{
    Button LoginB;
    Button LoginBface;
    EditText edit_email;
    EditText edit_pass;

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

                // get the array of users

                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+jsonString);
                }

                dataJsonArr = jsonObj.getJSONArray("resp");

                Log.v("JsonObject","objecto = "+ jsonObj);
                // loop through all users
                Log.v("JsonObject","objecto = "+ jsonObj);

                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Log.v("Nome","objecto = "+ c.getString("nome"));
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



    }

}
