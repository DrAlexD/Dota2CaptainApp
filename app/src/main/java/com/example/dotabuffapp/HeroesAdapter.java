package com.example.dotabuffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private boolean isAllyCountersWorkingWith;
    private boolean isEnemyCountersWorkingWith;
    private ArrayList<Hero> enemyCountersByWinRateDiff;
    private ArrayList<Hero> allyCountersByWinRateDiff;
    private ArrayList<Hero> heroes;

    private ArrayList<Hero> originalHeroesOrCounters;

    HeroesAdapter(Context context, boolean isAllyCountersWorkingWith, boolean isEnemyCountersWorkingWith, ArrayList<Hero> allyCountersByWinRateDiff, ArrayList<Hero> enemyCountersByWinRateDiff, HeroesWithTiers heroesTier) {
        this.inflater = LayoutInflater.from(context);
        this.isAllyCountersWorkingWith = isAllyCountersWorkingWith;
        this.isEnemyCountersWorkingWith = isEnemyCountersWorkingWith;

        if (isEnemyCountersWorkingWith) {
            this.enemyCountersByWinRateDiff = enemyCountersByWinRateDiff;
        } else if (isAllyCountersWorkingWith) {
            this.allyCountersByWinRateDiff = allyCountersByWinRateDiff;
        } else {
            this.heroes = heroesTier.getCurrentHeroesWithTiers();
        }
    }

    @Override
    @NonNull
    public HeroesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hero_info_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView heroImageView;
        final TextView tierView;
        final TextView nameView;
        final TextView winRateDiffView;
        final TextView newWinRateView;

        ViewHolder(View view) {
            super(view);
            tierView = view.findViewById(R.id.tier);
            heroImageView = view.findViewById(R.id.heroImage);
            nameView = view.findViewById(R.id.name);
            winRateDiffView = view.findViewById(R.id.winRateDiff);
            newWinRateView = view.findViewById(R.id.newWinRate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HeroesAdapter.ViewHolder holder, int position) {
        Hero hero;

        if (isEnemyCountersWorkingWith) {
            hero = enemyCountersByWinRateDiff.get(position);
            holder.winRateDiffView.setText(textForWinRateDiffView(hero));
        } else if (isAllyCountersWorkingWith) {
            hero = allyCountersByWinRateDiff.get(position);
            holder.winRateDiffView.setText(textForWinRateDiffView(hero));
        } else {
            hero = heroes.get(position);
        }

        holder.tierView.setText(hero.getTier());
        holder.heroImageView.setImageResource(hero.getImage());
        holder.nameView.setText(hero.getName());
        holder.newWinRateView.setText(hero.getNewWinRate() + "%");
    }

    private String textForWinRateDiffView(Hero hero) {
        String frontSign = "";
        if (hero.getWinRateDiff() > 0.0)
            frontSign = "+";
        return frontSign + hero.getWinRateDiff() + "%";
    }

    @Override
    public int getItemCount() {
        if (isEnemyCountersWorkingWith) {
            return enemyCountersByWinRateDiff.size();
        } else if (isAllyCountersWorkingWith) {
            return allyCountersByWinRateDiff.size();
        }
        return heroes.size();
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults oReturn = new FilterResults();
                ArrayList<Hero> filterResults = new ArrayList<>();

                if (originalHeroesOrCounters == null) {
                    if (isEnemyCountersWorkingWith) {
                        originalHeroesOrCounters = enemyCountersByWinRateDiff;
                    } else if (isAllyCountersWorkingWith) {
                        originalHeroesOrCounters = allyCountersByWinRateDiff;
                    } else {
                        originalHeroesOrCounters = heroes;
                    }
                }

                String findTextChange = constraint.toString();
                if (!findTextChange.equals("")) {
                    for (Hero hero : originalHeroesOrCounters) {
                        if ((hero.getName().toLowerCase()).contains(findTextChange.toLowerCase()))
                            filterResults.add(hero);
                    }
                } else {
                    filterResults = originalHeroesOrCounters;
                }

                oReturn.values = filterResults;
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (isEnemyCountersWorkingWith) {
                    enemyCountersByWinRateDiff = (ArrayList<Hero>) results.values;
                } else if (isAllyCountersWorkingWith) {
                    allyCountersByWinRateDiff = (ArrayList<Hero>) results.values;
                } else {
                    heroes = (ArrayList<Hero>) results.values;
                }

                notifyDataSetChanged();
            }
        };
    }
}