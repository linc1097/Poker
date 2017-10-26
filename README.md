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
The AI uses a combination of 
