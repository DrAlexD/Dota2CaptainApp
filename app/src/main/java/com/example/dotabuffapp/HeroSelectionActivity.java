package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroSelectionActivity extends AppCompatActivity {
    private Intent intent;
    private HeroInfoAdapter heroInfoAdapter;
    private String lastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_activity);

        HeroPicker heroPicks;
        HeroTier heroesTier;

        Bundle arguments = getIntent().getExtras();
        boolean isNullAllyFlag;
        boolean isNullEnemyFlag;

        RecyclerView heroesInfoView = (RecyclerView) findViewById(R.id.heroesInfoList);
        SearchView searchView = (SearchView) findViewById(R.id.autocomplete);

        if (arguments != null) {
            int mode = (int) arguments.get("PickOrBan");
            heroPicks = (HeroPicker) arguments.getSerializable("Heroes");
            heroesTier = (HeroTier) arguments.getSerializable("HeroesTier");

            if (!heroPicks.isNullAllyHeroes() && !heroPicks.isNullEnemyHeroes()) {
                isNullAllyFlag = false;
                isNullEnemyFlag = false;
                heroInfoAdapter = new HeroInfoAdapter(this, isNullAllyFlag, isNullEnemyFlag, mode, heroPicks.getSortedHeroesWinDif(true), heroPicks.getSortedHeroesWinDif(false), heroesTier);
            } else if (heroPicks.isNullAllyHeroes() && heroPicks.isNullEnemyHeroes()) {
                isNullAllyFlag = true;
                isNullEnemyFlag = true;
                heroInfoAdapter = new HeroInfoAdapter(this, isNullAllyFlag, isNullEnemyFlag, mode, null, null, heroesTier);
            } else if (!heroPicks.isNullEnemyHeroes()) {
                isNullAllyFlag = true;
                isNullEnemyFlag = false;
                heroInfoAdapter = new HeroInfoAdapter(this, isNullAllyFlag, isNullEnemyFlag, mode, null, heroPicks.getSortedHeroesWinDif(false), heroesTier);
            } else {
                isNullAllyFlag = false;
                isNullEnemyFlag = true;
                heroInfoAdapter = new HeroInfoAdapter(this, isNullAllyFlag, isNullEnemyFlag, mode, heroPicks.getSortedHeroesWinDif(true), null, heroesTier);
            }

            heroesInfoView.setAdapter(heroInfoAdapter);
            heroesInfoView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, heroesInfoView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ArrayList<HeroInfo> currentList = new ArrayList<>();
                            intent = new Intent();
                            if (mode == 0 && !isNullEnemyFlag) {
                                for (HeroInfo h : heroPicks.getSortedHeroesWinDif(false)) {
                                    if (h.getName().toLowerCase().contains(lastSearch.toLowerCase())) {
                                        currentList.add(h);
                                    }
                                }
                            } else if (mode == 1 && !isNullAllyFlag) {
                                for (HeroInfo h : heroPicks.getSortedHeroesWinDif(true)) {
                                    if (h.getName().toLowerCase().contains(lastSearch.toLowerCase())) {
                                        currentList.add(h);
                                    }
                                }
                            } else {
                                for (HeroInfo h : heroesTier.getHeroesTier()) {
                                    if (h.getName().toLowerCase().contains(lastSearch.toLowerCase())) {
                                        currentList.add(h);
                                    }
                                }
                            }
                            intent.putExtra("ImageId", currentList.get(position).getImage());
                            intent.putExtra("HeroName", currentList.get(position).getName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    })
            );

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    lastSearch = newText;
                    heroInfoAdapter.getFilter().filter(newText);
                    return true;
                }
            });
        }
    }
}
