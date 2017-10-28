import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
/**
 * Contains info and directions to construct the starting menu
 * 
 * @Lincoln Updike 
 * @6.10.17
 */
public class Menu
{
    public Rectangle playButton = new Rectangle(Game.WIDTH/4+120,150,100,50);
    public Rectangle quitButton = new Rectangle(Game.WIDTH/4+120,250,100,50);
    /**
     * renders a menu screen
     */
    public void render(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        Font fnt1 = new Font("arial", Font.BOLD, (int)(50*Game.FONT_SCALE));
        g.setFont(fnt1);
        g.setColor(Color.white);
        g.drawString("HEADS UP POKER", Game.WIDTH/6, 100);
        g2D.draw(playButton);
        g2D.draw(quitButton);
        Font fnt2 = new Font("arial", Font.BOLD, (int)(30*Game.FONT_SCALE));
        //g.setFont(fnt2);
        Game.drawButtonText(playButton, "PLAY", fnt2, g);
        //g.drawString("PLAY",playButton.x + 13,playButton.y + 37);
        Game.drawButtonText(quitButton, "QUIT", fnt2, g);
        //g.drawString("QUIT",quitButton.x + 13,quitButton.y + 37);
    }
}
