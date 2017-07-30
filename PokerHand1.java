import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 * Write a description of class PokerHand1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PokerHand1
{
    private Map<Integer,Integer> map = new HashMap<Integer, Integer>();
    private int z;
    private int y;
    private int x;
    private int w;
    private int v;
    private int val;
    private int high;
    private int[] array = MakeMap.makeArray();
    /**
     * Constructor for objects of class PokerHand1
     */
    public PokerHand1()
    {
        map.put(new Integer(5355), new Integer(1510));
        map.put(new Integer(2167957),new Integer(2350));
        map.put(new Integer(422807),new Integer(2627));
        map.put(new Integer(260710),new Integer(3343));
        map.put(new Integer(2117843),new Integer(3731));
        map.put(new Integer(16782571),new Integer(3896));
        map.put(new Integer(20691),new Integer(4220));
        map.put(new Integer(70805),new Integer(4338));
        map.put(new Integer(2519959),new Integer(5380));
        map.put(new Integer(2357862),new Integer(5980));
    }

    public int winner(List<Card> hand1, List<Card> hand2)
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

        int a;
        
        int b;
        List<Card> hand = new ArrayList<Card>();
        Card c1 = new Card(1,2);
        Card c2 = new Card(2,6);
        Card c3 = new Card(1,7);
        Card c4 = new Card(4,13);
        Card c5 = new Card(3,13);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        double time = System.currentTimeMillis();
        for (int x = 0;x<9000000/8*21;x++)
        {
            //HandToInt z = new HandToInt(hand);
            //b = z.numValue();
            if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(3),hand.get(4)))
                ;
            if (x%(90000*21/8)==0)
                System.out.println(x/(21*90000/8));
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

    public int handValue(List<Card> hand)
    {
        high = 0;
        for (int a = 0;a<3;a++)
        {
            z = hand.get(a).getPrime();
            for (int b = a+1;b<4;b++)
            {
                y = z*hand.get(b).getPrime();
                for (int c = b+1;c<5;c++)
                {
                    x = y*hand.get(c).getPrime();
                    for (int d = c+1;d<6;d++)
                    {
                        w = x*hand.get(d).getPrime();
                        for (int e = d+1;e<7;e++)
                        {
                            v = w*hand.get(e).getPrime();
                            if (hasFlush(hand.get(a),hand.get(b),hand.get(c),hand.get(d),hand.get(e)))
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

    public int handV(List<Card> hand)
    {
        high = 0;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime();
        if (hasFlush(hand.get(1),hand.get(2),hand.get(3),hand.get(4),hand.get(0)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(5).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(3),hand.get(5)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(3),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(4),hand.get(5)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(4).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(4),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(2),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(3),hand.get(4),hand.get(5)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(3),hand.get(4),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(3).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(3),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(1).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(1),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime();
        if (hasFlush(hand.get(0),hand.get(2),hand.get(3),hand.get(4),hand.get(5)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(2),hand.get(3),hand.get(4),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(2),hand.get(3),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(2).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(2),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(0).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(0),hand.get(3),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime();
        if (hasFlush(hand.get(1),hand.get(2),hand.get(3),hand.get(4),hand.get(5)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(1),hand.get(2),hand.get(3),hand.get(4),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(1),hand.get(2),hand.get(3),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(1).getPrime()*hand.get(2).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(1),hand.get(2),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(1).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(1),hand.get(3),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        v = hand.get(2).getPrime()*hand.get(3).getPrime()*hand.get(4).getPrime()*hand.get(5).getPrime()*hand.get(6).getPrime();
        if (hasFlush(hand.get(2),hand.get(3),hand.get(4),hand.get(5),hand.get(6)))
            v*=43;
        val = rankOfHand(v);
        if (val>high)
            high = val;
        return high;
    }
    
    public void printNums()
    {
        for (int a = 0;a<3;a++)
        {
            for (int b = a+1;b<4;b++)
            {
                for (int c = b+1;c<5;c++)
                {
                    for (int d = c+1;d<6;d++)
                    {
                        for (int e = d+1;e<7;e++)
                        {
                            System.out.println("v = hand.get("+ a +").getPrime()*hand.get("+b+").getPrime()*hand.get("
                                +c+").getPrime()*hand.get("+d+").getPrime()*hand.get("+e+").getPrime()");                        }
                    }
                }
            }
        }
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
        return x==b.getSuit()&&x==c.getSuit()&&x==d.getSuit()&&x==d.getSuit();
    }
}
