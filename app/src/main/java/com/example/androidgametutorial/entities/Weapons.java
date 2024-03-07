package com.example.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;
import com.example.androidgametutorial.main.MainActivity;

public enum Weapons implements BitmapMethods {
  BIG_SWORD(R.drawable.big_sword);

  final Bitmap weaponImg;

  Weapons(int resId) {
    options.inScaled = false;
    weaponImg = getScaledBitmap(BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options));
  }

  public Bitmap getWeaponImg() {
    return weaponImg;
  }

  public int getWidth() {
    return weaponImg.getWidth();
  }

  public int getHeight() {
    return weaponImg.getHeight();
  }
}
