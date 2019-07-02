package com.example.dotabuffapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class HeroPicker extends AsyncTask<Void, Void, Void> implements Serializable {
    private HeroTier tiers;
    private ArrayList<Heroes> allyHeroes; //союзные герои
    private ArrayList<Heroes> enemyHeroes;
    private ArrayList<Heroes> banHeroes;
    private ArrayList<HeroInfo> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif;
    private Double sumWinDif; //насколько хороши союзные герои против вражеских
    private transient Context context;
    private transient MainActivity mainActivity;

    HeroPicker(Context context, MainActivity mainActivity) {
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        banHeroes = new ArrayList<>();
        allySortedHeroesWinDif = new ArrayList<>();
        enemySortedHeroesWinDif = new ArrayList<>();
        sumWinDif = 0.0;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    ArrayList<HeroInfo> getSortedHeroesWinDif(boolean isAllyCounters) {
        return (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;
    }

    void setSortedHeroesWinDif(ArrayList<HeroInfo> allySortedHeroesWinDif, ArrayList<HeroInfo> enemySortedHeroesWinDif) {
        this.allySortedHeroesWinDif.addAll(allySortedHeroesWinDif);
        this.enemySortedHeroesWinDif.addAll(enemySortedHeroesWinDif);
    }

    boolean isNullAllyHeroes() {
        return allyHeroes.isEmpty();
    }

    boolean isNullEnemyHeroes() {
        return enemyHeroes.isEmpty();
    }

    void addAllyHero(Heroes hero) {
        allyHeroes.add(hero);
    }

    void deleteAllyHero(Heroes currentHero) {
        String key = currentHero.toString();
        String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
        switch (key) {
            case "AntiMage":
                newKey = "Anti-Mage";
                break;
            case "KeeperOfTheLight":
                newKey = "Keeper of the Light";
                break;
            case "QueenOfPain":
                newKey = "Queen of Pain";
                break;
            case "NaturesProphet":
                newKey = "Nature's Prophet";
                break;
        }

        if (!isNullAllyHeroes()) {
            int k = 0;
            for (Heroes h : allyHeroes) {
                if (h.toString().equals(newKey)) {
                    allyHeroes.remove(k);
                    break;
                }
                k++;
            }
        }
    }

    void addEnemyHero(Heroes hero) {
        enemyHeroes.add(hero);
    }

    void deleteEnemyHero(Heroes currentHero) {
        String key = currentHero.toString();
        String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
        switch (key) {
            case "AntiMage":
                newKey = "Anti-Mage";
                break;
            case "KeeperOfTheLight":
                newKey = "Keeper of the Light";
                break;
            case "QueenOfPain":
                newKey = "Queen of Pain";
                break;
            case "NaturesProphet":
                newKey = "Nature's Prophet";
                break;
        }

        if (!isNullEnemyHeroes()) {
            int k = 0;
            for (Heroes h : enemyHeroes) {
                if (h.toString().equals(newKey)) {
                    enemyHeroes.remove(k);
                    break;
                }
                k++;
            }
        }
    }

    void addBanHero(Heroes hero) {
        banHeroes.add(hero);
    }

    void deleteBanHero(Heroes currentHero) {
        String key = currentHero.toString();
        String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
        switch (key) {
            case "AntiMage":
                newKey = "Anti-Mage";
                break;
            case "KeeperOfTheLight":
                newKey = "Keeper of the Light";
                break;
            case "QueenOfPain":
                newKey = "Queen of Pain";
                break;
            case "NaturesProphet":
                newKey = "Nature's Prophet";
                break;
        }

        int k = 0;
        for (Heroes h : banHeroes) {
            if (h.toString().equals(newKey)) {
                banHeroes.remove(k);
                break;
            }
            k++;
        }
    }

    void setAllyHeroes(ArrayList<Heroes> heroes) {
        allyHeroes.addAll(heroes);
    }

    void setEnemyHeroes(ArrayList<Heroes> heroes) {
        enemyHeroes.addAll(heroes);
    }

    void setBanHeroes(ArrayList<Heroes> heroes) {
        banHeroes.addAll(heroes);
    }

    Double getSumWinDif() {
        return Math.round(sumWinDif * 100.0) / 100.0;
    }

    void setTier(HeroTier tiers) {
        this.tiers = tiers;
    }

    void deleteBanHeroFromLists(Heroes currentHero) {
        String key = currentHero.toString();
        String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
        switch (key) {
            case "AntiMage":
                newKey = "Anti-Mage";
                break;
            case "KeeperOfTheLight":
                newKey = "Keeper of the Light";
                break;
            case "QueenOfPain":
                newKey = "Queen of Pain";
                break;
            case "NaturesProphet":
                newKey = "Nature's Prophet";
                break;
        }

        if (!isNullAllyHeroes()) {
            int k = 0;
            for (HeroInfo h : allySortedHeroesWinDif) {
                if (h.getName().equals(newKey)) {
                    allySortedHeroesWinDif.remove(k);
                    break;
                }
                k++;
            }
        }

        if (!isNullEnemyHeroes()) {
            int j = 0;
            for (HeroInfo h : enemySortedHeroesWinDif) {
                if (h.getName().equals(newKey)) {
                    enemySortedHeroesWinDif.remove(j);
                    break;
                }
                j++;
            }
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        for (Integer pos = 1; pos <= 5; pos++) {
            int currentNumberOfHeroesForOnePos = 1;

            for (HeroInfo h : allySortedHeroesWinDif) {
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
                            if (valueDif >= -1.0) {
                                ImageView currentImage;
                                if (pos.toString().equals("1")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFirstPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFirstPosBanHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFirstPosBanHero);
                                    }
                                } else if (pos.toString().equals("2")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthSecondPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthSecondPosBanHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthSecondPosBanHero);
                                    }
                                } else if (pos.toString().equals("3")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthThirdPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthThirdPosBanHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthThirdPosBanHero);
                                    }
                                } else if (pos.toString().equals("4")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFourthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFourthPosBanHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFourthPosBanHero);
                                    }
                                } else {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFifthPosBanHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFifthPosBanHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFifthPosBanHero);
                                    }
                                }
                                currentImage.setImageResource(h.getImage());
                                currentNumberOfHeroesForOnePos++;
                            } else
                                break;
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

            for (HeroInfo h : allySortedHeroesWinDif) {
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
                            if (valueDif >= -1.0) {
                                ImageView currentImage;
                                if (pos.toString().equals("1")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFirstPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFirstPosPickHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFirstPosPickHero);
                                    }
                                } else if (pos.toString().equals("2")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthSecondPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthSecondPosPickHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthSecondPosPickHero);
                                    }
                                } else if (pos.toString().equals("3")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthThirdPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthThirdPosPickHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthThirdPosPickHero);
                                    }
                                } else if (pos.toString().equals("4")) {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFourthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFourthPosPickHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFourthPosPickHero);
                                    }
                                } else {
                                    if (currentNumberOfHeroesForOnePos == 1) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.firstFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 2) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.secondFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 3) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.thirdFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 4) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fourthFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 5) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.fifthFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 6) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.sixthFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 7) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.seventhFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 8) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.eighthFifthPosPickHero);
                                    } else if (currentNumberOfHeroesForOnePos == 9) {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.ninthFifthPosPickHero);
                                    } else {
                                        currentImage = (ImageView) mainActivity.findViewById(R.id.tenthFifthPosPickHero);
                                    }
                                }
                                currentImage.setImageResource(h.getImage());
                                currentNumberOfHeroesForOnePos++;
                            } else
                                break;
                        }
                    } catch (IllegalArgumentException e) {
                        //
                    }
                } else
                    break;
            }
        }

        Toast.makeText(context, "Пики обновлены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... unused) {
        boolean isAllyCounters = false;

        for (int ij = 0; ij < 2; ij++) {
            if ((ij == 1 && !isNullAllyHeroes()) || (ij == 0 && !isNullEnemyHeroes())) {
                int numberOfHeroes;
                ArrayList<String> heroReadNames = new ArrayList<>();
                ArrayList<String> heroLinks = new ArrayList<>();
                ArrayList<HeroInfo> heroesWinDif = new ArrayList<>();
                ArrayList<Heroes> heroes = (isAllyCounters) ? allyHeroes : enemyHeroes;

                for (Heroes hero : heroes) {
                    heroReadNames.add(hero.title);
                }
                for (String heroName : heroReadNames) {
                    heroLinks.add("https://ru.dotabuff.com/heroes/" + heroName + "/counters?date=month"); //week, month, patch_7.22
                }

                numberOfHeroes = heroReadNames.size();
                try {
                    for (int i = 0; i < numberOfHeroes; i++) {
                        Document heroDoc = Jsoup.connect(heroLinks.get(i)).get();

                        Elements heroCounters = heroDoc.getElementsByTag("tr");

                        for (int j = 0; j < 18; j++) {
                            heroCounters.remove(0);
                        }

                        for (Element heroCounter : heroCounters) {
                            String heroCounterName = heroCounter.children().remove(1).text();
                            String heroCounterWinRate = heroCounter.children().remove(2).text();
                            double heroCounterWinRateToDouble =
                                    Double.valueOf(heroCounterWinRate.substring(0, heroCounterWinRate.length() - 1));

                            boolean f = true;
                            for (HeroInfo h : heroesWinDif) {
                                if (h.getName().equals(heroCounterName)) {
                                    double oldWinRateDif = h.getWinRateDif();
                                    double oldWinRate = h.getNewWinRate();
                                    h.setWinRateDif(heroCounterWinRateToDouble + oldWinRateDif);
                                    h.setNewWinRate(heroCounterWinRateToDouble + oldWinRate);
                                    f = false;
                                    break;
                                }
                            }
                            if (f) {
                                for (HeroInfo h : tiers.getHeroesTier()) {
                                    if (h.getName().equals(heroCounterName)) {
                                        heroesWinDif.add(new HeroInfo(h.getImage(), heroCounterName,
                                                heroCounterWinRateToDouble, h.getTier(),
                                                heroCounterWinRateToDouble + h.getNewWinRate()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                HeroTier.quickSort(heroesWinDif, 0, heroesWinDif.size() - 1);

                for (Heroes hero : allyHeroes) {
                    String key = hero.toString();
                    String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
                    switch (key) {
                        case "AntiMage":
                            newKey = "Anti-Mage";
                            break;
                        case "KeeperOfTheLight":
                            newKey = "Keeper of the Light";
                            break;
                        case "QueenOfPain":
                            newKey = "Queen of Pain";
                            break;
                        case "NaturesProphet":
                            newKey = "Nature's Prophet";
                            break;
                    }
                    int k = 0;
                    if (numberOfHeroes > 0) {
                        for (HeroInfo h : heroesWinDif) {
                            if (h.getName().equals(newKey)) {
                                double oldWinRateDif = h.getWinRateDif();
                                heroesWinDif.remove(k);
                                if (!isAllyCounters)
                                    sumWinDif += oldWinRateDif;
                                break;
                            }
                            k++;
                        }
                    }
                }

                for (Heroes hero : enemyHeroes) {
                    String key = hero.toString();
                    String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
                    switch (key) {
                        case "AntiMage":
                            newKey = "Anti-Mage";
                            break;
                        case "KeeperOfTheLight":
                            newKey = "Keeper of the Light";
                            break;
                        case "QueenOfPain":
                            newKey = "Queen of Pain";
                            break;
                        case "NaturesProphet":
                            newKey = "Nature's Prophet";
                            break;
                    }

                    if (numberOfHeroes > 0) {
                        int k = 0;
                        for (HeroInfo h : heroesWinDif) {
                            if (h.getName().equals(newKey)) {
                                heroesWinDif.remove(k);
                                break;
                            }
                            k++;
                        }
                    }
                }

                if (isAllyCounters)
                    allySortedHeroesWinDif = heroesWinDif;
                else
                    enemySortedHeroesWinDif = heroesWinDif;
                //writeInFile(isAllyCounters);
            }
            isAllyCounters = true;
        }
        deleteBanHeroes();
        return null;
    }

    private void deleteBanHeroes() {
        for (Heroes hero : banHeroes) {
            String key = hero.toString();
            String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
            switch (key) {
                case "AntiMage":
                    newKey = "Anti-Mage";
                    break;
                case "KeeperOfTheLight":
                    newKey = "Keeper of the Light";
                    break;
                case "QueenOfPain":
                    newKey = "Queen of Pain";
                    break;
                case "NaturesProphet":
                    newKey = "Nature's Prophet";
                    break;
            }

            if (!isNullAllyHeroes()) {
                int k = 0;
                for (HeroInfo h : allySortedHeroesWinDif) {
                    if (h.getName().equals(newKey)) {
                        allySortedHeroesWinDif.remove(k);
                        break;
                    }
                    k++;
                }
            }

            if (!isNullEnemyHeroes()) {
                int j = 0;
                for (HeroInfo h : enemySortedHeroesWinDif) {
                    if (h.getName().equals(newKey)) {
                        enemySortedHeroesWinDif.remove(j);
                        break;
                    }
                    j++;
                }
            }
        }
    }
/*
    private void writeInFile(boolean isAllyCounters) {
        ArrayList<HeroInfo> sortedHeroesWinDif = (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;

        try (FileWriter writer = new FileWriter("/storage/emulated/0/Documents/PicksAndBans.txt", false)) {
            for (HeroInfo heroWinDif : sortedHeroesWinDif) {
                String key = heroWinDif.getName();
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
                    double valueDif = Math.round(heroWinDif.getWinRateDif() * 100.0) / 100.0;
                    double valueWinRate = Math.round(heroWinDif.getNewWinRate() * 100.0) / 100.0;

                    if (valueDif > 0) {
                        writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + heroWinDif.getTier() + "| +" + valueDif + "% | " + valueWinRate + "%\n");
                    } else {
                        writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + heroWinDif.getTier() + "| " + valueDif + "% | " + valueWinRate + "%\n");
                    }
                } catch (IllegalArgumentException e) {
                    //
                }
            }

            writer.append("\n\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    */
}
