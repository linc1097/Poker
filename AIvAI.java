import java.util.List;
import java.util.ArrayList;
/**
 * The Framework and directions and graphics for game to run. In this game, a p1 plays heads up poker
 * against an AI.
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class AIvAI
{
    private static final int NUM_CHIPS = 1000;
    //int represeting who won at the end of a hand
    private int winner;

    public static int[] primes = {0,1,2,3,5,7,11,13,17,19,23,29,31,37,41,43};

    //these variables control the dealing of new cards as the game progresses
    private static boolean flopDone = false;
    private static boolean turnDone = false;
    private static boolean riverDone = false;
    private static boolean handDone = false;

    public static AI p1;
    public static AI p2;
    private static Player[] players;
    private static int handCount = 0;
    //List of all the cards in the hand that have been dealt, or will be dealt
    private static List<Card> cards = new ArrayList<Card>();
    //int representing what stage of the game it is, ex: flop, river, turn
    public static int stage = 0;
    public int lastStage = 0;
    public static int pot;
    public static int bet;
    //the number of chips in the pot after the last stage
    public static int lastPot;
    public int amountp1;
    public int amountp2;
    //true if the p2/p1 has just made a new move
    private boolean newp2Move = true;
    private boolean newp1Move = true;
    //when a new Stage of the game is reached (flop/turn/river)
    //this variable is set to System.currentTimeMillis()
    public static double newStageTime = 0;
    //the String that represents the last action of the p2/p1
    String displayp1;
    String displayp2;
    //keeps track of if someone folded, set to false if they folded
    public static boolean p2Fold = true;
    public static boolean p1Fold = true;

    private boolean running = true;
    private Thread thread;
    //when somebody folds, if it was the p2 who folded, this variable is set to true
    public boolean p2Folded;
    public boolean someoneAllIn = false;
    public boolean allIn = false;
    public final static int PRE_FLOP = 1, FLOP = 2, TURN = 3, RIVER = 4;
    public int hands = 0;

    public static enum STATE {
        MENU,
        GAME,
        END_HAND,
        FOLD,
        ALL_IN
    };

    public static STATE State = STATE.GAME;

    /**
     * the main game loop
     */
    public ArrayList<Object> run(AI a, AI b)
    {
        ArrayList<Object> list = new ArrayList<Object>();
        p1 = a;
        p2 = b;
        State = STATE.GAME;
        while (running)
        { 
            render();
            if (State == State.END_HAND)
            {
                list.add(new Integer(handCount));
                if (p1.chips == 0)
                    list.add(p2);
                else
                    list.add(p1);
                return list;
            }
        }
        return null;
    }

    /**
     * resets variables and deals new cards for a new hand to start
     */
    public void newHand()
    {
        System.out.println("p1: " + p1.chips);
        System.out.println("p2: " + p2.chips);
        if (p1.chips == 0||p2.chips == 0)
        {
            State = State.END_HAND;
        }
        handCount++;
        pot = 0;
        lastPot = 0;
        bet = 0;
        cards.clear();
        p1.fold = false;
        p2.fold = false;
        Deck deck = new Deck();
        deck.shuffle();
        p2Fold = true;
        p1Fold = true;
        allIn = false;
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        cards.add(deck.dealCard());
        if (handCount%2==0)
            players = new Player[] {p1,p2};
        else
            players = new Player[] {p2,p1};
        for (int x = 0;x<players.length;x++)
        {
            players[x].alreadyIn = 0;
        }
        p1.clearCards();
        p2.clearCards();
        p1.addCard(cards.get(0));
        p1.addCard(cards.get(1));
        p2.addCard(cards.get(7));
        p2.addCard(cards.get(8));
        flopDone = false;
        turnDone = false;
        riverDone = false;
        handDone = false;
        newTurn();
        for (int x = 0;x<players.length;x++)//blinds
        {
            if ((players[x].chips>=20&&bet==10)||bet==0)
            {
                players[x].raise(10);
                raise(players[x]);
            }
        }
        players[1].call = false; //allows for big blind to have option to raise
        switchTurn();
    }

    /**
     * takes a player (either p2 or player) and returns the other one
     */
    public static Player otherPlayer(Player self)
    {
        if (self == p1)
            return p2;
        else
            return p1;
    }

    /**
     * if a player raises, this method is called to alter all of the variables in order to carry out the raise
     */
    public static void raise(Player player)
    {
        int possibleBet;
        pot += bet - player.alreadyIn;
        player.chips -= bet - player.alreadyIn;
        player.alreadyIn += bet - player.alreadyIn;
        bet += player.raise;
        player.call = true;
        if (p1.chips<p2.chips)
            possibleBet = p1.chips;
        else
            possibleBet = p2.chips;
        if (bet - player.alreadyIn > possibleBet)
            ;
        else
            possibleBet = bet - player.alreadyIn;
        bet -= player.raise - possibleBet;
        pot += possibleBet;
        player.raise = 0;
        player.chips -= possibleBet;
        player.alreadyIn = possibleBet + player.alreadyIn;
    }

    /**
     * asks the AI what it wants to do, and will change variables move the game forward based 
     * on the AI or p1s actions
     */
    public void ask(Player player)
    {
        AI ai = (AI)player;
        ai.act();
        if (player.call && player.raise == 0 && !handDone)
        {
            player.chips -= bet -player.alreadyIn;
            pot += bet - player.alreadyIn;
            switchTurn();
            player.alreadyIn = bet;
            player.call = true;
        }
        else if (player.fold && !handDone)
        {
            otherPlayer(player).chips += pot;
            stage = 0;
            handDone = true;
        }
        else if (player.raise != 0 && !handDone)
        {
            raise(player);
            switchTurn();
        }
    }

    /**
     * changes whose turn it is between the p1 and AI
     */
    public static void switchTurn()
    {
        if (p1.isTurn)
        {
            p1.isTurn = false;
            p2.isTurn = true;
        }
        else
        {
            p1.isTurn = true;
            p2.isTurn = false;
        }
    }

    /**
     * based on timing of graphics and whose turn it is, it asks a player what they want to do and carries
     * out that action
     */
    public void askPlayers()
    {
        if (p1.isTurn)
        {
            ask(p1);
        }
        else if (p2.isTurn)
        {
            ask(p2);
        }
        if (p1.call && p2.call && !p2.fold && !p1.fold)
        {
            stage++;
        }
    }

    /**
     * called in between new cards being dealt (when both players have called/checked)
     */
    public static void newTurn()
    {
        players[1].isTurn = true;
        players[0].isTurn = false;
        p1.call = false;
        p2.call = false;
        p1.alreadyIn = 0;
        p2.alreadyIn = 0;
        bet = 0;
        lastPot = pot;
    }

    /**
     * deals the cards for the flop and displays them
     */
    public void flop()
    {
        if (!flopDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].addCard(cards.get(2));
                players[x].addCard(cards.get(3));
                players[x].addCard(cards.get(4));
            }
            flopDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
    }

    /**
     * deals the card for the turn and displays it
     */
    public void turn()
    {
        if (!turnDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].addCard(cards.get(5));
            }
            turnDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
    }

    /**
     * deals the card for the river and displays it
     */
    public void river()
    {
        if (!riverDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].addCard(cards.get(6));
            }
            riverDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
    }

    /**
     * finds and displays to the p1 who won the last hand
     */
    private void findWinner()
    {
        if (!handDone)
        {
            PokerHandOriginal u = new PokerHandOriginal(p1.getCards());
            PokerHandOriginal c = new PokerHandOriginal(p2.getCards());
            winner = u.beats(c);
            p2.showOpponentCards(cards.get(0),cards.get(1));
            p1.showOpponentCards(cards.get(7),cards.get(8));
        }
        if (winner == 1)
        {
            if (!handDone)
            {
                p1.chips += pot;
                handDone = true;
            }
        }
        else if (winner == 0)
        {
            if (!handDone)
            {
                p2.chips += pot;
                handDone = true;
            }
        }
        else
        {
            if (!handDone)
            {
                p2.chips += pot/2;
                p1.chips += pot/2;
                handDone = true;
            }
        }
    }

    /**
     * this method controls and displays the game when it is being played, and not in menu mode.
     * It deals and displays cards and determines winners and losers of hands
     */
    private void hand()
    {
        if (State != STATE.ALL_IN && (p1.chips == 0 || p2.chips == 0))
        {
            if (p1.call && p2.call)
            {
                if (!allIn)
                {
                    allIn = true;
                }
                State = STATE.ALL_IN;
            }
        }
        if (State != STATE.FOLD)
            askPlayers();
        if (stage > 1)//flop
        {
            flop();
        }
        if (stage > 2)//turn
        {
            turn();
        }
        if (stage > 3)//river
        {
            river();
        }
        if (stage > 4)//show opponents cards
        {
            findWinner();
        }
        if (stage > 5)
        {
            if (allIn)
                State = State.END_HAND;
            else
                stage = 0;
        }
    }

    public void allIn()
    {
        flop();
        turn();
        river();
        findWinner();
        stage = 6;
    }

    /**
     * renders whatever images are supposed to be created for the gameplay or menu
     */
    private void render()
    {
        if (State == STATE.ALL_IN)
        {
            allIn();
        }
        else
        {
            if (stage==0)
            {
                newHand();
                if (State == State.END_HAND)
                    return;
                stage++;
            }
            hand();
        }
    }
}