package com.example.androidgametutorial.entities;

import static com.example.androidgametutorial.helpers.GameConstants.Sprite.SCALE_MULTIPLIER;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.androidgametutorial.R;
import com.example.androidgametutorial.helpers.interfaces.BitmapMethods;
import com.example.androidgametutorial.main.MainActivity;

public enum Buildings implements BitmapMethods {
  HOUSE_ONE(0, 0, 64, 48, 23, 38, 20, 52);

  final Bitmap houseImg;
  PointF doorwayPoint;
  int hitboxRoof, hitboxFloor, hitboxHeight, hitboxWidth;

  Buildings(int x, int y, int width, int height, int doorwayX, int doorwayY, int hitboxRoof, int hitboxFloor) {
    options.inScaled = false;
    Bitmap atlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.buildings_atlas, options);
    houseImg = getScaledBitmap(Bitmap.createBitmap(atlas, x, y, width, height));
    doorwayPoint = new PointF(doorwayX * SCALE_MULTIPLIER, doorwayY * SCALE_MULTIPLIER);
    this.hitboxRoof = hitboxRoof;
    this.hitboxFloor = hitboxFloor;
    this.hitboxHeight = (hitboxFloor - hitboxRoof) * SCALE_MULTIPLIER;
    this.hitboxWidth = width * SCALE_MULTIPLIER;
  }

  public PointF getDoorwayPoint() {
    return doorwayPoint;
  }

  public Bitmap getHouseImg() {
    return houseImg;
  }

  public int getHitboxRoof() {
    return hitboxRoof;
  }
}
