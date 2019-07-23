package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements AsyncResponse, Serializable {
    HeroesCountersTask heroCountersTask;
    HeroesCounters heroCounters;
    boolean[] isNotFrame = new boolean[22];
    HeroesPool[] heroesPlaces = new HeroesPool[22];
    HeroesPool[][] heroesPickPlaces = new HeroesPool[5][10];
    HeroesPool[][] heroesBanPlaces = new HeroesPool[5][10];
    int imageViewTagInt;
    boolean isNotAfterEntrance;
    int remainingPickUpdateProcessesNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new PageTurningAdapter(this, getSupportFragmentManager()));
        pager.setCurrentItem(1);
        pager.setOffscreenPageLimit(2);

        heroCounters = new HeroesCounters();
        heroCounters.getHeroesWithTiers().onPostExecuteResponse = this;
        heroCounters.getHeroesWithTiers().execute();
    }

    public void selectHeroOrClear(View view) {
        String imageViewTagString = (String) view.getTag();
        imageViewTagInt = Integer.parseInt(imageViewTagString);
        HeroesPool imageViewHero = heroesPlaces[imageViewTagInt - 1];

        if (isNotAfterEntrance) {
            heroCounters.setAllyCountersByWinRateDiff(
                    heroCountersTask.getHeroesCounters().getAllyCountersByWinRateDiff());
            heroCounters.setEnemyCountersByWinRateDiff(
                    heroCountersTask.getHeroesCounters().getEnemyCountersByWinRateDiff());
            heroCounters.setWinRateDiffBetweenAllyAndEnemyPicks(
                    heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks());
        } else
            isNotAfterEntrance = true;

        if (isNotFrame[imageViewTagInt - 1]) {
            remainingPickUpdateProcessesNumber += 1;

            ImageView currentImage = (ImageView) view;
            currentImage.setImageResource(R.drawable.frame);
            isNotFrame[imageViewTagInt - 1] = false;
            heroesPlaces[imageViewTagInt - 1] = null;

            if (imageViewTagInt >= 1 && imageViewTagInt <= 5) {
                heroCounters.getAllyHeroes().remove(imageViewHero);
            } else if (imageViewTagInt >= 12 && imageViewTagInt <= 16) {
                heroCounters.getEnemyHeroes().remove(imageViewHero);
            } else {
                heroCounters.getBanHeroes().remove(imageViewHero);
            }
            heroCounters.getHeroesWithTiers().addHero(imageViewHero);

            heroCountersTask = new HeroesCountersTask();
            heroCountersTask.onPostExecuteResponse = this;
            heroCountersTask.setHeroesCounters(heroCounters);
            heroCountersTask.execute();
        } else {
            Intent intent = new Intent(this, HeroSelectionActivity.class);

            intent.putExtra("HeroesCounters", heroCounters);
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
        HeroesPool currentHero = heroesPickPlaces[(imageViewTagIntForPick - 1) / 10][(imageViewTagIntForPick - 1) % 10];

        if (currentHero != null) {
            remainingPickUpdateProcessesNumber += 1;

            if (!isNotFrame[0]) {
                currentImage = findViewById(R.id.imageAllyFirstPick);
                isNotFrame[0] = true;
                heroesPlaces[0] = currentHero;
            } else if (!isNotFrame[1]) {
                currentImage = findViewById(R.id.imageAllySecondPick);
                isNotFrame[1] = true;
                heroesPlaces[1] = currentHero;
            } else if (!isNotFrame[2]) {
                currentImage = findViewById(R.id.imageAllyThirdPick);
                isNotFrame[2] = true;
                heroesPlaces[2] = currentHero;
            } else if (!isNotFrame[3]) {
                currentImage = findViewById(R.id.imageAllyFourthPick);
                isNotFrame[3] = true;
                heroesPlaces[3] = currentHero;
            } else {
                currentImage = findViewById(R.id.imageAllyFifthPick);
                isNotFrame[4] = true;
                heroesPlaces[4] = currentHero;
            }

            currentImage.setImageDrawable(pressedImage.getDrawable());
            pressedImage.setImageResource(R.drawable.frame);

            if (isNotAfterEntrance) {
                heroCounters.setAllyCountersByWinRateDiff(
                        heroCountersTask.getHeroesCounters().getAllyCountersByWinRateDiff());
                heroCounters.setEnemyCountersByWinRateDiff(
                        heroCountersTask.getHeroesCounters().getEnemyCountersByWinRateDiff());
                heroCounters.setWinRateDiffBetweenAllyAndEnemyPicks(
                        heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks());
            } else
                isNotAfterEntrance = true;

            heroCounters.getAllyHeroes().add(currentHero);
            heroCounters.getHeroesWithTiers().deleteHero(currentHero);

            heroCountersTask = new HeroesCountersTask();
            heroCountersTask.onPostExecuteResponse = this;
            heroCountersTask.setHeroesCounters(heroCounters);
            heroCountersTask.execute();
        }
    }

    public void selectBanHeroWithPos(View view) {
        ImageView currentImage;
        ImageView pressedImage = (ImageView) view;

        String imageViewTagString = (String) view.getTag();
        int imageViewTagIntForPick = Integer.parseInt(imageViewTagString);
        HeroesPool currentHero = heroesBanPlaces[(imageViewTagIntForPick - 1) / 10][(imageViewTagIntForPick - 1) % 10];

        if (currentHero != null) {
            remainingPickUpdateProcessesNumber += 1;

            if (!isNotFrame[5]) {
                currentImage = findViewById(R.id.imageAllyFirstBan);
                isNotFrame[5] = true;
                heroesPlaces[5] = currentHero;
            } else if (!isNotFrame[6]) {
                currentImage = findViewById(R.id.imageAllySecondBan);
                isNotFrame[6] = true;
                heroesPlaces[6] = currentHero;
            } else if (!isNotFrame[7]) {
                currentImage = findViewById(R.id.imageAllyThirdBan);
                isNotFrame[7] = true;
                heroesPlaces[7] = currentHero;
            } else if (!isNotFrame[8]) {
                currentImage = findViewById(R.id.imageAllyFourthBan);
                isNotFrame[8] = true;
                heroesPlaces[8] = currentHero;
            } else if (!isNotFrame[9]) {
                currentImage = findViewById(R.id.imageAllyFifthBan);
                isNotFrame[9] = true;
                heroesPlaces[9] = currentHero;
            } else {
                currentImage = findViewById(R.id.imageAllySixthBan);
                isNotFrame[10] = true;
                heroesPlaces[10] = currentHero;
            }

            currentImage.setImageDrawable(pressedImage.getDrawable());
            pressedImage.setImageResource(R.drawable.frame);

            if (isNotAfterEntrance) {
                heroCounters.setAllyCountersByWinRateDiff(
                        heroCountersTask.getHeroesCounters().getAllyCountersByWinRateDiff());
                heroCounters.setEnemyCountersByWinRateDiff(
                        heroCountersTask.getHeroesCounters().getEnemyCountersByWinRateDiff());
                heroCounters.setWinRateDiffBetweenAllyAndEnemyPicks(
                        heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks());
            } else
                isNotAfterEntrance = true;

            heroCounters.getBanHeroes().add(currentHero);
            heroCounters.getHeroesWithTiers().deleteHero(currentHero);

            heroCountersTask.getHeroesCounters().deleteBanHeroFromLists(currentHero);
            heroesCountersProcessFinish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                remainingPickUpdateProcessesNumber += 1;
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

                HeroesPool currentHero = HeroesPool.valueOf(changedHeroName);
                heroCounters.getHeroesWithTiers().deleteHero(currentHero);
                if (imageViewTagInt >= 1 && imageViewTagInt <= 5) {
                    if (!isNotFrame[0]) {
                        currentImage = findViewById(R.id.imageAllyFirstPick);
                        isNotFrame[0] = true;
                        heroesPlaces[0] = currentHero;
                    } else if (!isNotFrame[1]) {
                        currentImage = findViewById(R.id.imageAllySecondPick);
                        isNotFrame[1] = true;
                        heroesPlaces[1] = currentHero;
                    } else if (!isNotFrame[2]) {
                        currentImage = findViewById(R.id.imageAllyThirdPick);
                        isNotFrame[2] = true;
                        heroesPlaces[2] = currentHero;
                    } else if (!isNotFrame[3]) {
                        currentImage = findViewById(R.id.imageAllyFourthPick);
                        isNotFrame[3] = true;
                        heroesPlaces[3] = currentHero;
                    } else {
                        currentImage = findViewById(R.id.imageAllyFifthPick);
                        isNotFrame[4] = true;
                        heroesPlaces[4] = currentHero;
                    }

                    heroCounters.getAllyHeroes().add(currentHero);
                    heroCountersTask.setHeroesCounters(heroCounters);
                    heroCountersTask.execute();
                } else if (imageViewTagInt >= 6 && imageViewTagInt <= 11) {
                    if (!isNotFrame[5]) {
                        currentImage = findViewById(R.id.imageAllyFirstBan);
                        isNotFrame[5] = true;
                        heroesPlaces[5] = currentHero;
                    } else if (!isNotFrame[6]) {
                        currentImage = findViewById(R.id.imageAllySecondBan);
                        isNotFrame[6] = true;
                        heroesPlaces[6] = currentHero;
                    } else if (!isNotFrame[7]) {
                        currentImage = findViewById(R.id.imageAllyThirdBan);
                        isNotFrame[7] = true;
                        heroesPlaces[7] = currentHero;
                    } else if (!isNotFrame[8]) {
                        currentImage = findViewById(R.id.imageAllyFourthBan);
                        isNotFrame[8] = true;
                        heroesPlaces[8] = currentHero;
                    } else if (!isNotFrame[9]) {
                        currentImage = findViewById(R.id.imageAllyFifthBan);
                        isNotFrame[9] = true;
                        heroesPlaces[9] = currentHero;
                    } else {
                        currentImage = findViewById(R.id.imageAllySixthBan);
                        isNotFrame[10] = true;
                        heroesPlaces[10] = currentHero;
                    }

                    heroCounters.getBanHeroes().add(currentHero);
                    heroCountersTask.getHeroesCounters().deleteBanHeroFromLists(currentHero);
                    heroesCountersProcessFinish();
                } else if (imageViewTagInt >= 12 && imageViewTagInt <= 16) {
                    if (!isNotFrame[11]) {
                        currentImage = findViewById(R.id.imageEnemyFirstPick);
                        isNotFrame[11] = true;
                        heroesPlaces[11] = currentHero;
                    } else if (!isNotFrame[12]) {
                        currentImage = findViewById(R.id.imageEnemySecondPick);
                        isNotFrame[12] = true;
                        heroesPlaces[12] = currentHero;
                    } else if (!isNotFrame[13]) {
                        currentImage = findViewById(R.id.imageEnemyThirdPick);
                        isNotFrame[13] = true;
                        heroesPlaces[13] = currentHero;
                    } else if (!isNotFrame[14]) {
                        currentImage = findViewById(R.id.imageEnemyFourthPick);
                        isNotFrame[14] = true;
                        heroesPlaces[14] = currentHero;
                    } else {
                        currentImage = findViewById(R.id.imageEnemyFifthPick);
                        isNotFrame[15] = true;
                        heroesPlaces[15] = currentHero;
                    }

                    heroCounters.getEnemyHeroes().add(currentHero);
                    heroCountersTask.setHeroesCounters(heroCounters);
                    heroCountersTask.execute();
                } else {
                    if (!isNotFrame[16]) {
                        currentImage = findViewById(R.id.imageEnemyFirstBan);
                        isNotFrame[16] = true;
                        heroesPlaces[16] = currentHero;
                    } else if (!isNotFrame[17]) {
                        currentImage = findViewById(R.id.imageEnemySecondBan);
                        isNotFrame[17] = true;
                        heroesPlaces[17] = currentHero;
                    } else if (!isNotFrame[18]) {
                        currentImage = findViewById(R.id.imageEnemyThirdBan);
                        isNotFrame[18] = true;
                        heroesPlaces[18] = currentHero;
                    } else if (!isNotFrame[19]) {
                        currentImage = findViewById(R.id.imageEnemyFourthBan);
                        isNotFrame[19] = true;
                        heroesPlaces[19] = currentHero;
                    } else if (!isNotFrame[20]) {
                        currentImage = findViewById(R.id.imageEnemyFifthBan);
                        isNotFrame[20] = true;
                        heroesPlaces[20] = currentHero;
                    } else {
                        currentImage = findViewById(R.id.imageEnemySixthBan);
                        isNotFrame[21] = true;
                        heroesPlaces[21] = currentHero;
                    }

                    heroCounters.getBanHeroes().add(currentHero);
                    heroCountersTask.getHeroesCounters().deleteBanHeroFromLists(currentHero);
                    heroesCountersProcessFinish();
                }
                currentImage.setImageResource(imageRes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void heroesWithTiersProcessFinish() {
        Toast.makeText(this, "Винрейты героев загружены", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void heroesCountersProcessFinish() {
        TextView tv = findViewById(R.id.picksWinRateDif);
        if (!heroCountersTask.getHeroesCounters().getAllyHeroes().isEmpty() &&
                !heroCountersTask.getHeroesCounters().getEnemyHeroes().isEmpty()) {
            if (heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks() > 0.0)
                tv.setText("+" + heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks().toString() + "%");
            else
                tv.setText(heroCountersTask.getHeroesCounters().getWinRateDiffBetweenAllyAndEnemyPicks().toString() + "%");
        } else {
            tv.setText("");
        }
        for (Integer pos = 1; pos <= 5; pos++) {
            int currentNumberOfHeroesForOnePos = 1;

            for (Hero h : heroCountersTask.getHeroesCounters().getAllyCountersByWinRateDiff()) {
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
                        double valueDif = h.getWinRateDiff();
                        double valueWinRate = h.getNewWinRate();

                        if (HeroesPool.valueOf(newKey).pos.contains(pos.toString())) {
                            ImageView currentImage;
                            if (pos.toString().equals("1")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFirstPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFirstPosBanHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFirstPosBanHero);

                                }
                            } else if (pos.toString().equals("2")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthSecondPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthSecondPosBanHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthSecondPosBanHero);

                                }
                            } else if (pos.toString().equals("3")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthThirdPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthThirdPosBanHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthThirdPosBanHero);

                                }
                            } else if (pos.toString().equals("4")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFourthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFourthPosBanHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFourthPosBanHero);

                                }
                            } else {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFifthPosBanHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFifthPosBanHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFifthPosBanHero);

                                }
                            }
                            if (valueDif >= -1.0) {
                                currentImage.setImageResource(h.getImage());
                                heroesBanPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = HeroesPool.valueOf(newKey);
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

            for (Hero h : heroCountersTask.getHeroesCounters().getEnemyCountersByWinRateDiff()) {
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
                        double valueDif = h.getWinRateDiff();
                        double valueWinRate = h.getNewWinRate();

                        if (TeamHeroesPool.valueOf(newKey).pos.contains(pos.toString())) {
                            ImageView currentImage;
                            if (pos.toString().equals("1")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFirstPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFirstPosPickHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFirstPosPickHero);

                                }
                            } else if (pos.toString().equals("2")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthSecondPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthSecondPosPickHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthSecondPosPickHero);

                                }
                            } else if (pos.toString().equals("3")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthThirdPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthThirdPosPickHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthThirdPosPickHero);

                                }
                            } else if (pos.toString().equals("4")) {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFourthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFourthPosPickHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFourthPosPickHero);

                                }
                            } else {
                                if (currentNumberOfHeroesForOnePos == 1) {
                                    currentImage = findViewById(R.id.firstFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 2) {
                                    currentImage = findViewById(R.id.secondFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 3) {
                                    currentImage = findViewById(R.id.thirdFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 4) {
                                    currentImage = findViewById(R.id.fourthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 5) {
                                    currentImage = findViewById(R.id.fifthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 6) {
                                    currentImage = findViewById(R.id.sixthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 7) {
                                    currentImage = findViewById(R.id.seventhFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 8) {
                                    currentImage = findViewById(R.id.eighthFifthPosPickHero);

                                } else if (currentNumberOfHeroesForOnePos == 9) {
                                    currentImage = findViewById(R.id.ninthFifthPosPickHero);

                                } else {
                                    currentImage = findViewById(R.id.tenthFifthPosPickHero);

                                }
                            }
                            if (valueDif >= -1.0) {
                                currentImage.setImageResource(h.getImage());
                                heroesPickPlaces[pos - 1][currentNumberOfHeroesForOnePos - 1] = HeroesPool.valueOf(newKey);
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

        remainingPickUpdateProcessesNumber -= 1;
        if (remainingPickUpdateProcessesNumber > 0)
            Toast.makeText(this, "Пики обновлены (" + remainingPickUpdateProcessesNumber + ")", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Пики обновлены", Toast.LENGTH_SHORT).show();
    }
}
