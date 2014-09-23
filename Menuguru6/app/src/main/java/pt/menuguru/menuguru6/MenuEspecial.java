package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Descricao_Especial;
import pt.menuguru.menuguru6.Utils.Horario_Especial;
import pt.menuguru.menuguru6.Utils.Menu_Especial;


public class MenuEspecial extends Activity {

    private static MyListAdapter mAdapter;
    private ArrayList<Menu_Especial> some_list;
    private ArrayList<Descricao_Especial> desc_list;
    private ArrayList<Horario_Especial> hora_list;

    private ListView mListView;

    private ProgressDialog progressDialog;

    private String rest_id;
    private String rest_cartao_id;

    public EditText edit_dias;
    public EditText edit_hrs;
    public EditText edit_min;
    public EditText edit_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_menu_especial);
        Intent intent = getIntent();
        rest_id = getIntent().getExtras().getString("rest_id");
        rest_cartao_id = getIntent().getExtras().getString("rest_cartao_id");


        //Button bt_reserva = (Button)findViewById(R.id.bt_reservar);
/*
        edit_dias = (EditText)findViewById(R.id.text_dias);
        edit_hrs = (EditText)findViewById(R.id.text_preco);
        edit_min = (EditText)findViewById(R.id.text_oferta);
        edit_sec = (EditText)findViewById(R.id.text_preco_ant);

*/
        // Set the adapter
        mListView = (ListView)findViewById(R.id.list_esp);

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
        switch (item.getItemId()) {
            case android.R.id.home:
                mAdapter=null;
                mListView=null;
                finish();

                return false;
            default:
                break;
        }

        return false;
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
            View row=inflater.inflate(R.layout.row_especial, parent, false);
            getActionBar().setTitle(some_list.get(0).getNome());
            LinearLayout linear_ris = (LinearLayout)row.findViewById(R.id.linearLayout_risca);
            LinearLayout linear_tit = (LinearLayout)row.findViewById(R.id.linear_tit);

            TextView label = (TextView)row.findViewById(R.id.text_titulo);
            TextView text_preco_ant = (TextView)row.findViewById(R.id.text_preco_ant);
            TextView text_preco_act = (TextView)row.findViewById(R.id.text_preco);
            TextView text_nr_ofertas = (TextView)row.findViewById(R.id.text_rating_desc);
            TextView text_desc = (TextView)row.findViewById(R.id.text_desc);
            TextView text_desc2 = (TextView)row.findViewById(R.id.text_desc2);
            TextView text_oferta = (TextView)row.findViewById(R.id.text_oferta);

            String aux_tipo = some_list.get(0).getTipo();

            if(position==0){
                if(aux_tipo.equals("desconto")){
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_act.setText(some_list.get(position).getPreco_actual());
                    text_preco_ant.setText(some_list.get(position).getPreco_ant());
                    text_nr_ofertas.setText(some_list.get(position).getReserva_feita());
                    text_desc.setText(desc_list.get(position).getDescricao());
                    text_desc2.setVisibility(View.GONE);

                }else if(aux_tipo.equals("off")){
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_act.setText(some_list.get(position).getDesconto());
                    text_preco_ant.setVisibility(View.GONE);
                    text_nr_ofertas.setText(some_list.get(position).getReserva_feita());
                    text_desc.setText(desc_list.get(position).getDescricao());
                    text_desc2.setVisibility(View.GONE);
                }else{
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_act.setVisibility(View.GONE);
                    text_preco_ant.setVisibility(View.GONE);
                    text_nr_ofertas.setVisibility(View.GONE);
                    text_desc2.setVisibility(View.GONE);
                    text_desc.setVisibility(View.GONE);
                    linear_ris.setVisibility(View.GONE);
                    text_oferta.setVisibility(View.GONE);
                }
            }else if(position==1 && !aux_tipo.equals("desconto") && !aux_tipo.equals("off")) {
                text_preco_ant.setVisibility(View.GONE);
                text_preco_act.setVisibility(View.GONE);
                text_nr_ofertas.setVisibility(View.GONE);
                text_desc.setVisibility(View.GONE);
                linear_ris.setVisibility(View.GONE);
                text_oferta.setVisibility(View.GONE);
                linear_tit.setVisibility(View.GONE);
                text_desc2.setText(desc_list.get(position).getDescricao());

            }else{
                text_preco_ant.setVisibility(View.GONE);
                text_preco_act.setVisibility(View.GONE);
                text_nr_ofertas.setVisibility(View.GONE);
                text_desc.setVisibility(View.GONE);
                linear_ris.setVisibility(View.GONE);
                text_oferta.setVisibility(View.GONE);
                label.setText(desc_list.get(position).getTitulo());
                text_desc2.setText(desc_list.get(position).getDescricao());
            }
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
                dict.put("rest_id",rest_id);
                dict.put("rest_cartao_id",rest_cartao_id);



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
                menu.setTipo(dataJsonArr.getString("tipo"));
                some_list.add(menu);

                Log.v("PRECO ANT",dataJsonArr.getString("preco_ant"));
                Log.v("PRECO ACT",dataJsonArr.getString("preco_actual"));
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

        mAdapter = new MyListAdapter(this, R.layout.list_item, desc_list);

        LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_menuespcial, mListView, false);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_menuespecial, mListView, false);

        mListView.addFooterView(footer, null, false);
        mListView.addHeaderView(header, null, false);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}