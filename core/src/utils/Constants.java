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

    public static Box2DDebugRenderer b2dr;
    public static OrthographicCamera camera;
    public static OrthogonalTiledMapRenderer tmr, treeRender;
    public static TiledMap map, trees;
    public static World world;
    public static Body player, platform, shadowDave;

    public static TbsHotbar tbsHotbar;
    public static TbsGUI tbsGUI;
    public static TbGUI tbGUI, tbHotbar[] = new TbGUI[4], tbInv[] = new TbGUI[2], tbItems[] = new TbGUI[7],
            tbJournal[] = new TbGUI[2], tbCraft[] = new TbGUI[2], tbBuild[] = new TbGUI[2], tbCraftItems[] = new TbGUI[3];

    public static String sFinalTime, sFinalDays, sFinalItems;
    public static boolean oneTime = true;
}
