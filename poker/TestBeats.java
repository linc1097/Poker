import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Used to test and debug the beats method of PokerHand, which is responsible for evaluating
 * and comparing hands in poker
 * 
 * @Updike
 * @6.18.17
 */
public class TestBeats
{
    /**
     * starts the game
     */
    public static void main(String[] args)
    {
        Map<Character, Integer> m = new HashMap<Character, Integer>();
        m.put('c', Card.CLUBS);
        m.put('s', Card.SPADES);
        m.put('h', Card.HEARTS);
        m.put('d', Card.DIAMONDS);
        m.put('2', Card.TWO);
        m.put('3', Card.THREE);
        m.put('4', Card.FOUR);
        m.put('5', Card.FIVE);
        m.put('6', Card.SIX);
        m.put('7', Card.SEVEN);
        m.put('8', Card.EIGHT);
        m.put('9', Card.NINE);
        m.put('t', Card.TEN);
        m.put('j', Card.JACK);
        m.put('q', Card.QUEEN);
        m.put('k', Card.KING);
        m.put('a', Card.ACE);

        Scanner input = new Scanner(System.in);

        while (input.hasNextLine()){
            String hands = input.nextLine();
            hands = hands.replaceAll(" ", "");

            // skip empty lines and commented out lines
            if (hands.isEmpty() || hands.charAt(0) == '#')
                continue;

            int idx = 0;
            Card pl1 = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card pl2 = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card ai1 = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card ai2 = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card b1  = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card b2  = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card b3  = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card b4  = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            Card b5  = new Card(m.get(hands.charAt(idx+1)), m.get(hands.charAt(idx))); idx += 2;
            PokerHandOriginal pl = new PokerHandOriginal(new ArrayList<Card>(Arrays.asList(pl1, pl2, b1, b2, b3, b4, b5)));
            PokerHandOriginal ai = new PokerHandOriginal(new ArrayList<Card>(Arrays.asList(ai1, ai2, b1, b2, b3, b4, b5)));
            int result = pl.beats(ai);
            int expected = Integer.parseInt(Character.toString(hands.charAt(idx)));
            if (result != expected) {
                System.out.println("Player: " + pl);
                System.out.println("AI:     " + ai);
                System.out.println("expected: " + expected + ", actual: " + result + "\n");
            }
        }
    }
}
