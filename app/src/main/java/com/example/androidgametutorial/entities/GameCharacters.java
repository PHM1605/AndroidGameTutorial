package com.example.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import com.example.androidgametutorial.MainActivity;
import com.example.androidgametutorial.R;

public enum GameCharacters {
  PLAYER(R.drawable.player_spritesheet),
  SKELETON(R.drawable.skeleton_spritesheet);

  private Bitmap spriteSheet;
  private final Bitmap[][] sprites = new Bitmap[7][4];
  private BitmapFactory.Options options;

  GameCharacters(int resId) {
    options = new BitmapFactory.Options();
    options.inScaled = false;
    spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for(int j=0; j<sprites.length; j++) {
      for(int i=0; i<sprites[j].length; i++) {
        sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, 16*i, 16*j, 16, 16));
      }
    }
  }

  public Bitmap getSpriteSheet() {
    return spriteSheet;
  }

  public Bitmap getSprite(int yPos, int xPos) {
    return sprites[yPos][xPos];
  }

  private Bitmap getScaledBitmap(Bitmap bitmap) {
    return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*6, bitmap.getHeight()*6, false);
  }
}
