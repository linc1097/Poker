import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * extends Player, has the ability to make decisions on what to do based on the information
 * the game provides it.
 * 
 * @Lincoln Updike
 * @6.24.27
 */
public class AI extends Player
{
    private int numRaises = 0;
    private Map<StartingHand, Double> map = new HashMap<StartingHand, Double>();
    private Card[] opponentHand= new Card[7];
    private PokerHandFast evaluator = new PokerHandFast();
    private double aggressive;
    private double loose;
    private int lastStage = -1;
    private boolean raised = false;
    public AI(int numChips, double a, double l)
    {
        super(numChips);
        initMap();
        aggressive = a;
        loose = l;
    }

    /**
     * returns true if the player has 4 cards of the same suit. only to be used after flop and turn
     */
    public boolean flushDraw()
    {
        int[] suits = new int[5];
        for (int x = 0;x<hand.length;x++)
        {
            if (hand[x] != null)
            {
                suits[hand[x].getSuit()]++;
            }
        }
        for (int x = 0;x<suits.length;x++)
        {
            if (suits[x]==4)
                return true;
        }
        return false;
    }

    /**
     * returns true if AI has 4 cards in a row, but not a straight, only works after flop and turn.
     */
    public boolean straightDraw()
    {
        int[] ranks = new int[6];
        for (int x = 0;x<hand.length-1;x++)
        {
            if (hand[x]!= null)
            {
                ranks[x] = hand[x].getRank();
            }
        }
        Arrays.sort(ranks);
        for (int x = 0;x<3;x++)
        {
            int first = ranks[x];
            for (int i = x+1;i<x+4;i++)
            {
                if (ranks[i] == first+1)
                {
                    if (i == x+3)
                    {
                        if (i<ranks.length-1)
                            if (ranks[i+1] == first+2)
                                return false;
                        return true;
                    }
                    else
                        first++;
                }
            }
        }
        return false;
    }

    public double winPercentage()
    {
        if (AIvAI.stage == AIvAI.PRE_FLOP)
        {
            Card c1 = hand[0];
            Card c2 = hand[1];
            return map.get(new StartingHand(c1.getRank(),c2.getRank(),c1.getSuit() == c2.getSuit()));
        }
        else
        {
            int value;
            int wins = 0;
            int loses = 0;
            int ties = 0;
            int count = 0;
            Deck deck = new Deck();
            deck.setInOrder();
            if (AIvAI.stage == AIvAI.FLOP)
            {
                for (int x = 2;x<5;x++)
                {
                    opponentHand[x] = hand[x];
                }
                for (int x = 0;x<5;x++)
                {
                    deck.remove(hand[x]);
                }
                for (int a = 0;a<deck.size()-1;a++)
                {
                    hand[5] = deck.get(a);
                    opponentHand[5] = deck.get(a);
                    List<Card> z = clone(deck.getDeck());
                    z.remove(a);
                    for (int b = a+1;b<z.size();b++)
                    {
                        hand[6] = z.get(b);
                        opponentHand[6] = z.get(b);
                        List<Card> y = clone(z);
                        y.remove(b);
                        for (int c = 0;c<y.size()-1;c++)
                        {
                            opponentHand[0] = y.get(c);
                            List<Card> x = clone(y);
                            x.remove(c);
                            for (int d = c+1;d<x.size();d++)
                            {
                                if (different(a,b,c,d))
                                {
                                    opponentHand[1] = x.get(d);
                                    value = evaluator.winner(hand,opponentHand);
                                    if (value == 1)
                                        wins++;
                                    else if (value == 0)
                                        loses++;
                                    else
                                        ties++;
                                    count ++;
                                }
                            }
                        }
                    }
                }
                System.out.println("wins: " +(double)wins/count); 
                hand[5] = null;
                hand[6] = null;
                return (double)wins/count*100;
            }
            else if (AIvAI.stage == AIvAI.TURN)
            {
                for (int x = 2;x<6;x++)
                {
                    opponentHand[x] = hand[x];
                }
                for (int x = 0;x<6;x++)
                {
                    deck.remove(hand[x]);
                }
                for (int b = 0;b<deck.size();b++)
                {
                    hand[6] = deck.get(b);
                    opponentHand[6] = deck.get(b);
                    List<Card> y = clone(deck.getDeck());
                    y.remove(b);
                    for (int c = 0;c<y.size()-1;c++)
                    {
                        opponentHand[0] = y.get(c);
                        List<Card> x = clone(y);
                        x.remove(c);
                        for (int d = c+1;d<x.size();d++)
                        {
                            if (different(b,c,d))
                            {
                                opponentHand[1] = x.get(d);
                                value = evaluator.winner(hand,opponentHand);
                                if (value == 1)
                                    wins++;
                                else if (value == 0)
                                    loses++;
                                else
                                    ties++;
                                count++;
                            }
                        }
                    }
                }
                System.out.println("wins: " +(double)wins/count); 
                hand[6] = null;
                return (double)wins/count*100;
            }
            else if (AIvAI.stage == AIvAI.RIVER)
            {
                for (int x = 2;x<7;x++)
                {
                    opponentHand[x] = hand[x];
                }
                for (int x = 0;x<7;x++)
                {
                    deck.remove(hand[x]);
                }
                for (int c = 0;c<deck.size()-1;c++)
                {
                    opponentHand[0] = deck.get(c);
                    List<Card> x = clone(deck.getDeck());
                    x.remove(c);
                    for (int d = c+1;d<x.size();d++)
                    {
                        opponentHand[1] = x.get(d);
                        value = evaluator.winner(hand,opponentHand);
                        if (value == 1)
                            wins++;
                        else if (value == 0)
                            loses++;
                        else
                            ties++;
                        count++;
                    }
                }
                System.out.println("wins: " +(double)wins/count); 
                return (double)wins/count*100;
            }
            return 0;
        }
    }

