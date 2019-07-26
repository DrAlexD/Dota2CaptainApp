package com.example.dotabuffapp;

import java.io.Serializable;
import java.util.ArrayList;

class HeroesInitialization implements Serializable {
    private ArrayList<Hero> original;
    private ArrayList<Hero> current;
    private ArrayList<Hero> deleted;

    HeroesInitialization() {
        this.original = new ArrayList<>();
        this.current = new ArrayList<>();
        this.deleted = new ArrayList<>();
    }

    ArrayList<Hero> getOriginal() {
        return this.original;
    }

    ArrayList<Hero> getCurrent() {
        return this.current;
    }

    void addDeletedHero(HeroesPool heroesPoolHero) {
        int i = 0;
        String heroName = heroesPoolHero.toHeroName();
        for (Hero hero : deleted) {
            if (heroName.equals(hero.getName())) {
                current.add(deleted.remove(i));
                Hero.sortHeroes(current, 0, current.size() - 1, false);
                break;
            }
            i++;
        }
    }

    void deleteHero(HeroesPool heroesPoolHero) {
        if (heroesPoolHero != null) {
            int i = 0;
            String heroName = heroesPoolHero.toHeroName();
            for (Hero hero : current) {
                if (heroName.equals(hero.getName())) {
                    deleted.add(current.remove(i));
                    break;
                }
                i++;
            }
        }
    }
}