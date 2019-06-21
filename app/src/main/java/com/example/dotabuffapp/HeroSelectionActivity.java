package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeroSelectionActivity extends AppCompatActivity {

    private ArrayList<HeroInfo> heroesInfo = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_selection_activity);

        setInitialData();
        RecyclerView heroesInfoView = (RecyclerView) findViewById(R.id.heroesInfoList);
        HeroInfoAdapter heroInfoAdapter = new HeroInfoAdapter(this, heroesInfo);
        heroesInfoView.setAdapter(heroInfoAdapter);
        heroesInfoView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, heroesInfoView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        intent = new Intent();
                        intent.putExtra("ImageId", heroesInfo.get(position).getHeroImage());
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
    }

    private void setInitialData() {
        heroesInfo.add(new HeroInfo("Abaddon", "Бразилиа", R.drawable.abaddon));
        heroesInfo.add(new HeroInfo("Alchemist", "Буэнос-Айрес", R.drawable.alchemist));
        heroesInfo.add(new HeroInfo("Anti-Mage", "Богота", R.drawable.anti_mage));
        heroesInfo.add(new HeroInfo("ArcWarden", "Монтевидео", R.drawable.arc_warden));
        heroesInfo.add(new HeroInfo("Ancient Apparition", "Сантьяго", R.drawable.ancient_apparition));
        heroesInfo.add(new HeroInfo("Axe", "Сантьяго", R.drawable.axe));
    }
}
