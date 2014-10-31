package pt.menuguru.playstore;

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

import com.google.android.gms.analytics.GoogleAnalytics;

import pt.menuguru.menuguru.R;

/**
 * @author Paresh N. Mayani
 * @Website http://www.technotalkative.com
 */
public class ReadFileAssetsActivity extends Activity {

    /** Called when the activity is first created. */


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

        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_read_file);

        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_left_b);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Sobre a Menu Guru");

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
            case android.R.id.home: {
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);


                return false;
            }
            default:
                break;
        }

        return false;
    }
}