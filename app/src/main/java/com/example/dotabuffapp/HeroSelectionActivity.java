package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroSelectionActivity extends AppCompatActivity {
    private String lastSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_activity);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            HeroesAdapter heroesAdapter;
            RecyclerView heroesView = findViewById(R.id.heroesInfoList);
            SearchView searchView = findViewById(R.id.autocomplete);

            int mode = (int) arguments.get("PickOrBan");
            HeroesCounters heroesCounters = (HeroesCounters) arguments.getSerializable("HeroesCounters");

            boolean isAllyCountersWorkingWith;
            boolean isEnemyCountersWorkingWith;
            boolean isNullAllyHeroes = heroesCounters.getAllyHeroes().isEmpty();
            boolean isNullEnemyHeroes = heroesCounters.getEnemyHeroes().isEmpty();
            ArrayList<Hero> allyCountersByWinRateDiff = heroesCounters.getAllyCountersByWinRateDiff();
            ArrayList<Hero> enemyCountersByWinRateDiff = heroesCounters.getEnemyCountersByWinRateDiff();
            ArrayList<Hero> heroes = heroesCounters.getHeroesWithTiers().getCurrentHeroesWithTiers();

            if (!isNullAllyHeroes && !isNullEnemyHeroes) {
                isAllyCountersWorkingWith = mode == 1;
                isEnemyCountersWorkingWith = mode == 0;
            } else if (isNullAllyHeroes && isNullEnemyHeroes) {
                isAllyCountersWorkingWith = false;
                isEnemyCountersWorkingWith = false;
            } else if (!isNullEnemyHeroes) {
                isAllyCountersWorkingWith = false;
                isEnemyCountersWorkingWith = mode == 0;
            } else {
                isAllyCountersWorkingWith = mode == 1;
                isEnemyCountersWorkingWith = false;
            }

            heroesAdapter = new HeroesAdapter(this, isAllyCountersWorkingWith, isEnemyCountersWorkingWith, allyCountersByWinRateDiff, enemyCountersByWinRateDiff, heroes);

            heroesView.setAdapter(heroesAdapter);
            heroesView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, heroesView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            ArrayList<Hero> currentFilteredHeroes = new ArrayList<>();

                            ArrayList<Hero> heroesOrCounters;
                            if (isEnemyCountersWorkingWith) {
                                heroesOrCounters = enemyCountersByWinRateDiff;
                            } else if (isAllyCountersWorkingWith) {
                                heroesOrCounters = allyCountersByWinRateDiff;
                            } else {
                                heroesOrCounters = heroes;
                            }

                            if (!lastSearch.equals("")) {
                                for (Hero hero : heroesOrCounters) {
                                    if ((hero.getName().toLowerCase()).contains(lastSearch.toLowerCase())) {
                                        currentFilteredHeroes.add(hero);
                                    }
                                }
                            } else
                                currentFilteredHeroes = heroesOrCounters;

                            Intent intent = new Intent();
                            intent.putExtra("ImageId", currentFilteredHeroes.get(position).getImage());
                            intent.putExtra("HeroName", currentFilteredHeroes.get(position).getName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    })
            );

            searchView.setOnQueryTextListener(
                    new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            lastSearch = newText;
                            heroesAdapter.getFilter().filter(newText);
                            return true;
                        }
                    });
        }
    }
}
