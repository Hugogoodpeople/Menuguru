package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Descricao_Especial;
import pt.menuguru.menuguru6.Utils.Horario_Especial;
import pt.menuguru.menuguru6.Utils.Menu_Especial;
import pt.menuguru.menuguru6.Utils.Menu_do_restaurante;


public class MenuEspecial extends Activity {

    private static MyListAdapter mAdapter;
    private ArrayList<Menu_Especial> some_list;
    private ArrayList<Descricao_Especial> desc_list;
    private ArrayList<Horario_Especial> hora_list;

    private AbsListView mListView;

    private ProgressDialog progressDialog;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.header_menuespecial);

        // Set the adapter
        mListView = (AbsListView)findViewById(R.id.list_esp);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        new AsyncTaskParseJson(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_especial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyListAdapter extends ArrayAdapter<Descricao_Especial> {

        Context myContext;


        public MyListAdapter(Context context, int textViewResourceId, ArrayList<Descricao_Especial> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.fragment_especiais, parent, false);
            return row;
        }

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_menu_especial2.php";

        // contacts JSONArray
        JSONObject dataJsonArr = null;
        JSONObject dataJsonRep = null;
        JSONArray dataJsonPessoas = null;
        JSONArray dataJsonResTitulo = null;
        JSONArray dataJsonHorarios = null;

        private MenuEspecial delegate;

        public AsyncTaskParseJson (MenuEspecial delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MenuEspecial.this);
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
                dict.put("linguatel","pt");

                dict.put("face_id", "0");
                dict.put("user_id", "0");

                dict.put("horario_lang","pt");
                dict.put("rest_id","923");
                dict.put("rest_cartao_id","482");



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna isto"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonRep = jsonObj.getJSONObject("resp");
                dataJsonArr = dataJsonRep.getJSONObject("res");

                dataJsonPessoas = dataJsonArr.getJSONArray("n_pessoas");
                dataJsonHorarios = dataJsonRep.getJSONArray("h_esp");
                dataJsonResTitulo = dataJsonRep.getJSONArray("res_titulo");


               // Log.v("JSON RES",dataJsonArr.getString("res"));
                //Log.v("JSON NRPESSOAS",dataJsonArr.getString("res"));

                // loop through all users
                some_list = new ArrayList<Menu_Especial>();
                Menu_Especial menu = new Menu_Especial();
                menu.setReserva_disp(dataJsonArr.getString("reserva_disp"));
                menu.setReserva_feita(dataJsonArr.getString("reserva_feita"));
                menu.setReserva_tem(dataJsonArr.getString("reserva_tem"));
                menu.setNumero_vaucher(dataJsonArr.getString("numero_vaucher"));
                menu.setNome_cat(dataJsonArr.getString("nome_cat"));
                menu.setNome(dataJsonArr.getString("nome"));
                menu.setDescricao(dataJsonArr.getString("descricao"));
                menu.setPreco_actual(dataJsonArr.getString("preco_ant"));
                menu.setPreco_actual(dataJsonArr.getString("preco_actual"));
                menu.setDestaque(dataJsonArr.getString("destaque"));
                menu.setImagem(dataJsonArr.getString("imagem"));
                menu.setId(dataJsonArr.getString("id"));
                menu.setPartilhar(dataJsonArr.getString("partilhar"));
                menu.setReserva_obrigatoria(dataJsonArr.getString("reserva_obrigatoria"));
                menu.setDesconto(dataJsonArr.getString("desconto"));
                menu.setDatafinal(dataJsonArr.getString("datafinal"));
                menu.setDataActual(dataJsonArr.getString("dataActual"));
                some_list.add(menu);

                Log.v("RESERVA DISP",dataJsonArr.getString("reserva_disp"));
                Log.v("IMAGEM",dataJsonArr.getString("imagem"));
                Log.v("NOME",dataJsonArr.getString("nome"));


                desc_list = new ArrayList<Descricao_Especial>();
                for (int i = 0; i < dataJsonResTitulo.length(); i++) {
                    JSONObject c = dataJsonResTitulo.getJSONObject(i);

                    Descricao_Especial desc = new Descricao_Especial();
                    desc.setDescricao(c.getString("descricao"));
                    desc.setTitulo(c.getString("titulo"));
                    //desc.setTitulo_id(c.getString("titulo_id"));
                    Log.v("descricao",c.getString("descricao"));
                    //Log.v("titulo",c.getString("titulo_id"));
                    Log.v("titulo",c.getString("descricao"));
                    desc_list.add(desc);
                }

                hora_list = new ArrayList<Horario_Especial>();
                for (int i = 0; i < dataJsonHorarios.length(); i++) {
                    JSONObject a = dataJsonHorarios.getJSONObject(i);

                    Horario_Especial hora = new Horario_Especial();
                    hora.setId(a.getString("id"));
                    hora.setHora_inicio(a.getString("hora_inicio"));
                    hora.setN_pessoas_h(a.getString("n_pessoas_h"));
                    hora.setDia_id(a.getString("dia_id"));

                    hora_list.add(hora);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }



    public void asyncComplete(boolean success){

        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, desc_list);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }
}