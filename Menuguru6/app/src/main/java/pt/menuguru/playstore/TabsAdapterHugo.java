package pt.menuguru.playstore;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

import pt.menuguru.menuguru.R;

/**
 * Created by hugocosta on 11/08/14.
 */
public class TabsAdapterHugo extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
    private final Context mContext;
    private final TabHost mTabHost;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo
    {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(String _tag, Class<?> _class, Bundle _args)
        {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory
    {
        private final Context mContext;

        public DummyTabFactory(Context context)
        {
            mContext = context;
        }

        public View createTabContent(String tag)
        {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabsAdapterHugo(FragmentActivity activity,FragmentManager managercenas,TabHost tabHost, ViewPager pager)
    {
        super(managercenas);
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);



    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
    {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);

        notifyDataSetChanged();

    }

    @Override
    public int getCount()
    {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        TabInfo info = mTabs.get(position);

        return Fragment.instantiate(mContext, info.clss.getName(), info.args);

    }

    public void onTabChanged(String tabId) {

        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);





        if (mTabHost.getTabWidget().getTabCount() == 3)
        {
            ViewGroup vg;
            TextView tv;
            for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
                vg = (ViewGroup) mTabHost.getTabWidget().getChildAt(i);
                tv = (TextView) vg.getChildAt(1);
                //tv.setTypeface(font);
                if (i == position) {
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    // Currentab = 0;
                } else {
                    tv.setTextColor(Color.parseColor("#000000"));
                }
            }



            if (tabId.equals("Inicio")) {
                mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tabselectedcolor_centro);
                mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.rgb(229, 229, 229));
                mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.rgb(229, 229, 229));
            } else if (tabId.equals("Verde")) {
                mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tabselectedcolor_centro);
                mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(229, 229, 229));
                mTabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.rgb(229, 229, 229));
            } else if (tabId.equals("Vermelha")) {
                mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tabselectedcolor_centro);
                mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.rgb(229, 229, 229));
                mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(229, 229, 229));
            }
        }


    }



    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        //mViewPager.setAdapter( this );
    }

    public void onPageSelected(int position)
    {
        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);
    }

    public void onPageScrollStateChanged(int state)
    {

        //mViewPager.setAdapter( this );

    }
}