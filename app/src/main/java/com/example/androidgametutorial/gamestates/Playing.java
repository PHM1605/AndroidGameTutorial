package com.example.androidgametutorial.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.androidgametutorial.entities.Character;
import com.example.androidgametutorial.entities.Player;
import com.example.androidgametutorial.entities.Weapons;
import com.example.androidgametutorial.entities.enemies.Skeleton;
import com.example.androidgametutorial.environments.MapManager;
import com.example.androidgametutorial.helpers.GameConstants;
import com.example.androidgametutorial.helpers.interfaces.GameStateInterface;
import com.example.androidgametutorial.main.Game;
import com.example.androidgametutorial.ui.PlayingUI;

import java.util.ArrayList;

public class Playing extends BaseState implements GameStateInterface {
  private float cameraX, cameraY;
  private boolean movePlayer;
  private PointF lastTouchDiff;
  private MapManager mapManager;
  private Player player;
  private final ArrayList<Skeleton> skeletons;
  private final Paint redPaint;

  private PlayingUI playingUI;

  public Playing(Game game) {
    super(game);
    mapManager = new MapManager();

    player = new Player();
    skeletons = new ArrayList<>();
    playingUI = new PlayingUI(this);

    redPaint = new Paint();
    redPaint.setStrokeWidth(1);
    redPaint.setStyle(Paint.Style.STROKE);
    redPaint.setColor(Color.RED);
  }

  @Override
  public void update(double delta) {
    updatePlayerMove(delta);
    player.update(delta, movePlayer);
    synchronized (skeletons) {
      for (Skeleton skeleton: skeletons)
        skeleton.update(delta);
    }
    mapManager.setCameraValues(cameraX, cameraY);
  }

  @Override
  public void render(Canvas c) {
    mapManager.draw(c);
    drawPlayer(c);
    synchronized (skeletons) {
      for (Skeleton skeleton: skeletons) {
        drawCharacter(c, skeleton);
      }
    }

    playingUI.draw(c);
  }

  private void drawPlayer(Canvas c) {
    c.drawBitmap(
        player.getGameCharType().getSprite(player.getAniIndex(), player.getFaceDir()),
        player.getHitbox().left,
        player.getHitbox().top,
        null
    );
    c.drawRect(player.getHitbox(), redPaint);
    drawWeapon(c);
  }

  private void drawWeapon(Canvas c) {
    c.drawBitmap(
        Weapons.BIG_SWORD.getWeaponImg(),
        player.getHitbox().left,
        player.getHitbox().top,
        null
    );
  }

  public void drawCharacter(Canvas canvas, Character c) {
    canvas.drawBitmap(
        c.getGameCharType().getSprite(c.getAniIndex(), c.getFaceDir()),
        c.getHitbox().left + cameraX,
        c.getHitbox().top + cameraY,
        null
    );
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
      if (lastTouchDiff.x > 0) player.setFaceDir(GameConstants.Face_Dir.RIGHT);
      else player.setFaceDir(GameConstants.Face_Dir.LEFT);
    } else {
      if (lastTouchDiff.y > 0) player.setFaceDir(GameConstants.Face_Dir.DOWN);
      else player.setFaceDir(GameConstants.Face_Dir.UP);
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
    if (mapManager.canMoveHere(player.getHitbox().left-(cameraX+deltaX)+pWidth, player.getHitbox().top-(cameraY+deltaY)+pHeight)) {
      cameraX += deltaX;
      cameraY += deltaY;
    }
  }

  public void setPlayerMoveTrue(PointF lastTouchDiff) {
    movePlayer = true;
    this.lastTouchDiff = lastTouchDiff;
  }

  public void setPlayerMoveFalse() {
    movePlayer = false;
    player.resetAnimation();
  }

  public void setGameStateToMenu() {
    game.setCurrentGameState(Game.GameState.MENU);
  }

  @Override
  public void touchEvents(MotionEvent event) {
    playingUI.touchEvents(event);
  }

  public void spawnSkeleton() {
    synchronized (skeletons) {
      skeletons.add(new Skeleton(new PointF(player.getHitbox().left - cameraX, player.getHitbox().top - cameraY)));
    }

  }

}
