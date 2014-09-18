package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pt.menuguru.menuguru6.Utils.Globals;

/**
 * Created by hugocosta on 18/09/14.
 */
public class tab_rating extends Fragment
{


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
                .inflate(R.layout.tab_rating, container, false);





        return rootView;
    }

    public static tab_rating create() {
        tab_rating fragment = new tab_rating();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



}
