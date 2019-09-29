package com.example.dotabuffapp;

import java.io.Serializable;
import java.util.ArrayList;

class HeroItems implements Serializable {
    private Hero hero;
    private ArrayList<Item> heroItemsByWinRateDiff;
    private ArrayList<Item> allItems;

    HeroItems() {
        this.heroItemsByWinRateDiff = new ArrayList<>();
        this.allItems = new ArrayList<>();
    }

    Hero getHero() {
        return this.hero;
    }

    void setHero(Hero hero) {
        this.hero = hero;
    }

    ArrayList<Item> getHeroItemsByWinRateDiff() {
        return this.heroItemsByWinRateDiff;
    }

    void setHeroItemsByWinRateDiff(ArrayList<Item> heroItemsByWinRateDiff) {
        this.heroItemsByWinRateDiff = heroItemsByWinRateDiff;
    }

    ArrayList<Item> getAllItems() {
        return this.allItems;
    }

    void setAllItems(ArrayList<Item> allItems) {
        this.allItems = allItems;
    }
}
