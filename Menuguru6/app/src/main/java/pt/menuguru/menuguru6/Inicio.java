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
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import pt.menuguru.menuguru6.Inspiracoes.Activity_Inspiracao;
import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;
import pt.menuguru.menuguru6.Utils.Utils;


public class Inicio extends Fragment implements AbsListView.OnItemClickListener {

    String value;

    Restaurante[] some_array = null;

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void asyncComplete(boolean success){


       // mCallbacks.onButtonClicked();


        //if (actual == 9)
        {
            mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);
            mListView.setAdapter(mAdapter);
            mListView.setSelection(anterior-1);
        }
        /*
        else
        {
            mAdapter.notifyDataSetChanged();
        }
        */
        /*
        mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        */




        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // threshold being indicator if bottom of list is hit
                if (firstVisibleItem  == actual - 2  && loading == false && anterior != actual) {

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

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


// we will using AsyncTask during parsing
        new AsyncTaskParseJson(this).execute();



        return view;
    }





    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(Globals.getInstance().getCidadeÇ_nome());
       // ab.setSubtitle("sub-title");

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
                myIntent.putExtra("local", value); //Optional parameters
                getActivity().startActivity(myIntent);
                this.getActivity().finish();
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


                                        /*
                                        Intent myIntent2 = new Intent(getActivity(), Activity_Inspiracao.class);
                                        startActivity(myIntent2);
                                        */

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
            progressDialog.setCancelable(true);
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
                dict.put("not","pt");
                dict.put("lang","pt");
                dict.put("cidade_id", Globals.getInstance().cidedade_id);
                dict.put("lon", Globals.getInstance().getLongitude());
                //dict.put("ordem","relevancia");
                dict.put("ordem","distancia");
                dict.put("user_id","0");
                dict.put("lat",Globals.getInstance().getLatitude());
                dict.put("face_id","0");


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna isto"+jsonString);
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
                    Log.v(TAG, "firstname: " + firstname + " votos = " + c.getString("votacoes"));


                    Restaurante rest = new Restaurante();
                    rest.setNome(firstname);

                    rest.morada = c.getString("morada");
                    //rest.mediarating = c.getString("mediarating");
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
                mListView.setOnItemClickListener(delegate);


                actual =actual + dataJsonArr.length();


                Log.v("sdffgddvsdsv","objecto especial = "+ jsonObj);

                loading = false;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();delegate.asyncComplete(true);
        }
    }




}
