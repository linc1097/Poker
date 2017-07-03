import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class HandRep here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HandToInt
{
    int value = 1;
    private List<Card> hand;
    
    public HandToInt(List<Card> cards)
    {
        for (int x = 0;x<cards.size();x++)
        {
            value *= primeRep(cards.get(x).getRank());
        }
        if (hasFlush(cards))
        value*=43;
        hand = cards;
    }
    
    public String toString()
    {
        return "" + numValue();
    }
    
    public boolean hasFlush(List<Card> cards)
    {
        int suit = cards.get(0).getSuit();
        for (int x = 1;x<cards.size();x++)
        {
            if (cards.get(x).getSuit()!=suit)
            return false;
        }
        return true;
    }
    
    public int primeRep(int x)
    {
        return Game.primes[x];
    }
    
    public int numValue()
    {
       return value;
    }
    
    public List<Card> getCards()
    {
        return hand;
    }
}
