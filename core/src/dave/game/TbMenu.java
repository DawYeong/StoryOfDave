package dave.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by luke on 2016-04-13.
 */

public class TbMenu extends TextButton{
    String sText;
    public TbMenu(String _sText, TextButtonStyle _tbs) {
        super(_sText, _tbs);
        sText = _sText;
        this.setSize(175, 35);
        this.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                System.out.println(sText);
            }
        });
    }}


