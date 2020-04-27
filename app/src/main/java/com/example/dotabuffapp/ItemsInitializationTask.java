package com.example.dotabuffapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemsInitializationTask extends AsyncTask<Void, Void, HeroItems> implements Serializable {
    private HeroItems heroItems;
    private HeroItemsAsyncResponse onPostExecuteResponse;

    private final String INTERVAL_OF_COLLECTED_ITEMS_DATA = "month"; //week, month, 3month, patch_7.22, season_3

    ItemsInitializationTask(HeroForItemsSelectionActivity onPostExecuteResponse, HeroItems heroItems) {
        this.onPostExecuteResponse = onPostExecuteResponse;
        this.heroItems = heroItems;
    }

    @Override
    protected void onPostExecute(HeroItems heroItems) {
        onPostExecuteResponse.heroItemsProcessFinish(heroItems);
    }

    @Override
    protected HeroItems doInBackground(Void... unused) {
        String itemsLink = "https://ru.dotabuff.com/items?date=" + INTERVAL_OF_COLLECTED_ITEMS_DATA;

        ArrayList<Item> allItems = new ArrayList<>();
        try {
            Document itemsDoc = Jsoup.connect(itemsLink).get();

            Elements itemsElements = itemsDoc.getElementsByTag("tr");

            itemsElements.remove(0);

            boolean bootsOfTravelsFlag = false;
            for (Element item : itemsElements) {
                String itemName = item.children().remove(1).text();

                if (itemName.equals("Boots of Travel")) {
                    if (!bootsOfTravelsFlag) {
                        bootsOfTravelsFlag = true;
                    } else {
                        continue;
                    }
                }

                try {
                    ItemsPool.valueOf(Item.toItemsPoolItemName(itemName));

                    String itemUseFrequency = item.children().remove(3).text();
                    String itemWinRate = item.children().remove(4).text();
                    double itemUseFrequencyToDouble = Double.parseDouble(itemUseFrequency.substring(0, itemUseFrequency.length() - 1));
                    double itemWinRateToDouble = Double.parseDouble(itemWinRate.substring(0, itemWinRate.length() - 1));

                    allItems.add(new Item(Item.getItemImageByName(itemName),
                            itemName.replace(" уровень", ""),
                            -itemWinRateToDouble, 0.0, itemUseFrequencyToDouble));

                } catch (IllegalArgumentException e) {
                    //
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        heroItems.setAllItems(allItems);
        return heroItems;
    }
}
