/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dave.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 *
 * @author Thomas
 */
    //Styling for hotbar buttons
    public class TbsHotbar extends TextButton.TextButtonStyle {

        Skin skin = new Skin();
        TextureAtlas buttonAtlas;

        public TbsHotbar(String sIcon) {
            BitmapFont font = new BitmapFont();
            skin.add("default", font);
            buttonAtlas = new TextureAtlas(Gdx.files.internal("gameAssets.atlas"));
            skin.addRegions(buttonAtlas);

            this.up = skin.getDrawable(sIcon);
            this.down = skin.getDrawable(sIcon + "Pressed");
            this.over = skin.getDrawable(sIcon + "Hover");
            //this.checked = skin.getDrawable("inventoryIcon");
            this.font = skin.getFont("default");
        }
    }
