
package utils;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dave.game.TbGUI;
import dave.game.TbsGUI;
import dave.game.TbsHotbar;
import java.util.Random;


public final class Constants {
    public static final float PPM = 32; //Pixels per meter
    public static Stage stage;
    public static ShapeRenderer SR;
    public static SpriteBatch batch, batchAction;
    public static BitmapFont screenName;
    public static Texture img;
    public static Sprite sprVlad, sprLogic;
    public static Random ranGen = new Random();
    public static Texture txSheet, txBackground, txTemp, txOne, txShadow, txWater, txInvIcon;
    public static Animation araniVlad[];
    public static TextureRegion trTemp, trHouse;// a single temporary texture region
    public static boolean[] arbKeys = new boolean[512];
    public static int fW, fH, fSx, fSy; // height and width of SpriteSheet image - and the starting upper coordinates on the Sprite Sheet
    public static int nFrame, nPos, nBar = 0, nBarWidth = 0;
    public static final int gameWidth = 200;
    public static final int gameHeight = 100;
    public static int nX, nY;

    public static float fSpeed;
    public static float fAniSpeed;
    public static float fInvPosX, fInvPosY;
    public static  Box2DDebugRenderer b2dr;
    public static  OrthographicCamera camera;
    public static  OrthogonalTiledMapRenderer tmr, treeRender;
    public static  TiledMap map, trees;
    public static  World world;
    public static  Body player, platform;

    public static TbsHotbar tbsHotbar;
    public static TbsGUI tbsGUI;
    public static TbGUI tbGUI, tbHotbar[] = new TbGUI[4], tbInv[] = new TbGUI[2], tbItems[] = new TbGUI[5],
            tbJournal[] = new TbGUI[2];
    public static int nInvY, nInvX, nItemY, nItemX;
    public static float fStamina, fHealth, fThirst, fSanity;
    public static int nAction;
    public static int nItemNum[] = new int[5];
    public static boolean isInvOpen = false;
    public static boolean isJournalOpen = false;
    public static InputMultiplexer multiplexer;
    public static boolean isStaminaBuffer = false;
    public static BitmapFont font;
    public static String sItem[] = new String[5];
    public static String sIcon[] = new String[4];

    public static  RayHandler rayHandler;
    public static  PointLight torchLight;
    public static  float time;
    public static  boolean day = false;
    public static int nTimeFrame, nSeconds, nMinutes, nHours, nDays, nItemsTotal;
    //Day Night cycle source: http://pastie.org/private/8qpksvi8wy9gntolvtya

    public static Sprite sprAction;
    public static float fPlayX, fPlayY, fHitRadX, fHitRadY, fRad, fMouseY, fCurMouseX, fCurMouseY;
    public static int nIconHit = 0, nGrowth = 0;
    public static boolean isRadHit = false, isCollecting = false, isClicked;

    
}
