package com.example.dotabuffapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class PageTurningAdapter extends FragmentPagerAdapter {

    PageTurningAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return (3);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return (PageFragment.newInstance(position));
    }

    @Override
    public String getPageTitle(int position) {
        return (PageFragment.getTitle(position));
    }
}