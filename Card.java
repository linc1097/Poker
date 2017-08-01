import java.awt.image.BufferedImage;
import java.io.IOException;
/**
 * A Card which is made of two ints that represent suit and value.
 * 
 * @Lincoln Updike
 * @2.9.17
 */
public class Card
{
    //suits
    final static int CLUBS = 1;
    final static int SPADES = 2;
    final static int HEARTS = 3;
    final static int DIAMONDS = 4;
    //cards
    final static int ACE = 14;
    final static int KING = 13;
    final static int QUEEN = 12;
    final static int JACK = 11;
    final static int TEN = 10,NINE = 9,EIGHT = 8,SEVEN = 7,SIX = 6,FIVE = 5,FOUR = 4,THREE = 3,TWO = 2;

    private int suit;
    private int rank;
    private int prime;
    /**
     * takes the int values of rank and suit, and initializes the CardSheet, which holds a picture of all
     * the cards in the deck
     */
    public Card(int theSuit, int theRank)
    {
        suit = theSuit;
        rank = theRank;
        prime = primeRep(rank);
    }
    
    public int primeRep(int x)
    {
        return Game.primes[x];
    }
    
    /**
     * returns the value in blackjack(face cards are 10)(Aces are always one) for a card
     */
    public int getRank()
    {
        return rank;
    }
   
    public void setRank(int theRank)
    {
        rank = theRank;
    }
    
    public int getPrime()
    {
        return prime;
    }
    
    /**
     * Returns a string that is the rank for the to String method.
     */
    public String getStringRank()
    {
        if (rank == ACE)
            return("Ace of ");
        else if (rank == KING)
            return("King of ");
        else if (rank == QUEEN)
            return("Queen of ");
        else if (rank == JACK)
            return("Jack of ");
        else if (rank == TEN)
            return("Ten of ");
        else if (rank == NINE)
            return("Nine of ");
        else if (rank == EIGHT)
            return("Eigth of ");
        else if (rank == SEVEN)
            return("Seven of ");
        else if (rank == SIX)
            return("Six of ");
        else if (rank == FIVE)
            return("Five of ");
        else if (rank == FOUR)
            return("Four of ");
        else if (rank == THREE)
            return("Three of ");
        else if (rank == TWO)
            return("Two of ");
        else
            return("");
    }
    
    /**
     * converts the int value into a string that is a suit (spades, hearts, etc.) for the to String method.
     */
    public String getStringSuit()
    {
        if (suit == DIAMONDS)
            return "diamonds";
        else if (suit == HEARTS)
            return "hearts";
        else if (suit == SPADES)
            return "spades";
        else if (suit == CLUBS)
            return "clubs";
        else
            return "";

    }
    
    public int getSuit()
    {
        return suit;
    }
    
    /**
     * returns true if a card has the same suit as the other card and the blackjack values are the same.
     */
    public boolean equals(Card other)
    {
        if (suit == other.getSuit()&&rank == other.getRank())
        return true;
        else
        return false;
    }
    
    /**
     * prints the value of a cards in the format rank of suit. ex: ace of spades
     */
    public String toString()
    {
        return getStringRank() + getStringSuit();
    }

    /**
     * returns a clone of the card
     */
    public Card clone()
    {
        Card c = new Card(suit,rank);
        return c;
    }

}
