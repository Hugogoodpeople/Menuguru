package pt.menuguru.menuguru6;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hugocosta on 18/09/14.
 */
public class tab_five_ratin extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.tab_todas_estrelas, container, false);





        return rootView;
    }

    public static tab_five_ratin create() {
        tab_five_ratin fragment = new tab_five_ratin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

}
