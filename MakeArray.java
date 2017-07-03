import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Write a description of class MakeArray here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MakeArray
{
    public List<HandToInt> hands = new ArrayList<HandToInt>();

    public void main()
    {
        int count = 0;
        Deck deck = new Deck();
        deck.setInOrder();
        System.out.println(deck.getDeck());
        List<Card> hand = new ArrayList<Card>();
        Card card = new Card(2,2);
        for (int x = 0;x<5;x++)
        {
            hand.add(card);
        }
        Card one;
        Card two;
        Card three;
        Card four;
        Card five;
        for (int a = 2;a<15;a++)
        {
            for (int b = 2;b<15;b++)
            {
                for (int c = 2;c<15;c++)
                {
                    for (int d = 2;d<15;d++)
                    {
                        for (int e = 2;e<15;e++)
                        {
                            if (a==b&&b==c&&c==d&&d==e)
                            {
                                ;
                            }
                            else
                            {
                                if (anyAreEqual(a,b,c,d,e))
                                {
                                    one = new Card(1,a);
                                    two = new Card(2,b);
                                    three = new Card(3,c);
                                    four = new Card(4,d);
                                    five = new Card(1,e);
                                    hand.set(0,one);
                                    hand.set(1,two);
                                    hand.set(2,three);
                                    hand.set(3,four);
                                    hand.set(4,five);
                                    addToHands(new HandToInt(clone(hand)));
                                }
                                else
                                {
                                    one = new Card(1,a);
                                    two = new Card(2,b);
                                    three = new Card(3,c);
                                    four = new Card(4,d);
                                    five = new Card(1,e);
                                    hand.set(0,one);
                                    hand.set(1,two);
                                    hand.set(2,three);
                                    hand.set(3,four);
                                    hand.set(4,five);
                                    addToHands(new HandToInt(clone(hand)));
                                    one = new Card(1,a);
                                    two = new Card(1,b);
                                    three = new Card(1,c);
                                    four = new Card(1,d);
                                    five = new Card(1,e);
                                    hand.set(0,one);
                                    hand.set(1,two);
                                    hand.set(2,three);
                                    hand.set(3,four);
                                    hand.set(4,five);
                                    addToHands(new HandToInt(clone(hand)));
                                }
                            }
                            count++;
                            if (count%3712==0)
                            {
                                printHands();
                                System.out.println(count/3712);
                                System.out.println(hands.size());
                            }
                        }
                    }
                }
            }
        }
        printHands();
    }

    public boolean anyAreEqual(int a,int b,int c,int d,int e)
    {
        if (a==b||a==c||a==d||a==e)
            return true;
        else if (b==c||b==d||b==e)
            return true;
        else if (c==d||c==e)
            return true;
        else if (d==e)
            return true;
        else
            return false;
    }

    public void printHands()
    {
        String wumbo = "";
        for (int x = 0;x<hands.size();x++)
        {
            wumbo+= "" + hands.get(x) + ", ";
            if (x%150 == 0)
            {
                wumbo+= "\n";
            }
        }
        System.out.println(wumbo);
    }

    public void addToHands(HandToInt hand)
    {
        if (hands.size() == 0)
        {
            hands.add(hand);
        }
        else
        {
            int top = hands.size()-1;
            int bottom = 0;
            int check = (top+bottom)/2;
            int beats = 100;
            while (check!=bottom&&beats!=2)
            {
                PokerHand a = new PokerHand(hand.getCards());
                PokerHand b = new PokerHand(hands.get(check).getCards());
                beats = a.beats(b);
                if (beats == 0)
                {
                    top = check;
                }
                else if (beats == 1)
                {
                    bottom = check;
                }
                check = (top+bottom)/2;
            }
            if (check==bottom)
            {
                PokerHand a = new PokerHand(hand.getCards());
                PokerHand b = new PokerHand(hands.get(check).getCards());
                beats = a.beats(b);
                if (beats == 0)
                {
                   hands.add(check,hand); 
                }
                else if (beats == 1)
                {
                    hands.add(check+1,hand);
                }
            }
        }
    }
    
    public void oddDiv()
    {
        System.out.println(5/2);
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
}
