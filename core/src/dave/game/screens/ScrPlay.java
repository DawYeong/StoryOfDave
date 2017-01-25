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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import static utils.Constants.*;
import utils.TiledObjectUtil;
import dave.game.GamMenu;
import dave.game.TbGUI;
import dave.game.TbMenu;
import dave.game.TbsGUI;
import dave.game.TbsHotbar;
import dave.game.TbsMenu;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by luke on 2016-04-05.
 */
public class ScrPlay extends ApplicationAdapter implements Screen, InputProcessor {

    GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu, tbGameover;
    ShapeRenderer SR;
    SpriteBatch batch, batchAction;
    BitmapFont screenName;
    
    Random ranGen = new Random();
    Texture txSheet, txBackground, txWater, txSheetShadow;
    Animation araniVlad[], araniShadow[];
    TextureRegion trPlayer, trShadow;// a single temporary texture region
    
    int nBar = 0, nBarWidth = 0;
    int  nFrame, nPos, nShadowFrame, nShadowPos;
    int nX, nY;

    float fSpeed;
    float fAniSpeed;
    float fInvPosX, fInvPosY;

    int nInvY, nInvX, nItemY, nItemX;
    float fStamina, fThirst, fSanity, fHealth;
    
    int nAction;
    int nItemNum[] = new int[7];
    boolean isInvOpen = false;
    boolean isJournalOpen = false;
    boolean isCraftingOpen = false;
    boolean isBuildingOpen = false;
    InputMultiplexer multiplexer;
    boolean isStaminaBuffer = false;
    BitmapFont font;
    String sItem[] = new String[5];
    String sIcon[] = new String[4];

    RayHandler rayHandler;
    PointLight torchLight;
    float time;
    boolean day = false;
    public static int nTimeFrame, nDays, nItemsTotal;
    //Day Night cycle source: http://pastie.org/private/8qpksvi8wy9gntolvtya

    Sprite sprAction;
    float fPlayX, fPlayY, fHitRadX, fHitRadY, fRad, fMouseY, fCurMouseX, fCurMouseY;
    int nIconHit = 0;
    boolean isRadHit = false, isCollecting = false;

    int nTorchFlicker;
    int nTorchRange = ranGen.nextInt((30 - 1) + 1) + 1;
    boolean isMining = false, isCutting, isBuilding;
    boolean isClicked = false;
    int nGrowth = 0, nLuck;
    int nSane = 0;
    float fArmor = 0;
    float fArmorSpeed = 0;
    float fStaminaMax = 200;
    int nTorchNum, nInsanity = 0;
    float colorBlue = 0.4f, colorRed = 0.6f, colorGreen = 0.2f;
    PointLight groundLight;
    boolean torchPlace;
    boolean isSane;
    Texture txGroundTorch;

    float Game_Width = 640;
    float Game_Height = 480;
    Viewport viewport;
    int nThirstBuffer = 0;

