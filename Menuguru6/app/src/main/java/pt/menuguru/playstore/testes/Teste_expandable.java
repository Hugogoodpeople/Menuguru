package pt.menuguru.playstore.testes;

/**
 * Created by hugocosta on 24/10/14.
 */

    import android.app.ActionBar;
    import android.app.Dialog;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.graphics.drawable.ColorDrawable;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.app.Activity;
    import android.content.Context;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.Window;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ExpandableListView;
    import android.widget.ExpandableListView.OnGroupClickListener;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.analytics.GoogleAnalytics;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.List;

    import pt.menuguru.playstore.Utils.ImageLoader;
    import pt.menuguru.playstore.Inspiracoes.Resultado_inspiracao;
    import pt.menuguru.playstore.Json_parser.JSONParser;
    import pt.menuguru.playstore.MyApplication;
    import pt.menuguru.menuguru.R;
    import pt.menuguru.playstore.Utils.Globals;
    import pt.menuguru.playstore.Utils.Utils;

/**
     * This is an example usage of the AnimatedExpandableListView class.
     *
     * It is an activity that holds a listview which is populated with 100 groups
     * where each group has from 1 to 100 children (so the first group will have one
     * child, the second will have two children and so on...).
     */
public class Teste_expandable extends Activity implements ExpandableListView.OnChildClickListener {
        private AnimatedExpandableListView listView;
        private ExampleAdapter adapter;

        private List<GroupItem> headerItens;

        private int dia_selecionado= -1;
        private int ref_selecionado= -1;
        private ProgressDialog progressDialog;

    private Dialog dialog;
    private ListView listDias;
    ExpandableListView expListView;




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
            setContentView(R.layout.actividade_texte);

            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


            // este codigo funciona apenas para quando a lingua do
            // dispositivo é igual a lingua escolhida pelo utilizador

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

        /*
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        */

            String dayOfTheWeek = getResources().getStringArray(R.array.dias_semana)[day - 1];

            Calendar cc = Calendar.getInstance();
            cc.setTime(new Date());
            dia_selecionado = cc.get(Calendar.DAY_OF_WEEK) -1;
            int hora = cc.get(Calendar.HOUR_OF_DAY);

            String refeicao;

            if (hora >= 17)
            {
                refeicao =  getResources().getStringArray(R.array.refeicoes)[1];
                ref_selecionado = 1;
            }
            else
            {
                refeicao =  getResources().getStringArray(R.array.refeicoes)[0];
                ref_selecionado = 0;
            }

