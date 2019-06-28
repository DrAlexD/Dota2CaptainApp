package com.example.dotabuffapp;

import java.io.FileWriter;
import java.io.IOException;

public class PicksAndBans {
    private void writeInFile(HeroPicker heroPicks, boolean isAllyCounters) {
        try (FileWriter writer = new FileWriter("/storage/emulated/0/Documents/PicksAndBans.txt", isAllyCounters)) {
            if (isAllyCounters) {
                writer.append("Bans \n");
            } else {
                writer.append("Picks (" + heroPicks.getSumWinDif().toString() + ")\n");
            }

            for (Integer pos = 1; pos <= 5; pos++) {
                int currentNumberOfHeroesForOnePos = 0;
                double maxValueDif = 0.0;

                writer.append(pos.toString() + ")");

                for (HeroInfo h : heroPicks.getSortedHeroesWinDif(isAllyCounters)) {
                    if (currentNumberOfHeroesForOnePos < 10) {
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
                            HeroPool.valueOf(newKey);
                            double valueDif = Math.round(h.getWinRateDif() * 100.0) / 100.0;
                            double valueWinRate = Math.round(h.getNewWinRate() * 100.0) / 100.0;

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
