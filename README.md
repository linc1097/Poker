# Heads Up Poker AI
This game allows a user to play heads up, no limit hold'em against an AI that collects data
and alters its playing style in order to beat the user. **To run the program download and run the poker.jar
file above**
## Creating The Poker Environment
The poker game itself that the AI plays was made with my knowledge of the rules of poker and with help
from online tutorials. The main game loop uses BufferedImages and drawn text and shapes to display
the graphics to the user. The poker game is made so that an AI can call simple methods like `call()`, 
`fold()`, and `raise(int num)` to play against the user. 
## The AI
One of the main things the AI uses to make decisions is the percentage chance it will win the hand if the hand goes to
showdown. This was reletively easy to find pre-flop, because there are only 169 distinct starting hands since the only
factors that need to be considered are suited or unsuited and the ranks of the cards. The odds of winning the hand with
each starting hand are known, so a map containing the percentage chance of winning is used to determine those 
odds. After the flop comes however, it becomes much more challenging to determine the odds of winning the hand.
To accomplish this, the AI runs through every possible card that could come and keeps track of the number of
hands the AI wins and loses. After the turn and river, this happens quickly and does not slow down the game.
After the flop however, there are 1070190 possible outcomes, which means the AI has to evaluate the winner of 
that many hands. With my first poker hand evaluator, this process took over a minute, which made the game basically
unplayable. 
## Quickly Comparing Seven Card Poker Hands
My first attempt at a method that finds the winner between two seven card poker hands took too long. I researched
and brainstormed ways to speed it up. I came across a website: http://suffe.cool/poker/evaluator.html. On this
website it explained that for 5 card poker hands, there are only 7462 distinct 5 card poker hands when the only 
things considered are if there is a flush and the ranks of the cards. Also, it is easy to represent each distinct
poker hand as an integer by starting with 1, and multiplying by a specific prime number for each rank of card in
the hand and multiplying by another, different, prime number if there was a flush. With these new ideas, I decided
to make a map where the key is the integer representation of the hand and the value is a number between 1 and 7462
that represents the rank of that hand compared to other hands. Because this only evaluates five card hands, and
each seven card hand contains 21 different five card hands, to compare two hands, 42 five card
hands had to be evaluated. When I ran the game with this method, it still took about a minute to find the 
percentage chance of winning on the flop. looking in a map 21 times to evaluate each hand was taking
too long. With some inspiration from my dad and some of his colleagues, I decided to use a sparsely populated array
for the lookup. The index at certain points on the array represents the integer hand value, and the value at that index
is the rank of that hand compared to other five card hands. The range of integers that represented hands was too large, however,
so each hand value was taken mod 2^21. This made it so that there were minimal times when two hands were represented by
the same index on the array, and all of the integers that represented specific five card poker hands would be in the range 
(0,2096696). Here is the method that populates the sparse array: 
```java
        int[] array = new int[2096696];  // sparsely populated array
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
There were 10 repeats because the numbers had to be modded by 2^21, and those were stored in a map. The map is
only used a fraction of the time. Because getting the value at a certain index of an array is much faster than
looking up a key in a map, this sped up the poker hand evaluator considereably, and now the 
1070190 hands that needed to be evaluated to get a percent chance the AI wins the hand could be evaluated in about
0.6 seconds. 
## Altering the AI's Play Style Based on the Opponent
The AI alters it's play style according to how the user is playing. In order to change its own play style, 
the AI holds two doubles, `aggressive`, which represents how aggressively the AI plays, and `loose`, which
represents how loose that AI plays. Both are floating point values between 0 and 1. `agressive` represents how often
a player raises (a higher aggressive setting leads to more raises) This is shown in the graph below, which 
shows the percentage of the time an AI raises each time it has to make a decision plotted against the aggressive
setting.
![alt text](https://github.com/linc1097/Poker/blob/master/poker/pictures/graph%20aggressive.PNG "graph of raises vs. aggressive setting")

`loose` represents how willing the AI is to call. (A higher loose setting means more calling and less folding)
The graph below shows the loose setting plotted against the percentage of the time the AI folds at each point 
it makes a decision.

![alt text](https://github.com/linc1097/Poker/blob/master/poker/pictures/graph%20loose.PNG "graph of folds vs. loose setting")

This data was obtained by having the AI play itself in every combination (from 0 - 1, by increments of .2) of `loose` 
and `aggressive` settings for 200 hands. Data from having the AI play itself is also used to determine how the 
AI should play based on the play style of its opponent. The percent times each style of play (36 distinct styles of
play based on `aggressive` and `loose` settings) calls folds and raises was recorded, and the AI keeps track of 
the percent time the user calls folds and raises. Once enough data is gathered, the AI determines which play style
the user is playing most similarly to. Then the AI uses the data to determine which play style it should use to play
best against the user. For example, if the AI determines that the user is playing like `aggressive = 0` and
`loose = 0`, then the AI will choose the optimal aggressive and loose settings that maximize its average winnings.
This is an example of the data it looks at:

![alt text](https://github.com/linc1097/Poker/blob/master/poker/pictures/table%20done.PNG "table")

The values in the center of the table represent the average winnings per hand of that playing style against the
opponent, which is set to 0,0. This data was collected beforehand and is stored in a txt file that the AI reads. 
In this case, the highest average winnings are 17.25 by an AI set to (0.6, 0.6). As a result, if the user is determined
to be playing most like an AI set to (0,0), the AI will set itself to (0.6, 0.6). 
## The Process
In AP Computer Science class, we had a project where we made a program to evaluate poker hands. I thought it was
really cool. I had also been playing poker with my friends a lot and had read some books on strategy. I am still
very interested in the game of poker. Over the summer I embarked on this project, with the goal of making something
that could beat my friends and maybe even myself. In its current state, I am very happy to say that it can beat 
a lot of my friends most of the time, and even when I play against it (which is a little unfair because I know exactly
how it thinks) I have a hard time beating it. It is mostly finished but I still find little things I want to tweak 
every time I look at it. The problems I had to solve to make this program were very fun and interesting to me, and I
am glad I put in the time and energy to make something that I think is really cool.
