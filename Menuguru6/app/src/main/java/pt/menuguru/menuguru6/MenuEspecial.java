package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Restaurante.Restaurante_main;
import pt.menuguru.menuguru6.Utils.CustomTimePickerDialog;
import pt.menuguru.menuguru6.Utils.Descricao_Especial;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.Horario_Especial;
import pt.menuguru.menuguru6.Utils.Menu_Especial;
import pt.menuguru.menuguru6.Utils.Nr_Pessoas_Especial;
import pt.menuguru.menuguru6.gif.decoder.GifRun;

import static android.R.layout.simple_list_item_1;


public class MenuEspecial extends Activity {

    private static MyListAdapter mAdapter;
    private ArrayList<Menu_Especial> some_list;
    private ArrayList<Descricao_Especial> desc_list;
    private ArrayList<Horario_Especial> hora_list;
    private ArrayList<Horario_Especial> aux_hora_list;
    private ArrayList<Nr_Pessoas_Especial> nr_pes_list;
    private ArrayList<Nr_Pessoas_Especial> aux_nr_pes_list;

    private ListView mListView;

    private ProgressDialog progressDialog;

    private String rest_id;
    private String rest_cartao_id;

    public String lat;
    public String lng;
    public String nome_rest;
    public String imagem_rest;
    public String morada;
    public String rating;
    public String votacoes;
    public String hora_min_reserva;

    public Button bt_reserva;
    public Button bt_voucher;

    CalendarView calendar;

    String sel_id_hora = "";
    String sel_hora = "";
    String sel_nr_pes = "";
    String sel_obs = "";
    String sel_nome = "";
    String sel_telefone = "";
    String sel_email = "" ;
    String sel_dia_semana = "" ;


    TextView edt;
    TextView edt1;
    TextView edt2;
    TextView edt3;
    TextView edt4;
    TextView edt5;
    TextView edt6;
    TextView edt7;
    TextView edt8;
    ImageView edt9;
    Menu menu;

    String data_selec;


    Dialog dialog_loading;
    Dialog dialog_conf;
    Dialog dialog_obs;
    Dialog dialog_pes;
    Dialog dialog_hora;
    Dialog dialog1;

    String res_reserva;
    String msg_reserva;

    String num_por_dia;
    String num_por_hora;

    String reportDate;

    public MenuEspecial() {
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

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
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_menu_especial);

        rest_id = getIntent().getExtras().getString("rest_id");
        rest_cartao_id = getIntent().getExtras().getString("rest_cartao_id");
        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lon");
        nome_rest = getIntent().getExtras().getString("nome_rest");
        imagem_rest = getIntent().getExtras().getString("urlfoto");
        morada = getIntent().getExtras().getString("morada");
        hora_min_reserva = getIntent().getExtras().getString("hora_min_reserva");
//        Log.v("HORA MIN RESERVA",hora_min_reserva);
        // Set the adapter
        mListView = (ListView)findViewById(R.id.list_esp);

        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        //Log.v("RESERVA FEITA",some_list.get(0).getReserva_feita());
        new AsyncTaskParseJson(this).execute();
        bt_reserva = (Button)findViewById(R.id.button_reservar);
        bt_voucher = (Button)findViewById(R.id.bt_voucher);

        bt_reserva.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog1 = new Dialog(MenuEspecial.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_reserva);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                data_selec = df.format(c.getTime());

