 

import java.util.List;
import java.util.ArrayList;
/**
 * contains two arrays, one containing all 52 cards in order, one containing a shuffled version.
 * 
 * @Lincoln Updike
 * @2.9.17
 */
public class Deck
{
    //number of cards in a deck
    final int NUM_CARDS = 52;
    //number of suits in a deck of cards
    final int NUM_SUITS = 4;
    //number of ranks in a deck of cards
    final int NUM_RANKS = 14;
    final int SHUFFLE_NUMB = 500;
    private List<Card> deck = new ArrayList<Card>();
    private List<Card> allCards = new ArrayList<Card>();
    private int place = 0;

    public Deck()
    {

    }

    public void remove(Card card)
    {
        for (int x = 0;x<deck.size();x++)
        {
            if (card.equals(deck.get(x)))
                deck.remove(x);
        }
    }

    public void setInOrder()
    {
        setAllCards();
        deck = allCards;
    }

    public int size()
    {
        return deck.size();
    }

    /**
     * sets the allCards array to contain a deck of all cards in order of suit and rank.
     */
    public void setAllCards()
    {
        for (int suit = 1;suit<=NUM_SUITS;suit++)
        {
            for (int num = 2;num<=NUM_RANKS;num++)
            {
                Card card = new Card(suit,num);
                allCards.add(card);
            }
        }
    }

    /**
     * prints a shuffled deck (deck array)
     */
    public void printDeck()
    {
        for (int x = 0; x<NUM_CARDS;x++)
        {
            System.out.println(deck.get(x));
        }
    }

    public List<Card> getDeck()
    {
        return deck;
    }

    /**
     * returns a random int in the range 0 - a desired int
     */
    public int getRandNum(int high)
    {
        java.util.Random rand = new java.util.Random();
        return rand.nextInt(high);
    }

    /**
     * returns true if the deck (array deck) is empty
     */
    public boolean empty()
    {
        for (int x = 0;x<NUM_CARDS;x++)
        {
            if (deck.get(x)!=null)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * the array deck is filled with randomly placed cards that include all 52 cards in the array allCards.
     */
    public void shuffle()
    {
        deck.clear();
        setAllCards();
        int num = NUM_CARDS;
        for (int x = 0;x<NUM_CARDS;x++)
        {
            int rand = getRandNum(allCards.size());
            deck.add(allCards.get(rand));
            allCards.remove(rand);
        }
    }

    public Card get(int x)
    {
        return deck.get(x);
    }

    /**
     * takes a card from the front of the deck and makes it null, returning the card drawn.
     */
    public Card dealCard()
    {
        Card c = deck.get(0);
        deck.remove(0);
        return c;
    }
}
