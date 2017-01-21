package dave.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.Viewport;
import dave.game.screens.ScrControls;
import dave.game.screens.ScrMenu;
import dave.game.screens.ScrPlay;
import dave.game.screens.ScrGameover;


/**
 * Created by luke on 2016-04-05.
 * This is the master "game" class that contains all the screens.
 */

public class GamMenu extends Game {
    ScrMenu scrMenu;
    ScrPlay scrPlay;
    ScrGameover scrGameover;
    ScrControls scrControls;
    int nScreen; // 0 for menu, 1 for play, and 2 for game over

    
    public void updateState(int _nScreen) {
        nScreen = _nScreen;
        if ( nScreen == 0) {
            setScreen(scrMenu);
        } else if (nScreen == 1) {
            setScreen(scrPlay);
        } else if (nScreen ==2) {
            setScreen(scrGameover);
        } else if(nScreen == 3) {
            setScreen(scrControls);
        }
    }

    @Override
    public void create() {
        nScreen = 0;
        // notice that "this" is passed to each screen. Each screen now has access to methods within the "game" master program
        scrMenu = new ScrMenu(this);
        scrPlay = new ScrPlay(this);
        scrGameover = new ScrGameover(this);
        scrControls = new ScrControls(this);
        
        updateState(0);
    }

    @Override
    public void render() {
        super.render();
        
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}