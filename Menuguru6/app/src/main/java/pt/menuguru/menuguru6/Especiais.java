package pt.menuguru.menuguru6;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;


public class Especiais extends Fragment implements AbsListView.OnItemClickListener{

    String value;

    Restaurante[] some_array = null;

    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapter mAdapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }










    public class MyListAdapter extends ArrayAdapter<Restaurante> {

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
            View row=inflater.inflate(R.layout.fragment_especiais, parent, false);
            TextView label=(TextView)row.findViewById(R.id.textView);
            label.setText(some_array[position].nome);


            TextView label2=(TextView)row.findViewById(R.id.textView2);
            label2.setText(some_array[position].cosinhas);

            ImageView icon=(ImageView)row.findViewById(R.id.capa);

            //Customize your icon here
            //icon.setImageResource(R.drawable.sem_foto);


            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), icon);



            return row;
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_defenicoesteste, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

       //
       // new AsyncTaskParseJson(this).execute();

        return view;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Especiais delegate;

        // set your json string url here
        //String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_especiais_todos.php";
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_pertomin_relevancia_noticiavideo.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Especiais delegate){
            this.delegate = delegate;
        }


        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {

            try {

                // instantiate our json parser
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();


                dict.put("inicio","0");
                dict.put("not","pt");
                dict.put("lang","pt");
                dict.put("cidade_id","0");
                dict.put("lon","-8.30983");
                dict.put("ordem","relevancia");
                dict.put("user_id","0");
                dict.put("lat","41.3764");
                dict.put("face_id","0");



               // String json = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // get the array of users

                //dataJsonArr = json.getJSONArray("res");

                // loop through all users




                some_array = new Restaurante[dataJsonArr.length()];
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    /*
                    imagem = "/imagens_menuguru/restaurantes/pequena/custodio_a.jpg";
                    lat = "41.3865250";
                    lon = "-8.3102292";
                    mediarating = "4.2";
                    morada = "Rua Pena de Galo, 220 \n4815-516 Vizela";
                    nome = "Casa de Pasto Cust\U00f3dio";
                    pag = "";
                    precomedio = "Almo\U00e7o At\U00e9 15\U20ac | Jantar At\U00e9 15\U20ac";
                    telefone = "+351253587584";
                    tipo = restaurante;
                    votacoes = 5;

                    * */

                    // Storing each json item in variable
                    String firstname = c.getString("nome");

                    // show the values in our logcat
                    Log.v(TAG, "firstname: " + firstname
                    );


                    Restaurante rest = new Restaurante();
                    rest.setNome(firstname);

                    rest.morada = c.getString("morada");
                    //rest.mediarating = c.getString("mediarating");
                    //rest.cidade = c.getString("cidade");
                    rest.urlImagem = c.getString("imagem");
                    //rest.votacoes = c.getString("votacoes");
                    //rest.morada = c.getString("morada");
                    //rest.latitude = c.getString("lat");
                    //rest.longitude = c.getString("lon");
                    //rest.precoMedio = c.getString("precomedio");

                    JSONArray cozinhas = c.getJSONArray("cozinhas");

                    for (int z = 0; z < cozinhas.length(); z++)
                    {
                        JSONObject cozinha = cozinhas.getJSONObject(z);
                        if (cozinhas.length()-1 > z)
                            rest.cosinhas = rest.cosinhas + cozinha.getString("cozinhas_nome")+ ", ";
                        else
                            rest.cosinhas = rest.cosinhas + " " + cozinha.getString("cozinhas_nome");
                    }
                    //rest.cosinhas = rest.cosinhas.substring(0, rest.cosinhas.length() - 1);


                    some_array[i] = rest;



                }

                //some_array = getResources().getStringArray(R.array.defenicoes_array);

                // TODO: Change Adapter to display your content
                mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);



                // Set OnItemClickListener so we can be notified on item clicks
                mListView.setOnItemClickListener(delegate);




                //Log.v("sdffgddvsdsv","objecto = "+ json);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
           // delegate.asyncComplete(true);
        }
    }



}
