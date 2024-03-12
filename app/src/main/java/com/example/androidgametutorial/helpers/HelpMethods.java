package com.example.androidgametutorial.helpers;

import android.graphics.PointF;
import android.graphics.RectF;
import android.location.GnssAntennaInfo;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.enemies.Skeleton;
import com.example.androidgametutorial.environments.Doorway;
import com.example.androidgametutorial.environments.GameMap;
import com.example.androidgametutorial.environments.Tiles;

import java.util.ArrayList;

public class HelpMethods {
//  public static void AddDoorwayToGameMap(GameMap gameMapLocatedIn, GameMap gameMapTarget, int buildingIndex) {
//    Building building = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex);
//    float houseX = building.getPos().x;
//    float houseY = building.getPos().y;
//    RectF hitbox = building.getBuildingType().getHitboxDoorway();
//    Doorway doorway = new Doorway(new RectF(
//        hitbox.left+houseX, hitbox.top+houseY, hitbox.right+houseX, hitbox.bottom+houseY), gameMapTarget);
//    gameMapLocatedIn.addDoorway(doorway);
//  }

  // Use this in case we have a Building from a GameMap
  public static RectF CreateHitboxForDoorway(GameMap gameMapLocatedIn, int buildingIndex) {
    Building building = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex);
    float houseX = building.getPos().x;
    float houseY = building.getPos().y;
    RectF hitbox = building.getBuildingType().getHitboxDoorway();
    return new RectF(hitbox.left+houseX, hitbox.top+houseY, hitbox.right+houseX, hitbox.bottom+houseY);
  }

  // Use this in case we have a Tile pattern only
  public static RectF CreateHitboxForDoorway(int xTile, int yTile) {
    float x = xTile * GameConstants.Sprite.SIZE;
    float y = yTile * GameConstants.Sprite.SIZE;
    return new RectF(x, y, x+GameConstants.Sprite.SIZE, y+GameConstants.Sprite.SIZE);
  }

  public static void ConnectTwoDoorways(GameMap gameMapOne, RectF hitboxOne, GameMap gameMapTwo, RectF hitboxTwo) {
    Doorway doorwayOne = new Doorway(hitboxOne, gameMapOne);
    Doorway doorwayTwo = new Doorway(hitboxTwo, gameMapTwo);
    doorwayOne.connectDoorway(doorwayTwo);
    doorwayTwo.connectDoorway(doorwayOne);
  }

  public static ArrayList<Skeleton> GetSkeletonsRandomized(int amount, int[][] gameMapArray) {
    // Minus 1 to prevent spawning Skeleton (top-left) too close to border
    int width = (gameMapArray[0].length-1) * GameConstants.Sprite.SIZE;
    int height = (gameMapArray.length-1) * GameConstants.Sprite.SIZE;
    ArrayList<Skeleton> skeletonArrayList = new ArrayList<>();
    for (int i=0; i<amount; i++) {
      float x = (float) Math.random() * width;
      float y = (float) Math.random() * height;
      skeletonArrayList.add(new Skeleton(new PointF(x, y)));
    }
    return skeletonArrayList;
  }

  public static boolean CanWalkHere(float x, float y, GameMap gameMap) {
    if (x<0 || y<0)
      return false;
    if(x>=gameMap.getMapWidth() || y>=gameMap.getMapHeight())
      return false;
    int tileX = (int) (x/GameConstants.Sprite.SIZE);
    int tileY = (int) (y/GameConstants.Sprite.SIZE);
    int tileId = gameMap.getSpriteId(tileX, tileY);
    return isTileWalkable(tileId, gameMap.getFloorType());
  }

  public static boolean isTileWalkable(int tileId, Tiles tilesType) {
    if (tilesType == Tiles.INSIDE) {
      return (tileId == 394 || tileId < 374);
    }
    return true;
  }
}