    public ScrPlay(GamMenu _gamMenu) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public void show() {

        batch = new SpriteBatch();
        batchAction = new SpriteBatch();

        float nWScreen, nHScreen;
        nWScreen = Gdx.graphics.getWidth();
        nHScreen = Gdx.graphics.getHeight();
        int aspectRatio = (int) nHScreen / (int) nWScreen;

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

        player = createBox(ranGen.nextInt((700 - 300) + 1) + 300, ranGen.nextInt((1500 - 1300) + 1) + 1300, 13, 5, false);
        shadowDave = createBox(ranGen.nextInt((700 - 300) + 1) + 300, ranGen.nextInt((1500 - 1300) + 1) + 1300, 13, 5, false);
        
        nFrame = 0;
        nPos = 0; // the position in the SpriteSheet - 0 to 7
        nShadowPos = 0;
        nShadowFrame = 0;
        
        txSheetShadow = new Texture("shadowSprite.png");
        txSheet = new Texture("playerSprite.png");
        txWater = new Texture("water.png");
        txGroundTorch = new Texture("groundTorch.png");
        txWater.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        araniVlad = new Animation[18];
        araniShadow = new Animation[18];
        playerSprite(5.2f);
        shadowSprite(4.4f);
        //shadowSprite(5.2f);

        setupGUI();
        btnInvListener();

        //Day Night cycle setup
        rayHandler = new RayHandler(world);
        rayHandler.useDiffuseLight(true);
        rayHandler.setCulling(true);

        torchLight = new PointLight(rayHandler, 100, Color.ORANGE, 3f, 0, 0);
        torchLight.setXray(true);
        torchLight.setActive(false);

        // 0 is dark
        // 1 is bright
        time = 1f;

//        sprAction = new Sprite(new Texture(Gdx.files.internal("sword.png")));
        //http://stackoverflow.com/questions/30902428/libgdx-stage-input-handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        if(oneTime == true) {
            gameTime();
            oneTime = false;
        }
    }

    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        frameAnimation();
        nShadowFrame++;
        trPlayer = araniVlad[nPos].getKeyFrame(nFrame, true);
        trShadow = araniShadow[nShadowPos].getKeyFrame(nShadowFrame, true);
        daynight();
        updateItems();
        torchLightFlicker();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(txWater, -129, -133, 32, 32, 4000, 2500); // makes water infinite
        batch.end();
        tmr.render();
        batch.begin();
        batch.draw(trPlayer, player.getPosition().x * PPM - 16, player.getPosition().y * PPM - 5);
        batch.draw(trShadow, shadowDave.getPosition().x * PPM - 16, shadowDave.getPosition().y * PPM - 5);
        batch.end();
        treeRender.render();
        //b2dr.render(world, camera.combined.scl(PPM));

        if (nPos == 0) {
            torchLight.setPosition(player.getPosition().x + (11 / PPM), player.getPosition().y + (11 / PPM));
        } else if (nPos == 1) {
            torchLight.setPosition(player.getPosition().x - (11 / PPM), player.getPosition().y + (11 / PPM));
        }
        rayHandler.setCombinedMatrix(camera.combined.scl(PPM));
//        rayHandler.setCombinedMatrix(camera.combined, camera.position.x * PPM, camera.position.y * PPM, camera.viewportWidth, 
//                camera.viewportHeight);
        rayHandler.updateAndRender();
        statsBars();
        stage.act();
        stage.draw();
        actionSwitch();
        Mining();
        InvOpen();
        addItems();
        sanity();
        thirst();
        Death();
        
        //Test death screen
        hitDamage(0.1f);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 2, height / 2);
