package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.MenuEspecial;


public class Reservas extends Fragment {
    View view;

    ImageView imagem;

    MenuEspecial[] some_array = null;

    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private static MyListAdapterReservas mAdapter;
    private ProgressDialog progressDialog;

    public class MyListAdapterReservas extends ArrayAdapter<MenuEspecial> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapterReservas(Context context, int textViewResourceId,
                                      MenuEspecial[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getActivity().getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.fragment_especiais, parent, false);
            TextView label=(TextView)row.findViewById(R.id.nomeRestaurante);
            label.setText(some_array[position].restaurante.getNome());


            TextView label2 = (TextView)row.findViewById(R.id.nomeMenu);
            label2.setText(some_array[position].getNome());



            ImageView imagem=(ImageView)row.findViewById(R.id.capa);
            ImageView icon = (ImageView)row.findViewById(R.id.imagemTipo);

            //Customize your icon here
            //icon.setImageResource(R.drawable.sem_foto);

            if(some_array[position].tipo.equalsIgnoreCase("especial_doisprecos"))
            {
                icon.setImageResource(R.drawable.antes_depois);
                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText(some_array[position].precoAntigo+"€");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].precoNovo+"€");
                TextView label5=(TextView)row.findViewById(R.id.desconto);

                Float  preco1 = Float.parseFloat( some_array[position].precoAntigo);
                Float  preco2 =  Float.parseFloat( some_array[position].precoNovo);

                Float percentagem = (((preco2 / preco1) * 100) - 100) * -1;

                label5.setText("Desconto "+ String.format("%.0f", percentagem) + "%");

            }
            else if(some_array[position].tipo.equalsIgnoreCase("especial_desconto"))
            {
                icon.setImageResource(R.drawable.desc_fatura);

                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText("");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].desconto+"% off");

                TextView label5=(TextView)row.findViewById(R.id.desconto);
                label5.setText("Desconto em factura ");
            }
            else
            {
                icon.setImageResource(R.drawable.menu_esp);
                TextView label5=(TextView)row.findViewById(R.id.desconto);
                label5.setText(some_array[position].especialFita);
                TextView label3=(TextView)row.findViewById(R.id.precoAntigo);
                label3.setPaintFlags(label3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                label3.setText("");
                TextView label4=(TextView)row.findViewById(R.id.precoActual);
                label4.setText(some_array[position].precoNovo+"€");
            }

            imageLoader.DisplayImage("http://menuguru.pt/"+some_array[position].getUrlImage(), imagem);

            return row;
        }

    }


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
