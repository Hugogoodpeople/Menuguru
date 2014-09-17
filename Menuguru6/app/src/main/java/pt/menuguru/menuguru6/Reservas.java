package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
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
import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Globals;


public class Reservas extends Fragment {
    View view;

    ImageView imagem;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(Globals.getInstance().getUser()!=null) {
            Log.v("ID",Globals.get_instance().getUser().getUserid());
            Log.v("ID_FACE",Globals.get_instance().getUser().getId_face());
            new AsyncTaskParseJson(this).execute();

            view = inflater.inflate(R.layout.fragment_reservas, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_sem_reservas, container, false);
            imagem = (ImageView) view.findViewById(R.id.imageView_sem);
                  if(Globals.getInstance().getLingua()=="pt"){
                      imagem.setImageResource(R.drawable.reserva_pt);
            }else if(Globals.getInstance().getLingua()=="en"){
                      imagem.setImageResource(R.drawable.reserva_en);
            }else if(Globals.getInstance().getLingua()=="fr"){
                      imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua()=="it"){
                      imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua()=="de"){
                      imagem.setImageResource(R.drawable.reserva_de);
            }else if(Globals.getInstance().getLingua()=="es"){
                      imagem.setImageResource(R.drawable.reserva_es);
            }
        }
        getActivity().getActionBar().setTitle(R.string.reservas);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_listar_reservas.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Reservas delegate;

        public AsyncTaskParseJson (Reservas delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                if(Globals.getInstance().getUser().getId_face()!="0"){
                    dict.put("face_id", Globals.get_instance().getUser().getId_face());
                    dict.put("user_id", "0");
                    Log.v("Entrou"," FACE");
                }else{
                    dict.put("face_id", "0");
                    dict.put("user_id", Globals.get_instance().getUser().getUserid());
                    Log.v("Entrou"," NORMAL");
                }

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                //dataJsonArr = jsonObj.getJSONArray("res");
               /* como_array = new ComoFunc[dataJsonArr.length()];


                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    ComoFunc cfunc = new ComoFunc();

                    cfunc.setId(c.getString("id"));
                    cfunc.setImg1(c.getString("img1"));
                    cfunc.setImg2(c.getString("img2"));


                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("id"));
                    Log.v("IMG1","objecto = "+ c.getString("img1"));
                    Log.v("IMG2","objecto = "+ c.getString("img2"));

                    como_array[i] = cfunc;
                }

                Globals.getInstance().setCfunc(como_array);

*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){ delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){

    }


}
