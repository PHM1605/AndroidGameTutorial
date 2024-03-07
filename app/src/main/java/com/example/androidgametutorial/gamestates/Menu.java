package com.example.androidgametutorial.gamestates;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.androidgametutorial.helpers.interfaces.GameStateInterface;
import com.example.androidgametutorial.main.Game;
import com.example.androidgametutorial.main.MainActivity;
import com.example.androidgametutorial.ui.ButtonImages;
import com.example.androidgametutorial.ui.CustomButton;
import com.example.androidgametutorial.ui.GameImages;

public class Menu extends BaseState implements GameStateInterface {
  private final CustomButton btnStart;
  private final int menuX = MainActivity.GAME_WIDTH / 6;
  private final int menuY = 25;

  public Menu(Game game) {
    super(game);
    int btnStartX = menuX + GameImages.MAINMENU_MENUBG.getImage().getWidth() / 2 - ButtonImages.MENU_START.getWidth() / 2;
    int btnStartY = menuY + 100;
    btnStart = new CustomButton(btnStartX, btnStartY, ButtonImages.MENU_START.getWidth(), ButtonImages.MENU_START.getHeight());
  }

  @Override
  public void update(double delta) {
  }

  @Override
  public void render(Canvas c) {
    c.drawBitmap(
        GameImages.MAINMENU_MENUBG.getImage(),
        menuX,
        menuY,
        null
    );
    c.drawBitmap(
        ButtonImages.MENU_START.getBtnImg(btnStart.isPushed()),
        btnStart.getHitbox().left,
        btnStart.getHitbox().top,
        null
    );
  }

  @Override
  public void touchEvents(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      if (isIn(event, btnStart)) {
        btnStart.setPushed(true);
      }
    } else if(event.getAction() == MotionEvent.ACTION_UP) {
      if (isIn(event, btnStart) && btnStart.isPushed()) {
        game.setCurrentGameState(Game.GameState.PLAYING);
      }
      btnStart.setPushed(false);
    }
  }

  private boolean isIn(MotionEvent e, CustomButton b) {
    return b.getHitbox().contains(e.getX(), e.getY());
  }
}
