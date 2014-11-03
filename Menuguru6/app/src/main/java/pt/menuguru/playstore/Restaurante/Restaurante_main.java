package pt.menuguru.playstore.Restaurante;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import pt.menuguru.playstore.Favoritos.Favoritos;
import pt.menuguru.playstore.Json_parser.JSONParser;
import pt.menuguru.playstore.LoginMenuGuru;
import pt.menuguru.playstore.Mapa.Mapa;
import pt.menuguru.playstore.MenuEspecial;
import pt.menuguru.playstore.Restaurante.Info.InfoRestaurante;
import pt.menuguru.playstore.Utils.Comentario;
import pt.menuguru.playstore.Utils.Globals;
import pt.menuguru.playstore.Utils.Horario_Mesa;
import pt.menuguru.playstore.Utils.ImageLoader;
import pt.menuguru.playstore.Utils.Menu_do_restaurante;
import pt.menuguru.playstore.Utils.Nr_pessoas_mesa;
import pt.menuguru.playstore.gif.decoder.GifRun;
import pt.menuguru.playstore.menus.Menu_diaria;
import pt.menuguru.playstore.MyApplication;
import pt.menuguru.playstore.menus.Menu_ementa;
import pt.menuguru.menuguru.R;
import pt.menuguru.playstore.Restaurante.Avaliar.Avaliar_restaurante;
import pt.menuguru.playstore.Restaurante.Comentarios.Lista_comentarios;
import pt.menuguru.playstore.Utils.User;
import pt.menuguru.playstore.Utils.Utils;

import static android.R.layout.simple_list_item_1;

/**
 * Created by hugocosta on 19/09/14.
 */
public class Restaurante_main extends FragmentActivity {


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
   // private static final int NUM_PAGES = Globals.getInstance().getCfunc().length;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    private ViewGroup header2;

    private static MyListAdapter mAdapter;
    private ArrayList<Menu_do_restaurante> some_list;
    private ArrayList<Horario_Mesa> hora_list;
    private ArrayList<Horario_Mesa> aux_hora_list;
    private ArrayList<Nr_pessoas_mesa> nr_pes_list;

    private ArrayList<String> fotos;

    private ListView gridView;
    private ProgressDialog progressDialog;
    // informaçao que já vem de traz
    private String rest_id;
    private String url_foto;
    private String latitude;
    private String longitude;
    private String morada;
    private String mediarating;
    private String votacoes;
    private String abertoFechado;
    private String horarioAbertura;
    private String nome_rest;
    private String cidade_nome;
    private String telefone;
    private boolean segue_rest = false;
    private String pode_reservar;

    private Restaurante_main delegado_principal;

    String data_selec;
    String num_por_dia;
    String sel_id_hora = "";
    String sel_hora = "";
    String sel_nr_pes = "";
    String sel_obs = "";
    String sel_nome = "";
    String sel_telefone = "";
    String sel_email = "" ;
    String sel_dia_semana = "" ;
    String num_por_hora = "" ;
    String nr_voucher = "" ;

    Dialog dialog_loading;
    Dialog dialog_conf;
    Dialog dialog_obs;
    Dialog dialog_pes;
    Dialog dialog_hora;
    Dialog dialog1;

    CalendarView calendar;

    String hora_minimo_antedencia_mesa;

    private String[] listEstrelas;
    ArrayList<Comentario> comentarios;

    String reportDate;

    Button bt_reserva;

    String res_reserva;
    String msg_reserva;

    // se tiver destaque tem de aparecer um layout diferente
    private boolean destaque = false;


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

