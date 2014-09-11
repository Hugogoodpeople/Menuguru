package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Locale;

import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.Locais;

/**
 * Created by hugocosta on 11/09/14.
 */
public class LanguageMenu extends ActivityLogin
{
    //language_list
    ListView listView;



    public String value;

    SearchView inputSearch;

    Locale myLocale;

    String[] some_array = null;


    private static MyListAdapter mAdapter;


    public class MyListAdapter extends ArrayAdapter<String> {

        Context myContext;

        public MyListAdapter(Context context, int textViewResourceId,
                             String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater =(LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.row_defenicoes, parent, false);
            TextView label=(TextView)row.findViewById(R.id.month);
            label.setText(some_array[position]);

            return row;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        Intent intent = getIntent();
        value = intent.getStringExtra("local");
        //setTitle(value);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setCustomView(R.layout.tab_header);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView t =(TextView) findViewById(R.id.mytext);
        t.setText(value);




        //mAdapter.notifyDataSetChanged();
        //mAdapter = new MyListAdapter(getApplicationContext(), R.layout.row_defenicoes, some_array);


        some_array = getResources().getStringArray(R.array.linguas);
        listView = (ListView) findViewById(R.id.language_list);
        //adapter = new ArrayAdapter<Locais>(this,android.R.layout.simple_list_item_1, android.R.id.text1, local);

        // Assign adapter to ListView
        //listView.setAdapter(adapter);
        mAdapter = new MyListAdapter(this, R.layout.row_defenicoes, some_array);
        listView.setAdapter(mAdapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

               // tenho de colocar nas globais a lingua selecionada

                switch (position)
                {
                    case 0:
                    {
                        setLocale("pt");
                        break;
                    }
                    case 1:
                    {
                        setLocale("it");
                        break;
                    }
                    case 2:
                    {
                        setLocale("de");
                        break;
                    }
                    case 3:
                    {
                        setLocale("es");
                        break;
                    }
                    case 4:
                    {
                        setLocale("fr");
                        break;
                    }
                    case 5:
                    {
                        setLocale("en");
                        break;
                    }


                }


                fechar();

            }

        });


    }


    public void fechar()
    {
        finish();
        this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);

    }



    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        // esta parte serve para refrescar a propria actividade criando uma nova
        /*
        Intent refresh = new Intent(this, LanguageMenu.class);
        startActivity(refresh);
        */
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*
                Intent myIntent = new Intent(LanguageMenu.this, MainActivity.class);
                myIntent.putExtra("local", value);
                LanguageMenu.this.startActivity(myIntent);
                */
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }






}
