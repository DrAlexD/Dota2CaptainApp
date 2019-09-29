package com.example.dotabuffapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class HeroesCountersTask extends AsyncTask<Void, Void, HeroesCounters> implements Serializable {
    private HeroesCounters heroesCounters;
    private HeroesAsyncResponse onPostExecuteResponse;

    private final String INTERVAL_OF_COLLECTED_COUNTERS_DATA = "week"; //week, month, 3month, patch_7.22, season_3

    HeroesCountersTask(MainActivity onPostExecuteResponse) {
        this.onPostExecuteResponse = onPostExecuteResponse;
    }

    void setHeroesCounters(HeroesCounters heroesCounters) {
        this.heroesCounters = heroesCounters;
    }

    @Override
    protected void onPostExecute(HeroesCounters heroesCounters) {
        onPostExecuteResponse.heroesCountersProcessFinish(heroesCounters);
    }

    @Override
    protected HeroesCounters doInBackground(Void... unused) {
        heroesCounters.setWinRateDiffBetweenAllyAndEnemyPicks(0.0);

        boolean isAllyCounters = false;
        for (int mode = 0; mode < 2; mode++, isAllyCounters = true) {

            if ((mode == 0 && !heroesCounters.getEnemyHeroes().isEmpty()) ||
                    (mode == 1 && !heroesCounters.getAllyHeroes().isEmpty())) {

                ArrayList<String> heroesNames = new ArrayList<>();
                ArrayList<String> heroesLinks = new ArrayList<>();
                ArrayList<Hero> countersByWinRateDiff = new ArrayList<>();
                ArrayList<HeroesPool> heroes = (isAllyCounters) ? heroesCounters.getAllyHeroes() : heroesCounters.getEnemyHeroes();

                for (HeroesPool hero : heroes) {
                    heroesNames.add(hero.title);
                }
                for (String heroName : heroesNames) {
                    heroesLinks.add("https://ru.dotabuff.com/heroes/" + heroName + "/counters?date=" +
                            INTERVAL_OF_COLLECTED_COUNTERS_DATA);
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
                                    hero.setWinRateDiff(heroCounterWinRateToDouble + hero.getWinRateDiff());
                                    hero.setNewWinRate(heroCounterWinRateToDouble + hero.getNewWinRate());
                                    isNewHeroForAdding = false;
                                    break;
                                }
                            }
                            if (isNewHeroForAdding) {
                                for (Hero hero : heroesCounters.getHeroesInitialization().getOriginal()) {
                                    if (heroCounterName.equals(hero.getName())) {
                                        countersByWinRateDiff.add(new Hero(hero.getTier(), hero.getImage(), heroCounterName,
                                                heroCounterWinRateToDouble, heroCounterWinRateToDouble + hero.getNewWinRate(),
                                                hero.getAllMatches()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                deleteSelectedHeroes(isAllyCounters, countersByWinRateDiff);

                Hero.sortHeroes(countersByWinRateDiff, 0, countersByWinRateDiff.size() - 1, true);

                if (isAllyCounters)
                    heroesCounters.setAllyCountersByWinRateDiff(countersByWinRateDiff);
                else
                    heroesCounters.setEnemyCountersByWinRateDiff(countersByWinRateDiff);
            }
        }
        return heroesCounters;
    }

    private void deleteSelectedHeroes(boolean isAllyCounters, ArrayList<Hero> countersByWinRateDiff) {
        for (HeroesPool heroesPoolHero : heroesCounters.getAllyHeroes()) {
            String heroName = heroesPoolHero.toHeroName();

            int i = 0;
            for (Hero hero : countersByWinRateDiff) {
                if (heroName.equals(hero.getName())) {
                    if (!isAllyCounters) {
                        heroesCounters.setWinRateDiffBetweenAllyAndEnemyPicks(
                                heroesCounters.getWinRateDiffBetweenAllyAndEnemyPicks()
                                        + hero.getWinRateDiff());
                    }
                    countersByWinRateDiff.remove(i);
                    break;
                }
                i++;
            }
        }

        for (HeroesPool heroesPoolHero : heroesCounters.getEnemyHeroes()) {
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

        for (HeroesPool heroesPoolHero : heroesCounters.getBanHeroes()) {
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
    }
}
