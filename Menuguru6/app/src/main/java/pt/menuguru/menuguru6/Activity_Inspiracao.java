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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Inspiracao;

public class Activity_Inspiracao extends Activity implements AdapterView.OnItemClickListener {

    Inspiracao[] some_array = null;

    private static MyListAdapterInspiracoes mAdapter;

    private AbsListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiracoes);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));




        // Set the adapter
        mListView = (AbsListView) findViewById(R.id.lista_inspiracoes);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        new AsyncTaskParseJson(this).execute();
    }

    public void asyncComplete(boolean success){


        // mCallbacks.onButtonClicked();

        mAdapter = new MyListAdapterInspiracoes(this, R.layout.row_defenicoes, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public class MyListAdapterInspiracoes extends ArrayAdapter<Inspiracao> {


        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapterInspiracoes(Context context, int textViewResourceId,
                                      Inspiracao[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_inspiracao, parent, false);

            TextView label4=(TextView)row.findViewById(R.id.nomeInspiracao);
            label4.setText(some_array[position].getNome());


            ImageView imagem=(ImageView)row.findViewById(R.id.imagem_inspiracao);

            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImagem(), imagem);

            return row;
        }
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



                some_array = new Inspiracao[dataJsonArr.length()];
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

                    some_array[i] = inspira;
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
