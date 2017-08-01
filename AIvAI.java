import java.util.List;
import java.util.ArrayList;
/**
 * The Framework and directions and graphics for game to run. In this game, a user plays heads up poker
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

    public static AI user;
    public static AI cpu;
    private static Player[] players;
    private static int handCount = 1;
    //List of all the cards in the hand that have been dealt, or will be dealt
    private static List<Card> cards = new ArrayList<Card>();
    //int representing what stage of the game it is, ex: flop, river, turn
    public static int stage = 0;
    public int lastStage = 0;
    public static int pot;
    public static int bet;
    //the number of chips in the pot after the last stage
    public static int lastPot;
    public int amountUSER;
    public int amountCPU;
    //true if the cpu/user has just made a new move
    private boolean newCPUMove = true;
    private boolean newUSERMove = true;
    //when a new Stage of the game is reached (flop/turn/river)
    //this variable is set to System.currentTimeMillis()
    public static double newStageTime = 0;
    //the String that represents the last action of the cpu/user
    String displayUSER;
    String displayCPU;
    //keeps track of if someone folded, set to false if they folded
    public static boolean cpuFold = true;
    public static boolean userFold = true;

    private boolean running = true;
    private Thread thread;
    //when somebody folds, if it was the CPU who folded, this variable is set to true
    public boolean cpuFolded;
    public boolean someoneAllIn = false;
    public boolean allIn = false;
    public final static int PRE_FLOP = 1, FLOP = 2, TURN = 3, RIVER = 4;

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
    public AI run(AI a, AI b)
    {
        user = a;
        cpu = b;
        while (running)
        { 
            render();
            if (State == State.END_HAND)
            {
                if (cpu.chips == 0)
                return user;
                else
                return cpu;
            }
        }
        return null;
    }

    /**
     * resets variables and deals new cards for a new hand to start
     */
    public void newHand()
    {
        System.out.println("chips: " + cpu.chips + ", " + user.chips);
        if (user.chips == 0||cpu.chips == 0)
        {
            System.out.println("done");
            State = State.END_HAND;
        }
        handCount++;
        pot = 0;
        lastPot = 0;
        bet = 0;
        cards.clear();
        user.fold = false;
        cpu.fold = false;
        Deck deck = new Deck();
        deck.shuffle();
        cpuFold = true;
        userFold = true;
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
            players = new Player[] {user,cpu};
        else
            players = new Player[] {cpu,user};
        for (int x = 0;x<players.length;x++)
        {
            players[x].alreadyIn = 0;
        }
        user.clearCards();
        cpu.clearCards();
        user.addCard(cards.get(0));
        user.addCard(cards.get(1));
        cpu.addCard(cards.get(7));
        cpu.addCard(cards.get(8));
        flopDone = false;
        turnDone = false;
        riverDone = false;
        handDone = false;
        newTurn();
        for (int x = 0;x<players.length;x++)//blinds
        {
            players[x].raise(10);
            raise(players[x]);
        }
        players[1].call = false; //allows for big blind to have option to raise
        switchTurn();
    }

    /**
     * takes a player (either cpu or player) and returns the other one
     */
    public static Player otherPlayer(Player self)
    {
        if (self == user)
            return cpu;
        else
            return user;
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
        if (user.chips<cpu.chips)
            possibleBet = user.chips;
        else
            possibleBet = cpu.chips;
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
     * on the AI or users actions
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
            State = STATE.FOLD;
            otherPlayer(player).chips += pot;
            handDone = true;
        }
        else if (player.raise != 0 && !handDone)
        {
            raise(player);
            switchTurn();
        }
    }

    /**
     * changes whose turn it is between the user and AI
     */
    public static void switchTurn()
    {
        if (user.isTurn)
        {
            user.isTurn = false;
            cpu.isTurn = true;
        }
        else
        {
            user.isTurn = true;
            cpu.isTurn = false;
        }
    }

    /**
     * based on timing of graphics and whose turn it is, it asks a player what they want to do and carries
     * out that action
     */
    public void askPlayers()
    {
        if (user.isTurn)
        {
            ask(user);
        }
        else if (cpu.isTurn)
        {
            ask(cpu);
        }
        if (user.call && cpu.call && !cpu.fold && !user.fold)
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
        user.call = false;
        cpu.call = false;
        user.alreadyIn = 0;
        cpu.alreadyIn = 0;
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
     * finds and displays to the user who won the last hand
     */
    private void findWinner()
    {
        if (!handDone)
        {
            PokerHandOriginal u = new PokerHandOriginal(user.getCards());
            PokerHandOriginal c = new PokerHandOriginal(cpu.getCards());
            winner = u.beats(c);
            System.out.println("cpu: " + cpu.getCards());
            System.out.println("user: " + user.getCards());
            cpu.showOpponentCards(cards.get(0),cards.get(1));
            user.showOpponentCards(cards.get(7),cards.get(8));
        }
        if (winner == 1)
        {
            if (!handDone)
            {
                user.chips += pot;
                handDone = true;
            }
        }
        else if (winner == 0)
        {
            if (!handDone)
            {
                cpu.chips += pot;
                handDone = true;
            }
        }
        else
        {
            if (!handDone)
            {
                cpu.chips += pot/2;
                user.chips += pot/2;
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
        if (State != STATE.ALL_IN && (user.chips == 0 || cpu.chips == 0))
        {
            if (user.call && cpu.call)
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
        else if (State == STATE.FOLD)
        {
            hand();
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