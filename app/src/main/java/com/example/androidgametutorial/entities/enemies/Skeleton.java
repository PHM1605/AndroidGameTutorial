package com.example.androidgametutorial.entities.enemies;

import static com.example.androidgametutorial.main.MainActivity.GAME_HEIGHT;
import static com.example.androidgametutorial.main.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

import com.example.androidgametutorial.entities.Character;
import com.example.androidgametutorial.entities.GameCharacters;
import com.example.androidgametutorial.environments.GameMap;
import com.example.androidgametutorial.helpers.GameConstants;

import java.util.Random;

public class Skeleton extends Character {
  private Random rand = new Random();
  private long lastDirChange = System.currentTimeMillis();

  public Skeleton(PointF pos) {
    super(pos, GameCharacters.SKELETON);
  }

  public void update(double delta, GameMap gameMap) {
    updateMove(delta, gameMap);
    updateAnimation();
  }

  private void updateMove(double delta, GameMap gameMap) {
    if(System.currentTimeMillis() - lastDirChange >= 3000) {
      faceDir = rand.nextInt(4);
      lastDirChange = System.currentTimeMillis();
    }
    switch(faceDir) {
      case GameConstants.Face_Dir.DOWN:
        hitbox.top += delta * 300;
        hitbox.bottom += delta * 300;
        if(hitbox.bottom >= gameMap.getMapHeight()) {
          faceDir = GameConstants.Face_Dir.UP;
        }
        break;
      case GameConstants.Face_Dir.UP:
        hitbox.top -= delta * 300;
        hitbox.bottom -= delta * 300;
        if(hitbox.top <= 0) {
          faceDir = GameConstants.Face_Dir.DOWN;
        }
        break;
      case GameConstants.Face_Dir.RIGHT:
        hitbox.left += delta * 300;
        hitbox.right += delta * 300;
        if(hitbox.right >= gameMap.getMapWidth()) {
          faceDir = GameConstants.Face_Dir.LEFT;
        }
        break;
      case GameConstants.Face_Dir.LEFT:
        hitbox.left -= delta * 300;
        hitbox.right -= delta * 300;
        if(hitbox.left <= 0) {
          faceDir = GameConstants.Face_Dir.RIGHT;
        }
        break;

    }
  }
}
