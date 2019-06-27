package com.example.dotabuffapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class HeroPicker extends AsyncTask<Void, Void, Void> implements Serializable {
    private HeroTier tiers;
    private ArrayList<Heroes> allyHeroes; //союзные герои
    private ArrayList<Heroes> enemyHeroes;
    private ArrayList<HeroInfo> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif;
    private Double allySumWinDif; //насколько хороши союзные герои против вражеских
    private Double enemySumWinDif;
    private transient Context context;

    HeroPicker(Context context) {
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        allySortedHeroesWinDif = new ArrayList<>();
        enemySortedHeroesWinDif = new ArrayList<>();
        allySumWinDif = 0.0;
        enemySumWinDif = 0.0;
        this.context = context;
    }

    ArrayList<HeroInfo> getSortedHeroesWinDif(boolean isAllyCounters) {
        return (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;
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

    void addEnemyHero(Heroes hero) {
        enemyHeroes.add(hero);
    }

    void addAllyHeroes(ArrayList<Heroes> heroes) {
        allyHeroes.addAll(heroes);
    }

    void addEnemyHeroes(ArrayList<Heroes> heroes) {
        enemyHeroes.addAll(heroes);
    }

    Double getAllySumWinDif() {
        return Math.round(allySumWinDif * 100.0) / 100.0;
    }

    Double getEnemySumWinDif() {
        return Math.round(enemySumWinDif * 100.0) / 100.0;
    }

    void setTier(HeroTier tiers) {
        this.tiers = tiers;
    }

    @Override
    protected void onPostExecute(Void unused) {
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
                    heroLinks.add("https://ru.dotabuff.com/heroes/" + heroName + "/counters?date=patch_7.22"); //week, month, patch_7.22
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

                Collections.sort(heroesWinDif);

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
                                    allySumWinDif += oldWinRateDif;
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
                                double oldWinRateDif = h.getWinRateDif();
                                heroesWinDif.remove(k);
                                if (isAllyCounters)
                                    enemySumWinDif += oldWinRateDif;
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
        return null;
    }

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
}
