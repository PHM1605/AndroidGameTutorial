package com.example.androidgametutorial;

import static com.example.androidgametutorial.MainActivity.GAME_HEIGHT;
import static com.example.androidgametutorial.MainActivity.GAME_WIDTH;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.androidgametutorial.entities.GameCharacters;
import com.example.androidgametutorial.environments.GameMap;
import com.example.androidgametutorial.environments.MapManager;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.inputs.TouchEvents;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder holder;
  private GameLoop gameLoop;

  private TouchEvents touchEvents;
  // Player
  private float playerX = GAME_WIDTH/2, playerY=GAME_HEIGHT/2;
  private float cameraX, cameraY;
  private int aniTick;
  private int aniSpeed = 10;
  private int playerAniIndexY, playerFaceDir = GameConstants.Face_Dir.RIGHT;
  private boolean movePlayer;
  private PointF lastTouchDiff;
  // Skeleton movement
  private PointF skeletonPos;

  // Map
  private MapManager mapManager;

  public GamePanel(Context context) {
    super(context);
    holder = getHolder();
    holder.addCallback(this);
    gameLoop = new GameLoop(this);
    skeletonPos = new PointF(rand.nextInt(GAME_WIDTH), rand.nextInt(GAME_HEIGHT));
    touchEvents = new TouchEvents(this);
    mapManager = new MapManager();
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
    // Move the camera
    updatePlayerMove(delta);
    mapManager.setCameraValues(cameraX, cameraY);
    // Update skeleton movement


  }

  public void render() {
    Canvas c = holder.lockCanvas();
    c.drawColor(Color.BLACK);
    mapManager.draw(c);
    c.drawBitmap(GameCharacters.PLAYER.getSprite(playerAniIndexY, playerFaceDir), playerX, playerY, null);
    c.drawBitmap(GameCharacters.SKELETON.getSprite(playerAniIndexY, skeletonDir), skeletonPos.x+cameraX, skeletonPos.y+cameraY, null);
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

    // Player width and height
    int pWidth = GameConstants.Sprite.SIZE;
    int pHeight = GameConstants.Sprite.SIZE;
    // Camera displacement X and Y
    float deltaX = xSpeed * baseSpeed * (-1);
    float deltaY = ySpeed * baseSpeed * (-1);
    if (xSpeed <= 0) pWidth = 0;
    if (ySpeed <=0) pHeight = 0;
    if (mapManager.canMoveHere(playerX-(cameraX+deltaX)+pWidth, playerY-(cameraY+deltaY)+pHeight)) {
      cameraX += deltaX;
      cameraY += deltaY;
    }
    System.out.println(cameraX);
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
