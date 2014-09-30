package pt.menuguru.menuguru6.Favoritos;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;

/**
 * Created by hugocosta on 29/09/14.
 */
public class Favoritos extends Activity
{
    private ArrayList<Favorito_item> some_list;

    private ViewGroup footer;
    private ListView listView;
    private String rest_id;
    private AdapterFavoritos mAdapter;
    private ArrayList<String> favs_selecionados = new ArrayList<String>();
    private boolean primeiravez = true;
    private ProgressDialog progressDialog;
    private EditText nova_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.favoritos);
        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante");



        new AsyncTaskParseJsonFavoritos(this).execute();

    }

    public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            //match this behavior to your 'Send' (or Confirm) button
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.favoritos, menu);


        return true;//return true so that the menu pop up is opened

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition( R.anim.abc_fade_in , R.anim.abc_slide_out_bottom);
                return false;
            case R.id.clicar_menu_gravar_favoritos:
                new AsyncTaskParseJsonAdicionarAsListas(this).execute();
                return false;
            default:
                break;
        }

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonFavoritos extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listagem_listanomefav.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Favoritos delegate;

        public AsyncTaskParseJsonFavoritos (Favoritos delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();


                dict.put("lang", Globals.getInstance().getLingua());
                dict.put("id_rest", rest_id);
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());

                /*
                [dict setObject: [Globals user].faceId forKey:@"face_id"];
        [dict setObject: [NSString stringWithFormat:@"%d", 0] forKey:@"user_id"];
                 */


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado dos comentarios = " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna esto dos comentarios"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("list");



                /*
                list =     (
                {
                     count = 6;
                     existe = "<null>";
                     id = 1;
                    nomelista = favoritos;
                    tipo = 1;
                },
                 */

                some_list = new ArrayList<Favorito_item>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Favorito_item fav = new Favorito_item();
                    fav.setFav_id(c.getString("id"));
                    fav.setFav_name(c.getString("nomelista"));
                    fav.setFav_number(c.getString("count"));
                    fav.setExiste(c.getString("existe"));

                    if (fav.getExiste().equalsIgnoreCase("1"))
                    {
                       favs_selecionados.add(fav.getFav_id());
                    }

                    some_list.add(fav);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteListaFavoritos(true);
        }

    }

    private void asyncCompleteListaFavoritos(boolean success)
    {
        // aqui tenhe de inicializar o novo adapter para ter a lista de comentarios
        listView = (ListView) findViewById(R.id.lista_favoritos);
        final LayoutInflater inflater = LayoutInflater.from(this);

        footer = (ViewGroup)  inflater.inflate(R.layout.footer_favoritos, listView, false);
        nova_lista = (EditText) footer.findViewById(R.id.editText_adcionar_lista);

        // codigo pedido emprestado do vitor
        nova_lista.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.v("CARREGOU","CONCLUIDO");

                    new AsyncTaskParseJsonCriarLista(Favoritos.this).execute();
                }
                return false;
            }
        });




        mAdapter = new AdapterFavoritos(this, R.layout.row_favorito, some_list);


        if (primeiravez) {
            listView.addFooterView(footer);
            primeiravez = false;
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // tenho de fazer algumas verificaçoes antes de anvançar
                if(favs_selecionados.contains(some_list.get(position).getFav_id()))
                {
                    favs_selecionados.remove(some_list.get(position).getFav_id());
                }else {
                    favs_selecionados.add(some_list.get(position).getFav_id());
                }

                mAdapter.notifyDataSetChanged();
            }
        });


    }

    public class AdapterFavoritos extends ArrayAdapter<Favorito_item> {

        Context myContext;
        public ImageLoader imageLoader;
        HashMap<Integer,Integer> selectionValueMap = new HashMap<Integer,Integer>();

        ArrayList<Favorito_item> DataValueList = new ArrayList<Favorito_item>();

        public AdapterFavoritos(Context context, int textViewResourceId, ArrayList<Favorito_item> objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getApplicationContext());
            myContext = context;
            this.DataValueList = objects;
        }

        /*
        codigo para encher chouriças
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        public void selectedItem(int postion ,int flag)
        {
            selectionValueMap.put(postion, flag);
            notifyDataSetChanged();
        }
        public void removeSelection(int position)
        {
            selectionValueMap.remove(position);
            notifyDataSetChanged();
        }
        public void removeItem()
        {
            Set<Integer> mapKeySet = selectionValueMap.keySet();
            Iterator keyIterator = mapKeySet.iterator();
            while(keyIterator.hasNext())
            {
                int key = (Integer) keyIterator.next();
                Log.d("key", Integer.toString(key));
                DataValueList.remove(key);
                //DataValueList.remove(selectionValueMap.get(key));
            }
            notifyDataSetChanged();
        }

        /*
        acaba aqui o codigo para encher chouriças
        este codigo ainda vai dar muito jeito mais tarde por isso que mantenho aqui
         */

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.row_favorito, parent, false);

            // para o comentario do utilizador
            TextView label1=(TextView)row.findViewById(R.id.favoritos_number);
            label1.setText(some_list.get(position).getFav_number());

            TextView label2=(TextView)row.findViewById(R.id.texto_fav_nome);
            label2.setText(some_list.get(position).getFav_name());

            ImageView selecionado = (ImageView) row.findViewById(R.id.imageView_tem_fav);


            // aqui tem de ficar diferente do que está
            // sempre que clico eu removo e adiciono cenas ao array que depois precisa ser actualizado aqui

            selecionado.setImageResource(0);

            if(favs_selecionados.contains(some_list.get(position).getFav_id())) {
                selecionado.setImageResource(R.drawable.ico_check);
            }

            return row;
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonAdicionarAsListas extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_apagar_restaurante_listanome.php";

        private Favoritos delegate;

        public AsyncTaskParseJsonAdicionarAsListas (Favoritos delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();


                dict.put("lang", Globals.getInstance().getLingua());
                dict.put("id_rest", rest_id);
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                dict.put("idlista", new JSONArray(favs_selecionados.toString()));



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado de adicionar aos favoritos = " + jsonString);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteAdicionar_remover_favoritos(true);
        }

    }

    private void asyncCompleteAdicionar_remover_favoritos(boolean success)
    {
        //new AsyncTaskParseJsonFavoritos(this).execute();
        finish();
        overridePendingTransition( R.anim.abc_fade_in , R.anim.abc_slide_out_bottom);
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonCriarLista extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_criar_listanomefav.php";

        private Favoritos delegate;

        public AsyncTaskParseJsonCriarLista (Favoritos delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();


                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                dict.put("nome", nova_lista.getText());



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado de adicionar aos favoritos = " + jsonString);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteAdicionar_criar_lista(true);
        }

    }

    private void asyncCompleteAdicionar_criar_lista(boolean success)
    {
        nova_lista.setText("");
        new AsyncTaskParseJsonFavoritos(this).execute();
    }

}
