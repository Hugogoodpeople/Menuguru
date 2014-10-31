package pt.menuguru.menuguru.Mapa;

/**
 * Created by hugocosta on 15/10/14.
 */

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;

import pt.menuguru.menuguru.R;
import pt.menuguru.menuguru.Utils.Utils;

public class Mapa extends FragmentActivity {

    GoogleMap mMap;
    LatLng coordenadas;
    String rest_name;

    String latitude;
    String longitude;

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
        setContentView(R.layout.activity_map);



        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        // tenho de ir buscar as coordenadas ao intent
        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude= intent.getStringExtra("longitude");
        rest_name = intent.getStringExtra("nome");
        String url_foto = "http://www.menuguru.pt" + intent.getStringExtra("foto");


        MapsInitializer.initialize(this);

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng( Double.parseDouble(latitude),
                        Double.parseDouble(longitude)));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        coordenadas = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));



        // tenho de meter esta mascara



        new DownloadImageTask(this)
                .execute(url_foto);





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                this.overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);

                return false;
            default:
                break;
        }

        return false;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        Mapa delegado;

        public DownloadImageTask(Mapa contexto) {
            delegado = contexto;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error carregamento da imagem", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
           // bmImage.setImageBitmap(result);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            int tamanhoCerto = width/4;

            Canvas canvas = new Canvas();
            Bitmap mainImage = Utils.getResizedBitmap( result, tamanhoCerto, tamanhoCerto ); //get original image

            //Bitmap mainImage = BitmapFactory.decodeResource(delegado.getResources(), R.drawable.sem_foto);
            Bitmap maskImage = BitmapFactory.decodeResource(delegado.getResources(), R.drawable.pin_map); //get mask image
            maskImage = Utils.getResizedBitmap( maskImage, tamanhoCerto, tamanhoCerto );
            Bitmap resultado = Bitmap.createBitmap(tamanhoCerto, tamanhoCerto, Bitmap.Config.ARGB_8888);

            canvas.setBitmap(resultado);
            Paint paint = new Paint();
            paint.setFilterBitmap(false);

            canvas.drawBitmap(mainImage, 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
            canvas.drawBitmap(maskImage, 0, 0, paint);
            paint.setXfermode(null);

            resultado = Utils.addShadow(resultado, resultado.getHeight(), resultado.getWidth(), Color.BLACK, 4, 1, 5);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(rest_name)
                    .icon(BitmapDescriptorFactory.fromBitmap(resultado)));

           mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
               @Override
               public boolean onMarkerClick(Marker marker) {

                   /*
                   Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                           Uri.parse("http://maps.google.com/maps?saddr="+ latitude +","+ longitude +
                                   "&daddr="+ Globals.getInstance().getLatitude()+","+Globals.getInstance().getLongitude()));
                   startActivity(intent);
                    */

                   Intent nav = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + " , " + longitude + ""));
                   startActivity(nav);


                   return false;
               }
           });

        }
    }



}