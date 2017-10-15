
/**
 * Represents the possibilitis of the two hand starting hands held by the players
 * 
 * @Lincoln Updike 
 * @10.14.17
 */
public class StartingHand
{
    private int rank1;
    private int rank2;
    private boolean isSuited;

    public StartingHand(int r1, int r2, boolean suited)
    {
        if (r1 > r2)
        {
            rank1 = r1;
            rank2 = r2;
        }
        else
        {
            rank1 = r2;
            rank2 = r1;
        }
        isSuited = suited;
    }

    public int getRank1()
    {
        return rank1;
    }

    public int getRank2()
    {
        return rank2;
    }

    public boolean getSuited()
    {
        return isSuited;
    }

    public boolean equals(Object x)
    {
        StartingHand other = (StartingHand)x;
        return (rank1 == other.getRank1()) && (rank2 == other.getRank2()) && (isSuited == other.getSuited());
    }

    public int hashCode()
    {
        int x;
        if (isSuited)
            x = 256;
        else
            x = 0;
        return x + rank2*16 + rank1;
    }
}
