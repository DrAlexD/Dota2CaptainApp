package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroSelectionActivity extends AppCompatActivity {
    private RecyclerView heroesView;
    private HeroesAdapter heroesAdapter;
    private String lastSearch = "";
    private ArrayList<Hero> allyCountersByWinRateDiff;
    private ArrayList<Hero> enemyCountersByWinRateDiff;
    private ArrayList<Hero> heroesInitialization;
    boolean isAllyCountersWorkingWith;
    boolean isEnemyCountersWorkingWith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heroes_activity);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            heroesView = findViewById(R.id.heroesInfoList);

            int mode = (int) arguments.get("AllyPickOrAllyBanOrEnemyPickAndBan");
            HeroesCounters heroesCounters = (HeroesCounters) arguments.getSerializable("HeroesCounters");

            allyCountersByWinRateDiff = heroesCounters.getAllyCountersByWinRateDiff();
            enemyCountersByWinRateDiff = heroesCounters.getEnemyCountersByWinRateDiff();
            heroesInitialization = heroesCounters.getHeroesInitialization().getCurrent();

            boolean isNullAllyHeroes = heroesCounters.getAllyHeroes().isEmpty();
            boolean isNullEnemyHeroes = heroesCounters.getEnemyHeroes().isEmpty();
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

            heroesAdapter = new HeroesAdapter(this, isAllyCountersWorkingWith,
                    isEnemyCountersWorkingWith, allyCountersByWinRateDiff, enemyCountersByWinRateDiff, heroesInitialization);
            heroesView.setAdapter(heroesAdapter);

            heroesView.addOnItemTouchListener(getClickListener());

            SearchView searchView = findViewById(R.id.autoCompleteHeroesSearch);
            searchView.setOnQueryTextListener(getSearchTextUpdateListener());
        }
    }

    private RecyclerItemClickListener getClickListener() {

        return new RecyclerItemClickListener(this, heroesView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                ArrayList<Hero> currentFilteredHeroes = new ArrayList<>();

                ArrayList<Hero> heroesOrCounters;
                if (isEnemyCountersWorkingWith) {
                    heroesOrCounters = enemyCountersByWinRateDiff;
                } else if (isAllyCountersWorkingWith) {
                    heroesOrCounters = allyCountersByWinRateDiff;
                } else {
                    heroesOrCounters = heroesInitialization;
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
                intent.putExtra("ImageResource", currentFilteredHeroes.get(position).getImage());
                intent.putExtra("HeroName", currentFilteredHeroes.get(position).getName());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        });
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {

        return new SearchView.OnQueryTextListener() {

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
        };
    }
}
