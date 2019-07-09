package com.example.dotabuffapp;

import android.os.AsyncTask;

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
    AsyncResponse delegate;

    HeroPicker() {
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        banHeroes = new ArrayList<>();
        allySortedHeroesWinDif = new ArrayList<>();
        enemySortedHeroesWinDif = new ArrayList<>();
        sumWinDif = 0.0;
        delegate = null;
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
        delegate.processFinish();
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
                                for (HeroInfo h : tiers.getOriginalTier()) {
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

                HeroTier.quickSort(heroesWinDif, 0, heroesWinDif.size() - 1, true);

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
                                if (!isAllyCounters) {
                                    sumWinDif += oldWinRateDif;
                                }
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
}
