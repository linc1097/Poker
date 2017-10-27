 

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 * Evaluates poker hands
 * 
 * @Lincoln Updike
 * @10.14.17
 */
public class PokerHandFast
{
    private Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    private int z;
    private int y;
    private int x;
    private int w;
    private int v;
    private int val;
    private int high;
    private int[] array = MakeFinalArray.makeArray();
    /**
     * Constructor for objects of class PokerHand1
     */
    public PokerHandFast()
    {
        map.put(new Integer(16782571),new Integer(3896));
        map.put(new Integer(20691),new Integer(4220));
        map.put(new Integer(70805),new Integer(4338));
        map.put(new Integer(2519959),new Integer(5380));
        map.put(new Integer(2357862),new Integer(5980));
        map.put(new Integer(5355),new Integer(1510));
        map.put(new Integer(2117843),new Integer(3731));
        map.put(new Integer(2167957),new Integer(2350));
        map.put(new Integer(422807),new Integer(2627));
        map.put(new Integer(260710),new Integer(3343));
    }

    /**
     * returns 1 if hand1 is better
     * 0 if hand2 is better
     * 2 if tie
     */
    public int winner(Card[] hand1, Card[] hand2)
    {
        int h1 = handValue(hand1);
        int h2 = handValue(hand2);
        if (h1>h2)
            return 1;
        else if (h2>h1)
            return 0;
        else
            return 2;
    }

    /**
     * returns the integer representation of the best five card hand in the given seven card hand
     */
    public int handValue(Card[] hand)
    {
        high = 0;
        for (int a = 0;a<3;a++)
        {
            z = hand[a].getPrime();
            for (int b = a+1;b<4;b++)
            {
                y = z*hand[b].getPrime();
                for (int c = b+1;c<5;c++)
                {
                    x = y*hand[c].getPrime();
                    for (int d = c+1;d<6;d++)
                    {
                        w = x*hand[d].getPrime();
                        for (int e = d+1;e<7;e++)
                        {
                            v = w*hand[e].getPrime();   
                            if (hasFlush(hand[a],hand[b],hand[c],hand[d],hand[e]))
                                v*=43;
                            val = rankOfHand(v);
                            if (val>high)
                                high = val;
                            v = w;
                        }
                        w = x;
                    }
                    x = y;
                }
                y = z;
            }
            z = 1;
        }
        return high;
    }

    /**
     * returns an int representing how good a hand was, the hand is represented by the given int num
     */
    public int rankOfHand(int num)
    {
        int x = array[(num%(2*1024*1024))];
        if (x == 0)
        {    
            return map.get(num);
        }
        else
            return x;
    }

    public boolean hasFlush(Card a,Card b,Card c,Card d,Card e)
    {
        int x = a.getSuit();
        return x==b.getSuit()&&x==c.getSuit()&&x==d.getSuit()&&x==e.getSuit();
    }
}
