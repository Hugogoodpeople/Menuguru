package pt.menuguru.menuguru.testes;

/**
 * Created by hugocosta on 28/10/14.
 */
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import pt.menuguru.menuguru.Especiais;
import pt.menuguru.menuguru.Inicio;
import pt.menuguru.menuguru.Procurar_mesa;
import pt.menuguru.menuguru.R;


public class TabCustomizationActivity extends TabActivity implements OnTabChangeListener
{
    /** Called when the activity is first created. */
    TabHost tHost;

    public void onCreate()
    {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources resources = getResources();
        tHost = getTabHost();
        TabHost.TabSpec tSpec;
        Intent intent;
        intent = new Intent().setClass(this, Inicio.class);
        tSpec = tHost.newTabSpec("first").setIndicator("One").setContent(intent);
        tHost.addTab(tSpec);
        intent = new Intent().setClass(this, Especiais.class);
        tSpec = tHost.newTabSpec("second").setIndicator("Second").setContent(intent);
        tHost.addTab(tSpec);
        intent = new Intent().setClass(this, Procurar_mesa.class);
        tSpec = tHost.newTabSpec("third").setIndicator("Third").setContent(intent);
        tHost.addTab(tSpec); tHost.setCurrentTab(0);
        // Default Selected Tab tHost.setOnTabChangedListener(this);
        tHost.getTabWidget().getChildAt(0).getLayoutParams().height = 40;
        tHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.WHITE);
        tHost.getTabWidget().getChildAt(1).getLayoutParams().height = 40;
        tHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.WHITE);
        tHost.getTabWidget().getChildAt(2).getLayoutParams().height = 40;
        tHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.WHITE);
        tHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(00, 219, 239));
    }


    public void onTab(String tabId)
    {
        if (tabId.equals("first"))
        {
            tHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector);
            tHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.rgb(229, 229, 229)); tHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.WHITE);
        } else if (tabId.equals("second"))
        {
            tHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector);
            tHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(229, 229, 229));
            tHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.rgb(229, 229, 229));
        } else if (tabId.equals("third"))
        {
            tHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_selector);
            tHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.rgb(229, 229, 229));
            tHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(229, 229, 229));
        }
    }

    @Override
    public void onTabChanged(String s) {

    }
}


