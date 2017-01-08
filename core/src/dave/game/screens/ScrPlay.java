package dave.game.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import static utils.Constants.PPM;
import utils.TiledObjectUtil;
import dave.game.GamMenu;
import dave.game.TbGUI;
import dave.game.TbMenu;
import dave.game.TbsGUI;
import dave.game.TbsHotbar;
import dave.game.TbsMenu;
import java.util.*;

/**
 * Created by luke on 2016-04-05.
 */
public class ScrPlay extends ApplicationAdapter implements Screen, InputProcessor {

    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu, tbGameover;
    Stage stage;
    ShapeRenderer SR;
    SpriteBatch batch;
    BitmapFont screenName;
    Texture img;
    Sprite sprVlad, sprLogic;
    Random ranGen = new Random();
    Texture txSheet, txBackground, txTemp, txOne, txShadow, txWater, txInvIcon;
    Animation araniVlad[];
    TextureRegion trTemp, trHouse;// a single temporary texture region
    boolean[] arbKeys = new boolean[512];
    int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
    int nFrame, nPos, nBar = 0, nBarWidth = 0;
    final int gameWidth = 200;
    final int gameHeight = 100;
    int nX, nY;

    float fSpeed;
    float fAniSpeed;
    float fInvPosX, fInvPosY;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tmr, treeRender;
    private TiledMap map, trees;
    private World world;
    private Body player, platform;

    TbsHotbar tbsHotbar;
    TbsGUI tbsGUI;
    TbGUI tbGUI, tbHotbar[] = new TbGUI[4], tbInv[] = new TbGUI[2], tbItems[] = new TbGUI[5],
            tbJournal[] = new TbGUI[2];

    float PPM = 32;
    int nInvY, nInvX, nItemY, nItemX;
    int nStamina, nHealth, nThirst, nSanity;
    int nAction;
    int nItemNum[] = new int[5];
    boolean isInvOpen = false;
    boolean isJournalOpen = false;
    InputMultiplexer multiplexer;
    boolean isStaminaBuffer = false;
    BitmapFont font;
    String sItem[] = new String[5];
    String sIcon[] = new String[4];

    private RayHandler rayHandler;
    private PointLight torchLight, sun;
    private float time;
    private boolean day = false;
    int nTimeFrame, nSeconds, nMinutes, nHours, nDays, nItemsTotal;
    //Day Night cycle source: http://pastie.org/private/8qpksvi8wy9gntolvtya
    
    Sprite sprAction;
    float fPlayX, fPlayY, fHitRadX, fHitRadY, fRad, fMouseY, fCurMouseX, fCurMouseY;
    int nIconHit = 0, nGrowth = 0;
    boolean isRadHit = false, isCollecting = false, isClicked;

