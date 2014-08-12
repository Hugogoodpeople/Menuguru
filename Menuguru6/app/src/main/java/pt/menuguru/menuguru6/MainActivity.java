package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;



import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;




public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,Inicio.Callbacks {


    // para o fragmento... nao gosto muito mas por enquanto é o que se arranja
    Fragment framgmentoPrincipal;
    private static boolean framgmentoPrincipalVisivel= false;

    @Override
    public void onButtonClicked() {

        if (framgmentoPrincipalVisivel == false)
        {
           // android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("pagina_principal");
            android.support.v4.app.FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(framgmentoPrincipal);
            fragTransaction.attach(framgmentoPrincipal);
            fragTransaction.commit();

            framgmentoPrincipalVisivel = true;
        }

    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public void setActionBarTitle(String title) {
        setTitle(title);
    }

    public void asyncComplete(boolean success){

        //refreshSthHere();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, new MyFragment());
        fragmentTransaction.commit();
        Log.v("csdfsrgnn","ja passaram os 5");

    }
    

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();


        switch (position) {
            case 0: {
                //transaction.replace(R.id.container, new Inicio());
                Fragment tabbed = new MyFragment();
                framgmentoPrincipal = tabbed;

                fragmentTransaction.replace(R.id.container, tabbed);
                break;
            }
            case 1: {
                fragmentTransaction.replace(R.id.container, new Reservas());
                break;
            }
            case 2:
            {
                fragmentTransaction.replace(R.id.container,new DefenicoesTesteFragment());
                break;
            }
            case 3: {
                fragmentTransaction.replace(R.id.container, new Destaques());
                break;
            }
            case 4: {
                fragmentTransaction.replace(R.id.container, new ComoFunciona());
                break;
            }
        }

// Commit the transaction
        fragmentTransaction.commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.inicio);
                break;
            case 2:
                mTitle = getString(R.string.reservas);
                break;
            case 3:
                mTitle = getString(R.string.definicao);
                break;
            case 4:
                mTitle = getString(R.string.destaque);
                break;
            case 5:
                mTitle = getString(R.string.como);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.activity_read_file, menu);
                restoreActionBar();
                return true;
            }
            return super.onCreateOptionsMenu(menu);
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }


    }



}
