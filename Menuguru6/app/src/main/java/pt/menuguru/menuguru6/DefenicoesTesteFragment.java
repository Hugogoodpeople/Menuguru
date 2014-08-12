package pt.menuguru.menuguru6;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;


public class DefenicoesTesteFragment extends Fragment implements AbsListView.OnItemClickListener {

    public class MyListAdapter extends ArrayAdapter<String> {

        Context myContext;

        public MyListAdapter(Context context, int textViewResourceId,
                             String[] objects) {
            super(context, textViewResourceId, objects);
            myContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            View row;
            if (position == 0 || position == 3 || position == 6) {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_header, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(some_array[position]);

                TextView label2 = (TextView) row.findViewById(R.id.headerText);
                label2.setText(array_headers[position/3]);

                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_right_b);
            }else
            {
                LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_defenicoes, parent, false);
                TextView label = (TextView) row.findViewById(R.id.month);
                label.setText(some_array[position]);
                ImageView icon = (ImageView) row.findViewById(R.id.icon);

                //Customize your icon here
                icon.setImageResource(R.drawable.ic_right_b);
            }

            return row;
        }

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String[] some_array = null;
    String[] array_headers = null;

    //private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static DefenicoesTesteFragment newInstance(String param1, String param2) {
        DefenicoesTesteFragment fragment = new DefenicoesTesteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DefenicoesTesteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        some_array = getResources().getStringArray(R.array.defenicoes_array);
        array_headers = getResources().getStringArray(R.array.headersdefenicoes);


        // TODO: Change Adapter to display your content
        MyListAdapter myListAdapter = new MyListAdapter(getActivity(), R.layout.row_defenicoes, some_array);
        mAdapter =myListAdapter;

        //mAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1 ,some_array);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defenicoesteste, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }
    */

    /*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
        */

        Log.v("msg", "clicado " + some_array[position]);

        if (position == 0)
        {
            Intent intent = new Intent(this.getActivity(), MinhaConta.class);
            startActivity(intent);
        }else if (position == 3 || position == 4 || position == 5)
        {
            Intent intent = new Intent(this.getActivity(), ReadFileAssetsActivity.class);



            String mensagem = null;

            switch (position)
            {
                case 3:
                {
                    mensagem = getString(R.string.sobre_menuguru);
                    break;
                }
                case 4:
                {
                    mensagem = getString(R.string.termos_condicoes);
                    break;
                }
                case 5:
                {
                    mensagem = getString(R.string.politica_privacidade);
                    break;
                }

            }

            intent.putExtra("menssagem", mensagem);

            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.push_view1, R.anim.push_view2);
        }else if(position == 6) {
            // este assim esta a funcionar

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

/*
            Uri phototUri = Uri.parse("http://menuguru.pt/layers/logo.png");

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri);
            shareIntent.setType("image/*");
            //startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            startActivity(shareIntent);
*/
        }


    }


    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }




    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
    */

}