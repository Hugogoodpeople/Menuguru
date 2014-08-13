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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Locais;
import pt.menuguru.menuguru6.Utils.Restaurante;


public class Localizacao extends Activity {
    ListView listView;

    Locais[] local = null;

    public String value;

    public Locais[] some_array = null;

    SearchView inputSearch;

    ArrayAdapter<Locais> adapter;

    private ProgressDialog progressDialog;


    private static MyListAdapter mAdapter;


    public class MyListAdapter extends ArrayAdapter<Locais> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             Locais[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_defenicoes, parent, false);
            TextView label=(TextView)row.findViewById(R.id.month);
            label.setText(local[position].nome);

            return row;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);
        Intent intent = getIntent();
        value = intent.getStringExtra("local");
        //setTitle(value);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setCustomView(R.layout.tab_header);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView t =(TextView) findViewById(R.id.mytext);
        t.setText(value);
        inputSearch = (SearchView) findViewById(R.id.searchView);

        new AsyncTaskParseJson(this).execute();

    }


    public void asyncComplete(boolean success){

        //mAdapter.notifyDataSetChanged();
        //mAdapter = new MyListAdapter(getApplicationContext(), R.layout.row_defenicoes, some_array);

        listView = (ListView) findViewById(R.id.listV_localizacao);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);

        // Assign adapter to ListView
        //listView.setAdapter(adapter);
        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, local);
        listView.setAdapter(mAdapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Locais itemValue = local[position];
                Log.v("Clicou",itemValue.db_id);
                Globals.getInstance().setCidedade_id(itemValue.db_id);

                Intent myIntent = new Intent(Localizacao.this, MainActivity.class);
                Localizacao.this.startActivity(myIntent);


                finish();


            }

        });

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_search_cities.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Localizacao delegate;

        public AsyncTaskParseJson (Localizacao delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Localizacao.this);
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

                // get the array of users

                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+jsonString);
                }

                dataJsonArr = jsonObj.getJSONArray("resp");

                Log.v("JsonObject","objecto = "+ jsonObj);
                // loop through all users

                local = new Locais[dataJsonArr.length()];
                some_array = new Locais[dataJsonArr.length()];


                //local[0] = "Perto de mim";
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);
                    Locais locall = new Locais();
                    locall.nome = c.getString("nome");
                    locall.db_id = c.getString("id");
                    locall.longitude = c.getString("lon");
                    locall.latitude = c.getString("lat");


                    //local = c.getString("nome");
                    Log.v("Nome","objecto = "+ c.getString("nome"));
                    local[i] = locall;
                }
                Log.v("Nome","objecto = "+ some_array.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(Localizacao.this, MainActivity.class);
                myIntent.putExtra("local", value);
                Localizacao.this.startActivity(myIntent);
                finish();

                return false;
            default:
                break;
        }

        return false;
    }



}
