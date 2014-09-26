package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.model.GraphUser;

import java.util.Locale;

import pt.menuguru.menuguru6.Utils.Globals;

/**
 * Created by hugocosta on 11/09/14.
 */
public class EscolherLingua extends Activity
{
    //language_list
    ListView listView;

    SharedPreferences preferences;

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
            View row;
            if (convertView == null)
                row=inflater.inflate(R.layout.row_defenicoes, parent, false);
            else
                row= convertView;

            TextView label=(TextView)row.findViewById(R.id.month);
            label.setText(some_array[position]);



                ImageView icon1 = (ImageView) row.findViewById(R.id.icon);
                //parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.silver) );
                icon1.setImageResource(0);


                String lingua = preferences.getString("lingua", "");

                if (lingua.equalsIgnoreCase("pt") && position == 0)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }else if (lingua.equalsIgnoreCase("it") && position == 1)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }else if (lingua.equalsIgnoreCase("de") && position == 2)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }else if (lingua.equalsIgnoreCase("es") && position == 3)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }else if (lingua.equalsIgnoreCase("fr") && position == 4)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }else if (lingua.equalsIgnoreCase("en") && position == 5)
                {
                    icon1.setImageResource(R.drawable.ic_check_b);
                }


            return row;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.idioma);

        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

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

                for(int a = 0; a < parent.getChildCount(); a++)
                {
                    ImageView icon1 = (ImageView) parent.getChildAt(a).findViewById(R.id.icon);
                    //parent.getChildAt(a).setBackgroundColor(getResources().getColor(R.color.silver) );
                    icon1.setImageResource(0);
                }

                //view.setBackgroundColor(Color.RED);


                ImageView icon2 = (ImageView) view.findViewById(R.id.icon);
                //Customize your icon here
                icon2.setImageResource(R.drawable.ic_check_b);

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
              //  fechar();
            }

        });


        //--READ data

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

        Globals.getInstance().setLingua(lang);


        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lingua", lang);
        editor.commit();
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
