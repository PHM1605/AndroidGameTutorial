package com.example.androidgametutorial.entities;

import static com.example.androidgametutorial.MainActivity.GAME_HEIGHT;
import static com.example.androidgametutorial.MainActivity.GAME_WIDTH;

import android.graphics.PointF;

import com.example.androidgametutorial.MainActivity;

public class Player extends Character{
  public Player() {
    super(new PointF(GAME_WIDTH/2, GAME_HEIGHT), GameCharacters.PLAYER);
  }

  public void update(double delta, boolean movePlayer) {
    if(movePlayer)
      updateAnimation();
  }
}
