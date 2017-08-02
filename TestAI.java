
/**
 * Write a description of class TestAI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestAI
{
    public void run()
    {
        double aggressive = 0;
        double loose = 0;
        AI a = new AI(1000,.5,.5);
        for (int z = 0;z<4;z++)
        {
            for (int y = 0;y<4;y++)
            {
                AI b = new AI(1000,aggressive+(.25*z),loose+(.25*y));
                int win = 0;
                for (int x = 0;x<5;x++)
                {
                    AIvAI game = new AIvAI();
                    win+=game.run(b,a);
                }
                System.out.println("agressive: " + aggressive+(.25*z) + "loose: " + loose+(.25*y));
            }
        }
    }
    
    public void test()
    {
        AI a = new AI(1000,1.0,1.0);
        AI b = new AI(1000,.7,.2);
        AIvAI game = new AIvAI();
        System.out.println(game.run(a,b));
    }
}
