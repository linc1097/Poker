
/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AI extends Player
{
    private int numRaises = 0;

    public AI(int numChips)
    {
        super(numChips);
    }

    public void actPreFlop()
    {
        int rankSum = cards.get(0).getRank() + cards.get(1).getRank();
        if (Game.bet == 20 && this.alreadyIn == 10)
        {
            if (rankSum < 16)
                fold();
            else if (rankSum < 20)
                call();
            else
            {
                int raiseNum = 10*(int)(Math.random()*3+1);
                raise(raiseNum);
                numRaises++;
            }
        }
        else if (Game.bet>20)
        {
            numRaises++;
            boolean smallBet = Game.bet<50;
            if (rankSum < 16)
                fold();
            else if (smallBet && rankSum <20)
                call();
            else if (rankSum > 20)
            {
                if (randBool(.22))
                {
                    int raiseNum = (int)((Game.bet - alreadyIn)*(Math.random()*2+1));
                    raise(raiseNum);
                    numRaises++;
                }
                else
                {
                    call();
                }
            }
            else
                fold();
        }
        else
        {
            if (rankSum > 20)
            {
                int raiseNum = 10*(int)(Math.random()*3+1);
                raise(raiseNum);
                numRaises++;
            }
            else
                call();
        }

    }

    public boolean randBool(double percentTrue)
    {
        return Math.random()<percentTrue;
    }

    public void act()
    {
        if (Game.stage == Game.PRE_FLOP)
        {
            actPreFlop();
        }
        else
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
}
