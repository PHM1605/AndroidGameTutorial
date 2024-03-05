package com.example.androidgametutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.androidgametutorial.entities.GameCharacters;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.inputs.TouchEvents;

import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder holder;
  private GameLoop gameLoop;
  private Random rand = new Random();
  private TouchEvents touchEvents;
  // Player
  private float x, y;
  private int aniTick;
  private int aniSpeed = 10;
  private int playerAniIndexY, playerFaceDir = GameConstants.Face_Dir.RIGHT;
  private boolean movePlayer;
  private PointF lastTouchDiff;
  // Skeleton movement
  private PointF skeletonPos;
  private int skeletonDir = GameConstants.Face_Dir.DOWN;
  private long lastDirChange = System.currentTimeMillis();

  public GamePanel(Context context) {
    super(context);
    holder = getHolder();
    holder.addCallback(this);
    gameLoop = new GameLoop(this);
    skeletonPos = new PointF(rand.nextInt(720), rand.nextInt(1600));
    touchEvents = new TouchEvents(this);
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    render();
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    gameLoop.startGameLoop();
  }

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return touchEvents.touchEvent(event);
  }

  public void update(double delta) {
    // Update skeleton movement
    if(System.currentTimeMillis() - lastDirChange >= 3000) {
      skeletonDir = rand.nextInt(4);
      lastDirChange = System.currentTimeMillis();
    }
    switch(skeletonDir) {
      case GameConstants.Face_Dir.DOWN:
        skeletonPos.y += delta * 300;
        if(skeletonPos.y >= 1600) {
          skeletonDir = GameConstants.Face_Dir.UP;
        }
        break;
      case GameConstants.Face_Dir.UP:
        skeletonPos.y -= delta * 300;
        if(skeletonPos.y <= 0) {
          skeletonDir = GameConstants.Face_Dir.DOWN;
        }
        break;
      case GameConstants.Face_Dir.LEFT:
        skeletonPos.x -= delta * 300;
        if(skeletonPos.x <= 0) {
          skeletonDir = GameConstants.Face_Dir.RIGHT;
        }
        break;
      case GameConstants.Face_Dir.RIGHT:
        skeletonPos.x += delta * 300;
        if(skeletonPos.x >= 720) {
          skeletonDir = GameConstants.Face_Dir.LEFT;
        }
        break;
    }

    updatePlayerMove(delta);
    updateAnimation();
  }

  public void render() {
    Canvas c = holder.lockCanvas();
    c.drawColor(Color.BLACK);
    c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAniIndexY, playerFaceDir), x, y, null);
    c.drawBitmap(GameCharacters.SKELETON.getSprite(playerAniIndexY, skeletonDir), skeletonPos.x, skeletonPos.y, null);
    touchEvents.draw(c);
    holder.unlockCanvasAndPost(c);
  }

  private void updatePlayerMove(double delta) {
    if(!movePlayer)
      return;
    float baseSpeed = (float)delta * 300;
    float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
    double angle = Math.atan(ratio);
    float xSpeed = (float) Math.cos(angle);
    float ySpeed = (float) Math.sin(angle);

    if (xSpeed > ySpeed) {
      if (lastTouchDiff.x > 0) playerFaceDir = GameConstants.Face_Dir.RIGHT;
      else playerFaceDir = GameConstants.Face_Dir.LEFT;
    } else {
      if (lastTouchDiff.y > 0) playerFaceDir = GameConstants.Face_Dir.DOWN;
      else playerFaceDir = GameConstants.Face_Dir.UP;
    }

    if (lastTouchDiff.x < 0) {
      xSpeed *= -1;
    }
    if (lastTouchDiff.y < 0) {
      ySpeed *= -1;
    }
    x += xSpeed * baseSpeed;
    y += ySpeed * baseSpeed;
  }

  private void updateAnimation() {
    if(!movePlayer) return;
    aniTick++;
    if(aniTick >= aniSpeed) {
      aniTick -= aniSpeed;
      playerAniIndexY++;
      if(playerAniIndexY >= 4)
        playerAniIndexY = 0;
    }
  }

  public void setPlayerMoveTrue(PointF lastTouchDiff) {
    movePlayer = true;
    this.lastTouchDiff = lastTouchDiff;
  }

  public void setPlayerMoveFalse() {
    movePlayer = false;
    resetAnimation();
  }

  private void resetAnimation() {
    aniTick = 0;
    playerAniIndexY = 0;
  }
}
