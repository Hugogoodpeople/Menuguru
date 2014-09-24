package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.text.format.Time;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Restaurante.Restaurante_main;
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

    public String lat;
    public String lng;
    public String nome_rest;
    public String imagem_rest;
    public String morada;
    public String rating;
    public String votacoes;

    public EditText edit_dias;
    public EditText edit_hrs;
    public EditText edit_min;
    public EditText edit_sec;


    TextView edt;
    TextView edt1;
    TextView edt2;
    TextView edt3;
    TextView edt4;
    TextView edt5;
    TextView edt6;
    TextView edt7;
    TextView edt8;
    ImageView edt9;
    Menu menu;

    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_menu_especial);
        Intent intent = getIntent();
        rest_id = getIntent().getExtras().getString("rest_id");
        rest_cartao_id = getIntent().getExtras().getString("rest_cartao_id");
        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lon");
        nome_rest = getIntent().getExtras().getString("nome_rest");
        imagem_rest = getIntent().getExtras().getString("urlfoto");
        morada = getIntent().getExtras().getString("morada");


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
            case R.id.partilhar:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");


                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.o_que_pretende_pesquisar)));
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
                    text_preco_ant.setPaintFlags(text_preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    text_preco_ant.setText(some_list.get(position).getPreco_ant());
                    text_nr_ofertas.setText(some_list.get(position).getReserva_feita());
                    text_desc.setText(desc_list.get(position).getDescricao());
                    text_desc2.setVisibility(View.GONE);

                }else if(aux_tipo.equals("off")){
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_ant.setPaintFlags(text_preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    text_preco_act.setText(some_list.get(position).getDesconto()+"%");
                    //text_preco_ant.setVisibility(View.GONE);
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
                menu.setPreco_ant(dataJsonArr.getString("preco_ant"));
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



    public void asyncComplete(boolean success) {

        mAdapter = new MyListAdapter(this, R.layout.list_item, desc_list);

        LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_menuespcial, mListView, false);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_menuespecial, mListView, false);

        mListView.addFooterView(footer, null, false);
        mListView.addHeaderView(header, null, false);

        LinearLayout forward = (LinearLayout) footer.findViewById(R.id.linear_footer);
        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuEspecial.this, Restaurante_main.class);
                myIntent.putExtra("restaurante", rest_id); //Optional parameters
                myIntent.putExtra("urlfoto", imagem_rest);
                myIntent.putExtra("nome_rest", nome_rest);
                myIntent.putExtra("lat", lat);
                myIntent.putExtra("lon", lng);
                myIntent.putExtra("morada", morada);
                myIntent.putExtra("rating", "2");
                myIntent.putExtra("votacoes", "2");

                startActivity(myIntent);

                overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }
        });

        edt = (TextView) findViewById(R.id.text_rating_desc);
        edt1 = (TextView) findViewById(R.id.text_preco);
        edt2 = (TextView) findViewById(R.id.text_oferta);
        edt3 = (TextView) findViewById(R.id.text_preco_ant);
        edt4 = (TextView) findViewById(R.id.text_titulo);
        edt5 = (TextView) findViewById(R.id.textView3);
        edt6 = (TextView) findViewById(R.id.textView2);
        edt7 = (TextView) findViewById(R.id.textView);
        edt8 = (TextView) findViewById(R.id.textView10);
        edt9 = (ImageView) findViewById(R.id.imageView2);

        Calendar c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = df.format(c.getTime());

        Log.v("DATA INICIO",""+some_list.get(0).getDatafinal());
        Log.v("DATA ACTUAL",formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date a = null, b = null;
        try {
            a = sdf.parse(some_list.get(0).getDatafinal());
            b = sdf.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final StringBuilder time = new StringBuilder();
        //      .getTime() does the conversion: Date --> long
        CountDownTimer cdt = new CountDownTimer(a.getTime() - b.getTime(), 1000) {

            public void onTick(long millisUntilFinished) {

                //edt1.setText("Falta" + millisUntilFinished / 1000);
                //Log.v("FALTA TANTO TEMPO:", "" + millisUntilFinished / 1000);


                time.setLength(0);
                // Use days if appropriate
                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                    long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                    edt.setText(""+count);
                    if(count > 1) {
                        edt5.setText(R.string.dias);
                        time.append(count).append(" days, ");
                    }else {
                        edt5.setText(R.string.dia);
                        time.append(count).append(" day, ");
                    }
                    millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
                }

                if(millisUntilFinished > DateUtils.HOUR_IN_MILLIS) {
                    long count2 = millisUntilFinished / DateUtils.HOUR_IN_MILLIS;
                    edt1.setText(""+count2);
                    if(count2 > 1) {
                        edt6.setText(R.string.horas);
                        time.append(count2).append(" hours, ");
                    }else {
                        edt6.setText(R.string.hora);
                        time.append(count2).append(" hour, ");
                    }
                    millisUntilFinished %= DateUtils.HOUR_IN_MILLIS;
                }
                if(millisUntilFinished > DateUtils.MINUTE_IN_MILLIS) {
                    long count3 = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
                    edt2.setText(""+count3);
                    if(count3 > 1) {
                        edt7.setText(R.string.mins);
                        time.append(count3).append(" minutes, ");
                    }else {
                        edt7.setText(R.string.min);
                        time.append(count3).append(" minute, ");
                    }
                    millisUntilFinished %= DateUtils.MINUTE_IN_MILLIS;
                }

                if(millisUntilFinished > DateUtils.SECOND_IN_MILLIS) {
                    long count4 = millisUntilFinished / DateUtils.SECOND_IN_MILLIS;
                    edt3.setText(""+count4);
                    if(count4 > 1) {
                        edt8.setText(R.string.secs);
                        time.append(count4).append(" minutes, ");
                    }else {
                        edt8.setText(R.string.sec);
                        time.append(count4).append(" minute, ");
                    }
                    millisUntilFinished %= DateUtils.MINUTE_IN_MILLIS;
                }

                time.append(DateUtils.formatElapsedTime(Math.round(millisUntilFinished / 1000d)));
                //edt1.setText(time.toString());
            }



            public void onFinish() {
                // TODO Auto-generated method stub

            }
        }.start();




        if(some_list.get(0).getTipo().equals("fixo")){


            edt.setVisibility(View.GONE);
            edt1.setVisibility(View.GONE);
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt4.setVisibility(View.GONE);
            edt5.setVisibility(View.GONE);
            edt6.setVisibility(View.GONE);
            edt7.setVisibility(View.GONE);
            edt8.setVisibility(View.GONE);
            edt9.setVisibility(View.GONE);
        }
        new DownloadImageTask((ImageView) header.findViewById(R.id.image_capa)).execute("http://menuguru.pt/"+ some_list.get(0).getImagem());


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