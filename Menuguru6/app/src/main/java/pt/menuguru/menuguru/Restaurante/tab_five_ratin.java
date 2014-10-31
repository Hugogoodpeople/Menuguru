package pt.menuguru.menuguru.Restaurante;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import pt.menuguru.menuguru.R;

/**
 * Created by hugocosta on 18/09/14.
 */
public class tab_five_ratin extends Fragment {

    private String[] lista;
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
                .inflate(R.layout.tab_todas_estrelas, container, false);

        lista = getArguments().getStringArray("array_estrelas");
        contagem = getArguments().getString("contagem");


        Log.v("cenas", "resultodo do conteudo do array " + lista);

        ProgressBar progressBar1 =(ProgressBar) rootView.findViewById(R.id.progressBar1);
        ProgressBar progressBar2 =(ProgressBar) rootView.findViewById(R.id.progressBar2);
        ProgressBar progressBar3 =(ProgressBar) rootView.findViewById(R.id.progressBar3);
        ProgressBar progressBar4 =(ProgressBar) rootView.findViewById(R.id.progressBar4);
        ProgressBar progressBar5 =(ProgressBar) rootView.findViewById(R.id.progressBar5);

        progressBar1.setProgress((int)(Float.parseFloat(lista[4]) / Float.parseFloat(contagem) * 100));
        progressBar2.setProgress((int)(Float.parseFloat(lista[3]) / Float.parseFloat(contagem) * 100));
        progressBar3.setProgress((int)(Float.parseFloat(lista[2]) / Float.parseFloat(contagem) * 100));
        progressBar4.setProgress((int)(Float.parseFloat(lista[1]) / Float.parseFloat(contagem) * 100));
        progressBar5.setProgress((int)(Float.parseFloat(lista[0]) / Float.parseFloat(contagem) * 100));


        /*
        progressBar1.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.DST_IN);
        progressBar2.getProgressDrawable().setColorFilter(R.color.dourado, PorterDuff.Mode.DST_IN);
        progressBar3.getProgressDrawable().setColorFilter(R.color.dourado, PorterDuff.Mode.SRC_IN);
        progressBar4.getProgressDrawable().setColorFilter(R.color.dourado, PorterDuff.Mode.SRC_IN);
        progressBar5.getProgressDrawable().setColorFilter(R.color.dourado, PorterDuff.Mode.SRC_IN);
        */

        return rootView;
    }

    public static tab_five_ratin create(String[] listEstrelas, String votacoes) {
        tab_five_ratin fragment = new tab_five_ratin();
        Bundle args = new Bundle();
        args.putStringArray("array_estrelas",listEstrelas);
        args.putString("contagem", votacoes);
        fragment.setArguments(args);

        return fragment;
    }



}
