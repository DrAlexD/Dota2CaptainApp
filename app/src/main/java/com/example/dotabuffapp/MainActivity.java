package com.example.dotabuffapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    int imageViewId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(this, getSupportFragmentManager()));
        pager.setCurrentItem(1);
    }

    public void selectHeroOrClear(View view) {
        //TODO пиздец с очисткой
        imageViewId = view.getId();
        System.out.println("ТЕКУЩИЙ " + R.drawable.ramka);
        System.out.println("РАМКА " + imageViewId);
        if (imageViewId == R.drawable.ramka) {
            Intent intent = new Intent(this, HeroSelectionActivity.class);
            startActivityForResult(intent, 1);
        } else {
            imageViewId = R.drawable.ramka;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int imageRes = data.getIntExtra("ImageId", 0);
                ImageView currentImage = (ImageView) findViewById(imageViewId); // TODO зменить на добавление в первую свободную ячейку
                currentImage.setImageResource(imageRes);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
