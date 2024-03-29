package com.example.androidgametutorial.main;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private final Game game;

  public GamePanel(Context context) {
    super(context);
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    game = new Game(holder);
  }

  @Override
  public void surfaceCreated(@NonNull SurfaceHolder holder) {
    game.startGameLoop();
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
  }

  @Override
  public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return game.touchEvent(event);
  }









}
