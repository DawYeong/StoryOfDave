/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dave.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *
 * @author Thomas
 */
    public class TbGUI extends TextButton {

        String sText;

        public TbGUI(String _sText, TextButton.TextButtonStyle _tbs, int nW, int nH) {
            super(_sText, _tbs);
            sText = _sText;
            this.setSize(nW, nH);
            this.addListener(new ClickListener() {
                public void clicked(InputEvent e, float x, float y) {

                }
            });
        }
    }
