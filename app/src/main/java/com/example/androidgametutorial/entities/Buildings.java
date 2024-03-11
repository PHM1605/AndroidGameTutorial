package com.example.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;
import com.example.androidgametutorial.main.MainActivity;

public enum Buildings implements BitmapMethods {
  HOUSE_ONE(0, 0, 64, 48);
  final Bitmap houseImg;

  Buildings(int x, int y, int width, int height) {
    options.inScaled = false;
    Bitmap atlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.buildings_atlas, options);
    houseImg = getScaledBitmap(Bitmap.createBitmap(atlas, x, y, width, height));
  }

  public Bitmap getHouseImg() {
    return houseImg;
  }
}
