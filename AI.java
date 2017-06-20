
/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AI extends Player
{
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;
    private Card card5;
    private Card card6;
    private Card card7;
    private Card opponentCard1;
    private Card opponentCard2;
    
    public AI(int numChips)
    {
        super(numChips);
    }
    
    public void newHand(Card a, Card b)
    {
        card1 = a;
        card2 = b;
    }
    
    public void addFlop(Card a, Card b , Card c)
    {
        card3 = a;
        card4 = b;
        card5 = c;
    }
    
    public void addRiver(Card a)
    {
        card6 = a;
    }
    
    public void addTurn(Card a)
    {
        card7 = a;
    }
    
    public void seeOpponentsCards(Card a, Card b)
    {
        opponentCard1 = a;
        opponentCard2 = b;
    }
    
    public void act()
    {
        if (Game.bet == 0)//will check if it can
        {
            call();
        }
        else if (Game.bet > 100)//if bet is over 100, folds
        {
            fold();
        }
        else if (Game.bet%2!=0)//if bet is odd, raises 11
        {
            raise(1);
        }
        else//if bet is even, calls
        {
            call();
        }
    }
}
