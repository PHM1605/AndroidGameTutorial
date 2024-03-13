package com.example.androidgametutorial.helpers;

import android.graphics.Point;
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

  public static boolean CanWalkHere (RectF hitbox, float deltaX, float deltaY, GameMap gameMap) {
    if (hitbox.left + deltaX < 0 || hitbox.top + deltaY < 0) {
      return false;
    }
    if (hitbox.right + deltaX >= gameMap.getMapWidth()) {
      return false;
    }
    if (hitbox.bottom + deltaY >= gameMap.getMapHeight())
      return false;
    Point[] tileCoords = GetTileCoords(hitbox, deltaX, deltaY);
    int[] tileIds = GetTileIds(tileCoords, gameMap);
    return IsTilesWalkable(tileIds, gameMap.getFloorType());
  }

  private static int[] GetTileIds(Point[] tileCoords, GameMap gameMap) {
    int[] tileIds = new int[4];
    for (int i=0; i<tileCoords.length; i++) {
      tileIds[i] = gameMap.getSpriteId(tileCoords[i].x, tileCoords[i].y);
    }
    return tileIds;
  }


  private static Point[] GetTileCoords(RectF hitbox, float deltaX, float deltaY) {
    Point[] tileCoords = new Point[4];
    int left = (int)((hitbox.left + deltaX) / GameConstants.Sprite.SIZE);
    int right = (int)((hitbox.right + deltaX) / GameConstants.Sprite.SIZE);
    int top = (int)((hitbox.top + deltaY) / GameConstants.Sprite.SIZE);
    int bottom = (int)((hitbox.bottom + deltaY) / GameConstants.Sprite.SIZE);
    tileCoords[0] = new Point(left, top);
    tileCoords[1] = new Point(right, top);
    tileCoords[2] = new Point(left, bottom);
    tileCoords[3] = new Point(right, bottom);
    return tileCoords;
  }

  public static boolean IsTileWalkable(int tileId, Tiles tilesType) {
    if (tilesType == Tiles.INSIDE) {
      return (tileId == 394 || tileId < 374);
    }
    return true;
  }

  public static boolean IsTilesWalkable(int[] tileIds, Tiles tilesType) {
    for(int i: tileIds) {
      if (!(IsTileWalkable(i, tilesType)))
        return false;
    }
    return true;
  }

  public static float MoveNextToTileUpDown(RectF hitbox, float cameraY, float deltaY) {
    int currentTile;
    int playerPosY; // player position on screen
    float cameraYReturn; // tiny gap between top and the "wall"

    // Player moving up
    if (deltaY > 0) {
      playerPosY = (int) (hitbox.top - cameraY);
      currentTile = playerPosY / GameConstants.Sprite.SIZE;
      cameraYReturn = hitbox.top - (currentTile * GameConstants.Sprite.SIZE);
    } else {
      playerPosY = (int) (hitbox.bottom - cameraY);
      currentTile = playerPosY / GameConstants.Sprite.SIZE;
      // bottom+1 to make it just over the bottom border
      cameraYReturn = (hitbox.bottom+1) - (currentTile+1)*GameConstants.Sprite.SIZE;
    }
    // when move up: -epsilon; when move down: + epsilon
    return cameraYReturn;
  }
}
