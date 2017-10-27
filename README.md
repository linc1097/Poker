# Heads Up Poker AI
Contains game which allows a user to play heads up, no limit hold'em against an AI that collects data
and alters its playing style in order to beat the user.
## Creating The Poker Environment
The poker game itself that the AI plays was made with my knowledge of the rules of poker and with help
from online tutorials. The main game loop uses a BufferedImages and drawn text and shapes to display
the graphics to the user. The game loop continually runs, and the images which appear on screen are 
redrawn continuously until action by the user or AI prompts the game to display different information
to the user. This was a challenge when designing the poker game, because the game did not follow a logical
progression. The game loop was contantly moving through all of the methods. As a result, lots of booleans
and Enums are used to keep track of what stage the game is in, and what information should be displayed. 
The graphics and rules of the poker game are held in the Game class. 
## The AI
One of the main things the AI uses to make decisions is the percentage chance it will win the hand. This 
was reletively easy to find pre-flop, because there are only 169 distinct starting hands when the only factors
being considered are suited or unsuited and the ranks of the cards. The odds of winning the hand with each
starting hand are known, so a map containing the percentage chance of winning is used to determine those 
odds. After the flop comes however, it becomes much more challenging to determine the odds of winning the hand.
To accomplish this, the AI runs through every possible card that could come and keeps track of the number of
hands the AI wins and loses. After the turn and river, this happened quickly and did not slow down the game.
After the flop however, there are 1070190 possible outcomes, which means the AI had to evaluate the winner of 
that many hands. With my first poker hand evaluator, this process took over a minute, which made the game basically
unplayable. 
## Quickly Comparing Seven Card Poker Hands
My first attempt at a method that finds the winner between two seven card poker hands took too long. I researched
and brainstormed ways to speed it up. I came across a website: http://suffe.cool/poker/evaluator.html. On this
website it explained that for 5 card poker hands, there are only 7462 distinct 5 card poker hands when the only 
things considered are if there is a flush and the ranks of the cards. Also, it is easy to represent each distinct
poker hand as an integer by starting with 1, and multiplying by a specific prime number for each rank of card in
the hand and multiplying by another, different, prime number if there was a flush. With these new ideas, I decided
to make a map where the key is the integer representation of the hand and the value it unlocks is a number
between 1 and 7462 that represents the rank of that hand compared to other hands. Because this only evaluates 
five card hands, and each seven card hand contains 21 different five card hands, to compare two hands, 42 five card
hands had to be evaluated, and when I ran the game with this method, it still took about a minute to find the 
percentage chance of winning when the flop came out. looking in a map 21 times to evaluate each hand was taking
too long. With some inspiration from my dad, I decided to make a sparse array. The index at certain points on the
array represents the integer hand value, and the value in that index is the rank of that hand compared to other
five card hands. The range of integers that represented hands was too large however, so each hand value was taken
mod 2^21. This made it so that there were minimal times when two hands were represented by the same index on the
sparse array, and all of the integers that represented specific five card poker hands would be in the range 
(0,2096696). Here is the method that populates the sparse array: 
```java
        int[] array = new int[2096696];  // sparse array
        int[] repeats = new int[10];  // contains any indexes that represent more than one hands
        for (int x = 0;x<hands.length;x++){  // hands contains int values of hands ordered by strength of hand
            int mod = hands[x]%(1024*1024*2);
            if (array[mod] == 0)
                array[mod] = x+1;
            else
                add(repeats,mod);
        }
        for (int x = 0;x<repeats.length;x++)  // sets any index that represents more than one hand to zero
            array[repeats[x]] = 0;
        return array;
```

The AI alters it's play style according to how the user is playing. In order to alter its own play style, 
the AI holds two doubles, aggressive, which represents how aggressively the AI plays, and Loose, which
represents how loose that AI plays. 
