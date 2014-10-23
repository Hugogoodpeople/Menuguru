package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import pt.menuguru.menuguru6.Inspiracoes.Activity_Inspiracao;
import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Restaurante.Restaurante_main;
import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Festival;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;
import pt.menuguru.menuguru6.Utils.TopTitulosFiltros;
import pt.menuguru.menuguru6.Utils.Utils;
import pt.menuguru.menuguru6.os_tres_tipos.Noticia;
import pt.menuguru.menuguru6.os_tres_tipos.lista_festivais_sugestoes;
import pt.menuguru.menuguru6.os_tres_tipos.video;


public class Inicio extends Fragment implements AbsListView.OnItemClickListener {

    String value;

    ComoFunc[] como_array = null;

    Restaurante[] some_array = null;

    Festival[] fest_array = null;

    private MainActivity delegateInicio;
    //private OnFragmentInteractionListener mListener;


    private static String url = "http://10.0.2.2/JSON/";
    //JSON Node Names
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    JSONArray user = null;

    private Boolean loading = false;
    private int actual = 0;
    private int anterior = 0;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapter mAdapter;

    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(anterior - 2);
            }
        });
    }

    public void asyncComplete(boolean success){

        mListView.setAdapter(mAdapter);
        scrollMyListViewToBottom();


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // threshold being indicator if bottom of list is hit
                if (firstVisibleItem  == actual - 2  && loading == false && anterior != actual)
                {
                    Log.v("lermais","carregar mais items para aparecer");
                    loading = true;
                    pullMoreData();
                }
            }
        });
    }

    private void pullMoreData() {
        new AsyncTaskParseJson(this).execute();
    }


    private Callbacks mCallbacks;


    public interface Callbacks {
        //Callback for when button clicked.
        public void onButtonClicked();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks
//        mCallbacks = (Callbacks) activity;

    }



    public class MyListAdapter extends ArrayAdapter<Restaurante>  {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             Restaurante[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getActivity().getApplicationContext());
            myContext = context;
        }

        @Override
        public int getCount(){

            return some_array.length;
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

                RatingBar rating = (RatingBar)row.findViewById(R.id.ratingBar_avaliar);

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

                label4.setText(Utils.getDistance(locationPhone,locationRest));

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_defenicoesteste, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // para colocar quando esta vazia
        mListView.setEmptyView(view.findViewById(R.id.emty_view));

        // Set OnItemClickListener so we can be notified on item clicks
        //mListView.setOnItemClickListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (some_array[position].tipo.equalsIgnoreCase("restaurante"))  {
                    Log.v("clicou no resutaurante","abrir " + some_array[position].getNome());
                    Intent myIntent = new Intent(getActivity(), Restaurante_main.class);
                    myIntent.putExtra("restaurante", some_array[position].getDb_id()); //Optional parameters
                    myIntent.putExtra("urlfoto", some_array[position].getUrlImagem());
                    myIntent.putExtra("nome_rest",some_array[position].getNome());
                    myIntent.putExtra("lat",some_array[position].getLatitude());
                    myIntent.putExtra("lon",some_array[position].getLongitude());
                    myIntent.putExtra("morada",some_array[position].getMorada());
                    myIntent.putExtra("rating",some_array[position].getMediarating());
                    myIntent.putExtra("votacoes",some_array[position].getVotacoes());
                    myIntent.putExtra("cidade_nome", some_array[position].getCidade());
                    myIntent.putExtra("telefone",some_array[position].getTelefone());

                    getActivity().startActivity(myIntent);

                    getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

                }else if(some_array[position].tipo.equalsIgnoreCase("colecao"))
                {
                    Log.v("clicou na noticia", "abrir" + some_array[position].getNome());
                    Intent myIntent = new Intent(getActivity(), lista_festivais_sugestoes.class);
                    myIntent.putExtra("id_colecao", some_array[position].getDb_id());
                    myIntent.putExtra("colecao_nome", some_array[position].getNome());

                    getActivity().startActivity(myIntent);

                    getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                }else if(some_array[position].tipo.equalsIgnoreCase("video"))
                {
                    Intent videoPlaybackActivity = new Intent(getActivity(), video.class);
                    videoPlaybackActivity.putExtra("id_rest", some_array[position].getDb_id());

                    startActivity(videoPlaybackActivity);
                }else if(some_array[position].tipo.equalsIgnoreCase("noticia"))
                {
                    Intent videoPlaybackActivity = new Intent(getActivity(), Noticia.class);
                    videoPlaybackActivity.putExtra("id_noticia", some_array[position].getDb_id());

                    startActivity(videoPlaybackActivity);
                    getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                }


            }

        });


        //--READ data

