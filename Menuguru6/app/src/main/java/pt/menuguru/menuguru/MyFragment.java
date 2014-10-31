package pt.menuguru.menuguru;




import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

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

        // Default Selected Tab tHost.setOnTabChangedListener(this);
        //mTabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 40;
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(229, 229, 229));
        //mTabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 40;
        mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.rgb(229, 229, 229));
        //mTabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 40;
        mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.rgb(229, 229, 229));

        // para a primeira vez tem de estar pintado a dourado

        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tabselectedcolor_centro);

        /*
        mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tabdeselected_centro);
        mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tabdeselectedcolor_direita);
        */
        //mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(204, 153, 102));

        //mTabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_NONE);

        ViewGroup vg;
        TextView tv;
        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            vg = (ViewGroup) mTabHost.getTabWidget().getChildAt(i);
            tv = (TextView) vg.getChildAt(1);
            //tv.setTypeface(font);
            if (i == 0) {
                tv.setTextColor(Color.parseColor("#ffffff"));
                // Currentab = 0;
            } else {
                tv.setTextColor(Color.parseColor("#000000"));
            }
        }

        return v;
    }


}