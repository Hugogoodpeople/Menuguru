package pt.menuguru.menuguru6;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Destaques extends Fragment {


    public Destaques() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView t =(TextView) getActivity().findViewById(R.id.mytext);
        t.setText("Destaque");
        return inflater.inflate(R.layout.fragment_destaques, container, false);
    }


}