        delegado_principal = this;
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_left_b);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);


        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante");
        url_foto = intent.getStringExtra("urlfoto");
        latitude = intent.getStringExtra("lat");
        longitude = intent.getStringExtra("lon");
        morada = intent.getStringExtra("morada");
        nome_rest = intent.getStringExtra("nome_rest");
        cidade_nome = intent.getStringExtra("cidade_nome");
        telefone = intent.getStringExtra("telefone");

        actionBar.setTitle(nome_rest);

        setContentView(R.layout.activity_restaurante_main);
        bt_reserva = (Button) findViewById(R.id.bt_reservar_mesa);
        new AsyncTaskParseJson1(this).execute();
        new AsyncTaskParseJsonHorarioMesa(this).execute();

        bt_reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1 = new Dialog(Restaurante_main.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_reserva);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                data_selec = df.format(c.getTime());

                ImageButton close = (ImageButton)dialog1.findViewById(R.id.imageButton_close_dialogs);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });

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
                        Date date1=null, date2=null;
                        try {
                            date1 = sdf.parse(data_selec);
                            date2 = sdf.parse(dataactual);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.v("data 1",""+date1);
                        Log.v("data 2",""+date2);

                        if(date1.compareTo(date2)>=0){

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

                                aux_hora_list = new ArrayList<Horario_Mesa>();
                                for (int i = 0; i < hora_list.size(); i++) {
                                    Horario_Mesa hora = new Horario_Mesa();
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
                                        Log.v("HORA MINIMO",hora_minimo_antedencia_mesa);
                                        if(Long.parseLong(hora_minimo_antedencia_mesa)<aaa){
                                            hora.setId(hora_list.get(i).getId());
                                            hora.setDia_id(hora_list.get(i).getDia_id());
                                            hora.setHora_inicio(hora_list.get(i).getHora_inicio());
                                            hora.setN_pessoas_h(hora_list.get(i).getN_pessoas_h());
                                            aux_hora_list.add(hora);
                                        }
                                    }
                                }
                            new AsyncTaskParseJsonDiaLimite(Restaurante_main.this).execute();
                                SelecionaHora();

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

                dialog1.show();

            }
        });

        // new AsyncTaskParseJsonEstrelas(this).execute();
    }

    public void SelecionaHora(){
        sel_hora = "";
        sel_dia_semana = "";
        sel_id_hora = "";
        //, R.style.PauseDialog2);
        dialog_hora = new Dialog(Restaurante_main.this);
        dialog_hora.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_hora.setContentView(R.layout.dialog_hora);


        ImageButton close = (ImageButton)dialog_hora.findViewById(R.id.imageButton_close_dialogs);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dialog_hora.dismiss();
            }
        });

        TextView bt_ant = (TextView) dialog_hora.findViewById(R.id.bt_ant_cal);
        TextView bt_seg = (TextView) dialog_hora.findViewById(R.id.bt_seg_nrp);

        ListView list_hora = (ListView) dialog_hora.findViewById(R.id.list_hora);
        SpinnerAdapterVitor dataAdapter = new SpinnerAdapterVitor(Restaurante_main.this,
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
                    Log.v("NUM POR DIA",""+num_por_dia);
                    Log.v("NR POSSOAS POSSIVEL",""+nr_voucher);
                    int local;
                    if (Integer.parseInt(num_por_hora) < Integer.parseInt(num_por_dia))
                        local = Integer.parseInt(num_por_hora);
                    else
                        local = Integer.parseInt(num_por_dia);
                    if(local < Integer.parseInt(nr_voucher))
                        local = local;
                    else
                        local = Integer.parseInt(num_por_dia);

                    Log.v("O NUMERO MENOR",""+local);
                    nr_pes_list = new ArrayList<Nr_pessoas_mesa>();
                    for (int i = 0; i < local; i++) {
                        Nr_pessoas_mesa nr_pes = new Nr_pessoas_mesa();
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append(i+1);
                            String strI = sb.toString();
                            nr_pes.setNr(strI);
                            nr_pes_list.add(nr_pes);
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
        dialog_pes = new Dialog(Restaurante_main.this);
        dialog_pes.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pes.setContentView(R.layout.dialog_pessoas);

        ImageButton close = (ImageButton)dialog_pes.findViewById(R.id.imageButton_close_dialogs);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dialog_hora.dismiss();
                dialog_pes.dismiss();
            }
        });

        TextView bt_ant_hora = (TextView) dialog_pes.findViewById(R.id.bt_ant_hora);
        TextView bt_seg_obs = (TextView) dialog_pes.findViewById(R.id.bt_seg_obs);

        ListView list_pes = (ListView) dialog_pes.findViewById(R.id.list_nr_pessoa);
        AdapterVitor dataAdapter = new AdapterVitor(Restaurante_main.this,
                simple_list_item_1,nr_pes_list);
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
        dialog_obs = new Dialog(Restaurante_main.this);
        dialog_obs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_obs.setContentView(R.layout.dialog_obs);

        ImageButton close = (ImageButton)dialog_obs.findViewById(R.id.imageButton_close_dialogs);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dialog_hora.dismiss();
                dialog_pes.dismiss();
                dialog_obs.dismiss();
            }
        });

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
        dialog_conf = new Dialog(Restaurante_main.this);
        dialog_conf.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_conf.setContentView(R.layout.dialog_conf_dados);


        ImageButton close = (ImageButton)dialog_conf.findViewById(R.id.imageButton_close_dialogs);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dialog_hora.dismiss();
                dialog_pes.dismiss();
                dialog_obs.dismiss();
                dialog_conf.dismiss();
            }
        });

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
                        Intent myIntent = new Intent(Restaurante_main.this, LoginMenuGuru.class);
                        startActivity(myIntent);
                    }
                    sel_nome =  edit_nome.getText().toString();
                    sel_telefone =  edit_telefone.getText().toString();
                    sel_email =  edit_email.getText().toString();
                    new AsyncTaskParseJsonReserva(Restaurante_main.this).execute();
                }
            }
        });
        dialog_conf.show();
    }



    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
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

    public class SpinnerAdapterVitor extends ArrayAdapter<Horario_Mesa>
    {
        private ArrayList<Horario_Mesa> hro;

        public SpinnerAdapterVitor(Context context, int resource, ArrayList<Horario_Mesa> lista)
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


    public class AdapterVitor extends ArrayAdapter<Nr_pessoas_mesa>
    {
        private ArrayList<Nr_pessoas_mesa> pes;

        public AdapterVitor(Context context, int resource, ArrayList<Nr_pessoas_mesa> lista)
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


    public void AvisoData_ant(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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

    public void AvisoHora(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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

    public void AvisoData_dep(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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

    public void AvisoPessoas(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Restaurante_main.this);
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
        dialog_loading = new Dialog(Restaurante_main.this);
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setContentView(R.layout.dialog_loading);
        TextView tx_data = (TextView) dialog_loading.findViewById(R.id.tv_data);
        TextView tx_hora= (TextView) dialog_loading.findViewById(R.id.tv_hora);
        TextView tx_nome = (TextView) dialog_loading.findViewById(R.id.tv_nome);
        TextView tx_tele = (TextView) dialog_loading.findViewById(R.id.tv_telefone);
        TextView tx_email = (TextView) dialog_loading.findViewById(R.id.tv_email);
        TextView tx_obs = (TextView) dialog_loading.findViewById(R.id.tv_obs);
        SurfaceView v = (SurfaceView) dialog_loading.findViewById(R.id.surfaceView_loading);
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


    @Override
    public void onResume()
    {
        super.onResume();
        // tenho de chamar as estrelas de novo
        new AsyncTaskParseJsonEstrelas(this).execute();
        new AsyncTaskParseJsonComentarios(this).execute();
        new AsyncTaskParseJsonVerificaSeguir(this).execute();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_localizacao, menu);
        return true;
    }
    */


    public void initializeGalery()
    {

        List<Fragment> fragments = new Vector<Fragment>();


        Fragment fragmentCapa = new Imagem_galeria().create(url_foto);
        fragments.add(fragmentCapa);

        // aqui tem de conter um ciclo e mudar para ir buscar imagens a net
        for (int i = 0 ; i< fotos.size() ; i++)
        {
            Fragment fragment = new Imagem_galeria().create(fotos.get(i));
            fragments.add(fragment);
        }

        ViewPager galeria = (ViewPager) findViewById(R.id.galeria_imagens_slider);
        PagerAdapter adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);


        galeria.setAdapter(adapter);

    }


    @Override
    public void onBackPressed()
    {
        delegado_principal = null;
        finish();

        overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
    }

    public void initialisePagin()
    {
        TextView votos = (TextView) header2.findViewById(R.id.textView_avaliacoes);
        votos.setText(votacoes +" "+ getString(R.string.votacoes));

        List<Fragment> fragments = new Vector<Fragment>();

        // tenho de fazer alterações para poder atribuir o numero de estrelas
        Fragment fragmentMedia = new tab_rating().create(mediarating, votacoes);

        fragments.add(fragmentMedia);

        // agora tenho de fazer as alterções para receber um array com 5 posiçoes para o rating
        Fragment fragmentAllStars = new tab_five_ratin().create(listEstrelas, votacoes);

        fragments.add(fragmentAllStars);

        ViewPager mPager = (ViewPager) findViewById(R.id.pager_restaurante);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);

        if(delegado_principal != null)
            mPager.setAdapter(mPagerAdapter);

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments)
        {
            super(fm);
            this.fragments = fragments;
        }



        @Override
        public Fragment getItem(int position) {


            return fragments.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                delegado_principal = null;
                finish();

                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }
        return false;
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson1 extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_m_rest.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJson1 (Restaurante_main delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Restaurante_main.this);
            progressDialog.setCancelable(false);
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

                dict.put("lang","");
                /*
                dict.put("face_id", Globals.getInstance().getUser().getId_face());
                dict.put("user_id", Globals.getInstance().getUser().getUserid());
                */

                dict.put("face_id", "0");
                dict.put("user_id", "0");

                dict.put("horario_lang","pt");
                dict.put("rest_id",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // Log.v("jfgrhng","resultado da procura = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna dentro do menu"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("res");

                // loop through all users

                // para saber se o restaurante esta aberto ou fechado
                JSONObject horario = jsonObj.getJSONObject("horario");
                abertoFechado = horario.getString("aberto");
                horarioAbertura = horario.getString("horario");
                hora_minimo_antedencia_mesa = jsonObj.getString("hora_minimo_antedencia_mesa");

                // para remover o botão de reserva caso nao possa reservar
                pode_reservar = jsonObj.getString("reserva_mesa");


                String completo = jsonObj.getString("envio");
                JSONObject outro =new JSONObject(completo);
                nr_voucher = outro.getString("num_pessoas");

                some_list = new ArrayList<Menu_do_restaurante>();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Menu_do_restaurante menu = new Menu_do_restaurante();
                    menu.setNome(c.getString("nome"));
                    menu.setUrlImage(c.getString("imagem"));
                    menu.setTipo(c.getString("tipo"));
                    menu.setDb_id(c.getString("pai_id"));
                    menu.setId_rest(rest_id);
                    menu.setDestaque(c.getString("destaque"));
                    menu.setDescricao(c.getString("descricao"));
                    menu.setHora_minimo_antedencia_especial(jsonObj.getString("hora_minimo_antedencia_especial"));
                    if (menu.getDestaque().equalsIgnoreCase("1"))
                        destaque = true;

                    if (menu.getTipo().equalsIgnoreCase("menu_especial"))
                    {
                        menu.setPrecoNovo(c.getString("precoesp"));
                        menu.setPrecoAntigo(c.getString("precoespant"));
                        menu.setEspecialFita(c.getString("especial_um_fita"));
                        menu.setDesconto(c.getString("desconto"));


                    }

                    some_list.add(menu);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompleteMenus(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonGaleria extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_galeria_rest.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonGaleria (Restaurante_main delegate){
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // não preciso desta parte porque ja tenho outro loading a correr
            /*
            progressDialog = new ProgressDialog(Restaurante_main.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
            */
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

                dict.put("lang","");

                dict.put("rest_id",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado da galeria = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna galeria "+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("rest");

                // loop through all users


                fotos = new ArrayList<String>();
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    String foto = c.getString("foto");

                    fotos.add(foto);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
           // progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompleteFotos(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonEstrelas extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listar_rating.php";


        JSONObject dataJason = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonEstrelas (Restaurante_main delegate){
            this.delegate = delegate;
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

                dict.put("lang",Globals.getInstance().getLingua());

                dict.put("id_rest",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado das estrelas = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna para as estrelas"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);


                // loop through all users


                votacoes = outro.getString("contagem");
                mediarating = outro.getString("media");

                listEstrelas = new String[5];
                listEstrelas[0] = outro.getString("umaestrela");
                listEstrelas[1] = outro.getString("duasestrela");
                listEstrelas[2] = outro.getString("tresestrela");
                listEstrelas[3] = outro.getString("quatroestrela");
                listEstrelas[4] = outro.getString("cincoestrela");


                Log.v("werqwe", "numero de votações " + votacoes);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompleteEstrelas(true);
        }

    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonComentarios extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_listar_comentarios.php";


        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonComentarios (Restaurante_main delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("lang",Globals.getInstance().getLingua());

                dict.put("id_rest",rest_id);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado dos comentarios = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna estes comentarios"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array

                dataJsonArr = jsonObj.getJSONArray("res");



                comentarios = new ArrayList<Comentario>();

                for (int i = 0 ; i < dataJsonArr.length() ; i++ )
                {
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    Comentario comentario = new Comentario();
                    comentario.setId_com(c.getString("id_com"));
                    comentario.setComentario(c.getString("comentario"));
                    comentario.setResp_com(c.getString("resp_com"));
                    comentario.setData_com(c.getString("data_com"));
                    comentario.setData_respest(c.getString("data_respest"));
                    comentario.setNome_user_com(c.getString("nome_user_com"));
                    comentario.setNome_rest_com(c.getString("nome_rest_com"));

                    comentarios.add(comentario);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompleteComentarios(true);
        }

    }

    public void asyncCompleteComentarios(boolean success)
    {
        // esta parte serve para os comentarios
        TextView user_name = (TextView) header2.findViewById(R.id.textView_username);
        TextView data_coment = (TextView) header2.findViewById(R.id.textView_data_comentario);
        TextView comentario = (TextView) header2.findViewById(R.id.textView_Comentario);
        TextView ver_todos = (TextView) header2.findViewById(R.id.textView_ver_todos);



        // quando ainda não foram feitos comentarios

        if (comentarios.size() == 0) {
            user_name.setText(getString(R.string.sem_coments));
            data_coment.setText("");
            comentario.setText(getString(R.string.seja_o_primeiro_c));
            comentario.setTextColor(R.color.gray);
            ver_todos.setText("");
        }else // quando ja tem comentarios
        {
            user_name.setText(comentarios.get(0).getNome_user_com());
            data_coment.setText(comentarios.get(0).getData_com());
            //comentario.setTextColor(R.color.black);
            comentario.setText(comentarios.get(0).getComentario());
            ver_todos.setText(getString(R.string.ver_todos));

            LinearLayout com = (LinearLayout) header2.findViewById(R.id.laout_comentario);
            com.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Restaurante_main.this, Lista_comentarios.class);
                    intent.putExtra("restaurante_id", rest_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
                }
            });

        }
    }

    public void asyncCompleteEstrelas(boolean success)
    {
        //new AsyncTaskParseJson1(this).execute();

        initialisePagin();
    }


    public void asyncCompleteFotos(boolean success)
    {
        initializeGalery();
    }


    public void asyncCompleteMenus(boolean success){


        if(pode_reservar.equalsIgnoreCase("0"))
        {
            Button bt_reservar_mesa = (Button) findViewById(R.id.bt_reservar_mesa);
            bt_reservar_mesa.setAlpha(0);
            bt_reservar_mesa.setEnabled(false);
        }


        gridView = (ListView) findViewById(R.id.morada_rest);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);

        // para calcular a distancia
        Location locationRest = new Location("");
        locationRest.setLatitude(Double.parseDouble(latitude));
        locationRest.setLongitude(Double.parseDouble(longitude));

        Location locationPhone = new Location("");
        locationPhone.setLatitude(Double.parseDouble(Globals.getInstance().getLatitude()));
        locationPhone.setLongitude(Double.parseDouble(Globals.getInstance().getLongitude()));


        final LayoutInflater inflater = LayoutInflater.from(this);

        if (destaque) {
            // como tem algom em destaque tenho de chamar outro layout
            header2 = (ViewGroup) inflater.inflate(R.layout.header_restaurante_main_destaque, gridView, false);

            // este novo layout tem um menu no topo que tenho de preencher
            // mas primeiro tenho de achar esse menu na lista de menus
            for(int i = 0 ; i < some_list.size() ; i++)
            {
                Menu_do_restaurante menu = some_list.get(i);
                if (menu.getDestaque().equalsIgnoreCase("1"))
                {

                    // aqui tenho de colocar este menu no topo do header
                    TextView nome_menu = (TextView) header2.findViewById(R.id.textView_nome_especial);
                    nome_menu.setText(menu.getNome());

                    TextView desc_menu = (TextView) header2.findViewById(R.id.textView_desc_especial);
                    desc_menu.setText(menu.getDescricao());

                    TextView preco_ant = (TextView) header2.findViewById(R.id.textView_preco_ant_especial);
                    preco_ant.setText(menu.getPrecoAntigo());
                    preco_ant.setPaintFlags(preco_ant.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    TextView preco_novo = (TextView) header2.findViewById(R.id.preco_novo_especial);
                    preco_novo.setText(menu.getPrecoNovo());

                    ImageView imagemEspecial = (ImageView) header2.findViewById(R.id.imageView_destaque);
                    ImageLoader imageLoaderEspecial=new ImageLoader(getApplicationContext());
                    imageLoaderEspecial.DisplayImage("http://menuguru.pt/"+ menu.getUrlImage(), imagemEspecial);


                    final int especial = i;

                    RelativeLayout destaqueView = (RelativeLayout) header2.findViewById(R.id.view_destaques);
                    destaqueView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            abrirRestaurante(especial);

                        }
                    });

                }

            }




        }
        else
        {
            header2 = (ViewGroup) inflater.inflate(R.layout.header_restaurante_main, gridView, false);
        }
        TextView dist = (TextView) header2.findViewById(R.id.distancia_restaurante_header);
        dist.setText(Utils.getDistance(locationPhone, locationRest));

        dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Restaurante_main.this, Mapa.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("nome", nome_rest);
                intent.putExtra("foto", url_foto);
                startActivity(intent);
                overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }
        });


        TextView adress = (TextView) header2.findViewById(R.id.morada_rest);
        adress.setText(morada);

        /*
        TextView votos = (TextView) header2.findViewById(R.id.textView_avaliacoes);
        votos.setText(votacoes +" "+ getString(R.string.votacoes));
        */


        ImageButton buttonInfo = (ImageButton)header2.findViewById(R.id.button_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
                {
                    if(Globals.get_instance().getUser() == null)
                    {
                        Intent intent = new Intent(Restaurante_main.this, LoginMenuGuru.class);
                        startActivity(intent);
                    }else
                    {

                        // lançar intent com a info
                        Intent intent = new Intent(Restaurante_main.this, InfoRestaurante.class);
                        intent.putExtra("restaurante_id", rest_id);
                        intent.putExtra("nome_rest", nome_rest);
                        intent.putExtra("cidade", cidade_nome);

                        startActivity(intent);

                        overridePendingTransition(R.anim.abc_slide_in_bottom, 0);
                    }
            }
        });

        ImageButton buttonAvaliar = (ImageButton) header2.findViewById(R.id.button_avaliar);
        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Globals.get_instance().getUser() == null)
                {
                    Intent intent = new Intent(Restaurante_main.this, LoginMenuGuru.class);
                    startActivity(intent);

                }else
                {
                    Intent inten = new Intent(Restaurante_main.this, Avaliar_restaurante.class);
                    inten.putExtra("restaurante", rest_id);
                    inten.putExtra("rating",mediarating);
                    //startActivityForResult(inten);
                    startActivity(inten);
                    overridePendingTransition(R.anim.abc_slide_in_bottom , R.anim.abc_fade_out);
                }
            }
        });


        ImageButton buttonFavoritos = (ImageButton) header2.findViewById(R.id.texto_fav_nome);
        buttonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Globals.get_instance().getUser() == null)
                {
                    Intent intent = new Intent(Restaurante_main.this, LoginMenuGuru.class);
                    startActivity(intent);

                }else
                {
                    Intent inten = new Intent(Restaurante_main.this, Favoritos.class);
                    //Intent inten = new Intent(Restaurante_main.this, EmptyListActivity.class);
                    inten.putExtra("restaurante", rest_id);

                    startActivity(inten);
                    overridePendingTransition(R.anim.abc_slide_in_bottom , R.anim.abc_fade_out);
                }
            }
        });


        // para quando tem comentarios tenho de ir buscar por webservice

        ViewGroup aberto_fechado = (ViewGroup)inflater.inflate(R.layout.restaurante_aberto_fechado, gridView , false);

        TextView labelAberto = (TextView) aberto_fechado.findViewById(R.id.textView_aberto_fechado);
       // LinearLayout layout_aberto = (LinearLayout) aberto_fechado.findViewById(R.)

        // toast para saber se esta aberto ou fechado
        // encontrei uma alternativa... podemos adicionar varios headers a lista... adicionamos se esta aberto ou fechado
        // depois so temos de meter este texto em baixo nessa view



        if (abertoFechado.equalsIgnoreCase("nao"))
        {
            //display in short period of time
            //linearLayout_abertofechado
            RelativeLayout abc =(RelativeLayout) aberto_fechado.findViewById(R.id.relativeLayout_aberto_fechado);
            abc.setBackgroundColor(Color.parseColor("#cc0000"));
            labelAberto.setText( getString(R.string.hoje) +" "+ horarioAbertura);
        }
        else
        {
            //display in short period of time
            labelAberto.setText(getString(R.string.aberto));
            RelativeLayout abc =(RelativeLayout) aberto_fechado.findViewById(R.id.relativeLayout_aberto_fechado);
            abc.setBackgroundColor(Color.parseColor("#669900"));
        }

        ImageButton partilhar = (ImageButton) header2.findViewById(R.id.imageButton3);
        partilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                partilharRestaurante();
            }
        });


        gridView.addHeaderView(aberto_fechado, null, false);
        gridView.addHeaderView(header2, null, false);

        View v = getLayoutInflater().inflate(R.layout.footer_menu, null);
        gridView.addFooterView(v);



        // Assign adapter to ListView
        //listView.setAdapter(adapter);
        mAdapter = new MyListAdapter(this, R.layout.grid_menu, some_list);
        gridView.setAdapter(mAdapter);


        //final int altura = gridView.getChildAt(0).getHeight();
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

        final int altura = (int)displayMetrics.heightPixels/17;

        gridView.setSelection(1);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                //gridView.smoothScrollToPosition(0);
                Log.v("handler","primeiro scroll automatico");

                gridView.smoothScrollBy(-altura,
                        500);
            }
        }, 1000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                //gridView.smoothScrollToPosition(1);
                Log.v("handler", "segundo scroll automatico");

                gridView.smoothScrollBy(altura,
                        500);
            }
        }, 3000);


        //initialisePagin();
        new AsyncTaskParseJsonEstrelas(this).execute();
        new AsyncTaskParseJsonGaleria(this).execute();
        new AsyncTaskParseJsonComentarios(this).execute();

    }

    public void abrirRestaurante(int index)
    {
        Intent myIntent = new Intent(Restaurante_main.this, MenuEspecial.class);
        //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
        myIntent.putExtra("rest_cartao_id", "" + some_list.get(index).getDb_id());
        myIntent.putExtra("rest_id", "" + some_list.get(index).getId_rest());
        myIntent.putExtra("hora_min_reserva", some_list.get(index).getHora_minimo_antedencia_especial());
        myIntent.putExtra("nome", some_list.get(index).getNome());

        startActivity(myIntent);

        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
    }



    public class MyListAdapter extends ArrayAdapter<Menu_do_restaurante> {

        Context myContext;
        public ImageLoader imageLoader;

        public MyListAdapter(Context context, int textViewResourceId, ArrayList<Menu_do_restaurante> objects) {
            super(context, textViewResourceId, objects);
            imageLoader=new ImageLoader(getApplicationContext());
            myContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;


            if (row == null)
                row=inflater.inflate(R.layout.grid_menu, parent, false);
            TextView label1=(TextView)row.findViewById(R.id.morada_rest);
            label1.setText(some_list.get(position*2).getNome());


            ImageView icon=(ImageView)row.findViewById(R.id.galeria_imagens);
            imageLoader.DisplayImage("http://menuguru.pt/"+some_list.get(position*2).getUrlImage(), icon);

            ImageView icon2 = (ImageView) row.findViewById(R.id.imagem_menu_2);
            TextView label2=(TextView)row.findViewById(R.id.lista_menus_restaurante_1);
            RelativeLayout rel = (RelativeLayout)row.findViewById(R.id.odd_view);
            RelativeLayout relsombra = (RelativeLayout)row.findViewById(R.id.odd_view_sombra);


            ImageView icon3 = (ImageView) row.findViewById(R.id.imagem_tipo1);
            ImageView icon4 = (ImageView) row.findViewById(R.id.imagem_tipo2);

            // tenho de criar o listener para quando clica na imagem abrir o especial
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ok aqui tenho de ver qual é o tipo selecionada


                    if(some_list.get(position*2).tipo.equalsIgnoreCase("menu_especial")) {

                        Intent myIntent = new Intent(Restaurante_main.this, MenuEspecial.class);
                        //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                        myIntent.putExtra("rest_cartao_id", "" + some_list.get(position * 2).getDb_id());
                        myIntent.putExtra("rest_id", "" + some_list.get(position * 2).getId_rest());
                        myIntent.putExtra("hora_min_reserva", some_list.get(position * 2).getHora_minimo_antedencia_especial());
                        myIntent.putExtra("nome", some_list.get(position * 2 ).getNome());

                        startActivity(myIntent);

                        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                    }else if(some_list.get(position*2).tipo.equalsIgnoreCase("menu_ementa"))
                    {
                        Intent myIntent = new Intent(Restaurante_main.this, Menu_ementa.class);
                        //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                        myIntent.putExtra("nome_cat_em", "" + some_list.get(position * 2).getNome());
                        myIntent.putExtra("rest_id", "" + rest_id);
                        myIntent.putExtra("url_foto", some_list.get(position *2).getUrlImage());

                        startActivity(myIntent);
                        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

                    }else if(some_list.get(position*2).tipo.equalsIgnoreCase("menu_dia"))
                    {
                        Intent myIntent = new Intent(Restaurante_main.this, Menu_diaria.class);
                        //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                        myIntent.putExtra("rest_id", "" + rest_id);
                        myIntent.putExtra("url_foto", some_list.get(position *2).getUrlImage());
                        myIntent.putExtra("nome", some_list.get(position * 2).getNome());

                        startActivity(myIntent);
                        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

                    }
                }
            });


            setiamgeTipo(position * 2, icon3);

            if (some_list.size() > (position * 2) +1) {

                imageLoader.DisplayImage("http://menuguru.pt/" + some_list.get((position * 2)+1).getUrlImage(), icon2);

                label2.setText(some_list.get(position*2 + 1).getNome());


                setiamgeTipo(position*2 + 1, icon4);
                rel.setVisibility(View.VISIBLE);
                relsombra.setVisibility(View.VISIBLE);

                icon2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ok aqui tenho de ver qual é o tipo selecionada

                        if(some_list.get(position*2 +1 ).tipo.equalsIgnoreCase("menu_especial"))
                        {
                            Intent myIntent = new Intent(Restaurante_main.this, MenuEspecial.class);
                            //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                            myIntent.putExtra("rest_cartao_id", "" + some_list.get(position * 2 + 1).getDb_id());
                            myIntent.putExtra("rest_id", "" + some_list.get(position * 2 + 1).getId_rest());
                            myIntent.putExtra("vem_de_especias", false);
                            myIntent.putExtra("hora_min_reserva", some_list.get(position * 2 +1).getHora_minimo_antedencia_especial());
                            myIntent.putExtra("nome", some_list.get(position * 2 + 1).getNome());

                            startActivity(myIntent);

                            overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                        }else if(some_list.get(position*2 +1).tipo.equalsIgnoreCase("menu_ementa"))
                        {
                            Intent myIntent = new Intent(Restaurante_main.this, Menu_ementa.class);
                            //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                            myIntent.putExtra("nome_cat_em", "" + some_list.get(position * 2+1).getNome());
                            myIntent.putExtra("rest_id", "" + rest_id);
                            myIntent.putExtra("url_foto", some_list.get(position *2 +1).getUrlImage());

                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
                        }else if(some_list.get(position*2+1).tipo.equalsIgnoreCase("menu_dia"))
                        {
                            Intent myIntent = new Intent(Restaurante_main.this, Menu_diaria.class);
                            //myIntent.putExtra("rest_id", some_array[position].getRestaurante());
                            myIntent.putExtra("rest_id", "" + rest_id);
                            myIntent.putExtra("url_foto", some_list.get(position *2+1).getUrlImage());
                            myIntent.putExtra("nome", some_list.get(position * 2+1).getNome());

                            startActivity(myIntent);
                            overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

                        }
                    }
                });


            }else
            {
                rel.setVisibility(View.GONE);
                relsombra.setVisibility(View.GONE);
            }

            return row;
        }

        public int getCount() {

            int odd = some_list.size()% 2;
            int div = some_list.size()/ 2;
            return div + odd;
        }

        private void setiamgeTipo(int position, ImageView image)
        {

            Menu_do_restaurante menu = some_list.get(position);

            if (menu.getTipo().equalsIgnoreCase("menu_especial")) {
                image.setVisibility(View.VISIBLE);
                if (menu.getPrecoNovo().length() != 0 && menu.getPrecoAntigo().length() != 0) {
                    image.setImageResource(R.drawable.antes_depois);
                } else if (!menu.getDesconto().equalsIgnoreCase("0")) {
                    image.setImageResource(R.drawable.desc_fatura);
                } else {
                    image.setImageResource(R.drawable.menu_esp);
                }
            }
            else
            {
                image.setVisibility(View.GONE);
            }

        }

    }

    // para verificar se o utilizador segue o restaurante ou nao
    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonVerificaSeguir extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao5/json_verifica_rest_fav_user.php";



        private Restaurante_main delegate;

        public AsyncTaskParseJsonVerificaSeguir (Restaurante_main delegate){
            this.delegate = delegate;
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

                User utilizador = Globals.getInstance().getUser();

                String id_face = "";
                String UserId = "";

                if (utilizador != null)
                {
                    id_face = utilizador.getId_face();
                    UserId = utilizador.getUserid();
                }else
                {
                    id_face = "0";
                    UserId = "0";
                }

                dict.put("rest_id", rest_id);
                dict.put("face_id", id_face);
                dict.put("user_id", UserId);

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng","resultado do seguir = "+ jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna galeria "+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String seque;
                seque = jsonObj.getString("resp");

                if(seque.equalsIgnoreCase("nao"))
                {
                    segue_rest = false;
                }else
                {
                    segue_rest = true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            // progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompleteVerificaSeguir(true);
        }

    }


    private void asyncCompleteVerificaSeguir(boolean success)
    {
        ImageView imageSegue = (ImageView) header2.findViewById(R.id.texto_fav_nome);
        // mudar a imagem do segui aqui
        if (segue_rest)
        {
            imageSegue.setImageResource(R.drawable.ic_emlista);
        }
        else
        {
            imageSegue.setImageResource(R.drawable.ic_lista);
        }

    }




    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonDiaLimite extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_mesa_dia_limite.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonDiaLimite (Restaurante_main delegate){
            this.delegate = delegate;
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

                dict.put("id_rest", rest_id);
                dict.put("data", data_selec);
                Log.v("REST_id", rest_id);
                Log.v("DATA",data_selec);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna galeria "+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);
                num_por_dia = outro.getString("num");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            // progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompletediaLimite(true);
        }

    }


    private void asyncCompletediaLimite(boolean success)
    {


    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonHorarioMesa extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao3/json_horario_reserva_mesa.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;
        JSONObject dataJsonRep = null;

        private Restaurante_main delegate;

        public AsyncTaskParseJsonHorarioMesa (Restaurante_main delegate){
            this.delegate = delegate;
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
                dict.put("rest_id", rest_id);
                Log.v("LANG", Globals.get_instance().getLingua());
                Log.v("REST_ID",rest_id);


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna HORARIO "+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users
                dataJsonRep = jsonObj.getJSONObject("res");

                dataJsonArr = dataJsonRep.getJSONArray("h_esp");
                hora_list = new ArrayList<Horario_Mesa>();

                for (int i = 0; i < dataJsonArr.length(); i++) {
                    JSONObject a = dataJsonArr.getJSONObject(i);

                    Horario_Mesa hora = new Horario_Mesa();
                    hora.setId(a.getString("id"));
                    hora.setHora_inicio(a.getString("hora_inicio"));
                    hora.setN_pessoas_h(a.getString("n_pessoas_h"));
                    hora.setDia_id(a.getString("dia_id"));
                    hora.setN_pessoas_min(a.getString("n_pessoas_min"));
                    hora_list.add(hora);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            // progressDialog.dismiss();
            if(delegate != null)
                delegate.asyncCompletediaHorario(true);
        }

    }


    private void asyncCompletediaHorario(boolean success)
    {


    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonReserva extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_reserva_mesa_criar.php";


        private Restaurante_main delegate;

        public AsyncTaskParseJsonReserva (Restaurante_main delegate){
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

                dict.put("rest_id",rest_id);
                dict.put("lang",Globals.get_instance().getLingua());
                dict.put("nome_cartao",some_list.get(0).getNome());
                dict.put("email_user",sel_email);
                dict.put("telefone",sel_telefone);
                dict.put("h_id",sel_id_hora);
                dict.put("hora",sel_hora);
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
                // res_reserva = jsonObj.getString("res");
                // msg_reserva = jsonObj.getString("msg");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            if(delegate != null)
                delegate.asyncCompleteReserva(true);  }

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
                    final Dialog dialog_final = new Dialog(Restaurante_main.this);
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
                    //bt_reserva.setVisibility(View.GONE);
                    //bt_voucher.setVisibility(View.VISIBLE);
                    dialog_final.show();

            }
        }, 7000);
    }


    public void partilharRestaurante()
    {
        String nomeRest_formatado = nome_rest.replace(" ","_");
        String sharing_message = nomeRest_formatado +"\n"+ morada + "\n" + telefone +"\nwww.menuguru.pt/restaurante_"+ rest_id + "/" + nome_rest ;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        // tenho de colocar o nome do restaurante a morada e o telefone seguido do url da menu guru
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharing_message);

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.o_que_pretende_pesquisar)));



    }

}
