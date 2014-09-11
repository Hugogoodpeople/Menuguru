package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;


public class CriarUser extends Activity {

    EditText edit_pnome;
    EditText edit_snome;
    Button bt_sexo;
    EditText edit_cidade;
    Button bt_data_nasc;
    Button bt_registar;
    EditText edit_email;
    EditText edit_pass;

    String g_pnome;
    String g_snome;
    String g_sexo;
    String g_cidade;
    String g_data_nasc;
    String g_email;
    String g_pass;

    private ProgressDialog progressDialog;

    private Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_user);

        edit_pnome  = (EditText)findViewById(R.id.edit_pnome);
        edit_snome  = (EditText)findViewById(R.id.edit_snome);
        bt_sexo  = (Button)findViewById(R.id.edit_sexo);
        edit_cidade  = (EditText)findViewById(R.id.edit_cidade);
        bt_data_nasc  = (Button)findViewById(R.id.edit_data_nasc);
        edit_email  = (EditText)findViewById(R.id.edit_email);
        edit_pass  = (EditText)findViewById(R.id.edit_pass);

        bt_data_nasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tenho de adicionar cenas
                Log.v("coiso", "Procurar");

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(CriarUser.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();

            }
        });
        bt_sexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("coiso", "Sexo");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CriarUser.this);
                // set dialog message
                alertDialogBuilder
                        .setTitle("Sexo")
                        .setItems(R.array.sexo, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {
                                    bt_sexo.setText("Homem");
                                }else if(which==1){
                                    bt_sexo.setText("Mulher");
                                }
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
        bt_registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_pnome = edit_pnome.getText().toString().trim();
                g_snome = edit_snome.getText().toString().trim();
                g_sexo =  bt_sexo.getText().toString().trim();
                g_data_nasc = bt_data_nasc.getText().toString().trim();
                g_cidade = edit_cidade.getText().toString().trim();
                g_email = edit_email.getText().toString().trim();
                g_pass = edit_pass.getText().toString().trim();
                new AsyncTaskParseJson(CriarUser.this).execute();
            }
        });
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

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(cal.getTime());

            bt_data_nasc.setText(formattedDate);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.criar_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_editar_conta_android.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private CriarUser delegate;

        public AsyncTaskParseJson (CriarUser delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CriarUser.this);
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

                dict.put("id", Globals.get_instance().getUser().getUserid());


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String resp = jsonObj.getString("resp");

                Log.v("resp","objecto = "+ jsonObj.getString("resp"));
                Log.v("msg","objecto = "+ jsonObj.getString("msg"));
                Log.v("titulo","objecto = "+ jsonObj.getString("titulo"));




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }


    public void asyncComplete(boolean success){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.guardar_perfil)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
