package pt.menuguru.menuguru.Restaurante.Info;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import pt.menuguru.menuguru.Json_parser.JSONParser;
import pt.menuguru.menuguru.R;
import pt.menuguru.menuguru.Utils.Globals;

/**
 * Created by hugocosta on 25/09/14.
 */
public class InfoRestReportarErro extends Activity
{


    private boolean morada_errada = false;
    private boolean fechado = false;
    private boolean ementa_desactualizada = false;

    private String rest_id;
    private String nome_rest;
    private String cidade_nome;

    private EditText detalhes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(getString(R.string.reportar_erro));

        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante_id");
        nome_rest = intent.getStringExtra("nome_rest");
        cidade_nome = intent.getStringExtra("cidade_nome");


        setContentView(R.layout.activity_reportar_erro);


        Button button = (Button) findViewById(R.id.button_enviar_erros);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarErros();
            }
        });


        RelativeLayout morada_er = (RelativeLayout) findViewById(R.id.lay_morada_errada);
        morada_er.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!morada_errada)
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err);
                    icon.setImageResource(R.drawable.ic_action_framecheck_w);

                }else
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err);
                    icon.setImageResource(R.drawable.uncheck);
                }
                morada_errada = !morada_errada;
            }
        });

        RelativeLayout fechad = (RelativeLayout) findViewById(R.id.lay_fechado);
        fechad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fechado)
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err2);
                    icon.setImageResource(R.drawable.ic_action_framecheck_w);

                }else
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err2);
                    icon.setImageResource(R.drawable.uncheck);
                }
                fechado = !fechado;
            }
        });

        RelativeLayout ementD = (RelativeLayout) findViewById(R.id.lay_ementa_des);
        ementD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ementa_desactualizada)
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err3);
                    icon.setImageResource(R.drawable.ic_action_framecheck_w);

                }else
                {
                    ImageView icon = (ImageView) findViewById(R.id.icon_err3);
                    icon.setImageResource(R.drawable.uncheck);
                }
                ementa_desactualizada = !ementa_desactualizada;
            }
        });

        detalhes = (EditText) findViewById(R.id.editText_outros_detalhes);



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

                dict.put("rest_name", nome_rest);
                dict.put("rest_cidade", cidade_nome);
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                dict.put("user_nome", Globals.getInstance().getUser().getPnome());
                dict.put("sugestao", detalhes.getText());

                String sugestao = "";

                if (morada_errada)
                {
                    sugestao = "morada errada, ";
                }
                if (fechado)
                {
                    sugestao = sugestao + "fechado errado, ";
                }
                if (ementa_desactualizada)
                {
                    sugestao = sugestao + "ementa incorrecta, ";
                }

                dict.put("menus",sugestao);

                /*
                    [0]	(null)	@"user_id" : @"862"
                    [1]	(null)	@"sugestao" : @"nada de errado encontrado...\nContinuem o bom trabalho."
                    [2]	(null)	@"rest_cidade" : @"Porto"
                    [3]	(null)	@"menus" : @"morada errada, fechado errado, ementa incorreta, "
                    [4]	(null)	@"face_id" : @"0"
                    [5]	(null)	@"rest_name" : @"O Caçula"
                    [6]	(null)	@"user_nome" : @"Hugo Filipe"
                */




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
