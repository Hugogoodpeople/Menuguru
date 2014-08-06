package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import pt.menuguru.menuguru6.R;

public class Localizacao extends Activity {
    ListView listView;
    String[] local = new String[]{"Perto de mim",
            "Vizela",
            "Porto",
            "Braga",
            "Guimarães",
            "Bragança",
            "Lisboa",
            "Aveiro"
    };
    public String value;

    SearchView inputSearch;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);
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
        inputSearch = (SearchView) findViewById(R.id.searchView);


        listView = (ListView) findViewById(R.id.listV_localizacao);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, local);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                Intent myIntent = new Intent(Localizacao.this, MainActivity.class);
                myIntent.putExtra("local", itemValue); //Optional parameters
                Localizacao.this.startActivity(myIntent);
                finish();

            }

        });
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(Localizacao.this, MainActivity.class);
                myIntent.putExtra("local", value);
                Localizacao.this.startActivity(myIntent);
                finish();

                return false;
            default:
                break;
        }

        return false;
    }

}
