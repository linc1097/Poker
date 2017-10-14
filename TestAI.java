import java.util.*;
import java.io.*;
/**
 * Write a description of class TestAI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestAI
{
    final int NUM_HANDS = 200;
    final int NUM_CHIPS = 1000;
    public double[] runThrough(double aggressive1, double loose1, double aggressive2, double loose2)
    {
        double[] answer = new double[12];
        int c1 = 0, c2 = 0, r1 = 0, r2 = 0, f1 = 0, f2 = 0;
        int[] results = new int[2];
        int p1Winnings = 0;
        int numGamesPlayed = 0;
        while (numGamesPlayed<NUM_HANDS)
        {
            AI p1 = new AI(NUM_CHIPS,aggressive1,loose1);
            AI p2 = new AI(NUM_CHIPS,aggressive2,loose2);
            AIvAI game = new AIvAI();
            results = game.run(p1,p2,numGamesPlayed,NUM_HANDS);
            numGamesPlayed+=results[0];
            p1Winnings += results[1];
            c1+= p1.getNumCalls();
            c2+= p2.getNumCalls();
            r1+= p1.getNumRaises();
            r2+= p2.getNumRaises();
            f1+= p1.getNumFolds();
            f2+= p2.getNumFolds();
        }
        double avgP1Winnings = p1Winnings/(double)(NUM_HANDS);
        answer[0] = aggressive1;
        answer[1] = loose1;
        answer[2] = aggressive2;
        answer[3] = loose2;
        answer[4] = avgP1Winnings;
        answer[5] = c1;
        answer[6] = r1;
        answer[7] = f1;
        answer[8] = c2;
        answer[9] = r2;
        answer[10] = f2;
        System.out.println("" + aggressive1 + loose1 + aggressive2 + loose2);
        return answer;
    }

    public void collectData() throws FileNotFoundException
    {
        PrintWriter out = new PrintWriter("data.txt");
        int count = 0;
        for (double A1 = 0;A1<1.1;A1+=.2)
            for (double L1 = 0;L1<1.1;L1+=.2)
                for (double A2 = A1;A2<1.1;A2+=.2)
                    for (double L2 = L1;L2<1.1;L2+=.2)
                    {
                        double[] results = runThrough(A1,L1,A2,L2);
                        out.println(results[0]);
                        out.println(results[1]);
                        out.println(results[2]);
                        out.println(results[3]);
                        out.println(results[4]);
                        double moves1 = results[5] + results[6] + results[7];
                        double moves2 = results[8] + results[9] + results[10];
                        out.println(results[5]/moves1);
                        out.println(results[6]/moves1);
                        out.println(results[7]/moves1);
                        out.println(results[8]/moves2);
                        out.println(results[9]/moves2);
                        out.println(results[10]/moves2);
                        count++;
                        for (int x = 0;x<5;x++)
                        {
                            System.out.println((double)(count/(double)666));
                        }
                    }
        out.close();
        System.out.println("Done! cross your fingers!");
    }
}
