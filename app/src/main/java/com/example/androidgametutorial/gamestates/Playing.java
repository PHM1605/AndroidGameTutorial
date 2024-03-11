package com.example.androidgametutorial.gamestates;

import static com.example.androidgametutorial.helpers.GameConstants.Sprite.X_DRAW_OFFSET;
import static com.example.androidgametutorial.helpers.GameConstants.Sprite.Y_DRAW_OFFSET;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
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
  private RectF attackBox = null;
  private boolean attacking = false;
  private boolean attackChecked;

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

    for(int i=0; i<5; i++)
      spawnSkeleton();

    updateWepHitbox();
  }

  @Override
  public void update(double delta) {
    updatePlayerMove(delta);
    player.update(delta, movePlayer);
    updateWepHitbox();

    // Check attack - deal damage to skeleton - only once
    if(attacking && !attackChecked)
      checkAttack();

    for (Skeleton skeleton: skeletons)
      if (skeleton.isActive())
        skeleton.update(delta);

    mapManager.setCameraValues(cameraX, cameraY);
  }

  private void checkAttack() {
    RectF attackBoxWithoutCamera = new RectF(attackBox);
    attackBoxWithoutCamera.left -= cameraX;
    attackBoxWithoutCamera.top -= cameraY;
    attackBoxWithoutCamera.right -= cameraX;
    attackBoxWithoutCamera.bottom -= cameraY;
    for(Skeleton s: skeletons) {
      if (attackBoxWithoutCamera.intersects(s.getHitbox().left, s.getHitbox().top, s.getHitbox().right, s.getHitbox().bottom)) {
        s.setActive(false);
      }
    }
    attackChecked = true;
  }

  private void updateWepHitbox() {
    PointF pos = getWepPos();
    float w = getWepWidth();
    float h = getWepHeight();
    attackBox = new RectF(pos.x, pos.y, pos.x+w, pos.y+h);
  }

  private PointF getWepPos() {
    PointF pos = null;
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.UP:
        pos = new PointF(
            player.getHitbox().left-0.5f*GameConstants.Sprite.SCALE_MULTIPLIER,
            player.getHitbox().top-Weapons.BIG_SWORD.getHeight()-Y_DRAW_OFFSET);
        break;
      case GameConstants.Face_Dir.DOWN:
        pos = new PointF(
            player.getHitbox().left+0.75f*GameConstants.Sprite.SCALE_MULTIPLIER,
            player.getHitbox().bottom);
        break;
      case GameConstants.Face_Dir.LEFT:
        pos = new PointF(
            player.getHitbox().left-Weapons.BIG_SWORD.getHeight() - X_DRAW_OFFSET,
            player.getHitbox().bottom-0.75f*GameConstants.Sprite.SCALE_MULTIPLIER-Weapons.BIG_SWORD.getWidth());
        break;
      case GameConstants.Face_Dir.RIGHT:
        pos = new PointF(
            player.getHitbox().right + X_DRAW_OFFSET,
            player.getHitbox().bottom-0.75f*GameConstants.Sprite.SCALE_MULTIPLIER-Weapons.BIG_SWORD.getWidth());
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + player.getFaceDir());
    }
    return pos;
  }

  private float getWepHeight() {
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.UP:
      case GameConstants.Face_Dir.DOWN:
        return Weapons.BIG_SWORD.getHeight();
      case GameConstants.Face_Dir.LEFT:
      case GameConstants.Face_Dir.RIGHT:
        return Weapons.BIG_SWORD.getWidth();
      default:
        throw new IllegalStateException("Unexpected value: " + player.getFaceDir());
    }
  }

  private float getWepWidth() {
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.UP:
      case GameConstants.Face_Dir.DOWN:
        return Weapons.BIG_SWORD.getWidth();
      case GameConstants.Face_Dir.LEFT:
      case GameConstants.Face_Dir.RIGHT:
        return Weapons.BIG_SWORD.getHeight();
      default:
        throw new IllegalStateException("Unexpected value: " + player.getFaceDir());
    }
  }

  @Override
  public void render(Canvas c) {
    mapManager.draw(c);
    drawPlayer(c);
    for (Skeleton skeleton: skeletons) {
      if (skeleton.isActive())
        drawCharacter(c, skeleton);
    }

    playingUI.draw(c);
  }

  private void drawPlayer(Canvas c) {
    c.drawBitmap(
        Weapons.SHADOW.getWeaponImg(),
        player.getHitbox().left,
        player.getHitbox().bottom - 4 * GameConstants.Sprite.SCALE_MULTIPLIER,
        null);
    c.drawBitmap(
        player.getGameCharType().getSprite(getAniIndex(), player.getFaceDir()),
        player.getHitbox().left - X_DRAW_OFFSET,
        player.getHitbox().top - GameConstants.Sprite.Y_DRAW_OFFSET,
        null
    );
    c.drawRect(player.getHitbox(), redPaint);
    if (attacking)
      drawWeapon(c);
  }

  private int getAniIndex() {
    // Row number 4 is attacking in sprite
    if(attacking) return 4;
    return player.getAniIndex();
  }

  private void drawWeapon(Canvas c) {
    c.rotate(getWepRot(), attackBox.left, attackBox.top);
    c.drawBitmap(
        Weapons.BIG_SWORD.getWeaponImg(),
        attackBox.left + wepRotAdjustLeft(),
        attackBox.top + wepRotAdjustTop(),
        null
    );
    c.rotate(-getWepRot(), attackBox.left, attackBox.top);
    c.drawRect(attackBox, redPaint);
  }

  private float wepRotAdjustTop() {
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.LEFT:
      case GameConstants.Face_Dir.UP:
        return -Weapons.BIG_SWORD.getHeight();
      default:
        return 0.0f;
    }
  }

  private float wepRotAdjustLeft() {
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.RIGHT:
      case GameConstants.Face_Dir.UP:
        return -Weapons.BIG_SWORD.getWidth();
      default:
        return 0.0f;
    }
  }

  private float getWepRot() {
    switch(player.getFaceDir()) {
      case GameConstants.Face_Dir.UP:
        return 180.0f;
      case GameConstants.Face_Dir.LEFT:
        return 90.0f;
      case GameConstants.Face_Dir.RIGHT:
        return 270.0f;
      default:
        return 0.0f;
    }
  }

  // draw skeletons
  public void drawCharacter(Canvas canvas, Character c) {
    canvas.drawBitmap(
        Weapons.SHADOW.getWeaponImg(),
        c.getHitbox().left + cameraX,
        c.getHitbox().bottom - 4 * GameConstants.Sprite.SCALE_MULTIPLIER + cameraY,
        null);
    canvas.drawBitmap(
        c.getGameCharType().getSprite(c.getAniIndex(), c.getFaceDir()),
        c.getHitbox().left + cameraX - X_DRAW_OFFSET,
        c.getHitbox().top + cameraY - GameConstants.Sprite.Y_DRAW_OFFSET,
        null
    );
    canvas.drawRect(
        c.getHitbox().left+cameraX,
        c.getHitbox().top+cameraY,
        c.getHitbox().right+cameraX,
        c.getHitbox().bottom+cameraY,
        redPaint);
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
    skeletons.add(new Skeleton(new PointF(player.getHitbox().left - cameraX, player.getHitbox().top - cameraY)));
  }

  public void setAttacking(boolean attacking) {
    this.attacking = attacking;
    if (!attacking)
      attackChecked = false;
  }
}
