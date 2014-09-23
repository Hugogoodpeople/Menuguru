package pt.menuguru.menuguru6.Restaurante;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pt.menuguru.menuguru6.R;
import pt.menuguru.menuguru6.Utils.ImageLoader;

/**
 * Created by hugocosta on 23/09/14.
 */
public class Imagem_galeria extends Fragment
{

    ImageView imageView;

    public ImageLoader imageLoader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_imagem_restaurante, container, false);

        imageView = (ImageView)rootView.findViewById(R.id.imagem_galeria_fragment);

        String src = getArguments().getString("someString");

        imageLoader=new ImageLoader(getActivity().getApplicationContext());
        //imageView.setImageResource(src);
        imageLoader.DisplayImage("http://menuguru.pt/" + src, imageView);

        return rootView;
    }

    public static Imagem_galeria create(String src)
    {

        Imagem_galeria fragment = new Imagem_galeria();
        Bundle args = new Bundle();
        args.putString("someString", src);
        fragment.setArguments(args);

        return fragment;
    }


}
