package pt.menuguru.menuguru6;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Defenicoes extends ListFragment {

    String[] some_array = null;


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.v("msg","clicado "+ some_array[position]);
        // aqui tenho de chamar as activities para abrir cada uma das propriedades

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView t =(TextView) getActivity().findViewById(R.id.mytext);
        t.setText("Defenições");

        some_array = getResources().getStringArray(R.array.defenicoes_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( inflater.getContext(), android.R.layout.simple_list_item_1,some_array);
        setListAdapter(adapter);

        // versao antiga para fragmentos simples
        // return inflater.inflate(R.layout.fragment_defenicoes, container, false);


        // versao nova que funciona bem
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
