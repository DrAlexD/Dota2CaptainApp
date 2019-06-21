package com.example.dotabuffapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

class HeroTier {
    HashMap<String, SimpleEntry<String, Double>> heroesTier;

    HeroTier() {
        try {
            this.heroesTier = new HashMap<>();

            Document heroesDoc = Jsoup.connect("https://ru.dotabuff.com/heroes/trends").get();

            Elements heroes = heroesDoc.getElementsByTag("tr");

            for (int j = 0; j < 3; j++) {
                heroes.remove(0);
            }

            for (Element hero : heroes) {
                String heroName = hero.children().remove(0).text();
                String heroWinRate = hero.children().remove(2).text();
                double heroWinRateToDouble =
                        Double.valueOf(heroWinRate.substring(0, heroWinRate.length() - 1));

                if (heroWinRateToDouble > 55.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("S", heroWinRateToDouble));
                else if (heroWinRateToDouble > 53.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("A", heroWinRateToDouble));
                else if (heroWinRateToDouble > 51.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("B", heroWinRateToDouble));
                else if (heroWinRateToDouble > 49.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("C", heroWinRateToDouble));
                else if (heroWinRateToDouble > 47.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("D", heroWinRateToDouble));
                else if (heroWinRateToDouble > 45.0)
                    this.heroesTier.put(heroName, new SimpleEntry<>("E", heroWinRateToDouble));
                else
                    this.heroesTier.put(heroName, new SimpleEntry<>("F", heroWinRateToDouble));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}