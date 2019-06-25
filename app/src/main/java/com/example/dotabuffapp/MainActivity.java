package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    HeroPicker heroPicks;
    HeroTier heroTier;
    boolean[] isNotFrame = new boolean[22];
    int imageViewTagInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(this, getSupportFragmentManager()));
        pager.setCurrentItem(1);

        heroTier = new HeroTier(getApplicationContext());
        heroPicks = new HeroPicker(getApplicationContext());

        heroTier.execute();
    }

    public void selectHeroOrClear(View view) {
        String imageViewTagString = (String) view.getTag();
        imageViewTagInt = Integer.parseInt(imageViewTagString);
        if (isNotFrame[imageViewTagInt]) {
            ImageView currentImage = (ImageView) view;
            currentImage.setImageResource(R.drawable.frame);
        } else {
            Intent intent = new Intent(this, HeroSelectionActivity.class);
            heroPicks.setTier(heroTier);
            intent.putExtra("Heroes", heroPicks);
            intent.putExtra("HeroesTier", heroTier);
            if (imageViewTagInt >= 1 && imageViewTagInt <= 5)
                intent.putExtra("PickOrBan", 0);
            else if (imageViewTagInt >= 6 && imageViewTagInt <= 11)
                intent.putExtra("PickOrBan", 1);
            else if (imageViewTagInt >= 12 && imageViewTagInt <= 22)
                intent.putExtra("PickOrBan", 2);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //System.out.println("КОД"+resultCode);
                Bundle arguments = getIntent().getExtras();
                int imageRes;
                String heroName;
                ImageView currentImage;
                if (arguments != null) {
                    imageRes = (int) arguments.get("ImageId");
                    heroName = (String) arguments.get("HeroName");
                    //System.out.println("ВНУТРИ"+imageRes);
                    if (imageViewTagInt >= 1 && imageViewTagInt <= 5) {
                        if (!isNotFrame[0]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFirstPick);
                        } else if (!isNotFrame[1]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllySecondPick);
                        } else if (!isNotFrame[2]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyThirdPick);
                        } else if (!isNotFrame[3]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFourthPick);
                        } else {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFifthPick);
                        }
                        //System.out.println("ИЧЕЙКА"+currentImage);
                        heroPicks.addAllyHero(Heroes.valueOf(heroName));
                        heroPicks.execute();
                    } else if (imageViewTagInt >= 6 && imageViewTagInt <= 11) {
                        if (!isNotFrame[5]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFirstBan);
                        } else if (!isNotFrame[6]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllySecondBan);
                        } else if (!isNotFrame[7]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyThirdBan);
                        } else if (!isNotFrame[8]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFourthBan);
                        } else if (!isNotFrame[9]) {
                            currentImage = (ImageView) findViewById(R.id.imageAllyFifthBan);
                        } else {
                            currentImage = (ImageView) findViewById(R.id.imageAllySixthBan);
                        }
                    } else if (imageViewTagInt >= 12 && imageViewTagInt <= 16) {
                        if (!isNotFrame[11]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFirstPick);
                        } else if (!isNotFrame[12]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemySecondPick);
                        } else if (!isNotFrame[13]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyThirdPick);
                        } else if (!isNotFrame[14]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFourthPick);
                        } else {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFifthPick);
                        }
                        heroPicks.addEnemyHero(Heroes.valueOf(heroName));
                        heroPicks.execute();
                    } else {
                        if (!isNotFrame[16]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFirstBan);
                        } else if (!isNotFrame[17]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemySecondBan);
                        } else if (!isNotFrame[18]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyThirdBan);
                        } else if (!isNotFrame[19]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFourthBan);
                        } else if (!isNotFrame[20]) {
                            currentImage = (ImageView) findViewById(R.id.imageEnemyFifthBan);
                        } else {
                            currentImage = (ImageView) findViewById(R.id.imageEnemySixthBan);
                        }
                    }
                    currentImage.setImageResource(imageRes);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
