package com.example.dotabuffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HeroInfoAdapter extends ArrayAdapter<HeroInfo> {

    private LayoutInflater inflater;
    private int layout;
    private List<HeroInfo> heroesInfo;

    public HeroInfoAdapter(Context context, int resource, List<HeroInfo> heroesInfo) {
        super(context, resource, heroesInfo);
        this.heroesInfo = heroesInfo;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HeroInfo heroInfo = heroesInfo.get(position);

        viewHolder.heroImageView.setImageResource(heroInfo.getHeroImage());
        viewHolder.nameView.setText(heroInfo.getName());
        viewHolder.winRateDifView.setText(heroInfo.getWinRateDif());

        return convertView;
    }

    private class ViewHolder {
        final ImageView heroImageView;
        final TextView nameView, winRateDifView;

        ViewHolder(View view) {
            heroImageView = (ImageView) view.findViewById(R.id.heroImage);
            nameView = (TextView) view.findViewById(R.id.name);
            winRateDifView = (TextView) view.findViewById(R.id.winRateDif);
        }
    }
}