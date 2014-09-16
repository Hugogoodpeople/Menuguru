package pt.menuguru.menuguru6.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import pt.menuguru.menuguru6.R;


public class ComoFunciona2 extends Fragment {
    public static final String ARG_PAGE = "page";
    public static final String ARG_IMAGEM = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    public int mPageNumber;
    public String mImagem;
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ComoFunciona2 create(int pageNumber) {
        ComoFunciona2 fragment = new ComoFunciona2();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        //args.putString(ARG_IMAGEM, imagem);
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
        //mImagem = getArguments().getString(ARG_IMAGEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_como_funciona2, container, false);

        String resto = Globals.get_instance().getCfunc().getImg1();
        Log.v("IMG!", resto);

        new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView2)).execute("http://menuguru.pt/imagens_menuguru/comofunciona/"+ (mPageNumber +1) +".jpg");
        new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView3)).execute("http://menuguru.pt/imagens_menuguru/comofunciona/texto00"+ (mPageNumber +1) +".png");

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