                Date data_act = null;
                try {
                    data_act = df.parse(data_selec);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long data_A = data_act.getTime();
                calendar = (CalendarView)dialog1.findViewById(R.id.calendarView2);
                calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.dourado_trans));
                calendar.setFocusedMonthDateColor(getResources().getColor(R.color.black));
                calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.silver));
                calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
                calendar.setSelectedDateVerticalBar(R.color.dourado);
                calendar.setDate(data_A);
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month,
                                                    int dayOfMonth) {
                        month = month+1;
                        Log.v("ANO",""+year);

                        String auxmonth = ""+month;
                        if(month<10){
                            auxmonth = String.format("%02d", month);
                        }
                        Log.v("MES",""+auxmonth);

                        String auxdayOfMonth = ""+dayOfMonth;
                        if(dayOfMonth<10){
                            auxdayOfMonth = String.format("%02d", dayOfMonth);
                        }
                        Log.v("DIA",""+auxdayOfMonth);
                        data_selec = year+"-"+auxmonth+"-"+auxdayOfMonth;
                    }
                });

                TextView bt_cancelar = (TextView) dialog1.findViewById(R.id.bt_cancelar);
                TextView bt_segasd = (TextView) dialog1.findViewById(R.id.bt_seguuintw);

                bt_segasd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Calendar cc = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dataactual = sdf.format(cc.getTime());
                        Date date1=null, date2=null, date3=null;
                        try {
                            date1 = sdf.parse(data_selec);
                            date2 = sdf.parse(dataactual);
                            date3 = sdf.parse(some_list.get(0).getDatafinal());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.v("data 1",""+date1);
                        Log.v("data 2",""+date2);

                        if(date1.compareTo(date2)>=0){
                            if(date1.compareTo(date3)<=0){
                                cc.setTime(date1);
                                int day = cc.get(Calendar.DAY_OF_WEEK);
                                int dia_semana = 0;
                                switch (day){
                                    case 1:dia_semana=7;break;
                                    case 2:dia_semana=1;break;
                                    case 3:dia_semana=2;break;
                                    case 4:dia_semana=3;break;
                                    case 5:dia_semana=4;break;
                                    case 6:dia_semana=5;break;
                                    case 7:dia_semana=6;break;
                                }

                                aux_hora_list = new ArrayList<Horario_Especial>();
                                for (int i = 0; i < hora_list.size(); i++) {
                                    Horario_Especial hora = new Horario_Especial();
                                    int foo = Integer.parseInt(hora_list.get(i).getDia_id());

                                    if(dia_semana==foo){

                                        Calendar ccc = Calendar.getInstance();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd k:m:s");
                                        String test2 = data_selec + " "+ hora_list.get(i).getHora_inicio()+":00";
                                        String hora_actual = format.format(ccc.getTime());

                                        Date teste2 = null, teste1 = null;
                                        try {
                                            teste1 = format.parse(hora_actual);
                                            teste2 = format.parse(test2);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Log.v("TESTE 1",""+teste1);
                                        Log.v("TESTE 2",""+teste2);
                                        long aaa = getDateDiff(teste1, teste2, TimeUnit.HOURS);
                                        Log.v("DIFERENCA "+i ,""+aaa);

                                        if(Long.parseLong(hora_min_reserva)<=aaa){
                                            hora.setId(hora_list.get(i).getId());
                                            hora.setDia_id(hora_list.get(i).getDia_id());
                                            hora.setHora_inicio(hora_list.get(i).getHora_inicio());
                                            hora.setN_pessoas_h(hora_list.get(i).getN_pessoas_h());
                                            aux_hora_list.add(hora);
                                        }
                                    }
                                }

                                new AsyncTaskParseJsonNrpessoas(MenuEspecial.this).execute();
                                SelecionaHora();
                            }else{
                                AvisoData_dep();
                            }
                        }else if(date1.compareTo(date2)<0){
                            AvisoData_ant();
                        }



                    }
                });

                bt_cancelar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });



                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setLenient(false);
                SimpleDateFormat formatte = new SimpleDateFormat("yyyy-MM-dd");
                formatte.setLenient(false);
                String oldTime = some_list.get(0).getDatafinal();
                Date oldDate = null;
                try {
                    oldDate = formatter.parse(oldTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long oldMillis = oldDate.getTime();

                String nTime = some_list.get(0).getDataActual();
                Date nDate = null;
                try {
                    nDate = formatte.parse(nTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long nMillis = nDate.getTime();


                //calendar.setMaxDate(oldMillis);
                //calendar.setMinDate(nMillis);

                dialog1.show();


            }
        });


        bt_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux_pes;
                if(sel_nr_pes.equals("1")){
                    aux_pes = getString(R.string.pessoa);
                }else{
                    aux_pes = getString(R.string.pessoas);
                }
                Intent intent = new Intent(MenuEspecial.this, Voucher.class);
                intent.putExtra("data", reportDate);
                intent.putExtra("hora", sel_hora+" "+sel_nr_pes+" "+aux_pes);
                intent.putExtra("id_rest", rest_id);
                intent.putExtra("id_especial", rest_cartao_id);
                intent.putExtra("nome_rest", nome_rest);
                intent.putExtra("id_pai", some_list.get(0).getId_pai());
                intent.putExtra("desc", some_list.get(0).getDescricao());
                startActivity(intent);
                overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }
        });

    }



    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public void AvisoData_ant(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.erro_data_ant)
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
    }

    public void AvisoData_dep(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.erro_data_dep)
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
    }

    public void AvisoHora(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.erro_hora)
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
    }

    public void AvisoPessoas(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.erro_pes)
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
    }

    public void AvisoNome(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.inserir_nome)
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
    }

    public void AvisoEmail(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.inserir_email)
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
    }

    public void AvisoTelefone(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuEspecial.this);
        // set dialog message
        alertDialogBuilder
                .setMessage(R.string.inserir_telefone)
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
    }

    public void Loading(){
        dialog_loading = new Dialog(MenuEspecial.this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setContentView(R.layout.dialog_loading);
        TextView tx_data = (TextView) dialog_loading.findViewById(R.id.tv_data);
        TextView tx_hora= (TextView) dialog_loading.findViewById(R.id.tv_hora);
        TextView tx_nome = (TextView) dialog_loading.findViewById(R.id.tv_nome);
        TextView tx_tele = (TextView) dialog_loading.findViewById(R.id.tv_telefone);
        TextView tx_email = (TextView) dialog_loading.findViewById(R.id.tv_email);
        TextView tx_obs = (TextView) dialog_loading.findViewById(R.id.tv_obs);
        SurfaceView v = (SurfaceView) dialog_pes.findViewById(R.id.surfaceView_loading);
        GifRun w = new  GifRun();
        w.LoadGiff(v, this, R.drawable.loading2);
        tx_data.setText(reportDate);
        String aux_pes;
        if(sel_nr_pes.equals("1")){
            aux_pes = getString(R.string.pessoa);
        }else{
            aux_pes = getString(R.string.pessoas);
        }
        tx_hora.setText(sel_hora+" "+sel_nr_pes+" "+aux_pes);
        tx_nome.setText(sel_nome);
        tx_tele.setText(sel_telefone);
        tx_email.setText(sel_email);
        tx_obs.setText(sel_obs);


        dialog_loading.show();
    }


    public void SelecionaHora(){
        sel_hora = "";
        sel_dia_semana = "";
        sel_id_hora = "";
        //, R.style.PauseDialog2);
        dialog_hora = new Dialog(MenuEspecial.this);
        dialog_hora.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_hora.setContentView(R.layout.dialog_hora);

        TextView bt_ant = (TextView) dialog_hora.findViewById(R.id.bt_ant_cal);
        TextView bt_seg = (TextView) dialog_hora.findViewById(R.id.bt_seg_nrp);

        ListView list_hora = (ListView) dialog_hora.findViewById(R.id.list_hora);
        SpinnerAdapterVitor dataAdapter = new SpinnerAdapterVitor(MenuEspecial.this,
                simple_list_item_1,aux_hora_list);
        list_hora.setAdapter(dataAdapter);
        list_hora.setEmptyView(dialog_hora.findViewById(R.id.emty_view));

        // ListView Item Click Listener
        list_hora.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                }
                view.setBackgroundColor(getResources().getColor(R.color.dourado));
                sel_id_hora = aux_hora_list.get(position).getId();
                sel_hora = aux_hora_list.get(position).getHora_inicio();
                sel_dia_semana = aux_hora_list.get(position).getDia_id();
                num_por_hora = aux_hora_list.get(position).getN_pessoas_h();

            }

        });
        bt_ant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_hora.dismiss();
            }
        });

        bt_seg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!sel_hora.equals("")){
                    Log.v("NUM POR HORA",""+num_por_hora);
                    aux_nr_pes_list = new ArrayList<Nr_Pessoas_Especial>();
                    for (int i = 0; i < nr_pes_list.size(); i++) {
                        Nr_Pessoas_Especial nr_pes = new Nr_Pessoas_Especial();
                        if(Integer.parseInt(nr_pes_list.get(i).getNr())<=Integer.parseInt(num_por_dia) && Integer.parseInt(nr_pes_list.get(i).getNr())<=Integer.parseInt(num_por_hora)
                                && Integer.parseInt(nr_pes_list.get(i).getNr())<=Integer.parseInt(some_list.get(0).getNumero_vaucher())) {
                            nr_pes.setNr(nr_pes_list.get(i).getNr());
                            aux_nr_pes_list.add(nr_pes);
                        }
                    }

                    SelecionaNrPessoas();

                    Log.v("ID_HORA",sel_id_hora);
                    Log.v("DIA SEMANA",sel_dia_semana);
                }else {
                    AvisoHora();
                }
            }
        });
        dialog_hora.show();
    }

    public void SelecionaNrPessoas(){
        sel_nr_pes = "";
        //, R.style.PauseDialog2);
        dialog_pes = new Dialog(MenuEspecial.this);
        dialog_pes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pes.setContentView(R.layout.dialog_pessoas);

        TextView bt_ant_hora = (TextView) dialog_pes.findViewById(R.id.bt_ant_hora);
        TextView bt_seg_obs = (TextView) dialog_pes.findViewById(R.id.bt_seg_obs);

        ListView list_pes = (ListView) dialog_pes.findViewById(R.id.list_nr_pessoa);
        AdapterVitor dataAdapter = new AdapterVitor(MenuEspecial.this,
                simple_list_item_1,aux_nr_pes_list);
        list_pes.setAdapter(dataAdapter);
        list_pes.setEmptyView(dialog_pes.findViewById(R.id.emty_view));


        // ListView Item Click Listener
        list_pes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                }
                view.setBackgroundColor(getResources().getColor(R.color.dourado));
                sel_nr_pes = nr_pes_list.get(position).getNr();

            }

        });

        bt_ant_hora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_pes.dismiss();
            }
        });

        bt_seg_obs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!sel_nr_pes.equals("")){
                    SelecionaObs();
                }else {
                    AvisoPessoas();
                }
            }
        });
        dialog_pes.show();
    }

    public void SelecionaObs(){
        //, R.style.PauseDialog2);
        dialog_obs = new Dialog(MenuEspecial.this);
        dialog_obs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_obs.setContentView(R.layout.dialog_obs);

        TextView bt_ant_pes = (TextView) dialog_obs.findViewById(R.id.bt_ant_pes);
        TextView bt_seg_dados = (TextView) dialog_obs.findViewById(R.id.bt_seg_dados);
        bt_ant_pes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_obs.dismiss();
            }
        });

        bt_seg_dados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText text = (EditText) dialog_obs.findViewById(R.id.editText_obs);
                sel_obs = text.getText().toString();
                SelecionaConfDados();
            }
        });
        dialog_obs.show();
    }

    public void SelecionaConfDados() {
        //, R.style.PauseDialog2);
        dialog_conf = new Dialog(MenuEspecial.this);
        dialog_conf.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_conf.setContentView(R.layout.dialog_conf_dados);

        TextView bt_ant_obs = (TextView) dialog_conf.findViewById(R.id.bt_ant_obs);
        TextView bt_conf = (TextView) dialog_conf.findViewById(R.id.bt_conf);
        TextView textView2 = (TextView) dialog_conf.findViewById(R.id.textView_data);
        textView2.setText(sel_obs);
        final EditText edit_nome = (EditText) dialog_conf.findViewById(R.id.edit_nome);
        final EditText edit_telefone = (EditText) dialog_conf.findViewById(R.id.edit_telefone);
        final EditText edit_email = (EditText) dialog_conf.findViewById(R.id.edit_email);
        final TextView textView_data = (TextView) dialog_conf.findViewById(R.id.textView_data);
        final TextView textView_hora = (TextView) dialog_conf.findViewById(R.id.textView_hora);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(data_selec);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy");

        reportDate = df.format(date);
        textView_data.setText(""+reportDate);
        String aux_pes;
        if(sel_nr_pes.equals("1")){
            aux_pes = getString(R.string.pessoa);
        }else{
            aux_pes = getString(R.string.pessoas);
        }
        textView_hora.setText(sel_hora+" "+sel_nr_pes+" "+aux_pes);

        if(Globals.getInstance().getUser()!=null) {
            edit_nome.setText(Globals.get_instance().getUser().getPnome()+" "+Globals.get_instance().getUser().getSnome());
            edit_telefone.setText(Globals.get_instance().getUser().getTelefone_user());
            edit_email.setText(Globals.get_instance().getUser().getEmail());
        }


        bt_ant_obs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_conf.dismiss();
            }
        });

        bt_conf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(edit_nome.getText().toString().isEmpty()){
                    AvisoNome();
                }else if(edit_telefone.length()<9 || edit_telefone.length()>9){
                    AvisoTelefone();
                }else if(edit_email.getText().toString().isEmpty() || !isEmailValid(edit_email.getText().toString())){
                    AvisoEmail();
                }else{
                    if(Globals.get_instance().getUser() == null) {
                        Intent myIntent = new Intent(MenuEspecial.this, LoginMenuGuru.class);
                        startActivity(myIntent);
                    }
                    sel_nome =  edit_nome.getText().toString();
                    sel_telefone =  edit_telefone.getText().toString();
                    sel_email =  edit_email.getText().toString();
                    new AsyncTaskParseJsonReserva(MenuEspecial.this).execute();
                }
            }
        });
        dialog_conf.show();
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

    public class SpinnerAdapterVitor extends ArrayAdapter<Horario_Especial>
    {
        private ArrayList<Horario_Especial> hro;

        public SpinnerAdapterVitor(Context context, int resource, ArrayList<Horario_Especial> lista)
        {
            super(context, resource, lista);
            hro = lista;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override public View getView(int pos, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.list_spiner, parent, false);

            TextView tx_hora = (TextView) mySpinner.findViewById(R.id.textSpiner);
            tx_hora.setText(hro.get(position).getHora_inicio());
            return mySpinner;
        }
    }


    public class AdapterVitor extends ArrayAdapter<Nr_Pessoas_Especial>
    {
        private ArrayList<Nr_Pessoas_Especial> pes;

        public AdapterVitor(Context context, int resource, ArrayList<Nr_Pessoas_Especial> lista)
        {
            super(context, resource, lista);
            pes = lista;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override public View getView(int pos, View cnvtView, ViewGroup prnt)
        {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.list_spiner, parent, false);

            TextView tx_hora = (TextView) mySpinner.findViewById(R.id.textSpiner);
            tx_hora.setText(pes.get(position).getNr());
            return mySpinner;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_especial, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        mAdapter=null;
        mListView=null;

        //some_list = null ;
        //desc_list = null ;
        // hora_list = null ;
        //aux_hora_list = null ;
        // nr_pes_list = null ;
        // aux_nr_pes_list = null ;

        finish();
        overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                mAdapter=null;
                mListView=null;
                //some_list = null ;
                //desc_list = null ;
                //hora_list = null ;
                //aux_hora_list = null ;
                //nr_pes_list = null ;
                //aux_nr_pes_list = null ;
                finish();
                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            case R.id.partilhar:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                // aqui tenho de alterar a partilha
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://menuguru.pt/especial_"+ rest_cartao_id + "/"+ nome_rest);


                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.o_que_pretende_pesquisar)));
                return false;
            default:
                break;
        }

        return false;
    }

    public class MyListAdapter extends ArrayAdapter<Descricao_Especial> {

        Context myContext;


        public MyListAdapter(Context context, int textViewResourceId, ArrayList<Descricao_Especial> objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.row_especial, parent, false);
            getActionBar().setTitle(some_list.get(0).getNome());
            LinearLayout linear_ris = (LinearLayout)convertView.findViewById(R.id.linearLayout_risca);
            LinearLayout linear_tit = (LinearLayout)convertView.findViewById(R.id.linear_tit);



            TextView label = (TextView)convertView.findViewById(R.id.text_titulo);
            TextView text_preco_ant = (TextView)convertView.findViewById(R.id.text_preco_ant);
            TextView text_preco_act = (TextView)convertView.findViewById(R.id.text_preco);
            TextView text_nr_ofertas = (TextView)convertView.findViewById(R.id.text_rating_desc);
            TextView text_desc = (TextView)convertView.findViewById(R.id.text_desc);
            TextView text_desc2 = (TextView)convertView.findViewById(R.id.text_desc2);
            TextView text_oferta = (TextView)convertView.findViewById(R.id.text_oferta);

            String aux_tipo = some_list.get(0).getTipo();

            if(position==0){
                if(aux_tipo.equals("desconto")){
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_act.setText(some_list.get(position).getPreco_actual());
                    text_preco_ant.setPaintFlags(text_preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    text_preco_ant.setText(some_list.get(position).getPreco_ant());
                    text_nr_ofertas.setText(some_list.get(position).getReserva_feita());
                    text_desc.setText(desc_list.get(position).getDescricao());
                    text_desc2.setVisibility(View.GONE);

                }else if(aux_tipo.equals("off")){
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_ant.setPaintFlags(text_preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    text_preco_act.setText(some_list.get(position).getDesconto()+"%");
                    //text_preco_ant.setVisibility(View.GONE);
                    text_nr_ofertas.setText(some_list.get(position).getReserva_feita());
                    text_desc.setText(desc_list.get(position).getDescricao());
                    text_desc2.setVisibility(View.GONE);
                }else{
                    label.setText(some_list.get(position).getDescricao());
                    text_preco_act.setVisibility(View.GONE);
                    text_preco_ant.setVisibility(View.GONE);
                    text_nr_ofertas.setVisibility(View.GONE);
                    text_desc2.setVisibility(View.GONE);
                    text_desc.setVisibility(View.GONE);
                    linear_ris.setVisibility(View.GONE);
                    text_oferta.setVisibility(View.GONE);
                }
            }else if(position==1 && !aux_tipo.equals("desconto") && !aux_tipo.equals("off")) {
                text_preco_ant.setVisibility(View.GONE);
                text_preco_act.setVisibility(View.GONE);
                text_nr_ofertas.setVisibility(View.GONE);
                text_desc.setVisibility(View.GONE);
                linear_ris.setVisibility(View.GONE);
                text_oferta.setVisibility(View.GONE);
                linear_tit.setVisibility(View.GONE);
                text_desc2.setText(desc_list.get(position).getDescricao());

            }else{
                text_preco_ant.setVisibility(View.GONE);
                text_preco_act.setVisibility(View.GONE);
                text_nr_ofertas.setVisibility(View.GONE);
                text_desc.setVisibility(View.GONE);
                linear_ris.setVisibility(View.GONE);
                text_oferta.setVisibility(View.GONE);
                label.setText(desc_list.get(position).getTitulo());
                text_desc2.setText(desc_list.get(position).getDescricao());
            }
            return convertView;
        }

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_menu_especial2.php";

        // contacts JSONArray
        JSONObject dataJsonArr = null;
        JSONObject dataJsonRep = null;
        JSONArray dataJsonPessoas = null;
        JSONArray dataJsonResTitulo = null;
        JSONArray dataJsonHorarios = null;

        private MenuEspecial delegate;

        public AsyncTaskParseJson (MenuEspecial delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MenuEspecial.this);
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
                JSONObject dict2 = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict2.put("lang","pt");
                dict2.put("linguatel","pt");

                if(Globals.getInstance().getUser()!=null &&  Globals.getInstance().getUser().getTipoconta().equals("facebook")){
                    dict2.put("face_id",Globals.get_instance().getUser().getId_face());
                    dict2.put("user_id","0");
                }else if(Globals.getInstance().getUser()!=null && Globals.getInstance().getUser().getTipoconta().equals("guru")){
                    dict2.put("face_id","0");
                    dict2.put("user_id",Globals.get_instance().getUser().getUserid());
                }else{
                    dict2.put("face_id","0");
                    dict2.put("user_id","0");
                }

                dict2.put("horario_lang","pt");
                dict2.put("rest_id",rest_id);
                dict2.put("rest_cartao_id",rest_cartao_id);



                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict2);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna isto no especial"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonRep = jsonObj.getJSONObject("resp");
                dataJsonArr = dataJsonRep.getJSONObject("res");

                dataJsonPessoas = dataJsonArr.getJSONArray("n_pessoas");
                dataJsonHorarios = dataJsonRep.getJSONArray("h_esp");
                dataJsonResTitulo = dataJsonRep.getJSONArray("res_titulo");


                // Log.v("JSON RES",dataJsonArr.getString("res"));
                //Log.v("JSON NRPESSOAS",dataJsonArr.getString("res"));

                // loop through all users
                some_list = new ArrayList<Menu_Especial>();
                Menu_Especial menu = new Menu_Especial();
                menu.setReserva_disp(dataJsonArr.getString("reserva_disp"));
                menu.setReserva_feita(dataJsonArr.getString("reserva_feita"));
                menu.setReserva_tem(dataJsonArr.getString("reserva_tem"));
                menu.setNumero_vaucher(dataJsonArr.getString("numero_vaucher"));
                menu.setNome_cat(dataJsonArr.getString("nome_cat"));
                menu.setNome(dataJsonArr.getString("nome"));
                menu.setDescricao(dataJsonArr.getString("descricao"));
                menu.setPreco_ant(dataJsonArr.getString("preco_ant"));
                menu.setPreco_actual(dataJsonArr.getString("preco_actual"));
                menu.setDestaque(dataJsonArr.getString("destaque"));
                menu.setImagem(dataJsonArr.getString("imagem"));
                menu.setId(dataJsonArr.getString("id"));
                menu.setPartilhar(dataJsonArr.getString("partilhar"));
                menu.setReserva_obrigatoria(dataJsonArr.getString("reserva_obrigatoria"));
                menu.setDesconto(dataJsonArr.getString("desconto"));
                menu.setDatafinal(dataJsonArr.getString("datafinal"));
                menu.setDataActual(dataJsonArr.getString("dataActual"));
                menu.setTipo(dataJsonArr.getString("tipo"));
                menu.setId_pai(dataJsonArr.getString("id_pai"));
                some_list.add(menu);

                Log.v("PRECO ANT",dataJsonArr.getString("preco_ant"));
                Log.v("PRECO ACT",dataJsonArr.getString("preco_actual"));
                Log.v("NOME",dataJsonArr.getString("nome"));


                desc_list = new ArrayList<Descricao_Especial>();
                for (int i = 0; i < dataJsonResTitulo.length(); i++) {
                    JSONObject c = dataJsonResTitulo.getJSONObject(i);

                    Descricao_Especial desc = new Descricao_Especial();
                    desc.setDescricao(c.getString("descricao"));
                    desc.setTitulo(c.getString("titulo"));
                    desc_list.add(desc);
                }

                hora_list = new ArrayList<Horario_Especial>();
                for (int i = 0; i < dataJsonHorarios.length(); i++) {
                    JSONObject a = dataJsonHorarios.getJSONObject(i);

                    Horario_Especial hora = new Horario_Especial();
                    hora.setId(a.getString("id"));
                    hora.setHora_inicio(a.getString("hora_inicio"));
                    hora.setN_pessoas_h(a.getString("n_pessoas_h"));
                    hora.setDia_id(a.getString("dia_id"));
                    hora_list.add(hora);
                }


                nr_pes_list = new ArrayList<Nr_Pessoas_Especial>();
                for (int i=0;i<dataJsonPessoas.length();i++){
                    Nr_Pessoas_Especial pes = new Nr_Pessoas_Especial();
                    pes.setNr(dataJsonPessoas.get(i).toString());
                    nr_pes_list.add(pes);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncComplete(true);  }

    }



    public void asyncComplete(boolean success) {


        mAdapter = new MyListAdapter(this, R.layout.list_item, desc_list);

        LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_menu, mListView, false);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_menuespecial, mListView, false);

        mListView.addFooterView(footer, null, false);
        mListView.addHeaderView(header, null, false);


        edt = (TextView) findViewById(R.id.text_rating_desc);
        edt1 = (TextView) findViewById(R.id.text_preco);
        edt2 = (TextView) findViewById(R.id.text_oferta);
        edt3 = (TextView) findViewById(R.id.text_preco_ant);
        edt4 = (TextView) findViewById(R.id.text_titulo);
        edt5 = (TextView) findViewById(R.id.bt_seguuintw);
        edt6 = (TextView) findViewById(R.id.textView_desc_especial);
        edt7 = (TextView) findViewById(R.id.textView_nome_especial);
        edt8 = (TextView) findViewById(R.id.textView10);
        edt9 = (ImageView) findViewById(R.id.icon_err3);

        Calendar c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = df.format(c.getTime());

        Log.v("DATA INICIO",""+some_list.get(0).getDatafinal());
        Log.v("DATA ACTUAL",formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date a = null, b = null;
        try {
            a = sdf.parse(some_list.get(0).getDatafinal());
            b = sdf.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final StringBuilder time = new StringBuilder();
        //      .getTime() does the conversion: Date --> long
        CountDownTimer cdt = new CountDownTimer(a.getTime() - b.getTime(), 1000) {

            public void onTick(long millisUntilFinished) {

                //edt1.setText("Falta" + millisUntilFinished / 1000);
                //Log.v("FALTA TANTO TEMPO:", "" + millisUntilFinished / 1000);


                time.setLength(0);
                // Use days if appropriate
                if(millisUntilFinished > DateUtils.DAY_IN_MILLIS) {
                    long count = millisUntilFinished / DateUtils.DAY_IN_MILLIS;
                    edt.setText(""+count);
                    if(count > 1) {
                        edt5.setText(R.string.dias);
                        time.append(count).append(" days, ");
                    }else {
                        edt5.setText(R.string.dia);
                        time.append(count).append(" day, ");
                    }
                    millisUntilFinished %= DateUtils.DAY_IN_MILLIS;
                }

                if(millisUntilFinished > DateUtils.HOUR_IN_MILLIS) {
                    long count2 = millisUntilFinished / DateUtils.HOUR_IN_MILLIS;
                    edt1.setText(""+count2);
                    if(count2 > 1) {
                        edt6.setText(R.string.horas);
                        time.append(count2).append(" hours, ");
                    }else {
                        edt6.setText(R.string.hora);
                        time.append(count2).append(" hour, ");
                    }
                    millisUntilFinished %= DateUtils.HOUR_IN_MILLIS;
                }
                if(millisUntilFinished > DateUtils.MINUTE_IN_MILLIS) {
                    long count3 = millisUntilFinished / DateUtils.MINUTE_IN_MILLIS;
                    edt2.setText(""+count3);
                    if(count3 > 1) {
                        edt7.setText(R.string.mins);
                        time.append(count3).append(" minutes, ");
                    }else {
                        edt7.setText(R.string.min);
                        time.append(count3).append(" minute, ");
                    }
                    millisUntilFinished %= DateUtils.MINUTE_IN_MILLIS;
                }

                if(millisUntilFinished > DateUtils.SECOND_IN_MILLIS) {
                    long count4 = millisUntilFinished / DateUtils.SECOND_IN_MILLIS;
                    edt3.setText(""+count4);
                    if(count4 > 1) {
                        edt8.setText(R.string.secs);
                        time.append(count4).append(" minutes, ");
                    }else {
                        edt8.setText(R.string.sec);
                        time.append(count4).append(" minute, ");
                    }
                    millisUntilFinished %= DateUtils.MINUTE_IN_MILLIS;
                }

                time.append(DateUtils.formatElapsedTime(Math.round(millisUntilFinished / 1000d)));
                //edt1.setText(time.toString());
            }

            public void onFinish() {
                // TODO Auto-generated method stub

            }
        }.start();

        if(some_list.get(0).getTipo().equals("fixo")){
            edt.setVisibility(View.GONE);
            edt1.setVisibility(View.GONE);
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt4.setVisibility(View.GONE);
            edt5.setVisibility(View.GONE);
            edt6.setVisibility(View.GONE);
            edt7.setVisibility(View.GONE);
            edt8.setVisibility(View.GONE);
            edt9.setVisibility(View.GONE);
        }
        new DownloadImageTask((ImageView) header.findViewById(R.id.image_capa)).execute("http://menuguru.pt/"+ some_list.get(0).getImagem());


        // Assign adapter to ListView
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        Log.v("RESERVA FEITA",some_list.get(0).getReserva_tem());

        if(some_list.get(0).getReserva_tem().equals("1")){
            bt_reserva.setVisibility(View.GONE);
            bt_voucher.setVisibility(View.VISIBLE);
        }else{
            bt_reserva.setVisibility(View.VISIBLE);
            bt_voucher.setVisibility(View.GONE);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonReserva extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_reserva_especial_criar.php";


        private MenuEspecial delegate;

        public AsyncTaskParseJsonReserva (MenuEspecial delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading();
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

                dict.put("id_pai",some_list.get(0).getId_pai());
                dict.put("id_esp",some_list.get(0).getId());
                dict.put("lang",Globals.get_instance().getLingua());
                dict.put("nome_cartao",some_list.get(0).getNome());
                dict.put("email_user",sel_email);
                dict.put("telefone",sel_telefone);
                dict.put("h_id",sel_id_hora);
                dict.put("num_pes_reserva",sel_nr_pes);
                if(sel_obs.isEmpty()){
                    dict.put("escrito"," ");
                }else{
                    dict.put("escrito",sel_obs);
                }
                dict.put("dia",sel_dia_semana);
                dict.put("data", data_selec);
                dict.put("rest_id", rest_id);
                dict.put("user_nome",sel_nome);
                if(Globals.getInstance().getUser().getTipoconta().equals("facebook")){
                    dict.put("face_id",Globals.get_instance().getUser().getId_face());
                    dict.put("user_id","0");
                }else if(Globals.getInstance().getUser().getTipoconta().equals("guru")){
                    dict.put("face_id","0");
                    dict.put("user_id",Globals.get_instance().getUser().getUserid());
                }else{
                    dict.put("face_id","0");
                    dict.put("user_id","0");
                }




                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna na reserva"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                // dataJsonArr = jsonObj.getString("res");
                res_reserva = jsonObj.getString("res");
                msg_reserva = jsonObj.getString("msg");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  delegate.asyncCompleteReserva(true);  }

    }



    public void asyncCompleteReserva(boolean success) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog1.dismiss();
                dialog_hora.dismiss();
                dialog_pes.dismiss();
                dialog_obs.dismiss();
                dialog_conf.dismiss();
                dialog_loading.dismiss();
                if(res_reserva.equals("sucesso")){
                    final Dialog dialog_final = new Dialog(MenuEspecial.this);
                    dialog_final.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_final.setContentView(R.layout.dialog_final);
                    TextView tx_fdata = (TextView) dialog_final.findViewById(R.id.tv_fdata);
                    TextView tx_fhora= (TextView) dialog_final.findViewById(R.id.tv_fhora);
                    TextView tx_fnome = (TextView) dialog_final.findViewById(R.id.tv_fnome);
                    TextView tx_ftele = (TextView) dialog_final.findViewById(R.id.tv_ftelefone);
                    TextView tx_femail = (TextView) dialog_final.findViewById(R.id.tv_femail);
                    TextView tx_fobs = (TextView) dialog_final.findViewById(R.id.tv_fobs);

                    tx_fdata.setText(reportDate);
                    String aux_pes;
                    if(sel_nr_pes.equals("1")){
                        aux_pes = getString(R.string.pessoa);
                    }else{
                        aux_pes = getString(R.string.pessoas);
                    }
                    tx_fhora.setText(sel_hora+" "+sel_nr_pes+" "+aux_pes);
                    tx_fnome.setText(sel_nome);
                    tx_ftele.setText(sel_telefone);
                    tx_femail.setText(sel_email);
                    tx_fobs.setText(sel_obs);
                    bt_reserva.setVisibility(View.GONE);
                    bt_voucher.setVisibility(View.VISIBLE);
                    dialog_final.show();
                }else{
                    final Dialog dialog_final = new Dialog(MenuEspecial.this);
                    dialog_final.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_final.setContentView(R.layout.dialog_final);
                    dialog_final.show();
                }
            }
        }, 7000);
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonNrpessoas extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_especial_dia_limite.php";

        // contacts JSONArray
        JSONObject dataJsonArr = null;

        private MenuEspecial delegate;

        public AsyncTaskParseJsonNrpessoas (MenuEspecial delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MenuEspecial.this);
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

                dict.put("id_pai",rest_cartao_id);
                Log.v("ID PAI",rest_cartao_id);
                Log.v("DATA",data_selec);
                dict.put("data",data_selec);




                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna isto"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);
                num_por_dia = outro.getString("num");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){  progressDialog.dismiss();delegate.asyncCompleteNrPessoas(true);  }

    }



    public void asyncCompleteNrPessoas(boolean success) {


    }




}