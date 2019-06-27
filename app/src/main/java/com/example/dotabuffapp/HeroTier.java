package com.example.dotabuffapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

class HeroTier extends AsyncTask<Void, Void, Void> implements Serializable {
    private ArrayList<HeroInfo> heroesTier;
    private transient Context context;

    HeroTier(Context context) {
        this.context = context;
        this.heroesTier = new ArrayList<>();
    }

    ArrayList<HeroInfo> getHeroesTier() {
        return heroesTier;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Toast.makeText(context, "Винрейты героев загружены", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... unused) {
        try {
            Document heroesDoc = Jsoup.connect("https://ru.dotabuff.com/heroes/trends").get();

            Elements heroes = heroesDoc.getElementsByTag("tr");

            for (int j = 0; j < 3; j++) {
                heroes.remove(0);
            }

            for (Element hero : heroes) {
                String heroName = hero.children().remove(0).text();
                String heroWinRate = hero.children().remove(2).text();
                double heroWinRateToDouble =
                        Double.valueOf(heroWinRate.substring(0, heroWinRate.length() - 1));

                int se = 0;
                switch (heroName) {
                    case ("Abaddon"):
                        se = R.drawable.abaddon;
                        break;
                    case ("Alchemist"):
                        se = R.drawable.alchemist;
                        break;
                    case ("Ancient Apparition"):
                        se = R.drawable.ancient_apparition;
                        break;
                    case ("Anti-Mage"):
                        se = R.drawable.anti_mage;
                        break;
                    case ("Arc Warden"):
                        se = R.drawable.arc_warden;
                        break;
                    case ("Axe"):
                        se = R.drawable.axe;
                        break;
                        /*
                    case ("Bane"):
                        se = R.drawable.bane;
                        break;
                    case ("Batrider"):
                        se = R.drawable.batrider;
                        break;
                    case ("Beastmaster"):
                        se = R.drawable.beastmaster;
                        break;
                    case ("Bloodseeker"):
                        se = R.drawable.bloodseeker;
                        break;
                    case ("Bounty Hunter"):
                        se = R.drawable.bounty_hunter;
                        break;
                    case ("Brewmaster"):
                        se = R.drawable.brewmaster;
                        break;
                    case ("Bristleback"):
                        se = R.drawable.bristleback;
                        break;
                    case ("Broodmother"):
                        se = R.drawable.broodmother;
                        break;
                    case ("Centaur Warrunner"):
                        se = R.drawable.centaur_warrunner;
                        break;
                    case ("Chaos Knight"):
                        se = R.drawable.chaos_knight;
                        break;
                    case ("Chen"):
                        se = R.drawable.chen;
                        break;
                    case ("Clinkz"):
                        se = R.drawable.clinkz;
                        break;
                    case ("Clockwerk"):
                        se = R.drawable.clockwerk;
                        break;
                    case ("Crystal Maiden"):
                        se = R.drawable.crystal_maiden;
                        break;
                    case ("Dark Seer"):
                        se = R.drawable.dark_seer;
                        break;
                    case ("Dark Willow"):
                        se = R.drawable.dark_willow;
                        break;
                    case ("Dazzle"):
                        se = R.drawable.dazzle;
                        break;
                    case ("Death Prophet"):
                        se = R.drawable.death_prophet;
                        break;
                    case ("Disruptor"):
                        se = R.drawable.disruptor;
                        break;
                    case ("Doom"):
                        se = R.drawable.doom;
                        break;
                    case ("Dragon Knight"):
                        se = R.drawable.dragon_knight;
                        break;
                    case ("Drow Ranger"):
                        se = R.drawable.drow_ranger;
                        break;
                    case ("Earth Spirit"):
                        se = R.drawable.earth_spirit;
                        break;
                    case ("Earthshaker"):
                        se = R.drawable.earthshaker;
                        break;
                    case ("Elder Titan"):
                        se = R.drawable.elder_titan;
                        break;
                    case ("Ember Spirit"):
                        se = R.drawable.ember_spirit;
                        break;
                    case ("Enchantress"):
                        se = R.drawable.enchantress;
                        break;
                    case ("Enigma"):
                        se = R.drawable.enigma;
                        break;
                    case ("Faceless Void"):
                        se = R.drawable.faceless_void;
                        break;
                    case ("Grimstroke"):
                        se = R.drawable.grimstroke;
                        break;
                    case ("Gyrocopter"):
                        se = R.drawable.gyrocopter;
                        break;
                    case ("Huskar"):
                        se = R.drawable.huskar;
                        break;
                    case ("Invoker"):
                        se = R.drawable.invoker;
                        break;
                    case ("Io"):
                        se = R.drawable.io;
                        break;
                    case ("Jakiro"):
                        se = R.drawable.jakiro;
                        break;
                    case ("Juggernaut"):
                        se = R.drawable.juggernaut;
                        break;
                    case ("Keeper of the Light"):
                        se = R.drawable.keeper_of_the_light;
                        break;
                    case ("Kunkka"):
                        se = R.drawable.kunkka;
                        break;
                    case ("Legion Commander"):
                        se = R.drawable.legion_commander;
                        break;
                    case ("Leshrac"):
                        se = R.drawable.leshrac;
                        break;
                    case ("Lich"):
                        se = R.drawable.lich;
                        break;
                    case ("Lifestealer"):
                        se = R.drawable.lifestealer;
                        break;
                    case ("Lina"):
                        se = R.drawable.lina;
                        break;
                    case ("Lion"):
                        se = R.drawable.lion;
                        break;
                    case ("Lone Druid"):
                        se = R.drawable.lone_druid;
                        break;
                    case ("Luna"):
                        se = R.drawable.luna;
                        break;
                    case ("Lycan"):
                        se = R.drawable.lycan;
                        break;
                    case ("Magnus"):
                        se = R.drawable.magnus;
                        break;
                    case ("Mars"):
                        se = R.drawable.mars;
                        break;
                    case ("Medusa"):
                        se = R.drawable.medusa;
                        break;
                    case ("Meepo"):
                        se = R.drawable.meepo;
                        break;
                    case ("Mirana"):
                        se = R.drawable.mirana;
                        break;
                    case ("Monkey King"):
                        se = R.drawable.monkey_king;
                        break;
                    case ("Morphling"):
                        se = R.drawable.morphling;
                        break;
                    case ("Naga Siren"):
                        se = R.drawable.naga_siren;
                        break;
                    case ("Nature's Prophet"):
                        se = R.drawable.natures_prophet;
                        break;
                    case ("Necrophos"):
                        se = R.drawable.necrophos;
                        break;
                    case ("Night Stalker"):
                        se = R.drawable.night_stalker;
                        break;
                    case ("Nyx Assassin"):
                        se = R.drawable.nyx_assassin;
                        break;
                    case ("Ogre Magi"):
                        se = R.drawable.ogre_magi;
                        break;
                    case ("Omniknight"):
                        se = R.drawable.omniknight;
                        break;
                    case ("Oracle"):
                        se = R.drawable.oracle;
                        break;
                    case ("Outworld Devourer"):
                        se = R.drawable.outworld_devourer;
                        break;
                    case ("Pangolier"):
                        se = R.drawable.pangolier;
                        break;
                    case ("Phantom Assassin"):
                        se = R.drawable.phantom_assassin;
                        break;
                    case ("Phantom Lancer"):
                        se = R.drawable.phantom_lancer;
                        break;
                    case ("Phoenix"):
                        se = R.drawable.phoenix;
                        break;
                    case ("Puck"):
                        se = R.drawable.puck;
                        break;
                    case ("Pudge"):
                        se = R.drawable.pudge;
                        break;
                    case ("Pugna"):
                        se = R.drawable.pugna;
                        break;
                    case ("Queen of Pain"):
                        se = R.drawable.queen_of_pain;
                        break;
                    case ("Razor"):
                        se = R.drawable.razor;
                        break;
                    case ("Riki"):
                        se = R.drawable.riki;
                        break;
                    case ("Rubick"):
                        se = R.drawable.rubick;
                        break;
                    case ("Sand King"):
                        se = R.drawable.sand_king;
                        break;
                    case ("Shadow Demon"):
                        se = R.drawable.shadow_demon;
                        break;
                    case ("Shadow Fiend"):
                        se = R.drawable.shadow_fiend;
                        break;
                    case ("Shadow Shaman"):
                        se = R.drawable.shadow_shaman;
                        break;
                    case ("Silencer"):
                        se = R.drawable.silencer;
                        break;
                    case ("Skywrath Mage"):
                        se = R.drawable.skywrath_mage;
                        break;
                    case ("Slardar"):
                        se = R.drawable.slardar;
                        break;
                    case ("Slark"):
                        se = R.drawable.slark;
                        break;
                    case ("Sniper"):
                        se = R.drawable.sniper;
                        break;
                    case ("Spectre"):
                        se = R.drawable.spectre;
                        break;
                    case ("Spirit Breaker"):
                        se = R.drawable.spirit_breaker;
                        break;
                    case ("Storm Spirit"):
                        se = R.drawable.storm_spirit;
                        break;
                    case ("Sven"):
                        se = R.drawable.sven;
                        break;
                    case ("Techies"):
                        se = R.drawable.techies;
                        break;
                    case ("Templar Assassin"):
                        se = R.drawable.templar_assassin;
                        break;
                    case ("Terrorblade"):
                        se = R.drawable.terrorblade;
                        break;
                    case ("Tidehunter"):
                        se = R.drawable.tidehunter;
                        break;
                    case ("Timbersaw"):
                        se = R.drawable.timbersaw;
                        break;
                    case ("Tinker"):
                        se = R.drawable.tinker;
                        break;
                    case ("Tiny"):
                        se = R.drawable.tiny;
                        break;
                    case ("Treant Protector"):
                        se = R.drawable.treant_protector;
                        break;
                    case ("Troll Warlord"):
                        se = R.drawable.troll_warlord;
                        break;
                    case ("Tusk"):
                        se = R.drawable.tusk;
                        break;
                    case ("Underlord"):
                        se = R.drawable.underlord;
                        break;
                    case ("Undying"):
                        se = R.drawable.undying;
                        break;
                    case ("Ursa"):
                        se = R.drawable.ursa;
                        break;
                    case ("Vengeful Spirit"):
                        se = R.drawable.vengeful_spirit;
                        break;
                    case ("Venomancer"):
                        se = R.drawable.venomancer;
                        break;
                    case ("Viper"):
                        se = R.drawable.viper;
                        break;
                    case ("Visage"):
                        se = R.drawable.visage;
                        break;
                    case ("Warlock"):
                        se = R.drawable.warlock;
                        break;
                    case ("Weaver"):
                        se = R.drawable.weaver;
                        break;
                    case ("Windranger"):
                        se = R.drawable.windranger;
                        break;
                    case ("Winter Wyvern"):
                        se = R.drawable.winter_wyvern;
                        break;
                    case ("Witch Doctor"):
                        se = R.drawable.witch_doctor;
                        break;
                    case ("Wraith King"):
                        se = R.drawable.wraith_king;
                        break;
                    case ("Zeus"):
                        se = R.drawable.zeus;
                        break;
                    */
                }
                if (heroWinRateToDouble > 55.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "S", heroWinRateToDouble));
                else if (heroWinRateToDouble > 53.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "A", heroWinRateToDouble));
                else if (heroWinRateToDouble > 51.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "B", heroWinRateToDouble));
                else if (heroWinRateToDouble > 49.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "C", heroWinRateToDouble));
                else if (heroWinRateToDouble > 47.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "D", heroWinRateToDouble));
                else if (heroWinRateToDouble > 45.0)
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "E", heroWinRateToDouble));
                else
                    this.heroesTier.add(new HeroInfo(se, heroName, 0.0, "F", heroWinRateToDouble));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}