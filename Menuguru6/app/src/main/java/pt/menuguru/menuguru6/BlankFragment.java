package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Globals;


public class BlankFragment extends Fragment {

    private ProgressDialog progressDialog;

    Button bt_teste;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTaskParseJson(this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        Button button = (Button) view.findViewById(R.id.bt_novo);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ComoFunciona.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }
        });


        return view;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_comofunciona.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private BlankFragment delegate;

        public AsyncTaskParseJson (BlankFragment delegate){
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

                dict.put("lang", Globals.get_instance().getLingua());
                dict.put("tamanho", "640x1136");
                dict.put("versao", "");



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                ComoFunc cfunc = new ComoFunc();
                dataJsonArr = jsonObj.getJSONArray("res");
                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);




                    cfunc.setId(c.getString("id"));
                    cfunc.setId(c.getString("img1"));
                    cfunc.setId(c.getString("img2"));
                    Log.v("TAMANHO ARRAY",cfunc.getId());



                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("id"));
                    Log.v("IMG1","objecto = "+ c.getString("img1"));
                    Log.v("IMG2","objecto = "+ c.getString("img2"));


                }

                Globals.getInstance().setCfunc(cfunc);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){

    }


}
