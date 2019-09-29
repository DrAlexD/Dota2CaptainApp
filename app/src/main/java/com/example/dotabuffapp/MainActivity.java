package com.example.dotabuffapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements HeroesAsyncResponse, Serializable {
    private final int NUMBER_OF_PICKS_PLUS_BANS = 22;
    private final int NUMBER_OF_POSITIONS = 5;
    private final int NUMBER_OF_HEROES_FOR_EVERY_POS = 10;

    private Settings settings;
    private HeroesCountersTask heroesCountersTask;
    private HeroesCounters heroesCounters;
    private HeroesPool[] pickAndBansHeroes = new HeroesPool[NUMBER_OF_PICKS_PLUS_BANS];
    private HeroesPool[][] recommendedHeroesPicks = new HeroesPool[NUMBER_OF_POSITIONS][NUMBER_OF_HEROES_FOR_EVERY_POS];
    private HeroesPool[][] recommendedHeroesBans = new HeroesPool[NUMBER_OF_POSITIONS][NUMBER_OF_HEROES_FOR_EVERY_POS];
    private int numberOfPressedImageOfPicksAndBansHeroes;
    private int numberOfRemainingPickUpdateProcesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new PageTurningAdapter(this, getSupportFragmentManager()));
        pager.setCurrentItem(1);
        pager.setOffscreenPageLimit(2);

        settings = new Settings();
        settings.setPersonalHeroesPool(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("personalHeroesPool", false));

        heroesCounters = new HeroesCounters();
        HeroesInitializationTask heroesInitializationTask = new HeroesInitializationTask(this);
        heroesCounters.setHeroesInitialization(heroesInitializationTask.getHeroesInitialization());
        heroesInitializationTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("personalHeroesPool", false) != settings.getPersonalHeroesPool()) {
            settings.setPersonalHeroesPool(!settings.getPersonalHeroesPool());
            displayRecommendedPicks();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getTitle().toString()) {
            case "Picker":
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(mainIntent);
                return true;
            case "Items":
                Intent itemsIntent = new Intent(this, HeroForItemsSelectionActivity.class);
                itemsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                itemsIntent.putExtra("HeroesCounters", heroesCounters);
                startActivity(itemsIntent);
                return true;
            case "Settings":
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                settingsIntent.putExtra("HeroesCounters", heroesCounters);
                startActivity(settingsIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectOrClearHero(View view) {
        String imageViewTagString = (String) view.getTag();
        numberOfPressedImageOfPicksAndBansHeroes = Integer.parseInt(imageViewTagString);
        HeroesPool pressedHeroImage = pickAndBansHeroes[numberOfPressedImageOfPicksAndBansHeroes - 1];

        heroesCountersTask = new HeroesCountersTask(this);

        if (pressedHeroImage != null) {
            numberOfRemainingPickUpdateProcesses += 1;
            clearHero(view, pressedHeroImage);
        } else {
            startActivityForSelectHero();
        }
    }

    private void clearHero(View view, HeroesPool pressedHeroImage) {
        ImageView currentImage = (ImageView) view;
        currentImage.setImageResource(R.drawable.hero_frame);
        pickAndBansHeroes[numberOfPressedImageOfPicksAndBansHeroes - 1] = null;

        if (numberOfPressedImageOfPicksAndBansHeroes >= 1 && numberOfPressedImageOfPicksAndBansHeroes <= 5) {
            heroesCounters.getAllyHeroes().remove(pressedHeroImage);
        } else if (numberOfPressedImageOfPicksAndBansHeroes >= 12 && numberOfPressedImageOfPicksAndBansHeroes <= 16) {
            heroesCounters.getEnemyHeroes().remove(pressedHeroImage);
        } else {
            heroesCounters.getBanHeroes().remove(pressedHeroImage);
        }

        heroesCounters.getHeroesInitialization().addDeletedHero(pressedHeroImage);

        heroesCountersTask.setHeroesCounters(new HeroesCounters(heroesCounters));
        heroesCountersTask.execute();
    }

    private void startActivityForSelectHero() {
        Intent intent = new Intent(this, HeroSelectionActivity.class);

        intent.putExtra("HeroesCounters", heroesCounters);

        if (numberOfPressedImageOfPicksAndBansHeroes >= 1 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 5) {
            intent.putExtra("AllyPickOrAllyBanOrEnemyPickAndBan", 0);

        } else if (numberOfPressedImageOfPicksAndBansHeroes >= 6 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 11) {
            intent.putExtra("AllyPickOrAllyBanOrEnemyPickAndBan", 1);

        } else if (numberOfPressedImageOfPicksAndBansHeroes >= 12 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 22) {
            intent.putExtra("AllyPickOrAllyBanOrEnemyPickAndBan", 2);
        }

        startActivityForResult(intent, 1);
    }

    public void selectHeroFromRecommendedPicks(View view) {
        String imageViewTagString = (String) view.getTag();
        int imageViewTagIntForPick = Integer.parseInt(imageViewTagString);
        HeroesPool currentHero =
                recommendedHeroesPicks[(imageViewTagIntForPick - 1) / NUMBER_OF_HEROES_FOR_EVERY_POS]
                        [(imageViewTagIntForPick - 1) % NUMBER_OF_HEROES_FOR_EVERY_POS];
        if (currentHero != null) {
            numberOfRemainingPickUpdateProcesses += 1;

            ImageView pressedImage = (ImageView) view;
            Drawable pressedImageDrawable = pressedImage.getDrawable();
            pressedImage.setImageResource(R.drawable.hero_frame);

            heroesCounters.getHeroesInitialization().deleteHero(currentHero);
            heroesCountersTask = new HeroesCountersTask(this);
            ImageView currentImage = addHeroToAllyPicks(currentHero);
            currentImage.setImageDrawable(pressedImageDrawable);
        }
    }

    public void selectHeroFromRecommendedBans(View view) {
        String imageViewTagString = (String) view.getTag();
        int imageViewTagIntForPick = Integer.parseInt(imageViewTagString);
        HeroesPool currentHero =
                recommendedHeroesBans[(imageViewTagIntForPick - 1) / NUMBER_OF_HEROES_FOR_EVERY_POS]
                        [(imageViewTagIntForPick - 1) % NUMBER_OF_HEROES_FOR_EVERY_POS];
        if (currentHero != null) {
            numberOfRemainingPickUpdateProcesses += 1;

            ImageView pressedImage = (ImageView) view;
            Drawable pressedImageDrawable = pressedImage.getDrawable();
            pressedImage.setImageResource(R.drawable.hero_frame);

            heroesCounters.getHeroesInitialization().deleteHero(currentHero);
            heroesCountersTask = new HeroesCountersTask(this);
            ImageView currentImage = addHeroToAllyBans(currentHero);
            currentImage.setImageDrawable(pressedImageDrawable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                numberOfRemainingPickUpdateProcesses += 1;
                addHeroToPicksAndBans(data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addHeroToPicksAndBans(Intent data) {
        int imageResource = data.getIntExtra("ImageResource", 0);
        String heroName = data.getStringExtra("HeroName");
        HeroesPool currentHero = HeroesPool.valueOf(Hero.toHeroesPoolHeroName(heroName));

        heroesCounters.getHeroesInitialization().deleteHero(currentHero);

        ImageView currentImage;
        if (numberOfPressedImageOfPicksAndBansHeroes >= 1 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 5) {

            currentImage = addHeroToAllyPicks(currentHero);
        } else if (numberOfPressedImageOfPicksAndBansHeroes >= 6 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 11) {

            currentImage = addHeroToAllyBans(currentHero);
        } else if (numberOfPressedImageOfPicksAndBansHeroes >= 12 &&
                numberOfPressedImageOfPicksAndBansHeroes <= 16) {

            currentImage = addHeroToEnemyPicks(currentHero);
        } else {
            currentImage = addHeroToEnemyBans(currentHero);
        }

        currentImage.setImageResource(imageResource);
    }

    private ImageView addHeroToAllyPicks(HeroesPool currentHero) {
        ImageView currentImage;

        if (pickAndBansHeroes[0] == null) {
            currentImage = findViewById(R.id.imageAllyFirstPick);
            pickAndBansHeroes[0] = currentHero;
        } else if (pickAndBansHeroes[1] == null) {
            currentImage = findViewById(R.id.imageAllySecondPick);
            pickAndBansHeroes[1] = currentHero;
        } else if (pickAndBansHeroes[2] == null) {
            currentImage = findViewById(R.id.imageAllyThirdPick);
            pickAndBansHeroes[2] = currentHero;
        } else if (pickAndBansHeroes[3] == null) {
            currentImage = findViewById(R.id.imageAllyFourthPick);
            pickAndBansHeroes[3] = currentHero;
        } else {
            currentImage = findViewById(R.id.imageAllyFifthPick);
            pickAndBansHeroes[4] = currentHero;
        }

        heroesCounters.getAllyHeroes().add(currentHero);
        heroesCountersTask.setHeroesCounters(new HeroesCounters(heroesCounters));
        heroesCountersTask.execute();

        return currentImage;
    }

    private ImageView addHeroToAllyBans(HeroesPool currentHero) {
        ImageView currentImage;

        if (pickAndBansHeroes[5] == null) {
            currentImage = findViewById(R.id.imageAllyFirstBan);
            pickAndBansHeroes[5] = currentHero;
        } else if (pickAndBansHeroes[6] == null) {
            currentImage = findViewById(R.id.imageAllySecondBan);
            pickAndBansHeroes[6] = currentHero;
        } else if (pickAndBansHeroes[7] == null) {
            currentImage = findViewById(R.id.imageAllyThirdBan);
            pickAndBansHeroes[7] = currentHero;
        } else if (pickAndBansHeroes[8] == null) {
            currentImage = findViewById(R.id.imageAllyFourthBan);
            pickAndBansHeroes[8] = currentHero;
        } else if (pickAndBansHeroes[9] == null) {
            currentImage = findViewById(R.id.imageAllyFifthBan);
            pickAndBansHeroes[9] = currentHero;
        } else {
            currentImage = findViewById(R.id.imageAllySixthBan);
            pickAndBansHeroes[10] = currentHero;
        }

        heroesCounters.getBanHeroes().add(currentHero);

        HeroesCounters heroesCountersClone = new HeroesCounters(heroesCounters);
        heroesCountersClone.deleteBanHeroFromLists(currentHero);
        heroesCountersProcessFinish(heroesCountersClone);

        return currentImage;
    }

    private ImageView addHeroToEnemyPicks(HeroesPool currentHero) {
        ImageView currentImage;

        if (pickAndBansHeroes[11] == null) {
            currentImage = findViewById(R.id.imageEnemyFirstPick);
            pickAndBansHeroes[11] = currentHero;
        } else if (pickAndBansHeroes[12] == null) {
            currentImage = findViewById(R.id.imageEnemySecondPick);
            pickAndBansHeroes[12] = currentHero;
        } else if (pickAndBansHeroes[13] == null) {
            currentImage = findViewById(R.id.imageEnemyThirdPick);
            pickAndBansHeroes[13] = currentHero;
        } else if (pickAndBansHeroes[14] == null) {
            currentImage = findViewById(R.id.imageEnemyFourthPick);
            pickAndBansHeroes[14] = currentHero;
        } else {
            currentImage = findViewById(R.id.imageEnemyFifthPick);
            pickAndBansHeroes[15] = currentHero;
        }

        heroesCounters.getEnemyHeroes().add(currentHero);

        heroesCountersTask.setHeroesCounters(new HeroesCounters(heroesCounters));
        heroesCountersTask.execute();

        return currentImage;
    }

    private ImageView addHeroToEnemyBans(HeroesPool currentHero) {
        ImageView currentImage;

        if (pickAndBansHeroes[16] == null) {
            currentImage = findViewById(R.id.imageEnemyFirstBan);
            pickAndBansHeroes[16] = currentHero;
        } else if (pickAndBansHeroes[17] == null) {
            currentImage = findViewById(R.id.imageEnemySecondBan);
            pickAndBansHeroes[17] = currentHero;
        } else if (pickAndBansHeroes[18] == null) {
            currentImage = findViewById(R.id.imageEnemyThirdBan);
            pickAndBansHeroes[18] = currentHero;
        } else if (pickAndBansHeroes[19] == null) {
            currentImage = findViewById(R.id.imageEnemyFourthBan);
            pickAndBansHeroes[19] = currentHero;
        } else if (pickAndBansHeroes[20] == null) {
            currentImage = findViewById(R.id.imageEnemyFifthBan);
            pickAndBansHeroes[20] = currentHero;
        } else {
            currentImage = findViewById(R.id.imageEnemySixthBan);
            pickAndBansHeroes[21] = currentHero;
        }

        heroesCounters.getBanHeroes().add(currentHero);

        HeroesCounters heroesCountersClone = new HeroesCounters(heroesCounters);
        heroesCountersClone.deleteBanHeroFromLists(currentHero);
        heroesCountersProcessFinish(heroesCountersClone);

        return currentImage;
    }

    @Override
    public void heroesInitializationProcessFinish() {
        Toast.makeText(this, "Винрейты героев загружены", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void heroesCountersProcessFinish(HeroesCounters heroesCountersResult) {
        updateLocalHeroesCounters(heroesCountersResult);
        displayWinRateDiffBetweenPicks();
        displayRecommendedPicks();
        displayRecommendedBans();

        numberOfRemainingPickUpdateProcesses -= 1;
        if (numberOfRemainingPickUpdateProcesses > 0)
            Toast.makeText(this, "Пики обновлены (" + numberOfRemainingPickUpdateProcesses + ")",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Пики обновлены", Toast.LENGTH_SHORT).show();
    }

    private void updateLocalHeroesCounters(HeroesCounters heroesCountersResult) {
        heroesCounters.setAllyCountersByWinRateDiff(
                heroesCountersResult.getAllyCountersByWinRateDiff());
        heroesCounters.setEnemyCountersByWinRateDiff(
                heroesCountersResult.getEnemyCountersByWinRateDiff());
        heroesCounters.setWinRateDiffBetweenAllyAndEnemyPicks(
                heroesCountersResult.getWinRateDiffBetweenAllyAndEnemyPicks());
    }

    private void displayWinRateDiffBetweenPicks() {
        TextView picksWinRateDiffTextView = findViewById(R.id.picksWinRateDiff);

        if (!heroesCounters.getAllyHeroes().isEmpty() && !heroesCounters.getEnemyHeroes().isEmpty()) {
            if (heroesCounters.getWinRateDiffBetweenAllyAndEnemyPicks() > 0.0)
                picksWinRateDiffTextView.setText("+" + heroesCounters.getWinRateDiffBetweenAllyAndEnemyPicks() + "%");
            else
                picksWinRateDiffTextView.setText(heroesCounters.getWinRateDiffBetweenAllyAndEnemyPicks() + "%");
        } else {
            picksWinRateDiffTextView.setText("");
        }
    }

    private void displayRecommendedPicks() {
        if (heroesCounters.getEnemyHeroes().isEmpty()) {
            for (Integer pos = 1; pos <= NUMBER_OF_POSITIONS; pos++) {
                for (int i = 1; i <= NUMBER_OF_HEROES_FOR_EVERY_POS; i++) {
                    ImageView currentImage;

                    if (pos.toString().equals("1")) {
                        currentImage = getImageForFirstPosRecommendedPicks(i);
                    } else if (pos.toString().equals("2")) {
                        currentImage = getImageForSecondPosRecommendedPicks(i);
                    } else if (pos.toString().equals("3")) {
                        currentImage = getImageForThirdPosRecommendedPicks(i);
                    } else if (pos.toString().equals("4")) {
                        currentImage = getImageForFourthPosRecommendedPicks(i);
                    } else {
                        currentImage = getImageForFifthPosRecommendedPicks(i);
                    }

                    currentImage.setImageResource(R.drawable.hero_frame);
                    recommendedHeroesPicks[pos - 1][i - 1] = null;
                }
            }
        } else {
            for (Integer pos = 1; pos <= NUMBER_OF_POSITIONS; pos++) {
                int currentNumberOfHeroesForOnePos = 1;

                for (Hero h : heroesCounters.getEnemyCountersByWinRateDiff()) {
                    if (currentNumberOfHeroesForOnePos <= NUMBER_OF_HEROES_FOR_EVERY_POS) {
                        String heroesPoolHeroName = Hero.toHeroesPoolHeroName(h.getName());

                        try {
                            boolean isHeroHasPos = settings.getPersonalHeroesPool() ?
                                    PersonalHeroesPool.valueOf(heroesPoolHeroName).pos.contains(pos.toString()) :
                                    HeroesPool.valueOf(heroesPoolHeroName).pos.contains(pos.toString());

                            if (isHeroHasPos) {
                                ImageView currentImage;

                                if (pos.toString().equals("1")) {
                                    currentImage = getImageForFirstPosRecommendedPicks(currentNumberOfHeroesForOnePos);
                                } else if (pos.toString().equals("2")) {
                                    currentImage = getImageForSecondPosRecommendedPicks(currentNumberOfHeroesForOnePos);
                                } else if (pos.toString().equals("3")) {
                                    currentImage = getImageForThirdPosRecommendedPicks(currentNumberOfHeroesForOnePos);
                                } else if (pos.toString().equals("4")) {
                                    currentImage = getImageForFourthPosRecommendedPicks(currentNumberOfHeroesForOnePos);
                                } else {
                                    currentImage = getImageForFifthPosRecommendedPicks(currentNumberOfHeroesForOnePos);
                                }

                                if (h.getWinRateDiff() >= 0.0) {
                                    currentImage.setImageResource(h.getImage());
                                    recommendedHeroesPicks[pos - 1][currentNumberOfHeroesForOnePos - 1] =
                                            HeroesPool.valueOf(heroesPoolHeroName);
                                } else {
                                    currentImage.setImageResource(R.drawable.hero_frame);
                                    recommendedHeroesPicks[pos - 1][currentNumberOfHeroesForOnePos - 1] = null;
                                }

                                currentNumberOfHeroesForOnePos++;
                            }
                        } catch (IllegalArgumentException e) {
                            //
                        }
                    } else
                        break;
                }
            }
        }
    }

    private void displayRecommendedBans() {
        if (heroesCounters.getAllyHeroes().isEmpty()) {
            for (Integer pos = 1; pos <= NUMBER_OF_POSITIONS; pos++) {
                for (int i = 1; i <= NUMBER_OF_HEROES_FOR_EVERY_POS; i++) {
                    ImageView currentImage;

                    if (pos.toString().equals("1")) {
                        currentImage = getImageForFirstPosRecommendedBans(i);
                    } else if (pos.toString().equals("2")) {
                        currentImage = getImageForSecondPosRecommendedBans(i);
                    } else if (pos.toString().equals("3")) {
                        currentImage = getImageForThirdPosRecommendedBans(i);
                    } else if (pos.toString().equals("4")) {
                        currentImage = getImageForFourthPosRecommendedBans(i);
                    } else {
                        currentImage = getImageForFifthPosRecommendedBans(i);
                    }

                    currentImage.setImageResource(R.drawable.hero_frame);
                    recommendedHeroesBans[pos - 1][i - 1] = null;
                }
            }
        } else {
            for (Integer pos = 1; pos <= NUMBER_OF_POSITIONS; pos++) {
                int currentNumberOfHeroesForOnePos = 1;

                for (Hero h : heroesCounters.getAllyCountersByWinRateDiff()) {
                    if (currentNumberOfHeroesForOnePos <= NUMBER_OF_HEROES_FOR_EVERY_POS) {
                        String heroesPoolHeroName = Hero.toHeroesPoolHeroName(h.getName());

                        if (HeroesPool.valueOf(heroesPoolHeroName).pos.contains(pos.toString())) {
                            ImageView currentImage;

                            if (pos.toString().equals("1")) {
                                currentImage = getImageForFirstPosRecommendedBans(currentNumberOfHeroesForOnePos);
                            } else if (pos.toString().equals("2")) {
                                currentImage = getImageForSecondPosRecommendedBans(currentNumberOfHeroesForOnePos);
                            } else if (pos.toString().equals("3")) {
                                currentImage = getImageForThirdPosRecommendedBans(currentNumberOfHeroesForOnePos);
                            } else if (pos.toString().equals("4")) {
                                currentImage = getImageForFourthPosRecommendedBans(currentNumberOfHeroesForOnePos);
                            } else {
                                currentImage = getImageForFifthPosRecommendedBans(currentNumberOfHeroesForOnePos);
                            }

                            if (h.getWinRateDiff() >= 0.0) {
                                currentImage.setImageResource(h.getImage());
                                recommendedHeroesBans[pos - 1][currentNumberOfHeroesForOnePos - 1] =
                                        HeroesPool.valueOf(heroesPoolHeroName);
                            } else {
                                currentImage.setImageResource(R.drawable.hero_frame);
                                recommendedHeroesBans[pos - 1][currentNumberOfHeroesForOnePos - 1] = null;
                            }

                            currentNumberOfHeroesForOnePos++;
                        }
                    } else
                        break;
                }
            }
        }
    }

    private ImageView getImageForFirstPosRecommendedPicks(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFirstPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFirstPosPickHero);
        } else {
            return findViewById(R.id.tenthFirstPosPickHero);
        }
    }

    private ImageView getImageForSecondPosRecommendedPicks(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthSecondPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthSecondPosPickHero);
        } else {
            return findViewById(R.id.tenthSecondPosPickHero);
        }
    }

    private ImageView getImageForThirdPosRecommendedPicks(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthThirdPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthThirdPosPickHero);
        } else {
            return findViewById(R.id.tenthThirdPosPickHero);
        }
    }

    private ImageView getImageForFourthPosRecommendedPicks(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFourthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFourthPosPickHero);
        } else {
            return findViewById(R.id.tenthFourthPosPickHero);
        }
    }

    private ImageView getImageForFifthPosRecommendedPicks(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFifthPosPickHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFifthPosPickHero);
        } else {
            return findViewById(R.id.tenthFifthPosPickHero);
        }
    }

    private ImageView getImageForFirstPosRecommendedBans(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFirstPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFirstPosBanHero);
        } else {
            return findViewById(R.id.tenthFirstPosBanHero);
        }
    }

    private ImageView getImageForSecondPosRecommendedBans(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthSecondPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthSecondPosBanHero);
        } else {
            return findViewById(R.id.tenthSecondPosBanHero);
        }
    }

    private ImageView getImageForThirdPosRecommendedBans(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthThirdPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthThirdPosBanHero);
        } else {
            return findViewById(R.id.tenthThirdPosBanHero);
        }
    }

    private ImageView getImageForFourthPosRecommendedBans(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFourthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFourthPosBanHero);
        } else {
            return findViewById(R.id.tenthFourthPosBanHero);
        }
    }

    private ImageView getImageForFifthPosRecommendedBans(int currentNumberOfHeroesForOnePos) {
        if (currentNumberOfHeroesForOnePos == 1) {
            return findViewById(R.id.firstFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 2) {
            return findViewById(R.id.secondFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 3) {
            return findViewById(R.id.thirdFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 4) {
            return findViewById(R.id.fourthFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 5) {
            return findViewById(R.id.fifthFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 6) {
            return findViewById(R.id.sixthFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 7) {
            return findViewById(R.id.seventhFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 8) {
            return findViewById(R.id.eighthFifthPosBanHero);
        } else if (currentNumberOfHeroesForOnePos == 9) {
            return findViewById(R.id.ninthFifthPosBanHero);
        } else {
            return findViewById(R.id.tenthFifthPosBanHero);
        }
    }
}