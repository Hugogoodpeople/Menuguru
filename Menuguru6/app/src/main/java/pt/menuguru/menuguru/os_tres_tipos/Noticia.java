package pt.menuguru.menuguru.os_tres_tipos;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru.Json_parser.JSONParser;
import pt.menuguru.menuguru.MyApplication;
import pt.menuguru.menuguru.R;
import pt.menuguru.menuguru.Utils.ImageLoader;

/**
 * Created by hugocosta on 16/10/14.
 */
public class Noticia extends Activity{

    private String id_noticia;

    private String titulo_noticia;
    private String conteudo_noticia;
    private String urlImagem;

    public ImageLoader imageLoader;
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

        setContentView(R.layout.noticia);


        //setTitle(value);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id_noticia = intent.getStringExtra("id_noticia");

        new AsyncTaskParseJsonVideo(this).execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }

    // o que recebo do json das noticias
    //-(void)tocarVideo:(NSString *)url
    /*
    NSLog(@"resultado das noticias %@", result.description);
    NSString * urlImagem = [[result objectForKey:@"res"] objectForKey:@"imagem"];
    NSString * titulo = [[result objectForKey:@"res"] objectForKey:@"titulo"];
    NSString * conteudo = [[result objectForKey:@"res"] objectForKey:@"corponoticia"];
    */

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonVideo extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_noticia_abrir.php";


        JSONObject dataJason = null;

        private Noticia delegate;

        public AsyncTaskParseJsonVideo (Noticia delegate){
            this.delegate = delegate;
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



                dict.put("id_noticia",id_noticia);
                dict.put("lang","pt");

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado do video = " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna para o video"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);


                // loop through all users



                urlImagem = outro.getString("imagem");
                titulo_noticia = outro.getString("titulo");
                conteudo_noticia = outro.getString("corponoticia");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteNoticia(true);
        }

    }

    public void asyncCompleteNoticia(boolean success)
    {
        imageLoader=new ImageLoader(this.getApplicationContext());

        ImageView imagem = (ImageView) findViewById(R.id.imagem_noticia);

        imageLoader.DisplayImage("http://menuguru.pt" + urlImagem, imagem);

        TextView titulo = (TextView) findViewById(R.id.titulo_noticia);
        titulo.setText(titulo_noticia);

        TextView contudo = (TextView) findViewById(R.id.texto_noticia);
        contudo.setText(conteudo_noticia);

    }



}
