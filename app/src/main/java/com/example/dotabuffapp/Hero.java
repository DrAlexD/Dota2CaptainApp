package com.example.dotabuffapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Hero implements Serializable {
    private int image;
    private String name;
    private double winRateDiff;
    private String tier;
    private double newWinRate;

    Hero(int image, String name, double winRateDif, String tier, double newWinRate) {
        this.image = image;
        this.name = name;
        this.winRateDiff = Math.round(winRateDif * 100.0) / 100.0;
        this.tier = tier;
        this.newWinRate = Math.round(newWinRate * 100.0) / 100.0;
    }

    int getImage() {
        return this.image;
    }

    String getName() {
        return this.name;
    }

    double getWinRateDiff() {
        return this.winRateDiff;
    }

    void setWinRateDif(double winRateDif) {
        this.winRateDiff = Math.round(winRateDif * 100.0) / 100.0;
    }

    String getTier() {
        return this.tier;
    }


    double getNewWinRate() {
        return this.newWinRate;
    }

    void setNewWinRate(double newWinRate) {
        this.newWinRate = Math.round(newWinRate * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return getTier() + " | " + getName() + " | " + getWinRateDiff() + " | " + getNewWinRate();
    }

    static String toHeroesPoolHeroName(String heroName) {
        String heroNameWithOutSeparators;

        switch (heroName) {
            case "Anti-Mage":
                heroNameWithOutSeparators = "AntiMage";
                break;
            case "Keeper of the Light":
                heroNameWithOutSeparators = "KeeperOfTheLight";
                break;
            case "Queen of Pain":
                heroNameWithOutSeparators = "QueenOfPain";
                break;
            case "Nature's Prophet":
                heroNameWithOutSeparators = "NaturesProphet";
                break;
            default:
                heroNameWithOutSeparators = heroName.replace(" ", "");
        }

        return heroNameWithOutSeparators;
    }

    static void sortHeroes(ArrayList<Hero> heroes, int startPos, int endPos, boolean isSortByWinRateDiff) {
        if (heroes.size() == 0)
            return;

        if (startPos >= endPos)
            return;

        int centerPos = (startPos + endPos) / 2;
        double centerWinRateDiff = 0.0;
        String centerHeroName = "";

        if (isSortByWinRateDiff) {
            centerWinRateDiff = heroes.get(centerPos).getWinRateDiff();
        } else {
            centerHeroName = heroes.get(centerPos).getName();
        }

        int i = startPos, j = endPos;
        while (i <= j) {
            if (isSortByWinRateDiff) {
                while (heroes.get(i).getWinRateDiff() > centerWinRateDiff) {
                    i++;
                }
                while (heroes.get(j).getWinRateDiff() < centerWinRateDiff) {
                    j--;
                }
            } else {
                while (heroes.get(i).getName().compareToIgnoreCase(centerHeroName) < 0) {
                    i++;
                }
                while (heroes.get(j).getName().compareToIgnoreCase(centerHeroName) > 0) {
                    j--;
                }
            }

            if (i <= j) {
                Hero tempHero = heroes.get(i);
                heroes.set(i, heroes.get(j));
                heroes.set(j, tempHero);
                i++;
                j--;
            }
        }

        if (startPos < j)
            sortHeroes(heroes, startPos, j, isSortByWinRateDiff);

        if (endPos > i)
            sortHeroes(heroes, i, endPos, isSortByWinRateDiff);
    }
}
