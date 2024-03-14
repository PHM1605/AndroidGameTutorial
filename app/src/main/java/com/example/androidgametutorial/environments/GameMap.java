package com.example.androidgametutorial.environments;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.Entity;
import com.example.androidgametutorial.entities.GameObject;
import com.example.androidgametutorial.entities.enemies.Skeleton;
import com.example.androidgametutorial.helpers.GameConstants;

import java.util.ArrayList;

public class GameMap {
  private int[][] spriteIds;
  private Tiles tilesType;
  private ArrayList<Building> buildingArrayList;
  private ArrayList<Doorway> doorwayArrayList;
  private ArrayList<GameObject> gameObjectArrayList;
  private ArrayList<Skeleton> skeletonArrayList;

  public GameMap(int[][] spriteIds, Tiles tilesType, ArrayList<Building> buildingArrayList, ArrayList<GameObject> gameObjectArrayList, ArrayList<Skeleton> skeletonArrayList) {
    this.spriteIds = spriteIds;
    this.tilesType = tilesType;
    this.buildingArrayList = buildingArrayList;
    this.doorwayArrayList = new ArrayList<>();
    this.gameObjectArrayList = gameObjectArrayList;
    this.skeletonArrayList = skeletonArrayList;
  }

  public Entity[] getDrawableList() {
    Entity[] list = new Entity[getDrawableAmount()];
    int i = 0;
    if(buildingArrayList != null)
      for (Building b: buildingArrayList) {
        list[i++] = b;
      }
    if (skeletonArrayList != null)
      for (Skeleton s: skeletonArrayList) {
        list[i++] = s;
      }
    if (gameObjectArrayList != null)
      for (GameObject go: gameObjectArrayList) {
        list[i++] = go;
      }
    return list;
  }

  public int getDrawableAmount() {
    int amount = 0;
    if (buildingArrayList != null)
      amount += buildingArrayList.size();
    if (gameObjectArrayList != null)
      amount += gameObjectArrayList.size();
    if (skeletonArrayList != null)
      amount += skeletonArrayList.size();
    amount++; // Player
    return amount;
  }

  public void addDoorway(Doorway doorway) {
    this.doorwayArrayList.add(doorway);
  }

  public ArrayList<Doorway> getDoorwayArrayList() {
    return doorwayArrayList;
  }

  public ArrayList<Building> getBuildingArrayList() {
    return buildingArrayList;
  }

  public ArrayList<GameObject> getGameObjectArrayList() { return gameObjectArrayList; }

  public ArrayList<Skeleton> getSkeletonArrayList() {
    return skeletonArrayList;
  }
  
  public int getSpriteId(int xIndex, int yIndex) {
    return spriteIds[yIndex][xIndex];
  }

  public int getArrayWidth() {
    return spriteIds[0].length;
  }

  public int getArrayHeight() {
    return spriteIds.length;
  }

  public Tiles getFloorType() {
    return tilesType;
  }

  public int getMapWidth() {
    return getArrayWidth() * GameConstants.Sprite.SIZE;
  }

  public int getMapHeight() {
    return getArrayHeight() * GameConstants.Sprite.SIZE;
  }


}
