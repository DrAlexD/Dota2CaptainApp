package com.example.dotabuffapp;

import java.io.Serializable;
import java.util.ArrayList;

class HeroesCounters implements Serializable {
    private HeroesWithTiers heroesWithTiers;
    private ArrayList<HeroesPool> allyHeroes;
    private ArrayList<HeroesPool> enemyHeroes;
    private ArrayList<HeroesPool> banHeroes;
    private ArrayList<Hero> allyCountersByWinRateDiff;
    private ArrayList<Hero> enemyCountersByWinRateDiff;
    private Double winRateDiffBetweenAllyAndEnemyPicks;

    HeroesCounters() {
        heroesWithTiers = new HeroesWithTiers();
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        banHeroes = new ArrayList<>();
        allyCountersByWinRateDiff = new ArrayList<>();
        enemyCountersByWinRateDiff = new ArrayList<>();
        winRateDiffBetweenAllyAndEnemyPicks = 0.0;
    }

    HeroesWithTiers getHeroesWithTiers() {
        return this.heroesWithTiers;
    }

    ArrayList<HeroesPool> getAllyHeroes() {
        return this.allyHeroes;
    }

    ArrayList<HeroesPool> getEnemyHeroes() {
        return this.enemyHeroes;
    }

    ArrayList<HeroesPool> getBanHeroes() {
        return this.banHeroes;
    }

    ArrayList<Hero> getAllyCountersByWinRateDiff() {
        return this.allyCountersByWinRateDiff;
    }

    ArrayList<Hero> getEnemyCountersByWinRateDiff() {
        return this.enemyCountersByWinRateDiff;
    }

    Double getWinRateDiffBetweenAllyAndEnemyPicks() {
        return this.winRateDiffBetweenAllyAndEnemyPicks;
    }

    void setAllyCountersByWinRateDiff(ArrayList<Hero> allyCountersByWinRateDiff) {
        this.allyCountersByWinRateDiff = allyCountersByWinRateDiff;
    }

    void setEnemyCountersByWinRateDiff(ArrayList<Hero> enemyCountersByWinRateDiff) {
        this.enemyCountersByWinRateDiff = enemyCountersByWinRateDiff;
    }

    void setWinRateDiffBetweenAllyAndEnemyPicks(double winRateDiffBetweenAllyAndEnemyPicks) {
        this.winRateDiffBetweenAllyAndEnemyPicks = winRateDiffBetweenAllyAndEnemyPicks;
    }

    void deleteBanHeroFromLists(HeroesPool heroesPoolHero) {
        String heroName = heroesPoolHero.toHeroName();

        if (!allyHeroes.isEmpty()) {
            int i = 0;
            for (Hero hero : allyCountersByWinRateDiff) {
                if (heroName.equals(hero.getName())) {
                    allyCountersByWinRateDiff.remove(i);
                    break;
                }
                i++;
            }
        }

        if (!enemyHeroes.isEmpty()) {
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
}
