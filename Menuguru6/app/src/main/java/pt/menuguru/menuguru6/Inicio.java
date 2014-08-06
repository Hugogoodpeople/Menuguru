package pt.menuguru.menuguru6;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
        //getActivity().getActionBar().setCustomView(R.layout.xyz);
        //getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder
                        .setItems(R.array.pesquisa, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent myIntent = new Intent(getActivity(), Pesquisa_avancada.class);
                                        startActivity(myIntent);
                                        break;
                                    case 1:
                                        Intent myIntent2 = new Intent(getActivity(), Inspiracao.class);
                                        startActivity(myIntent2);
                                        break;
                                    case 2:

                                    default:
                                        break;
                                }
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;


            default:
                break;
        }

        return false;
    }
}
