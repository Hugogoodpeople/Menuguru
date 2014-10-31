package pt.menuguru.menuguru;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.menuguru.menuguru.Json_parser.JSONParser;
import pt.menuguru.menuguru.Utils.Globals;
import pt.menuguru.menuguru.Utils.Locais;


public class Localizacao extends Activity{
    ListView listView;

    ArrayList<Locais>  local = null;

    public String value;

    SearchView inputSearch;

    private ProgressDialog progressDialog;

    private SearchView mSearchView;

    private static MyListAdapter mAdapter;

    private List<Locais> mOrigionalValues;
    private List<Locais> mObjects;


    public class MyListAdapter extends ArrayAdapter<Locais> {


        private Filter mFilter;

        Context myContext;

        public MyListAdapter(Context context, int textViewResourceId,
                             ArrayList<Locais> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            mOrigionalValues = objects;
            mObjects = objects;
        }


        public void add(Locais object) {
            mOrigionalValues.add(object);
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mObjects.size();
        }

        @Override
        public Locais getItem(int position) {
            return mObjects.get(position);
        }


        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new CustomFilter();
            }
            return mFilter;
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_defenicoes, parent, false);
            TextView label=(TextView)row.findViewById(R.id.month);
            label.setText(mObjects.get(position).nome);

            return row;
        }



        private class CustomFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint == null || constraint.length() == 0) {
                    ArrayList<Locais> list = new ArrayList<Locais>(mOrigionalValues);
                    results.values = list;
                    results.count = list.size();
                } else {
                    ArrayList<Locais> newValues = new ArrayList<Locais>();
                    for(int i = 0; i < mOrigionalValues.size(); i++) {
                        Locais item = mOrigionalValues.get(i);
                        if(item.getNome().toLowerCase().contains(constraint)) {
                            newValues.add(item);
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mObjects = (List<Locais>) results.values;
                Log.d("CustomArrayAdapter", String.valueOf(results.values));
                Log.d("CustomArrayAdapter", String.valueOf(results.count));
                notifyDataSetChanged();
            }

        }



    }

    @Override
    public void onStart()
    {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(Globals.get_instance().getLingua());
        res.updateConfiguration(conf, dm);

        setContentView(R.layout.activity_localizacao);




        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        getActionBar().setIcon(R.drawable.ic_close_b);


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
        //setupSearchView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Locais itemValue = mObjects.get(position);
                Log.v("Clicou",itemValue.db_id);
                Globals.getInstance().setCidedade_id(itemValue.db_id);
                Globals.getInstance().setCidadeÇ_nome(itemValue.nome);

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
                Log.v("JsonObject","objecto = "+ jsonObj);
                local = new ArrayList<Locais>();
                                //local[0] = "Perto de mim";
                //Locais localli = new Locais();
                //localli.nome = "Perto de mim";
                //localli.db_id = "0";
                //local[0] = localli;
                for (int i = -1; i < dataJsonArr.length(); i++) {


                    Locais locall = new Locais();
                    if(i==-1){
                    locall.nome = getString(R.string.perto_de_mim);
                    locall.db_id = "0";
                    local.add(locall);
                    }else{
                    JSONObject c = dataJsonArr.getJSONObject(i);
                    //Locais locall = new Locais();
                    locall.nome = c.getString("nome");
                    locall.db_id = c.getString("id");
                    locall.longitude = c.getString("lon");
                    locall.latitude = c.getString("lat");
                    Log.v("Nome","objecto = "+ c.getString("nome"));
                    local.add(locall);
                    }
                    //

                }

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
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);

                return false;
            default:
                break;
        }

        return false;
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        //mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint(getString(R.string.procurar));
    }

    /*
    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }
    */

    /*
    @Override
    public boolean onQueryTextChange(String newText) {
        listView.setFilterText(newText.toString());

        mAdapter.getFilter().filter(newText.toLowerCase());

        return false;
    }
    */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchable, menu);
        MenuItem menuItem = menu.findItem(R.id.pesquisa_prato);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.procurar));

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate!=null) {
            searchPlate.setBackgroundColor(Color.LTGRAY);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
                //searchText.setTextColor(Color.WHITE);
                //searchText.setHintTextColor(Color.WHITE);
            }
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("algo", "Escreveu " + query);

                listView.setFilterText(query.toString());

                mAdapter.getFilter().filter(query.toLowerCase());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setFilterText(newText.toString());

                mAdapter.getFilter().filter(newText.toLowerCase());

                return false;
            }

        });




        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.v("fazcoisas","clicou no botao de pesquisa");

            } });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Log.v("cenasfechar","clicou para fechar");

                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        return true;

    }


}