// tenho de verificar se os filtros estao preenchidos
        // se sim tem fazer outra querie


        new AsyncTaskParseJsonComo(this).execute();

        new AsyncTaskParseJsonDestaque(this).execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(Globals.getInstance().hasFilters == false) {
            new AsyncTaskParseJson(this).execute();
        }
        else {
            actual = 0;
            new AsyncTaskParseJsonFiltros(this).execute();
        }

    }


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ActionBar ab = getActivity().getActionBar();

        String cidade = Globals.getInstance().getCidadeÇ_nome();
        String cidade_id = Globals.getInstance().getCidedade_id();
        if(cidade_id.equalsIgnoreCase("0")) {
            cidade = getString(R.string.perto_de_mim);
        }

        ab.setTitle(cidade);
       // ab.setSubtitle("sub-title");

        if(!Utils.isOnline(getActivity())) {
            Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
            getActivity().finish(); //Calling this method to close this activity when internet is not available.
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_localizacao:
                Intent myIntent = new Intent(getActivity(), Localizacao.class);
                getActivity().startActivity(myIntent);
                return false;
            case R.id.action_pesquisa:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder
                        .setItems(R.array.pesquisa, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {

                                        Intent myIntent = new Intent(getActivity(), Filtros_mega_avancados.class);
                                        startActivity(myIntent);

                                        break;
                                    }
                                    case 1: {
                                        Intent myIntent2 = new Intent(getActivity(), Activity_Inspiracao.class);
                                        startActivity(myIntent2);
                                        break;
                                    }
                                    case 2:

                                    default:
                                        break;
                                }
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;


            default:
                break;
        }

        return false;
    }


    // para o scrool


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Inicio delegate;

        // set your json string url here
        //String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_especiais_todos.php";
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_pertomin_relevancia_noticiavideo.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Inicio delegate){
            this.delegate = delegate;
        }


        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();

        }



        private Object[] appendValue(Object[] obj, Object newObj) {

            ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
            temp.add(newObj);
            return temp.toArray();

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


                dict.put("inicio",actual);
                //dict.put("not","pt");
                dict.put("lang",Globals.getInstance().getLingua());
                dict.put("cidade_id", Globals.getInstance().cidedade_id);
                dict.put("lon", Globals.getInstance().getLongitude());
                dict.put("ordem","relevancia");
                //dict.put("ordem","distancia");
                dict.put("user_id","0");
                dict.put("lat",Globals.getInstance().getLatitude());
                dict.put("face_id","0");

                int contagem = 0;

                if(actual == 0)
                {
                    some_array = null;
                }

                if(some_array != null)
                {
                    //for (Restaurante item : some_array)
                    for(int i = 0 ; i<some_array.length ; i++)
                    {
                        Restaurante item = some_array[i];

                        if (!item.tipo.equalsIgnoreCase("restaurante"))
                        {
                            contagem++;
                        }
                    }
                }

                dict.put("not",contagem);


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Retorno inicio"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("res");

                // loop through all users


               // tenho de gravar os valores antrriores aqui
                if(some_array == null)
                {
                    some_array = new Restaurante[dataJsonArr.length()];
                }
                else {

                    Restaurante[] tempArray = some_array;

                    some_array = new Restaurante[dataJsonArr.length() + some_array.length ];

                    for (int z = 0; z<tempArray.length ; z++)
                    {
                        some_array[z] = tempArray[z];
                    }
                }


                anterior = actual;

                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);



                    // Storing each json item in variable
                    String firstname = c.getString("nome");

                    // show the values in our logcat
                    //Log.v(TAG, "firstname: " + firstname + " votos = " + c.getString("votacoes"));


                    Restaurante rest = new Restaurante();
                    rest.setNome(firstname);

                    rest.morada = c.getString("morada");
                    //rest.mediarating = c.getString("mediarating");
                    rest.cidade = c.getString("cidade");
                    rest.urlImagem = c.getString("imagem");

                    rest.morada = c.getString("morada");
                    //rest.precoMedio = c.getString("precomedio");
                    rest.db_id = c.getString("id");

                    rest.tipo = c.getString("tipo");

                    if (rest.tipo.equalsIgnoreCase("restaurante")) {
                        rest.votacoes = c.getString("votacoes");
                        rest.latitude = c.getString("lat");
                        rest.longitude = c.getString("lon");
                        rest.mediarating = c.getString("mediarating");
                        rest.precoMedio = c.getString("precomedio");
                        rest.telefone = c.getString("telefone");

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

                    some_array[i+ actual] = rest;
                    // some_array =  appendValue(some_array, rest);
                    //some_array = newObj;


                }

                //some_array = getResources().getStringArray(R.array.defenicoes_array);

                // TODO: Change Adapter to display your content
               // if (actual == 0)
               //     mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);
                //else
                //    mAdapter.notifyDataSetChanged();


                // Set OnItemClickListener so we can be notified on item clicks
                //mListView.setOnItemClickListener(delegate);


                actual =actual + dataJsonArr.length();


                Log.v("sdffgddvsdsv","objecto especial = "+ jsonObj);

                loading = false;

                mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncComplete(true);
        }
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonComo extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_comofunciona.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Inicio delegate;

        public AsyncTaskParseJsonComo (Inicio delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
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

                dict.put("lang", Globals.get_instance().getLingua());
                dict.put("tamanho", "640x1136");
                dict.put("versao", "");



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                dataJsonArr = jsonObj.getJSONArray("res");
                como_array = new ComoFunc[dataJsonArr.length()];


                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    ComoFunc cfunc = new ComoFunc();

                    cfunc.setId(c.getString("id"));
                    cfunc.setImg1(c.getString("img1"));
                    cfunc.setImg2(c.getString("img2"));


                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("id"));
                    Log.v("IMG1","objecto = "+ c.getString("img1"));
                    Log.v("IMG2","objecto = "+ c.getString("img2"));

                    como_array[i] = cfunc;
                }

                Globals.getInstance().setCfunc(como_array);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            progressDialog.dismiss();delegate.asyncCompleteComo(true);
        }

    }


    public void asyncCompleteComo(boolean success){

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonDestaque extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_listagem_festivais.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Inicio delegate;

        public AsyncTaskParseJsonDestaque (Inicio delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                dict.put("lang", Globals.get_instance().getLingua());
                dict.put("tamanho", "640x1136");
                dict.put("versao", "");



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                dataJsonArr = jsonObj.getJSONArray("res");
                fest_array = new Festival[dataJsonArr.length()];


                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    Festival fest = new Festival();

                    fest.setId(c.getString("id"));
                    fest.setButaodois(c.getString("butaodois"));
                    fest.setButaoum(c.getString("butaoum"));
                    fest.setImagem(c.getString("imagem"));
                    fest.setFundo(c.getString("fundo"));
                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("id"));
                    Log.v("botao2","objecto = "+ c.getString("butaodois"));
                    Log.v("botao1","objecto = "+ c.getString("butaoum"));

                    fest_array[i] = fest;
                }

                Globals.getInstance().setFestival(fest_array);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();
            if(delegate != null)
            delegate.asyncCompleteDestaque(true);  }

    }


    public void asyncCompleteDestaque(boolean success){

    }







    public void asyncCompleteResultadosFiltros(boolean success){


       // mListView = (ListView) findViewById(R.id.listViewResultadoInspiras);


       // mListView.setEmptyView(findViewById(R.id.emty_view));
/*
        mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Log.v("clicou no resutaurante","abrir " + some_array[position].getNome());
                Intent myIntent = new Intent(getActivity(), Restaurante_main.class);
                myIntent.putExtra("restaurante", some_array[position].getDb_id()); //Optional parameters
                myIntent.putExtra("urlfoto", some_array[position].getUrlImagem());
                myIntent.putExtra("nome_rest",some_array[position].getNome());
                myIntent.putExtra("lat",some_array[position].getLatitude());
                myIntent.putExtra("lon",some_array[position].getLongitude());
                myIntent.putExtra("morada",some_array[position].getMorada());
                myIntent.putExtra("rating",some_array[position].getMediarating());
                myIntent.putExtra("votacoes",some_array[position].getVotacoes());
                myIntent.putExtra("cidade_nome", some_array[position].getCidade());
                myIntent.putExtra("telefone",some_array[position].getTelefone());

                startActivity(myIntent);

                getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

            }

        });

        // Assign adapter to ListView


        mListView.setAdapter(mAdapter);

*/
        mAdapter.notifyDataSetChanged();
        Globals.getInstance().setHasFilters(false);

    }



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonFiltros extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_pertomim_relevancia.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Inicio delegate;

        public AsyncTaskParseJsonFiltros (Inicio delegate)
        {
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                dict.put("lon", Globals.getInstance().getLongitude());
                dict.put("lat",Globals.getInstance().getLatitude());

                // ok aqui começa a festa... são 17 chaves de informaçao que tenho de enviar

                dict.put("cidade_id",Globals.getInstance().getCidedade_id());
                dict.put("aberto","0");
                dict.put("take","0");
                dict.put("pagina","0");
                dict.put("prato",Globals.getInstance().getTextoPesquisado());
                dict.put("tipo_pesquisa", Globals.getInstance().getTipoRestPrat());


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
                    Log.v(TAG, "firstname: " + firstname);


                    Restaurante rest = new Restaurante();
                    rest.setNome(firstname);

                    rest.morada = c.getString("morada");
                    rest.mediarating = c.getString("mediarating");
                    //rest.cidade = c.getString("cidade");
                    rest.urlImagem = c.getString("imagem");
                    rest.votacoes = c.getString("votacoes");
                    //rest.morada = c.getString("morada");
                    //rest.precoMedio = c.getString("precomedio");

                    rest.tipo = "restaurante";

                   // if (rest.tipo.equalsIgnoreCase("restaurante"))
                    {
                        rest.latitude = c.getString("lat");
                        rest.longitude = c.getString("lon");
                        rest.mediarating = c.getString("mediarating");
                        rest.precoMedio = c.getString("precomedio");
                        rest.db_id = c.getString("id");
                        rest.telefone = c.getString("telefone");

                        JSONArray cozinhas = c.getJSONArray("cozinhas");

                        for (int z = 0; z < cozinhas.length(); z++)
                        {
                            JSONObject cozinha = cozinhas.getJSONObject(z);
                            if (cozinhas.length() - 1 > z)
                            {

                                rest.cosinhas = rest.cosinhas + cozinha.getString("cozinhas_nome") + ", ";

                            }
                            else
                            {

                                rest.cosinhas = rest.cosinhas + "" + cozinha.getString("cozinhas_nome");

                            }
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
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncCompleteResultadosFiltros(true);  }

    }


}
