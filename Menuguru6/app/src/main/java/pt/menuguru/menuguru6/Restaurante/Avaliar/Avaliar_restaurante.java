package pt.menuguru.menuguru6.Restaurante.Avaliar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;

/**
 * Created by hugocosta on 26/09/14.
 */
public class Avaliar_restaurante extends Activity
{
    private String rest_id;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante");

        String media_rating = intent.getStringExtra("rating");

        actionBar.setTitle(getString(R.string.avaliar));

        setContentView(R.layout.avaliar_restaurante);

        rating = (RatingBar) findViewById(R.id.ratingBar_avaliar);
        rating.setRating(Float.parseFloat(media_rating));

        Button button_avaliar_comentar = (Button) findViewById(R.id.button_avaliar_comentar);
        button_avaliar_comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avaliar_comentar();
            }
        });



    }

    private void avaliar_comentar(){ new AsyncTaskParseJsonAvaliar(this).execute();}

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


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_criarupdate_rating.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Avaliar_restaurante delegate;

        public AsyncTaskParseJsonAvaliar (Avaliar_restaurante delegate){
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
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                dict.put("rating", Float.toString(rating.getRating()));

                /*
                [0]	(null)	@"face_id" : @"0"
                [1]	(null)	@"user_id" : @"862"
                [2]	(null)	@"id_rest" : @"819"
                [3]	(null)	@"rating" : @"3"
                 */

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado do rating = " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna esto do rating"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("res");


/*
                comentarios = new ArrayList<Comentario>();

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


                    comentarios.add(comentario);
                }
*/



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteAvaliar(true);
        }

    }

    private void asyncCompleteAvaliar(boolean success)
    {
        Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        overridePendingTransition( R.anim.abc_fade_in , R.anim.abc_slide_out_bottom);


    }

}
