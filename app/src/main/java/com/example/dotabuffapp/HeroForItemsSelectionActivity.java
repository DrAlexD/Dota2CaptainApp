package com.example.dotabuffapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HeroForItemsSelectionActivity extends AppCompatActivity implements HeroItemsAsyncResponse {
    private RecyclerView heroesView;
    private HeroesAdapter heroesAdapter;
    private String lastSearch = "";
    private HeroesCounters heroesCounters;
    private ArrayList<Hero> heroesInitialization;
    private HeroItems heroItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heroes_activity);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            heroesCounters = (HeroesCounters) arguments.getSerializable("HeroesCounters");

            HeroItems heroItems = new HeroItems();
            ItemsInitializationTask itemsInitializationTask = new ItemsInitializationTask(this, heroItems);
            itemsInitializationTask.execute();
        }
    }

    @Override
    public void heroItemsProcessFinish(HeroItems heroItems) {
        heroesView = findViewById(R.id.heroesInfoList);

        heroesInitialization = heroesCounters.getHeroesInitialization().getOriginal();

        heroesAdapter = new HeroesAdapter(this, false,
                false, null, null, heroesInitialization);
        heroesView.setAdapter(heroesAdapter);

        heroesView.addOnItemTouchListener(getClickListener(this));

        SearchView searchView = findViewById(R.id.autoCompleteHeroesSearch);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());

        this.heroItems = heroItems;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getTitle().toString()) {
            case "Picker":
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(mainIntent);
                return true;
            case "Items":
                Intent itemsIntent = new Intent(this, HeroForItemsSelectionActivity.class);
                itemsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                itemsIntent.putExtra("HeroesCounters", heroesCounters);
                startActivity(itemsIntent);
                return true;
            case "Settings":
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                settingsIntent.putExtra("HeroesCounters", heroesCounters);
                startActivity(settingsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private RecyclerItemClickListener getClickListener(Context context) {

        return new RecyclerItemClickListener(context, heroesView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                ArrayList<Hero> currentFilteredHeroes = new ArrayList<>();
                ArrayList<Hero> heroes = heroesInitialization;

                if (!lastSearch.equals("")) {
                    for (Hero hero : heroes) {
                        if ((hero.getName().toLowerCase()).contains(lastSearch.toLowerCase())) {
                            currentFilteredHeroes.add(hero);
                        }
                    }
                } else
                    currentFilteredHeroes = heroes;

                heroItems.setHero(currentFilteredHeroes.get(position));

                Intent heroItemsIntent = new Intent(context, HeroItemsActivity.class);
                heroItemsIntent.putExtra("HeroItems", heroItems);
                startActivity(heroItemsIntent);
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
