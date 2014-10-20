package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Reserva;

public class MinhasReservas extends Fragment {

    Reserva[] some_array = null;

    private ProgressDialog progressDialog;

    private AbsListView mListView;

    private static MyListAdapter mAdapter;

    ImageView imagem;

    CharSequence[] items = new String[2];

    String dia;
    String mes;
    Calendar cal = Calendar.getInstance();

    int posicao_apagar = 0;


    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.reservas);
        items =null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(Globals.getInstance().getUser()!=null) {
            new AsyncTaskParseJson(this).execute();

            view = inflater.inflate(R.layout.fragment_reservaslist, container, false);
            mListView = (AbsListView) view.findViewById(R.id.list_reservas);
            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

            // para colocar quando esta vazia
            mListView.setEmptyView(view.findViewById(R.id.emty_view_reserva));

            imagem = (ImageView) view.findViewById(R.id.imageView_sem_r);
            if(Globals.getInstance().getLingua().equalsIgnoreCase("pt")){
                imagem.setImageResource(R.drawable.reserva_pt);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("en")){
                imagem.setImageResource(R.drawable.reserva_en);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("fr")){
                imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("it")){
                imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("de")){
                imagem.setImageResource(R.drawable.reserva_de);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("es")){
                imagem.setImageResource(R.drawable.reserva_es);
            }

        }else{
            view = inflater.inflate(R.layout.fragment_sem_reservas, container, false);
            imagem = (ImageView) view.findViewById(R.id.imageView_sem);
            if(Globals.getInstance().getLingua().equalsIgnoreCase("pt")){
                imagem.setImageResource(R.drawable.reserva_pt);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("en")){
                imagem.setImageResource(R.drawable.reserva_en);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("fr")){
                imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("it")){
                imagem.setImageResource(R.drawable.reserva_fr);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("de")){
                imagem.setImageResource(R.drawable.reserva_de);
            }else if(Globals.getInstance().getLingua().equalsIgnoreCase("es")){
                imagem.setImageResource(R.drawable.reserva_es);
            }
        }

        return view;
    }

    public class MyListAdapter extends ArrayAdapter<Reserva> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId,
                             Reserva[] objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getActivity().getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = null;

            row=inflater.inflate(R.layout.fragment_reservas, parent, false);
            TextView label = (TextView) row.findViewById(R.id.nomeRestaurante);
            label.setText(some_array[position].getNome_ret());


            TextView label2 = (TextView) row.findViewById(R.id.nomeMenu);
            if (some_array[position].getTipo().equals("normal")) {
                label2.setText("Reserva de mesa");
            } else {
                label2.setText(some_array[position].getNome_menu());
            }

            TextView label3 = (TextView) row.findViewById(R.id.desconto);

            Date data = ConvertToDate(some_array[position].getData_rm());
            cal.setTime(data);

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            SimpleDateFormat mesFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

            dia = dayFormat.format(data);
            mes = mesFormat.format(data);

            String texto = dia + " " + cal.get(Calendar.DAY_OF_MONTH) + " " + mes + " " + cal.get(Calendar.YEAR) + " " + some_array[position].getHora_rm() + ", " + some_array[position].getN_pesssoas_rm() + " pax";
            label3.setText(texto);

            ImageView imagem = (ImageView) row.findViewById(R.id.capa);
            ImageView icon = (ImageView) row.findViewById(R.id.imagemTipo);

            imageLoader.DisplayImage("http://menuguru.pt/" + some_array[position].getImagem_rest(), imagem);



            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    Date data2 = ConvertToDate2(some_array[position].getData_rm() + " " + some_array[position].getHora_rm());
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                    Date data_act = ConvertToDate2(date);
                    if(data2.before(data_act)) {
                        alertDialogBuilder
                                .setItems(R.array.apagar_reserva, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0: {
                                                posicao_apagar = position;
                                                Log.v("Posicao",""+posicao_apagar);
                                                if(some_array[position].getTipo().equals("especial")){
                                                    new AsyncTaskParseJsonCancelarEsp(MinhasReservas.this).execute();
                                                }else{
                                                    new AsyncTaskParseJsonCancelar(MinhasReservas.this).execute();
                                                }
                                                break;
                                            }
                                            case 1: {
                                                Intent nav = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + some_array[position].getLat() + " , " + some_array[position].getLon() + ""));
                                                startActivity(nav);
                                                break;
                                            }
                                            case 2:
                                            default:
                                                break;
                                        }
                                    }
                                });
                    }else{
                        alertDialogBuilder
                                .setItems(R.array.cancelar_reserva, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0: {
                                                posicao_apagar = position;
                                                Log.v("Posicao",""+posicao_apagar);
                                                 if(some_array[position].getTipo().equals("especial")){
                                                        new AsyncTaskParseJsonCancelarEsp(MinhasReservas.this).execute();
                                                 }else{
                                                        new AsyncTaskParseJsonCancelar(MinhasReservas.this).execute();
                                                 }
                                                break;
                                            }
                                            case 1: {
                                                Intent nav = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + some_array[position].getLat() + " , " + some_array[position].getLon() + ""));
                                                startActivity(nav);
                                                break;
                                            }
                                            case 2:
                                            default:
                                                break;
                                        }
                                    }
                                });
                    }
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            });

            return row;
        }

    }
    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    private Date ConvertToDate2(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }





    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_reservanormal_especial.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private MinhasReservas delegate;

        public AsyncTaskParseJson (MinhasReservas delegate){
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

                String tipo_conta = Globals.get_instance().getUser().getTipoconta();
                if(tipo_conta.equals("facebook")){
                    dict.put("face_id", Globals.get_instance().getUser().getId_face());
                    dict.put("user_id", "0");
                    dict.put("lang",Globals.get_instance().getLingua());
                }else if(tipo_conta.equals("guru")){
                    dict.put("face_id", "0");
                    dict.put("user_id", Globals.get_instance().getUser().getUserid());
                    dict.put("lang",Globals.get_instance().getLingua());
                }

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                dataJsonArr = jsonObj.getJSONArray("res");
                some_array = new Reserva[dataJsonArr.length()];


                for (int i = 0; i < dataJsonArr.length() ; i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);


                    Reserva reserva = new Reserva();

                    reserva.setData_rm(c.getString("data_rm"));
                    reserva.setTipo(c.getString("tipo"));
                    reserva.setId_rest(c.getString("id_rest"));
                    reserva.setId_mesa(c.getString("id_mesa"));
                    reserva.setId_hmesa(c.getString("id_hmesa"));
                    reserva.setHora_rm(c.getString("hora_rm"));
                    reserva.setTelefone_rm(c.getString("telefone_rm"));
                    reserva.setN_pesssoas_rm(c.getString("n_pessoas_rm"));
                    reserva.setNome_menu(c.getString("nome_menu"));
                    reserva.setNum_pes_disponivel(c.getString("num_pes_disponiveis"));
                    reserva.setNome_ret(c.getString("nome_rest"));
                    reserva.setLat(c.getString("lat"));
                    reserva.setLon(c.getString("lon"));
                    reserva.setImagem_rest(c.getString("imagem_rest"));


                    // Storing each json item in variable
                    Log.v("ID","objecto = "+ c.getString("data_rm"));
                    Log.v("IMG1","objecto = "+ c.getString("tipo"));
                    Log.v("IMG2","objecto = "+ c.getString("id_rest"));

                    some_array[i] = reserva;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){ progressDialog.dismiss();
            if(delegate != null)
            delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){
        mAdapter = new MyListAdapter(getActivity(), R.layout.fragment_reservaslist, some_array);
        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonCancelar extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/teste_mesa_reserva_apagar.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private MinhasReservas delegate;

        public AsyncTaskParseJsonCancelar (MinhasReservas delegate){
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

                String tipo_conta = Globals.get_instance().getUser().getTipoconta();
                if(tipo_conta.equals("facebook")){
                    dict.put("face_id", Globals.get_instance().getUser().getId_face());
                    dict.put("user_id", "0");
                    dict.put("lang",Globals.get_instance().getLingua());
                }else if(tipo_conta.equals("guru")){
                    dict.put("face_id", "0");
                    dict.put("user_id", Globals.get_instance().getUser().getUserid());
                    dict.put("lang",Globals.get_instance().getLingua());
                }

                // para as opçoes adicionais
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> listOpc = new ArrayList<String>();
                listOpc.add(some_array[posicao_apagar].getId_mesa());

                dict.put("apagar", new JSONArray(listOpc.toString()));

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }

                String completo = jsonObj.getString("envio");
                JSONObject outro =new JSONObject(completo);




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){ progressDialog.dismiss();delegate.asyncCompleteCancelar(true);  }

    }


    public void asyncCompleteCancelar(boolean success){
        new AsyncTaskParseJson(this).execute();
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonCancelarEsp extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_apagar_especial.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private MinhasReservas delegate;

        public AsyncTaskParseJsonCancelarEsp (MinhasReservas delegate){
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

                String tipo_conta = Globals.get_instance().getUser().getTipoconta();
                if(tipo_conta.equals("facebook")){
                    dict.put("face_id", Globals.get_instance().getUser().getId_face());
                    dict.put("user_id", "0");
                    dict.put("lang",Globals.get_instance().getLingua());
                }else if(tipo_conta.equals("guru")){
                    dict.put("face_id", "0");
                    dict.put("user_id", Globals.get_instance().getUser().getUserid());
                    dict.put("lang",Globals.get_instance().getLingua());
                }

                // para as opçoes adicionais
                // agr os arrays, vou tentar usar arraylists para ser mais facil fazer o que quero
                ArrayList<String> listOpc = new ArrayList<String>();
                listOpc.add(some_array[posicao_apagar].getId_mesa());

                dict.put("apagar", new JSONArray(listOpc.toString()));

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }

                String completo = jsonObj.getString("envio");
                JSONObject outro =new JSONObject(completo);




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){ progressDialog.dismiss();delegate.asyncCompleteCancelarEsp(true);  }

    }


    public void asyncCompleteCancelarEsp(boolean success){
        new AsyncTaskParseJson(this).execute();
    }
}
