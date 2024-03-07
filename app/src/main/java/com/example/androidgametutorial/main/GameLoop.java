package com.example.androidgametutorial.main;

public class GameLoop implements Runnable {
  private Game game;
  private Thread gameThread;

  public GameLoop(Game game) {
    this.game = game;
    gameThread = new Thread(this);
  }

  @Override
  public void run() {
    long lastDelta = System.nanoTime();
    long nanoSec = 1_000_000_000;
    while(true) {
      long nowDelta = System.nanoTime();
      double timeSinceLastDelta = nowDelta - lastDelta;
      double delta = timeSinceLastDelta / nanoSec;
      game.update(delta);
      game.render();
      lastDelta = nowDelta;
    }
  }

  public void startGameLoop() {
    gameThread.start();
  }
}
