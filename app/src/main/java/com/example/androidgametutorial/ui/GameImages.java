package com.example.androidgametutorial.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;
import com.example.androidgametutorial.main.MainActivity;

public enum GameImages implements BitmapMethods {
  MAINMENU_MENUBG(R.drawable.mainmenu_menubackground);

  private final Bitmap image;

  GameImages(int resID) {
    options.inScaled = false;
    image = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resID, options);
  }

  public Bitmap getImage() {
    return image;
  }
}
