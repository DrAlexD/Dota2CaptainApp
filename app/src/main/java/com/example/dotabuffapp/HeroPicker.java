package com.example.dotabuffapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HeroPicker {
    private HeroTier tiers;
    private ArrayList<Heroes> allyHeroes; //союзные герои
    private ArrayList<Heroes> enemyHeroes;
    private LinkedHashMap<String, SimpleEntry<Double, Double>> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    private LinkedHashMap<String, SimpleEntry<Double, Double>> enemySortedHeroesWinDif;
    private Double allySumWinDif; //насколько хороши союзные герои против вражеских
    private Double enemySumWinDif;

    HeroPicker() {
        tiers = new HeroTier();
        allySumWinDif = 0.0;
        enemySumWinDif = 0.0;
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();

        //allyHeroes.add(Heroes.Disruptor);
        //allyHeroes.add(Heroes.SpiritBreaker);
        //allyHeroes.add(Heroes.DarkSeer);
        //allyHeroes.add(Heroes.Sven);
        //allyHeroes.add(Heroes.Grimstroke);

        enemyHeroes.add(Heroes.WraithKing);
        enemyHeroes.add(Heroes.Ursa);
        enemyHeroes.add(Heroes.EmberSpirit);
        enemyHeroes.add(Heroes.TemplarAssassin);
        enemyHeroes.add(Heroes.OutworldDevourer);
        enemyHeroes.add(Heroes.StormSpirit);
        enemyHeroes.add(Heroes.DarkWillow);
        enemyHeroes.add(Heroes.NyxAssassin);
        enemyHeroes.add(Heroes.Earthshaker);
        enemyHeroes.add(Heroes.WinterWyvern);
        enemyHeroes.add(Heroes.Bristleback);

    }

    /*
        public static void main(String[] args) {
            HeroPicker heroPicks = new HeroPicker();

            heroPicks.calc(false);
            heroPicks.calc(true);

            heroPicks.writeInFile(false);
            heroPicks.writeInFile(true);
        }
    */
    void calc(boolean isAllyCounters) {
        int numberOfHeroes;
        ArrayList<String> heroReadNames = new ArrayList<>();
        ArrayList<String> heroLinks = new ArrayList<>();
        HashMap<String, SimpleEntry<Double, Double>> heroesWinDif = new HashMap<>();
        LinkedHashMap<String, SimpleEntry<Double, Double>> sortedHeroesWinDif;

        if (isAllyCounters) {
            for (Heroes hero : allyHeroes) {
                heroReadNames.add(hero.title);
            }
        } else {
            for (Heroes hero : enemyHeroes) {
                heroReadNames.add(hero.title);
            }
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

                    heroesWinDif.merge(heroCounterName, new SimpleEntry<>(heroCounterWinRateToDouble,
                                    heroCounterWinRateToDouble + tiers.heroesTier.get(heroCounterName).getValue()),
                            (oldEval, newEval) -> new SimpleEntry<>
                                    (oldEval.getKey() + newEval.getKey(), oldEval.getValue() + newEval.getKey()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        sortedHeroesWinDif = heroesWinDif
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparingDouble(e -> e.getValue().getKey())))
                //e.getValue().getKey() для сортировки для лучшего котрпика, e.getValue().getValue() для лучшего героя с учетом меты
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

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
            if (numberOfHeroes > 0) {
                if (isAllyCounters)
                    sortedHeroesWinDif.remove(newKey);
                else
                    allySumWinDif += sortedHeroesWinDif.remove(newKey).getKey();
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
                if (isAllyCounters)
                    enemySumWinDif += sortedHeroesWinDif.remove(newKey).getKey();
                else
                    sortedHeroesWinDif.remove(newKey);
            }
        }
        if (isAllyCounters)
            allySortedHeroesWinDif = sortedHeroesWinDif;
        else
            enemySortedHeroesWinDif = sortedHeroesWinDif;
    }

    private void writeInFile(boolean isAllyCounters) {
        LinkedHashMap<String, SimpleEntry<Double, Double>> sortedHeroesWinDif = (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;

        try (FileWriter writer = new FileWriter("Z:/Programming/DotabuffApps/src/PicksAndBans.txt", true)) {
            for (Map.Entry<String, SimpleEntry<Double, Double>> heroWinDif : sortedHeroesWinDif.entrySet()) {
                String key = heroWinDif.getKey();
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
                    HeroPool.valueOf(newKey);
                    double valueDif = Math.round(heroWinDif.getValue().getKey() * 100.0) / 100.0;
                    double valueWinRate = Math.round(heroWinDif.getValue().getValue() * 100.0) / 100.0;

                    if (valueDif > 0) {
                        writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + tiers.heroesTier.get(key).getKey() +
                                "| +" + valueDif + "% | " + valueWinRate + "%\n");
                    } else {
                        writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + tiers.heroesTier.get(key).getKey() +
                                "| " + valueDif + "% | " + valueWinRate + "%\n");
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

    LinkedHashMap<String, SimpleEntry<Double, Double>> getSortedHeroesWinDif(boolean isAllyCounters) {
        return (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;
    }

    Double getAllySumWinDif() {
        return Math.round(allySumWinDif * 100.0) / 100.0;
    }

    Double getEnemySumWinDif() {
        return Math.round(enemySumWinDif * 100.0) / 100.0;
    }
}
