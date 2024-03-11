package com.example.androidgametutorial.environments;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.Buildings;
import com.example.androidgametutorial.helpers.GameConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapManager {

  private GameMap currentMap, outsideMap, insideMap;
  private float cameraX, cameraY;

  public MapManager() {
    initTestMap();
  }

  public void setCameraValues(float cameraX, float cameraY) {
    this.cameraX = cameraX;
    this.cameraY = cameraY;
  }

  private void initTestMap() {
    int[][] outsideArray = {
            {454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132},
            {454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132},
            {454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
            {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
            {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
            {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
            {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
            {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
            {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
            {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
            {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
            {454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132},
            {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
            {454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132},
            {454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132}
    };

    int[][] insideArray = {
        {0, 1, 1, 1, 2},
        {22, 23, 23, 23, 24},
        {22, 23, 23, 23, 24},
        {22, 23, 23, 23, 24},
        {44, 45, 45, 45, 46}
    };

    ArrayList<Building> buildingArrayList = new ArrayList<>();
    buildingArrayList.add(new Building(new PointF(200, 200), Buildings.HOUSE_ONE));

    insideMap = new GameMap(insideArray, Floor.INSIDE, null);
    outsideMap = new GameMap(outsideArray, Floor.OUTSIDE, buildingArrayList);
    currentMap = insideMap;
  }
  public void drawBuildings(Canvas c) {
    if (currentMap.getBuildingArrayList() != null)
      for(Building b: currentMap.getBuildingArrayList()) {
        c.drawBitmap(b.getBuildingType().getHouseImg(), b.getPos().x + cameraX, b.getPos().y + cameraY, null);
      }
  }

  public void drawTiles(Canvas c) {
    for (int j=0; j<currentMap.getArrayHeight(); j++) {
      for (int i=0; i<currentMap.getArrayWidth(); i++) {
        c.drawBitmap(currentMap.getFloorType().getSprite(currentMap.getSpriteId(i,j)), i*GameConstants.Sprite.SIZE+cameraX, j*GameConstants.Sprite.SIZE+cameraY, null);
      }
    }
  }

   public void draw(Canvas c) {
    drawTiles(c);
    drawBuildings(c);
   }


  public boolean canMoveHere(float x, float y) {
    if (x<0 || y<0)
      return false;
    if (x >= getMaxWidthCurrentMap() || y >= getMaxHeightCurrentMap())
      return false;
    return true;
  }

  public int getMaxWidthCurrentMap() {
    return currentMap.getArrayWidth() * GameConstants.Sprite.SIZE;
  }

  public int getMaxHeightCurrentMap() {
    return currentMap.getArrayHeight() * GameConstants.Sprite.SIZE;
  }
}
