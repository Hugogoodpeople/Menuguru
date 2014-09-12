package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.AvancadosObject;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;
import pt.menuguru.menuguru6.Utils.TopTitulosFiltros;
import pt.menuguru.menuguru6.Utils.Utils;


/**
 * Created by hugocosta on 11/09/14.
 */
public class Filtros_mega_avancados extends Activity
{

    private AbsListView mListView;
    private static MyListAdapter mAdapter;
    private ProgressDialog progressDialog;

    AvancadosObject[] some_array = null;

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_avancada);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        button1 = (ImageButton) this.findViewById(R.id.buttonfiltros1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 1");
                highlightButton(1);
            }
        });

        button2 = (ImageButton) this.findViewById(R.id.buttonfiltros2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 2");
                highlightButton(2);
            }
        });

        button3 = (ImageButton) this.findViewById(R.id.buttonfiltros3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 3");
                highlightButton(3);
            }
        });

        button4 = (ImageButton) this.findViewById(R.id.buttonfiltros4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 4");
                highlightButton(4);
            }
        });

        button5 = (ImageButton) this.findViewById(R.id.buttonfiltros5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 5");
                highlightButton(5);
            }
        });

        button6 = (ImageButton) this.findViewById(R.id.buttonfiltros6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 6");
                highlightButton(6);
            }
        });

        highlightButton(1);


        new AsyncTaskParseJson(this).execute();

    }

    private void highlightButton(int selectedButton)
    {

        button1.setImageResource(R.drawable.ico_ordem_1);
        button2.setImageResource(R.drawable.ico_cozinha);
        button3.setImageResource(R.drawable.ico_preco);
        button4.setImageResource(R.drawable.ico_opcoes);
        button5.setImageResource(R.drawable.ico_ideal);
        button6.setImageResource(R.drawable.ico_ambiente);


        switch (selectedButton)
        {
            case 1:
            {
                button1.setImageResource(R.drawable.ico_ordem_2);
                break;
            }
            case 2:
            {
                button2.setImageResource(R.drawable.ico_cozinha_2);
                break;
            }
            case 3:
            {
                button3.setImageResource(R.drawable.ico_preco_2);
                break;
            }
            case 4:
            {
                button4.setImageResource(R.drawable.ico_opcoes_2);
                break;
            }
            case 5:
            {
                button5.setImageResource(R.drawable.ico_ideal_2);
                break;
            }
            case 6:
            {
                button6.setImageResource(R.drawable.ico_ambiente_2);
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inspiracao, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Intent myIntent = new Intent(Inspiracao.this, MainActivity.class);
                //startActivity(myIntent);
                finish();

                return false;
            default:
                break;
        }

        return false;
    }



// para ir buscar a net os filtros super mega avançados
// you can make this class as another java file so it will be separated from your main activity.
public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    final String TAG = "AsyncTaskParseJson.java";


    String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_listar_filtros_avancados.php";

    // contacts JSONArray
    JSONArray dataJsonArr = null;

    private Filtros_mega_avancados delegate;

    public AsyncTaskParseJson (Filtros_mega_avancados delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(Filtros_mega_avancados.this);
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

            String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

            // try parse the string to a JSON object
            try {
                Log.v("Ver Json ","Ele retorna isto"+jsonString);
                jsonObj = new JSONObject(jsonString);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }
            // get the array of users


            JSONObject resultado = jsonObj.getJSONObject("topcoes");
            JSONArray ideal = resultado.getJSONArray("sug");
            JSONObject idealObject = ideal.getJSONObject(0);



            JSONArray array_conteudo = idealObject.getJSONArray("conteudo");
            AvancadosObject[] arrayAmbiente = new AvancadosObject[array_conteudo.length()];

            for (int i = 0; i < array_conteudo.length(); i++)
            {

                JSONObject jsonAmbiente = array_conteudo.getJSONObject(i);
                AvancadosObject objects = new AvancadosObject();
                objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                arrayAmbiente[i] = objects;

            }


            TopTitulosFiltros ambiente = new TopTitulosFiltros();
            ambiente.setId_titulo(idealObject.getString("id_titulo"));
            ambiente.setTitulo(idealObject.getString("titulo"));
            ambiente.setMultiSelection(idealObject.getString("multi"));
            ambiente.setArrayObjectos(arrayAmbiente);



            Log.v("sdffgddvsdsv","conteudo do array de pagamentos = "+ idealObject.getString("id_titulo"));

            some_array = arrayAmbiente;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

}


    public void asyncComplete(boolean success)
    {
        // mCallbacks.onButtonClicked();

        mListView = (ListView) findViewById(R.id.lista_avancada);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);


        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        // ListView Item Click Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {


                for (int a = 0; a < parent.getChildCount(); a++) {
                    ImageView icon1 = (ImageView) parent.getChildAt(a).findViewById(R.id.icon);
                    //parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.silver) );
                    icon1.setImageResource(R.drawable.ic_action_frame_b);
                }

                //view.setBackgroundColor(Color.RED);


                ImageView icon2 = (ImageView) view.findViewById(R.id.icon);
                //Customize your icon here
                icon2.setImageResource(R.drawable.ic_action_framecheck_b);

            }

        });

    }


    public class MyListAdapter extends ArrayAdapter<AvancadosObject> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             AvancadosObject[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(context);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row;

            if (position == 0 || position == 1 ) {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_header, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(some_array[position].getSub_titulo());


                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }else
            {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_defenicoes, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(some_array[position].getSub_titulo());
                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }

            return row;
        }

    }

}