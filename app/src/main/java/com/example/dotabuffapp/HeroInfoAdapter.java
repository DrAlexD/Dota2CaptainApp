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
    private List<HeroInfo> heroes;

    public HeroInfoAdapter(Context context, int resource, List<HeroInfo> heroes) {
        super(context, resource, heroes);
        this.heroes = heroes;
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

        HeroInfo hero = heroes.get(position);

        viewHolder.heroImageView.setImageResource(hero.getHeroImage());
        viewHolder.nameView.setText(hero.getName());
        viewHolder.winRateDifView.setText(hero.getWinRateDif());

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