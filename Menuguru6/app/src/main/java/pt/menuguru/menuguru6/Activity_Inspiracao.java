package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.ExpandableListAdapter;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Inspiracao;
import pt.menuguru.menuguru6.Utils.InspiracaoItem;

public class Activity_Inspiracao extends Activity implements ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Inspiracao> listDataHeader;
    HashMap<Inspiracao, List<InspiracaoItem>> listDataChild;


    public Inspiracao[] some_array;


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

        Button p1_button = (Button)findViewById(R.id.button_dias);
        p1_button.setText(dayOfTheWeek);


// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lista_inspiracoes);



        expListView.setOnChildClickListener( this);
        // preparing list data
        //prepareListData();




        new AsyncTaskParseJson(this).execute();
    }




    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        InspiracaoItem childText = (InspiracaoItem) listAdapter.getChild(groupPosition, childPosition);
        Log.v("child selected",  childText.getNome());
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



                dict.put("lang","pt");
                dict.put("almajant","almoco");
                dict.put("diasemana","4");


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

}
