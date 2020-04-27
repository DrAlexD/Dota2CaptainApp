package com.example.dotabuffapp;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    private int image;
    private String name;
    private double winRateDiff;
    private double newWinRate;
    private double useFrequency;

    Item(int image, String name, double winRateDiff, double newWinRate, double useFrequency) {
        this.image = image;
        this.name = name;
        this.winRateDiff = Math.round(winRateDiff * 100.0) / 100.0;
        this.newWinRate = Math.round(newWinRate * 100.0) / 100.0;
        this.useFrequency = useFrequency;
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

    double getNewWinRate() {
        return this.newWinRate;
    }

    double getUseFrequency() {
        return this.useFrequency;
    }

    @NotNull
    @Override
    public String toString() {
        return getName() + " | " + getWinRateDiff() + " | " + getNewWinRate();
    }

    static void sortItems(ArrayList<Item> items, int startPos, int endPos, boolean isSortByWinRateDiff) {
        if (items.isEmpty())
            return;

        if (startPos >= endPos)
            return;

        int centerPos = (startPos + endPos) / 2;
        double centerWinRateDiff = 0.0;
        String centerItemName = "";

        if (isSortByWinRateDiff) {
            centerWinRateDiff = items.get(centerPos).getWinRateDiff();
        } else {
            centerItemName = items.get(centerPos).getName();
        }

        int i = startPos;
        int j = endPos;

        while (i <= j) {
            if (isSortByWinRateDiff) {
                while (items.get(i).getWinRateDiff() > centerWinRateDiff) {
                    i++;
                }
                while (items.get(j).getWinRateDiff() < centerWinRateDiff) {
                    j--;
                }
            } else {
                while (items.get(i).getName().compareToIgnoreCase(centerItemName) < 0) {
                    i++;
                }
                while (items.get(j).getName().compareToIgnoreCase(centerItemName) > 0) {
                    j--;
                }
            }

            if (i <= j) {
                Item tempItem = items.get(i);
                items.set(i, items.get(j));
                items.set(j, tempItem);
                i++;
                j--;
            }
        }

        if (startPos < j)
            sortItems(items, startPos, j, isSortByWinRateDiff);

        if (endPos > i)
            sortItems(items, i, endPos, isSortByWinRateDiff);
    }

    static String toItemsPoolItemName(String itemName) {
        String itemNameWithOutSeparators;

        switch (itemName) {
            case "Sange and Yasha":
                itemNameWithOutSeparators = "SangeAndYasha";
                break;
            case "Yasha and Kaya":
                itemNameWithOutSeparators = "YashaAndKaya";
                break;
            case "Dagon (5-й уровень)":
                itemNameWithOutSeparators = "Dagon5";
                break;
            case "Kaya and Sange":
                itemNameWithOutSeparators = "KayaAndSange";
                break;
            case "Helm of the Dominator":
                itemNameWithOutSeparators = "HelmOfTheDominator";
                break;
            case "Necronomicon (3-й уровень)":
                itemNameWithOutSeparators = "Necronomicon3";
                break;
            case "Dagon (3-й уровень)":
                itemNameWithOutSeparators = "Dagon3";
                break;
            case "Dagon (2-й уровень)":
                itemNameWithOutSeparators = "Dagon2";
                break;
            case "Dagon (4-й уровень)":
                itemNameWithOutSeparators = "Dagon4";
                break;
            case "Necronomicon (2-й уровень)":
                itemNameWithOutSeparators = "Necronomicon2";
                break;
            default:
                itemNameWithOutSeparators = itemName.replace(" ", "")
                        .replace("of", "Of").replace("'", "");
        }

        return itemNameWithOutSeparators;
    }

    static int getItemImageByName(String itemName) {
        switch (itemName) {
            //TODO добавить картинки итемов
            default:
                return R.drawable.item_frame;
        }
    }
}