    public ScrPlay(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {

        batch = new SpriteBatch();

        float nWScreen, nHScreen;
        nWScreen = Gdx.graphics.getWidth();
        nHScreen = Gdx.graphics.getHeight();
        float aspectRatio = (float) nHScreen / (float) nWScreen;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, nWScreen / 2, nHScreen / 2);

        SR = new ShapeRenderer();
        stage = new Stage();
        tbsMenu = new TbsMenu();

        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();

        map = new TmxMapLoader().load("gameMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(map);
        trees = new TmxMapLoader().load("gameMapTrees.tmx");
        treeRender = new OrthogonalTiledMapRenderer(trees);

        TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("Object Layer 1").getObjects());

        player = createBox(ranGen.nextInt((600 - 500) + 1) + 500, ranGen.nextInt((1400 - 1200) + 1) + 1200, 13, 27, false);
        platform = createBox(0, 0, 64, 32, true);

        nFrame = 0;
        nPos = 0; // the position in the SpriteSheet - 0 to 7
        txSheet = new Texture("playerSprite.png");
        txWater = new Texture("water.png");
        txInvIcon = new Texture("inventoryIcon.png");
        txWater.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        araniVlad = new Animation[18];
        playerSprite(5.2f);

        //All of the GUI setup
        font = new BitmapFont();
        nStamina = 200;
        nHealth = 200;
        nThirst = 100;
        nSanity = 100;

        sItem[0] = "stone";
        sItem[1] = "wood";
        sItem[2] = "iron";
        sItem[3] = "gold";
        sItem[4] = "diamond";

        sIcon[0] = "invIconSword";
        sIcon[1] = "invIconPick";
        sIcon[2] = "invIconAxe";
        sIcon[3] = "invIconHam";

        nInvY = Gdx.graphics.getHeight() / 2 - 128;
        nInvX = Gdx.graphics.getWidth() - 64;
        nItemY = 198;
        nItemX = 30;
        nItemNum[0] = 1; //Stone
        nItemNum[1] = 1; //Wood
        nItemNum[2] = 1; //Iron
        nItemNum[3] = 1; //Gold
        nItemNum[4] = 1; //Diamond

        for (int i = 0; i < 4; i++) {
            tbsHotbar = new TbsHotbar(sIcon[i]);
            tbHotbar[i] = new TbGUI("", tbsHotbar, 64, 64);
            tbHotbar[i].setY(nInvY);
            tbHotbar[i].setX(nInvX);
            nInvY += 64;
            stage.addActor(tbHotbar[i]);
        }
        for (int i = 0; i < 5; i++) {
            tbsGUI = new TbsGUI(sItem[i]);
            tbItems[i] = new TbGUI(sItem[i] + " x " + nItemNum[i], tbsGUI, 32, 32);
            tbItems[i].setY(nItemY);
            tbItems[i].setX(nItemX);
            nItemY -= 64;
            if (i == 3) {
                nItemX += 90;
                nItemY = 198;
            }
        }
        //Day Night cycle setup
        rayHandler = new RayHandler(world);

        torchLight = new PointLight(rayHandler, 100, Color.ORANGE, 0.5f, 0, 0);
        torchLight.setSoftnessLength(0f);
        torchLight.setXray(true);
        torchLight.setActive(false);

        // 0 is dark
        // 1 is bright
        sun = new PointLight(rayHandler, 100, new Color(255, 255, 255, 0.5f), 300, 400, 400);
        sun.setActive(false);
        sun.setXray(true);
        time = 1f;

        setupGUI();
        btnInvListener();
        

        //http://stackoverflow.com/questions/30902428/libgdx-stage-input-handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
    }

    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        actionSwitch();
        frameAnimation();
        trTemp = araniVlad[nPos].getKeyFrame(nFrame, true);     
        daynight();
        updateItems();
        gameTime();
        
        batch.begin();
        batch.draw(txWater, -129, -135, 32, 32, 2500, 2500); // makes water infinite
        batch.end();
        tmr.render();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(trTemp, player.getPosition().x * PPM - 16, player.getPosition().y * PPM - 16);
        batch.draw(txInvIcon, fInvPosX - 32, fInvPosY - 32);
        batch.end();
        
