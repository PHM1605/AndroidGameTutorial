package com.example.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import com.example.androidgametutorial.MainActivity;
import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {
  PLAYER(R.drawable.player_spritesheet),
  SKELETON(R.drawable.skeleton_spritesheet);

  private Bitmap spriteSheet;
  private final Bitmap[][] sprites = new Bitmap[7][4];

  GameCharacters(int resId) {
    options.inScaled = false;
    spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for(int j=0; j<sprites.length; j++) {
      for(int i=0; i<sprites[j].length; i++) {
        sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprite.DEFAULT_SIZE*i, GameConstants.Sprite.DEFAULT_SIZE*j, GameConstants.Sprite.DEFAULT_SIZE, GameConstants.Sprite.DEFAULT_SIZE));
      }
    }
  }

  public Bitmap getSpriteSheet() {
    return spriteSheet;
  }

  public Bitmap getSprite(int yPos, int xPos) {
    return sprites[yPos][xPos];
  }
}
