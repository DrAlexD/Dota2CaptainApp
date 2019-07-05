package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse, Serializable {
    HeroPicker heroPicks;
    HeroTier heroTier;
    boolean[] isNotFrame = new boolean[22];
    Heroes[] heroesPlaces = new Heroes[22];
    Heroes[][] heroesPickPlaces = new Heroes[5][10];
    Heroes[][] heroesBanPlaces = new Heroes[5][10];
    int imageViewTagInt;
    boolean isNotAfterEntrance;
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
        pager.setOffscreenPageLimit(2);

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

        if (isNotAfterEntrance) {
            allySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(true);
            enemySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(false);
        } else
            isNotAfterEntrance = true;

        heroPicks = new HeroPicker();
        heroPicks.delegate = this;
        heroPicks.setTier(heroTier);
        heroPicks.setAllyHeroes(allyHeroes);
        heroPicks.setEnemyHeroes(enemyHeroes);
        heroPicks.setBanHeroes(banHeroes);
        heroPicks.setSortedHeroesWinDif(allySortedHeroesWinDif, enemySortedHeroesWinDif);

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

    public void selectPickHeroWithPos(View view) {
        ImageView currentImage;
        ImageView pressedImage = (ImageView) view;

        String imageViewTagString = (String) view.getTag();
        int imageViewTagIntForPick = Integer.parseInt(imageViewTagString);
        Heroes currentHero = heroesPickPlaces[(imageViewTagIntForPick - 1) / 10][(imageViewTagIntForPick - 1) % 10];

        if (currentHero != null) {

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

            currentImage.setImageDrawable(pressedImage.getDrawable());
            pressedImage.setImageResource(R.drawable.frame);

            if (isNotAfterEntrance) {
                allySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(true);
                enemySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(false);
            } else
                isNotAfterEntrance = true;

            heroPicks = new HeroPicker();
            heroPicks.delegate = this;
            heroPicks.setTier(heroTier);
            heroPicks.setAllyHeroes(allyHeroes);
            heroPicks.setEnemyHeroes(enemyHeroes);
            heroPicks.setBanHeroes(banHeroes);
            heroPicks.setSortedHeroesWinDif(allySortedHeroesWinDif, enemySortedHeroesWinDif);

            allyHeroes.add(currentHero);
            heroPicks.addAllyHero(currentHero);
            heroPicks.execute();

            heroTier.deleteHero(currentHero);
        }
    }

    public void selectBanHeroWithPos(View view) {
        ImageView currentImage;
        ImageView pressedImage = (ImageView) view;

        String imageViewTagString = (String) view.getTag();
        int imageViewTagIntForPick = Integer.parseInt(imageViewTagString);
        Heroes currentHero = heroesBanPlaces[(imageViewTagIntForPick - 1) / 10][(imageViewTagIntForPick - 1) % 10];

        if (currentHero != null) {

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

            currentImage.setImageDrawable(pressedImage.getDrawable());
            pressedImage.setImageResource(R.drawable.frame);

            if (isNotAfterEntrance) {
                allySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(true);
                enemySortedHeroesWinDif = heroPicks.getSortedHeroesWinDif(false);
            } else
                isNotAfterEntrance = true;

            heroPicks = new HeroPicker();
            heroPicks.delegate = this;
            heroPicks.setTier(heroTier);
            heroPicks.setAllyHeroes(allyHeroes);
            heroPicks.setEnemyHeroes(enemyHeroes);
            heroPicks.setBanHeroes(banHeroes);
            heroPicks.setSortedHeroesWinDif(allySortedHeroesWinDif, enemySortedHeroesWinDif);

            banHeroes.add(currentHero);
            heroPicks.deleteBanHeroFromLists(currentHero);
            processFinish();

            heroTier.deleteHero(currentHero);
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
                    heroPicks.deleteBanHeroFromLists(currentHero);
                    processFinish();
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
                    heroPicks.deleteBanHeroFromLists(currentHero);
                    processFinish();
                }
                heroTier.deleteHero(currentHero);
                currentImage.setImageResource(imageRes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void processFinish() {
        for (Integer pos = 1; pos <= 5; pos++) {
            int currentNumberOfHeroesForOnePos = 1;

            for (HeroInfo h : heroPicks.getSortedHeroesWinDif(true)) {
                if (currentNumberOfHeroesForOnePos <= 10) {
                    String key = h.getName();
                    String newKey = key.replace(" ", "");
                    switch (key) {
                        case "Anti-Mage":
                            newKey = "AntiMage";
                            break;
                        case "Keeper of the Light":
                            newKey = "KeeperOfTheLight";
                            break;
                        case "Queen of Pain":
                            newKey = "QueenOfPain";
                            break;
                        case "Nature's Prophet":
                            newKey = "NaturesProphet";
                            break;
                    }

                    try {
                        //HeroPool.valueOf(newKey);
                        double valueDif = h.getWinRateDif();
                        double valueWinRate = h.getNewWinRate();

                        if (Heroes.valueOf(newKey).pos.contains(pos.toString())) {
                            ImageView currentImage;
                            if (pos.toString().equals("1")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFirstPosBanHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFirstPosBanHero);

                                }
                            } else if (pos.toString().equals("2")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthSecondPosBanHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthSecondPosBanHero);

                                }
                            } else if (pos.toString().equals("3")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthThirdPosBanHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthThirdPosBanHero);

                                }
                            } else if (pos.toString().equals("4")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFourthPosBanHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFourthPosBanHero);

                                }
                            } else {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFifthPosBanHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFifthPosBanHero);

                                }
                            }
                            if (valueDif >= -1.0) {
                                currentImage.setImageResource(h.getImage());
                                heroesBanPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = Heroes.valueOf(newKey);
                            } else {
                                currentImage.setImageResource(R.drawable.frame);
                                heroesBanPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = null;
                            }
                            currentNumberOfHeroesForOnePos++;
                        }
                    } catch (IllegalArgumentException e) {
                        //
                    }
                } else
                    break;
            }
        }

        for (Integer pos = 1; pos <= 5; pos++) {
            int currentNumberOfHeroesForOnePos = 1;

            for (HeroInfo h : heroPicks.getSortedHeroesWinDif(false)) {
                if (currentNumberOfHeroesForOnePos <= 10) {
                    String key = h.getName();
                    String newKey = key.replace(" ", "");
                    switch (key) {
                        case "Anti-Mage":
                            newKey = "AntiMage";
                            break;
                        case "Keeper of the Light":
                            newKey = "KeeperOfTheLight";
                            break;
                        case "Queen of Pain":
                            newKey = "QueenOfPain";
                            break;
                        case "Nature's Prophet":
                            newKey = "NaturesProphet";
                            break;
                    }

                    try {
                        //HeroPool.valueOf(newKey);
                        double valueDif = h.getWinRateDif();
                        double valueWinRate = h.getNewWinRate();

                        if (Heroes.valueOf(newKey).pos.contains(pos.toString())) {
                            ImageView currentImage;
                            if (pos.toString().equals("1")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFirstPosPickHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFirstPosPickHero);

                                }
                            } else if (pos.toString().equals("2")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthSecondPosPickHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthSecondPosPickHero);

                                }
                            } else if (pos.toString().equals("3")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthThirdPosPickHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthThirdPosPickHero);

                                }
                            } else if (pos.toString().equals("4")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFourthPosPickHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFourthPosPickHero);

                                }
                            } else {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = (ImageView) findViewById(R.id.firstFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = (ImageView) findViewById(R.id.secondFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = (ImageView) findViewById(R.id.thirdFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = (ImageView) findViewById(R.id.fourthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = (ImageView) findViewById(R.id.fifthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = (ImageView) findViewById(R.id.sixthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = (ImageView) findViewById(R.id.seventhFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = (ImageView) findViewById(R.id.eighthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = (ImageView) findViewById(R.id.ninthFifthPosPickHero);

                                } else {
                                    currentImage = (ImageView) findViewById(R.id.tenthFifthPosPickHero);

                                }
                            }
                            if (valueDif >= -1.0) {
                                currentImage.setImageResource(h.getImage());
                                heroesPickPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = Heroes.valueOf(newKey);
                            } else {
                                currentImage.setImageResource(R.drawable.frame);
                                heroesPickPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = null;
                            }
                            currentNumberOfHeroesForOnePos++;
                        }
                    } catch (IllegalArgumentException e) {
                        //
                    }
                } else
                    break;
            }
        }

        Toast.makeText(this, "Пики обновлены", Toast.LENGTH_SHORT).show();
    }
}
