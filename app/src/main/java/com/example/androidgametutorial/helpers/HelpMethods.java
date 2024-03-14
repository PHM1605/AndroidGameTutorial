package com.example.androidgametutorial.helpers;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.GameObject;
import com.example.androidgametutorial.entities.enemies.Skeleton;
import com.example.androidgametutorial.environments.Doorway;
import com.example.androidgametutorial.environments.GameMap;
import com.example.androidgametutorial.environments.Tiles;

import java.util.ArrayList;

public class HelpMethods {

  // Use this in case we have a Building from a GameMap
  public static PointF CreatePointForDoorway(GameMap gameMapLocatedIn, int buildingIndex) {
    Building building = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex);
    float x = building.getPos().x;
    float y = building.getPos().y;
    PointF point = building.getBuildingType().getDoorwayPoint();
    return new PointF(point.x + x, point.y + y);
  }

  // Use this in case we have a Tile pattern only
  public static PointF CreatePointForDoorway(int xTile, int yTile) {
    float x = xTile * GameConstants.Sprite.SIZE+1;
    float y = yTile * GameConstants.Sprite.SIZE;
    return new PointF(x+1, y+1);
  }

  public static void ConnectTwoDoorways(GameMap gameMapOne, PointF pointOne, GameMap gameMapTwo, PointF pointTwo) {
    Doorway doorwayOne = new Doorway(pointOne, gameMapOne);
    Doorway doorwayTwo = new Doorway(pointTwo, gameMapTwo);
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

  public static boolean CanWalkHereUpDown (RectF hitbox, float currentCameraX, float deltaY, GameMap gameMap) {
    if (hitbox.top + deltaY < 0) {
      return false;
    }
    if (hitbox.bottom + deltaY >= gameMap.getMapHeight())
      return false;

    // collision with other GameObjects
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(hitbox.left + currentCameraX, hitbox.top + deltaY, hitbox.right + currentCameraX, hitbox.bottom + deltaY);
      for (GameObject go: gameMap.getGameObjectArrayList()) {
        if (RectF.intersects(go.getHitbox(), tempHitbox))
          return false;
      }
    }

    // collision with Buildings
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(
          hitbox.left + currentCameraX,
          hitbox.top + deltaY,
          hitbox.right + currentCameraX,
          hitbox.bottom + deltaY);
      for (Building b: gameMap.getBuildingArrayList()) {
        if (RectF.intersects(b.getHitbox(), tempHitbox))
          return false;
      }
    }

    Point[] tileCoords = GetTileCoords(hitbox, currentCameraX, deltaY);
    int[] tileIds = GetTileIds(tileCoords, gameMap);
    return IsTilesWalkable(tileIds, gameMap.getFloorType());
  }

  public static boolean CanWalkHereLeftRight (RectF hitbox, float deltaX, float currentCameraY, GameMap gameMap) {
    if (hitbox.left + deltaX < 0) {
      return false;
    }
    if (hitbox.right + deltaX >= gameMap.getMapWidth()) {
      return false;
    }

    // collision with other GameObjects
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(
          hitbox.left + deltaX,
          hitbox.top + currentCameraY,
          hitbox.right + deltaX,
          hitbox.bottom + currentCameraY);
      for (GameObject go: gameMap.getGameObjectArrayList()) {
        if (RectF.intersects(go.getHitbox(), tempHitbox))
          return false;
      }
    }

    // collision with Buildings
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(
          hitbox.left + deltaX,
          hitbox.top + currentCameraY,
          hitbox.right + deltaX,
          hitbox.bottom + currentCameraY);
      for (Building b: gameMap.getBuildingArrayList()) {
        if (RectF.intersects(b.getHitbox(), tempHitbox))
          return false;
      }
    }

    Point[] tileCoords = GetTileCoords(hitbox, deltaX, currentCameraY);
    int[] tileIds = GetTileIds(tileCoords, gameMap);
    return IsTilesWalkable(tileIds, gameMap.getFloorType());
  }

  // Bug potential: use this function to check moving up-down i.e. deltaX =0, deltaY=abc then returns false immediately
  // -> we need to split to CanWalkHereUpDown and CanWalkHereLeftRight
  public static boolean CanWalkHere (RectF hitbox, float deltaX, float deltaY, GameMap gameMap) {
    if (hitbox.left + deltaX < 0 || hitbox.top + deltaY < 0) {
      return false;
    }
    if (hitbox.right + deltaX >= gameMap.getMapWidth()) {
      return false;
    }
    if (hitbox.bottom + deltaY >= gameMap.getMapHeight())
      return false;

    // If we collide to any object
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(hitbox.left + deltaX, hitbox.top + deltaY, hitbox.right + deltaX, hitbox.bottom + deltaY);
      for (GameObject go: gameMap.getGameObjectArrayList()) {
        if (RectF.intersects(go.getHitbox(), tempHitbox))
          return false;
      }
    }

    // collision with Buildings
    if (gameMap.getGameObjectArrayList() != null) {
      RectF tempHitbox = new RectF(
          hitbox.left + deltaX,
          hitbox.top + deltaY,
          hitbox.right + deltaX,
          hitbox.bottom + deltaY);
      for (Building b: gameMap.getBuildingArrayList()) {
        if (RectF.intersects(b.getHitbox(), tempHitbox))
          return false;
      }
    }

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
    // cameraY: distance from top of Cam to top of Map
    int currentTile;
    int playerPosY; // player offset from where it should be
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
    return cameraYReturn;
  }

  public static float MoveNextToTileLeftRight(RectF hitbox, float cameraX, float deltaX) {
    // cameraY: distance from top of Cam to top of Map
    int currentTile;
    int playerPosX; // player offset from where it should be
    float cameraXReturn; // tiny gap between top and the "wall"

    // Player moving left
    if (deltaX > 0) {
      playerPosX = (int) (hitbox.left - cameraX);
      currentTile = playerPosX / GameConstants.Sprite.SIZE;
      cameraXReturn = hitbox.left - (currentTile * GameConstants.Sprite.SIZE);
    } else {
      playerPosX = (int) (hitbox.right - cameraX);
      currentTile = playerPosX / GameConstants.Sprite.SIZE;
      // bottom+1 to make it just over the bottom border
      cameraXReturn = (hitbox.right+1) - (currentTile+1)*GameConstants.Sprite.SIZE;
    }
    return cameraXReturn;
  }

}
