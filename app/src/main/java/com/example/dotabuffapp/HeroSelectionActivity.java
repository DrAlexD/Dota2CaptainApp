package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class HeroSelectionActivity extends AppCompatActivity {
    private Intent intent;

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
        HeroInfoAdapter heroInfoAdapter;
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
                System.out.println("IN FIRST INIT" + heroPicks.getSortedHeroesWinDif(false).size());
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
                            intent = new Intent();
                            if (mode == 0 && !isNullEnemyFlag) {
                                intent.putExtra("ImageId", heroPicks.getSortedHeroesWinDif(false).get(position).getImage());
                                intent.putExtra("HeroName", heroPicks.getSortedHeroesWinDif(false).get(position).getName());
                            } else if (mode == 1 && !isNullAllyFlag) {
                                intent.putExtra("ImageId", heroPicks.getSortedHeroesWinDif(true).get(position).getImage());
                                intent.putExtra("HeroName", heroPicks.getSortedHeroesWinDif(true).get(position).getName());
                            } else {
                                intent.putExtra("ImageId", heroesTier.getHeroesTier().get(position).getImage());
                                intent.putExtra("HeroName", heroesTier.getHeroesTier().get(position).getName());
                            }
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    })
            );
        }
    }
}
