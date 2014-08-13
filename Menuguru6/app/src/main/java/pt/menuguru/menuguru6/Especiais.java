package pt.menuguru.menuguru6;

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
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.MenuEspecial;


public class Especiais extends Fragment implements AbsListView.OnItemClickListener{

    String value;

    MenuEspecial[] some_array = null;

    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapterEspeciais mAdapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void asyncComplete(boolean success){


        // mCallbacks.onButtonClicked();

        mAdapter = new MyListAdapterEspeciais(getActivity(), R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

    }



    public class MyListAdapterEspeciais extends ArrayAdapter<MenuEspecial> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapterEspeciais(Context context, int textViewResourceId,
                                      MenuEspecial[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getActivity().getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.fragment_especiais, parent, false);
            TextView label=(TextView)row.findViewById(R.id.nomeRestaurante);
            label.setText(some_array[position].nome);


            TextView label2=(TextView)row.findViewById(R.id.nomeMenu);
            label2.setText(some_array[position].nome);

            ImageView icon=(ImageView)row.findViewById(R.id.capa);

            //Customize your icon here
            //icon.setImageResource(R.drawable.sem_foto);


            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImage(), icon);



            return row;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTaskParseJson(this).execute();

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
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_especiais_todos.php";
        //String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_pertomin_relevancia_noticiavideo.php";

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
                JSONObject jsonObj = new JSONObject();



                dict.put("lang","pt");
                dict.put("cidade_id", Globals.getInstance().getCidedade_id());





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



                some_array = new MenuEspecial[dataJsonArr.length()];
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);



                    // Storing each json item in variable
                    String firstname = c.getString("nome");

                    // show the values in our logcat
                    Log.v(TAG, "Especial name: " + firstname
                    );


                    MenuEspecial rest = new MenuEspecial();
                    rest.setNome(firstname);

                    //rest.morada = c.getString("morada");
                    //rest.mediarating = c.getString("mediarating");
                    //rest.cidade = c.getString("cidade");
                    rest.urlImage = c.getString("imagem");
                    //rest.votacoes = c.getString("votacoes");
                    //rest.morada = c.getString("morada");
                    //rest.latitude = c.getString("lat");
                    //rest.longitude = c.getString("lon");
                    //rest.precoMedio = c.getString("precomedio");

                    /*
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

                    */
                    some_array[i] = rest;



                }

                //some_array = getResources().getStringArray(R.array.defenicoes_array);

                /*
                // TODO: Change Adapter to display your content
                mAdapter = new MyListAdapterEspeciais(getActivity(), R.layout.row_defenicoes, some_array);



                // Set OnItemClickListener so we can be notified on item clicks
                mListView.setOnItemClickListener(delegate);

                */


                //Log.v("sdffgddvsdsv","objecto = "+ json);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            delegate.asyncComplete(true);
        }
    }



}
