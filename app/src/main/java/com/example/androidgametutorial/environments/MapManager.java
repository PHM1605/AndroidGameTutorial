package com.example.androidgametutorial.environments;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.androidgametutorial.entities.Building;
import com.example.androidgametutorial.entities.Buildings;
import com.example.androidgametutorial.entities.GameObject;
import com.example.androidgametutorial.entities.GameObjects;
import com.example.androidgametutorial.gamestates.Playing;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.helpers.HelpMethods;
import com.example.androidgametutorial.main.MainActivity;

import java.util.ArrayList;

public class MapManager {

  private GameMap currentMap, outsideMap, insideMap;
  private float cameraX, cameraY;
  Playing playing;

  public MapManager(Playing playing) {
    this.playing = playing;
    initTestMap();
  }

  public void setCameraValues(float cameraX, float cameraY) {
    this.cameraX = cameraX;
    this.cameraY = cameraY;
  }

  public GameMap getCurrentMap() {
    return currentMap;
  }

  public void changeMap(Doorway doorwayTarget) {
    this.currentMap = doorwayTarget.getGameMapLocatedIn();
    float cX = MainActivity.GAME_WIDTH/2 - doorwayTarget.getPosOfDoorway().x;
    float cY = MainActivity.GAME_HEIGHT/2 - doorwayTarget.getPosOfDoorway().y;
    playing.setCameraValues(new PointF(cX, cY));
    cameraX = cX;
    cameraY = cY;
    playing.setDoorwayJustPassed(true);
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
        {374, 377, 377, 377, 377, 377, 378},
        {396, 0, 1, 1, 1, 2, 400},
        {396, 22, 23, 23, 23, 24, 400},
        {396, 22, 23, 23, 23, 24, 400},
        {396, 22, 23, 23, 23, 24, 400},
        {396, 44, 45, 45, 45, 46, 400},
        {462, 465, 463, 394, 464, 465, 466},
    };

    ArrayList<Building> buildingArrayList = new ArrayList<>();
    buildingArrayList.add(new Building(new PointF(200, 200), Buildings.HOUSE_ONE));
    ArrayList<GameObject> gameObjectArrayList = new ArrayList<>();
    gameObjectArrayList.add(new GameObject(new PointF(600, 200), GameObjects.PILLAR_YELLOW));
    gameObjectArrayList.add(new GameObject(new PointF(600, 400), GameObjects.STATUE_ANGRY_YELLOW));
    gameObjectArrayList.add(new GameObject(new PointF(1000, 400), GameObjects.STATUE_ANGRY_YELLOW));
    gameObjectArrayList.add(new GameObject(new PointF(200, 350), GameObjects.FROG_YELLOW));
    gameObjectArrayList.add(new GameObject(new PointF(200, 550), GameObjects.FROG_GREEN));
    gameObjectArrayList.add(new GameObject(new PointF(50, 50), GameObjects.BASKET_FULL_RED_FRUIT));
    gameObjectArrayList.add(new GameObject(new PointF(800, 800), GameObjects.OVEN_SNOW_YELLOW));

    insideMap = new GameMap(insideArray, Tiles.INSIDE, null,null, HelpMethods.GetSkeletonsRandomized(2, insideArray));
    outsideMap = new GameMap(outsideArray, Tiles.OUTSIDE, buildingArrayList, gameObjectArrayList, HelpMethods.GetSkeletonsRandomized(5, outsideArray));
//    HelpMethods.AddDoorwayToGameMap(outsideMap, insideMap, 0);
    HelpMethods.ConnectTwoDoorways(
        outsideMap,
        HelpMethods.CreateHitboxForDoorway(outsideMap, 0),
        insideMap,
        HelpMethods.CreateHitboxForDoorway(3, 6)
        );
    currentMap = outsideMap;
  }

  public void drawObject(Canvas c, GameObject go) {
    c.drawBitmap(
        go.getObjectType().getObjectImg(),
        go.getHitbox().left + cameraX,
        go.getHitbox().top - go.getObjectType().getHitboxRoof() + cameraY,
        null);
  }

  public void drawBuilding(Canvas c, Building b) {
    c.drawBitmap(b.getBuildingType().getHouseImg(), b.getPos().x + cameraX, b.getPos().y + cameraY, null);
  }

  public void drawTiles(Canvas c) {
    for (int j=0; j<currentMap.getArrayHeight(); j++) {
      for (int i=0; i<currentMap.getArrayWidth(); i++) {
        c.drawBitmap(currentMap.getFloorType().getSprite(currentMap.getSpriteId(i,j)), i*GameConstants.Sprite.SIZE+cameraX, j*GameConstants.Sprite.SIZE+cameraY, null);
      }
    }
  }

  public Doorway isPlayerOnDoorway(RectF playerHitbox) {
    for (Doorway doorway: currentMap.getDoorwayArrayList())
      if(doorway.isPlayerInsideDoorway(playerHitbox, cameraX, cameraY))
        return doorway;
    return null;
  }

  public int getMaxWidthCurrentMap() {
    return currentMap.getArrayWidth() * GameConstants.Sprite.SIZE;
  }

  public int getMaxHeightCurrentMap() {
    return currentMap.getArrayHeight() * GameConstants.Sprite.SIZE;
  }
}
