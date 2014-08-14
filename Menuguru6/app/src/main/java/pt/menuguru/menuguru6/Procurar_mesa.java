package pt.menuguru.menuguru6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;


public class Procurar_mesa extends Fragment {

    private ProgressDialog progressDialog;

    public ImageLoader imageLoader;
    String imagem = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        /*
        Button bt_data = (Button) getActivity().findViewById(R.id.bt_cal);
        Button bt_hora = (Button) getActivity().findViewById(R.id.bt_hora);
        Button bt_agora = (Button) getActivity().findViewById(R.id.bt_agora);
        Button bt_mais = (Button) getActivity().findViewById(R.id.bt_mais);
        Button bt_menos = (Button) getActivity().findViewById(R.id.bt_menos);
        Button bt_sem_data = (Button) getActivity().findViewById(R.id.bt_sem_data);
        Button bt_procurar = (Button) getActivity().findViewById(R.id.bt_procurar);
        */




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new AsyncTaskParseJson(this).execute();
        // Inflate the layout for this fragment
        imageLoader=new ImageLoader(getActivity().getApplicationContext());
        return inflater.inflate(R.layout.fragment_procurar_mesa, container, false);
    }





    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Procurar_mesa delegate;

        // set your json string url here
        //String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_especiais_todos.php";
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_pubfiltros.php";

        // contacts JSONArray
        JSONObject dataJsonArr = null;

        public AsyncTaskParseJson (Procurar_mesa delegate){
            this.delegate = delegate;
        }


        @Override
        protected void onPreExecute() {

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
                dict.put("tamanho","640x1136");



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);
                Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                // try parse the string to a JSON object
                try {
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONObject("res");


                imagem = dataJsonArr.getString("imagem");



                //Customize your icon here
                //icon.setImageResource(R.drawable.sem_foto);

                Log.v("IMAGEM","objecto especial = "+ imagem);



               // Log.v("IMAGEM","objecto especial = "+ imagem);

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
    public void asyncComplete(boolean success){
        // loop through all users
        ImageView icon=(ImageView) getActivity().findViewById(R.id.imageView);
        imageLoader.DisplayImage("http://menuguru.pt"+imagem, icon);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_localizacao, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_localizacao:
                Intent myIntent = new Intent(getActivity(), Localizacao.class);
                getActivity().startActivity(myIntent);
                this.getActivity().finish();
                return false;

            default:
                break;
        }

        return false;
    }
}
