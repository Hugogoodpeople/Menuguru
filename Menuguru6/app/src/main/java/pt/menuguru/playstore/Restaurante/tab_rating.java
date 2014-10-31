package pt.menuguru.playstore.Restaurante;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import pt.menuguru.menuguru.R;

/**
 * Created by hugocosta on 18/09/14.
 */
public class tab_rating extends Fragment
{

    private String mediaRating;
    private String contagem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.tab_rating, container, false);


        this.contagem = getArguments().getString("contagem");
        mediaRating = getArguments().getString("mediarating");

        // ratingview
        RatingBar rating =(RatingBar)rootView.findViewById(R.id.ratingBar_avaliar);
        rating.setRating(Float.parseFloat(mediaRating));
        rating.setFocusable(false);
        rating.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        TextView ratingdesc = (TextView) rootView.findViewById(R.id.text_rating_desc);

        ratingdesc.setText(descRating(Float.parseFloat(mediaRating)));


        return rootView;
    }

    private String descRating(Float rating)
    {
        String desc;



        if (Float.parseFloat(contagem)!= 0) {
            if (rating < 2)
                desc = getString(R.string.horrivel) + " " + String.format("%.1f", rating);
            else if (rating < 3)
                desc = getString(R.string.mau) + " " +  String.format("%.1f", rating);
            else if (rating < 4)
                desc = getString(R.string.bom) + " "  + String.format("%.1f", rating);
            else if (rating < 5)
                desc = getString(R.string.muito_bom) + " "  + String.format("%.1f", rating);
            else
                desc = getString(R.string.excelente) + " " + String.format("%.1f", rating);
        }
        else
        {
            desc = "";
        }


        return desc;
    }


    public static tab_rating create(String mediarating , String contagem) {
        tab_rating fragment = new tab_rating();
        Bundle args = new Bundle();


        args.putString("mediarating", mediarating);
        args.putString("contagem", contagem);
        fragment.setArguments(args);
        return fragment;
    }



}
