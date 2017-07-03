import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
/**
 * The Framework and directions and graphics for game to run. In this game, a user plays heads up poker
 * against an AI.
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class Game extends Canvas implements Runnable
{
    public static final double FONT_SCALE = 1.;
    public static final int P1_OFFSET = 68;
    public static final int WIDTH = 640;
    public static final int HEIGHT = WIDTH/12*9;
    public final String TITLE = "Poker";
    private static final int NUM_CHIPS = 1000;
    //int represeting who won at the end of a hand
    private int winner;
    
    public static int[] primes = {0,1,2,3,5,7,11,13,17,19,23,29,31,37,41,43};

    //these variables control the dealing of new cards as the game progresses
    private static boolean flopDone = false;
    private static boolean turnDone = false;
    private static boolean riverDone = false;
    private static boolean handDone = false;

    public static Player user = new Player(NUM_CHIPS);
    public static AI cpu = new AI(NUM_CHIPS);
    private static Player[] players;
    //how many hands have been played
    private static int handCount = 1;

    //these dimensions represent points at which various cards appear on the screen
    public final Dimension P_1 = new Dimension(WIDTH-P1_OFFSET,HEIGHT-100);
    public final Dimension P_2 = new Dimension((int)P_1.getWidth()-90,HEIGHT-100);
    public final Dimension FLOP_3 = new Dimension(WIDTH/2-36,HEIGHT/2-49);
    public final Dimension FLOP_2 = new Dimension((int)FLOP_3.getWidth()-85,HEIGHT/2-49);
    public final Dimension FLOP_1 = new Dimension((int)FLOP_2.getWidth()-85,HEIGHT/2-49);
    public final Dimension TURN_DIMENSION = new Dimension((int)FLOP_3.getWidth()+85,HEIGHT/2-49);
    public final Dimension RIVER_DIMENSION = new Dimension((int)TURN_DIMENSION.getWidth()+85,HEIGHT/2-49);
    public final Dimension O_1 = new Dimension(10,10);
    public final Dimension O_2 = new Dimension((int)O_1.getWidth()+87,10);

    //rectangles that act as buttons and if clicked, will effect the game
    private Rectangle call = new Rectangle(20,HEIGHT-70,100,50);
    private Rectangle fold = new Rectangle(140,HEIGHT-70,100,50);
    private Rectangle raise = new Rectangle(20, HEIGHT-140,100,50);

    //Image of the back of a card
    private Image cardBack;
    //Image of poker Chips
    private Image chipsPic;
    //Image of small stack of poker chips
    private Image smallStack;

    //List of all the cards in the hand that have been dealt, or will be dealt
    private static List<Card> cards = new ArrayList<Card>();

    Menu menu = new Menu();
    //int representing what stage of the game it is, ex: flop, river, turn
    public static int stage = 0;
    public int lastStage = 0;
    public static int pot;
    public static int bet;
    //the number of chips in the pot after the last stage
    public static int lastPot;
    //strings to show the user what the last action taken by each player was (check/call/raise)
    public String cpuAction = "";
    public String userAction = "";
    //System.currentTimeMillis() at last new move of the CPU or USER
    public static double timeCPU = 0;
    public static double timeUSER = 0;
    //amount of chips the user or cpu has put in following their last action
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

    private boolean running = false;
    private Thread thread;
    //when somebody folds, if it was the CPU who folded, this variable is set to true
    public boolean cpuFolded;
    public boolean someoneAllIn = false;

    public final static int PRE_FLOP = 1, FLOP = 2, TURN = 3, RIVER = 4;

    public static enum STATE {
        MENU,
        GAME,
        END_HAND,
        FOLD,
        ALL_IN
    };

    public static STATE State = STATE.MENU;

    private BufferedImage image = new BufferedImage(WIDTH/2,HEIGHT/2,BufferedImage.TYPE_INT_RGB);
    private CardSheet cardSheet;
    /**
     * initializes the picture of the cardback and the creates a mouse listener to take input from the mouse
     * of the user
     */
    public void initialize()
    {
        this.addMouseListener(new MouseInput());
        ImageLoader x = new ImageLoader();
        try
        {
            BufferedImage y = x.loadImage("pictures/cardback.png");
            cardBack = y.getScaledInstance((y.getWidth()/9),(y.getHeight()/9),Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        try
        {
            BufferedImage z = x.loadImage("pictures/chips.png");
            chipsPic = z.getScaledInstance((z.getWidth()/4),(z.getHeight()/4),Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        try
        {
            BufferedImage i = x.loadImage("pictures/smallstack.png");
            smallStack = i.getScaledInstance((i.getWidth()/10),(i.getHeight()/10),Image.SCALE_DEFAULT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    private synchronized void start()
    {
        if (running)
            return;
        else
            running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop()
    {
        if (!running)
            return;

        running = false;
        try {thread.join();} 
        catch (InterruptedException e) {e.printStackTrace();}
        System.exit(1);
    }

    /**
     * starts the game
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        game.setMaximumSize(new Dimension(WIDTH,HEIGHT));
        game.setMinimumSize(new Dimension(WIDTH,HEIGHT));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }

    /**
     * the main game loop
     */
    public void run()
    {
        initialize();
        while (running)
        { 
            render();
        }
        stop();
    }

    /**
     * resets variables and deals new cards for a new hand to start
     */
    public void newHand()
    {
        if (user.chips == 0||cpu.chips == 0)
        {
            State = STATE.MENU;
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
        allInTime = Double.MAX_VALUE;
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
        user.set(0,cards.get(0));
        user.set(1,cards.get(1));
        cpu.set(0,cards.get(7));
        cpu.set(1,cards.get(8));
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
        newStageTime = System.currentTimeMillis();
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
        if (player == cpu && !cpu.call)
        {
            AI ai = (AI)player;
            ai.act();
        }
        if (player.call && player.raise == 0 && !handDone)
        {
            player.chips -= bet -player.alreadyIn;
            pot += bet - player.alreadyIn;
            switchTurn();
            player.alreadyIn = bet;
            player.call = true;
            if (player == cpu)
            {
                if (bet == 0||(stage == 1 && pot == 40 && otherPlayer(player).call))
                    cpuAction = "check";
                else
                    cpuAction = "call";
                newCPUMove = true;
            }
            else
            {
                if (bet == 0||(stage == 1 && pot == 40 && otherPlayer(player).call))
                    userAction = "check";
                else
                    userAction = "call";
                newUSERMove = true;
            }
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
            if (player == cpu)
            {
                cpuAction = "raise";
                newCPUMove = true;
            }
            else
            {
                userAction = "raise";
                newUSERMove = true;
            }
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
        if (user.isTurn && System.currentTimeMillis() - timeCPU > 2500)
        {
            ask(user);
        }
        else if (cpu.isTurn && System.currentTimeMillis() - timeUSER > 2500)
        {
            ask(cpu);
        }
        if (user.call && cpu.call && !cpu.fold && !user.fold && System.currentTimeMillis() - newStageTime > 2500)
        {
            stage++;
            newStageTime = System.currentTimeMillis();
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
    public void flop(Graphics g)
    {
        if (!flopDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].set(2,cards.get(2));
                players[x].set(3,cards.get(3));
                players[x].set(4,cards.get(4));
            }
            flopDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
        g.drawImage(cards.get(2).getCard(),(int)FLOP_1.getWidth(),(int)FLOP_1.getHeight(),this);
        g.drawImage(cards.get(3).getCard(),(int)FLOP_2.getWidth(),(int)FLOP_2.getHeight(),this);
        g.drawImage(cards.get(4).getCard(),(int)FLOP_3.getWidth(),(int)FLOP_3.getHeight(),this);
    }

    /**
     * deals the card for the turn and displays it
     */
    public void turn(Graphics g)
    {
        if (!turnDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].set(5,cards.get(5));
            }
            turnDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
        g.drawImage(cards.get(5).getCard(),(int)TURN_DIMENSION.getWidth(),(int)TURN_DIMENSION.getHeight(),this);
    }

    /**
     * deals the card for the river and displays it
     */
    public void river(Graphics g)
    {
        if (!riverDone)
        {
            for (int x = 0;x<players.length;x++)
            {
                players[x].set(6,cards.get(6));
            }
            riverDone = true;
            if (State != STATE.ALL_IN)
                newTurn();
        }
        g.drawImage(cards.get(6).getCard(),(int)RIVER_DIMENSION.getWidth(),(int)RIVER_DIMENSION.getHeight(),this);
    }

    /**
     * displays all of the information regarding chips
     */
    private void displayChips(Graphics g)
    {
        Font fnt2 = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
        g.setFont(fnt2);
        g.setColor(Color.WHITE);
        //users chips
        g.drawImage(chipsPic,(int)P_2.getWidth()-185,(int)P_2.getHeight()+30,this);
        g.drawString("Chips: " + user.chips, (int)P_2.getWidth()-125,(int)P_2.getHeight()+69);
        //cpu's chips
        g.drawImage(chipsPic,(int)O_2.getWidth()+80,(int)O_2.getHeight()+5,this);
        g.drawString("Chips: " + cpu.chips, (int)O_2.getWidth()+141,(int)O_2.getHeight()+44);
        //pot
        if (State == STATE.GAME)
        g.drawString("Pot: " + lastPot,(int)FLOP_3.getWidth()+10,(int)FLOP_3.getHeight()-10);
        else
        g.drawString("Pot: " + pot,(int)FLOP_3.getWidth()+10,(int)FLOP_3.getHeight()-10);
    }

    /**
     * draws the buttons the user can press based on what state the game is in
     */
    private void drawButtons(Graphics g)
    {
        if (State == STATE.GAME)
        {
            Graphics2D g2d = (Graphics2D)g;
            Font fnt1 = new Font("arial",Font.BOLD,(int)(18*FONT_SCALE));
            g.setFont(fnt1);
            g.setColor(Color.WHITE);
            g.drawString("call/check",call.x+7,call.y+31);
            Font fnt = new Font("arial",Font.BOLD,(int)(30*FONT_SCALE));
            g.setFont(fnt);
            g2d.draw(call);
            g2d.draw(raise);
            g2d.draw(fold);
            g.drawString("fold",fold.x+23,fold.y+37);
            g.drawString("raise",raise.x+17,raise.y+36);
        }
        else if (State == STATE.END_HAND || State == STATE.FOLD)
        {
            Graphics2D g2d = (Graphics2D)g;
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g2d.draw(call);
            if (cpu.chips == 0||user.chips == 0)
                g.drawString("menu",call.x+20,call.y+30);
            else
                g.drawString("next hand",call.x+3,call.y+30);
        }
        else if (State == STATE.ALL_IN)
        {
            Graphics2D g2d = (Graphics2D)g;
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g2d.draw(call);
            g.drawString("next card",call.x+3,call.y+30);
        }
    }

    /**
     * displays how many chips the user has bet and for 2 seconds after an action is taken (raise/call/check)
     * it is displayed for the user.
     */
    public void displayActionUser(Graphics g)
    {
        if (newUSERMove && System.currentTimeMillis() - timeUSER >= 2000)
        {
            timeUSER = System.currentTimeMillis();
            amountUSER = user.alreadyIn;
            displayUSER = userAction;
            newUSERMove = false;
        }
        if (System.currentTimeMillis() - timeUSER < 2000)
        {
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setColor(Color.WHITE);
            g.setFont(fnt);
            g.drawString(displayUSER, (int)P_2.getWidth()-175,(int)P_2.getHeight()-38);
        }
        if (user.alreadyIn != 0)
        {
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setColor(Color.WHITE);
            g.setFont(fnt);
            g.drawString("" + user.alreadyIn,(int)P_2.getWidth()-145,(int)P_2.getHeight()-3);
            g.drawImage(smallStack,(int)P_2.getWidth()-200,(int)P_2.getHeight()-35,this);
        }
    }

    /**
     * displays how many chips the cpu has bet and for 2 seconds after an action is taken (raise/call/check)
     * it is displayed for the user.
     */
    public void displayActionCpu(Graphics g)
    {
        if (newCPUMove && System.currentTimeMillis() - timeCPU >= 2000)
        {
            timeCPU = System.currentTimeMillis();
            amountCPU = cpu.alreadyIn;
            displayCPU = cpuAction;
            newCPUMove = false;
        }
        if (System.currentTimeMillis() - timeCPU < 2000)
        {
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setColor(Color.WHITE);
            g.setFont(fnt);
            g.drawString(displayCPU,(int)P_2.getWidth()-180,(int)P_2.getHeight()-238);
        }
        if (cpu.alreadyIn != 0)
        {
            Font fnt = new Font("arial",Font.BOLD,(int)(20*FONT_SCALE));
            g.setColor(Color.WHITE);
            g.setFont(fnt);
            g.drawString("" + cpu.alreadyIn,(int)P_2.getWidth()-145,(int)P_2.getHeight()-268);
            g.drawImage(smallStack,(int)P_2.getWidth()-200,(int)P_2.getHeight()-300,this);
        }
    }

    /**
     * finds and displays to the user who won the last hand
     */
    private void findWinner(Graphics g)
    {
        if (!handDone)
        {
            PokerHand u = new PokerHand(user.getCards());
            PokerHand c = new PokerHand(cpu.getCards());
            winner = u.beats(c);
        }
        Font fnt1 = new Font("arial", Font.BOLD,(int)(50*FONT_SCALE));
        g.setFont(fnt1);
        if (winner == 1)
        {
            g.drawString("You Win!", WIDTH/2+85, HEIGHT/2-150);
            if (!handDone)
            {
                user.chips += pot;
                handDone = true;
            }
        }
        else if (winner == 0)
        {
            g.drawString("You Lose", WIDTH/2+85, HEIGHT/2-150);
            if (!handDone)
            {
                cpu.chips += pot;
                handDone = true;
            }
        }
        else
        {
            g.drawString("You Tied", WIDTH/2+85, HEIGHT/2-150);
            if (!handDone)
            {
                cpu.chips += pot/2;
                user.chips += pot/2;
                handDone = true;
            }
        }
    }
    
    public boolean allIn = false;
    public double allInTime = Double.MAX_VALUE;
    /**
     * this method controls and displays the game when it is being played, and not in menu mode.
     * It deals and displays cards and determines winners and losers of hands
     */
    private void hand(Graphics g)
    {
        drawButtons(g);
        displayChips(g);
        g.drawImage(cards.get(0).getCard(),(int)P_1.getWidth(),(int)P_1.getHeight(),this);
        g.drawImage(cards.get(1).getCard(),(int)P_2.getWidth(),(int)P_2.getHeight(),this);
        if (State != STATE.ALL_IN && (user.chips == 0 || cpu.chips == 0))
        {
            if (user.call && cpu.call)
            {
                if (!allIn)
                {
                    allInTime = System.currentTimeMillis();
                    allIn = true;
                }
                if (System.currentTimeMillis() - allInTime > 1500)
                State = STATE.ALL_IN;
            }
        }
        if (System.currentTimeMillis() - newStageTime > 2500 && State != STATE.FOLD)
            askPlayers();
        displayActionUser(g);
        displayActionCpu(g);
        if (stage > 1)//flop
        {
            if (stage == 2)
            {
                if (System.currentTimeMillis() - newStageTime > 2000)
                    flop(g);
                else
                {
                    g.drawImage(cardBack,(int)FLOP_1.getWidth(),(int)FLOP_1.getHeight(),this);
                    g.drawImage(cardBack,(int)FLOP_2.getWidth(),(int)FLOP_2.getHeight(),this);
                    g.drawImage(cardBack,(int)FLOP_3.getWidth(),(int)FLOP_3.getHeight(),this);
                }
            }
            else
                flop(g);
        }
        else
        {
            g.drawImage(cardBack,(int)FLOP_1.getWidth(),(int)FLOP_1.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_2.getWidth(),(int)FLOP_2.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_3.getWidth(),(int)FLOP_3.getHeight(),this);
        }
        if (stage > 2)//turn
        {
            if (stage == 3)
            {
                if (System.currentTimeMillis() - newStageTime > 2000)
                    turn(g);
                else
                    g.drawImage(cardBack,(int)TURN_DIMENSION.getWidth(),(int)TURN_DIMENSION.getHeight(),this);
            }
            else
                turn(g);
        }
        else
            g.drawImage(cardBack,(int)TURN_DIMENSION.getWidth(),(int)TURN_DIMENSION.getHeight(),this);
        if (stage > 3)//river
        {
            if (stage == 4)
            {
                if (System.currentTimeMillis() - newStageTime > 2000)
                    river(g);
                else
                    g.drawImage(cardBack,(int)RIVER_DIMENSION.getWidth(),(int)RIVER_DIMENSION.getHeight(),this);
            }
            else
                river(g);
        }
        else
            g.drawImage(cardBack,(int)RIVER_DIMENSION.getWidth(),(int)RIVER_DIMENSION.getHeight(),this);
        if (stage > 4)//show opponents cards
        {
            if (stage == 5)
            {
                if (System.currentTimeMillis() - newStageTime > 2000)
                {
                    g.drawImage(cards.get(7).getCard(),(int)O_1.getWidth(),(int)O_1.getHeight(),this);
                    g.drawImage(cards.get(8).getCard(),(int)O_2.getWidth(),(int)O_2.getHeight(),this);
                    findWinner(g);
                }
                else
                {
                    g.drawImage(cardBack,(int)O_1.getWidth(),(int)O_1.getHeight(),this);
                    g.drawImage(cardBack,(int)O_2.getWidth(),(int)O_2.getHeight(),this);
                }
            }
            else
            {
                g.drawImage(cards.get(7).getCard(),(int)O_1.getWidth(),(int)O_1.getHeight(),this);
                g.drawImage(cards.get(8).getCard(),(int)O_2.getWidth(),(int)O_2.getHeight(),this);
                findWinner(g);
            }
        }
        else
        {
            g.drawImage(cardBack,(int)O_1.getWidth(),(int)O_1.getHeight(),this);
            g.drawImage(cardBack,(int)O_2.getWidth(),(int)O_2.getHeight(),this);
        }
        if (stage > 5)
            State = STATE.END_HAND;
    }

    public void allInRender(Graphics g)
    {
        drawButtons(g);
        displayChips(g);
        g.drawImage(cards.get(0).getCard(),(int)P_1.getWidth(),(int)P_1.getHeight(),this);
        g.drawImage(cards.get(1).getCard(),(int)P_2.getWidth(),(int)P_2.getHeight(),this);
        g.drawImage(cards.get(7).getCard(),(int)O_1.getWidth(),(int)O_1.getHeight(),this);
        g.drawImage(cards.get(8).getCard(),(int)O_2.getWidth(),(int)O_2.getHeight(),this);
        if (stage > 2)//flop
        {
            flop(g);
        }
        else
        {
            g.drawImage(cardBack,(int)FLOP_1.getWidth(),(int)FLOP_1.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_2.getWidth(),(int)FLOP_2.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_3.getWidth(),(int)FLOP_3.getHeight(),this);
        }
        if (stage > 3)//turn
        {
            turn(g);
        }
        else
            g.drawImage(cardBack,(int)TURN_DIMENSION.getWidth(),(int)TURN_DIMENSION.getHeight(),this);
        if (stage > 4)//river
        {
            river(g);
            findWinner(g);
            State = STATE.END_HAND;
            stage = 6;
            user.alreadyIn = 0;
            cpu.alreadyIn = 0;
        }
        else
        {
            g.drawImage(cardBack,(int)RIVER_DIMENSION.getWidth(),(int)RIVER_DIMENSION.getHeight(),this);
        }
    }

    /**
     * renders whatever images are supposed to be created for the gameplay or menu
     */
    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs==null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if (State == STATE.MENU)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
            menu.render(g);
        }
        else if (State == STATE.ALL_IN)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
            allInRender(g);
        }
        else if (State == STATE.FOLD)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
            Font fnt1 = new Font("arial", Font.BOLD,(int)(50*FONT_SCALE));
            g.setFont(fnt1);
            g.setColor(Color.WHITE);
            g.drawString("Fold", WIDTH/2+85, HEIGHT/2-150);
            hand(g);
            if (cpu.fold)
                g.drawString("You Win", WIDTH/2+85, HEIGHT/2-100);
            else
                g.drawString("You Lose", WIDTH/2+85, HEIGHT/2-100);
        }
        else
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
            if (stage==0)
            {
                newHand();
                stage++;
            }
            hand(g);
        }
        g.dispose();
        bs.show();
    }
}
