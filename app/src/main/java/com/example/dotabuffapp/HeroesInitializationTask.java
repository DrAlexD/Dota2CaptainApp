package com.example.dotabuffapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;

class HeroesInitializationTask extends AsyncTask<Void, Void, Void> implements Serializable {
    private HeroesInitialization heroesInitialization;
    private static final String INTERVAL_OF_COLLECTED_INITIALIZATION_DATA = "week"; //week, month, 3month, patch_7.22, season_3
    private static final String INTERVAL_OF_COLLECTED_ITEMS_DATA = "month"; //week, month, 3month, patch_7.22, season_3
    private transient HeroesAsyncResponse onPostExecuteResponse;

    HeroesInitializationTask(MainActivity onPostExecuteResponse) {
        this.heroesInitialization = new HeroesInitialization();
        this.onPostExecuteResponse = onPostExecuteResponse;
    }

    HeroesInitialization getHeroesInitialization() {
        return this.heroesInitialization;
    }

    @Override
    protected void onPostExecute(Void unused) {
        onPostExecuteResponse.heroesInitializationProcessFinish();
    }

    @Override
    protected Void doInBackground(Void... unused) {
        try {
            Document heroesDoc = Jsoup.connect("https://ru.dotabuff.com/heroes/trends?date=" +
                    INTERVAL_OF_COLLECTED_INITIALIZATION_DATA).get();

            Document heroesMatchesDoc = Jsoup.connect("https://ru.dotabuff.com/heroes/played?date=" +
                    INTERVAL_OF_COLLECTED_ITEMS_DATA).get();

            Elements heroes = heroesDoc.getElementsByTag("tr");
            Elements heroesMatches = heroesMatchesDoc.getElementsByTag("tr");

            heroesMatches.remove(0);
            heroes.subList(0, 3).clear();

            for (Element hero : heroes) {
                String heroName = hero.children().remove(0).text();
                String heroWinRate = hero.children().remove(2).text();
                double heroWinRateToDouble = Double.parseDouble(heroWinRate.substring(0, heroWinRate.length() - 1));

                addingHeroToTierListByWinRateDiff(heroName, heroWinRateToDouble);
            }

            for (Element heroMatches : heroesMatches) {
                String heroName = heroMatches.children().remove(1).text();
                String heroAllMatches = heroMatches.children().remove(2).text();
                int heroAllMatchesToInt = Integer.parseInt(heroAllMatches.replace(",", ""));

                for (Hero hero : heroesInitialization.getOriginal()) {
                    if (heroName.equals(hero.getName())) {
                        hero.setAllMatches(heroAllMatchesToInt);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Hero.sortHeroes(heroesInitialization.getOriginal(), 0, heroesInitialization.getOriginal().size() - 1, false);
        heroesInitialization.getCurrent().addAll(heroesInitialization.getOriginal());

        return null;
    }

    private void addingHeroToTierListByWinRateDiff(String heroName, double heroWinRateToDouble) {
        if (heroWinRateToDouble > 55.0)
            heroesInitialization.getOriginal().add(
                    new Hero("S", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else if (heroWinRateToDouble > 53.0)
            heroesInitialization.getOriginal().add(
                    new Hero("A", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else if (heroWinRateToDouble > 51.0)
            heroesInitialization.getOriginal().add(
                    new Hero("B", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else if (heroWinRateToDouble > 49.0)
            heroesInitialization.getOriginal().add(
                    new Hero("C", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else if (heroWinRateToDouble > 47.0)
            heroesInitialization.getOriginal().add(
                    new Hero("D", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else if (heroWinRateToDouble > 45.0)
            heroesInitialization.getOriginal().add(
                    new Hero("E", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
        else
            heroesInitialization.getOriginal().add(
                    new Hero("F", Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, heroWinRateToDouble, 0));
    }
}