package com.example.dotabuffapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class HeroesCounters extends AsyncTask<Void, Void, Void> implements Serializable {
    private HeroesWithTiers heroesWithTiers;
    private ArrayList<HeroesPool> allyHeroes;
    private ArrayList<HeroesPool> enemyHeroes;
    private ArrayList<HeroesPool> banHeroes;
    private ArrayList<Hero> allyCountersByWinRateDiff;
    private ArrayList<Hero> enemyCountersByWinRateDiff;
    private Double winRateDiffBetweenAllyAndEnemyPicks;
    AsyncResponse delegate;

    HeroesCounters() {
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        banHeroes = new ArrayList<>();
        allyCountersByWinRateDiff = new ArrayList<>();
        enemyCountersByWinRateDiff = new ArrayList<>();
        winRateDiffBetweenAllyAndEnemyPicks = 0.0;
        delegate = null;
    }

    boolean isNullAllyHeroes() {
        return allyHeroes.isEmpty();
    }

    boolean isNullEnemyHeroes() {
        return enemyHeroes.isEmpty();
    }

    ArrayList<Hero> getCounters(boolean isAllyCounters) {
        return (isAllyCounters) ? allyCountersByWinRateDiff : enemyCountersByWinRateDiff;
    }

    void setCounters(ArrayList<Hero> allyCountersByWinRateDiff, ArrayList<Hero> enemyCountersByWinRateDiff) {
        this.allyCountersByWinRateDiff.addAll(allyCountersByWinRateDiff);
        this.enemyCountersByWinRateDiff.addAll(enemyCountersByWinRateDiff);
    }

    Double getSumWinDif() {
        return Math.round(winRateDiffBetweenAllyAndEnemyPicks * 100.0) / 100.0;
    }

    void setTier(HeroesWithTiers heroesWithTiers) {
        this.heroesWithTiers = heroesWithTiers;
    }

    void setAllyHeroes(ArrayList<HeroesPool> allyHeroes) {
        this.allyHeroes.addAll(allyHeroes);
    }

    void setEnemyHeroes(ArrayList<HeroesPool> enemyHeroes) {
        this.enemyHeroes.addAll(enemyHeroes);
    }

    void setBanHeroes(ArrayList<HeroesPool> banHeroes) {
        this.banHeroes.addAll(banHeroes);
    }

    void addAllyHero(HeroesPool heroesPoolHero) {
        allyHeroes.add(heroesPoolHero);
    }

    void deleteAllyHero(HeroesPool heroesPoolHero) {
        if (!isNullAllyHeroes()) {
            int i = 0;
            for (HeroesPool hero : allyHeroes) {
                if (hero.toString().equals(heroesPoolHero.toString())) {
                    allyHeroes.remove(i);
                    break;
                }
                i++;
            }
        }
    }

    void addEnemyHero(HeroesPool heroesPoolHero) {
        enemyHeroes.add(heroesPoolHero);
    }

    void deleteEnemyHero(HeroesPool heroesPoolHero) {
        if (!isNullEnemyHeroes()) {
            int i = 0;
            for (HeroesPool hero : enemyHeroes) {
                if (hero.toString().equals(heroesPoolHero.toString())) {
                    enemyHeroes.remove(i);
                    break;
                }
                i++;
            }
        }
    }

    void deleteBanHero(HeroesPool heroesPoolHero) {
        int i = 0;
        for (HeroesPool hero : banHeroes) {
            if (hero.toString().equals(heroesPoolHero.toString())) {
                banHeroes.remove(i);
                break;
            }
            i++;
        }
    }

    void deleteBanHeroFromLists(HeroesPool heroesPoolHero) {
        String heroName = heroesPoolHero.toHeroName();

        if (!isNullAllyHeroes()) {
            int i = 0;
            for (Hero hero : allyCountersByWinRateDiff) {
                if (heroName.equals(hero.getName())) {
                    allyCountersByWinRateDiff.remove(i);
                    break;
                }
                i++;
            }
        }

        if (!isNullEnemyHeroes()) {
            int i = 0;
            for (Hero hero : enemyCountersByWinRateDiff) {
                if (heroName.equals(hero.getName())) {
                    enemyCountersByWinRateDiff.remove(i);
                    break;
                }
                i++;
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

        for (int mode = 0; mode < 2; mode++, isAllyCounters = true) {
            if ((mode == 0 && !isNullEnemyHeroes()) || (mode == 1 && !isNullAllyHeroes())) {
                ArrayList<String> heroesNames = new ArrayList<>();
                ArrayList<String> heroesLinks = new ArrayList<>();
                ArrayList<Hero> countersByWinRateDiff = new ArrayList<>();
                ArrayList<HeroesPool> heroes = (isAllyCounters) ? allyHeroes : enemyHeroes;

                for (HeroesPool hero : heroes) {
                    heroesNames.add(hero.title);
                }
                for (String heroName : heroesNames) {
                    heroesLinks.add("https://ru.dotabuff.com/heroes/" + heroName + "/counters?date=week"); //week, month, patch_7.22
                }

                int numberOfHeroes = heroes.size();

                try {
                    for (int i = 0; i < numberOfHeroes; i++) {
                        Document heroDoc = Jsoup.connect(heroesLinks.get(i)).get();

                        Elements heroCounters = heroDoc.getElementsByTag("tr");

                        for (int j = 0; j < 18; j++) {
                            heroCounters.remove(0);
                        }

                        for (Element heroCounter : heroCounters) {
                            String heroCounterName = heroCounter.children().remove(1).text();
                            String heroCounterWinRate = heroCounter.children().remove(2).text();
                            double heroCounterWinRateToDouble =
                                    Double.valueOf(heroCounterWinRate.substring(0, heroCounterWinRate.length() - 1));

                            boolean isNewHeroForAdding = true;
                            for (Hero hero : countersByWinRateDiff) {
                                if (heroCounterName.equals(hero.getName())) {
                                    hero.setWinRateDif(heroCounterWinRateToDouble + hero.getWinRateDiff());
                                    hero.setNewWinRate(heroCounterWinRateToDouble + hero.getNewWinRate());
                                    isNewHeroForAdding = false;
                                    break;
                                }
                            }
                            if (isNewHeroForAdding) {
                                for (Hero hero : heroesWithTiers.getOriginalHeroesWithTiers()) {
                                    if (heroCounterName.equals(hero.getName())) {
                                        countersByWinRateDiff.add(new Hero(hero.getImage(), heroCounterName,
                                                heroCounterWinRateToDouble, hero.getTier(),
                                                heroCounterWinRateToDouble + hero.getNewWinRate()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                for (HeroesPool heroesPoolHero : allyHeroes) {
                    String heroName = heroesPoolHero.toHeroName();

                    int i = 0;
                    for (Hero hero : countersByWinRateDiff) {
                        if (heroName.equals(hero.getName())) {
                            if (!isAllyCounters) {
                                winRateDiffBetweenAllyAndEnemyPicks += hero.getWinRateDiff();
                            }
                            countersByWinRateDiff.remove(i);
                            break;
                        }
                        i++;
                    }
                }

                for (HeroesPool heroesPoolHero : enemyHeroes) {
                    String heroName = heroesPoolHero.toHeroName();

                    int i = 0;
                    for (Hero hero : countersByWinRateDiff) {
                        if (heroName.equals(hero.getName())) {
                            countersByWinRateDiff.remove(i);
                            break;
                        }
                        i++;
                    }
                }

                for (HeroesPool heroesPoolHero : banHeroes) {
                    String heroName = heroesPoolHero.toHeroName();

                    int i = 0;
                    for (Hero hero : countersByWinRateDiff) {
                        if (heroName.equals(hero.getName())) {
                            countersByWinRateDiff.remove(i);
                            break;
                        }
                        i++;
                    }
                }

                Hero.sortHeroes(countersByWinRateDiff, 0, countersByWinRateDiff.size() - 1, true);

                if (isAllyCounters)
                    allyCountersByWinRateDiff = countersByWinRateDiff;
                else
                    enemyCountersByWinRateDiff = countersByWinRateDiff;
            }
        }
        return null;
    }
}
