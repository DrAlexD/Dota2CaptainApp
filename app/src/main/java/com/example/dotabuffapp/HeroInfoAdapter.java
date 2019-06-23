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
    private boolean isNullFlag;
    private int mode;
    private ArrayList<HeroInfo> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif;
    private HeroTier heroesTier;

    HeroInfoAdapter(Context context, boolean isNullFlag, int mode, ArrayList<HeroInfo> allySortedHeroesWinDif, ArrayList<HeroInfo> enemySortedHeroesWinDif, HeroTier heroesTier) {
        this.isNullFlag = isNullFlag;
        this.inflater = LayoutInflater.from(context);
        this.mode = mode;
        if (!isNullFlag && mode != 2) {
            if (mode == 0)
                this.enemySortedHeroesWinDif = enemySortedHeroesWinDif;
            else if (mode == 1)
                this.allySortedHeroesWinDif = allySortedHeroesWinDif;
        } else {
            this.heroesTier = heroesTier;
        }
    }

    @Override
    public HeroInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hero_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeroInfoAdapter.ViewHolder holder, int position) {
        HeroInfo heroInfo;
        if (!isNullFlag && mode != 2) {
            if (mode == 0) {
                heroInfo = enemySortedHeroesWinDif.get(position);
                holder.heroImageView.setImageResource(heroInfo.getHeroImage());
                holder.nameView.setText(heroInfo.getName());
                holder.winRateDifView.setText(((Double) heroInfo.getWinRateDif()).toString());
                holder.changedWinRateDifView.setText(((Double) heroInfo.getChangedWinRate()).toString());
            } else if (mode == 1) {
                heroInfo = allySortedHeroesWinDif.get(position);
                holder.heroImageView.setImageResource(heroInfo.getHeroImage());
                holder.nameView.setText(heroInfo.getName());
                holder.winRateDifView.setText(((Double) heroInfo.getWinRateDif()).toString());
                holder.changedWinRateDifView.setText(((Double) heroInfo.getChangedWinRate()).toString());
            }
        } else {
            holder.heroImageView.setImageResource(heroesTier.heroesTier.get(position).getValue().getKey());
            holder.nameView.setText(heroesTier.heroesTier.get(position).getKey());
            holder.winRateDifView.setText("");
            holder.changedWinRateDifView.setText(heroesTier.heroesTier.get(position).getValue().getValue().getValue().toString());
        }
    }

    @Override
    public int getItemCount() {
        if (!isNullFlag && mode != 2) {
            if (mode == 0) {
                return enemySortedHeroesWinDif.size();
            } else if (mode == 1) {
                return allySortedHeroesWinDif.size();
            }
        }
        return heroesTier.heroesTier.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView heroImageView;
        final TextView nameView, winRateDifView, changedWinRateDifView;

        ViewHolder(View view) {
            super(view);
            heroImageView = (ImageView) view.findViewById(R.id.heroImage);
            nameView = (TextView) view.findViewById(R.id.name);
            winRateDifView = (TextView) view.findViewById(R.id.winRateDif);
            changedWinRateDifView = (TextView) view.findViewById(R.id.changedWinRate);
        }
    }
}