        treeRender.render();
        rayHandler.render();
        //b2dr.render(world, camera.combined.scl(PPM));
        statsBars();
        addItems();
        stage.act();
        stage.draw();
        InvOpen();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 2, height / 2);
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
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        SR.dispose();
        tmr.dispose();
        treeRender.dispose();
        map.dispose();
        trees.dispose();
        stage.dispose();
        rayHandler.dispose();
        torchLight.dispose();
        sun.dispose();

    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        inputUpdate(delta);
        cameraUpdate(delta);
        rayHandler.update();

        tmr.setView(camera);
        treeRender.setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void inputUpdate(float delta) {
        int nHorizontalForce = 0;
        int nVerticalForce = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            nVerticalForce += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            nVerticalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            nHorizontalForce -= 1;
            nPos = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            nHorizontalForce += 1;
            nPos = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            nAction = 0;
            for (int i = 0; i < 4; i++) {
                tbHotbar[i].setChecked(false);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            nAction = 5;
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {

            if (isStaminaBuffer == false) {
                fSpeed = 2.5f;
                playerSprite(3.6f);
                if (nStamina > 0) {
                    nStamina--;
                }
                if (nStamina == 0) {
                    isStaminaBuffer = true;

                }
            }
            if (isStaminaBuffer == true) {
                if (nStamina < 200) {
                    nStamina++;
                }
                if (nStamina == 200) {
                    isStaminaBuffer = false;
                }
                fSpeed = 1.5f;
                playerSprite(5.2f);
            }
        } else {
            fSpeed = 1.5f;
            playerSprite(5.2f);
            if (nStamina < 200) {
                nStamina++;
            }
            if (nStamina == 200) {
                isStaminaBuffer = false;
            }

        }
        player.setLinearVelocity(nHorizontalForce * fSpeed, player.getLinearVelocity().y);
        player.setLinearVelocity(player.getLinearVelocity().x, nVerticalForce * fSpeed);
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * .1f;
        position.y = camera.position.y + (player.getPosition().y * PPM - camera.position.y) * .1f;
        camera.position.set(position);

        camera.update();
    }

    public Body createBox(int nX, int nY, int nWidth, int nHeight, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(nX / PPM, nY / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(nWidth / 2 / PPM, nHeight / 2 / PPM);
        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public void playerSprite(float nAniSpeed) {
        fW = txSheet.getWidth() / 9;
        fH = txSheet.getHeight() / 2;
        for (int i = 0; i < 9; i++) {
            Sprite[] arSprVlad = new Sprite[9];
            for (int j = 0; j < 9; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprVlad = new Sprite(txSheet, fSx, fSy, fW, fH);
                arSprVlad[j] = new Sprite(sprVlad);
            }
            araniVlad[i] = new Animation(nAniSpeed, arSprVlad);

        }
    }

    public void frameAnimation() {
        if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.D)
                && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (nPos == 0 || (player.getLinearVelocity().x == 0 && player.getLinearVelocity().y == 0) ) {
                nFrame = 0;
            } else if (nPos == 1 || (player.getLinearVelocity().x == 0 && player.getLinearVelocity().y == 0)) {
                nFrame = 45; // I dont know why this works but it does.
                // Resets 1st frame when player stopped
            }
        } else {
            nFrame++;
        }
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
        if (amount == 1 && camera.zoom <= 1.2) {
            camera.zoom += 0.1;
        } else if (amount == -1 && camera.zoom >= 0.4) {
            camera.zoom -= 0.1;
        }
        return false;
    }

    public void handleInput() {
        //https://github.com/libgdx/libgdx/wiki/Orthographic-camera        
        camera.zoom = MathUtils.clamp(camera.zoom, 1.5f, 1.8f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f,
                Gdx.graphics.getWidth() - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f,
                Gdx.graphics.getHeight() - effectiveViewportHeight / 2f);
    }

    public void btnInvListener() {
        tbHotbar[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Sword");
                nAction = 1;
            }
        });
        tbHotbar[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Pickaxe");

                nAction = 2;

            }
        });
        tbHotbar[2].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Axe");
                nAction = 3;
            }
        });
        tbHotbar[3].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("Hammer");
                nAction = 4;
            }
        });
        tbInv[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbInv[0].remove();
                stage.addActor(tbInv[1]);
                isInvOpen = true;
                isJournalOpen = false;
            }
        });
        tbInv[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbInv[1].remove();
                stage.addActor(tbInv[0]);
                isInvOpen = false;
            }
        });
        tbJournal[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbJournal[0].remove();
                stage.addActor(tbJournal[1]);
                isJournalOpen = true;
                isInvOpen = false;
            }
        });
        tbJournal[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbJournal[1].remove();
                stage.addActor(tbJournal[0]);
                isJournalOpen = false;
            }
        });

    }

    public void InvOpen() {
        if (isInvOpen) {
            tbInv[0].remove();
            tbJournal[0].setX(200);
            stage.addActor(tbInv[1]);
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                isInvOpen = false;
            }
        } else if (isInvOpen == false) {
            tbInv[1].remove();
            tbJournal[0].setX(64);
            stage.addActor(tbInv[0]);
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                isInvOpen = true;
                isJournalOpen = false;
            }
        }
        if (isJournalOpen) {
            tbJournal[0].remove();
            tbInv[0].setX(200);
            stage.addActor(tbJournal[1]);
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                isJournalOpen = false;
            }
        } else if (!isJournalOpen) {
            tbJournal[1].remove();
            tbInv[0].setX(0);
            stage.addActor(tbJournal[0]);
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                isJournalOpen = true;
                isInvOpen = false;
            }
        }
    }

    public void setupGUI() {

        tbsGUI = new TbsGUI("itemInventory");
        tbInv[1] = new TbGUI("", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("journalInventory");
        tbJournal[1] = new TbGUI("Days survived: \n\n Time played: \n\nItems Collected: ", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("Journal");
        tbJournal[0] = new TbGUI("Journal", tbsGUI, 64, 64);
        tbJournal[0].setX(64);
        stage.addActor(tbJournal[0]);

        tbsGUI = new TbsGUI("bag");
        tbInv[0] = new TbGUI("Inventory", tbsGUI, 64, 64);
        stage.addActor(tbInv[0]);

        tbsGUI = new TbsGUI("healthIcon");
        tbGUI = new TbGUI(null, tbsGUI, 30, 30);
        tbGUI.setY(Gdx.graphics.getHeight() - 40);
        tbGUI.setX(5);
        stage.addActor(tbGUI);

        tbsGUI = new TbsGUI("thirstIcon");
        tbGUI = new TbGUI(null, tbsGUI, 32, 30);
        tbGUI.setY(Gdx.graphics.getHeight() - 78);
        tbGUI.setX(3);
        stage.addActor(tbGUI);

        tbsGUI = new TbsGUI("sanityIcon");
        tbGUI = new TbGUI(null, tbsGUI, 30, 30);
        tbGUI.setY(Gdx.graphics.getHeight() - 116);
        tbGUI.setX(5);
        stage.addActor(tbGUI);
    }

    public void statsBars() {
        SR.begin(ShapeRenderer.ShapeType.Filled);
        if (nHealth <= 75) {
            SR.setColor(Color.FIREBRICK);
        } else {
            SR.setColor(Color.RED);
        }
        SR.rect(35, Gdx.graphics.getHeight() - 32, nHealth, 15);
        if (isStaminaBuffer) {
            SR.setColor(Color.ROYAL);
        } else if (!isStaminaBuffer) {
            SR.setColor(Color.LIME);
        }
        SR.rect(35, Gdx.graphics.getHeight() - 32 - 7, nStamina, 5);
        SR.setColor(Color.SKY);
        SR.rect(35, Gdx.graphics.getHeight() - 69, nThirst, 10);
        SR.setColor(Color.YELLOW);
        SR.rect(35, Gdx.graphics.getHeight() - 105, nSanity, 10);
        SR.end();
    }

    public void addItems() {
        if (isInvOpen) {
            for (int i = 0; i < 5; i++) {
                if (nItemNum[i] > 0) {
                    stage.addActor(tbItems[i]);
                }
            }
        } else if (!isInvOpen) {
            for (int i = 0; i < 5; i++) {
                if (nItemNum[i] > 0) {
                    tbItems[i].remove();
                }
            }
        }
    }

    public void daynight() {
        // Very simple if statement that will repeat count from 0..1..0..
        // if Day time make light brighter 
        if (day) {
            time += Gdx.graphics.getDeltaTime() / 100; // divide by 100 to slow down
            if (time > 1) {
                day = false;
            }
            // if Night make light dimmer
        } else {
            time -= Gdx.graphics.getDeltaTime() / 100;
            if (time < 0.1f) {
                day = true;
                nDays++;
            }
        }

        // Set brightness to time
        rayHandler.setAmbientLight(time);
    }

    public void updateItems() {
        nItemsTotal = nItemNum[0] + nItemNum[1] + nItemNum[2] + nItemNum[3] + nItemNum[4];
        for (int i = 0; i < 5; i++) {
            tbItems[i].setText(sItem[i] + " x " + nItemNum[i]);
        }
        if (nSeconds < 10) {
            tbJournal[1].setText("Days survived: " + nDays + " \n\n Time played: " + nHours
                    + ":" + nMinutes + ":0" + nSeconds + "\n\nItems Collected: " + nItemsTotal);
        } else {
            tbJournal[1].setText("Days survived: " + nDays + " \n\n Time played: " + nHours
                    + ":" + nMinutes + ":" + nSeconds + "\n\nItems Collected: " + nItemsTotal);
        }

    }

    public void gameTime() {
        nTimeFrame++;
        if (nTimeFrame == 60) {
            nSeconds++;
            nTimeFrame = 0;
        }
        if (nSeconds == 60) {
            nMinutes++;
            nSeconds = 0;
        }
        if (nMinutes == 60) {
            nHours++;
            nMinutes = 0;
        }
    }
    
    public void actionSwitch() {
        if (nAction == 0) {
            txSheet = new Texture("playerSprite.png");
        } else if(nAction == 5) {
            txSheet = new Texture("playerSpriteTorch.png");
            torchLight.setActive(true);       
        } else if(nAction == 1) {
            txSheet = new Texture("playerSpriteSword.png");
        } else if(nAction == 2) {
            txSheet = new Texture("playerSpritePick.png");
        } else if(nAction == 3) {
            txSheet = new Texture("playerSpriteAxe.png");
        } else if(nAction == 4) {
            txSheet = new Texture("playerSpriteHam.png");
        }
        if(nAction != 5) {
            torchLight.setActive(false);    
        }    
    }
}
