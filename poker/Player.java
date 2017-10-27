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
    protected Card[] hand = new Card[7];
    protected Card[] opponentHole = new Card[2];
    public int chips;
    public boolean isTurn = false;
    public boolean call = false;
    public boolean calling = false;
    public int raise = 0;
    public boolean fold = false;
    public int alreadyIn = 0;
    //ints containing how many times the player had raised called and folded throughout the game
    public int calls = 0;
    public int raises = 0;
    public int folds = 0;

    /**
     * Constructor for objects of class Player, takes the starting amount of chips
     */
    public Player(int num)
    {
        chips = num;
    }

    /**
     * adds the card to the hand, if already 7 cards in hand, does nothing
     */
    public void addCard(Card card)
    {
        for (int x = 0;x<hand.length;x++)
        {
            if (hand[x] == null)
            {
                hand[x] = card;
                return;
            }
        }
    }
    
    public void showOpponentCards(Card c1, Card c2)
    {
        opponentHole[0] = c1;
        opponentHole[1] = c2;
    }

    public void set(int index, Card card)
    {
        hand[index] = card;
    }

    public void clearCards()
    {
        for (int x = 0;x<hand.length;x++)
        hand[x] = null;
    }

    /**
     * returns a copy of the List conataining the cards int he players hand
     */
    public List<Card> getCards()
    {
        List<Card> x = new ArrayList<Card>();
        for (int i = 0;i<hand.length;i++)
        {
            x.add(hand[i].clone());
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
        calls++;
    }

    public void fold()
    {
        fold = true;
        folds++;
    }

    public void raise(int amount)
    {
        if (chips <= Game.bet)
            call();
        else
        {
            raise = amount;
            Game.otherPlayer(this).call = false;
            call = true;
            raises++;
        }
    }

    public void changeIsTurn()
    {
        if (isTurn)
            isTurn = false;
        else
            isTurn = true;
    }
    
    public int getNumCalls()
    {
        return calls;
    }
    
    public int getNumRaises()
    {
        return raises;
    }
    
    public int getNumFolds()
    {
        return folds;
    }
}
