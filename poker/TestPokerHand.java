 

import java.util.List;
import java.util.ArrayList;
/**
 * Used to test and debug the PokerHand method, which is responsible for evaluating
 * and comparing hands in poker
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class TestPokerHand
{
    private int RF = 0;
    private int SF = 0;
    //four of a kinds
    private int FoK = 0;
    //full houses
    private int FH = 0;
    //flushes
    private int F = 0;
    //straights
    private int S = 0;
    //three of a kinds
    private int ToK = 0;
    //two pairs
    private int TP = 0;
    //pairs
    private int P = 0;
    //high cards
    private int HC = 0;
    private int ITERATIONS = 1000000;
    /**
     * runs one million hands and records how many times each type of hand is gotten
     */
    public void run_Million_Hands()
    {
        countHands();
        printResults();
    }

    /**
     * used to test the method in PokerCards hasWhat(), which determines what the hand holds, ex: two pair
     */
    public void test_Hand()
    {
        List<Card> hand = new ArrayList<Card>();
        Card a = new Card(1,13);
        hand.add(a);
        Card b = new Card(2,13);
        hand.add(b);
        Card c = new Card(3,13);
        hand.add(c);
        Card d = new Card(4,2);
        hand.add(d);
        Card e = new Card(1,2);
        hand.add(e);
        PokerHandOriginal p = new PokerHandOriginal(hand);
        int x = p.hasWhat();
        System.out.println(x);
    }

    /**
     * evaluates one million hands, recording how many times each hand is gotten
     */
    public void countHands()
    {
        Deck deck = new Deck();
        List<Card> hand = new ArrayList<Card>();
        int handVal;
        for (int x = 0;x<ITERATIONS;x++)
        {
            deck.shuffle();
            hand.clear();
            for (int y = 0;y<7;y++)
                hand.add(deck.dealCard());
            PokerHandOriginal i = new PokerHandOriginal(hand);
            handVal = i.hasWhat();
            if (handVal == 9)
                SF++;
            else if (handVal == 8)
                FoK++;
            else if (handVal == 7)
                FH++;
            else if (handVal == 6)
                F++;
            else if (handVal == 5)
                S++;
            else if (handVal == 4)
                ToK++;
            else if (handVal == 3)
                TP++;
            else if (handVal == 2)
                P++;
            else
                HC++;
            if (x%100 == 0)
                System.out.println(x/100);
        }
        printResults();
    }

    public void testSpeed()
    {
        Deck deck = new Deck();
        deck.shuffle();
        List<Card> hand1 = new ArrayList<Card>();
        List<Card> hand2 = new ArrayList<Card>();
        int count = 0;
        int wins = 0;
        int loses = 0;
        int ties = 0;
        int value;
        hand1.add(deck.dealCard());
        hand1.add(deck.dealCard());
        for (int x = 0;x<3;x++)
        {
            Card c = deck.dealCard();
            hand1.add(c);
            hand2.add(c);
        }
        Card card = new Card(3,3);
        hand1.add(card);
        hand1.add(card);
        hand2.add(card);
        hand2.add(card);
        hand2.add(card);
        hand2.add(card);
        for (int a = 0;a<deck.size();a++)
        {
            hand1.set(5,deck.get(a));
            hand2.set(3,deck.get(a));
            List<Card> z = clone(deck.getDeck());
            z.remove(a);
            for (int b = 0;b<z.size();b++)
            {
                hand1.set(6,z.get(b));
                hand2.set(4,z.get(b));
                List<Card> y = clone(z);
                y.remove(b);
                for (int c = 0;c<y.size();c++)
                {
                    hand2.set(5,y.get(c));
                    List<Card> x = clone(y);
                    x.remove(c);
                    for (int d = 0;d<x.size();d++)
                    {
                        hand2.set(6,x.get(d));
                        PokerHandOriginal self = new PokerHandOriginal(hand1);
                        PokerHandOriginal opponent = new PokerHandOriginal(hand2);
                        value = self.beats(opponent);
                        if (value == 1)
                            wins++;
                        else if (value == 0)
                            loses++;
                        else
                            ties++;
                        if (count % 42807 == 0)
                            System.out.println(count/42807);
                        count++;
                    }
                }
            }
        }
        System.out.println("wins: " +(double)wins/count); 
        System.out.println("loses: " +(double)loses/count); 
        System.out.println("ties: " +(double)ties/count); 
        System.out.println(hand1);
        System.out.println(hand2);
    }

    public List<Card> clone (List<Card> cards)
    {
        List<Card> list = new ArrayList<Card>();
        for (int i = 0;i<cards.size();i++)
        {
            list.add(cards.get(i));
        }
        return list;
    }

    /**
     * prints the percentages of each hand being the best possible hand
     */
    public void printResults()
    {
        System.out.println("Straight Flushes: " + (double)SF/ITERATIONS);
        System.out.println("Four of a Kinds: " + (double)FoK/ITERATIONS);
        System.out.println("Full Houses: " + (double)FH/ITERATIONS);
        System.out.println("Flushes: " + (double)F/ITERATIONS);
        System.out.println("Straights: " + (double)S/ITERATIONS);
        System.out.println("Three of a Kinds: " + (double)ToK/ITERATIONS);
        System.out.println("Two Pairs: " + (double)TP/ITERATIONS);
        System.out.println("Pairs: " + (double)P/ITERATIONS);
        System.out.println("High Card: " + (double)HC/ITERATIONS);
    }
}
