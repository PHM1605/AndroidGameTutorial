package com.example.androidgametutorial.gamestates;

import com.example.androidgametutorial.main.Game;

public abstract class BaseState {
  protected Game game;

  public BaseState(Game game) {
    this.game = game;
  }

  public Game getGame() {
    return game;
  }
}