//        viewport.update(width, height);
//        viewport.apply();
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
        batchAction.dispose();

        txSheet.dispose();
        txBackground.dispose();
        txWater.dispose();

        SR.dispose();
        tmr.dispose();
        treeRender.dispose();
        map.dispose();
        trees.dispose();
        stage.dispose();
        rayHandler.dispose();
        torchLight.dispose();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        inputUpdate(delta);
        cameraUpdate(delta);

        tmr.setView(camera);
        treeRender.setView(camera);
        batch.setProjectionMatrix(camera.combined);

    }

    public void inputUpdate(float delta) {
        int nHorizontalForce = 0;
        int nVerticalForce = 0;
        if (!isMining) {
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
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {

            if (isStaminaBuffer == false && fThirst > 0) {
                fSpeed = 2.5f - fArmorSpeed;
                playerSprite(3.6f - fArmorSpeed);
                if (fStamina > 0) {
                    fStamina--;
                }
                if (fStamina == 0) {
                    isStaminaBuffer = true;

                }
            }
            if (isStaminaBuffer == true) {
                if (fStamina < fStaminaMax) {
                    fStamina++;
                }
                if (fStamina == fStaminaMax) {
                    isStaminaBuffer = false;
                }
                fSpeed = 1.5f - fArmorSpeed;
                playerSprite(5.2f - fArmorSpeed);
            }
        } else {
            fSpeed = 1.5f - fArmorSpeed;
            playerSprite(5.2f - fArmorSpeed);
            if (fStamina < fStaminaMax) {
                fStamina++;
            }
            if (fStamina == fStaminaMax) {
                isStaminaBuffer = false;
            }

        }
        player.setLinearVelocity(nHorizontalForce * fSpeed, player.getLinearVelocity().y);
        player.setLinearVelocity(player.getLinearVelocity().x, nVerticalForce * fSpeed);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (nAction != 0) {
                Gdx.input.setCursorCatched(false);
                Gdx.input.setCursorPosition((int) fCurMouseX, (int) fCurMouseY);
            }
            nAction = 0;
//            for (int i = 0; i < 4; i++) {
//                tbHotbar[i].setChecked(false);
//            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (nAction != 5) {
                Gdx.input.setCursorCatched(false);
                Gdx.input.setCursorPosition((int) fCurMouseX, (int) fCurMouseY);
            }
            nAction = 5;
        }
    }

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * .1f;
        position.y = (camera.position.y + (player.getPosition().y * PPM - camera.position.y) * .1f) + (32 / PPM);
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
        Sprite sprVlad;
        int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
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
    public void shadowSprite(float nAniSpeed) {
        Sprite sprShadow;
        int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
        fW = txSheetShadow.getWidth() / 9;
        fH = txSheetShadow.getHeight() / 2;
        for (int i = 0; i < 9; i++) {
            Sprite[] arSprVlad = new Sprite[9];
            for (int j = 0; j < 9; j++) {
                fSx = j * fW;
                fSy = i * fH;
                sprShadow = new Sprite(txSheetShadow, fSx, fSy, fW, fH);
                arSprVlad[j] = new Sprite(sprShadow);
            }
            araniShadow[i] = new Animation(nAniSpeed, arSprVlad);

        }
    }    

    public void frameAnimation() {
        if (!isMining) {
            if (!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.D)
                    && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
                if (nPos == 0) {
                    nFrame = 0;
                } else if (nPos == 1) {
                    nFrame = 45; // I dont know why 45 works but it does.
                    // Resets 1st frame when player stopped
                }
            } else {
                nFrame++;
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                || Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            isMining = false;
            nInsanity = 0;
            isSane = true;
        }
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
        if (nAction == 2) {
            isMining = true;
            player.setLinearVelocity(0, 0);
        }
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
        /*tbInv[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbInv[0].remove();
                stage.addActor(tbInv[1]);
                isInvOpen = true;
                isJournalOpen = false;
                isBuildingOpen = false;
                isCraftingOpen = false;
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
                isBuildingOpen = false;
                isCraftingOpen = false;
            }
        });
        tbJournal[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbJournal[1].remove();
                stage.addActor(tbJournal[0]);
                isJournalOpen = false;
            }
        });
        tbCraft[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbCraft[0].remove();
                stage.addActor(tbCraft[1]);
                isJournalOpen = false;
                isInvOpen = false;
                isBuildingOpen = false;
                isCraftingOpen = true;
            }
        });
        tbCraft[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbCraft[1].remove();
                stage.addActor(tbCraft[0]);
                isCraftingOpen = false;
            }
        });
        tbBuild[0].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbBuild[0].remove();
                stage.addActor(tbBuild[1]);
                isJournalOpen = false;
                isInvOpen = false;
                isBuildingOpen = true;
                isCraftingOpen = false;
            }
        });
        tbBuild[1].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                tbBuild[1].remove();
                stage.addActor(tbBuild[0]);
                isBuildingOpen = false;
            }
        });*/
        tbItems[5].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (nItemNum[5] > 0 && fHealth < 100) {
                    fHealth += 25;
                    nItemNum[5]--;
                    nThirstBuffer = 0;
                    fThirst += 25;
                    if (fHealth > 100) {
                        fHealth = 100;
                    }
                }
            }
        });
        tbCraftItems[0].addListener(new ChangeListener() { //Armor Listener
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (nItemNum[2] >= 10) {
                    nItemNum[2] -= 10;
                    fArmor += 10;
//                    fArmorSpeed += 0.5f;
//                    fStamina -=5;
                    if (fArmor > 100) {
                        fArmor = 100;
                    }
                }
            }
        });
        tbCraftItems[1].addListener(new ChangeListener() { //Torch Listener
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (nItemNum[1] >= 1) {
                    nItemNum[1] -= 1;
                    nItemNum[6] += 2;
                }
            }
        });
        tbItems[6].addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                groundLight = new PointLight(rayHandler, 100, Color.ORANGE, 3f, 0, 0);
                groundLight.setXray(true);
                groundLight.setActive(true);
                groundLight.setPosition(player.getPosition().x, player.getPosition().y);
                nItemNum[6] -= 1;
                txGroundTorch = new Texture("groundTorch.png");
                torchPlace = true;
            }
        });
    }

    public void InvOpen() {
        if (isInvOpen || isJournalOpen || isCraftingOpen || isBuildingOpen) {
            if (isInvOpen) {
                tbInv[0].remove();
                stage.addActor(tbJournal[0]);
                stage.addActor(tbCraft[0]);
                stage.addActor(tbBuild[0]);
                tbJournal[0].setX(200);
                tbCraft[0].setX(264);
                tbBuild[0].setX(328);
                stage.addActor(tbInv[1]);
                if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    isInvOpen = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                    isInvOpen = false;
                    isJournalOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    isInvOpen = false;
                    isBuildingOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    isInvOpen = false;
                    isCraftingOpen = true;
                }
            } else if (isJournalOpen) {
                tbJournal[0].remove();
                stage.addActor(tbInv[0]);
                stage.addActor(tbCraft[0]);
                stage.addActor(tbBuild[0]);
                tbInv[0].setX(200);
                tbCraft[0].setX(264);
                tbBuild[0].setX(328);
                stage.addActor(tbJournal[1]);
                if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                    isJournalOpen = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    isJournalOpen = false;
                    isInvOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    isJournalOpen = false;
                    isBuildingOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    isJournalOpen = false;
                    isCraftingOpen = true;
                }
            } else if (isCraftingOpen) {
                tbCraft[0].remove();
                stage.addActor(tbJournal[0]);
                stage.addActor(tbInv[0]);
                stage.addActor(tbBuild[0]);
                tbInv[0].setX(200);
                tbJournal[0].setX(264);
                tbBuild[0].setX(328);
                stage.addActor(tbCraft[1]);
                if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    isCraftingOpen = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                    isCraftingOpen = false;
                    isJournalOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    isCraftingOpen = false;
                    isBuildingOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    isCraftingOpen = false;
                    isInvOpen = true;
                }
            } else if (isBuildingOpen) {
                tbBuild[0].remove();
                stage.addActor(tbInv[0]);
                stage.addActor(tbCraft[0]);
                stage.addActor(tbJournal[0]);
                tbInv[0].setX(200);
                tbJournal[0].setX(264);
                tbCraft[0].setX(328);
                stage.addActor(tbBuild[1]);
                if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                    isBuildingOpen = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                    isBuildingOpen = false;
                    isJournalOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                    isBuildingOpen = false;
                    isInvOpen = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                    isBuildingOpen = false;
                    isCraftingOpen = true;
                }
            }

        } else if (!isInvOpen || !isJournalOpen || !isCraftingOpen || !isBuildingOpen) {
            tbInv[0].setX(0);
            tbJournal[0].setX(64);
            tbCraft[0].setX(128);
            tbBuild[0].setX(192);
            stage.addActor(tbInv[0]);
            stage.addActor(tbJournal[0]);
            stage.addActor(tbCraft[0]);
            stage.addActor(tbBuild[0]);
            tbInv[1].remove();
            tbJournal[1].remove();
            tbBuild[1].remove();
            tbCraft[1].remove();
            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                isInvOpen = true;
                isJournalOpen = false;
                isBuildingOpen = false;
                isCraftingOpen = false;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                isInvOpen = false;
                isJournalOpen = true;
                isBuildingOpen = false;
                isCraftingOpen = false;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                isInvOpen = false;
                isJournalOpen = false;
                isBuildingOpen = false;
                isCraftingOpen = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                isInvOpen = false;
                isJournalOpen = false;
                isBuildingOpen = true;
                isCraftingOpen = false;
            }
        }
