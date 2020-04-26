package com.example.dotabuffapp;

enum HeroesPool {
    Abaddon("abaddon", "3|5"),
    Alchemist("alchemist", "1|2"),
    AncientApparition("ancient-apparition", "5"),
    AntiMage("anti-mage", "1"),
    ArcWarden("arc-warden", "2"),
    Axe("axe", "3"),
    Bane("bane", "5"),
    Batrider("batrider", "3"),
    Beastmaster("beastmaster", "3"),
    Bloodseeker("bloodseeker", "1"),
    BountyHunter("bounty-hunter", "3|4"),
    Brewmaster("brewmaster", "3"),
    Bristleback("bristleback", "1|3"),
    Broodmother("broodmother", "2"),
    CentaurWarrunner("centaur-warrunner", "3"),
    ChaosKnight("chaos-knight", "1"),
    Chen("chen", "5"),
    Clinkz("clinkz", "1|2"),
    Clockwerk("clockwerk", "3|4"),
    CrystalMaiden("crystal-maiden", "4|5"),
    DarkSeer("dark-seer", "3"),
    DarkWillow("dark-willow", "4|5"),
    Dazzle("dazzle", "2|4|5"),
    DeathProphet("death-prophet", "2"),
    Disruptor("disruptor", "4|5"),
    Doom("doom", "3"),
    DragonKnight("dragon-knight", "2"),
    DrowRanger("drow-ranger", "1"),
    EarthSpirit("earth-spirit", "4"),
    Earthshaker("earthshaker", "3|4"),
    ElderTitan("elder-titan", "4"),
    EmberSpirit("ember-spirit", "1|2"),
    Enchantress("enchantress", "3|4"),
    Enigma("enigma", "3"),
    FacelessVoid("faceless-void", "1"),
    Grimstroke("grimstroke", "4|5"),
    Gyrocopter("gyrocopter", "1"),
    Huskar("huskar", "2"),
    Invoker("invoker", "2"),
    Io("io", "4|5"),
    Jakiro("jakiro", "4|5"),
    Juggernaut("juggernaut", "1"),
    KeeperOfTheLight("keeper-of-the-light", "5"),
    Kunkka("kunkka", "2|3"),
    LegionCommander("legion-commander", "3"),
    Leshrac("leshrac", "2|3"),
    Lich("lich", "5"),
    Lifestealer("lifestealer", "1"),
    Lina("lina", "2|4"),
    Lion("lion", "5"),
    LoneDruid("lone-druid", "1|2"),
    Luna("luna", "1"),
    Lycan("lycan", "1"),
    Magnus("magnus", "2|3"),
    Mars("mars", "2|3"),
    Medusa("medusa", "1|2"),
    Meepo("meepo", "2"),
    Mirana("mirana", "2|4"),
    MonkeyKing("monkey-king", "1|2"),
    Morphling("morphling", "1|2"),
    NagaSiren("naga-siren", "1"),
    NaturesProphet("natures-prophet", "3|5"),
    Necrophos("necrophos", "2|3"),
    NightStalker("night-stalker", "3"),
    NyxAssassin("nyx-assassin", "4"),
    OgreMagi("ogre-magi", "4|5"),
    Omniknight("omniknight", "3"),
    Oracle("oracle", "5"),
    OutworldDevourer("outworld-devourer", "2"),
    Pangolier("pangolier", "3|4"),
    PhantomAssassin("phantom-assassin", "1"),
    PhantomLancer("phantom-lancer", "1"),
    Phoenix("phoenix", "3|4|5"),
    Puck("puck", "2|3|4"),
    Pudge("pudge", "4"),
    Pugna("pugna", "2|3"),
    QueenOfPain("queen-of-pain", "2"),
    Razor("razor", "1|2"),
    Riki("riki", "1|3"),
    Rubick("rubick", "4"),
    SandKing("sand-king", "3"),
    ShadowDemon("shadow-demon", "4|5"),
    ShadowFiend("shadow-fiend", "2"),
    ShadowShaman("shadow-shaman", "5"),
    Silencer("silencer", "5"),
    SkywrathMage("skywrath-mage", "4"),
    Slardar("slardar", "3"),
    Slark("slark", "1"),
    Snapfire("snapfire","4|5"),
    Sniper("sniper", "2"),
    Spectre("spectre", "1"),
    SpiritBreaker("spirit-breaker", "4"),
    StormSpirit("storm-spirit", "2"),
    Sven("sven", "1"),
    Techies("techies", "4"),
    TemplarAssassin("templar-assassin", "2"),
    Terrorblade("terrorblade", "1"),
    Tidehunter("tidehunter", "3"),
    Timbersaw("timbersaw", "1|3"),
    Tinker("tinker", "2"),
    Tiny("tiny", "4"),
    TreantProtector("treant-protector", "4"),
    TrollWarlord("troll-warlord", "1"),
    Tusk("tusk", "4"),
    Underlord("underlord", "3"),
    Undying("undying", "5"),
    Ursa("ursa", "1"),
    VengefulSpirit("vengeful-spirit", "5"),
    Venomancer("venomancer", "3"),
    Viper("viper", "2"),
    Visage("visage", "2"),
    VoidSpirit("void-spirit", "2|3"),
    Warlock("warlock", "5"),
    Weaver("weaver", "1|3|4"),
    Windranger("windranger", "2"),
    WinterWyvern("winter-wyvern", "4|5"),
    WitchDoctor("witch-doctor", "5"),
    WraithKing("wraith-king", "1"),
    Zeus("zeus", "2");

    String title;
    String pos;

    HeroesPool(String title, String pos) {
        this.title = title;
        this.pos = pos;
    }

    public String toHeroName() {
        String heroNameWithOutSeparators = this.toString();
        String heroName;

        switch (heroNameWithOutSeparators) {
            case "AntiMage":
                heroName = "Anti-Mage";
                break;
            case "KeeperOfTheLight":
                heroName = "Keeper of the Light";
                break;
            case "QueenOfPain":
                heroName = "Queen of Pain";
                break;
            case "NaturesProphet":
                heroName = "Nature's Prophet";
                break;
            default:
                heroName = heroNameWithOutSeparators.replaceAll("([a-z])([A-Z])", "$1 $2");
        }

        return heroName;
    }
}