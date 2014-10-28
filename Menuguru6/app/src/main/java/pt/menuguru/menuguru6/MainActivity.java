package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import pt.menuguru.menuguru6.Json_parser.JSONParser;
import pt.menuguru.menuguru6.Utils.Globals;
import pt.menuguru.menuguru6.Utils.User;


public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener {


    // para o fragmento... nao gosto muito mas por enquanto é o que se arranja
    Fragment framgmentoPrincipal;

    private LocationManager locationManager;
    private String provider;
    public String latitude;
    public String longitude;


    SharedPreferences preferences;

    // para as pushup notifications
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "271963050754";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";


    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;


    public void ReloadoTabs()
    {
           // android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("pagina_principal");
            android.support.v4.app.FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(framgmentoPrincipal);
            fragTransaction.attach(framgmentoPrincipal);
            fragTransaction.commit();
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

        Globals.getInstance();

        // para ir buscar as preferencias de utilizador
        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String lingua = preferences.getString("lingua", "");
        String user_name = preferences.getString("user_name","");
        String user_lastName = preferences.getString("user_last_name", "");
        String user_id = preferences.getString("user_id","");
        String user_aniversario = preferences.getString("user_data","");
        String user_telf = preferences.getString("user_tel","");
        String user_mail = preferences.getString("user_mail","");
        String user_tipo_conta = preferences.getString("user_tipo","");
        String user_password = preferences.getString("user_pass","");
        String user_cidade = preferences.getString("user_cidade","");
        String face_id = preferences.getString("id_face","");
        Boolean user_news = preferences.getBoolean("user_news", false);

        if (!user_id.equals("") || !face_id.equals(""))
        {
            User u = new User();
            u.setUserid(user_id);
            u.setData_nasc(user_aniversario);
            u.setPnome(user_name);
            u.setSnome(user_lastName);
            u.setEmail(user_mail);
            u.setTipoconta(user_tipo_conta);
            u.setTelefone_user(user_telf);
            u.setPass(user_password);
            u.setCidade(user_cidade);
            u.setNews(user_news);
            u.setId_face(face_id);

            Globals.getInstance().setLingua(lingua);

            Globals.getInstance().setUser(u);

        }

        if(!lingua.equalsIgnoreCase(""))
        {
            setLocale(lingua);
        }
        else
        {
            setLocale("pt");
        }
        //Globals.getInstance().setCidadeÇ_nome(getString(R.string.perto_de_mim));


        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        locationManager.requestLocationUpdates(provider, 0, 0, this);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            longitude = "Location not available";
            latitude = "Location not available";
        }

        Log.v("geolocsdfsdgf","A minha geolocalização lat= "+ latitude);
// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled)
        {
           // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
           // startActivity(intent);
            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, "Active a geolocalização", duration);
            toast.show();
        }


        //notify(null);

        context = getApplicationContext();

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            Globals.getInstance().setDeviceID(regid);


            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }





    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("semregisto", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("versaomudou", "App version changed.");
            return "";
        }
        return registrationId;
    }


    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTaskNotification(this).execute();

    }

    public class AsyncTaskNotification extends AsyncTask<String, String, String> {
        private MainActivity delegate;

        public AsyncTaskNotification (MainActivity delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;

                Globals.getInstance().setDeviceID(regid);

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(delegate, strFromDoInBg, duration);
            //mDisplay.append(strFromDoInBg + "\n");
            delegate.asysnkCompleteNotificacao();
            }
    }


    public void asysnkCompleteNotificacao()
    {
        // tenho de chamar aqui um novo webservice para poder enviar este novo id para a base de dados
        new AsyncTaskRegistarDispositivo(this).execute();

    }

    public class AsyncTaskRegistarDispositivo extends AsyncTask<String, String, String> {
        private MainActivity delegate;


        // este webservice precisa de ser trocado por um novo destinado a android
        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/json_add_android_device.php";

        public AsyncTaskRegistarDispositivo (MainActivity delegate){
            if(delegate != null)
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();

                dict.put("id_dispositivo", regid);

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                try {
                    jsonObj = new JSONObject(jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data [" + e.getMessage()+"] "+jsonString);
                }

                Log.v("JsonObject","resultado do registo do dispositovo = "+ jsonObj);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            if(delegate != null)
                delegate.asysnkCompleteRegistoDispositivo();
        }
    }

    public void asysnkCompleteRegistoDispositivo()
    {
        // aqui dentro não preciso de fazer nada

    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        // esta parte serve para refrescar a propria actividade criando uma nova

        /*
        finish();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        */

        Globals.getInstance().setLingua(lang);

/*
        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lingua", lang);
        editor.commit();
        */
    }


   @Override
   public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        longitude = String.valueOf(lng);
        latitude = String.valueOf(lat);
        Log.v("geolocsdfsdgf","A minha geolocalização changed "+ latitude);
        locationManager.removeUpdates(this);

        Globals.getInstance().setLatitude(latitude);
        Globals.getInstance().setLongitude(longitude);

       // onButtonClicked();
   }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.v("geolocsdfsdgf","A minha geolocalização status "+ latitude);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
        Log.v("geolocsdfsdgf","A minha geolocalização enable "+ latitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        Log.v("geolocsdfsdgf", "A minha geolocalização disable " + latitude);
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
        Log.v("csdfsrgnn", "ja passaram os 5");

    }
    

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();


        switch (position) {
            // por algum motivo depois de adicionar o header quando clico em algum elemento ele passa +1 a frente
            case 0: {
                //transaction.replace(R.id.container, new Inicio());
                if (framgmentoPrincipal == null) {
                    Fragment tabbed = new MyFragment();
                    framgmentoPrincipal = tabbed;
                }
                fragmentTransaction.replace(R.id.container, framgmentoPrincipal);
                break;
            }
            case 1: {
                //transaction.replace(R.id.container, new Inicio());
                if (framgmentoPrincipal == null) {
                    Fragment tabbed = new MyFragment();
                    framgmentoPrincipal = tabbed;
                }
                fragmentTransaction.replace(R.id.container, framgmentoPrincipal);
                break;
            }
            case 2: {
                fragmentTransaction.replace(R.id.container, new MinhasReservas());
                break;
            }
            case 3:
            {
                fragmentTransaction.replace(R.id.container,new Defenicoes());
                break;
            }
            case 4: {
                Intent intent = new Intent(this, Destaques.class);
                startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(this, ComoFunciona.class);
                startActivity(intent);
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
