package com.example.dotabuffapp;

import java.io.Serializable;

public class HeroInfo implements Serializable {
    private int image;
    private String name;
    private double winRateDif;
    private String tier;
    private double newWinRate;

    HeroInfo(int image, String name, double winRateDif, String tier, double newWinRate) {
        this.image = image;
        this.name = name;
        this.winRateDif = Math.round(winRateDif * 100.0) / 100.0;
        this.tier = tier;
        this.newWinRate = Math.round(newWinRate * 100.0) / 100.0;
    }

    int getImage() {
        return this.image;
    }

    void setImage(int image) {
        this.image = image;
    }

    String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    double getWinRateDif() {
        return this.winRateDif;
    }

    void setWinRateDif(double winRateDif) {
        this.winRateDif = Math.round(winRateDif * 100.0) / 100.0;
    }

    String getTier() {
        return this.tier;
    }

    void setTier(String tier) {
        this.tier = tier;
    }

    double getNewWinRate() {
        return this.newWinRate;
    }

    void setNewWinRate(double newWinRate) {
        this.newWinRate = Math.round(newWinRate * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
