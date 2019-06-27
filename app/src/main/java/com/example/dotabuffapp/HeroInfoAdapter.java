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
    private boolean isNullAllyFlag;
    private boolean isNullEnemyFlag;
    private int mode;
    private ArrayList<HeroInfo> allySortedHeroesWinDif = new ArrayList<>(); //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif = new ArrayList<>();
    private HeroTier heroesTier;

    HeroInfoAdapter(Context context, boolean isNullAllyFlag, boolean isNullEnemyFlag, int mode, ArrayList<HeroInfo> allySortedHeroesWinDif, ArrayList<HeroInfo> enemySortedHeroesWinDif, HeroTier heroesTier) {
        this.isNullAllyFlag = isNullAllyFlag;
        this.isNullEnemyFlag = isNullEnemyFlag;
        this.inflater = LayoutInflater.from(context);
        this.mode = mode;

        if (mode == 0 && !isNullEnemyFlag) {
            this.enemySortedHeroesWinDif = enemySortedHeroesWinDif;
        } else if (mode == 1 && !isNullAllyFlag) {
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
        String preStr = "";
        if (mode == 0 && !isNullEnemyFlag) {
            heroInfo = enemySortedHeroesWinDif.get(position);
            if (heroInfo.getWinRateDif() > 0.0)
                preStr = "+";
            holder.winRateDifView.setText(preStr + ((Double) heroInfo.getWinRateDif()).toString() + "%");
        } else if (mode == 1 && !isNullAllyFlag) {
            heroInfo = allySortedHeroesWinDif.get(position);
            if (heroInfo.getWinRateDif() > 0.0)
                preStr = "+";
            holder.winRateDifView.setText(preStr + ((Double) heroInfo.getWinRateDif()).toString() + "%");
        } else {
            heroInfo = heroesTier.getHeroesTier().get(position);
            holder.winRateDifView.setText("    ");
        }
        holder.tierView.setText(heroInfo.getTier());
        holder.heroImageView.setImageResource(heroInfo.getImage());
        holder.nameView.setText(heroInfo.getName());
        holder.changedWinRateDifView.setText(((Double) heroInfo.getNewWinRate()).toString() + "%");
    }

    @Override
    public int getItemCount() {
        if (mode == 0 && !isNullEnemyFlag) {
            return enemySortedHeroesWinDif.size();
        } else if (mode == 1 && !isNullAllyFlag) {
            return allySortedHeroesWinDif.size();
        }
        return heroesTier.getHeroesTier().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView heroImageView;
        final TextView tierView, nameView, winRateDifView, changedWinRateDifView;

        ViewHolder(View view) {
            super(view);
            tierView = (TextView) view.findViewById(R.id.tier);
            heroImageView = (ImageView) view.findViewById(R.id.heroImage);
            nameView = (TextView) view.findViewById(R.id.name);
            winRateDifView = (TextView) view.findViewById(R.id.winRateDif);
            changedWinRateDifView = (TextView) view.findViewById(R.id.changedWinRate);
        }
    }
}