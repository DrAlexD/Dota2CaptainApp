package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    HeroPicker heroPicks;
    HeroTier heroTier;
    boolean[] isNotFrame = new boolean[22];
    Heroes[] heroesPlaces = new Heroes[22];
    int imageViewTagInt;
    boolean firstIn;
    ArrayList<Heroes> allyHeroes; //союзные герои
    ArrayList<Heroes> enemyHeroes;
    ArrayList<Heroes> banHeroes;
    ArrayList<HeroInfo> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    ArrayList<HeroInfo> enemySortedHeroesWinDif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(this, getSupportFragmentManager()));
        pager.setCurrentItem(1);

        heroTier = new HeroTier(getApplicationContext());
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        banHeroes = new ArrayList<>();
        allySortedHeroesWinDif = new ArrayList<>();
        enemySortedHeroesWinDif = new ArrayList<>();
        heroTier.execute();
    }

    public void selectHeroOrClear(View view) {
        String imageViewTagString = (String) view.getTag();
        imageViewTagInt = Integer.parseInt(imageViewTagString);
        Heroes imageViewHero = heroesPlaces[imageViewTagInt - 1];
        if (isNotFrame[imageViewTagInt - 1]) {
            heroTier.addHero(imageViewHero);
            if (imageViewTagInt >= 1 && imageViewTagInt <= 5) {
                allyHeroes.remove(imageViewHero);
                heroPicks.deleteAllyHero(imageViewHero);
            } else if (imageViewTagInt >= 12 && imageViewTagInt <= 16) {
                enemyHeroes.remove(imageViewHero);
                heroPicks.deleteEnemyHero(imageViewHero);
            } else {
                banHeroes.remove(imageViewHero);
                heroPicks.deleteBanHero(imageViewHero);
            }
            ImageView currentImage = (ImageView) view;
            currentImage.setImageResource(R.drawable.frame);
            isNotFrame[imageViewTagInt - 1] = false;
            heroesPlaces[imageViewTagInt - 1] = null;
            heroPicks.execute();
        } else {
            Intent intent = new Intent(this, HeroSelectionActivity.class);
            if (firstIn) {
                allySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(true);
                enemySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(false);
            } else
                firstIn = true;

            heroPicks = new HeroPicker(getApplicationContext(), getCurrentFocus());
            heroPicks.setTier(heroTier);
            heroPicks.setAllyHeroes(allyHeroes);
            heroPicks.setEnemyHeroes(enemyHeroes);
            heroPicks.setBanHeroes(banHeroes);
            heroPicks.setSortedHeroesWinDif(allySortedHeroesWinDif, enemySortedHeroesWinDif);

            intent.putExtra("HeroesTier", heroTier);
            intent.putExtra("Heroes", heroPicks);
            if (imageViewTagInt >= 1 && imageViewTagInt <= 5)
                intent.putExtra("PickOrBan", 0);
            else if (imageViewTagInt >= 6 && imageViewTagInt <= 11)
                intent.putExtra("PickOrBan", 1);
            else if (imageViewTagInt >= 12 && imageViewTagInt <= 22)
                intent.putExtra("PickOrBan", 2);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int imageRes;
                String heroName;
                ImageView currentImage;

                imageRes = data.getIntExtra("ImageId", 0);
                heroName = data.getStringExtra("HeroName");

                String changedHeroName = heroName.replace(" ", "");
                switch (changedHeroName) {
                    case "Anti-Mage":
                        changedHeroName = "AntiMage";
                        break;
                    case "KeeperoftheLight":
                        changedHeroName = "KeeperOfTheLight";
                        break;
                    case "QueenofPain":
                        changedHeroName = "QueenOfPain";
                        break;
                    case "Nature'sProphet":
                        changedHeroName = "NaturesProphet";
                        break;
                }

                Heroes currentHero = Heroes.valueOf(changedHeroName);
                if (imageViewTagInt >= 1 && imageViewTagInt <= 5) {
                    if (!isNotFrame[0]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFirstPick);
                        isNotFrame[0] = true;
                        heroesPlaces[0] = currentHero;
                    } else if (!isNotFrame[1]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllySecondPick);
                        isNotFrame[1] = true;
                        heroesPlaces[1] = currentHero;
                    } else if (!isNotFrame[2]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyThirdPick);
                        isNotFrame[2] = true;
                        heroesPlaces[2] = currentHero;
                    } else if (!isNotFrame[3]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFourthPick);
                        isNotFrame[3] = true;
                        heroesPlaces[3] = currentHero;
                    } else {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFifthPick);
                        isNotFrame[4] = true;
                        heroesPlaces[4] = currentHero;
                    }

                    allyHeroes.add(currentHero);
                    heroPicks.addAllyHero(currentHero);
                    heroPicks.execute();
                } else if (imageViewTagInt >= 6 && imageViewTagInt <= 11) {
                    if (!isNotFrame[5]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFirstBan);
                        isNotFrame[5] = true;
                        heroesPlaces[5] = currentHero;
                    } else if (!isNotFrame[6]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllySecondBan);
                        isNotFrame[6] = true;
                        heroesPlaces[6] = currentHero;
                    } else if (!isNotFrame[7]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyThirdBan);
                        isNotFrame[7] = true;
                        heroesPlaces[7] = currentHero;
                    } else if (!isNotFrame[8]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFourthBan);
                        isNotFrame[8] = true;
                        heroesPlaces[8] = currentHero;
                    } else if (!isNotFrame[9]) {
                        currentImage = (ImageView) findViewById(R.id.imageAllyFifthBan);
                        isNotFrame[9] = true;
                        heroesPlaces[9] = currentHero;
                    } else {
                        currentImage = (ImageView) findViewById(R.id.imageAllySixthBan);
                        isNotFrame[10] = true;
                        heroesPlaces[10] = currentHero;
                    }

                    banHeroes.add(currentHero);
                    heroPicks.addBanHero(currentHero);
                    heroPicks.deleteBanHeroFromLists(currentHero);
                } else if (imageViewTagInt >= 12 && imageViewTagInt <= 16) {
                    if (!isNotFrame[11]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFirstPick);
                        isNotFrame[11] = true;
                        heroesPlaces[11] = currentHero;
                    } else if (!isNotFrame[12]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemySecondPick);
                        isNotFrame[12] = true;
                        heroesPlaces[12] = currentHero;
                    } else if (!isNotFrame[13]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyThirdPick);
                        isNotFrame[13] = true;
                        heroesPlaces[13] = currentHero;
                    } else if (!isNotFrame[14]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFourthPick);
                        isNotFrame[14] = true;
                        heroesPlaces[14] = currentHero;
                    } else {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFifthPick);
                        isNotFrame[15] = true;
                        heroesPlaces[15] = currentHero;
                    }

                    enemyHeroes.add(currentHero);
                    heroPicks.addEnemyHero(currentHero);
                    heroPicks.execute();
                } else {
                    if (!isNotFrame[16]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFirstBan);
                        isNotFrame[16] = true;
                        heroesPlaces[16] = currentHero;
                    } else if (!isNotFrame[17]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemySecondBan);
                        isNotFrame[17] = true;
                        heroesPlaces[17] = currentHero;
                    } else if (!isNotFrame[18]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyThirdBan);
                        isNotFrame[18] = true;
                        heroesPlaces[18] = currentHero;
                    } else if (!isNotFrame[19]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFourthBan);
                        isNotFrame[19] = true;
                        heroesPlaces[19] = currentHero;
                    } else if (!isNotFrame[20]) {
                        currentImage = (ImageView) findViewById(R.id.imageEnemyFifthBan);
                        isNotFrame[20] = true;
                        heroesPlaces[20] = currentHero;
                    } else {
                        currentImage = (ImageView) findViewById(R.id.imageEnemySixthBan);
                        isNotFrame[21] = true;
                        heroesPlaces[21] = currentHero;
                    }

                    banHeroes.add(currentHero);
                    heroPicks.addBanHero(currentHero);
                    heroPicks.deleteBanHeroFromLists(currentHero);
                }
                heroTier.deleteHero(currentHero);
                currentImage.setImageResource(imageRes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
