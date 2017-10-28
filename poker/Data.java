import java.io.*;
import java.util.*;
/**
 * Analyzes data from data.txt, which contains information gathered from AI playing itself
 * set to different play styles. Used by AI
 * 
 * @Lincoln Updike
 * @10.27.17
 */
public class Data
{
    /**
     * returns two maps conatining data from data.txt
     */ 
    public Map[] readFile() throws FileNotFoundException, IOException
    {
        //BufferedReader file = new BufferedReader(new FileReader("data.txt"));
        BufferedReader file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("data.txt")));
        Map<Double2D,Double> map1 = new HashMap<Double2D,Double>();
        Map<Double2D,Double2D> map2 = new HashMap<Double2D,Double2D>();
        Map[] answer = new Map[2];
        answer[0] = map1;
        answer[1] = map2;
        String line;
        int count = 0;
        Double avgChipWin = 0.;
        Double2D style = new Double2D(2,2);
        Double2D percents = new Double2D(2,3);
        while ((line = file.readLine())!=null)
        {
            if (count == 0)
            {
                style = new Double2D(2,2);
                percents = new Double2D(2,3);
                style.set(Double.parseDouble(line),0,0);
            }
            else if (count == 1)
                style.set(Double.parseDouble(line),0,1);
            else if (count == 2)
                style.set(Double.parseDouble(line),1,0);
            else if (count == 3)
                style.set(Double.parseDouble(line),1,1);
            else if (count == 4)
            {
                avgChipWin = new Double(Double.parseDouble(line));
            }
            else if (count == 5)
                percents.set(Double.parseDouble(line),0,0);
            else if (count == 6)
                percents.set(Double.parseDouble(line),0,1);
            else if (count == 7)
                percents.set(Double.parseDouble(line),0,2);
            else if (count == 8)
                percents.set(Double.parseDouble(line),1,0);
            else if (count == 9)
                percents.set(Double.parseDouble(line),1,1);
            else if (count == 10)
            {
                percents.set(Double.parseDouble(line),1,2);
                Double2D style1 = new Double2D(2,2);
                Double2D percents1 = new Double2D(2,3);
                for (int a = 0; a<2; a++)
                    for (int b = 0; b<2; b++)
                        style1.set(new Double(style.get(a,b)),a,b);

                for (int a = 0; a<2; a++)
                    for (int b = 0; b<3; b++)
                        percents1.set(new Double(percents.get(a,b)),a,b);

                map1.put(style1,avgChipWin);
                map2.put(style1,percents1);
            }
            if (count == 10)
                count =0;
            else
                count++;
        }
        return answer;
    }

    /**
     * prints info to be used in table on README.md
     */
    public void printTableInfo()throws FileNotFoundException,IOException
    {
        Map[] maps = readFile();
        Double2D players = new Double2D(2,2);
        players.set(0,0,0);//p1 aggressive
        players.set(0,0,1);//p1 loose
        for (double a = 0.0;a<1.1;a+=.2)
            for (double b = 0.0;b<1.1;b+=.2)
            {
                players.set(a,1,0);
                players.set(b,1,1);
                System.out.println((int)(a*10) + "," + (int)(b*10) + ": " + 
                    maps[0].get(players));
            }
    }

    /**
     * prints info to be used on the graphs of the README.md
     */
    public void printGraphInfo()throws FileNotFoundException,IOException
    {
        Map[] maps = readFile();
        double[] percs = new double[6];
        for (double x = 0;x<1.1;x+=.2)
            for (double y = 0;y<1.1;y+=.2)
            {
                Double2D percentages = getPercentages(maps[1], x, y);
                percs[(int)(y*5)] += percentages.get(0,2);
            }
        for (int i = 0;i<percs.length;i++)
        {
            System.out.println(percs[i]/6);
        }
    }

    /**
     * takes an aggressive and loose setting and finds the setting that p;ays the 
     * best against the given setting
     */
    public Double2D findBest(Map map, double agg, double loose)
    {
        Double2D check = new Double2D(2,2);
        check.set(agg,0,0);
        check.set(loose,0,1);
        Double2D answer = new Double2D(1,2);
        double high = 0;
        int count = 0;
        for (double A2 = 0;A2<1.1;A2+=.2)
            for (double L2 = 0;L2<1.1;L2+=.2)
            {
                check.set(A2,1,0);
                check.set(L2,1,1);
                if (map.get(check)!=null)
                {
                    count++;
                    if ((double)map.get(check)*-1>high)
                    {
                        high = (double)map.get(check)*-1;
                        answer.set(A2,0,0);
                        answer.set(L2,0,1);
                    }
                }
            }
        check.set(agg,1,0);
        check.set(loose,1,1);
        for (double A2 = 0;A2<1.1;A2+=.2)
            for (double L2 = 0;L2<1.1;L2+=.2)
            {
                check.set(A2,0,0);
                check.set(L2,0,1);
                if (map.get(check)!=null)
                {
                    count++;
                    if ((double)map.get(check)>high)
                    {
                        high = (double)map.get(check);
                        answer.set(A2,0,0);
                        answer.set(L2,0,1);
                    }
                }
            }
        return answer;
    }

    public void testFindBest()throws FileNotFoundException,IOException
    {
        Map[] maps = readFile();
        Double2D a = findBest(maps[0],.2,.2);
        System.out.println(a.get(0,0) + " " + a.get(0,1));
    }

    /**
     * returns the percentage time the given aggressive and loose setting
     * calls, folds, and raises
     */
    public Double2D getPercentages(Map map, double agg, double loose)
    {
        Double2D check = new Double2D(2,2);
        check.set(agg,0,0);
        check.set(loose,0,1);
        Double2D answer = new Double2D(1,3);
        int count = 0;
        double calls = 0;
        double raises = 0;
        double folds = 0;
        for (double A2 = 0;A2<1.1;A2+=.2)
            for (double L2 = 0;L2<1.1;L2+=.2)
            {
                check.set(A2,1,0);
                check.set(L2,1,1);
                if (map.get(check)!=null)
                {
                    count++;
                    calls+= ((Double2D)map.get(check)).get(0,0);
                    raises+= ((Double2D)map.get(check)).get(0,1);
                    folds+= ((Double2D)map.get(check)).get(0,2);
                }
            }
        check.set(agg,1,0);
        check.set(loose,1,1);
        for (double A2 = 0;A2<1.1;A2+=.2)
            for (double L2 = 0;L2<1.1;L2+=.2)
            {
                check.set(A2,0,0);
                check.set(L2,0,1);
                if (map.get(check)!=null)
                {
                    count++;
                    calls+= ((Double2D)map.get(check)).get(1,0);
                    raises+= ((Double2D)map.get(check)).get(1,1);
                    folds+= ((Double2D)map.get(check)).get(1,2);
                }
            }
        calls /= count;
        raises /= count;
        folds /= count;
        answer.set(calls,0,0);
        answer.set(raises,0,1);
        answer.set(folds,0,2);
        return answer;
    }

    public void testGetPercentages()throws FileNotFoundException,IOException
    {
        Map[] maps = readFile();
        Double2D a = getPercentages(maps[1],.0,.0);
        System.out.println(a);
    }

    /**
     * returns a map where the keys are a Double2D of play styles and what it unlocks
     * is a Double2D containing the percentage time that style raises, calls, and folds
     */
    public Map percentages()
    {
        try {
            Map[] maps = readFile();
            Map<Double2D,Double2D> answer = new HashMap<Double2D,Double2D>();
            for (double a = 0;a<1.1;a+=.2)
                for (double b = 0;b<1.1;b+=.2)
                {
                    Double2D x = new Double2D(1,2);
                    x.set(a,0,0);
                    x.set(b,0,1);
                    answer.put(x , getPercentages(maps[1],a,b));
                }
            return answer;
        } catch (Exception e)
        {
            throw new IndexOutOfBoundsException("lol u suck");
        }
    }

    /**
     * returns a map of the key being a given aggressive/loose combination and what it
     * unlocks is the aggreessive/loose setting that wins the most chips per hand on
     * average against the key's settings.
     */
    public Map matchups()
    {
        try {
            Map[] maps = readFile();
            Map<Double2D,Double2D> answer = new HashMap<Double2D,Double2D>();
            for (double a = 0;a<1.1;a+=.2)
                for (double b = 0;b<1.1;b+=.2)
                {
                    Double2D x = new Double2D(1,2);
                    x.set(a,0,0);
                    x.set(b,0,1);
                    answer.put(x , findBest(maps[0],a,b));
                }
            return answer;
        } catch (Exception e)
        {
            throw new IndexOutOfBoundsException("lol u suck");
        }
    }
}
