package pt.menuguru.menuguru6;

/**
 * Created by hugocosta on 06/08/14.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Paresh N. Mayani
 * @Website http://www.technotalkative.com
 */
public class ReadFileAssetsActivity extends Activity {

    /** Called when the activity is first created. */


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_read_file);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Sobre a Menu Guru");
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView txtContent = (TextView) findViewById(R.id.txtContent);
        TextView txtFileName = (TextView) findViewById(R.id.txtFileName);
        ImageView imgAssets = (ImageView) findViewById(R.id.imgAssets);

        //AssetManager assetManager = getAssets();
        //txtContent.setText(R.string.sobre_menuguru);

        Intent intent = getIntent();
        String message = intent.getStringExtra("menssagem");
        txtContent.setText(message);

        return;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return false;
            default:
                break;
        }

        return false;
    }
}