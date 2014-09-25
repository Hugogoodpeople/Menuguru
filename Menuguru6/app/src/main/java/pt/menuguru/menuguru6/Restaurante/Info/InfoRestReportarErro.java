package pt.menuguru.menuguru6.Restaurante.Info;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import pt.menuguru.menuguru6.R;

/**
 * Created by hugocosta on 25/09/14.
 */
public class InfoRestReportarErro extends Activity
{
    private String rest_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle(getString(R.string.reportar_erro));

        Intent intent = this.getIntent();
        rest_id = intent.getStringExtra("restaurante_id");


        setContentView(R.layout.activity_reportar_erro);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }

}
