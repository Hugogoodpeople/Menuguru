package pt.menuguru.menuguru6;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Inicio extends Fragment {

    String value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_inicio, container, false);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //String value = getActivity().getIntent().getExtras().getString("local");

        Intent intent = getActivity().getIntent();
        value = intent.getStringExtra("local");
        if(value == null || value.trim().equals("")){value="Perto de mim";}
        ((MainActivity) getActivity()).setActionBarTitle(value);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_localizacao:
                Intent myIntent = new Intent(getActivity(), Localizacao.class);
                myIntent.putExtra("local", value); //Optional parameters
                getActivity().startActivity(myIntent);
                this.getActivity().finish();
                return false;
            case R.id.action_pesquisa:
                // Do Fragment menu item stuff here
                return true;
            default:
                break;
        }

        return false;
    }
}
