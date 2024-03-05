package com.example.androidgametutorial.inputs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.androidgametutorial.GamePanel;

public class TouchEvents {
  private float xCenter = 360, yCenter = 1300, radius = 100;
  private Paint circlePaint, yellowPaint;
  private float xTouch, yTouch;
  private boolean touchDown;
  private GamePanel gamePanel;

  public TouchEvents(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    circlePaint = new Paint();
    circlePaint.setColor(Color.RED);;
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(5);
    yellowPaint = new Paint();
    yellowPaint.setColor(Color.YELLOW);
  }
  public boolean touchEvent(MotionEvent event) {
    switch(event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        float x = event.getX();
        float y = event.getY();
        float a = x - xCenter;
        float b = y - yCenter;
        float c = (float) Math.hypot(a, b);
        if (c <= radius) {
          touchDown = true;
          xTouch = x;
          yTouch = y;
        }
        break;
      case MotionEvent.ACTION_UP:
        touchDown = false;
        gamePanel.setPlayerMoveFalse();
        break;
      case MotionEvent.ACTION_MOVE:
        if(touchDown) {
          xTouch = event.getX();
          yTouch = event.getY();
          float xDiff = xTouch - xCenter;
          float yDiff = yTouch - yCenter;
          this.gamePanel.setPlayerMoveTrue(new PointF(xDiff, yDiff));
        }
    }
    return true;
  }
  public void draw(Canvas c) {
    c.drawCircle(xCenter, yCenter, radius, circlePaint);
    if(touchDown) {
      c.drawLine(xCenter, yCenter, xTouch, yTouch, yellowPaint);
      c.drawLine(xTouch, yTouch, xTouch, yCenter, yellowPaint);
      c.drawLine(xCenter, yCenter, xTouch, yCenter, yellowPaint);
    }
  }
}
