package com.example.androidgametutorial.entities;

import static com.example.androidgametutorial.helpers.GameConstants.Sprite.HITBOX_SIZE;

import android.graphics.PointF;

import com.example.androidgametutorial.helpers.GameConstants;

public abstract class Character extends Entity{
  protected int aniTick, aniIndex;
  protected int faceDir = GameConstants.Face_Dir.DOWN;
  protected final GameCharacters gameCharType;

  public Character(PointF pos, GameCharacters gameCharType) {
    super(pos, HITBOX_SIZE, HITBOX_SIZE);
    this.gameCharType = gameCharType;
  }

  protected void updateAnimation() {
    aniTick++;
    if(aniTick >= GameConstants.Animation.SPEED) {
      aniTick -= GameConstants.Animation.SPEED;
      aniIndex++;
      if(aniIndex >= 4)
        aniIndex = 0;
    }
  }

  public void resetAnimation() {
    aniTick = 0;
    aniIndex = 0;
  }

  // Getter
  public int getAniIndex() {
    return aniIndex;
  }
  public int getFaceDir() {
    return faceDir;
  }
  public GameCharacters getGameCharType() {
    return gameCharType;
  }

  // Setter
  public void setFaceDir(int faceDir) {
    this.faceDir = faceDir;
  }
}
