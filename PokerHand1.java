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
    private Map<Integer,Integer> map;

    /**
     * Constructor for objects of class PokerHand1
     */
    public PokerHand1()
    {
        HandMap handMap = new HandMap();
        map = handMap.getMap();
    }

    public int winner(List<Card> hand1, List<Card> hand2)
    {
        int h1 = handVal(hand1);
        int h2 = handVal(hand2);
        if (h1>h2)
            return 1;
        else if (h2>h1)
            return 0;
        else
            return 2;
    }
    
    public void testMap()
    {
        int a;
        int b;
        List<Card> hand = new ArrayList<Card>();
        Card c1 = new Card(1,2);
        Card c2 = new Card(2,6);
        Card c5 = new Card(1,7);
        Card c4 = new Card(4,13);
        Card c3 = new Card(3,13);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        for (int x = 0;x<9000000*21;x++)
        {
            HandToInt z = new HandToInt(hand);
            b = z.numValue();
            a = map.get(b);
            if (x%(90000*21)==0)
            System.out.println(x/(21*90000));
        }
    }

    public int handVal(List<Card> hand)
    {
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
            }
        }
        return high;
    }
}
