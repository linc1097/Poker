import java.util.*;
/**
 * Write a description of class TestAI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestAI
{
    final int NUM_GAMES = 20;
    public void test()
    {
        int player1 = 0;
        int player2 = 0;
        int hands = 0;
        for (int x = 0;x<NUM_GAMES;x++)
        {
            AI p1 = new AI(1000,.9,.1);
            AI p2 = new AI(1000,.2,.9);
            AIvAI game = new AIvAI();
            List<Object> results = game.run(p1,p2);
            hands+= (int)results.get(0);
            if ((AI)results.get(1)==p1)
            player1++;
            else
            player2++;
            System.out.println("player 1: " + player1 + " player 2: " + player2);
            double avgP1Winnings = (double)((player1+player2)*1000)/(double)hands;
            if (player1>player2)
            System.out.println("player 1 avg: " + avgP1Winnings);
            else if (player1<player2)
            System.out.println("player 2 avg: " + -1*avgP1Winnings);
            else
            System.out.println("tie");
            System.out.println("hands: " + hands);
        }
        System.out.println("player 1: " + player1 + " player 2: " + player2);
    }
}
