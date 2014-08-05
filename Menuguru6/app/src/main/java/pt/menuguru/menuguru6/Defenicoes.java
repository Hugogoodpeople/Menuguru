package pt.menuguru.menuguru6;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Defenicoes extends Fragment {

    private ListView mDrawerListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayAdapter adapter = new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.inicio),
                        getString(R.string.reservas),
                        getString(R.string.definicao),
                        getString(R.string.destaque),
                        getString(R.string.como),
                });



        return inflater.inflate(R.layout.fragment_defenicoes, container, false);


    }
}
