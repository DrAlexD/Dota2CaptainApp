package com.example.dotabuffapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

public class PicksAndBans {
    /*
    public static void main(String[] args) {
        PicksAndBans pab = new PicksAndBans();
        HeroPicker heroPicks = new HeroPicker();

        heroPicks.calc(false);
        heroPicks.calc(true);

        pab.writeInFile(heroPicks, false);
        pab.writeInFile(heroPicks, true);
    }
*/
    private void writeInFile(HeroPicker heroPicks, boolean isAllyCounters) {
        try (FileWriter writer = new FileWriter("Z:/Programming/DotabuffApps/src/PicksAndBans.txt", isAllyCounters)) {
            if (isAllyCounters) {
                writer.append("Bans (" + heroPicks.getEnemySumWinDif().toString() + ")\n");
            } else {
                writer.append("Picks (" + heroPicks.getAllySumWinDif().toString() + ")\n");
            }

            for (Integer pos = 1; pos <= 5; pos++) {
                int currentNumberOfHeroesForOnePos = 0;
                double maxValueDif = 0.0;

                writer.append(pos.toString() + ")");

                for (Map.Entry<String, SimpleEntry<Double, Double>> heroWinDif : heroPicks.getSortedHeroesWinDif(isAllyCounters).entrySet()) {
                    if (currentNumberOfHeroesForOnePos < 10) {
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

                            if (Heroes.valueOf(newKey).pos.contains(pos.toString())) {
                                if (valueDif >= -1.0) {
                                    if (currentNumberOfHeroesForOnePos == 0)
                                        maxValueDif = valueDif;
                                    else if (valueDif <= maxValueDif - 1) {
                                        writer.append(" ");
                                        for (; valueDif <= maxValueDif - 1; maxValueDif--) {
                                            writer.append("|");
                                        }
                                    } else
                                        writer.append(",");

                                    writer.append(" " + key);

                                    currentNumberOfHeroesForOnePos++;
                                }
                            /*else {
                                writer.append("\n");
                                break;
                            }*/
                            }
                        } catch (IllegalArgumentException e) {
                            //
                        }
                    } else
                        break;
                }

                if (currentNumberOfHeroesForOnePos == 10)
                    writer.append(".");
                else
                    writer.append("_");
                writer.append("\n");
            }

            writer.append("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
