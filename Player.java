import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Contains an arraylist of the cards the user or cpu knows it has in its hand
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class Player
{
    private List<Card> cards = new ArrayList<Card>();
    public int chips;
    public boolean isTurn = false;
    public boolean call = false;
    public int raise = 0;
    public boolean fold = false;
    public int alreadyIn = 0;

    /**
     * Constructor for objects of class Player, takes the two cards the player is dealt to start the hand
     */
    public Player(int num)
    {
        chips = num;
    }

    /**
     * adds the card to the hand
     */
    public void add(Card card)
    {
        cards.add(card);
    }
    
    public void clearCards()
    {
        cards.clear();
    }

    /**
     * returns a copy of the List conataining the cards int he players hand
     */
    public List<Card> getCards()
    {
        List<Card> x = new ArrayList<Card>();
        for (int i = 0;i<cards.size();i++)
        {
            x.add(cards.get(i).clone());
        }
        return x;
    }

    public String toString()
    {
        return "" + cards;
    }

    public void call()
    {
        call = true;
    }

    public void fold()
    {
        fold = true;
    }

    public void raise(int amount)
    {
        raise = amount;
        Game.otherPlayer(this).call = false;
        call = true;
    }
    
    public void changeIsTurn()
    {
        if (isTurn)
            isTurn = false;
        else
            isTurn = true;
    }
}
