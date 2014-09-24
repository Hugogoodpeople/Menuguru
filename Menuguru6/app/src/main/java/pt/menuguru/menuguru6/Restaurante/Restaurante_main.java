package pt.menuguru.menuguru6.Restaurante;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.MenuEspecial;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Comentario;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Menu_do_restaurante;
import pt.menuguru.menuguru6.Utils.Utils;

/**
 * Created by hugocosta on 19/09/14.
 */
public class Restaurante_main extends FragmentActivity {


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = Globals.get_instance().getCfunc().length;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ViewGroup header2;

    private static MyListAdapter mAdapter;
    private ArrayList<Menu_do_restaurante> some_list;
    private ArrayList<String> fotos;
    private ListView gridView;
    private ProgressDialog progressDialog;
    // informaçao que já vem de traz
    private String rest_id;
    private String url_foto;
    private String latitude;
    private String longitude;
    private String morada;
    private String mediarating;
    private String votacoes;


    private String[] listEstrelas;
    ArrayList<Comentario> comentarios;

    // se tiver destaque tem de aparecer um layout diferente
    private boolean destaque = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante");
        url_foto = intent.getStringExtra("urlfoto");
        latitude = intent.getStringExtra("lat");
        longitude = intent.getStringExtra("lon");
        morada = intent.getStringExtra("morada");


        actionBar.setTitle(intent.getStringExtra("nome_rest"));

        setContentView(R.layout.activity_restaurante_main);

        new AsyncTaskParseJsonEstrelas(this).execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_localizacao, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    public void initializeGalery()
    {

        List<Fragment> fragments = new Vector<Fragment>();


        Fragment fragmentCapa = new Imagem_galeria().create(url_foto);
        fragments.add(fragmentCapa);


        // aqui tem de conter um ciclo e mudar para ir buscar imagens a net
        for (int i = 0 ; i< fotos.size() ; i++)
        {
            Fragment fragment = new Imagem_galeria().create(fotos.get(i));
            fragments.add(fragment);
        }




        ViewPager galeria = (ViewPager) findViewById(R.id.galeria_imagens);
        PagerAdapter adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);

