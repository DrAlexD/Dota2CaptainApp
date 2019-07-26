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
    private AsyncResponse onPostExecuteResponse;

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
            Document heroesDoc = Jsoup.connect("https://ru.dotabuff.com/heroes/trends").get();

            Elements heroes = heroesDoc.getElementsByTag("tr");

            for (int j = 0; j < 3; j++) {
                heroes.remove(0);
            }

            for (Element hero : heroes) {
                String heroName = hero.children().remove(0).text();
                String heroWinRate = hero.children().remove(2).text();
                double heroWinRateToDouble = Double.valueOf(heroWinRate.substring(0, heroWinRate.length() - 1));

                addingHeroToTierListByWinRateDiff(heroName, heroWinRateToDouble);
            }

            Hero.sortHeroes(heroesInitialization.getOriginal(), 0, heroesInitialization.getOriginal().size() - 1, false);
            heroesInitialization.getCurrent().addAll(heroesInitialization.getOriginal());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void addingHeroToTierListByWinRateDiff(String heroName, double heroWinRateToDouble) {
        if (heroWinRateToDouble > 55.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "S", heroWinRateToDouble));
        else if (heroWinRateToDouble > 53.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "A", heroWinRateToDouble));
        else if (heroWinRateToDouble > 51.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "B", heroWinRateToDouble));
        else if (heroWinRateToDouble > 49.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "C", heroWinRateToDouble));
        else if (heroWinRateToDouble > 47.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "D", heroWinRateToDouble));
        else if (heroWinRateToDouble > 45.0)
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "E", heroWinRateToDouble));
        else
            heroesInitialization.getOriginal().add(
                    new Hero(Hero.getHeroImageByName(heroName), heroName, heroWinRateToDouble, "F", heroWinRateToDouble));
    }
}