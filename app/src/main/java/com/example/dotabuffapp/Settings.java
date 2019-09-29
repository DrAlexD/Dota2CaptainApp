package com.example.dotabuffapp;

import java.io.Serializable;

class Settings implements Serializable {
    private boolean personalHeroesPool;
    private boolean bestHeroItems;

    boolean getPersonalHeroesPool() {
        return this.personalHeroesPool;
    }

    void setPersonalHeroesPool(boolean personalHeroesPool) {
        this.personalHeroesPool = personalHeroesPool;
    }

    boolean getBestHeroItems() {
        return this.bestHeroItems;
    }

    void setBestHeroItems(boolean bestHeroItems) {
        this.bestHeroItems = bestHeroItems;
    }
}
