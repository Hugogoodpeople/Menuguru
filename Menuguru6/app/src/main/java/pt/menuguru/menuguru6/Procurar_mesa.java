package pt.menuguru.menuguru6;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new AsyncTaskParseJson(this).execute();
        // Inflate the layout for this fragment
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
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Procurar_mesa delegate){
            this.delegate = delegate;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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

                dataJsonArr = jsonObj.getJSONArray("res");

                JSONObject c = dataJsonArr.getJSONObject(0);
                String imagem = c.getString("imagem");

                // loop through all users
                ImageView icon=(ImageView) getActivity().findViewById(R.id.imageView);

                //Customize your icon here
                //icon.setImageResource(R.drawable.sem_foto);


                imageLoader.DisplayImage("http://menuguru.pt/"+imagem, icon);


                Log.v("IMAGEM","objecto especial = "+ imagem);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();delegate.asyncComplete(true);
        }
    }
    public void asyncComplete(boolean success){



    }
}
