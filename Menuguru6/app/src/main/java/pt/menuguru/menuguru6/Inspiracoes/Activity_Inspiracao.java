package pt.menuguru.menuguru6.Inspiracoes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.Inspiracao;
import pt.menuguru.menuguru6.Utils.InspiracaoItem;

public class Activity_Inspiracao extends Activity implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Inspiracao> listDataHeader;
    HashMap<Inspiracao, List<InspiracaoItem>> listDataChild;

    private Dialog dialog;
    public Inspiracao[] some_array;

    private int dia_selecionado= -1;
    private int ref_selecionado= -1;
    private ImageButton locButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiracoes);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        SimpleDateFormat sdf = new SimpleDateFormat("EEEE"/*, Locale.FRANCE */);
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);


        Calendar cc = Calendar.getInstance();
        cc.setTime(new Date());
        dia_selecionado = cc.get(Calendar.DAY_OF_WEEK) -1;
        int hora = cc.get(Calendar.HOUR_OF_DAY);

        String refeicao;

        if (hora >= 17)
        {
            refeicao =  getResources().getStringArray(R.array.refeicoes)[0];
            ref_selecionado = 0;
        }
        else
        {
            refeicao =  getResources().getStringArray(R.array.refeicoes)[1];
            ref_selecionado = 1;
        }



        Button p1_button = (Button)findViewById(R.id.button_data);
        p1_button.setText(dayOfTheWeek + " " + refeicao);





        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inspiras);

        ListView listDias = (ListView) dialog.findViewById(R.id.listView_dias);

        AdapterStringDias adapterString = new AdapterStringDias(this, R.layout.list_spiner,  getResources().getStringArray(R.array.dias_semana));

        listDias.setAdapter(adapterString);

        listDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                }
                view.setBackgroundColor(getResources().getColor(R.color.dourado));
                //sel_nr_pes = nr_pes_list.get(position).getNr();
                dia_selecionado = position;
            }
        });


        ListView listrefs = (ListView) dialog.findViewById(R.id.listView_refeicao);

        AdapterStringRef adapterStringRef = new AdapterStringRef(this, R.layout.list_spiner,  getResources().getStringArray(R.array.refeicoes));

        listrefs.setAdapter(adapterStringRef);

        listrefs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                }
                view.setBackgroundColor(getResources().getColor(R.color.dourado));
                //sel_nr_pes = nr_pes_list.get(position).getNr();
                ref_selecionado = position;
            }
        });


        Button buttonGravar = (Button) dialog.findViewById(R.id.button_gravar_inspiras);
        buttonGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tenho de colocar o selecionado na dialog no botao
                Button p1_button = (Button)findViewById(R.id.button_data);
                p1_button.setText( getResources().getStringArray(R.array.dias_semana)[dia_selecionado] + " " +  getResources().getStringArray(R.array.refeicoes)[ref_selecionado]);
                locButton.startAnimation(AnimationUtils.loadAnimation(Activity_Inspiracao.this, R.anim.rodar));
                chamarInspiras();
                dialog.dismiss();
            }
        });


        p1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });



        expListView = (ExpandableListView) findViewById(R.id.lista_inspiracoes);

        expListView.setOnChildClickListener( this);

        chamarInspiras();

    }



    private void chamarInspiras()
    {

        new AsyncTaskParseJson(this).execute();
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        InspiracaoItem childText = (InspiracaoItem) listAdapter.getChild(groupPosition, childPosition);
        Log.v("child selected",  childText.getNome());

        Intent intent = new Intent(this, Resultado_inspiracao.class);
        intent.putExtra("id_item",childText.getDb_id());
        intent.putExtra("local",childText.getNome());
        startActivity(intent);

        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

        return false;
    }


    /*
     * Preparing the list data
     */


    public void asyncComplete(boolean success){
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inspiracao, menu);


        locButton = (ImageButton) menu.findItem(R.id.action_actualizar_inspiras).getActionView();



        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Intent myIntent = new Intent(Inspiracao.this, MainActivity.class);
                //startActivity(myIntent);
                finish();

                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            case R.id.action_actualizar_inspiras:

                new AsyncTaskParseJson(this).execute();

                return false;
            default:
                break;
        }

        return false;
    }



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Activity_Inspiracao delegate;

        // set your json string url here
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_inspiracao_mostrar.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Activity_Inspiracao delegate){
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

                // para a refeiçao escolhida
                String refeicao;
                if (ref_selecionado ==  0)
                    refeicao = "almoco";
                else
                    refeicao = "jantar";

                // para o dia da semana selecionado
                int dia_semana;
                if(dia_selecionado == 0) {
                    dia_semana = 7;
                }
                else
                {
                    dia_semana = dia_selecionado;
                }


                dict.put("lang", Globals.getInstance().getLingua());
                dict.put("almajant",refeicao);
                dict.put("diasemana",dia_semana);


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("inspiracao");

                // loop through all users


                listDataHeader = new ArrayList<Inspiracao>();
                listDataChild = new HashMap<Inspiracao, List<InspiracaoItem>>();

                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    // Storing each json item in variable
                    String firstname = c.getString("tituloinsp");

                    // show the values in our logcat
                    Log.v(TAG, "Inspiração name: " + firstname
                    );


                    Inspiracao inspira = new Inspiracao();
                    inspira.setNome(firstname);
                    inspira.setUrlImagem(c.getString("imagem"));

                    // Adding child data
                    listDataHeader.add(inspira);


                    // Adding child data
                    List<InspiracaoItem> top250 = new ArrayList<InspiracaoItem>();



                    // tenho de fazer um ciclo

                    JSONArray items = c.getJSONArray("subtitulos");

                    for (int z = 0; z < items.length(); z++)
                    {
                        JSONObject item = items.getJSONObject(z);

                        InspiracaoItem ii = new InspiracaoItem();
                        ii.setNome(item.getString("subtitulo"));
                        ii.setDb_id(item.getString("subtituloid"));


                        top250.add(ii);

                    }

                    listDataChild.put(listDataHeader.get(i), top250); // Header, Child data
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


    public class AdapterStringDias extends ArrayAdapter<String> {

        Context myContext;
        public String[] some_list;

        public AdapterStringDias(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            this.some_list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.list_spiner, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.textSpiner);
            textView.setText(some_list[position]);


            if (position == dia_selecionado)
                row.setBackgroundColor(getResources().getColor(R.color.dourado));
            else
                row.setBackgroundColor(getResources().getColor(R.color.white));


            return row;
        }

    }


    public class AdapterStringRef extends ArrayAdapter<String> {

        Context myContext;
        public String[] some_list;

        public AdapterStringRef(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            this.some_list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.list_spiner, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.textSpiner);
            textView.setText(some_list[position]);


            if (position == ref_selecionado)
                row.setBackgroundColor(getResources().getColor(R.color.dourado));
            else
                row.setBackgroundColor(getResources().getColor(R.color.white));


            return row;
        }

    }

}
