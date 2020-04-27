package com.example.dotabuffapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private boolean isAllyCountersWorkingWith;
    private boolean isEnemyCountersWorkingWith;
    private ArrayList<Hero> enemyCountersByWinRateDiff;
    private ArrayList<Hero> allyCountersByWinRateDiff;
    private ArrayList<Hero> heroes;

    private ArrayList<Hero> originalHeroesOrCounters;

    HeroesAdapter(Context context, boolean isAllyCountersWorkingWith, boolean isEnemyCountersWorkingWith, ArrayList<Hero> allyCountersByWinRateDiff, ArrayList<Hero> enemyCountersByWinRateDiff, ArrayList<Hero> heroes) {
        this.inflater = LayoutInflater.from(context);
        this.isAllyCountersWorkingWith = isAllyCountersWorkingWith;
        this.isEnemyCountersWorkingWith = isEnemyCountersWorkingWith;

        if (isEnemyCountersWorkingWith) {
            this.enemyCountersByWinRateDiff = enemyCountersByWinRateDiff;
        } else if (isAllyCountersWorkingWith) {
            this.allyCountersByWinRateDiff = allyCountersByWinRateDiff;
        } else {
            this.heroes = heroes;
        }
    }

    @NotNull
    @Override
    public HeroesAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hero_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull HeroesAdapter.ViewHolder holder, int position) {
        Hero hero;

        if (isEnemyCountersWorkingWith) {
            hero = enemyCountersByWinRateDiff.get(position);
            holder.heroWinRateDiffView.setText(textForWinRateDiffView(hero));
        } else if (isAllyCountersWorkingWith) {
            hero = allyCountersByWinRateDiff.get(position);
            holder.heroWinRateDiffView.setText(textForWinRateDiffView(hero));
        } else {
            hero = heroes.get(position);
        }

        holder.heroTierView.setText(hero.getTier());
        holder.heroImageView.setImageResource(hero.getImage());
        holder.heroNameView.setText(hero.getName());
        holder.heroNewWinRateView.setText(hero.getNewWinRate() + "%");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView heroImageView;
        final TextView heroTierView;
        final TextView heroNameView;
        final TextView heroWinRateDiffView;
        final TextView heroNewWinRateView;

        ViewHolder(View view) {
            super(view);
            heroTierView = view.findViewById(R.id.heroTier);
            heroImageView = view.findViewById(R.id.heroImage);
            heroNameView = view.findViewById(R.id.heroName);
            heroWinRateDiffView = view.findViewById(R.id.heroWinRateDiff);
            heroNewWinRateView = view.findViewById(R.id.heroNewWinRate);
        }
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