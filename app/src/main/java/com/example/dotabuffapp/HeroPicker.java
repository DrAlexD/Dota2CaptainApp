package com.example.dotabuffapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;

public class HeroPicker extends AsyncTask<Void, Void, Void> implements Serializable {
    private HeroTier tiers;
    private ArrayList<Heroes> allyHeroes; //союзные герои
    private ArrayList<Heroes> enemyHeroes;
    private ArrayList<HeroInfo> allySortedHeroesWinDif; //отсортированные контрпики союзных героев
    private ArrayList<HeroInfo> enemySortedHeroesWinDif;
    private Double allySumWinDif; //насколько хороши союзные герои против вражеских
    private Double enemySumWinDif;
    private transient Context context;

    HeroPicker(Context context) {
        allyHeroes = new ArrayList<>();
        enemyHeroes = new ArrayList<>();
        this.context = context;
    }

    ArrayList<HeroInfo> getSortedHeroesWinDif(boolean isAllyCounters) {
        return (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;
    }

    public boolean isNullHeroes() {
        return allyHeroes.isEmpty() && enemyHeroes.isEmpty();
    }

    void addAllyHero(Heroes hero) {
        allyHeroes.add(hero);
    }

    void addEnemyHero(Heroes hero) {
        enemyHeroes.add(hero);
    }

    void addAllyHeroes(ArrayList<Heroes> heroes) {
        allyHeroes.addAll(heroes);
    }

    void addEnemyHeroes(ArrayList<Heroes> heroes) {
        enemyHeroes.addAll(heroes);
    }

    Double getAllySumWinDif() {
        return Math.round(allySumWinDif * 100.0) / 100.0;
    }

    Double getEnemySumWinDif() {
        return Math.round(enemySumWinDif * 100.0) / 100.0;
    }

    void setTier(HeroTier tiers) {
        this.tiers = tiers;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Toast.makeText(context, "Пики обновлены", Toast.LENGTH_LONG)
                .show();
    }


    @Override
    protected Void doInBackground(Void... unused) {
        boolean isAllyCounters = false;
        allySumWinDif = 0.0;
        enemySumWinDif = 0.0;

        for (int ij = 0; ij < 2; ij++) {
            int numberOfHeroes;
            ArrayList<String> heroReadNames = new ArrayList<>();
            ArrayList<String> heroLinks = new ArrayList<>();
            ArrayList<HeroInfo> heroesWinDif = new ArrayList<>();

            if (isAllyCounters) {
                for (Heroes hero : allyHeroes) {
                    heroReadNames.add(hero.title);
                }
            } else {
                for (Heroes hero : enemyHeroes) {
                    heroReadNames.add(hero.title);
                }
            }
            for (String heroName : heroReadNames) {
                heroLinks.add("https://ru.dotabuff.com/heroes/" + heroName + "/counters?date=patch_7.22"); //week, month, patch_7.22
            }

            numberOfHeroes = heroReadNames.size();
            try {
                for (int i = 0; i < numberOfHeroes; i++) {
                    Document heroDoc = Jsoup.connect(heroLinks.get(i)).get();

                    Elements heroCounters = heroDoc.getElementsByTag("tr");

                    for (int j = 0; j < 18; j++) {
                        heroCounters.remove(0);
                    }

                    for (Element heroCounter : heroCounters) {
                        String heroCounterName = heroCounter.children().remove(1).text();
                        String heroCounterWinRate = heroCounter.children().remove(2).text();
                        double heroCounterWinRateToDouble =
                                Double.valueOf(heroCounterWinRate.substring(0, heroCounterWinRate.length() - 1));

                        boolean f = true;
                        //int k = 0;
                        for (HeroInfo h : heroesWinDif) {
                            if (h.getName() == heroCounterName) {
                                double oldWinRateDif = h.getWinRateDif();
                                double oldWinRate = h.getChangedWinRate();
                                h.setWinRateDif(heroCounterWinRateToDouble + oldWinRateDif);
                                h.setChangedWinRate(heroCounterWinRateToDouble + oldWinRate);
                            /*
                            heroesWinDif.remove(k);
                            heroesWinDif.add(new HeroInfo(0, heroCounterName,
                                    heroCounterWinRateToDouble + oldWinRateDif,
                                    heroCounterWinRateToDouble + oldWinRate));
                                    */
                                f = false;
                                break;
                            }
                            //k++;
                        }
                        if (f) {
                            for (SimpleEntry<String, SimpleEntry<Integer, SimpleEntry<String, Double>>> h : tiers.getHeroesTier()) {
                                if (heroCounterName.equals(h.getKey())) {
                                    heroesWinDif.add(new HeroInfo(0, heroCounterName,
                                            heroCounterWinRateToDouble,
                                            heroCounterWinRateToDouble
                                                    + h.getValue().getValue().getValue()));
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            Collections.sort(heroesWinDif);

            for (Heroes hero : allyHeroes) {
                String key = hero.toString();
                String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
                switch (key) {
                    case "AntiMage":
                        newKey = "Anti-Mage";
                        break;
                    case "KeeperOfTheLight":
                        newKey = "Keeper of the Light";
                        break;
                    case "QueenOfPain":
                        newKey = "Queen of Pain";
                        break;
                    case "NaturesProphet":
                        newKey = "Nature's Prophet";
                        break;
                }
                int k = 0;
                if (numberOfHeroes > 0) {
                    for (HeroInfo h : heroesWinDif) {
                        if (h.getName().equals(newKey)) {
                            double oldWinRateDif = h.getWinRateDif();
                            heroesWinDif.remove(k);
                            if (!isAllyCounters)
                                allySumWinDif += oldWinRateDif;
                            break;
                        }
                        k++;
                    }
                }
            }

            for (Heroes hero : enemyHeroes) {
                String key = hero.toString();
                String newKey = key.replaceAll("([a-z])([A-Z])", "$1 $2");
                switch (key) {
                    case "AntiMage":
                        newKey = "Anti-Mage";
                        break;
                    case "KeeperOfTheLight":
                        newKey = "Keeper of the Light";
                        break;
                    case "QueenOfPain":
                        newKey = "Queen of Pain";
                        break;
                    case "NaturesProphet":
                        newKey = "Nature's Prophet";
                        break;
                }

                if (numberOfHeroes > 0) {
                    int k = 0;
                    for (HeroInfo h : heroesWinDif) {
                        if (h.getName().equals(newKey)) {
                            double oldWinRateDif = h.getWinRateDif();
                            heroesWinDif.remove(k);
                            if (isAllyCounters)
                                enemySumWinDif += oldWinRateDif;
                            break;
                        }
                        k++;
                    }
                }
            }

            for (HeroInfo h : heroesWinDif) {
                switch (h.getName()) {
                    case ("Abaddon"):
                        h.setHeroImage(R.drawable.abaddon);
                        break;
                    case ("Alchemist"):
                        h.setHeroImage(R.drawable.alchemist);
                        break;
                    case ("Ancient Apparition"):
                        h.setHeroImage(R.drawable.ancient_apparition);
                        break;
                    case ("Anti-Mage"):
                        h.setHeroImage(R.drawable.anti_mage);
                        break;
                    case ("Arc Warden"):
                        h.setHeroImage(R.drawable.arc_warden);
                        break;
                    case ("Axe"):
                        h.setHeroImage(R.drawable.axe);
                        break;
                    /*
                case ("Bane"):
                    h.setHeroImage(R.drawable.bane);
                    break;
                case ("Batrider"):
                    h.setHeroImage(R.drawable.batrider);
                    break;
                case ("Beastmaster"):
                    h.setHeroImage(R.drawable.beastmaster);
                    break;
                case ("Bloodseeker"):
                    h.setHeroImage(R.drawable.bloodseeker);
                    break;
                case ("Bounty Hunter"):
                    h.setHeroImage(R.drawable.bounty_hunter);
                    break;
                case ("Brewmaster"):
                    h.setHeroImage(R.drawable.brewmaster);
                    break;
                case ("Bristleback"):
                    h.setHeroImage(R.drawable.bristleback);
                    break;
                case ("Broodmother"):
                    h.setHeroImage(R.drawable.broodmother);
                    break;
                case ("Centaur Warrunner"):
                    h.setHeroImage(R.drawable.centaur_warrunner);
                    break;
                case ("Chaos Knight"):
                    h.setHeroImage(R.drawable.chaos_knight);
                    break;
                case ("Chen"):
                    h.setHeroImage(R.drawable.chen);
                    break;
                case ("Clinkz"):
                    h.setHeroImage(R.drawable.clinkz);
                    break;
                case ("Clockwerk"):
                    h.setHeroImage(R.drawable.clockwerk);
                    break;
                case ("Crystal Maiden"):
                    h.setHeroImage(R.drawable.crystal_maiden);
                    break;
                case ("Dark Seer"):
                    h.setHeroImage(R.drawable.dark_seer);
                    break;
                case ("Dark Willow"):
                    h.setHeroImage(R.drawable.dark_willow);
                    break;
                case ("Dazzle"):
                    h.setHeroImage(R.drawable.dazzle);
                    break;
                case ("Death Prophet"):
                    h.setHeroImage(R.drawable.death_prophet);
                    break;
                case ("Disruptor"):
                    h.setHeroImage(R.drawable.disruptor);
                    break;
                case ("Doom"):
                    h.setHeroImage(R.drawable.doom);
                    break;
                case ("Dragon Knight"):
                    h.setHeroImage(R.drawable.dragon_knight);
                    break;
                case ("Drow Ranger"):
                    h.setHeroImage(R.drawable.drow_ranger);
                    break;
                case ("Earth Spirit"):
                    h.setHeroImage(R.drawable.earth_spirit);
                    break;
                case ("Earthshaker"):
                    h.setHeroImage(R.drawable.earthshaker);
                    break;
                case ("Elder Titan"):
                    h.setHeroImage(R.drawable.elder_titan);
                    break;
                case ("Ember Spirit"):
                    h.setHeroImage(R.drawable.ember_spirit);
                    break;
                case ("Enchantress"):
                    h.setHeroImage(R.drawable.enchantress);
                    break;
                case ("Enigma"):
                    h.setHeroImage(R.drawable.enigma);
                    break;
                case ("Faceless Void"):
                    h.setHeroImage(R.drawable.faceless_void);
                    break;
                case ("Grimstroke"):
                    h.setHeroImage(R.drawable.grimstroke);
                    break;
                case ("Gyrocopter"):
                    h.setHeroImage(R.drawable.gyrocopter);
                    break;
                case ("Huskar"):
                    h.setHeroImage(R.drawable.huskar);
                    break;
                case ("Invoker"):
                    h.setHeroImage(R.drawable.invoker);
                    break;
                case ("Io"):
                    h.setHeroImage(R.drawable.io);
                    break;
                case ("Jakiro"):
                    h.setHeroImage(R.drawable.jakiro);
                    break;
                case ("Juggernaut"):
                    h.setHeroImage(R.drawable.juggernaut);
                    break;
                case ("Keeper of the Light"):
                    h.setHeroImage(R.drawable.keeper_of_the_light);
                    break;
                case ("Kunkka"):
                    h.setHeroImage(R.drawable.kunkka);
                    break;
                case ("Legion Commander"):
                    h.setHeroImage(R.drawable.legion_commander);
                    break;
                case ("Leshrac"):
                    h.setHeroImage(R.drawable.leshrac);
                    break;
                case ("Lich"):
                    h.setHeroImage(R.drawable.lich);
                    break;
                case ("Lifestealer"):
                    h.setHeroImage(R.drawable.lifestealer);
                    break;
                case ("Lina"):
                    h.setHeroImage(R.drawable.lina);
                    break;
                case ("Lion"):
                    h.setHeroImage(R.drawable.lion);
                    break;
                case ("Lone Druid"):
                    h.setHeroImage(R.drawable.lone_druid);
                    break;
                case ("Luna"):
                    h.setHeroImage(R.drawable.luna);
                    break;
                case ("Lycan"):
                    h.setHeroImage(R.drawable.lycan);
                    break;
                case ("Magnus"):
                    h.setHeroImage(R.drawable.magnus);
                    break;
                case ("Mars"):
                    h.setHeroImage(R.drawable.mars);
                    break;
                case ("Medusa"):
                    h.setHeroImage(R.drawable.medusa);
                    break;
                case ("Meepo"):
                    h.setHeroImage(R.drawable.meepo);
                    break;
                case ("Mirana"):
                    h.setHeroImage(R.drawable.mirana);
                    break;
                case ("Monkey King"):
                    h.setHeroImage(R.drawable.monkey_king);
                    break;
                case ("Morphling"):
                    h.setHeroImage(R.drawable.morphling);
                    break;
                case ("Naga Siren"):
                    h.setHeroImage(R.drawable.naga_siren);
                    break;
                case ("Nature's Prophet"):
                    h.setHeroImage(R.drawable.natures_prophet);
                    break;
                case ("Necrophos"):
                    h.setHeroImage(R.drawable.necrophos);
                    break;
                case ("Night Stalker"):
                    h.setHeroImage(R.drawable.night_stalker);
                    break;
                case ("Nyx Assassin"):
                    h.setHeroImage(R.drawable.nyx_assassin);
                    break;
                case ("Ogre Magi"):
                    h.setHeroImage(R.drawable.ogre_magi);
                    break;
                case ("Omniknight"):
                    h.setHeroImage(R.drawable.omniknight);
                    break;
                case ("Oracle"):
                    h.setHeroImage(R.drawable.oracle);
                    break;
                case ("Outworld Devourer"):
                    h.setHeroImage(R.drawable.outworld_devourer);
                    break;
                case ("Pangolier"):
                    h.setHeroImage(R.drawable.pangolier);
                    break;
                case ("Phantom Assassin"):
                    h.setHeroImage(R.drawable.phantom_assassin);
                    break;
                case ("Phantom Lancer"):
                    h.setHeroImage(R.drawable.phantom_lancer);
                    break;
                case ("Phoenix"):
                    h.setHeroImage(R.drawable.phoenix);
                    break;
                case ("Puck"):
                    h.setHeroImage(R.drawable.puck);
                    break;
                case ("Pudge"):
                    h.setHeroImage(R.drawable.pudge);
                    break;
                case ("Pugna"):
                    h.setHeroImage(R.drawable.pugna);
                    break;
                case ("Queen of Pain"):
                    h.setHeroImage(R.drawable.queen_of_pain);
                    break;
                case ("Razor"):
                    h.setHeroImage(R.drawable.razor);
                    break;
                case ("Riki"):
                    h.setHeroImage(R.drawable.riki);
                    break;
                case ("Rubick"):
                    h.setHeroImage(R.drawable.rubick);
                    break;
                case ("Sand King"):
                    h.setHeroImage(R.drawable.sand_king);
                    break;
                case ("Shadow Demon"):
                    h.setHeroImage(R.drawable.shadow_demon);
                    break;
                case ("Shadow Fiend"):
                    h.setHeroImage(R.drawable.shadow_fiend);
                    break;
                case ("Shadow Shaman"):
                    h.setHeroImage(R.drawable.shadow_shaman);
                    break;
                case ("Silencer"):
                    h.setHeroImage(R.drawable.silencer);
                    break;
                case ("Skywrath Mage"):
                    h.setHeroImage(R.drawable.skywrath_mage);
                    break;
                case ("Slardar"):
                    h.setHeroImage(R.drawable.slardar);
                    break;
                case ("Slark"):
                    h.setHeroImage(R.drawable.slark);
                    break;
                case ("Sniper"):
                    h.setHeroImage(R.drawable.sniper);
                    break;
                case ("Spectre"):
                    h.setHeroImage(R.drawable.spectre);
                    break;
                case ("Spirit Breaker"):
                    h.setHeroImage(R.drawable.spirit_breaker);
                    break;
                case ("Storm Spirit"):
                    h.setHeroImage(R.drawable.storm_spirit);
                    break;
                case ("Sven"):
                    h.setHeroImage(R.drawable.sven);
                    break;
                case ("Techies"):
                    h.setHeroImage(R.drawable.techies);
                    break;
                case ("Templar Assassin"):
                    h.setHeroImage(R.drawable.templar_assassin);
                    break;
                case ("Terrorblade"):
                    h.setHeroImage(R.drawable.terrorblade);
                    break;
                case ("Tidehunter"):
                    h.setHeroImage(R.drawable.tidehunter);
                    break;
                case ("Timbersaw"):
                    h.setHeroImage(R.drawable.timbersaw);
                    break;
                case ("Tinker"):
                    h.setHeroImage(R.drawable.tinker);
                    break;
                case ("Tiny"):
                    h.setHeroImage(R.drawable.tiny);
                    break;
                case ("Treant Protector"):
                    h.setHeroImage(R.drawable.treant_protector);
                    break;
                case ("Troll Warlord"):
                    h.setHeroImage(R.drawable.troll_warlord);
                    break;
                case ("Tusk"):
                    h.setHeroImage(R.drawable.tusk);
                    break;
                case ("Underlord"):
                    h.setHeroImage(R.drawable.underlord);
                    break;
                case ("Undying"):
                    h.setHeroImage(R.drawable.undying);
                    break;
                case ("Ursa"):
                    h.setHeroImage(R.drawable.ursa);
                    break;
                case ("Vengeful Spirit"):
                    h.setHeroImage(R.drawable.vengeful_spirit);
                    break;
                case ("Venomancer"):
                    h.setHeroImage(R.drawable.venomancer);
                    break;
                case ("Viper"):
                    h.setHeroImage(R.drawable.viper);
                    break;
                case ("Visage"):
                    h.setHeroImage(R.drawable.visage);
                    break;
                case ("Warlock"):
                    h.setHeroImage(R.drawable.warlock);
                    break;
                case ("Weaver"):
                    h.setHeroImage(R.drawable.weaver);
                    break;
                case ("Windranger"):
                    h.setHeroImage(R.drawable.windranger);
                    break;
                case ("Winter Wyvern"):
                    h.setHeroImage(R.drawable.winter_wyvern);
                    break;
                case ("Witch Doctor"):
                    h.setHeroImage(R.drawable.witch_doctor);
                    break;
                case ("Wraith King"):
                    h.setHeroImage(R.drawable.wraith_king);
                    break;
                case ("Zeus"):
                    h.setHeroImage(R.drawable.zeus);
                    break;
                    */
                }
            }
            if (isAllyCounters)
                allySortedHeroesWinDif = heroesWinDif;
            else
                enemySortedHeroesWinDif = heroesWinDif;
            isAllyCounters = true;
        }
        return null;
    }

    private void writeInFile(boolean isAllyCounters) {
        ArrayList<HeroInfo> sortedHeroesWinDif = (isAllyCounters) ? allySortedHeroesWinDif : enemySortedHeroesWinDif;

        try (FileWriter writer = new FileWriter("C:/Users/alexa/AndroidStudioProjects/DotabuffApp/app/src/main/java/com/example/dotabuffapp/PicksAndBans.txt", true)) {
            for (HeroInfo heroWinDif : sortedHeroesWinDif) {
                String key = heroWinDif.getName();
                String newKey = key.replace(" ", "");
                switch (key) {
                    case "Anti-Mage":
                        newKey = "AntiMage";
                        break;
                    case "Keeper of the Light":
                        newKey = "KeeperOfTheLight";
                        break;
                    case "Queen of Pain":
                        newKey = "QueenOfPain";
                        break;
                    case "Nature's Prophet":
                        newKey = "NaturesProphet";
                        break;
                }

                try {
                    HeroPool.valueOf(newKey);
                    double valueDif = Math.round(heroWinDif.getWinRateDif() * 100.0) / 100.0;
                    double valueWinRate = Math.round(heroWinDif.getChangedWinRate() * 100.0) / 100.0;

                    if (valueDif > 0) {
                        //writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + tiers.heroesTier.get(key).getKey() + "| +" + valueDif + "% | " + valueWinRate + "%\n");
                    } else {
                        //writer.append(Heroes.valueOf(newKey).pos + "|" + key + "|" + tiers.heroesTier.get(key).getKey() + "| " + valueDif + "% | " + valueWinRate + "%\n");
                    }
                } catch (IllegalArgumentException e) {
                    //
                }
            }

            writer.append("\n\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
