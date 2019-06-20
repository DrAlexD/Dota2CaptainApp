package com.example.dotabuffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroInfoAdapter extends RecyclerView.Adapter<HeroInfoAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<HeroInfo> heroesInfo;

    HeroInfoAdapter(Context context, ArrayList<HeroInfo> heroesInfo) {
        this.heroesInfo = heroesInfo;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public HeroInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hero_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeroInfoAdapter.ViewHolder holder, int position) {
        HeroInfo heroInfo = heroesInfo.get(position);
        holder.heroImageView.setImageResource(heroInfo.getHeroImage());
        holder.nameView.setText(heroInfo.getName());
        holder.winRateDifView.setText(heroInfo.getWinRateDif());
    }

    @Override
    public int getItemCount() {
        return heroesInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView heroImageView;
        final TextView nameView, winRateDifView;

        ViewHolder(View view) {
            super(view);
            heroImageView = (ImageView) view.findViewById(R.id.heroImage);
            nameView = (TextView) view.findViewById(R.id.name);
            winRateDifView = (TextView) view.findViewById(R.id.winRateDif);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
        }

    }
}