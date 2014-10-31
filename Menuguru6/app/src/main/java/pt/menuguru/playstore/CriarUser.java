package pt.menuguru.playstore;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.menuguru.playstore.Json_parser.JSONParser;
import pt.menuguru.menuguru.R;


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

    String resp;
    String msg;

    private ProgressDialog progressDialog;

    private Calendar cal;


    @Override
    public void onStart()
    {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
        setContentView(R.layout.activity_criar_user);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_pnome  = (EditText)findViewById(R.id.edit_criar_pnome);
        //edit_snome  = (EditText)findViewById(R.id.edit_criar_snome);
        bt_sexo  = (Button)findViewById(R.id.edit_criar_sexo);
        //edit_cidade  = (EditText)findViewById(R.id.edit_criar_cidade);
        //bt_data_nasc  = (Button)findViewById(R.id.edit_criar_data_nasc);
        edit_email  = (EditText)findViewById(R.id.edit_criar_email);
        edit_pass  = (EditText)findViewById(R.id.edit_criar_pass);
        bt_registar  = (Button)findViewById(R.id.bt_c_registo);

        /*bt_data_nasc.setOnClickListener(new View.OnClickListener() {
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
        });*/
        bt_registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_pnome = edit_pnome.getText().toString().trim();
                //g_snome = edit_snome.getText().toString().trim();
                //g_sexo =  bt_sexo.getText().toString().trim();
               // g_data_nasc = bt_data_nasc.getText().toString().trim();
                //g_cidade = edit_cidade.getText().toString().trim();
                g_email = edit_email.getText().toString().trim();
                g_pass = edit_pass.getText().toString().trim();
                if(g_pnome.isEmpty()||g_email.isEmpty()||g_pass.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CriarUser.this);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.prencher_campos)
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();

                }else if(!isEmailValid(g_email)){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CriarUser.this);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.erro_email)
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
                }else if(g_pass.length()<6){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CriarUser.this);
                    // set dialog message
                    alertDialogBuilder
                            .setMessage(R.string.min_pass)
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
                }else {
                    new AsyncTaskParseJson(CriarUser.this).execute();
                }
            }
        });
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_login.php";

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

                //dict.put("genero", g_sexo);
                dict.put("email", g_email);
                //dict.put("cidade", g_cidade);
                //dict.put("data_nasc", g_data_nasc);
                dict.put("password", g_pass);
                dict.put("primnome", g_pnome);
                //dict.put("segnome", g_snome);



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                resp = jsonObj.getString("resp");
                msg = jsonObj.getString("msg");
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
        if(resp.equals("INSUCESSO")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setTitle(R.string.tit_email)
                    .setMessage(R.string.desc_erro_mail)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.conf_email)
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        }
    }
}
