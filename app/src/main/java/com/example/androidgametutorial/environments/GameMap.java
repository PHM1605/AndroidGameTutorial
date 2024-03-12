package com.example.androidgametutorial.environments;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.enemies.Skeleton;
import com.example.androidgametutorial.helpers.GameConstants;

import java.util.ArrayList;

public class GameMap {
  private int[][] spriteIds;
  private Tiles tilesType;
  private ArrayList<Building> buildingArrayList;
  private ArrayList<Doorway> doorwayArrayList;
  private ArrayList<Skeleton> skeletonArrayList;

  public GameMap(int[][] spriteIds, Tiles tilesType, ArrayList<Building> buildingArrayList, ArrayList<Skeleton> skeletonArrayList) {
    this.spriteIds = spriteIds;
    this.tilesType = tilesType;
    this.buildingArrayList = buildingArrayList;
    this.doorwayArrayList = new ArrayList<>();
    this.skeletonArrayList = skeletonArrayList;
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