    public boolean different(int a, int b, int c, int d)
    {
        return a!=b && a!=c && a!=d && b!=c &&b!=d &&c!=d;
    }

    public boolean different (int b, int c, int d)
    {
        return b!=c &&b!=d &&c!=d;
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

    public int roundTen(double num)
    {
        String digits = String.valueOf(num);
        int index = digits.indexOf(".");
        double x = Double.parseDouble(digits.substring(index-1));
        double y = Double.parseDouble(digits.substring(0,index-1))*10;
        if (x>=5)
            x = 10;
        else
            x = 0;
        return (int)(y+x);
    }

    /**
     * returns a boolean that is true the given double (must be 0-1) percent of the time
     */
    public boolean randBool(double percentTrue)
    {
        return Math.random()<percentTrue;
    }

    public boolean func(double dep, double min, double range)
    {
        return randBool(min + range*dep);
    }

    /**
     * returns a random int in the given range (inclusive) divisible by div
     */
    public int randInt(int min, int max, int div)
    {
        return ((int)(Math.random()*(max/div)+(min/div)))*div;
    }

    public double randDoub(double min,double max)
    {
        double num = Math.random();
        num*=(max-min);
        num+=min;
        return num;
    }

    public void actPreFlop()
    {
        double percentWin = winPercentage();
        if (raised)
        {
            if (AIvAI.bet>199)
            {
                if (percentWin>77)
                {
                    if (func(aggressive,.10,.30))
                        raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                    else
                        call();
                }
                else if (percentWin>60)
                {
                    if (func(aggressive,.0,.15))
                        raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                    else if (func(loose,.0,.85))
                        call();
                    else
                        fold();
                }
                else
                {
                    if (func(loose,.0,.50))
                        call();
                    else
                        fold();
                }
            }
            else if (AIvAI.bet>100)
            {
                if (percentWin>60&&func(aggressive,.0,.35))
                    raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                else if (percentWin>77&&func(aggressive,20,80))
                    raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                else if (percentWin>77)
                    call();
                else if (percentWin>60&&func(loose,.70,.30))
                    call();
                else if (percentWin>50&&func(loose,.0,.50))
                    call();
                else if (func(loose,0,20))
                    call();
                else
                    fold();
            }
            else
            {
                if (percentWin>60&&func(aggressive,.0,.50))
                    raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                else if (percentWin>77&&func(aggressive,.10,.60))
                    raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                else if (percentWin>77)
                    call();
                else if (percentWin>60&&func(loose,.70,.30))
                    call();
                else if (percentWin>50&&func(loose,.0,.70))
                    call();
                else if (func(loose,0,20))
                    call();
                else
                    fold();
            }
        }
        else
        {
            if (AIvAI.bet == 20 && alreadyIn == 10)
            {
                if (percentWin>77)
                {
                    if (func(aggressive,.0,.90))
                        raise(randInt(50,70,10));
                    else if (func(aggressive,.55,.45))
                        raise(randInt(20,30,10));
                    else
                        call();
                }
                else if (percentWin>60)
                {
                    if (func(aggressive,.30,.60))
                        raise(randInt(20,40,10));
                    else if (func(aggressive,.10,.85))
                        raise(randInt(50,70,10));
                    else
                        call();
                }
                else if (percentWin>50)
                {
                    if (func(aggressive,.10,.70))
                        raise(randInt(20,30,10));
                    else if (func(aggressive,.0,.40))
                        raise(randInt(50,70,10));
                    else if (func(loose,.80,.20))
                        call();
                    else
                        fold();
                }
                else if (percentWin>40)
                {
                    if (func(aggressive,.0,.30))
                        raise(randInt(50,70,10));
                    else if (func(aggressive,.0,.28))
                        raise(randInt(10,30,10));
                    else if (func(loose,.35,.70))
                        call();
                    else
                        fold();
                }
                else
                {
                    if (func(aggressive,0,7))
                        raise(randInt(10,70,10));
                    else if (func(loose,10,80))
                        call();
                    else
                        fold();
                }
            }
            else if (AIvAI.bet == 20 && alreadyIn == 20)
            {
                if (percentWin>77)
                {
                    if (func(aggressive,.0,.90))
                        raise(randInt(50,70,10));
                    else if (func(aggressive,.55,.45))
                        raise(randInt(20,30,10));
                    else
                        call();
                }
                else if (percentWin>60)
                {
                    if (func(aggressive,.30,.60))
                        raise(randInt(20,40,10));
                    else if (func(aggressive,.10,.85))
                        raise(randInt(50,70,10));
                    else
                        call();
                }
                else if (percentWin>50)
                {
                    if (func(aggressive,.10,.70))
                        raise(randInt(20,30,10));
                    else if (func(aggressive,.0,.40))
                        raise(randInt(50,70,10));
                    else
                        call();
                }
                else if (percentWin>40)
                {
                    if (func(aggressive,.0,.28))
                        raise(randInt(50,70,10));
                    else if (func(aggressive,0,28))
                        raise(randInt(10,30,10));
                    else
                        call();
                }
                else
                {
                    if (func(aggressive,0,7))
                        raise(randInt(10,70,10));
                    else if (func(loose,10,80))
                        call();
                }
            }
            else 
            {
                if (AIvAI.bet>199)
                {
                    if (percentWin>77)
                    {
                        if (func(aggressive,.10,.30))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else
                            call();
                    }
                    else if (percentWin>60)
                    {
                        if (func(aggressive,.0,.15))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else if (func(loose,.0,.85))
                            call();
                        else
                            fold();
                    }
                    else
                    {
                        if (func(loose,.0,.40))
                            call();
                        else
                            fold();
                    }
                }
                else if (AIvAI.bet>69)
                {
                    if (percentWin>77)
                    {
                        if (func(aggressive,.0,.75))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else
                            call();
                    }
                    else if (percentWin>60)
                    {
                        if (func(aggressive,.0,.5))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else if (func(loose,.6,.4))
                            call();
                        else
                            fold();
                    }
                    else if (percentWin>47)
                    {
                        if (func(aggressive,.0,.33))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else if (func(loose,.5,.5))
                            call();
                        else
                            fold();
                    }
                    else
                    {
                        if (func(aggressive,.0,.12))
                            raise(roundTen((int)(AIvAI.pot*randDoub(.6,.8))));
                        else if (func(loose,.0,.4))
                            call();
                        else
                            fold();
                    }
                }
                else
                {
                    if (percentWin>77)
                    {
                        if (func(aggressive,.0,.90))
                            raise(randInt(50,70,10));
                        else if (func(aggressive,.55,.45))
                            raise(randInt(20,30,10));
                        else
                            call();
                    }
                    else if (percentWin>60)
                    {
                        if (func(aggressive,.0,.70))
                            raise(randInt(20,40,10));
                        else if (func(aggressive,.0,.20))
                            raise(randInt(50,70,10));
                        else
                            call();
                    }
                    else if (percentWin>50)
                    {
                        if (func(aggressive,.0,.50))
                            raise(randInt(20,30,10));
                        else if (func(aggressive,.0,.30))
                            raise(randInt(50,70,10));
                        else if (func(loose,.60,.40))
                            call();
                        else
                            fold();
                    }
                    else if (percentWin>40)
                    {
                        if (func(aggressive,.0,.13))
                            raise(randInt(10,70,10));
                        else if (func(loose,.0,.76))
                            call();
                        else
                            fold();
                    }
                    else
                    {
                        if (func(aggressive,.0,.07))
                            raise(randInt(10,70,10));
                        else if (func(loose,.10,.70))
                            call();
                        else
                            fold();
                    }
                }
            }
        }
    }

    public void actFlop()
    {
        double percentWin = winPercentage();
        call();
    }

    public void actTurn()
    {
        double percentWin = winPercentage();
        call();
    }

    public void actRiver()
    {
        double percentWin = winPercentage();
        call();
    }

    public void showOpponentCards(Card c1,Card c2)
    {
        super.showOpponentCards(c1,c2);
    }

    public void act()
    {
        if (AIvAI.stage == AIvAI.PRE_FLOP)
        {
            if (lastStage != AIvAI.stage)
                raised = false;
            actPreFlop();
            lastStage = AIvAI.stage;
        }
        else if (AIvAI.stage == AIvAI.FLOP)
        {
            if (lastStage != AIvAI.stage)
                raised = false;
            actFlop();
            lastStage = AIvAI.stage;
        }
        else if (AIvAI.stage == AIvAI.TURN)
        {
            if (lastStage != AIvAI.stage)
                raised = false;
            actTurn();
            lastStage = AIvAI.stage;
        }
        else if (AIvAI.stage == AIvAI.RIVER)
        {
            if (lastStage != AIvAI.stage)
                raised = false;
            actRiver();
            lastStage = AIvAI.stage;
        }
    }

    public String toString()
    {
        return "" + chips;
    }

    public void initMap()
    {
        map.put(new StartingHand(14,14,false), new Double(84.93));
        map.put(new StartingHand(13,13,false), new Double(82.12));
        map.put(new StartingHand(12,12,false), new Double(79.63));
        map.put(new StartingHand(11,11,false), new Double(77.15));
        map.put(new StartingHand(10,10,false), new Double(74.66));
        map.put(new StartingHand(9,9,false), new Double(71.67));
        map.put(new StartingHand(8,8,false), new Double(68.72));
        map.put(new StartingHand(7,7,false), new Double(65.73));
        map.put(new StartingHand(6,6,false), new Double(62.70));
        map.put(new StartingHand(5,5,false), new Double(59.64));
        map.put(new StartingHand(4,4,false), new Double(56.26));
        map.put(new StartingHand(3,3,false), new Double(52.84));
        map.put(new StartingHand(2,2,false), new Double(49.39));
        map.put(new StartingHand(13,14,true), new Double(66.22));
        map.put(new StartingHand(12,14,true), new Double(65.31));
        map.put(new StartingHand(11,14,true), new Double(64.40));
        map.put(new StartingHand(13,14,false), new Double(64.47));
        map.put(new StartingHand(10,14,true), new Double(63.49));
        map.put(new StartingHand(12,14,false), new Double(63.51));
        map.put(new StartingHand(11,14,false), new Double(62.54));
        map.put(new StartingHand(12,13,true), new Double(62.41));
        map.put(new StartingHand(9,14,true), new Double(61.51));
        map.put(new StartingHand(10,14,false), new Double(61.57));
        map.put(new StartingHand(11,13,true), new Double(61.48));
        map.put(new StartingHand(8,14,true), new Double(60.51));
        map.put(new StartingHand(10,13,true), new Double(60.59));
        map.put(new StartingHand(12,13,false), new Double(60.43));
        map.put(new StartingHand(7,14,true), new Double(59.39));
        map.put(new StartingHand(9,14,false), new Double(59.45));
        map.put(new StartingHand(11,13,false), new Double(59.44));
        map.put(new StartingHand(11,12,true), new Double(59.07));
        map.put(new StartingHand(9,13,true), new Double(58.64));
        map.put(new StartingHand(5,14,true), new Double(58.06));
        map.put(new StartingHand(6,14,true), new Double(58.18));
        map.put(new StartingHand(8,14,false), new Double(58.37));
        map.put(new StartingHand(10,13,false), new Double(58.49));
        map.put(new StartingHand(10,12,true), new Double(58.17));
        map.put(new StartingHand(4,14,true), new Double(57.14));
        map.put(new StartingHand(7,14,false), new Double(57.17));
        map.put(new StartingHand(8,13,true), new Double(56.79));
        map.put(new StartingHand(3,14,true), new Double(56.34));
        map.put(new StartingHand(11,12,false), new Double(56.91));
        map.put(new StartingHand(9,13,false), new Double(56.41));
        map.put(new StartingHand(5,14,false), new Double(55.74));
        map.put(new StartingHand(6,14,false), new Double(55.87));
        map.put(new StartingHand(9,12,true), new Double(56.22));
        map.put(new StartingHand(7,13,true), new Double(55.85));
        map.put(new StartingHand(10,11,true), new Double(56.15));
        map.put(new StartingHand(2,14,true), new Double(55.51));
        map.put(new StartingHand(10,12,false), new Double(55.95));
        map.put(new StartingHand(4,14,false), new Double(54.73));
        map.put(new StartingHand(6,13,true), new Double(54.80));
        map.put(new StartingHand(8,13,false), new Double(54.43));
        map.put(new StartingHand(8,12,true), new Double(54.42));
        map.put(new StartingHand(3,14,false), new Double(53.86));
        map.put(new StartingHand(5,13,true), new Double(53.83));
        map.put(new StartingHand(9,11,true), new Double(54.11));
        map.put(new StartingHand(9,12,false), new Double(53.86));
        map.put(new StartingHand(10,11,false), new Double(53.83));
        map.put(new StartingHand(7,13,false), new Double(53.42));
        map.put(new StartingHand(2,14,false), new Double(52.95));
        map.put(new StartingHand(4,13,true), new Double(52.89));
        map.put(new StartingHand(7,12,true), new Double(52.52));
        map.put(new StartingHand(6,13,false), new Double(52.30));
        map.put(new StartingHand(3,13,true), new Double(52.07));
        map.put(new StartingHand(9,10,true), new Double(52.38));
        map.put(new StartingHand(8,11,true), new Double(52.31));
        map.put(new StartingHand(6,12,true), new Double(51.68));
        map.put(new StartingHand(8,12,false), new Double(51.93));
        map.put(new StartingHand(5,13,false), new Double(51.25));
        map.put(new StartingHand(9,11,false), new Double(51.64));
        map.put(new StartingHand(2,13,true), new Double(51.24));
        map.put(new StartingHand(5,12,true), new Double(50.71));
        map.put(new StartingHand(8,10,true), new Double(50.51));
        map.put(new StartingHand(4,13,false), new Double(50.23));
        map.put(new StartingHand(7,11,true), new Double(50.45));
        map.put(new StartingHand(4,12,true), new Double(49.76));
        map.put(new StartingHand(7,12,false), new Double(49.90));
        map.put(new StartingHand(9,10,false), new Double(49.82));
        map.put(new StartingHand(8,11,false), new Double(49.71));
        map.put(new StartingHand(3,13,false), new Double(49.33));
        map.put(new StartingHand(6,12,false), new Double(49.));
        map.put(new StartingHand(3,12,true), new Double(48.94));
        map.put(new StartingHand(8,9,true), new Double(48.86));
        map.put(new StartingHand(7,10,true), new Double(48.65));
        map.put(new StartingHand(6,11,true), new Double(48.57));
        map.put(new StartingHand(2,13,false), new Double(48.42));
        map.put(new StartingHand(2,12,true), new Double(48.10));
        map.put(new StartingHand(5,12,false), new Double(47.96));
        map.put(new StartingHand(5,11,true), new Double(47.82));
        map.put(new StartingHand(8,10,false), new Double(47.82));
        map.put(new StartingHand(7,11,false), new Double(47.73));
        map.put(new StartingHand(4,12,false), new Double(46.92));
        map.put(new StartingHand(7,9,true), new Double(46.99));
        map.put(new StartingHand(4,11,true), new Double(46.87));
        map.put(new StartingHand(6,10,true), new Double(46.80));
        map.put(new StartingHand(3,11,true), new Double(46.04));
        map.put(new StartingHand(3,12,false), new Double(46.02));
        map.put(new StartingHand(8,9,false), new Double(46.07));
        map.put(new StartingHand(7,8,true), new Double(45.68));
        map.put(new StartingHand(7,10,false), new Double(45.83));
        map.put(new StartingHand(6,11,false), new Double(45.71));
        map.put(new StartingHand(6,9,true), new Double(45.15));
        map.put(new StartingHand(2,11,true), new Double(45.20));
        map.put(new StartingHand(2,12,false), new Double(45.11));
        map.put(new StartingHand(5,10,true), new Double(44.94));
        map.put(new StartingHand(5,11,false), new Double(44.90));
        map.put(new StartingHand(4,10,true), new Double(44.20));
        map.put(new StartingHand(7,9,false), new Double(44.07));
        map.put(new StartingHand(6,8,true), new Double(43.82));
        map.put(new StartingHand(4,11,false), new Double(43.87));
        map.put(new StartingHand(6,10,false), new Double(43.85));
        map.put(new StartingHand(5,9,true), new Double(43.31));
        map.put(new StartingHand(3,10,true), new Double(43.38));
        map.put(new StartingHand(6,7,true), new Double(42.83));
        map.put(new StartingHand(3,11,false), new Double(42.97));
        map.put(new StartingHand(7,8,false), new Double(42.69));
        map.put(new StartingHand(2,10,true), new Double(42.54));
        map.put(new StartingHand(5,8,true), new Double(41.99));
        map.put(new StartingHand(6,9,false), new Double(42.10));
        map.put(new StartingHand(2,11,false), new Double(42.05));
        map.put(new StartingHand(5,10,false), new Double(41.86));
        map.put(new StartingHand(4,9,true), new Double(41.41));
        map.put(new StartingHand(5,7,true), new Double(40.98));
        map.put(new StartingHand(4,10,false), new Double(41.06));
        map.put(new StartingHand(3,9,true), new Double(40.81));
        map.put(new StartingHand(6,8,false), new Double(40.70));
        map.put(new StartingHand(5,6,true), new Double(40.35));
        map.put(new StartingHand(4,8,true), new Double(40.10));
        map.put(new StartingHand(5,9,false), new Double(40.14));
        map.put(new StartingHand(3,10,false), new Double(40.16));
        map.put(new StartingHand(2,9,true), new Double(39.97));
        map.put(new StartingHand(6,7,false), new Double(39.65));
        map.put(new StartingHand(4,7,true), new Double(39.11));
        map.put(new StartingHand(2,10,false), new Double(39.24));
        map.put(new StartingHand(4,5,true), new Double(38.53));
        map.put(new StartingHand(5,8,false), new Double(38.74));
        map.put(new StartingHand(4,6,true), new Double(38.48));
        map.put(new StartingHand(3,8,true), new Double(38.28));
        map.put(new StartingHand(4,9,false), new Double(38.09));
        map.put(new StartingHand(5,7,false), new Double(37.67));
        map.put(new StartingHand(2,8,true), new Double(37.68));
        map.put(new StartingHand(3,7,true), new Double(37.30));
        map.put(new StartingHand(3,9,false), new Double(37.43));
        map.put(new StartingHand(5,6,false), new Double(37.01));
        map.put(new StartingHand(3,5,true), new Double(36.76));
        map.put(new StartingHand(3,6,true), new Double(36.69));
        map.put(new StartingHand(4,8,false), new Double(36.71));
        map.put(new StartingHand(2,9,false), new Double(36.52));
        map.put(new StartingHand(3,4,true), new Double(35.73));
        map.put(new StartingHand(4,7,false), new Double(35.66));
        map.put(new StartingHand(2,7,true), new Double(35.44));
        map.put(new StartingHand(4,5,false), new Double(35.07));
        map.put(new StartingHand(4,6,false), new Double(35.));
        map.put(new StartingHand(2,5,true), new Double(34.93));
        map.put(new StartingHand(2,6,true), new Double(34.84));
        map.put(new StartingHand(3,8,false), new Double(34.75));
        map.put(new StartingHand(2,4,true), new Double(33.92));
        map.put(new StartingHand(2,8,false), new Double(34.09));
        map.put(new StartingHand(3,7,false), new Double(33.72));
        map.put(new StartingHand(3,5,false), new Double(33.16));
        map.put(new StartingHand(3,6,false), new Double(33.07));
        map.put(new StartingHand(2,3,true), new Double(33.09));
        map.put(new StartingHand(3,4,false), new Double(32.07));
        map.put(new StartingHand(2,7,false), new Double(31.71));
        map.put(new StartingHand(2,5,false), new Double(31.19));
        map.put(new StartingHand(2,6,false), new Double(31.08));
        map.put(new StartingHand(2,4,false), new Double(30.12));
        map.put(new StartingHand(2,3,false), new Double(29.24));
    }
}
