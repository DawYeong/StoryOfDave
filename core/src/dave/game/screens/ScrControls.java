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

/**
 * Created by luke on 2016-04-05.
 */
public class ScrControls implements Screen, InputProcessor {
    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;
    Texture txMenu, txControls;

    public ScrControls(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        txMenu = new Texture("menu.png");
        txControls = new Texture("controls.png");
        tbMenu = new TbMenu("BACK", tbsMenu);
        tbMenu.setY(0);
        tbMenu.setX(0);
        tbMenu.setY(0);
        tbMenu.setX(0);
        stage.addActor(tbMenu);
        Gdx.input.setInputProcessor(stage);
        btnMenuListener();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); //black background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(txMenu,-2600, -1550, Gdx.graphics.getWidth()*8, Gdx.graphics.getHeight()*8);
        batch.draw(txControls,0,0);
        batch.end();
        stage.act();
        stage.draw();
    }

    public void btnMenuListener() {
        tbMenu.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               
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