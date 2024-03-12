package com.example.androidgametutorial.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.androidgametutorial.main.MainActivity;
import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;

public enum Tiles implements BitmapMethods {
  // width/height is in number of tiles in the png file
  OUTSIDE(R.drawable.titeset_floor, 22, 26),
  INSIDE(R.drawable.floor_inside, 22, 22);

  private Bitmap[] sprites;

  Tiles(int resId, int tilesInWidth, int tilesInHeight) {
    options.inScaled = false;
    sprites = new Bitmap[tilesInHeight*tilesInWidth];
    Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for(int j=0; j < tilesInHeight; j++) {
      for(int i=0; i < tilesInWidth; i++) {
        int index = j*tilesInWidth+i;
        sprites[index] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprite.DEFAULT_SIZE*i, GameConstants.Sprite.DEFAULT_SIZE*j, GameConstants.Sprite.DEFAULT_SIZE, GameConstants.Sprite.DEFAULT_SIZE));
      }
    }
  }

  public Bitmap getSprite(int id) {
    return sprites[id];
  }
}
