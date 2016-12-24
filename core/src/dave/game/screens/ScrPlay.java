package dave.game.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dave.game.GamMenu;
import dave.game.TbMenu;
import dave.game.TbsMenu;
import java.util.Random;

/**
 * Created by luke on 2016-04-05.
 */
public class ScrPlay extends ApplicationAdapter implements Screen, InputProcessor {

    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu, tbGameover;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;
    Texture img;
    OrthographicCamera camera;
    Sprite sprVlad, sprLogic;
    Random ranGen = new Random(1);
    Texture txSheet, txBackground, txTemp, txOne, txShadow;
    Animation araniVlad[];
    TextureRegion trTemp, trHouse;// a single temporary texture region
    boolean[] arbKeys = new boolean[512];
    int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
    int nFrame, nPos, nBar = 0, nBarWidth = 0;
    final int gameWidth = 200;
    final int gameHeight = 100;
    int nX, nY;

    public ScrPlay(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        //screenName = new BitmapFont();
        Gdx.input.setInputProcessor(this);
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        camera = new OrthographicCamera(gameWidth * aspectRatio, gameHeight);
        camera.position.set(gameWidth / 2, gameHeight / 2, 0);
        nX = gameWidth / 2 - 16;
        nY = gameHeight / 2 - 16;
        sprLogic = new Sprite(new Texture("background.png"));
        nFrame = 0;
        nPos = 0; // the position in the SpriteSheet - 0 to 7
        araniVlad = new Animation[18];
        batch = new SpriteBatch();
        txSheet = new Texture("playerSprite.png");
        txShadow = new Texture("shadow.png");
        sprLogic.setSize(gameWidth * 2, gameHeight * 2);
        fW = txSheet.getWidth() / 9;
        fH = txSheet.getHeight() / 2;
        //System.out.println(fW + " " + fH);
        playerSprite();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        nFrame++;
        trTemp = araniVlad[nPos].getKeyFrame(nFrame, true);
        camera.update();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        sprLogic.draw(batch);
        batch.draw(txShadow, nX + 9, nY - 2);
        batch.draw(trTemp, nX, nY);
        playerMovement();
        batch.end();
        stage.act();
        stage.draw();
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
    public boolean keyDown(int i) {
        arbKeys[i] = true;
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        arbKeys[i] = false;
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
        if (amount == 1 && camera.zoom <= 1.8) {
            camera.zoom += 0.1;
        } else if (amount == -1 && camera.zoom >= 0.5) {
            camera.zoom -= 0.1;
        }
        return false;
    }

    public void playerMovement() {
        if (arbKeys[Input.Keys.W]) {
            //nPos = 1;
            camera.translate(0f, 1f);
            nY += 1;
            if (arbKeys[Input.Keys.A]) {
                nPos = 1;
                nX -= 1;
                camera.translate(-1f, 0f);
            } else if (arbKeys[Input.Keys.D]) {
                nPos = 0;
                nX += 1;
                camera.translate(1f, 0f);

            }
        } else if (arbKeys[Input.Keys.S]) {
            //nPos = 4;
            nY -= 1;
            camera.translate(0f, -1f);
            if (arbKeys[Input.Keys.A]) {
                nPos = 1;
                nX -= 1;
                camera.translate(-1f, 0f);
            } else if (arbKeys[Input.Keys.D]) {
                nPos = 0;
                nX += 1;
                camera.translate(1f, 0f);
            }
        } else if (arbKeys[Input.Keys.A]) {
            nPos = 1;
            nX -= 1;
            camera.translate(-1f, 0f);
        } else if (arbKeys[Input.Keys.D]) {
            nPos = 0;
            nX += 1;
            camera.translate(1f, 0f);
        } else {
            nFrame = 0;
        }
    }

    private void handleInput() {
        //https://github.com/libgdx/libgdx/wiki/Orthographic-camera        
        camera.zoom = MathUtils.clamp(camera.zoom, 1.5f, 1.8f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, gameWidth - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, gameHeight - effectiveViewportHeight / 2f);
    }

    public void playerSprite() {
        for (int i = 0; i < 9; i++) {
            Sprite[] arSprVlad = new Sprite[9];
            for (int j = 0; j < 9; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprVlad = new Sprite(txSheet, fSx, fSy, fW, fH);
                arSprVlad[j] = new Sprite(sprVlad);
            }
            araniVlad[i] = new Animation(5.2f, arSprVlad);

        }
    }

}
