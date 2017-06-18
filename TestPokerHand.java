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
    private int ITERATIONS = 100;
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
        Card a = new Card(1,9);
        hand.add(a);
        Card b = new Card(2,10);
        hand.add(b);
        Card c = new Card(3,11);
        hand.add(c);
        Card d = new Card(4,12);
        hand.add(d);
        Card e = new Card(3,13);
        hand.add(e);
        Card f = new Card(2,3);
        hand.add(f);
        Card g = new Card(1,3);
        hand.add(g);
        PokerHand p = new PokerHand(hand);
        int x = p.hasWhat();
        System.out.println(x);
    }
    /**
     * used to test the method in PokerHand that determines which of two hands beats the other
     */
    public void test_Beats()
    {
        List<Card> hand1 = new ArrayList<Card>();
        List<Card> hand2 = new ArrayList<Card>();
        //hand1
        Card a1 = new Card(1,14);
        hand1.add(a1);
        Card b1 = new Card(1,10);
        hand1.add(b1);
        Card c1 = new Card(1,11);
        hand1.add(c1);
        Card d1 = new Card(1,12);
        hand1.add(d1);
        Card e1 = new Card(1,13);
        hand1.add(e1);
        Card f1 = new Card(2,3);
        hand1.add(f1);
        Card g1 = new Card(1,3);
        hand1.add(g1);
        //hand2
        Card a2 = new Card(1,14);
        hand2.add(a2);
        Card b2 = new Card(1,10);
        hand2.add(b2);
        Card c2 = new Card(1,11);
        hand2.add(c2);
        Card d2 = new Card(1,12);
        hand2.add(d2);
        Card e2 = new Card(1,13);
        hand2.add(e2);
        Card f2 = new Card(2,3);
        hand2.add(f2);
        Card g2 = new Card(1,3);
        hand2.add(g2);
        PokerHand p1 = new PokerHand(hand1);
        PokerHand p2 = new PokerHand(hand2);
        System.out.println(p1.beats(p2));
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
            PokerHand i = new PokerHand(hand);
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
            if (x%10 == 0)
            System.out.println(x/10);
        }
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
