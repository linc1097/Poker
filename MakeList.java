import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * This class is used to create an array in order of worst to best five card hands, represented
 * as integer values
 * 
 * @Lincoln Updike
 * @10.14.17
 */
public class MakeList
{
    //contains the list of hands (represented by ints) in order of worst to best
    public List<HandToInt> hands = new ArrayList<HandToInt>();
    
    /**
     * prints a list of ints representing all possible five card hands in order of worst to best
     */
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
        for (int a = 2;a<15;a++)//loops that will run through every possible five card hand
        //not paying attention to which suit, only if the hand is suited or not
        {
            for (int b = 2;b<15;b++)
            {
                for (int c = 2;c<15;c++)
                {
                    for (int d = 2;d<15;d++)
                    {
                        for (int e = 2;e<15;e++)
                        {
                            if (a==b&&b==c&&c==d&&d==e)//if all card ranks in hand are equal, hand not valid
                            {
                                ;
                            }
                            else
                            {
                                if (anyAreEqual(a,b,c,d,e))//if pair or more mathing ranks, no flush possible
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
                                else //if no ranks are equal, the code is executed for flush and not flush possibilities
                                {
                                    one = new Card(1,a); //hand set with cards with no flush
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
                                    one = new Card(1,a);//same ranks, but with a flush
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
                            if (count%3712==0)//prints the list at each whole percentage way done
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
    
    /**
     * prints the list of ints representing hands, or at least what has been completed so far
     */
    public void printHands()
    {
        String list = "";
        for (int x = 0;x<hands.size();x++)
        {
            list+= "" + hands.get(x) + ", ";
            if (x%150 == 0)
            {
                list+= "\n";
            }
        }
        System.out.println(list);
    }
    /**
     * executes a binary search on the list already compiled to add a new hand in the list. The
     * new hand is placed in front of any hands it is better than and behind any hands it is worse than
     * (not in order of the integers represneting the hands)
     */
    public void addToHands(HandToInt hand)
    {
        if (hands.size() == 0)
        {
            hands.add(hand);
        }
        else
        {   //binary search and insertion into correct spot
            int top = hands.size()-1;
            int bottom = 0;
            int check = (top+bottom)/2;
            int beats = 100;
            while (check!=bottom&&beats!=2)
            {
                PokerHandOriginal a = new PokerHandOriginal(hand.getCards());
                PokerHandOriginal b = new PokerHandOriginal(hands.get(check).getCards());
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
                PokerHandOriginal a = new PokerHandOriginal(hand.getCards());
                PokerHandOriginal b = new PokerHandOriginal(hands.get(check).getCards());
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
    
    /**
     * returns a clone of the list of cards given
     */
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
