
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
        AI a = new AI(1000,.5,.5);
        AI b = new AI(1000,.5,.5);
        AIvAI game = new AIvAI();
        System.out.println(game.run(a,b));
    }
}
