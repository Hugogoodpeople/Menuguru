package pt.menuguru.menuguru6;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.ResultadosProcurarMesa.Resultados;
import pt.menuguru.menuguru6.Utils.CustomTimePickerDialog;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.ImageLoader;
import pt.menuguru.menuguru6.Utils.Restaurante;


public class Procurar_mesa extends Fragment {

    private ProgressDialog progressDialog;
    private int atual = 1;
    private TextView pessoasQueVao;
    private Button bt_data;
    private Button bt_hora;
    private Calendar cal;
    private CustomTimePickerDialog mTimePicker;

    public ImageLoader imageLoader;
    String imagem = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void actualiza(int valor)
    {

        if (valor>0) {
            atual = valor;
            pessoasQueVao.setText(Integer.toString(valor));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new AsyncTaskParseJson(this).execute();
        // Inflate the layout for this fragment
        imageLoader=new ImageLoader(getActivity().getApplicationContext());


        RelativeLayout mLinearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_procurar_mesa,
                container, false);

        // note that we're looking for a button with id="@+id/myButton" in your inflated layout
        // Naturally, this can be any View; it doesn't have to be a button
        Button mButton = (Button) mLinearLayout.findViewById(R.id.button8);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
                Log.v("clickclick","clicou pesquisa");



            }
        });


        pessoasQueVao = (TextView) mLinearLayout.findViewById(R.id.numero_pessoas);

        bt_data = (Button) mLinearLayout.findViewById(R.id.button_data);
        bt_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("coiso", "Procurar");

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();

            }
        });
        bt_hora = (Button) mLinearLayout.findViewById(R.id.button_hora);
        bt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("cenas","clicou timer");

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                mTimePicker = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // ainda falta qui codigo para fazer o resto das cenas
                        // tenho de ter a validação se é 0 ou 30

                        if (selectedMinute == 0)
                            bt_hora.setText(Integer.toString(selectedHour) + ":00");
                        else
                            bt_hora.setText(Integer.toString(selectedHour) + ":30");


                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        Button bt_agora = (Button) mLinearLayout.findViewById(R.id.button3);
        bt_agora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                data_agora();
            }
        });

        Button bt_mais = (Button) mLinearLayout.findViewById(R.id.mais_pessoas);
        bt_mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                actualiza(atual + 1);
            }
        });

        Button bt_menos = (Button) mLinearLayout.findViewById(R.id.pessoas_menos);
        bt_menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                actualiza(atual - 1);
            }
        });

        //Button bt_sem_data = (Button) getActivity().findViewById(R.id.bt_sem_data);
        Button bt_procurar = (Button) mLinearLayout.findViewById(R.id.button8);
        bt_procurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("coiso", "Procurar");

                // tenho de lançar uma nova actividade aqui
                Intent intent = new Intent(getActivity(), Resultados.class);

                intent.putExtra("pessoas",pessoasQueVao.getText());


                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(cal.getTime());


                intent.putExtra("data",formattedDate);
                intent.putExtra("hora",bt_hora.getText());
                //intent.putExtra("");

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

            }
        });

        data_agora();

        return mLinearLayout;
    }


    public void data_agora()
    {
        cal = Calendar.getInstance();
        System.out.println("Current time => " + cal.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        String formattedDate = df.format(cal.getTime());

        bt_data.setText(formattedDate);


        Time dtNow = new Time();
        dtNow.setToNow();
        int hours = dtNow.hour;
        int minut = dtNow.minute;


        String horaActual;
        // tenho de fazer aqui a verificação para ser de 30 em 30 min de cada vez
        if (minut >= 30)
        {
            hours = hours +1;
            minut = 00;
            horaActual = hours + ":00";
        }
        else
        {
            minut = 30;
            horaActual = hours + ":"+ minut;
        }

        String lsNow = dtNow.format("%H:%M");
        String lsYMD = dtNow.toString();    // YYYYMMDDTHHMMSS


        bt_hora.setText(horaActual);

    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            String dataSelecionada= "Ano " + Integer.toString(mYear) +
                                    " Mes " + Integer.toString(mMonth+1) +
                                    " Dia " + Integer.toString(mDay);

            Log.v("selecionado",dataSelecionada);

            cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, mYear);
            cal.set(Calendar.MONTH, mMonth);
            cal.set(Calendar.DAY_OF_MONTH, mDay);

            SimpleDateFormat df = new SimpleDateFormat("dd MMM");
            String formattedDate = df.format(cal.getTime());

            bt_data.setText(formattedDate);


        }
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
