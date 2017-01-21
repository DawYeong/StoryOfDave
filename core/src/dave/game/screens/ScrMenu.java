package dave.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dave.game.GamMenu;
import dave.game.TbMenu;
import dave.game.TbsMenu;
import static utils.Constants.*;

import java.awt.Font;

/**
 * Created by luke on 2016-04-05.
 */
public class ScrMenu implements Screen, InputProcessor {

    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbPlay, tbGameover;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;
    Texture txMenu;
    public ScrMenu(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        txMenu = new Texture("menu.png");
        tbPlay = new TbMenu("Begin Dave's Story", tbsMenu);
        tbGameover = new TbMenu("Controls", tbsMenu);
        tbGameover.setY(Gdx.graphics.getHeight() / 2 - (tbGameover.getHeight() / 2) - 75);
        tbGameover.setX(Gdx.graphics.getWidth() / 3 - (tbGameover.getWidth() / 2));
        tbPlay.setY(Gdx.graphics.getHeight() / 2 - (tbGameover.getHeight() / 2) - 10);
        tbPlay.setX(Gdx.graphics.getWidth()/3 - (tbGameover.getWidth() / 2));
        stage.addActor(tbPlay);
        stage.addActor(tbGameover);
        Gdx.input.setInputProcessor(stage);
        btnPlayListener();
        btnGameoverListener();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1); //Green background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
//        batch.setProjectionMatrix(utils.Constants.camera.combined);
        batch.draw(txMenu,-250, -240, Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2);
        batch.end();
        stage.act();
        stage.draw();
    }

    public void btnPlayListener() {
        tbPlay.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                nHours = 0;
                nMinutes = 0;
                nSeconds = 0;
                gamMenu.updateState(1); // switch to Play screen.
            }
        });
    }

    public void btnGameoverListener() {
        tbGameover.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                gamMenu.updateState(3);
            }
        });
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        txMenu.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
