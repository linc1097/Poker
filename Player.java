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
    protected List<Card> hand = new ArrayList<Card>();
    public int chips;
    public boolean isTurn = false;
    public boolean call = false;
    public boolean calling = false;
    public int raise = 0;
    public boolean fold = false;
    public int alreadyIn = 0;

    /**
     * Constructor for objects of class Player, takes the two cards the player is dealt to start the hand
     */
    public Player(int num)
    {
        chips = num;
        Card card = new Card(2,2);
        for (int x = 0;x<7;x++)
        {
            hand.add(card);
        }
    }

    /**
     * adds the card to the hand
     */
    public void add(Card card)
    {
        hand.add(card);
    }

    public void set(int index, Card card)
    {
        hand.set(index,card);
    }

    public void clearCards()
    {
        hand.clear();
    }

    /**
     * returns a copy of the List conataining the cards int he players hand
     */
    public List<Card> getCards()
    {
        List<Card> x = new ArrayList<Card>();
        for (int i = 0;i<hand.size();i++)
        {
            x.add(hand.get(i).clone());
        }
        return x;
    }

    public String toString()
    {
        return "" + hand;
    }

    public void call()
    {
        call = true;
        calling = true;
    }

    public void fold()
    {
        fold = true;
    }

    public void raise(int amount)
    {
        if (chips <= Game.bet || Game.otherPlayer(this).chips <= Game.bet)
            call();
        else
        {
            raise = amount;
            Game.otherPlayer(this).call = false;
            call = true;
        }
    }

    public void changeIsTurn()
    {
        if (isTurn)
            isTurn = false;
        else
            isTurn = true;
    }
}
