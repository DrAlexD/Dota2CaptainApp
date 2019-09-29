package com.example.dotabuffapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class HeroItemsTask extends AsyncTask<Void, Void, HeroItems> implements Serializable {
    private HeroItems heroItems;
    private HeroItemsAsyncResponse onPostExecuteResponse;
    private Settings settings;

    private final String INTERVAL_OF_COLLECTED_HERO_ITEMS_DATA = "month"; //week, month, 3month, patch_7.22, season_3

    HeroItemsTask(HeroItemsActivity onPostExecuteResponse, HeroItems heroItems, Settings settings) {
        this.onPostExecuteResponse = onPostExecuteResponse;
        this.heroItems = heroItems;
        this.settings = settings;
    }

    @Override
    protected void onPostExecute(HeroItems heroItems) {
        onPostExecuteResponse.heroItemsProcessFinish(heroItems);
    }

    @Override
    protected HeroItems doInBackground(Void... unused) {
        String heroItemsLink = "";
        try {
            heroItemsLink = "https://ru.dotabuff.com/heroes/" +
                    HeroesPool.valueOf(Hero.toHeroesPoolHeroName(heroItems.getHero().getName())).title +
                    "/items?date=" + INTERVAL_OF_COLLECTED_HERO_ITEMS_DATA;
        } catch (IllegalArgumentException e) {
            //
        }

        ArrayList<Item> heroItemsByWinRateDiff = new ArrayList<>();
        try {
            Document heroItemsDoc = Jsoup.connect(heroItemsLink).get();

            Elements heroItemsElements = heroItemsDoc.getElementsByTag("tr");

            for (int i = 0; i < 6; i++) {
                heroItemsElements.remove(0);
            }

            boolean bootsOfTravelsFlag = false;
            for (Element heroItem : heroItemsElements) {
                String heroItemName = heroItem.children().remove(1).text();

                if (heroItemName.equals("Boots of Travel")) {
                    if (!bootsOfTravelsFlag) {
                        bootsOfTravelsFlag = true;
                    } else {
                        continue;
                    }
                }

                for (Item item : heroItems.getAllItems()) {

                    if (heroItemName.replace(" уровень", "").equals(item.getName())) {
                        String heroItemGames = heroItem.children().remove(2).text();
                        String heroItemWinRate = heroItem.children().remove(3).text();
                        int heroItemGamesToInt = Integer.valueOf(heroItemGames.replace(",", ""));
                        double heroItemWinRateToDouble = Double.valueOf(heroItemWinRate.substring(0, heroItemWinRate.length() - 1));

                        if (settings.getBestHeroItems()) {
                            if (heroItemGamesToInt > (int) ((double) (heroItems.getHero().getAllMatches()) * (item.getUseFrequency() / 100.0))) {
                                heroItemsByWinRateDiff.add(new Item(item.getImage(), item.getName(),
                                        item.getWinRateDiff() + heroItemWinRateToDouble,
                                        heroItemWinRateToDouble, item.getUseFrequency()));
                            }
                        } else {
                            heroItemsByWinRateDiff.add(new Item(item.getImage(), item.getName(),
                                    item.getWinRateDiff() + heroItemWinRateToDouble,
                                    heroItemWinRateToDouble, item.getUseFrequency()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Item.sortItems(heroItemsByWinRateDiff, 0, heroItemsByWinRateDiff.size() - 1, true);

        heroItems.setHeroItemsByWinRateDiff(heroItemsByWinRateDiff);
        return heroItems;
    }
}
