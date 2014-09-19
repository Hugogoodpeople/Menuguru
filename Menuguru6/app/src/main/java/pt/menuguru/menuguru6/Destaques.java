package pt.menuguru.menuguru6;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pt.menuguru.menuguru6.Utils.ComoFunc;
import pt.menuguru.menuguru6.Utils.Festival;
import pt.menuguru.menuguru6.Utils.Globals;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class Destaques extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = Globals.get_instance().getFestival().length;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    Festival[] some_array = null;
    ArrayList<Festival> como;


    Button bt_close;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new AsyncTaskParseJson(this).execute();
        if(Globals.get_instance().getFestival().length<=0){
            setContentView(R.layout.fragment_destaques_vazia);
            TextView text = (TextView)findViewById(R.id.textView_nao);
            text.setText(R.string.nao_encontramos);
            bt_close = (Button)findViewById(R.id.bt_close_fest);
            bt_close.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                    overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                }
            });
        }else{
            setContentView(R.layout.fragment_como_funciona);
            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When changing pages, reset the action bar actions since they are dependent
                    // on which page is currently active. An alternative approach is to have each
                    // fragment expose actions itself (rather than the activity exposing actions),
                    // but for simplicity, the activity provides the actions in this sample.
                    invalidateOptionsMenu();
                }
            });

            bt_close = (Button)findViewById(R.id.bt_close);
            bt_close.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                    overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                }
            });
        }

        getActionBar().hide();
        // Instantiate a ViewPager and a PagerAdapter.

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                //NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            return Destaques2.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



}
