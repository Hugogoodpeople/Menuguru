package pt.menuguru.playstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import pt.menuguru.playstore.Utils.Festival;
import pt.menuguru.playstore.Utils.Globals;
import pt.menuguru.menuguru.R;


public class Destaques2 extends Fragment {
    public static final String ARG_PAGE = "page";

    private Festival[] como;

    TextView bt_um;
    TextView bt_dois;

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    public int mPageNumber;
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static Destaques2 create(int pageNumber) {
        Destaques2 fragment = new Destaques2();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Destaques2() {
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
                .inflate(R.layout.fragment_destaques, container, false);


        como = Globals.getInstance().getFestival();
        new DownloadImageTask((ImageView) rootView.findViewById(R.id.image_festival)).execute("http://menuguru.pt/" + como[mPageNumber].getImagem());
        //new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView3)).execute("http://menuguru.pt/"+ como[mPageNumber].getImg2());
        bt_um = (TextView)rootView.findViewById(R.id.text_botaoum);
        bt_dois = (TextView)rootView.findViewById(R.id.text_botaodois);
        bt_um.setText(como[mPageNumber].getButaoum());
        bt_dois.setText(como[mPageNumber].getButaodois());
        if(como[mPageNumber].getFundo()=="1"){
            bt_um.setTextColor(Color.WHITE);
            bt_dois.setTextColor(Color.WHITE);
        }else{
            bt_um.setTextColor(Color.BLACK);
            bt_dois.setTextColor(Color.BLACK);
        }
        bt_um.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pesquisa = new Intent(getActivity(), Resultado_festivais.class);
                pesquisa.putExtra("id",como[mPageNumber].getId());
                startActivity(pesquisa);
                getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
            }
        });

        bt_dois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.pop_view1, R.anim.pop_view2);
            }
        });
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
