import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 * Evaluates poker hands
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PokerHandFast
{
    private Map<Integer,Integer> map;
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

    public void testMapSpeed()
    {
        int b;
        List<Card> hand = new ArrayList<Card>();
        Card c1 = new Card(1,2);
        Card c2 = new Card(2,6);
        Card c3 = new Card(1,7);
        Card c4 = new Card(4,13);
        Card c5 = new Card(3,13);
        Card c6 = new Card(3,2);
        Card c7 = new Card(2,3);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        hand.add(c6);
        hand.add(c7);
        double time = System.currentTimeMillis();
        for (int i = 0;i<894916;i++)
        {
            /*
            b = rankOfHand(10309819);
            if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(3),hand.get(4)))
            ;
             */
            //winner(hand,hand);
            if (i%(894916/100)==0)
                System.out.println(i/(894916/100));
        }
        System.out.println((System.currentTimeMillis() - time));
    }

    public int handVal(List<Card> hand)//old, slow method
    {
        int count = 0;
        int high = 0;
        int val;
        int index = 0;
        List<Card> list = new ArrayList<Card>();
        Card card = new Card(1,2);
        for (int x = 0;x<5;x++)
        {
            list.add(card);
        }
        for (int x = 0;x<6;x++)
        {
            for (int y = x+1;y<7;y++)
            {
                for (int z = 0;z<7;z++)
                {
                    if (z!=x&&z!=y)
                    {
                        list.set(index,hand.get(z));
                        index++;
                    }
                }
                HandToInt handInt = new HandToInt(list);
                val = map.get(handInt.numValue());
                if (val>high)
                    high = val;
                index = 0;
                count++;
            }
        }
        System.out.println(count);
        return high;
    }

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
