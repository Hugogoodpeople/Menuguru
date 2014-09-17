package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;
import pt.menuguru.menuguru6.Utils.TopTitulosFiltros;
import pt.menuguru.menuguru6.Utils.Utils;

/**
 * Created by hugocosta on 15/09/14.
 */
public class Resultados_filtros_avancados extends Activity
{

    Restaurante[] some_array = null;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapter mAdapter;

    private String prato= "";
    private String tipo = "1";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_inspiracao);
        Intent intent = getIntent();
        prato = intent.getStringExtra("prato");
        tipo = intent.getStringExtra("tipo");

        //setTitle(value);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setCustomView(R.layout.tab_header);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView t =(TextView) findViewById(R.id.mytext);

        if (prato.length()>0)
            t.setText(prato);
        else
            t.setText(getString(R.string.filtrosavancados));


        new AsyncTaskParseJson(this).execute();
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



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_pertomim_relevancia.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Resultados_filtros_avancados delegate;

        public AsyncTaskParseJson (Resultados_filtros_avancados delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Resultados_filtros_avancados.this);
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
                dict.put("lon", Globals.getInstance().getLongitude());
                dict.put("lat",Globals.getInstance().getLatitude());

                // ok aqui começa a festa... são 17 chaves de informaçao que tenho de enviar

                dict.put("cidade_id",Globals.getInstance().getCidedade_id());
                dict.put("aberto","0");
                dict.put("take","0");
                dict.put("pagina","0");
                dict.put("prato",prato);
                dict.put("tipo_pesquisa", tipo);


                // primeira informaçao vinda de array
                TopTitulosFiltros[] filtros = Globals.getInstance().getFiltros();
                // na primeira posição posição é onde temos a informação relativa a ordem pretendida

                int indexSelecionado = 0;
                for(int i = 0 ; i<filtros[0].getArrayObjectos().length ; i++)
                {
                    if (filtros[0].getArrayObjectos()[i].getSelecionado())
                    {
                        indexSelecionado = i;
                    }

                }

                String ordem = "relevancia";

                // $body['ordem'];//"relevancia";//"popularidade";//"distancia";//"ofertas";
                switch (indexSelecionado) {
                    case 0:{
                        ordem ="relevancia";
                        break;
                    }
                    case 1:{
                        ordem ="popularidade";
                        break;
                    }
                    case 2:{
                        ordem ="distancia";
                        break;
                    }
                    case 3:{
                        ordem ="ofertas";
                        break;
                    }
                    default:
                        break;
                }


                dict.put("ordem",ordem);

                // para as cozinhas selecinadas agora
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 1 ; i < filtros[1].getArrayObjectos().length ; i++)
                {
                    if (filtros[1].getArrayObjectos()[i].getSelecionado() != null && filtros[1].getArrayObjectos()[i].getSelecionado())
                    list.add(filtros[1].getArrayObjectos()[i].getId_sub_titulo());

                }


                dict.put("cozinha", new JSONArray(list.toString()));


                // para o preço almoço recebe uma string simples

                String preco_alm = "";
                for(int i = 1 ; i < filtros[2].getArrayObjectos().length ; i++)
                {
                    if (filtros[2].getArrayObjectos()[i].getSelecionado())
                    {
                        preco_alm = filtros[2].getArrayObjectos()[i].getId_sub_titulo();
                    }
                }

                dict.put("preco_alm", preco_alm);


                // para o preço jantar recebe uma string simples

                String preco_jantar = "";
                for(int i = 0 ; i < filtros[3].getArrayObjectos().length ; i++)
                {
                    if (filtros[3].getArrayObjectos()[i].getSelecionado())
                    {
                        preco_alm = filtros[3].getArrayObjectos()[i].getId_sub_titulo();
                    }
                }

                dict.put("preco_jant", preco_alm);

                // para as opçoes adicionais
                ArrayList<String> listAdd = new ArrayList<String>();
                for (int i = 1 ; i < filtros[4].getArrayObjectos().length ; i++)
                {
                    if (filtros[4].getArrayObjectos()[i].getSelecionado() != null && filtros[4].getArrayObjectos()[i].getSelecionado())
                        listAdd.add(filtros[4].getArrayObjectos()[i].getId_sub_titulo());

                }


                dict.put("opc_add", new JSONArray(listAdd.toString()));

                // para as opçoes adicionais
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> listOpc = new ArrayList<String>();
                for (int i = 0 ; i < filtros[5].getArrayObjectos().length ; i++)
                {
                    if (filtros[5].getArrayObjectos()[i].getSelecionado() != null && filtros[5].getArrayObjectos()[i].getSelecionado())
                        listOpc.add(filtros[5].getArrayObjectos()[i].getId_sub_titulo());

                }

                dict.put("opc_pag", new JSONArray(listOpc.toString()));


                // para ambiente
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> listIdeal = new ArrayList<String>();
                for (int i = 1 ; i < filtros[6].getArrayObjectos().length ; i++)
                {
                    if (filtros[6].getArrayObjectos()[i].getSelecionado() != null && filtros[6].getArrayObjectos()[i].getSelecionado())
                        listIdeal.add(filtros[6].getArrayObjectos()[i].getId_sub_titulo());

                }

                dict.put("ideal", new JSONArray(listIdeal.toString()));



                // para ambiente
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> listAmbiente = new ArrayList<String>();
                for (int i = 1 ; i < filtros[7].getArrayObjectos().length ; i++)
                {
                    if (filtros[7].getArrayObjectos()[i].getSelecionado() != null && filtros[7].getArrayObjectos()[i].getSelecionado())
                        listAmbiente.add(filtros[7].getArrayObjectos()[i].getId_sub_titulo());

                }


                dict.put("ambiente", new JSONArray(listAmbiente.toString()));



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("res");

                // loop through all users


                some_array = new Restaurante[dataJsonArr.length()];
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);



                    // Storing each json item in variable
                    String firstname = c.getString("nome");

                    // show the values in our logcat
                    Log.v(TAG, "firstname: " + firstname
                    );


                    Restaurante rest = new Restaurante();
                    rest.setNome(firstname);

                    rest.morada = c.getString("morada");
                    rest.mediarating = c.getString("mediarating");
                    //rest.cidade = c.getString("cidade");
                    rest.urlImagem = c.getString("imagem");
                    rest.votacoes = c.getString("votacoes");
                    //rest.morada = c.getString("morada");
                    //rest.precoMedio = c.getString("precomedio");

                    rest.tipo = c.getString("tipo");

                    if (rest.tipo.equalsIgnoreCase("restaurante")) {
                        rest.latitude = c.getString("lat");
                        rest.longitude = c.getString("lon");
                        rest.mediarating = c.getString("mediarating");
                        rest.precoMedio = c.getString("precomedio");

                        JSONArray cozinhas = c.getJSONArray("cozinhas");

                        for (int z = 0; z < cozinhas.length(); z++) {
                            JSONObject cozinha = cozinhas.getJSONObject(z);
                            if (cozinhas.length() - 1 > z)
                                rest.cosinhas = rest.cosinhas + cozinha.getString("cozinhas_nome") + ", ";
                            else
                                rest.cosinhas = rest.cosinhas + "" + cozinha.getString("cozinhas_nome");
                        }
                        //rest.cosinhas = rest.cosinhas.substring(0, rest.cosinhas.length() - 1);
                    }

                    some_array[i] = rest;

                }

                //some_array = getResources().getStringArray(R.array.defenicoes_array);



                Log.v("sdffgddvsdsv","objecto especial = "+ jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }

    public void asyncComplete(boolean success){


        // mCallbacks.onButtonClicked();

        mListView = (ListView) findViewById(R.id.listViewResultadoInspiras);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);


        mListView.setEmptyView(findViewById(R.id.emty_view));

        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

    }

    public class MyListAdapter extends ArrayAdapter<Restaurante> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             Restaurante[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(context);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = null;


            if (some_array[position].tipo.equalsIgnoreCase("restaurante")) {

                row=inflater.inflate(R.layout.fragment_inicio, parent, false);
                TextView label2=(TextView)row.findViewById(R.id.nomeMenu);
                label2.setText(some_array[position].cosinhas);

                TextView label3=(TextView)row.findViewById(R.id.desconto);
                label3.setText(some_array[position].precoMedio);

                TextView label=(TextView)row.findViewById(R.id.nomeRestaurante);
                label.setText(some_array[position].nome);

                ImageView icon=(ImageView)row.findViewById(R.id.capa);

                RatingBar rating = (RatingBar)row.findViewById(R.id.ratingBar);

                rating.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                rating.setFocusable(false);

                rating.setRating( Float.parseFloat(some_array[position].mediarating));

                TextView label4 = (TextView) row.findViewById(R.id.distancia);
                label4.setText("ND");

                TextView label5 = (TextView) row.findViewById(R.id.votosnumber);
                label5.setText("(" + some_array[position].getVotacoes() + ")");

                Location locationRest = new Location("");
                locationRest.setLatitude(Double.parseDouble(some_array[position].latitude));
                locationRest.setLongitude(Double.parseDouble(some_array[position].longitude));

                Location locationPhone = new Location("");
                locationPhone.setLatitude(Double.parseDouble(Globals.getInstance().getLatitude()));
                locationPhone.setLongitude(Double.parseDouble(Globals.getInstance().getLongitude()));

                label4.setText(Utils.getDistance(locationPhone, locationRest));


                imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), icon);

            }else
            {
                row=inflater.inflate(R.layout.fragment_row_colecao, parent, false);
                ImageView icon=(ImageView)row.findViewById(R.id.imagemColecao);
                imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), icon);
            }
            return row;
        }

    }
}
