package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.AvancadosObject;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.TopTitulosFiltros;


/**
 * Created by hugocosta on 11/09/14.
 */
public class Filtros_mega_avancados extends Activity
{

    private AbsListView mListView;
    private static MyListAdapter mAdapter;
    private ProgressDialog progressDialog;





    TopTitulosFiltros[] arrayTopTitulos = new TopTitulosFiltros[8];



    int selected = 1;

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton button6;
    private Button buttonGravar;


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
                preencherTabelas(1);
                highlightButton(1);
            }
        });

        button2 = (ImageButton) this.findViewById(R.id.buttonfiltros2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 2");
                preencherTabelas(2);
                highlightButton(2);
            }
        });

        button3 = (ImageButton) this.findViewById(R.id.buttonfiltros3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 3");
                preencherTabelas(3);
                highlightButton(3);
            }
        });

        button4 = (ImageButton) this.findViewById(R.id.buttonfiltros4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 4");
                preencherTabelas(4);
                highlightButton(4);
            }
        });

        button5 = (ImageButton) this.findViewById(R.id.buttonfiltros5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 5");
                preencherTabelas(5);
                highlightButton(5);
            }
        });

        button6 = (ImageButton) this.findViewById(R.id.buttonfiltros6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 6");
                preencherTabelas(6);
                highlightButton(6);
            }
        });

        buttonGravar = (Button) this.findViewById(R.id.buttonGravar);
        buttonGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button Gravar");

                Globals.getInstance().setFiltros(arrayTopTitulos);
                // depois tenho de lançar a pesquisa :)
                AbrirPesquisa();


            }
        });

        highlightButton(1);


        new AsyncTaskParseJson(this).execute();

    }

    private void AbrirPesquisa()
    {
        Intent pesquisa = new Intent(this, Resultados_filtros_avancados.class);
        startActivity(pesquisa);
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



            JSONArray ordenar = resultado.getJSONArray("sug");
            JSONObject idealObject = ordenar.getJSONObject(0);

            JSONArray array_conteudo = idealObject.getJSONArray("conteudo");
            AvancadosObject[] arrayAmbiente = new AvancadosObject[array_conteudo.length()];

            for (int i = 0; i < array_conteudo.length(); i++)
            {

                JSONObject jsonAmbiente = array_conteudo.getJSONObject(i);
                AvancadosObject objects = new AvancadosObject();
                objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                if (i == 0)
                    objects.setSelecionado(true);

                arrayAmbiente[i] = objects;

            }

            TopTitulosFiltros coiso1 = new TopTitulosFiltros();
            coiso1.setId_titulo(idealObject.getString("id_titulo"));
            coiso1.setTitulo(idealObject.getString("titulo"));
            coiso1.setMultiSelection(idealObject.getString("multi"));
            coiso1.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[0] = coiso1;

            //////////////////////////////////////////////////
            //////////////////////////////////////////////////


            ordenar = resultado.getJSONArray("coz");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()+1];

            for (int i = 0; i <= array_conteudo.length(); i++)
            {

                if (i == 0)
                {
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo("" + getString(R.string.todostipo) + " " + idealObject.getString("id_titulo"));
                    objects.setSelecionado(true);
                    arrayAmbiente[i] = objects;
                }else
                {
                    JSONObject jsonAmbiente = array_conteudo.getJSONObject(i-1);
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                    objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                    arrayAmbiente[i] = objects;
                }

            }




            TopTitulosFiltros coiso2 = new TopTitulosFiltros();
            coiso2.setId_titulo(idealObject.getString("id_titulo"));
            coiso2.setTitulo(idealObject.getString("titulo"));
            coiso2.setMultiSelection(idealObject.getString("multi"));
            coiso2.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[1] = coiso2;


            //////////////////////////////////////////////////
            //////////////////////////////////////////////////


            // este é o primeiro em que temos um caso especial em que temos 2 arrays em vez de um pk foi alterado depois de
            // a funcionalidade ter sido completada por isso teve de ser adaptado ás novas necessidades

            ordenar = resultado.getJSONArray("preA");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()+1];

            for (int i = 0; i <= array_conteudo.length(); i++)
            {

                if (i == 0)
                {
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(getString(R.string.todos) + " " +getString(R.string.precos));
                    objects.setSelecionado(true);
                    arrayAmbiente[i] = objects;
                }else
                {
                    JSONObject jsonAmbiente = array_conteudo.getJSONObject(i-1);
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                    objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                    arrayAmbiente[i] = objects;
                }

            }


            TopTitulosFiltros coiso3 = new TopTitulosFiltros();
            coiso3.setId_titulo(idealObject.getString("id_titulo"));
            coiso3.setTitulo(idealObject.getString("titulo"));
            coiso3.setMultiSelection(idealObject.getString("multi"));
            coiso3.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[2] = coiso3;

            //////////////////////////////////////////////////
            //////////////////////////////////////////////////


            // este é o primeiro em que temos um caso especial em que temos 2 arrays em vez de um pk foi alterado depois de
            // a funcionalidade ter sido completada por isso teve de ser adaptado ás novas necessidades

            ordenar = resultado.getJSONArray("preJ");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()];

            for (int i = 0; i < array_conteudo.length(); i++)
            {

                JSONObject jsonAmbiente = array_conteudo.getJSONObject(i);
                AvancadosObject objects = new AvancadosObject();
                objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                arrayAmbiente[i] = objects;

            }


            TopTitulosFiltros coiso4 = new TopTitulosFiltros();
            coiso4.setId_titulo(idealObject.getString("id_titulo"));
            coiso4.setTitulo(idealObject.getString("titulo"));
            coiso4.setMultiSelection(idealObject.getString("multi"));
            coiso4.setArrayObjectos(arrayAmbiente);




            arrayTopTitulos[3] = coiso4;


            //////////////////////////////////////////////////
            //////////////////////////////////////////////////


            ordenar = resultado.getJSONArray("opa");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()+1];

            for (int i = 0; i <= array_conteudo.length(); i++)
            {

                if (i == 0)
                {
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(getString(R.string.todos) + " " +getString(R.string.opcoes));
                    objects.setSelecionado(true);
                    arrayAmbiente[i] = objects;
                }else
                {
                    JSONObject jsonAmbiente = array_conteudo.getJSONObject(i-1);
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                    objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                    arrayAmbiente[i] = objects;
                }

            }


            TopTitulosFiltros coiso5 = new TopTitulosFiltros();
            coiso5.setId_titulo(idealObject.getString("id_titulo"));
            coiso5.setTitulo(idealObject.getString("titulo"));
            coiso5.setMultiSelection(idealObject.getString("multi"));
            coiso5.setArrayObjectos(arrayAmbiente);




            arrayTopTitulos[4] = coiso5;


            //////////////////////////////////////////////////
            //////////////////////////////////////////////////

            ordenar = resultado.getJSONArray("pag");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()];

            for (int i = 0; i < array_conteudo.length(); i++)
            {

                JSONObject jsonAmbiente = array_conteudo.getJSONObject(i);
                AvancadosObject objects = new AvancadosObject();
                objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                arrayAmbiente[i] = objects;

            }


            TopTitulosFiltros coiso6 = new TopTitulosFiltros();
            coiso6.setId_titulo(idealObject.getString("id_titulo"));
            coiso6.setTitulo(idealObject.getString("titulo"));
            coiso6.setMultiSelection(idealObject.getString("multi"));
            coiso6.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[5] = coiso6;


            //////////////////////////////////////////////////
            //////////////////////////////////////////////////

            ordenar = resultado.getJSONArray("ideal");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()+1];

            for (int i = 0; i <= array_conteudo.length(); i++)
            {

                if (i == 0)
                {
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(getString(R.string.todos) + " " +getString(R.string.idealpara));
                    objects.setSelecionado(true);
                    arrayAmbiente[i] = objects;
                }else
                {
                    JSONObject jsonAmbiente = array_conteudo.getJSONObject(i-1);
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                    objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                    arrayAmbiente[i] = objects;
                }

            }


            TopTitulosFiltros coiso7 = new TopTitulosFiltros();
            coiso7.setId_titulo(idealObject.getString("id_titulo"));
            coiso7.setTitulo(idealObject.getString("titulo"));
            coiso7.setMultiSelection(idealObject.getString("multi"));
            coiso7.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[6] = coiso7;


            //////////////////////////////////////////////////
            //////////////////////////////////////////////////

            ordenar = resultado.getJSONArray("amb");
            idealObject = ordenar.getJSONObject(0);

            array_conteudo = idealObject.getJSONArray("conteudo");
            arrayAmbiente = new AvancadosObject[array_conteudo.length()+1];

            for (int i = 0; i <= array_conteudo.length(); i++)
            {

                if (i == 0)
                {
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(getString(R.string.todos) + " " +getString(R.string.ambiente));
                    objects.setSelecionado(true);
                    arrayAmbiente[i] = objects;
                }else
                {
                    JSONObject jsonAmbiente = array_conteudo.getJSONObject(i-1);
                    AvancadosObject objects = new AvancadosObject();
                    objects.setSub_titulo(jsonAmbiente.getString("sub_titulo"));
                    objects.setId_sub_titulo(jsonAmbiente.getString("id_sub_titulo"));
                    arrayAmbiente[i] = objects;
                }

            }


            TopTitulosFiltros coiso8 = new TopTitulosFiltros();
            coiso8.setId_titulo(idealObject.getString("id_titulo"));
            coiso8.setTitulo(idealObject.getString("titulo"));
            coiso8.setMultiSelection(idealObject.getString("multi"));
            coiso8.setArrayObjectos(arrayAmbiente);

            arrayTopTitulos[7] = coiso8;


            Globals.getInstance().setFiltros(arrayTopTitulos);

            Log.v("sdffgddvsdsv","conteudo do array de pagamentos = "+ idealObject.getString("id_titulo"));



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
        preencherTabelas(selected);

    }

    public void preencherTabelas(final int selecionado)
    {

        // mCallbacks.onButtonClicked();
        selected = selecionado;

        mListView = (ListView) findViewById(R.id.lista_avancada);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);

        switch (selecionado)
        {
            case 1:
            {
                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, arrayTopTitulos[selecionado-1].getArrayObjectos());
                break;
            }
            case 2:
            {
                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, arrayTopTitulos[selecionado-1].getArrayObjectos());
                break;
            }
            case 3:
            {
                // caso especial
                AvancadosObject[] tmp = new AvancadosObject[arrayTopTitulos[2].getArrayObjectos().length + arrayTopTitulos[3].getArrayObjectos().length];
                System.arraycopy(arrayTopTitulos[2].getArrayObjectos(), 0, tmp, 0, arrayTopTitulos[2].getArrayObjectos().length);
                System.arraycopy(arrayTopTitulos[3].getArrayObjectos(), 0, tmp, arrayTopTitulos[2].getArrayObjectos().length, arrayTopTitulos[3].getArrayObjectos().length);

                //coiso3.setArrayObjectos(tmp);  // s == { 0, 1, 2, 3, 4, 5 }

                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, tmp);
                break;
            }
            case 4:
            {
                // caso especial
                AvancadosObject[] tmp = new AvancadosObject[arrayTopTitulos[4].getArrayObjectos().length + arrayTopTitulos[5].getArrayObjectos().length];
                System.arraycopy(arrayTopTitulos[4].getArrayObjectos(), 0, tmp, 0, arrayTopTitulos[4].getArrayObjectos().length);
                System.arraycopy(arrayTopTitulos[5].getArrayObjectos(), 0, tmp, arrayTopTitulos[2].getArrayObjectos().length, arrayTopTitulos[5].getArrayObjectos().length);

                //coiso3.setArrayObjectos(tmp);  // s == { 0, 1, 2, 3, 4, 5 }

                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, tmp);
                break;
            }
            case 5:
            {
                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, arrayTopTitulos[6].getArrayObjectos());
                break;
            }
            case 6:
            {
                mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, arrayTopTitulos[7].getArrayObjectos());
                break;
            }

        }

        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);



        // ListView Item Click Listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                // esta parte serve para dizer se é multi selection ou single selection
                // e tambem para se selecionar algum indice != de 0 ele desceleciona o 0

                if(position != 0) {
                    if (selected == 1 || selected == 3) {
                        int valorCorrecto = valorDoIndiceCorrecto(selected);


                        if (arrayTopTitulos[valorCorrecto].getArrayObjectos().length > position) {
                            descelecionar(selected - 1);
                        } else {
                            descelecionar(selected);
                        }

                    } else {
                        int valorCorrecto = valorDoIndiceCorrecto(selected);
                        descelecionarDefault(valorCorrecto);
                    }
                }else
                {
                    int valorCorrecto = valorDoIndiceCorrecto(selected);
                    descelecionar(valorCorrecto);

                    if (valorCorrecto == 4 )
                        descelecionar(5);
                }


                selecionar(position);


                mAdapter.notifyDataSetChanged();


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


            // aqui tenho de ter uma validação para verificar qual dos arrays é que tenho de abrir

            AvancadosObject[] tempArray = new AvancadosObject[0];

          switch (selected)
          {
              case 1:
              {
                  tempArray = arrayTopTitulos[0].getArrayObjectos();
                  break;
              }
              case 2:
              {
                  tempArray = arrayTopTitulos[1].getArrayObjectos();
                  break;
              }
              case 3:
              {
                  AvancadosObject[] tmp = new AvancadosObject[arrayTopTitulos[2].getArrayObjectos().length + arrayTopTitulos[3].getArrayObjectos().length];
                  System.arraycopy(arrayTopTitulos[2].getArrayObjectos(), 0, tmp, 0, arrayTopTitulos[2].getArrayObjectos().length);
                  System.arraycopy(arrayTopTitulos[3].getArrayObjectos(), 0, tmp, arrayTopTitulos[2].getArrayObjectos().length, arrayTopTitulos[3].getArrayObjectos().length);

                  //coiso3.setArrayObjectos(tmp);  // s == { 0, 1, 2, 3, 4, 5 }

                  tempArray = tmp;
                  break;
              }
              case 4:
              {
                  AvancadosObject[] tmp = new AvancadosObject[arrayTopTitulos[4].getArrayObjectos().length + arrayTopTitulos[5].getArrayObjectos().length];
                  System.arraycopy(arrayTopTitulos[4].getArrayObjectos(), 0, tmp, 0, arrayTopTitulos[4].getArrayObjectos().length);
                  System.arraycopy(arrayTopTitulos[5].getArrayObjectos(), 0, tmp, arrayTopTitulos[4].getArrayObjectos().length, arrayTopTitulos[5].getArrayObjectos().length);

                  //coiso3.setArrayObjectos(tmp);  // s == { 0, 1, 2, 3, 4, 5 }

                  tempArray = tmp;
                  break;
              }
              case 5:
              {
                  tempArray = arrayTopTitulos[6].getArrayObjectos();
                  break;
              }
              case 6:
              {
                  tempArray = arrayTopTitulos[7].getArrayObjectos();
                  break;
              }
          }


            // por causa das coisas que tiveram de ser alteradas no iOS tenho de fazer as mesmas invençoes aqui
            int valorNoArray = valorDoIndiceCorrecto(selected);



            if ((selected == 3 && position == arrayTopTitulos[2].getArrayObjectos().length ) || (selected == 4 && position == arrayTopTitulos[4].getArrayObjectos().length ) || position == 0)
            {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_header, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(tempArray[position].getSub_titulo());
                TextView label2;
                if(position != arrayTopTitulos[2].getArrayObjectos().length && position != 0)
                {
                    label2 = (TextView) row.findViewById(R.id.headerText);
                    label2.setText(arrayTopTitulos[valorNoArray ].getTitulo());
                }else if (position != 0)
                {
                    label2 = (TextView) row.findViewById(R.id.headerText);
                    label2.setText(arrayTopTitulos[valorNoArray +1].getTitulo());
                }

                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }
            else if (position == 0 ) {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_header, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(tempArray[position].getSub_titulo());


                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }else if (position == 1 ) {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_header, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(tempArray[position].getSub_titulo());

                TextView label2 = (TextView) row.findViewById(R.id.headerText);
                label2.setText(arrayTopTitulos[valorNoArray].getTitulo());

                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }else
            {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_defenicoes, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(tempArray[position].getSub_titulo());
                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_action_frame_b);
            }


            int valorCorrecto = valorDoIndiceCorrecto(selected);

            ImageView icon1 = (ImageView) row.findViewById(R.id.icon);

            if (arrayTopTitulos[valorCorrecto].getArrayObjectos().length > position)
            {
                // aqui ainda tem muitca coisa que lhe diga para por a funcionar a
                if (!arrayTopTitulos[valorDoIndiceCorrecto(selected)].getArrayObjectos()[position].getSelecionado())
                    icon1.setImageResource(R.drawable.ic_action_frame_b);
                else
                    icon1.setImageResource(R.drawable.ic_action_framecheck_b);

            }else
            {
                int novoIndex =  position - arrayTopTitulos[valorCorrecto].getArrayObjectos().length;

                // aqui ainda tem muitca coisa que lhe diga para por a funcionar a
                if (!arrayTopTitulos[valorCorrecto+1].getArrayObjectos()[novoIndex].getSelecionado())
                    icon1.setImageResource(R.drawable.ic_action_frame_b);
                else
                    icon1.setImageResource(R.drawable.ic_action_framecheck_b);
            }


            return row;
        }

    }

    public int valorDoIndiceCorrecto(int selecionado)
    {
        int correcto =0;
        switch (selecionado)
        {
            case 1:
            {
                correcto = 0;
                break;
            }
            case 2:
            {
                correcto = 1;
                break;
            }
            case 3:
            {
                correcto = 2;
                break;
            }
            case 4:
            {
                correcto = 4;
                break;
            }
            case 5:
            {
                correcto = 6;
                break;
            }
            case 6:
            {
                correcto = 7;
                break;
            }

        }

        return correcto;
    }

    public boolean selecionar(int index)
    {
        // tenho de ter em conta que em todos adicionou mais um indice ao inicio exceto no 3 e no 5
        // tambem tenho de ter cuidado pois tem aqui arrays juntos que nao deviam estar
        int valorCorrecto = valorDoIndiceCorrecto(selected);

        // ok a logica da cena vai ser assim:
        // no indice selecionado se for 2 ou 3 tenho de fazer na mesma o valor correcto
        // o indice se passar do tamanho do primeiro array tenho de passar para o segundo array e entao
        // remover o tamanho do primeiro array ao indice para começar do 0 de novo
        if (arrayTopTitulos[valorCorrecto].getArrayObjectos().length>index)
        {
            arrayTopTitulos[valorCorrecto].getArrayObjectos()[index].setSelecionado(!arrayTopTitulos[valorCorrecto].getArrayObjectos()[index].getSelecionado());

        }else
        {
            int novoIndex = index - arrayTopTitulos[valorCorrecto].getArrayObjectos().length;
            arrayTopTitulos[valorCorrecto+1].getArrayObjectos()[novoIndex].setSelecionado(!arrayTopTitulos[valorCorrecto+1].getArrayObjectos()[novoIndex].getSelecionado());
        }


        return true;
    }


    public void descelecionar(int index)
    {
        for (int i = 0 ; i < arrayTopTitulos[index].getArrayObjectos().length ; i++ )
        {
            arrayTopTitulos[index].getArrayObjectos()[i].setSelecionado(false);
        }

        mAdapter.notifyDataSetChanged();
    }

    public void descelecionarDefault(int index)
    {
        arrayTopTitulos[index].getArrayObjectos()[0].setSelecionado(false);
    }


    public void limparTudo()
    {
        for(int i = 0; i< arrayTopTitulos.length ; i++)
        {
            descelecionar(i);
        }
    }

}

