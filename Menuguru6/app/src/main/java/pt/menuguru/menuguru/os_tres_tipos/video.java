package pt.menuguru.menuguru.os_tres_tipos;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import pt.menuguru.menuguru.Json_parser.JSONParser;
import pt.menuguru.menuguru.MyApplication;
import pt.menuguru.menuguru.R;

/**
 * Created by hugocosta on 16/10/14.
 */
public class video extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,View.OnTouchListener{

    private VideoView mVV;
    private String id_rest;
    private String videoUrl;

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

        setContentView(R.layout.video);

        int fileRes=0;
        Bundle e = getIntent().getExtras();
        if (e!=null) {
            fileRes = e.getInt("fileRes");
        }

        mVV = (VideoView)findViewById(R.id.myvideoview);
        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);
        mVV.setOnTouchListener(this);


        Intent intent = getIntent();
        id_rest = intent.getStringExtra("id_rest");


        new AsyncTaskParseJsonVideo(this).execute();


    }

    private boolean playFileRes(int fileRes) {
        if (fileRes==0) {
            stopPlaying();
            return false;
        } else {
            //mVV.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + fileRes));
            return true;
        }
    }



    public void stopPlaying() {
        mVV.stopPlayback();
        this.finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        stopPlaying();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
                return false;
            default:
                break;
        }

        return false;
    }


    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJsonVideo extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";


        String yourJsonStringUrl = "http://menuguru.pt/menuguru/webservices/data/versao4/json_video_abrir.php";


        JSONObject dataJason = null;

        private video delegate;

        public AsyncTaskParseJsonVideo (video delegate){
            this.delegate = delegate;
        }


        @Override
        protected String doInBackground(String... arg0) {

            try {
                // instantiate our json parser
                JSONParser jParser = new JSONParser();

                // get json string from url
                // tenho de criar um jsonobject e adicionar la as cenas
                JSONObject dict = new JSONObject();
                JSONObject jsonObj = new JSONObject();



                dict.put("id_video",id_rest);
                dict.put("lang","pt");

                // tenho de enviar lat, long, data, hora, cidade, lang

                String jsonString = jParser.getJSONFromUrl(yourJsonStringUrl,dict);

                Log.v("jfgrhng", "resultado do video = " + jsonString);


                // try parse the string to a JSON object
                try {
                    Log.v("Ver Json ","Ele retorna para o video"+jsonString);
                    jsonObj = new JSONObject(jsonString);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing data " + e.toString());
                }
                // get the array of users

                String completo = jsonObj.getString("res");
                JSONObject outro =new JSONObject(completo);


                // loop through all users



                videoUrl = outro.getString("urlvideo");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg)
        {
            //progressDialog.dismiss();
            delegate.asyncCompleteVideo(true);
        }

    }

    public void asyncCompleteVideo(boolean success)
    {
        /*
        mVV.setVideoURI(Uri.parse(videoUrl));

        mVV.start();
        */

        String LINK = videoUrl;
        setContentView(R.layout.video);
        VideoView videoView =(VideoView)findViewById(R.id.myvideoview);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(LINK);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();
    }


}
