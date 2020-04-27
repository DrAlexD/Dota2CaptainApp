package com.example.dotabuffapp;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HeroItemsActivity extends AppCompatActivity implements HeroItemsAsyncResponse {
    private ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            HeroItems heroItems = (HeroItems) arguments.getSerializable("HeroItems");

            TextView heroTierOnItemsPage = findViewById(R.id.heroTierOnItemsPage);
            ImageView heroImageOnItemsPage = findViewById(R.id.heroImageOnItemsPage);
            TextView heroNameOnItemsPage = findViewById(R.id.heroNameOnItemsPage);
            TextView heroNewWinRateOnItemsPage = findViewById(R.id.heroNewWinRateOnItemsPage);

            heroTierOnItemsPage.setText(heroItems.getHero().getTier());
            heroImageOnItemsPage.setImageResource(heroItems.getHero().getImage());
            heroNameOnItemsPage.setText(heroItems.getHero().getName());
            heroNewWinRateOnItemsPage.setText(heroItems.getHero().getNewWinRate() + "%");

            Settings settings = new Settings();
            settings.setBestHeroItems(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("bestHeroItems", false));

            HeroItemsTask heroItemsTask = new HeroItemsTask(this, heroItems, settings);
            heroItemsTask.execute();
        }
    }

    @Override
    public void heroItemsProcessFinish(HeroItems heroItems) {
        RecyclerView itemsView = findViewById(R.id.itemsInfoList);
        itemsAdapter = new ItemsAdapter(this, heroItems.getHeroItemsByWinRateDiff());
        itemsView.setAdapter(itemsAdapter);

        SearchView searchView = findViewById(R.id.autoCompleteItemsSearch);
        searchView.setOnQueryTextListener(getSearchTextUpdateListener());
    }

    private SearchView.OnQueryTextListener getSearchTextUpdateListener() {

        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemsAdapter.getFilter().filter(newText);
                return true;
            }
        };
    }
}
