import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
/**
 * Controls changes/input made and given by the mouse of the player of the game
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class MouseInput implements MouseListener
{
    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * if the mouse is pressed in a certain area, with the game in a certain stage, the desired 
     * operation will be carried out
     */
    public void mousePressed(MouseEvent e)
    {
        Rectangle playButton = new Rectangle(Game.WIDTH/4+120,150,100,50);
        Rectangle quitButton = new Rectangle(Game.WIDTH/4+120,250,100,50);
        Rectangle call = new Rectangle(20,Game.HEIGHT-70,100,50);
        Rectangle fold = new Rectangle(140,Game.HEIGHT-70,100,50);
        Rectangle raise = new Rectangle(20,Game.HEIGHT-140,100,50);
        int mx = e.getX();
        int my = e.getY();
        if (Game.State == Game.STATE.MENU)
        {
            if (mx >= playButton.getX() && mx<= playButton.getX() + playButton.getWidth())
            {
                if (my >= playButton.getY() && my<= playButton.getY() + playButton.getHeight())
                {
                    Game.State = Game.STATE.GAME;
                    Game.user.chips = 1000;
                    Game.cpu.chips = 1000;
                    Game.stage = 0;
                }
            }
            if (mx >= quitButton.getX() && mx<= quitButton.getX() + quitButton.getWidth())
            {
                if (my >= quitButton.getY() && my<= quitButton.getY() + quitButton.getHeight())
                {
                    System.exit(0);
                }
            }
        }
        else if (Game.State == Game.STATE.GAME)
        {
            if (mx >= call.getX() && mx<= call.getX() + call.getWidth())
            {
                if (my >= call.getY() && my<= call.getY() + call.getHeight())
                {
                    if (Game.user.isTurn && System.currentTimeMillis() - Game.timeCPU > 2500)
                        Game.user.call();
                }
            }
            if (mx >= fold.getX() && mx<= fold.getX() + fold.getWidth())
            {
                if (my >= fold.getY() && my<= fold.getY() + fold.getHeight())
                {
                    if (Game.user.isTurn && System.currentTimeMillis() - Game.timeCPU > 2500)
                        Game.user.fold();
                }
            }
            if (mx >= raise.getX() && mx<= raise.getX() + raise.getWidth())
            {
                if (my >= raise.getY() && my<= raise.getY() + raise.getHeight())
                {
                    if (Game.user.isTurn && System.currentTimeMillis() - Game.timeCPU > 2500)
                    {
                        try {
                            int ans = Integer.parseInt( JOptionPane.showInputDialog("How much"));
                            Game.user.raise(ans);
                        } catch (Exception excep){}
                    }
                }
            }
        }
        else if (Game.State == Game.STATE.END_HAND || Game.State == Game.STATE.FOLD)
        {
            if (mx >= call.getX() && mx<= call.getX() + call.getWidth())
            {
                if (my >= call.getY() && my<= call.getY() + call.getHeight())
                {
                    Game.stage = 0;
                    Game.State = Game.STATE.GAME;
                }
            }
        }
        else if (Game.State == Game.STATE.ALL_IN)
        {
            if (mx >= call.getX() && mx<= call.getX() + call.getWidth())
            {
                if (my >= call.getY() && my<= call.getY() + call.getHeight())
                {
                    Game.stage++;
                }
            }
        }
        else if (Game.State == Game.STATE.END_GAME)
        {
            if (mx >= call.getX() && mx<= call.getX() + call.getWidth())
            {
                if (my >= call.getY() && my<= call.getY() + call.getHeight())
                {
                    Game.State = Game.STATE.MENU;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }
}
