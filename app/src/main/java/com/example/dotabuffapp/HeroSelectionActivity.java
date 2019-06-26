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
        boolean isNullFlag;

        RecyclerView heroesInfoView = (RecyclerView) findViewById(R.id.heroesInfoList);
        HeroInfoAdapter heroInfoAdapter;
        if (arguments != null) {
            int mode = (int) arguments.get("PickOrBan");
            heroPicks = (HeroPicker) arguments.getSerializable("Heroes");
            heroesTier = (HeroTier) arguments.getSerializable("HeroesTier");

            if (!heroPicks.isNullHeroes()) {
                isNullFlag = false;
                heroInfoAdapter = new HeroInfoAdapter(this, false, mode, heroPicks.getSortedHeroesWinDif(true), heroPicks.getSortedHeroesWinDif(false), heroesTier);
            } else {
                isNullFlag = true;
                heroInfoAdapter = new HeroInfoAdapter(this, true, mode, null, null, heroesTier);
            }
            heroesInfoView.setAdapter(heroInfoAdapter);
            heroesInfoView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, heroesInfoView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            intent = new Intent();
                            if (!isNullFlag && mode != 2) {
                                if (mode == 0) {
                                    intent.putExtra("ImageId", heroPicks.getSortedHeroesWinDif(false).get(position).getHeroImage());
                                    intent.putExtra("HeroName", heroPicks.getSortedHeroesWinDif(false).get(position).getName());
                                } else if (mode == 1) {
                                    intent.putExtra("ImageId", heroPicks.getSortedHeroesWinDif(true).get(position).getHeroImage());
                                    intent.putExtra("HeroName", heroPicks.getSortedHeroesWinDif(true).get(position).getName());
                                }
                            } else {
                                intent.putExtra("ImageId", heroesTier.getHeroesTier().get(position).getValue().getKey());
                                intent.putExtra("HeroName", heroesTier.getHeroesTier().get(position).getKey());
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
