package com.example.dotabuffapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    private int pageNumber = 1;

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    static String getTitle(Context context, int position) {
        if (position == 0)
            return "RecommendedPicks";
        else if (position == 1)
            return "PicksAndBans";
        else
            return "RecommendedBans";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (pageNumber == 1) {
            View result = inflater.inflate(R.layout.picks_and_bans_page, container, false);
            return result;
        }
        return null;
    }
}