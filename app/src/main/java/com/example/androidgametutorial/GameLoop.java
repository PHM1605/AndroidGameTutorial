package com.example.androidgametutorial;

public class GameLoop implements Runnable {
  private GamePanel gamePanel;
  private Thread gameThread;

  public GameLoop(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
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
      gamePanel.update(delta);
      gamePanel.render();
      lastDelta = nowDelta;
    }
  }

  public void startGameLoop() {
    gameThread.start();
  }
}
