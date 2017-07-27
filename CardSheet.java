import java.awt.image.BufferedImage;
import java.io.IOException;
/**
 * Contains an image of all the cards in the deck laid out in a grid
 */
public class CardSheet
{
    private BufferedImage image;
    private int[] suits = {0,1,2,0,3,0,0,0,4};
    /**
     * initializes the card sheet with the picture of the cards laid out on a grid
     */
    public CardSheet()
    {
        ImageLoader x = new ImageLoader();
        try{
            image = x.loadImage("pictures/cards.png.png");
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    /**
     * returns an image of a card of the desired rank and suit
     */
    public BufferedImage grabCardImage(int rank, int suit)
    {
        if (rank == 14)//aces are represented by the int 14, but appear on the grid before the 2
        rank = 1;
        suit = suits[suit];
        BufferedImage img = image.getSubimage((rank*73)-73,(suit*98)-98,73,98);
        return img;
    }
}
