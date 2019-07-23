package com.example.dotabuffapp;

import java.io.Serializable;
import java.util.ArrayList;

public class HeroesCounters implements Serializable {
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

    void setHeroesWithTiers(HeroesWithTiers heroesWithTiers) {
        this.heroesWithTiers = heroesWithTiers;
    }

    void setAllyHeroes(ArrayList<HeroesPool> allyHeroes) {
        this.allyHeroes = allyHeroes;
    }

    void setEnemyHeroes(ArrayList<HeroesPool> enemyHeroes) {
        this.enemyHeroes = enemyHeroes;
    }

    void setBanHeroes(ArrayList<HeroesPool> banHeroes) {
        this.banHeroes = banHeroes;
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

    void addAllyHero(HeroesPool heroesPoolHero) {
        allyHeroes.add(heroesPoolHero);
    }

    void deleteAllyHero(HeroesPool heroesPoolHero) {
        if (!allyHeroes.isEmpty()) {
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
        if (!enemyHeroes.isEmpty()) {
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
