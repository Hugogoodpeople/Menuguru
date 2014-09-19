package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Menu_do_restaurante;

/**
 * Created by hugocosta on 19/09/14.
 */
public class Restaurante_main extends Activity {


    private static MyListAdapter mAdapter;
    private ArrayList<Menu_do_restaurante> some_list;
    private ListView gridView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_restaurante_main);



        new AsyncTaskParseJson(this).execute();
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
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_m_rest.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJson (Restaurante_main delegate){
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
                dict.put("rest_id","477");

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado da procura = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna isto"+jsonString);
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

                    some_list.add(menu);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }



    public void asyncComplete(boolean success){

        //mAdapter.notifyDataSetChanged();
        //mAdapter = new MyListAdapter(getApplicationContext(), R.layout.row_defenicoes, some_array);

        gridView = (ListView) findViewById(R.id.lista_menus_restaurante);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);


        LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup header2 = (ViewGroup) inflater.inflate(R.layout.header_restaurante_main, gridView, false);

        gridView.addHeaderView(header2, null, false);


        // Assign adapter to ListView
        //listView.setAdapter(adapter);
        mAdapter = new MyListAdapter(this, R.layout.grid_menu, some_list);
        gridView.setAdapter(mAdapter);



        // ListView Item Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                /*
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                Locais itemValue = local[position];
                Log.v("Clicou",itemValue.db_id);
                Globals.getInstance().setCidedade_id(itemValue.db_id);
                Globals.getInstance().setCidadeÇ_nome(itemValue.nome);

                Intent myIntent = new Intent(Localizacao.this, MainActivity.class);
                Localizacao.this.startActivity(myIntent);


                finish();
*/

            }

        });

    }




    public class MyListAdapter extends ArrayAdapter<Menu_do_restaurante> {

        Context myContext;


        public MyListAdapter(Context context, int textViewResourceId, ArrayList<Menu_do_restaurante> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;



            if (row == null)
                row=inflater.inflate(R.layout.grid_menu, parent, false);
            TextView label2=(TextView)row.findViewById(R.id.lista_menus_restaurante);
            label2.setText(some_list.get(position).getNome());

            ImageView icon=(ImageView)row.findViewById(R.id.imageView_menu_refugio);

            //icon.setImageResource(some_list.get(position).getImg());

            return row;
        }

    }

}
