package com.example.androidgametutorial.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.androidgametutorial.gamestates.Playing;

public class PlayingUI {
  private final Playing playing;
  private final CustomButton btnMenu;
  private final PointF joystickCenterPos = new PointF(250, 500);
  private final PointF attackBtnCenterPos = new PointF(1300, 500);
  private final float radius = 100;
  private final Paint circlePaint;

  // For Multitouch joystick tracking
  private int joystickPointerId = -1;
  private int attackBtnPointerId = -1;
  private boolean touchDown; // joystick touchDown state

  public PlayingUI(Playing playing) {
    this.playing = playing;
    circlePaint = new Paint();
    circlePaint.setColor(Color.RED);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(5);
    btnMenu = new CustomButton(5, 5, ButtonImages.PLAYING_MENU.getWidth(), ButtonImages.PLAYING_MENU.getHeight());
  }

  public void draw(Canvas c) {
    drawUI(c);
  }

  private boolean isInsideRadius(PointF eventPos, PointF circle) {
    float a = eventPos.x - circle.x;
    float b = eventPos.y - circle.y;
    float c = (float) Math.hypot(a, b);
    return c <= radius;
  }

  private boolean checkInsideAttackBtn(PointF eventPos) {
    return isInsideRadius(eventPos, attackBtnCenterPos);
  }

  private boolean checkInsideJoyStick(PointF eventPos, int pointerId) {
    if (isInsideRadius(eventPos, joystickCenterPos)) {
      touchDown = true;
      joystickPointerId = pointerId;
      return true;
    }
    return false;
  }

  public void touchEvents(MotionEvent event) {
    final int action = event.getActionMasked();
    final int actionIndex = event.getActionIndex();
    final int pointerId = event.getPointerId(actionIndex);

    final PointF eventPos = new PointF(event.getX(actionIndex), event.getY(actionIndex));
    switch(action) {
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN: // Only when more than one touch points
        if (checkInsideJoyStick(eventPos, pointerId)) {
          touchDown = true;
        } else if(checkInsideAttackBtn(eventPos)) {
          // if another finger touch attack button -> do nothing
          if(attackBtnPointerId < 0) {
            playing.setAttacking(true);
            attackBtnPointerId = pointerId;
          }
        }
        else {
          if(isIn(eventPos, btnMenu)) {
            btnMenu.setPushed(true, pointerId);
          }
        }
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
        if(pointerId == joystickPointerId) {
          resetJoystickButton();
        } else {
          if (isIn(eventPos, btnMenu) && btnMenu.isPushed(pointerId)) {
            resetJoystickButton();
            playing.setGameStateToMenu();
          }
          // unPush will check if pointerId the same as what triggered; setPushed(false, pointerId) do not
          btnMenu.unPush(pointerId);
          // if release the finger that trigger the attack
          if (pointerId == attackBtnPointerId) {
            playing.setAttacking(false);
            attackBtnPointerId = -1;
          }
        }
        break;

      case MotionEvent.ACTION_MOVE:
        if (touchDown) {
          for(int i=0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == joystickPointerId) {
              float xDiff = event.getX(i) - joystickCenterPos.x;
              float yDiff = event.getY(i) - joystickCenterPos.y;
              playing.setPlayerMoveTrue(new PointF(xDiff, yDiff));
            }
          }
        }
    }
  }

  private void drawUI(Canvas c) {
    c.drawCircle(joystickCenterPos.x, joystickCenterPos.y, radius, circlePaint);
    c.drawCircle(attackBtnCenterPos.x, attackBtnCenterPos.y, radius, circlePaint);
    c.drawBitmap(
        ButtonImages.PLAYING_MENU.getBtnImg(btnMenu.isPushed(btnMenu.getPointerId())),
        btnMenu.getHitbox().left,
        btnMenu.getHitbox().top,
        null
    );
  }

  private void resetJoystickButton() {
    touchDown = false;
    playing.setPlayerMoveFalse();
    joystickPointerId = -1;
  }

  private boolean isIn(PointF eventPos, CustomButton b) {
    return b.getHitbox().contains(eventPos.x, eventPos.y);
  }
}
