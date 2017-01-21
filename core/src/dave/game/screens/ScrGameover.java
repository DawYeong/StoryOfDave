package dave.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dave.game.GamMenu;
import dave.game.TbMenu;
import dave.game.TbsMenu;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static utils.Constants.*;

/**
 * Created by luke on 2016-04-05.
 */
public class ScrGameover implements Screen, InputProcessor {

    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName[] = new BitmapFont[2];
    Texture txDeathScreen;
    float fOpacity;

    public ScrGameover(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        txDeathScreen = new Texture("Gameover.png");
        screenName[0] = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        screenName[1] = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        screenName[0].getData().setScale(3, 3);
        screenName[1].getData().setScale(0.7f, 0.7f);
        tbMenu = new TbMenu("Main Menu", tbsMenu);
        tbMenu.setY(0);
        tbMenu.setX(Gdx.graphics.getWidth() / 2);
        stage.addActor(tbMenu);
        Gdx.input.setInputProcessor(stage);
        btnMenuListener();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //black background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(txDeathScreen, 0, 0);
        
        if (fOpacity < 1f) {
            fOpacity += 0.005f;
        }
        
        screenName[1].setColor(1, 1, 1, fOpacity);
        screenName[0].draw(batch, "You Died.", 150, 450);
        screenName[1].draw(batch, sFinalDays, 35, 300);
        screenName[1].draw(batch, sFinalTime, 35, 250);
        screenName[1].draw(batch, sFinalItems, 35, 200);

        batch.end();
        stage.act();
        stage.draw();
    }

    public void btnMenuListener() {
        tbMenu.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                fOpacity = 0;
                gamMenu.updateState(0);
                
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
