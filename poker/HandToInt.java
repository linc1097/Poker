import java.util.ArrayList;
import java.util.List;
/**
 * takes a list of 5 cards and converts it into an integer that is specific for every possible 5 card hand.
 * The integer only takes into account flush or no flush and the ranks of cards
 * 
 * @Lincoln Updike
 * @10.14.17
 */
public class HandToInt
{
    int value = 1;
    private List<Card> hand;
    
    /**
     * converts the hand to an int value by multiplying by a prime number for each rank of each card in the hand
     * and multiplying by another prime number if the hand is suited. This way each possible 5 card hand
     * can be represented as an infividual int.
     */
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
    
    /**
     * true if the hands given are all the same suit
     */
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
    
    /**
     * takes an int, n, representing a rank of a card and returns the nth prime number
     */
    public int primeRep(int x)
    {
        return Game.primes[x];
    }
    
    /**
     * returns the int that represents the hand
     */
    public int numValue()
    {
       return value;
    }
    
    public List<Card> getCards()
    {
        return hand;
    }
}
