# DnD Minigames
A series of minigames I wrote to incorporate into my party's weekly rounds of Dungeons and Dragons (or, as we like to call it, Dungeons and Daphnes).


## Hangman
A two-player hangman implementation: Player 1 (P1) sets the word/phrase for Player 2 (P2) to guess.

#### Features:
* **Enter a key:** The answers to the puzzles (called keys) are not hard-coded. The game prompts for a key at the beginning of each round. 
* **Guess a letter or word/phrase:**
  * If a letter is guessed correctly, it will appear in the key.
  * If a letter is guessed incorrectly, it will add a limb to the hangman.
  * If a word/phrase is guessed correctly, the player wins and the round ends.
  * If a word/phrase is guessed incorrectly, the hangman dies instantly and the round ends.
* **Play again:** At the end of each round, the game asks if the player would like to start a new puzzle.

#### Pending Features:
* **One-player mode:** Offer a choice between one-player or two-player mode before starting the game. One-player mode would choose a random word from a list of presets.
* **Difficulty level:** After prompting for a key, prompt for a difficulty level. This feature would allow P1 to determine how many features should be added to the hangman before the game ends (i.e. basic stick figure, stick figure + face, stick figure + face + clothes, etc.).
* **Free hints:** After prompting for a key, prompt for free hints. This feature would allow P1 to add hints that P2 can access to try to guess the key. Free hints do not cost a body part.
* **Costly hints:** After prompting for a key and free hints, prompt for costly hints. This feature would allow P1 to add hints that P2 can access to try to guess the key. Accessing these hints costs 1 body part per hint.


## Pending Games:
1. Buzzfeed quizzes
2. Crossword
3. Maze
