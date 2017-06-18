import java.util.List;
import java.util.ArrayList;
/**
 * Used to evaluate and compare poker hands
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class PokerHand
{
    //contains all the cards that are initially put in the hand (cards may be removed if certain methods are called)
    //after the hand is evaluated, the cards that do not make up the hand will be stored here
    //for example if the hand has a pair, the remaining cards will be stored here, excluding the two that made up
    //the pair, this is so these cards can be looked at when determining kickers and high cards.
    private List<Card> hand = new ArrayList<Card>();
    //contains the important cards - ex: if there is a two pair, the four cards that make up the 
    //two pair will be moved from hand to impCards
    private List<Card> impCards = new ArrayList<Card>();
    private int flush;

    public static final int HIGH_CARD = 0;
    public static final int PAIR = 1;
    public static final int TWO_PAIR = 2;
    public static final int THREE_OF_A_KIND = 3;
    public static final int STRAIGHT = 4;
    public static final int FLUSH = 5;
    public static final int FULL_HOUSE = 6;
    public static final int FOUR_OF_A_KIND = 7;
    public static final int STRAIGHT_FLUSH = 8;
    /**
     * constructor, takes an List of cards, the hand to be evaluated. Can contain up to 7 cards
     */
    public PokerHand(List<Card> c)
    {
        hand = c;
    }
    /**
     * returns the List hand
     */
    public List<Card> getHand()
    {
        return hand;
    }
    /**
     * return the string representation of this class, PokerHand
     */
    public String toString()
    {
        String s = new String();
        for (Card c : hand)
        {
            s += "[" + c + "]";
        }
        return s;
    }
    /**
     * If the hand contains a three of a kind and two pairs, this method is called to remove the lower
     * of the two pairs from impCards so that what is left are te cards relevant to the full house
     */
    public void trimImpCards()
    {
        if (impCards.size()==7)
        {
            impCards.remove(impCards.size()-1);
            impCards.remove(impCards.size()-1);
        }
    }
    /**
     * sorts hand from lowest (2) to highest (ace)
     */
    public void selectionSort()
    {
        for (int x = 0;x<hand.size();x++)
        {
            int smallest = 20;
            int place = x;
            for (int i = x; i<hand.size();i++)
            {
                if (hand.get(i).getRank()<smallest)
                {
                    place = i;
                    smallest = hand.get(i).getRank();
                }
            }
            Card c = hand.get(x);
            hand.set(x,hand.get(place));
            hand.set(place,c);
        }
    }
    /**
     * returns an int representing what the hand contains
     */
    public int hasWhat()
    {
        selectionSort();
        flush = hasFlush();
        if (hasStraightFlush())
            return STRAIGHT_FLUSH;
        else if (hasStraight(hand,false))
            return STRAIGHT;
        else if (flush!=0)
            return FLUSH;
        else if (hasFourOfAKind())
            return FOUR_OF_A_KIND;
        else 
        {
            boolean t1 = hasThreeOfAKind();
            boolean t2 = hasThreeOfAKind();
            boolean p1 = hasPair();
            boolean p2 = hasPair();
            if ((t1&&t2)||(t1&&p1))
            {
                trimImpCards();
                return FULL_HOUSE;
            }
            else if (t1)
                return THREE_OF_A_KIND;
            else if (p1&&p2)
                return TWO_PAIR;
            else if (p1)
                return PAIR;
        }
        return HIGH_CARD;
    }

    public List<Card> impCards()
    {
        return impCards;
    }
    /**
     * returns true if the hand has a pair, moves the pair from hand to impCards
     */
    public boolean hasPair()
    {
        for (int x = hand.size()-1;x>0;x--)
        {
            if (hand.get(x-1).getRank()==hand.get(x).getRank())
            {
                impCards.add(hand.remove(x));
                impCards.add(hand.remove(x-1));
                return true;
            }
        }
        return false;
    }
    /**
     * returns true if the hand has a four of a kind, moves the four of a kind from hand to impCards
     */
    public boolean hasFourOfAKind()
    {
        for (int x = hand.size()-1;x>2;x--)
        {
            if (hand.get(x-1).getRank()==hand.get(x).getRank())
                if (hand.get(x-2).getRank() == hand.get(x-1).getRank())
                    if (hand.get(x-3).getRank() == hand.get(x-2).getRank())
                    {
                        impCards.add(hand.remove(x));
                        impCards.add(hand.remove(x-1));
                        impCards.add(hand.remove(x-2));
                        impCards.add(hand.remove(x-3));
                        return true;
                    }
        }
        return false;
    }
    /**
     * returns true if the hand has a three of a kind, and moves the three of a kind from hand to impCards
     */
    public boolean hasThreeOfAKind()
    {
        for (int x = hand.size()-1;x>1;x--)
        {
            if (hand.get(x-2).getRank() == hand.get(x-1).getRank() && hand.get(x-1).getRank() == hand.get(x).getRank())
            {
                impCards.add(hand.remove(x));
                impCards.add(hand.remove(x-1));
                impCards.add(hand.remove(x-2));
                return true;
            }
        }
        return false;
    }
    /**
     * returns true if hand contains a card with a rank (integer from 2-14, 14 being an ace) of the int given
     */
    public boolean hasRank(int rank, List<Card> c)
    {
        for (int x = 0;x<c.size();x++)
        {
            if (c.get(x).getRank()==rank)
                return true;
        }
        return false;
    }
    /**
     * returns true if the given List of cards contains a straight, if the method is being called on
     * the impCards method to determine if cards making up a flush contain a straight, the boolean is set to true
     */
    public boolean hasStraight(List<Card> c, boolean straightFlushCheck)
    {
        for (int x = 0;x<c.size()-4;x++)
        {
            int start = c.get(x).getRank();
            if (start>10)
            {
                ;
            }
            else if (start == 2)
            {
                if (hasRank(3,c)&&hasRank(4,c)&&hasRank(5,c)&&hasRank(14,c))
                {
                    if (straightFlushCheck)
                    {
                        List<Card> temp = new ArrayList<Card>();
                        temp.addAll(c);
                        impCards.clear();
                        impCards.add(temp.get(x));
                    }
                    else
                        impCards.add(c.get(x));
                    return true;
                }
            }
            else
            {
                if (hasRank(start+1,c)&&hasRank(start+2,c)&&hasRank(start+3,c)&&hasRank(start+4,c))
                {
                    if (straightFlushCheck)
                    {
                        List<Card> temp = new ArrayList<Card>();
                        temp.addAll(c);
                        impCards.clear();
                        impCards.add(temp.get(x));
                    }
                    else
                        impCards.add(c.get(x));
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * returns the value of the suit of the flush if hand contains a 
     * flush and 0 if there is no flush and moves the cards of the flush suit into impCards
     */
    public int hasFlush()
    {
        int CLUBS = 0;
        int SPADES = 0;
        int HEARTS = 0;
        int DIAMONDS = 0;
        int check = 0;
        for (int x = 0;x<hand.size();x++)
        {
            check = hand.get(x).getSuit();
            if (check == Card.CLUBS)
                CLUBS++;
            else if (check == Card.SPADES)
                SPADES++;
            else if (check == Card.HEARTS)
                HEARTS++;
            else
                DIAMONDS++;
            }  
        if (CLUBS>4)
        return Card.CLUBS;
        if (SPADES>4)
        return Card.SPADES;
        if (HEARTS>4)
        return Card.HEARTS;
        if (DIAMONDS>4)
        return Card.DIAMONDS;
        return 0;
    }
    /**
     * returns true if hand has a straight flush and moves the lowest card of the straight into impCards
     */
    public boolean hasStraightFlush()
    {
        if (flush!=0)
        {
            for (int x = hand.size()-1;x>=0;x--)
            {
                if (hand.get(x).getSuit() == flush)
                    impCards.add(hand.get(x));
            }
            return hasStraight(impCards,true);
        }
        else
            return false;
    }
    /**
     * returns the rank of the card in impCards that is the given integer far in the list
     * however, duplicates are skipped, so 1,1,2,2 with a 2 passed in returns 2
     */
    public int impCardRank(int i)
    {
        int rank;
        int lastRank = 20;
        for (int x = 0;x<impCards.size();x++)
        {
            rank = impCards.get(x).getRank();
            if (rank!=lastRank)
            {
                i--;
                if (i==0)
                    return rank;
            }
            else
            {
                lastRank = rank;
            }
        }
        return 0;
    }
    /**
     * returns the same value as impCardRank() but from hand, which contains cards that may be pertinent
     * to tie breaking and high cards
     */
    public int highCardRank(int i)
    {
        int rank;
        int lastRank = 20;
        for (int x = hand.size()-1;x>=0;x--)
        {
            rank = hand.get(x).getRank();
            if (rank!=lastRank)
            {
                i--;
                if (i==0)
                    return rank;
            }
            else
            {
                lastRank = rank;
            }
        }
        return 0;
    }

    /**
     * returns 1 if this beats the given hand
     * returns 0 if the given hand beats this
     * returns 2 if tie
     */
    public int beats(PokerHand other)
    {
        int otherHand = other.hasWhat();
        int myHand = this.hasWhat();
        if (otherHand > myHand)
            return 0;
        else if (myHand > otherHand)
            return 1;
        else
        {
            if (myHand == STRAIGHT_FLUSH)
            {
                if (impCardRank(1)>other.impCardRank(1))
                    return 1;
                if (impCardRank(1)<other.impCardRank(1))
                    return 0;
                return 2;
            }
            else if (myHand == FOUR_OF_A_KIND)
            {
                if (impCardRank(1)>other.impCardRank(1))
                    return 1;
                if (impCardRank(1)<other.impCardRank(1))
                    return 0;
                if (highCardRank(1)>other.highCardRank(1))
                    return 1;
                if (highCardRank(1)<other.highCardRank(1))
                    return 0;
                return 2;
            }
            else if (myHand == FULL_HOUSE)
            {
                for (int x = 1;x<3;x++)
                {
                    if (impCardRank(x)>other.impCardRank(x))
                        return 1;
                    if (impCardRank(x)<other.impCardRank(x))
                        return 0;
                }
                return 2;
            }
            else if (myHand == FLUSH)
            {
                for (int x = 1;x<6;x++)
                {
                    if (impCardRank(x)>other.impCardRank(x))
                        return 1;
                    if (impCardRank(x)<other.impCardRank(x))
                        return 0;
                }
                return 2;
            }
            else if (myHand == STRAIGHT)
            {
                if (impCardRank(1)>other.impCardRank(1))
                    return 1;
                if (impCardRank(1)<other.impCardRank(1))
                    return 0;
                return 2;
            }
            else if (myHand == THREE_OF_A_KIND)
            {
                if (impCardRank(1)>other.impCardRank(1))
                    return 1;
                if (impCardRank(1)<other.impCardRank(1))
                    return 0;
                    for (int x = 1;x<3;x++)
                {
                    if (this.highCardRank(x)>other.highCardRank(x))
                        return 1;
                    if (this.highCardRank(x)<other.highCardRank(x))
                        return 0;
                }
                return 2;
            }
            else if (myHand == TWO_PAIR)
            {
                for (int x = 1;x<3;x++)
                {
                    if (impCardRank(x)>other.impCardRank(x))
                        return 1;
                    if (impCardRank(x)<other.impCardRank(x))
                        return 0;
                }
                if (highCardRank(1)>other.highCardRank(1))
                    return 1;
                if (highCardRank(1)<other.highCardRank(1))
                    return 0;
                return 2;
            }
            else if (myHand == PAIR)
            {
                if (impCardRank(1)>other.impCardRank(1))
                    return 1;
                if (impCardRank(1)<other.impCardRank(1))
                    return 0;
                for (int x = 1;x<4;x++)
                {
                    if (this.highCardRank(x)>other.highCardRank(x))
                        return 1;
                    if (this.highCardRank(x)<other.highCardRank(x))
                        return 0;
                }
                return 2;
            }
            else if (myHand == HIGH_CARD)
            {
                for (int x = 1;x<6;x++)
                {
                    if (this.highCardRank(x)>other.highCardRank(x))
                        return 1;
                    if (this.highCardRank(x)<other.highCardRank(x))
                        return 0;
                }
                return 2;
            }
        }
        return 2;
    }
}

