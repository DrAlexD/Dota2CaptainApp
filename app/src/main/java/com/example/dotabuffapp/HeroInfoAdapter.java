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
    private ArrayList<HeroInfo> allySortedHeroesWinDif = new ArrayList<HeroInfo>(); //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif = new ArrayList<HeroInfo>();
    private HeroTier heroesTier;

    HeroInfoAdapter(Context context, boolean isNullAllyFlag, boolean isNullEnemyFlag, int mode, ArrayList<HeroInfo> allySortedHeroesWinDif, ArrayList<HeroInfo> enemySortedHeroesWinDif, HeroTier heroesTier) {
        this.isNullAllyFlag = isNullAllyFlag;
        this.isNullEnemyFlag = isNullEnemyFlag;
        this.inflater = LayoutInflater.from(context);
        this.mode = mode;

        if (mode == 0 && !isNullEnemyFlag) {
            System.out.println("IN ENEMY INITIALISATION: " + isNullEnemyFlag);
            this.enemySortedHeroesWinDif = enemySortedHeroesWinDif;
        } else if (mode == 1 && !isNullAllyFlag) {
            System.out.println("IN ALLY INITIALISATION");
            this.allySortedHeroesWinDif = allySortedHeroesWinDif;
        } else {
            System.out.println("IN USUAL INITIALISATION");
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
        if (mode == 0 && !isNullEnemyFlag) {
            heroInfo = enemySortedHeroesWinDif.get(position);
            holder.heroImageView.setImageResource(heroInfo.getHeroImage());
            holder.nameView.setText(heroInfo.getName());
            holder.winRateDifView.setText(((Double) heroInfo.getWinRateDif()).toString() + "%");
            holder.changedWinRateDifView.setText(((Double) heroInfo.getChangedWinRate()).toString() + "%");
        } else if (mode == 1 && !isNullAllyFlag) {
            heroInfo = allySortedHeroesWinDif.get(position);
            holder.heroImageView.setImageResource(heroInfo.getHeroImage());
            holder.nameView.setText(heroInfo.getName());
            holder.winRateDifView.setText(((Double) heroInfo.getWinRateDif()).toString() + "%");
            holder.changedWinRateDifView.setText(((Double) heroInfo.getChangedWinRate()).toString() + "%");
        } else {
            holder.heroImageView.setImageResource(heroesTier.getHeroesTier().get(position).getValue().getKey());
            holder.nameView.setText(heroesTier.getHeroesTier().get(position).getKey());
            holder.winRateDifView.setText("      ");
            holder.changedWinRateDifView.setText(heroesTier.getHeroesTier().get(position).getValue().getValue().getValue().toString() + "%");
        }
    }

    @Override
    public int getItemCount() {
        if (mode == 0 && !isNullEnemyFlag) {
            System.out.println("IN ENEMY SIZE");
            return enemySortedHeroesWinDif.size();
        } else if (mode == 1 && !isNullAllyFlag) {
            System.out.println("IN ALLY SIZE");
            return allySortedHeroesWinDif.size();
        }
        System.out.println("IN USUAL SIZE");
        return heroesTier.getHeroesTier().size();
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