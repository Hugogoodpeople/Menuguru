package pt.menuguru.menuguru6;




import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.Locale;

import pt.menuguru.menuguru6.Utils.Globals;

public class MyFragment extends Fragment
{

    public TabHost mTabHost;
    public ViewPager mViewPager;
    public TabsAdapterHugo mTabsAdapter;

    public MyFragment() {
    }

    @Override
    public void onCreate(Bundle instance)
    {


        super.onCreate(instance);


    }

    public void getTabs()
    {

        mViewPager.setAdapter( mTabsAdapter );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup();


        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapterHugo(getActivity(), getChildFragmentManager() , mTabHost, mViewPager);

        // Here we load the content for each tab.
        mTabsAdapter.addTab(mTabHost.newTabSpec("Inicio").setIndicator(getString(R.string.sugestoes)), Inicio.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("Verde").setIndicator(getString(R.string.Especias)), Especiais.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("Vermelha").setIndicator(getString(R.string.Reservas)), Procurar_mesa.class, null);
        //mTabsAdapter.addTab(mTabHost.newTabSpec("Branca").setIndicator("Branca"), Branca.class, null);

        return v;
    }


}