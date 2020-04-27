package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class SettingsActivity extends AppCompatActivity {
    private HeroesCounters heroesCounters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Bundle arguments = getIntent().getExtras();
        heroesCounters = (HeroesCounters) arguments.getSerializable("HeroesCounters");

        getFragmentManager()
                .beginTransaction()
                .add(R.id.settings_content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings);
        }
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
}
