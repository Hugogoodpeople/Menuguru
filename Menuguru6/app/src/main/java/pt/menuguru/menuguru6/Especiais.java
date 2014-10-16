package pt.menuguru.menuguru6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import pt.menuguru.menuguru6.Utils.Menu_do_restaurante;
import pt.menuguru.menuguru6.Utils.Restaurante;


public class Especiais extends Fragment implements AbsListView.OnItemClickListener{

    String value;

    Menu_do_restaurante[] some_array = null;

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

        mAdapter.notifyDataSetChanged();

    }



    public class MyListAdapterEspeciais extends ArrayAdapter<Menu_do_restaurante> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapterEspeciais(Context context, int textViewResourceId,
                                      Menu_do_restaurante[] objects) {
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
            label.setText(some_array[position].restaurante.getNome());


            TextView label2 = (TextView)row.findViewById(R.id.nomeMenu);
            label2.setText(some_array[position].getNome());


            ImageView imagem=(ImageView)row.findViewById(R.id.capa);
            ImageView icon = (ImageView)row.findViewById(R.id.imagemTipo);

            //Customize your icon here
            //icon.setImageResource(R.drawable.sem_foto);

            if(some_array[position].tipo.equalsIgnoreCase("especial_doisprecos"))
            {
                icon.setImageResource(R.drawable.antes_depois);
                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText(some_array[position].precoAntigo+"€");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].precoNovo+"€");
                TextView label5=(TextView)row.findViewById(R.id.desconto);

                Float  preco1 = Float.parseFloat( some_array[position].precoAntigo);
                Float  preco2 =  Float.parseFloat( some_array[position].precoNovo);

                Float percentagem = (((preco2 / preco1) * 100) - 100) * -1;

                label5.setText("Desconto "+ String.format("%.0f", percentagem) + "%");

            }
            else if(some_array[position].tipo.equalsIgnoreCase("especial_desconto"))
            {
                icon.setImageResource(R.drawable.desc_fatura);

                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText("");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].desconto+"% off");

                TextView label5=(TextView)row.findViewById(R.id.desconto);
                label5.setText("Desconto em factura ");
            }
            else
            {
                icon.setImageResource(R.drawable.menu_esp);
                TextView label5=(TextView)row.findViewById(R.id.desconto);
                label5.setText(some_array[position].especialFita);
                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText("");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].precoNovo+"€");
            }

            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImage(), imagem);

            return row;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        // para colocar quando esta vazia
        mListView.setEmptyView(view.findViewById(R.id.emty_view));

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Log.v("clicou no resutaurante","abrir " + some_array[position].getNome());
                Intent myIntent = new Intent(getActivity(), MenuEspecial_nos_especiais.class);
                //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                myIntent.putExtra("rest_cartao_id", some_array[position].getDb_id());
                myIntent.putExtra("rest_id", some_array[position].getId_rest());
                myIntent.putExtra("restaurante", some_array[position].getId_rest());
                myIntent.putExtra("urlfoto", some_array[position].getImagem_rest());
                myIntent.putExtra("nome_rest", some_array[position].getNome_rest());
                myIntent.putExtra("lat", some_array[position].getLat());
                myIntent.putExtra("lon", some_array[position].getLng());
                myIntent.putExtra("morada", some_array[position].getMorada());
                myIntent.putExtra("hora_min_reserva", some_array[position].getHora_minimo_antedencia_especial());

                //myIntent.putExtra("rating", ""+some_array[position].getId_rest());
                //myIntent.putExtra("votacoes", ""+some_array[position].getId_rest());

                getActivity().startActivity(myIntent);
                getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }

        });


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
                dict.put("city_id", Globals.getInstance().getCidedade_id());



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



                some_array = new Menu_do_restaurante[dataJsonArr.length()];
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    // Storing each json item in variable
                    String firstname = c.getString("nome");

                    // show the values in our logcat
                    Log.v(TAG, "Especial name: " + firstname
                    );


                    Menu_do_restaurante menu = new Menu_do_restaurante();
                    menu.setNome(firstname);


                    menu.setPrecoAntigo(c.getString("precoespant").replace(',','.'));
                    menu.setPrecoNovo(c.getString("precoesp").replace(',','.'));
                    menu.setTipo(c.getString("tipoespecial"));
                    menu.setDb_id(c.getString("id"));
                    menu.setDesconto(c.getString("desconto"));
                    menu.setEspecialFita(c.getString("especial_um_fita"));
                    menu.setId_rest(c.getString("id_rest"));
                    menu.urlImage = c.getString("imagem");
                    menu.setLat(c.getString("lat"));
                    menu.setLng(c.getString("lon"));
                    menu.setNome_rest(c.getString("nome_rest"));
                    menu.setImagem_rest(c.getString("imagem_rest"));
                    menu.setMorada(c.getString("adress"));
                    menu.setHora_minimo_antedencia_especial(c.getString("hora_minimo_antedencia_especial"));

                    Restaurante rest = new Restaurante();
                    rest.setNome(c.getString("nome_rest"));
                    menu.setRestaurante(rest);

                    some_array[i] = menu;
                }



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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_localizacao, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_localizacao:
                Intent myIntent = new Intent(getActivity(), Localizacao.class);
                getActivity().startActivity(myIntent);
                return false;

            default:
                break;
        }

        return false;
    }

}
