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
    public static final int WIDTH = 320;
    private static final long serialVersionUID = 1L;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 2;
    public final String TITLE = "Poker";
    private static final int NUM_CHIPS = 1000;
    private int winner;

    //these variables control the dealing of new cards as the game progresses
    private boolean flopDone = false;
    private boolean turnDone = false;
    private boolean riverDone = false;
    private boolean handDone = false;

    public static Player user = new Player(NUM_CHIPS);
    public static AI cpu = new AI(NUM_CHIPS);
    private Player[] players;
    private int handCount = 1;

    //these dimensions represent points at which various cards appear on the screen
    public final Dimension P_1 = new Dimension(WIDTH*SCALE-P1_OFFSET,HEIGHT*SCALE-100);
    public final Dimension P_2 = new Dimension((int)P_1.getWidth()-90,HEIGHT*SCALE-100);
    public final Dimension FLOP_3 = new Dimension(WIDTH-36,HEIGHT-49);
    public final Dimension FLOP_2 = new Dimension((int)FLOP_3.getWidth()-85,HEIGHT-49);
    public final Dimension FLOP_1 = new Dimension((int)FLOP_2.getWidth()-85,HEIGHT-49);
    public final Dimension TURN_DIMENSION = new Dimension((int)FLOP_3.getWidth()+85,HEIGHT-49);
    public final Dimension RIVER_DIMENSION = new Dimension((int)TURN_DIMENSION.getWidth()+85,HEIGHT-49);
    public final Dimension O_1 = new Dimension(10,10);
    public final Dimension O_2 = new Dimension((int)O_1.getWidth()+87,10);

    //rectangles that act as buttons and if clicked, will effect the game
    private Rectangle call = new Rectangle(20,HEIGHT*SCALE-70,100,50);
    private Rectangle fold = new Rectangle(140,HEIGHT*SCALE-70,100,50);
    private Rectangle raise = new Rectangle(20, HEIGHT*SCALE -140,100,50);

    //Image of the back of a card
    private Image cardBack;
    //Image of poker Chips
    private Image chipsPic;
    //Image of small stack of poker chips
    private Image smallStack;

    //List of all the cards in the hand that have been dealt, or will be dealt
    private List<Card> cards = new ArrayList<Card>();

    Menu menu = new Menu();
    //int representing what stage of the game it is, ex: flop, river, turn
    public static int stage = 0;
    public static int pot;
    public static int bet;
    public static int lastPot;
    public String cpuAction = "";
    public String userAction = "";
    public double timeCPU = 0;
    public double timeUSER = 0;
    public int amountUSER;
    public int amountCPU;
    private boolean newCPUMove = true;
    private boolean newUSERMove = true;

    private boolean running = false;
    private Thread thread;
    public boolean cpuFolded;

    public final static int PRE_FLOP = 1, FLOP = 2, TURN = 3, RIVER = 4;

    public static enum STATE {
        MENU,
        GAME,
        END_HAND,
        FOLD
    };

    public static STATE State = STATE.MENU;

    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
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
        game.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
        game.setMaximumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
        game.setMinimumSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));

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
    private void newHand()
    {
        handCount++;
        pot = 0;
        cards.clear();
        Deck deck = new Deck();
        deck.shuffle();
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
            players[x].clearCards();
            players[x].alreadyIn = 0;
        }
        user.add(cards.get(0));
        user.add(cards.get(1));
        cpu.add(cards.get(7));
        cpu.add(cards.get(8));
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

    public static Player otherPlayer(Player self)
    {
        if (self == user)
            return cpu;
        else
            return user;
    }

    public void raise(Player player)
    {
        pot += bet - player.alreadyIn;
        player.chips -= bet - player.alreadyIn;
        player.alreadyIn += bet - player.alreadyIn;
        bet += player.raise;
        pot += bet - player.alreadyIn;
        player.raise = 0;
        player.chips -= bet - player.alreadyIn;
        player.alreadyIn = bet;
    }

    public void ask(Player player)
    {
        if (player == cpu && !cpu.call)
        {
            AI ai = (AI)player;
            ai.act();
        }
        if ((player.call) && (player.raise == 0) && (!handDone))
        {
            player.chips -= bet -player.alreadyIn;
            pot += bet - player.alreadyIn;
            switchTurn();
            player.alreadyIn = bet;
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
            player.calling = false;
        }
        else if (player.fold && !handDone)
        {
            State = STATE.FOLD;
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

    public void switchTurn()
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
        if (user.call && cpu.call && System.currentTimeMillis() - newStageTime > 2500)
        {
            stage++;
            newStageTime = System.currentTimeMillis();
        }
    }

    public double newStageTime = 0;

    public void newTurn()
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
                players[x].add(cards.get(2));
                players[x].add(cards.get(3));
                players[x].add(cards.get(4));
            }
            flopDone = true;
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
                players[x].add(cards.get(5));
            }
            turnDone = true;
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
                players[x].add(cards.get(6));
            }
            riverDone = true;
            newTurn();
        }
        g.drawImage(cards.get(6).getCard(),(int)RIVER_DIMENSION.getWidth(),(int)RIVER_DIMENSION.getHeight(),this);
    }

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
        g.drawString("Pot: " + lastPot,(int)FLOP_3.getWidth()+10,(int)FLOP_3.getHeight()-10);
    }

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
            g.drawString("next hand",call.x+3,call.y+30);
        }
    }
    String displayUSER;
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
    String displayCPU;
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

    private void findWinner(Graphics g)
    {
        if (!handDone)
        {
            PokerHand u = new PokerHand(user.getCards());
            PokerHand c = new PokerHand(cpu.getCards());
            cpu.add(cards.get(0));
            cpu.add(cards.get(1));
            winner = u.beats(c);
        }
        Font fnt1 = new Font("arial", Font.BOLD,(int)(50*FONT_SCALE));
        g.setFont(fnt1);
        if (winner == 1)
        {
            g.drawString("You Win!", WIDTH+85, HEIGHT-150);
            if (!handDone)
            {
                user.chips += pot;
                handDone = true;
            }
        }
        else if (winner == 0)
        {
            g.drawString("You Lose", WIDTH+85, HEIGHT-150);
            if (!handDone)
            {
                cpu.chips += pot;
                handDone = true;
            }
        }
        else
        {
            g.drawString("You Tied", WIDTH+85, HEIGHT-150);
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
    private void hand(Graphics g)
    {
        drawButtons(g);
        displayChips(g);
        g.drawImage(cards.get(0).getCard(),(int)P_1.getWidth(),(int)P_1.getHeight(),this);
        g.drawImage(cards.get(1).getCard(),(int)P_2.getWidth(),(int)P_2.getHeight(),this);
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
        else if (State == STATE.FOLD)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
            Font fnt1 = new Font("arial", Font.BOLD,(int)(50*FONT_SCALE));
            g.setFont(fnt1);
            g.setColor(Color.WHITE);
            g.drawString("Fold", WIDTH+85, HEIGHT-150);
            hand(g);
            if (cpu.fold)
            {
                user.chips += pot;
                cpu.fold = false;
                cpuFolded = true;
            }
            else if (user.fold)
            {
                cpu.chips += pot;
                user.fold = false;
                cpuFolded = false;
            }
            if (cpuFolded)
                g.drawString("You Win", WIDTH+85, HEIGHT-100);
            else
                g.drawString("You Lose", WIDTH+85, HEIGHT-100);
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
