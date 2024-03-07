package com.example.androidgametutorial.ui;

import android.graphics.RectF;

public class CustomButton {
  private final RectF hitbox;
  private boolean pushed;
  private int pointerId = -1;

  public CustomButton(float x, float y, float width, float height) {
    hitbox = new RectF(x, y, x+width, y+height);
  }

  public RectF getHitbox() {
    return hitbox;
  }

  public boolean isPushed() {
    return pushed;
  }

  // Is this button pushed by the same finger?
  public boolean isPushed(int pointerId) {
    if (this.pointerId != pointerId)
      return false;
    return pushed;
  }

  public void setPushed(boolean pushed) {
    this.pushed = pushed;
  }

  public void setPushed(boolean pushed, int pointerId) {
    // if we push with one finger, push down again with another finger
    if (this.pushed)
      return;
    this.pushed = pushed;
    this.pointerId = pointerId;
  }

  public void unPush(int pointerId) {
    if (this.pointerId != pointerId) {
      return;
    }
    this.pointerId = -1;
    this.pushed = false;
  }

  public int getPointerId() {
    return pointerId;
  }

}
