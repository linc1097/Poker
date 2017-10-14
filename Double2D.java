import java.util.*;
/**
 * Write a description of class Double2D here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Double2D
{
    double[][] list;

    public Double2D(int a,int b)
    {
        list = new double[a][b];
    }

    public void set(double x, int a, int b)
    {
        list[a][b] = x;
    }

    public double get(int a, int b)
    {
        return list[a][b];
    }

    public boolean equals(Object obj)
    {
        Double2D other = (Double2D)obj;
        for (int a = 0;a<list.length;a++)
            for (int b = 0;b<list[0].length;b++)
                if (Math.abs(other.get(a,b)-this.get(a,b))>.005)
                    return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 1;
        int[] primes = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,
        59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,
        149,151,157,163,167,173,179,181,191,193,197,199};
        int index = 0;
        for (int a = 0;a<list.length;a++)
        {
            for (int b = 0;b<list[0].length;b++)
            {
                index = (int)(list[a][b]*5);
                if (a==0&&b==1)
                index+=6;
                else if (a==0&&b==2)
                index+=12;
                else if (a==1&&b==0)
                index+=18;
                else if (a==1&&b==1)
                index+=24;
                else if (a==1&&b==2)
                index+=30;
                hash*= primes[index];
            }
        }
        return hash;
    }

    public void testHash()
    {
        List<Integer> nums = new ArrayList<Integer>();
        for (double A1 = 0;A1<1.1;A1+=.2)
            for (double L1 = 0;L1<1.1;L1+=.2)
                for (double A2 = A1;A2<1.1;A2+=.2)
                    for (double L2 = L1;L2<1.1;L2+=.2)
                    {
                        Double2D d2d = new Double2D(2,2);
                        d2d.set(A1,0,0);
                        d2d.set(L1,0,1);
                        d2d.set(A2,1,0);
                        d2d.set(L2,1,1);
                        System.out.println("" + A1 + " " + L1 +" " + A2 + " " + L2);
                        System.out.println(d2d.hashCode());
                        for (int x = 0;x<nums.size();x++)
                            if (nums.get(x).equals(d2d.hashCode()))
                            {
                                System.out.println("fail");
                                return;
                            }
                        nums.add((Integer)d2d.hashCode());
                    }
        System.out.println("success");
    }
    
    public String toString()
    {
        String answer = "(";
        for (int x = 0;x<list.length;x++)
        {
            for (int y = 0;y<list[0].length;y++)
            {
               answer += list[x][y] + ", "; 
            }
        }
        answer = answer.substring(0,answer.length()-2);
        answer += ")";
        return answer;
    }
}
