package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
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
import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.MenuEspecial;
import pt.menuguru.menuguru6.Utils.Reserva;


public class Reservas extends Fragment implements AbsListView.OnItemClickListener{
    View view;
    View row;
    ImageView imagem;

    Reserva[] some_array = null;

    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapterReserva mAdapter;
    private ProgressDialog progressDialog;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class MyListAdapterReserva extends ArrayAdapter<Reserva> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapterReserva(Context context, int textViewResourceId,
                                      Reserva[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getActivity().getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.fragment_reservas, parent, false);


            TextView label=(TextView)row.findViewById(R.id.nomeRestaurante);
            label.setText(some_array[position].getNome_ret());


            TextView label2 = (TextView)row.findViewById(R.id.nomeMenu);
            if(some_array[position].getTipo().equals("normal")){
                label2.setText("Reserva de mesa");
            }else {
                label2.setText(some_array[position].getNome_menu());
            }

            TextView label3 = (TextView)row.findViewById(R.id.desconto);
            String texto = some_array[position].getData_rm() +" "+ some_array[position].getHora_rm() +" "+some_array[position].getN_pesssoas_rm() +" pax";
            label3.setText(texto);

            ImageView imagem=(ImageView)row.findViewById(R.id.capa);

            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getImagem_rest(), imagem);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_defenicoesteste, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // para colocar quando esta vazia
        mListView.setEmptyView(view.findViewById(R.id.emty_view));

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        //
        // new AsyncTaskParseJson(this).execute();

        return view;
        /*
        if(Globals.getInstance().getUser()!=null) {
            Log.v("ID",Globals.get_instance().getUser().getUserid());
            Log.v("ID_FACE",Globals.get_instance().getUser().getId_face());
            new AsyncTaskParseJson(this).execute();

            view = inflater.inflate(R.layout.fragment_reservas, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_sem_reservas, container, false);
            imagem = (ImageView) view.findViewById(R.id.imageView_sem);
                  if(Globals.getInstance().getLingua()=="pt"){
                      imagem.setImageResource(R.drawable.reserva_pt);
            }else if(Globals.getInstance().getLingua()=="en"){
                      imagem.setImageResource(R.drawable.reserva_en);
            }else if(Globals.getInstance().getLingua()=="fr"){
                      imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua()=="it"){
                      imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua()=="de"){
                      imagem.setImageResource(R.drawable.reserva_de);
            }else if(Globals.getInstance().getLingua()=="es"){
                      imagem.setImageResource(R.drawable.reserva_es);
            }
        }
        getActivity().getActionBar().setTitle(R.string.reservas);


        return view;
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_reservanormal_especial.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Reservas delegate;

        public AsyncTaskParseJson (Reservas delegate){
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

                String tipo_conta = Globals.getInstance().getUser().getTipoconta();
                if(tipo_conta.equals("facebook")){
                    dict.put("face_id", Globals.get_instance().getUser().getId_face());
                    Log.v("FACE",Globals.get_instance().getUser().getId_face());
                    dict.put("user_id", "0");
                    Log.v("USER","0");
                    dict.put("lang",Globals.get_instance().getLingua());
                    Log.v("Entrou"," FACE");
                }else{
                    dict.put("face_id", "0");
                    Log.v("FACE","0");
                    dict.put("user_id", Globals.get_instance().getUser().getUserid());
                    Log.v("USER", Globals.get_instance().getUser().getUserid());
                    dict.put("lang",Globals.get_instance().getLingua());
                    Log.v("Entrou"," NORMAL");
                }

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                dataJsonArr = jsonObj.getJSONArray("res");
                some_array = new Reserva[dataJsonArr.length()];


                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    Reserva reserva = new Reserva();

                    reserva.setData_rm(c.getString("data_rm"));
                    reserva.setTipo(c.getString("tipo"));
                    reserva.setId_rest(c.getString("id_rest"));
                    reserva.setId_mesa(c.getString("id_mesa"));
                    reserva.setId_hmesa(c.getString("id_hmesa"));
                    reserva.setHora_rm(c.getString("hora_rm"));
                    reserva.setTelefone_rm(c.getString("telefone_rm"));
                    reserva.setN_pesssoas_rm(c.getString("n_pessoas_rm"));
                    reserva.setNome_menu(c.getString("nome_menu"));
                    reserva.setNum_pes_disponivel(c.getString("num_pes_disponiveis"));
                    reserva.setNome_ret(c.getString("nome_rest"));
                    reserva.setLat(c.getString("lat"));
                    reserva.setLon(c.getString("lon"));
                    reserva.setImagem_rest(c.getString("imagem_rest"));


                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("data_rm"));
                    Log.v("IMG1","objecto = "+ c.getString("tipo"));
                    Log.v("IMG2","objecto = "+ c.getString("id_rest"));

                    some_array[i] = reserva;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){ delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){
        mAdapter = new MyListAdapterReserva(getActivity(), R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }


}
