package com.example.dotabuffapp;

public class HeroInfo implements Comparable<HeroInfo> {

    private int heroImage;
    private String name;
    private double winRateDif;
    private double changedWinRate;

    public HeroInfo(int heroImage, String name, double winRateDif, double changedWinRate) {
        this.heroImage = heroImage;
        this.name = name;
        this.winRateDif = (winRateDif * 100.0) / 100.0;
        this.changedWinRate = (changedWinRate * 100.0) / 100.0;
    }

    public int getHeroImage() {
        return this.heroImage;
    }

    public void setHeroImage(int heroImage) {
        this.heroImage = heroImage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWinRateDif() {
        return this.winRateDif;
    }

    public void setWinRateDif(double winRateDif) {
        this.winRateDif = winRateDif;
    }

    public double getChangedWinRate() {
        return this.changedWinRate;
    }

    public void setChangedWinRate(double changedWinRate) {
        this.changedWinRate = changedWinRate;
    }

    @Override
    public int compareTo(HeroInfo heroInfo) {
        return this.winRateDif > heroInfo.winRateDif ? this.winRateDif < heroInfo.winRateDif ? -1 : 0 : 1;
    }
}
