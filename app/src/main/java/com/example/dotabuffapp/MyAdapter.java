package com.example.dotabuffapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    private Context context = null;

    public MyAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        this.context = context;
    }

    @Override
    public int getCount() {
        return (3);
    }

    @Override
    public Fragment getItem(int position) {
        return (PageFragment.newInstance(position));
    }

    @Override
    public String getPageTitle(int position) {
        return (PageFragment.getTitle(context, position));
    }
}