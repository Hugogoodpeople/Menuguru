package pt.menuguru.menuguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

import pt.menuguru.menuguru.Utils.ComoFunc;
import pt.menuguru.menuguru.Utils.Globals;


public class ComoFunciona2 extends Fragment {
    public static final String ARG_PAGE = "page";

    private ComoFunc[] como;

    Button bt_close;

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    public int mPageNumber;
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ComoFunciona2 create(int pageNumber) {
        ComoFunciona2 fragment = new ComoFunciona2();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ComoFunciona2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        getActivity().getActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_como_funciona2, container, false);


        como = Globals.getInstance().getCfunc();
        new DownloadImageTask((ImageView) rootView.findViewById(R.id.galeria_imagens)).execute("http://menuguru.pt/" + como[mPageNumber].getImg1());
        new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView3)).execute("http://menuguru.pt/"+ como[mPageNumber].getImg2());



        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
