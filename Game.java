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
    public static final int WIDTH = 320;
    private static final long serialVersionUID = 1L;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 2;
    public final String TITLE = "Poker";
    private static final int NUM_CHIPS = 1000;

    //these variables control the dealing of new cards as the game progresses
    private boolean flopDone = false;
    private boolean turnDone = false;
    private boolean riverDone = false;
    private boolean handDone = false;

    public static Player user = new Player(NUM_CHIPS);
    public static Player cpu = new Player(NUM_CHIPS);
    private Player[] players;
    private int handCount = 0;

    //these dimensions represent points at which various cards appear on the screen
    public final Dimension P_1 = new Dimension(WIDTH*SCALE-68,HEIGHT*SCALE-100);
    public final Dimension P_2 = new Dimension((int)P_1.getWidth()-90,HEIGHT*SCALE-100);
    public final Dimension FLOP_3 = new Dimension(WIDTH-36,HEIGHT-49);
    public final Dimension FLOP_2 = new Dimension((int)FLOP_3.getWidth()-85,HEIGHT-49);
    public final Dimension FLOP_1 = new Dimension((int)FLOP_2.getWidth()-85,HEIGHT-49);
    public final Dimension TURN = new Dimension((int)FLOP_3.getWidth()+85,HEIGHT-49);
    public final Dimension RIVER = new Dimension((int)TURN.getWidth()+85,HEIGHT-49);
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

    //ArrayList of all the cards in the hand that have been dealt, or will be dealt
    private ArrayList<Card> cards = new ArrayList();

    Menu menu = new Menu();
    //int representing what stage of the game it is, ex: flop, river, turn
    public static int stage = 0;
    public int pot;
    public int bet;

    private boolean running = false;
    private Thread thread;

    public static enum STATE {
        MENU,
        GAME,
        END_HAND
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
            BufferedImage y = x.loadImage("C:\\Users\\linc1\\Desktop\\poker-game\\pictures\\cardback.png");
            cardBack = y.getScaledInstance((y.getWidth()/9),(y.getHeight()/9),Image.SCALE_DEFAULT);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
            return;
        }
        try
        {
            BufferedImage z = x.loadImage("C:\\Users\\linc1\\Desktop\\poker-game\\pictures\\chips.png");
            chipsPic = z.getScaledInstance((z.getWidth()/4),(z.getHeight()/4),Image.SCALE_DEFAULT);
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
    public static void main()
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
    }

    public static Player otherPlayer(Player self)
    {
        if (self == user)
            return cpu;
        else
            return user;
    }

    public void ask(Player player)
    {
        if (player == cpu)
        {
            player.call = true;
            player.chips -= bet-player.alreadyIn;
            pot += bet - player.alreadyIn;
            switchTurn();
        }
        else
        {
            if (player.call)
            {
                player.chips -= bet -player.alreadyIn;
                pot += bet - player.alreadyIn;
                switchTurn();
            }
            else if (player.fold)
            {
                otherPlayer(player).chips+=pot;
                stage = 0;
                player.fold = false;
                switchTurn();
            }
            else if (player.raise != 0)
            {
                player.chips -= player.raise;
                bet += player.raise;
                pot += bet - player.alreadyIn;
                player.raise = 0;
                player.call = true;
                switchTurn();
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
        if (user.isTurn)
        {
            ask(user);
        }
        else if (cpu.isTurn)
        {
            ask(cpu);
        }
        if (user.call&&cpu.call)
        {
            stage++;
        }
    }

    public void newTurn()
    {
        players[0].isTurn = true;
        players[1].isTurn = false;
        user.call = false;
        cpu.call = false;
        bet = 0;
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
        g.drawImage(cards.get(5).getCard(),(int)TURN.getWidth(),(int)TURN.getHeight(),this);
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
        g.drawImage(cards.get(6).getCard(),(int)RIVER.getWidth(),(int)RIVER.getHeight(),this);
    }

    private void displayChips(Graphics g)
    {
        Font fnt2 = new Font("arial",Font.BOLD,20);
        g.setFont(fnt2);
        g.setColor(Color.WHITE);
        //users chips
        g.drawImage(chipsPic,(int)P_2.getWidth()-185,(int)P_2.getHeight()+30,this);
        g.drawString("Chips: " + user.chips, (int)P_2.getWidth()-125,(int)P_2.getHeight()+69);
        //cpu's chips
        g.drawImage(chipsPic,(int)O_2.getWidth()+80,(int)O_2.getHeight()+5,this);
        g.drawString("Chips: " + cpu.chips, (int)O_2.getWidth()+141,(int)O_2.getHeight()+44);
        //pot
        g.drawString("Pot: " + pot,(int)FLOP_3.getWidth()+10,(int)FLOP_3.getHeight()-10);
    }

    private void drawButtons(Graphics g)
    {
        if (State == STATE.GAME)
        {
            Graphics2D g2d = (Graphics2D)g;
            Font fnt = new Font("arial",Font.BOLD,30);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g2d.draw(call);
            g2d.draw(raise);
            g2d.draw(fold);
            g.drawString("call",call.x+19,call.y+40);
            g.drawString("fold",fold.x+19,fold.y+40);
            g.drawString("raise",raise.x+19,raise.y+40);
        }
        else if (State == STATE.END_HAND)
        {
            Graphics2D g2d = (Graphics2D)g;
            Font fnt = new Font("arial",Font.BOLD,20);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g2d.draw(call);
            g.drawString("next hand",call.x+19,call.y+40);
        }
    }

    private void findWinner(Graphics g)
    {
        PokerHand u = new PokerHand(user.getCards());
        PokerHand c = new PokerHand(cpu.getCards());
        int winner = u.beats(c);
        Font fnt1 = new Font("arial", Font.BOLD,50);
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
        askPlayers();
        if (stage > 1)//flop
            flop(g);
        else
        {
            g.drawImage(cardBack,(int)FLOP_1.getWidth(),(int)FLOP_1.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_2.getWidth(),(int)FLOP_2.getHeight(),this);
            g.drawImage(cardBack,(int)FLOP_3.getWidth(),(int)FLOP_3.getHeight(),this);
        }
        if (stage > 2)//turn
            turn(g);
        else
            g.drawImage(cardBack,(int)TURN.getWidth(),(int)TURN.getHeight(),this);
        if (stage > 3)//river
            river(g);
        else
            g.drawImage(cardBack,(int)RIVER.getWidth(),(int)RIVER.getHeight(),this);
        if (stage > 4)//show opponents cards
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