        galeria.setAdapter(adapter);

    }

    public void initialisePagin()
    {
        List<Fragment> fragments = new Vector<Fragment>();

        // tenho de fazer alterações para poder atribuir o numero de estrelas
        Fragment fragmentMedia = new tab_rating().create(mediarating, votacoes);

        fragments.add(fragmentMedia);

        // agora tenho de fazer as alterções para receber um array com 5 posiçoes para o rating
        Fragment fragmentAllStars = new tab_five_ratin().create(listEstrelas, votacoes);

        fragments.add(fragmentAllStars);

        mPager = (ViewPager) findViewById(R.id.pager_restaurante);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);

        mPager.setAdapter(mPagerAdapter);

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments)
        {
            super(fm);
            this.fragments = fragments;
        }



        @Override
        public Fragment getItem(int position) {


            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson1 extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_m_rest.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJson1 (Restaurante_main delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Restaurante_main.this);
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

                dict.put("lang","");
                /*
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                */

                dict.put("face_id", "0");
                dict.put("user_id", "0");

                dict.put("horario_lang","pt");
                dict.put("rest_id",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                //Log.v("jfgrhng","resultado da procura = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna dentro do menu"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("res");

                // loop through all users


                some_list = new ArrayList<Menu_do_restaurante>();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Menu_do_restaurante menu = new Menu_do_restaurante();
                    menu.setNome(c.getString("nome"));
                    menu.setUrlImage(c.getString("imagem"));
                    menu.setTipo(c.getString("tipo"));
                    menu.setDb_id(c.getString("pai_id"));
                    menu.setId_rest(rest_id);
                    menu.setDestaque(c.getString("destaque"));
                    menu.setDescricao(c.getString("descricao"));

                    if (menu.getDestaque().equalsIgnoreCase("1"))
                        destaque = true;

                    if (menu.getTipo().equalsIgnoreCase("menu_especial"))
                    {
                        menu.setPrecoNovo(c.getString("precoesp"));
                        menu.setPrecoAntigo(c.getString("precoespant"));
                        menu.setEspecialFita(c.getString("especial_um_fita"));
                        menu.setDesconto(c.getString("desconto"));
                    }

                    some_list.add(menu);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();
            delegate.asyncCompleteMenus(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonGaleria extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_galeria_rest.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonGaleria (Restaurante_main delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // não preciso desta parte porque ja tenho outro loading a correr
            /*
            progressDialog = new ProgressDialog(Restaurante_main.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
            */
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

                dict.put("lang","");

                dict.put("rest_id",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado da galeria = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna dentro do menu"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("rest");

                // loop through all users


                fotos = new ArrayList<String>();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    String foto = c.getString("foto");

                    fotos.add(foto);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
           // progressDialog.dismiss();
            delegate.asyncCompleteFotos(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonEstrelas extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listar_rating.php";


        JSONObject dataJason = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonEstrelas (Restaurante_main delegate){
            this.delegate = delegate;
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

                dict.put("lang",Globals.getInstance().getLingua());

                dict.put("id_rest",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado das estrelas = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna para as estrelas"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);


                // loop through all users


                votacoes = outro.getString("contagem");
                mediarating = outro.getString("media");

                listEstrelas = new String[5];
                listEstrelas[0] = outro.getString("umaestrela");
                listEstrelas[1] = outro.getString("duasestrela");
                listEstrelas[2] = outro.getString("tresestrela");
                listEstrelas[3] = outro.getString("quatroestrela");
                listEstrelas[4] = outro.getString("cincoestrela");


                Log.v("werqwe", "numero de votações " + votacoes);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteEstrelas(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonComentarios extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listar_comentarios.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonComentarios (Restaurante_main delegate){
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

                dict.put("lang",Globals.getInstance().getLingua());

                dict.put("id_rest",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado dos comentarios = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna estes comentarios"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("res");



                comentarios = new ArrayList<Comentario>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Comentario comentario = new Comentario();
                    comentario.setId_com(c.getString("id_com"));
                    comentario.setComentario(c.getString("comentario"));
                    comentario.setResp_com(c.getString("resp_com"));
                    comentario.setData_com(c.getString("data_com"));
                    comentario.setData_respest(c.getString("data_respest"));
                    comentario.setNome_user_com(c.getString("nome_user_com"));
                    comentario.setNome_rest_com(c.getString("nome_rest_com"));


                    comentarios.add(comentario);
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
            delegate.asyncCompleteComentarios(true);
        }

    }

    public void asyncCompleteComentarios(boolean success)
    {
        // esta parte serve para os comentarios
        TextView user_name = (TextView) header2.findViewById(R.id.textView_user_name);
        TextView data_coment = (TextView) header2.findViewById(R.id.textView_data_comentario);
        TextView comentario = (TextView) header2.findViewById(R.id.textView_Comentario);
        TextView ver_todos = (TextView) header2.findViewById(R.id.textView_ver_todos);



        // quando ainda não foram feitos comentarios

        if (comentarios.size() == 0) {
            user_name.setText(getString(R.string.sem_coments));
            data_coment.setText("");
            comentario.setText(getString(R.string.seja_o_primeiro_c));
            comentario.setTextColor(R.color.gray);
            ver_todos.setText("");
        }else // quando ja tem comentarios
        {
            user_name.setText(comentarios.get(0).getNome_user_com());
            data_coment.setText(comentarios.get(0).getData_com());
            //comentario.setTextColor(R.color.black);
            comentario.setText(comentarios.get(0).getComentario());
            ver_todos.setText(getString(R.string.ver_todos));
        }
    }

    public void asyncCompleteEstrelas(boolean success)
    {
        new AsyncTaskParseJson1(this).execute();
    }


    public void asyncCompleteFotos(boolean success)
    {
        initializeGalery();
    }


    public void asyncCompleteMenus(boolean success){

        //mAdapter.notifyDataSetChanged();
        //mAdapter = new MyListAdapter(getApplicationContext(), R.layout.row_defenicoes, some_array);

        gridView = (ListView) findViewById(R.id.morada_rest);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);

        // para calcular a distancia
        Location locationRest = new Location("");
        locationRest.setLatitude(Double.parseDouble(latitude));
        locationRest.setLongitude(Double.parseDouble(longitude));

        Location locationPhone = new Location("");
        locationPhone.setLatitude(Double.parseDouble(Globals.getInstance().getLatitude()));
        locationPhone.setLongitude(Double.parseDouble(Globals.getInstance().getLongitude()));


        LayoutInflater inflater = LayoutInflater.from(this);

        if (destaque) {
            // como tem algom em destaque tenho de chamar outro layout
            header2 = (ViewGroup) inflater.inflate(R.layout.header_restaurante_main_destaque, gridView, false);

            // este novo layout tem um menu no topo que tenho de preencher
            // mas primeiro tenho de achar esse menu na lista de menus

            for(int i = 0 ; i < some_list.size() ; i++)
            {
                Menu_do_restaurante menu = some_list.get(i);
                if (menu.getDestaque().equalsIgnoreCase("1"))
                {

                    // aqui tenho de colocar este menu no topo do header
                    TextView nome_menu = (TextView) header2.findViewById(R.id.textView_nome_especial);
                    nome_menu.setText(menu.getNome());

                    TextView desc_menu = (TextView) header2.findViewById(R.id.textView_desc_especial);
                    desc_menu.setText(menu.getDescricao());

                    TextView preco_ant = (TextView) header2.findViewById(R.id.textView_preco_ant_especial);
                    preco_ant.setText(menu.getPrecoAntigo());
                    preco_ant.setPaintFlags(preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    TextView preco_novo = (TextView) header2.findViewById(R.id.preco_novo_especial);
                    preco_novo.setText(menu.getPrecoNovo());

                    ImageView imagemEspecial = (ImageView) header2.findViewById(R.id.imageView_destaque);
                    ImageLoader imageLoaderEspecial=new ImageLoader(getApplicationContext());
                    imageLoaderEspecial.DisplayImage("http://menuguru.pt/"+ menu.getUrlImage(), imagemEspecial);

                }

            }

        }
        else {
            header2 = (ViewGroup) inflater.inflate(R.layout.header_restaurante_main, gridView, false);
        }
        TextView dist = (TextView) header2.findViewById(R.id.distancia_restaurante_header);
        dist.setText(Utils.getDistance(locationPhone, locationRest));

        TextView adress = (TextView) header2.findViewById(R.id.morada_rest);
        adress.setText(morada);

        TextView votos = (TextView) header2.findViewById(R.id.textView_avaliacoes);
        votos.setText(votacoes +" "+ getString(R.string.votacoes));


        // para quando tem comentarios tenho de ir buscar por webservice

        gridView.addHeaderView(header2, null, false);

        // Assign adapter to ListView
        //listView.setAdapter(adapter);
        mAdapter = new MyListAdapter(this, R.layout.grid_menu, some_list);
        gridView.setAdapter(mAdapter);

        initialisePagin();
        new AsyncTaskParseJsonGaleria(this).execute();
        new AsyncTaskParseJsonComentarios(this).execute();

    }




    public class MyListAdapter extends ArrayAdapter<Menu_do_restaurante> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId, ArrayList<Menu_do_restaurante> objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;


            if (row == null)
                row=inflater.inflate(R.layout.grid_menu, parent, false);
            TextView label1=(TextView)row.findViewById(R.id.morada_rest);
            label1.setText(some_list.get(position*2).getNome());


            ImageView icon=(ImageView)row.findViewById(R.id.galeria_imagens);
            imageLoader.DisplayImage("http://menuguru.pt/"+some_list.get(position*2).getUrlImage(), icon);

            ImageView icon2 = (ImageView) row.findViewById(R.id.imagem_menu_2);
            TextView label2=(TextView)row.findViewById(R.id.lista_menus_restaurante_1);
            RelativeLayout rel = (RelativeLayout)row.findViewById(R.id.odd_view);
            ImageView icon3 = (ImageView) row.findViewById(R.id.imagem_tipo1);
            ImageView icon4 = (ImageView) row.findViewById(R.id.imagem_tipo2);

            // tenho de criar o listener para quando clica na imagem abrir o especial
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Restaurante_main.this, MenuEspecial.class);
                    //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                    myIntent.putExtra("rest_cartao_id", "" + some_list.get(position*2).getDb_id());
                    myIntent.putExtra("rest_id", ""+some_list.get(position*2).getId_rest());

                    startActivity(myIntent);

                    overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                }
            });


            setiamgeTipo(position * 2, icon3);

            if (some_list.size() > (position * 2) +1) {

                imageLoader.DisplayImage("http://menuguru.pt/" + some_list.get((position * 2)+1).getUrlImage(), icon2);

                label2.setText(some_list.get(position*2 + 1).getNome());


                setiamgeTipo(position*2 + 1, icon4);
                rel.setVisibility(View.VISIBLE);

                icon2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Restaurante_main.this, MenuEspecial.class);
                        //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                        myIntent.putExtra("rest_cartao_id", "" + some_list.get(position*2 +1).getDb_id());
                        myIntent.putExtra("rest_id", "" + some_list.get(position*2 +1).getId_rest());
                        myIntent.putExtra("vem_de_especias", false);

                        startActivity(myIntent);

                        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                    }
                });


            }else
            {
                rel.setVisibility(View.GONE);
            }

            return row;
        }

        public int getCount() {

            int odd = some_list.size()% 2;
            int div = some_list.size()/ 2;
            return div + odd;
        }

        private void setiamgeTipo(int position, ImageView image)
        {

            Menu_do_restaurante menu = some_list.get(position);

            if (menu.getTipo().equalsIgnoreCase("menu_especial")) {
                image.setVisibility(View.VISIBLE);
                if (menu.getPrecoNovo().length() != 0 && menu.getPrecoAntigo().length() != 0) {
                    image.setImageResource(R.drawable.antes_depois);
                } else if (!menu.getDesconto().equalsIgnoreCase("0")) {
                    image.setImageResource(R.drawable.desc_fatura);
                } else {
                    image.setImageResource(R.drawable.menu_esp);
                }
            }
            else
            {
                image.setVisibility(View.GONE);
            }

        }

    }
}
