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
        Rectangle playButton = new Rectangle(Game.WIDTH/2+120,150,100,50);
        Rectangle helpButton = new Rectangle(Game.WIDTH/2+120,250,100,50);
        Rectangle quitButton = new Rectangle(Game.WIDTH/2+120,350,100,50);
        Rectangle call = new Rectangle(20,Game.HEIGHT*Game.SCALE-70,100,50);
        Rectangle fold = new Rectangle(140,Game.HEIGHT*Game.SCALE-70,100,50);
        Rectangle raise = new Rectangle(20, Game.HEIGHT*Game.SCALE -140,100,50);
        int mx = e.getX();
        int my = e.getY();
        if (Game.State == Game.STATE.MENU)
        {
            if (mx >= playButton.getX() && mx<= playButton.getX() + playButton.getWidth())
            {
                if (my >= playButton.getY() && my<= playButton.getY() + playButton.getHeight())
                {
                    Game.State = Game.STATE.GAME;
                }
            }
        }
        else if (Game.State == Game.STATE.GAME)
        {
            if (mx >= call.getX() && mx<= call.getX() + call.getWidth())
            {
                if (my >= call.getY() && my<= call.getY() + call.getHeight())
                {
                    Game.user.call();
                }
            }
            if (mx >= fold.getX() && mx<= fold.getX() + fold.getWidth())
            {
                if (my >= fold.getY() && my<= fold.getY() + fold.getHeight())
                {
                    Game.user.fold();
                }
            }
            if (mx >= raise.getX() && mx<= raise.getX() + raise.getWidth())
            {
                if (my >= raise.getY() && my<= raise.getY() + raise.getHeight())
                {
                    int ans = Integer.parseInt( JOptionPane.showInputDialog("How much"));
                    Game.user.raise(ans);
                }
            }
        }
        else if (Game.State == Game.STATE.END_HAND)
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
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }
}
