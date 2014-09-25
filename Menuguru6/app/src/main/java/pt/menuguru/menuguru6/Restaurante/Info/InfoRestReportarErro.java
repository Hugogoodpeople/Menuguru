package pt.menuguru.menuguru6.Restaurante.Info;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;

/**
 * Created by hugocosta on 25/09/14.
 */
public class InfoRestReportarErro extends Activity
{
    private String rest_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(getString(R.string.reportar_erro));

        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante_id");


        setContentView(R.layout.activity_reportar_erro);


        Button button = (Button) findViewById(R.id.button_enviar_erros);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarErros();
            }
        });
    }

    public void enviarErros()
    {
        new AsyncTaskParseJsonEnviarErros(this).execute();
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

    // para enviar email com o report de erros



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonEnviarErros extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_email_interesse_menu.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private InfoRestReportarErro delegate;

        public AsyncTaskParseJsonEnviarErros (InfoRestReportarErro delegate){
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

                dict.put("lang", Globals.getInstance().getLingua());

                dict.put("id_rest",rest_id);

                /* exemplo das chaves que tenho de enviar
                [dict setObject:restaurante.name forKey:@"rest_name"];
                [dict setObject:restaurante.city forKey:@"rest_cidade"];

                [dict setObject: [Globals user].name forKey:@"user_nome"];
                [dict setObject:restaurante.name forKey:@"menus"];
                [dict setObject: self.textArea.text forKey:@"sugestao"];

                if([Globals user].faceId)
                {
                    [dict setObject: [Globals user].faceId forKey:@"face_id"];
                    [dict setObject: [NSString stringWithFormat:@"%d", 0] forKey:@"user_id"];
                }else
                {
                    [dict setObject: [NSString stringWithFormat:@"%d", 0] forKey:@"face_id"];
                    [dict setObject: [NSString stringWithFormat:@"%d", [Globals user].dbId] forKey:@"user_id"];
                }

                [dict setObject: interessado forKey:@"menus"];
                * */

                /*
                * preciso ir buscar
                * rest_name
                * rest_cidade  -> tenho de ver se é o id ou nome
                * user_name
                * menus
                * sugestao     -> contem texto simples com a descriçao do que o utilizador quer
                * face_id
                * user_id
                *   [0]	(null)	@"user_id" : @"862"
                    [1]	(null)	@"sugestao" : @"nada de errado encontrado...\nContinuem o bom trabalho."
                    [2]	(null)	@"rest_cidade" : @"Porto"
                    [3]	(null)	@"menus" : @"morada errada, fechado errado, ementa incorreta, "
                    [4]	(null)	@"face_id" : @"0"
                    [5]	(null)	@"rest_name" : @"O Caçula"
                    [6]	(null)	@"user_nome" : @"Hugo Filipe"
                * */




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


                // nao preciso de receber nada aqui... apenas envia informação

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteErros(true);
        }

    }

    public void asyncCompleteErros(boolean success)
    {

        finish();
        overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);

    }
}
