package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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


public class Inicio extends Fragment implements AbsListView.OnItemClickListener {

    String value;

    String[] some_array = null;

    private MainActivity delegateInicio;
    //private OnFragmentInteractionListener mListener;


    private static String url = "http://10.0.2.2/JSON/";
    //JSON Node Names
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    JSONArray user = null;


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapter mAdapter;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void asyncComplete(boolean success){

       // mAdapter.notifyDataSetChanged();


        //delegateInicio.asyncComplete(true);

    }






    public class MyListAdapter extends ArrayAdapter<String> {

        Context myContext;

        public MyListAdapter(Context context, int textViewResourceId,
                             String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =
                    (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.fragment_inicio, parent, false);
            TextView label=(TextView)row.findViewById(R.id.distancia);
            label.setText(some_array[position]);
            ImageView icon=(ImageView)row.findViewById(R.id.capa);

            //Customize your icon here
            icon.setImageResource(R.drawable.sem_foto);

            return row;
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defenicoesteste, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


// we will using AsyncTask during parsing
        new AsyncTaskParseJson(this).execute();



        return view;
    }



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Inicio delegate;

        // set your json string url here
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_especiais_todos.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Inicio delegate){
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
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                // get the array of users

                dataJsonArr = json.getJSONArray("res");

                // loop through all users


                some_array =  new String[dataJsonArr.length()];
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    // Storing each json item in variable
                    String firstname = c.getString("descricao");


                    // show the values in our logcat
                    Log.v(TAG, "firstname: " + firstname
                            );


                    some_array[i] = firstname;


                }

                //some_array = getResources().getStringArray(R.array.defenicoes_array);

                // TODO: Change Adapter to display your content
                mAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);


                Log.v("sdffgddvsdsv","objecto = "+ json);

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().getActionBar().setCustomView(R.layout.tab_header);
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getActivity().getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView t =(TextView) getActivity().findViewById(R.id.mytext);
        //ImageView imag = (ImageView) getActivity().findViewById(R.id.capa);
        //imag.setImageResource(R.drawable.bck_refugio);
        Intent intent = getActivity().getIntent();
        value = intent.getStringExtra("local");
        if(value == null || value.trim().equals("")){value="Perto de mim";}
        t.setText(value);

        /*
        some_array = getResources().getStringArray(R.array.defenicoes_array);

        // TODO: Change Adapter to display your content

        MyListAdapter myListAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);

        MyListAdapter myListAdapter =
                new MyListAdapter(getActivity(), R.layout.activity_my, some_array);


        mAdapter =myListAdapter;

    */


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_localizacao:
                Intent myIntent = new Intent(getActivity(), Localizacao.class);
                myIntent.putExtra("local", value); //Optional parameters
                getActivity().startActivity(myIntent);
                this.getActivity().finish();
                return false;
            case R.id.action_pesquisa:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder
                        .setItems(R.array.pesquisa, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent myIntent = new Intent(getActivity(), Pesquisa_avancada.class);
                                        startActivity(myIntent);
                                        break;
                                    case 1:
                                        Intent myIntent2 = new Intent(getActivity(), Inspiracao.class);
                                        startActivity(myIntent2);
                                        break;
                                    case 2:

                                    default:
                                        break;
                                }
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;


            default:
                break;
        }

        return false;
    }
}
