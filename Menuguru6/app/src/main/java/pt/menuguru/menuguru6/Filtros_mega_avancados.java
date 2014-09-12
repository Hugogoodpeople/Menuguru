package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * Created by hugocosta on 11/09/14.
 */
public class Filtros_mega_avancados extends Activity
{

    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private ImageButton button6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_avancada);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        button1 = (ImageButton) this.findViewById(R.id.buttonfiltros1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 1");
                highlightButton(1);
            }
        });

        button2 = (ImageButton) this.findViewById(R.id.buttonfiltros2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 2");
                highlightButton(2);
            }
        });

        button3 = (ImageButton) this.findViewById(R.id.buttonfiltros3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 3");
                highlightButton(3);
            }
        });

        button4 = (ImageButton) this.findViewById(R.id.buttonfiltros4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 4");
                highlightButton(4);
            }
        });

        button5 = (ImageButton) this.findViewById(R.id.buttonfiltros5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 5");
                highlightButton(5);
            }
        });

        button6 = (ImageButton) this.findViewById(R.id.buttonfiltros6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("filtros","click Button 6");
                highlightButton(6);
            }
        });

        highlightButton(1);

    }

    private void highlightButton(int selectedButton)
    {

        button1.setImageResource(R.drawable.ico_ordem_1);
        button2.setImageResource(R.drawable.ico_cozinha);
        button3.setImageResource(R.drawable.ico_preco);
        button4.setImageResource(R.drawable.ico_opcoes);
        button5.setImageResource(R.drawable.ico_ideal);
        button6.setImageResource(R.drawable.ico_ambiente);


        switch (selectedButton)
        {
            case 1:
            {
                button1.setImageResource(R.drawable.ico_ordem_2);
                break;
            }
            case 2:
            {
                button2.setImageResource(R.drawable.ico_cozinha_2);
                break;
            }
            case 3:
            {
                button3.setImageResource(R.drawable.ico_preco_2);
                break;
            }
            case 4:
            {
                button4.setImageResource(R.drawable.ico_opcoes_2);
                break;
            }
            case 5:
            {
                button5.setImageResource(R.drawable.ico_ideal_2);
                break;
            }
            case 6:
            {
                button6.setImageResource(R.drawable.ico_ambiente_2);
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inspiracao, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Intent myIntent = new Intent(Inspiracao.this, MainActivity.class);
                //startActivity(myIntent);
                finish();

                return false;
            default:
                break;
        }

        return false;
    }






}
