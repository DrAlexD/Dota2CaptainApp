package com.example.dotabuffapp;

public class HeroInfo {

    private String name;
    private String winRateDif;
    private int heroImage;

    public HeroInfo(String name, String winRateDif, int heroImage) {

        this.name = name;
        this.winRateDif = winRateDif;
        this.heroImage = heroImage;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWinRateDif() {
        return this.winRateDif;
    }

    public void setWinRateDif(String winRateDif) {
        this.winRateDif = winRateDif;
    }

    public int getHeroImage() {
        return this.heroImage;
    }

    public void setHeroImage(int heroImage) {
        this.heroImage = heroImage;
    }
}
