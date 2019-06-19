package com.example.dotabuffapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HeroSelectionActivity extends AppCompatActivity {

    private List<HeroInfo> heroesInfo = new ArrayList();

    ListView heroesInfoList;

    private void setInitialData() {
        heroesInfo.add(new HeroInfo("Abaddon", "Бразилиа", R.drawable.abaddon));
        heroesInfo.add(new HeroInfo("Alchemist", "Буэнос-Айрес", R.drawable.alchemist));
        heroesInfo.add(new HeroInfo("Anti-Mage", "Богота", R.drawable.anti_mage));
        heroesInfo.add(new HeroInfo("ArcWarden", "Монтевидео", R.drawable.arc_warden));
        heroesInfo.add(new HeroInfo("Ancient Apparition", "Сантьяго", R.drawable.ancient_apparition));
        heroesInfo.add(new HeroInfo("Axe", "Сантьяго", R.drawable.axe));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_activity);
        setInitialData();
        heroesInfoList = (ListView) findViewById(R.id.heroesInfoList);
        HeroInfoAdapter heroInfoAdapter = new HeroInfoAdapter(this, R.layout.hero_info_item, heroesInfo);
        heroesInfoList.setAdapter(heroInfoAdapter);
    }
}
