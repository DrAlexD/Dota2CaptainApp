package com.example.dotabuffapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HeroSelectionActivity extends AppCompatActivity {

    private List<HeroInfo> heroes = new ArrayList();

    ListView heroesList;

    private void setInitialData() {
        heroes.add(new HeroInfo("Abaddon", "Бразилиа", R.drawable.abaddon));
        heroes.add(new HeroInfo("Alchemist", "Буэнос-Айрес", R.drawable.alchemist));
        heroes.add(new HeroInfo("Anti-Mage", "Богота", R.drawable.anti_mage));
        heroes.add(new HeroInfo("ArcWarden", "Монтевидео", R.drawable.arc_warden));
        heroes.add(new HeroInfo("Ancient Apparition", "Сантьяго", R.drawable.ancient_apparition));
        heroes.add(new HeroInfo("Axe", "Сантьяго", R.drawable.axe));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_activity);
        setInitialData();
        heroesList = (ListView) findViewById(R.id.heroesList);
        HeroInfoAdapter stateAdapter = new HeroInfoAdapter(this, R.layout.hero_info_item, heroes);
        heroesList.setAdapter(stateAdapter);
    }
}