//        System.out.println("Inv:" + isInvOpen + ", Journal: " + isJournalOpen + ", Craft: " + isCraftingOpen + ", Build: " + isBuildingOpen);

    }

    public void setupGUI() {

        //All of the GUI setup
        font = new BitmapFont();
        fStamina = 200;
        fHealth = 100;
        fThirst = 100;
        fSanity = 100;

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
        nItemNum[5] = 3; //Food
        nItemNum[6] = 0; //Torch

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

        tbsGUI = new TbsGUI("fruitBasket");
        tbItems[5] = new TbGUI("Food x " + nItemNum[5], tbsGUI, 32, 32);
        tbItems[5].setY(nItemY);
        tbItems[5].setX(nItemX);

        tbsGUI = new TbsGUI("groundTorch");
        tbItems[6] = new TbGUI("Torch x" + nItemNum[6], tbsGUI, 32, 32);
        tbItems[6].setY(nItemY - 64);
        tbItems[6].setX(nItemX);

        tbsGUI = new TbsGUI("armor");
        tbCraftItems[0] = new TbGUI("Armor \n\n\n+10 Armor \n-5 Stamina", tbsGUI, 64, 64);
        tbCraftItems[0].setY(150);
        tbCraftItems[0].setX(30);
        stage.addActor(tbCraftItems[0]);

        tbsGUI = new TbsGUI("groundTorch");
        tbCraftItems[1] = new TbGUI("Torch \n\n\n+Light \n", tbsGUI, 64, 64);
        tbCraftItems[1].setY(150);
        tbCraftItems[1].setX(106);
        stage.addActor(tbCraftItems[1]);

        tbsGUI = new TbsGUI("itemInventory");
        tbInv[1] = new TbGUI("", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("journalInventory");
        tbJournal[1] = new TbGUI("Days survived: \n\n Time played: \n\nItems Collected: ", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("craftingInventory");
        tbCraft[1] = new TbGUI("", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("buildingInventory");
        tbBuild[1] = new TbGUI("", tbsGUI, 200, 262);

        tbsGUI = new TbsGUI("Journal");
        tbJournal[0] = new TbGUI("Journal", tbsGUI, 64, 64);
        tbJournal[0].setX(64);
        stage.addActor(tbJournal[0]);

        tbsGUI = new TbsGUI("bag");
        tbInv[0] = new TbGUI("Inventory", tbsGUI, 64, 64);
        stage.addActor(tbInv[0]);

        tbsGUI = new TbsGUI("crafting");
        tbCraft[0] = new TbGUI("Crafting", tbsGUI, 64, 64);
        tbCraft[0].setX(128);
        stage.addActor(tbCraft[0]);

        tbsGUI = new TbsGUI("well");
        tbBuild[0] = new TbGUI("Building", tbsGUI, 64, 64);
        tbBuild[0].setX(192);
        stage.addActor(tbBuild[0]);

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
        
        if(fArmor > fHealth) {
        SR.setColor(Color.valueOf("#d1d1d1"));
        SR.rect(35, Gdx.graphics.getHeight() - 32, fArmor * 2, 15);
        }
        
        if (fHealth <= 40) {
            SR.setColor(Color.FIREBRICK);
        } else {
            SR.setColor(Color.RED);
        }
        SR.rect(35, Gdx.graphics.getHeight() - 32, fHealth * 2, 15);
        
        if(fArmor <= fHealth) {
        SR.setColor(Color.valueOf("#d1d1d1"));
        SR.rect(35, Gdx.graphics.getHeight() - 32, fArmor * 2, 15);
        
        }

        SR.setColor(Color.GRAY);
        SR.rect(35, Gdx.graphics.getHeight() - 32 - 7, fStaminaMax, 5);

        if (isStaminaBuffer) {
            SR.setColor(Color.ROYAL);
        } else if (!isStaminaBuffer) {
            SR.setColor(Color.LIME);
        }
        SR.rect(35, Gdx.graphics.getHeight() - 32 - 7, fStamina, 5);

        if (fThirst <= 40) {
            SR.setColor(Color.SKY);
        } else {
            SR.setColor(Color.valueOf("#0080ff"));
        }
        SR.rect(35, Gdx.graphics.getHeight() - 69, fThirst * 2, 10);
        if (fSanity <= 40) {
            SR.setColor(Color.valueOf("#ffce89"));
        } else {
            SR.setColor(Color.YELLOW);
        }
        SR.rect(35, Gdx.graphics.getHeight() - 105, fSanity * 2, 10);
        SR.end();
    }

    public void addItems() {
        if (isInvOpen) {
            for (int i = 0; i < 7; i++) {
                if (nItemNum[i] > 0) {
                    stage.addActor(tbItems[i]);
                }
            }
        } else if (!isInvOpen) {
            for (int i = 0; i < 7; i++) {
                if (nItemNum[i] >= 0) {
                    tbItems[i].remove();
                }
            }
        }
        if (isCraftingOpen) {
            if (nItemNum[2] >= 10) {
                stage.addActor(tbCraftItems[0]);
            }
            if (nItemNum[1] >= 1) {
                stage.addActor(tbCraftItems[1]);
            }
        } else if (!isCraftingOpen) {
            for (int i = 0; i < 2; i++) {
                tbCraftItems[i].remove();
            }
        }
    }

    public void daynight() {
        // Very simple if statement that will repeat count from 0..1..0..
        // if Day time make light brighter 
        if (day) {
            time += 0.0001;
            if (time > 1) {
                day = false;
                nDays++;
            }
            // if Night make light dimmer
        } else {
            time -= 0.0001;
            if (time < 0.1f) {
                day = true;
            }
        }
        // Set brightness to time
        if (time < 0.4) {
            rayHandler.setAmbientLight(time, time, colorBlue, time);
        } else {
            rayHandler.setAmbientLight(time, time, time, time);
        }
    }

    public void updateItems() {
        // Updates the items number and text field in the draw menu
        nItemsTotal = nItemNum[0] + nItemNum[1] + nItemNum[2] + nItemNum[3] + nItemNum[4];
        for (int i = 0; i < 5; i++) {
            tbItems[i].setText(sItem[i] + " x " + nItemNum[i]);
        }
        tbItems[5].setText("Food x " + nItemNum[5]);
        tbItems[6].setText("Torch x " + nItemNum[6]);
        if (nSeconds < 10) {
            tbJournal[1].setText("Days survived: " + nDays + " \n\n Time played: " + nHours
                    + ":" + nMinutes + ":0" + nSeconds + "\n\nItems Collected: " + nItemsTotal);
        } else {
            tbJournal[1].setText("Days survived: " + nDays + " \n\n Time played: " + nHours
                    + ":" + nMinutes + ":" + nSeconds + "\n\nItems Collected: " + nItemsTotal);
        }

    }

    public void gameTime() {
        //Controls for game time displayed in journal
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                nSeconds++;
                if (nSeconds == 60) {
                    nMinutes++;
                    nSeconds = 0;
                }
                if (nMinutes == 60) {
                    nHours++;
                    nMinutes = 0;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void actionSwitch() {
        //This code switches the player sprite and cursor style when you switch weapons
        fMouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        batchAction.begin();
        if (nAction == 0) {
            txSheet = new Texture("playerSprite.png");
        } else if (nAction == 5) {
            txSheet = new Texture("playerSpriteTorch.png");
            torchLight.setActive(true);
        } else if (nAction == 1) {
            txSheet = new Texture("playerSpriteSword.png");
            sprAction = new Sprite(new Texture(Gdx.files.internal("sword.png")));
            batchAction.draw(sprAction, Gdx.input.getX() - 32,
                    fMouseY - 32, sprAction.getOriginX(),
                    sprAction.getOriginY(), sprAction.getWidth() * 2, sprAction.getHeight() * 2,
                    sprAction.getScaleX(), sprAction.getScaleY(), 0);
            Gdx.input.setCursorCatched(true);
        } else if (nAction == 2) {
            txSheet = new Texture("playerSpritePick.png");
            sprAction = new Sprite(new Texture(Gdx.files.internal("pickaxe.png")));
            batchAction.draw(sprAction, Gdx.input.getX() - 32,
                    fMouseY - 32, sprAction.getOriginX(),
                    sprAction.getOriginY(), sprAction.getWidth() * 2, sprAction.getHeight() * 2,
                    sprAction.getScaleX(), sprAction.getScaleY(), 0);
            Gdx.input.setCursorCatched(true);
        } else if (nAction == 3) {
            txSheet = new Texture("playerSpriteAxe.png");
            sprAction = new Sprite(new Texture(Gdx.files.internal("axe.png")));
            batchAction.draw(sprAction, Gdx.input.getX() - 32,
                    fMouseY - 32, sprAction.getOriginX(),
                    sprAction.getOriginY(), sprAction.getWidth() * 2, sprAction.getHeight() * 2,
                    sprAction.getScaleX(), sprAction.getScaleY(), 0);
            Gdx.input.setCursorCatched(true);
        } else if (nAction == 4) {
            txSheet = new Texture("playerSpriteHam.png");
            sprAction = new Sprite(new Texture(Gdx.files.internal("hammer.png")));
            batchAction.draw(sprAction, Gdx.input.getX() - 32,
                    fMouseY - 32, sprAction.getOriginX(),
                    sprAction.getOriginY(), sprAction.getWidth() * 2, sprAction.getHeight() * 2,
                    sprAction.getScaleX(), sprAction.getScaleY(), 0);
            Gdx.input.setCursorCatched(true);
        }
        if (nAction != 5) {
            torchLight.setActive(false);
        }
        if (nAction != 2) {
            isMining = false;
        }

        fCurMouseX = Gdx.input.getX();
        fCurMouseY = Gdx.input.getY();

        batchAction.end();
    }

    public void torchLightFlicker() {
        if (torchLight.isActive()) {
            nTorchFlicker++;
            nTorchRange = ranGen.nextInt((30 - 3) + 1) + 3; // Set random time range when flicker occurs
            if (nTorchFlicker >= nTorchRange) {
                torchLight.setDistance((float) Math.random() * (5 - 4.5f) + 1); // Set random size
                nTorchFlicker = 0;
            }
        }
    }

    public void Mining() {
        if (isMining) {
            txSheet = new Texture("playerActionMine.png");
            nFrame++;
            nGrowth++;
            SR.begin(ShapeRenderer.ShapeType.Filled);
            SR.setColor(1, 1, 0, 1);
            SR.rect(525, 50, nGrowth * 2, 20);
            SR.end();
            if (nGrowth == 60) {
                isSane = false;
                nInsanity++;
                if (nInsanity > 10) {
                    fSanity -= 0.5f;
                }
                //isCollecting = false;
                nLuck = random.nextInt(500) + 1;
                if (nLuck >= 1) {
                    nItemNum[0]++;
                    if (nLuck >= 1 && nLuck <= 150) {
                        nItemNum[2]++;
                    }
                    if (nLuck >= 151 && nLuck <= 156) {
                        nItemNum[4]++;
                    }
                    if (nLuck >= 157 && nLuck <= 207) {
                        nItemNum[3]++;
                    }
                }
                nGrowth = 0;
            }
        } else {
            nGrowth = 0;
        }

    }

    public void hitDamage(float fDamage) {
        if (fArmor > 0) {
            fArmor -= fDamage;
            if (fArmor < 0) {
                fArmor = 0;
            }
        } else if (fHealth > 0) {
            fHealth -= fDamage;
            if (fHealth < 0) {
                fHealth = 0;
            }
        }
    }

    public void sanity() {
        if (isSane == true) {
            nSane++;
            if (nSane >= 1000 && fSanity <= 100) {
                fSanity += 0.01f;
            }
        } else if (isSane == false) {
            nSane = 0;
        }
    }
    public void thirst() {
        nThirstBuffer++;
        if(nThirstBuffer >= 3000) {
            nThirstBuffer = 3000;
            if(fThirst >= 0) {
                fThirst -= 0.005f;    
            }
            if(fHealth >= 0 && fThirst <= 0) {
                fHealth -= 0.01f;
            }
        }
    }

    public void Death() {
        if (fHealth <= 0) {
            Gdx.input.setCursorCatched(false);
            nAction = 0;
            sFinalTime = "You spent " + nHours + " hr, " + nMinutes + " min and " + nSeconds + " sec in Dave's Story";
            sFinalDays = "You survived " + nDays + " days.";
            sFinalItems = "You had " + nItemsTotal + " items.";
            
            nHours = 0;
            nMinutes = 0;
            nSeconds = 0;
            for(int i = 0; i < nItemNum.length; i++) {
                nItemNum[i] = 0;
            }
            fHealth = 100;
            fArmor = 0;
            fSanity = 100;
            fStamina = 100;
            fThirst = 100;
            
            gamMenu.updateState(2);
        }
    }
}
