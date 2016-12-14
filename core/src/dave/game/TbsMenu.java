package dave.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by luke on 2016-04-13.
 */

public class TbsMenu extends TextButton.TextButtonStyle {
    Skin skin = new Skin();
    TextureAtlas buttonAtlas;

    public TbsMenu() {
        BitmapFont font = new BitmapFont();
        skin.add("default", font);
        buttonAtlas = new TextureAtlas(Gdx.files.internal("MenuButton.pack"));
        skin.addRegions(buttonAtlas);
        this.up = skin.getDrawable("MenuButtonUp");
        this.down = skin.getDrawable("MenuButtonDown");
        this.checked = skin.getDrawable("MenuButtonUp");
        this.over = skin.getDrawable("MenuButtonHover");
        this.font = skin.getFont("default");
    }
}