            Button p1_button = (Button)findViewById(R.id.button_data);
            p1_button.setText(dayOfTheWeek + " " + refeicao);

            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_inspiras);

            listDias = (ListView) dialog.findViewById(R.id.listView_dias);

            AdapterStringDias adapterString = new AdapterStringDias(this, R.layout.list_spiner,  getResources().getStringArray(R.array.dias_semana));

            listDias.setAdapter(adapterString);

            listDias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for(int a = 0; a < parent.getChildCount(); a++)
                    {
                        parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.dourado));
                    //sel_nr_pes = nr_pes_list.get(position).getNr();
                    dia_selecionado = position;
                }
            });


            ListView listrefs = (ListView) dialog.findViewById(R.id.listView_refeicao);

            AdapterStringRef adapterStringRef = new AdapterStringRef(this, R.layout.list_spiner,  getResources().getStringArray(R.array.refeicoes));

            listrefs.setAdapter(adapterStringRef);

            listrefs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for(int a = 0; a < parent.getChildCount(); a++)
                    {
                        parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.white) );
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.dourado));
                    //sel_nr_pes = nr_pes_list.get(position).getNr();
                    ref_selecionado = position;
                }
            });


            Button buttonGravar = (Button) dialog.findViewById(R.id.button_gravar_inspiras);
            buttonGravar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // tenho de colocar o selecionado na dialog no botao
                    Button p1_button = (Button)findViewById(R.id.button_data);
                    p1_button.setText( getResources().getStringArray(R.array.dias_semana)[dia_selecionado] + " " +  getResources().getStringArray(R.array.refeicoes)[ref_selecionado]);
                    //locButton.startAnimation(AnimationUtils.loadAnimation(Activity_Inspiracao.this, R.anim.rodar));
                    chamarInspiras();
                    dialog.dismiss();
                }
            });


            p1_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.show();
                    fazerScroll();
                }
            });





            if(!Utils.isOnline(this)) {
                Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
                finish(); //Calling this method to close this activity when internet is not available.
            }
            else
                chamarInspiras();
        }

    public void chamarListaRestaurantes(int groupPosition, int childPosition)
    {
        ChildItem childText = (ChildItem) adapter.getChild(groupPosition, childPosition);
        Log.v("child selected",  childText.getTitle());

        Intent intent = new Intent(this, Resultado_inspiracao.class);
        intent.putExtra("id_item",childText.getDb_id());
        intent.putExtra("local",childText.getTitle());
        startActivity(intent);

        overridePendingTransition(R.anim.push_view1, R.anim.push_view2);

    }

    public void fazerScroll()
    {
        listDias.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listDias.setSelection(dia_selecionado-1);
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
        return false;
    }

    private static class GroupItem {
            String title;


            String urlImagem;
            List<ChildItem> items = new ArrayList<ChildItem>();

            public void setUrlImagem(String urlImagem) {
                this.urlImagem = urlImagem;
            }

            public String getUrlImagem() {
                return urlImagem;
            }

        }

        private static class ChildItem {

            private String title;
            private String hint;
            private String db_id;

            public void setNome(String nome) {
                this.title = nome;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setDb_id(String db_id) {
                this.db_id = db_id;
            }

            public String getDb_id() {
                return db_id;
            }
        }

        private static class ChildHolder {
            TextView title;
            TextView hint;
        }

        private static class GroupHolder {
            TextView title;
        }

        /**
         * Adapter for our list of {@link GroupItem}s.
         */
        private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
            private LayoutInflater inflater;

            private List<GroupItem> items;
            public ImageLoader imageLoader;


            public ExampleAdapter(Context context) {
                inflater = LayoutInflater.from(context);
                imageLoader = new ImageLoader(context);
            }

            public void setData(List<GroupItem> items) {
                this.items = items;

            }

            @Override
            public ChildItem getChild(int groupPosition, int childPosition) {
                return items.get(groupPosition).items.get(childPosition);
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                ChildHolder holder;
                ChildItem item = getChild(groupPosition, childPosition);
                if (convertView == null) {
                    holder = new ChildHolder();
                    convertView = inflater.inflate(R.layout.list_item, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.lblListItem);
                    // holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                    convertView.setTag(holder);
                } else {
                    holder = (ChildHolder) convertView.getTag();
                }

                holder.title.setText(item.title);
                //holder.hint.setText(item.hint);

                return convertView;
            }

            @Override
            public int getRealChildrenCount(int groupPosition) {
                return items.get(groupPosition).items.size();
            }

            @Override
            public GroupItem getGroup(int groupPosition) {
                return items.get(groupPosition);
            }

            @Override
            public int getGroupCount() {
                return items.size();
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                GroupHolder holder;
                GroupItem item = getGroup(groupPosition);
                if (convertView == null) {
                    holder = new GroupHolder();
                    convertView = inflater.inflate(R.layout.row_inspiracao, parent, false);
                    holder.title = (TextView) convertView.findViewById(R.id.nomeInspiracao);
                    convertView.setTag(holder);

                } else {
                    holder = (GroupHolder) convertView.getTag();
                }

                ImageView imagem=(ImageView)convertView.findViewById(R.id.imagem_inspiracao);
                imageLoader.DisplayImage("http://menuguru.pt/"+item.getUrlImagem(), imagem);

                holder.title.setText(item.title);

                return convertView;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public boolean isChildSelectable(int arg0, int arg1) {
                return true;
            }

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inspiracao, menu);
        // ImageButton locButton = (ImageButton) menu.findItem(R.id.action_actualizar_inspiras).getActionView();
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Intent myIntent = new Intent(Inspiracao.this, MainActivity.class);
                //startActivity(myIntent);
                finish();

                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            case R.id.action_actualizar_inspiras:

                new AsyncTaskParseJson(this).execute();

                return false;
            default:
                break;
        }

        return false;
    }




    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        private Teste_expandable delegate;

        // set your json string url here
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_inspiracao_mostrar.php";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        public AsyncTaskParseJson (Teste_expandable delegate)
        {
            this.delegate = delegate;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(delegate);
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

                // para a refeiçao escolhida
                String refeicao;
                if (ref_selecionado == 0)
                    refeicao = "almoco";
                else
                    refeicao = "jantar";

                // para o dia da semana selecionado
                int dia_semana;
                if (dia_selecionado == 0) {
                    dia_semana = 7;
                } else {
                    dia_semana = dia_selecionado;
                }


                dict.put("lang", Globals.getInstance().getLingua());
                dict.put("almajant", refeicao);
                dict.put("diasemana", dia_semana);


                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl, dict);

                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ", "Ele retorna isto" + jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                dataJsonArr = jsonObj.getJSONArray("inspiracao");

                // loop through all users



                headerItens = new ArrayList<GroupItem>();


                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    GroupItem itemHeader = new GroupItem();
                    itemHeader.title = c.getString("tituloinsp");
                    itemHeader.setUrlImagem(c.getString("imagem"));

                    // tenho de fazer um ciclo

                    JSONArray items = c.getJSONArray("subtitulos");

                    for (int z = 0; z < items.length(); z++) {
                        JSONObject item = items.getJSONObject(z);
                        ChildItem child = new ChildItem();
                        child.setNome(item.getString("subtitulo"));
                        child.setDb_id(item.getString("subtituloid"));


                        itemHeader.items.add(child);
                    }

                   // listDataChild.put(listDataHeader.get(i), top250); // Header, Child data

                    headerItens.add(itemHeader);

                }


                //Log.v("sdffgddvsdsv","objecto = "+ json);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            progressDialog.dismiss();
            delegate.asyncComplete(true);
        }
    }


    public void asyncComplete(boolean success)
    {
        adapter = new ExampleAdapter(this);
        adapter.setData(headerItens);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });


        listView.setOnChildClickListener( new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                chamarListaRestaurantes(groupPosition, childPosition);
                return false;
            }
        });

    }

    public class AdapterStringDias extends ArrayAdapter<String> {

        Context myContext;
        public String[] some_list;

        public AdapterStringDias(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            this.some_list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.list_spiner, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.textSpiner);
            textView.setText(some_list[position]);


            if (position == dia_selecionado)
                row.setBackgroundColor(getResources().getColor(R.color.dourado));
            else
                row.setBackgroundColor(getResources().getColor(R.color.white));


            return row;
        }

    }


    public class AdapterStringRef extends ArrayAdapter<String> {

        Context myContext;
        public String[] some_list;

        public AdapterStringRef(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
            this.some_list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = convertView;

            if (row == null)
                row=inflater.inflate(R.layout.list_spiner, parent, false);

            TextView textView = (TextView) row.findViewById(R.id.textSpiner);
            textView.setText(some_list[position]);


            if (position == ref_selecionado)
                row.setBackgroundColor(getResources().getColor(R.color.dourado));
            else
                row.setBackgroundColor(getResources().getColor(R.color.white));


            return row;
        }

    }

    private void chamarInspiras()
    {
        new AsyncTaskParseJson(this).execute();
    }


}
