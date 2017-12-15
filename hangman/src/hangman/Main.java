package hangman;

import java.util.Arrays;
import java.util.Scanner;
//TODO: add appearance description to readme
//TODO: add to readme: limbs remaining, print correct answer on lose, difficulty levels
public class Main {
	
	static boolean play = true;	// whether to continue playing
	static boolean[] guesses = new boolean[26]; // the letters that have been guessed
	static boolean complete = false; // whether this round is over
	static boolean win = false; // whether this round has been won or lost
	static boolean quit = false; // whether the player has quit in the middle of the round
	static int numWrong = 0; // the number of letters guessed incorrectly
	static int difficulty = 0; // the difficulty level of the game. 0 = easy, 1 = medium, 2 = hard
	static int maxWrong; // the number of letters that can be guessed incorrectly before the game ends
	static Scanner sc = new Scanner(System.in); // scanner to read user input
	static char[] key; // the answer to the puzzle
	static boolean[] inKey = new boolean[26]; // the letters present in key (not case-sensitive)
	
	/* converts a char to an int between 0-26 inclusive representing its position in the alphabet
	 * used to access guesses[] and inKey[]
	 * char -> int */
	private static int charToIndex(char c){
		return (int)(Character.toUpperCase(c)) - 65;
	}
	
	/* converts an int between 0-26 inclusive to an uppercase letter
	 * used to print the letter bank
	 * int -> char */
	private static char indexToChar(int i){
		return (char)(i+65);
	}
	
	/* checks whether the char corresponding to the ASCII value of index is a letter
	 * int -> boolean */
	private static boolean validLetter(int index){
		return ((index >= 65 && index <= 90) || (index >= 97 && index <= 122));
	}
	
	/* sets the indices of inKey to true based on which letters appear in the key
	 * note: ignores chars that are not letters */
	private static void initiateInKey(){
		int index;
		for (int i = 0; i < key.length; i++){
			if (validLetter(key[i])){
				index = charToIndex(key[i]);
				inKey[index] = true;
			}
		}
	}
	
	/* prompts for the difficulty level to be associated with the key
	 * the difficulty level is one of:
	 * - 0: "easy" - 6 wrong letter guesses allowed
	 * - 1: "medium" - 9 wrong letter guesses allowed
	 * - 2: "hard" - 14 wrong letter guesses allowed */
	private static void promptDifficulty(){
		String response;
		boolean good = false;
		while (!good){
			System.out.print("Enter a difficulty (easy/medium/hard): ");
			response = sc.nextLine().toLowerCase();
			switch(response){
			case "easy":
				good = true;
				difficulty = 0;
				maxWrong = 6;
				break;
			case "medium":
				good = true;
				difficulty = 1;
				maxWrong = 9;
				break;
			case "hard":
				good = true;
				maxWrong = 14;
				difficulty = 2;
				break;
			default:
				System.out.println("Invalid response. Please enter easy, medium, or hard.");
			}
		}
	}
	
	/* prompts for a guess repeatedly, until a valid guess is entered
	 * if the guess is:
	 * - empty: prints an error message, prompts again for a guess
	 * - more than one char and if:
	 *   + the guess is "quit": ends the game (sudden death)
	 *   + otherwise: accepts the guess and checks whether it is correct
	 * - a single char and if:
	 *   + char is a non-letter symbol: prints an error message, prompts again for a guess
	 *   + char is a letter and that letter has already been guessed: prints an error message,
	 *     prompts again for a guess
	 *   + char is a letter and that letter has not yet been guessed: accepts the guess, increments the
	 *     number of incorrect guesses if that letter is not in the key
	 *  */
	private static void promptGuess(){
		String response;
		int index;
		boolean good = false;
		while (!good){
			System.out.print("Guess: ");
			response = sc.nextLine();
			if (response.length() <= 0){
				System.out.println("Not a valid guess. Please enter a letter.");
			}
			else if (response.length() > 1){
				if (response.toLowerCase().equals("quit")){
					play = false;
					quit = true;
					good = true;
				}
				else{	/* if the user guesses the full word/phrase */
					checkComplete(response);
					good = true;
				}
			}
			else{
				index = charToIndex(response.charAt(0));
				if (!validLetter(response.charAt(0))){
					System.out.println("Not a valid guess. Please enter a letter.");
				}
				else{
					if (guesses[index]){
						System.out.println("You already guessed that letter!");
					}
					else{
						guesses[index] = true;
						if (!inKey[index]){
							numWrong++;
						}
						good = true;
					}
				}
			}
		}
	}
	
	/* checks whether the puzzle has been completed a.k.a. the key has been guessed
	 * note: this version of checkComplete is called when the guess is 2+ chars and is used only to compare the
	 *       word/phrase guess to the key. in other words, this checkComplete will not check whether each
	 *       individual letter has been guessed, only whether the word/phrase as a whole has been guessed
	 * - if the word/phrase has been guessed correctly: marks the round won, adds all the letters guessed to the
	 *   letter bank, and ends the round
	 * - otherwise: invokes sudden death by marking the round lost, max-ing out the number of incorrect guesses,
	 *   and ending the round */
	private static void checkComplete(String guess){
		char[] temp = guess.toCharArray();
		if (Arrays.equals(temp, key)){
			win = true;
			for (int i = 0; i < inKey.length; i++){
				if (inKey[i]){
					guesses[i] = true;
				}
			}
		}
		else{
			numWrong = maxWrong;
		}
		complete = true;
	}
	
	/* checks whether the puzzle has been completed a.k.a. the key has been guessed
	 * note: this version of checkComplete is called after every guess and is used only to check whether every
	 *       individual letter of the key has guessed. in other words, this checkComplete will not check whether
	 *       a word/phrase guessed is correct.
	 * - if all the letters of the key have been guessed: marks the round won, prints a congrats message, and
	 *   ends the round
	 * - if not all the letters have been guessed but there are no chances remaining: marks the round lost,
	 *   prints an incorrect message with the correct answer, ends the round
	 * - if not all the letters have been guessed but there are chances remaining: does nothing, allows the round
	 *   to continue */
	private static void checkComplete(){
		boolean temp = true;
		for (int i = 0; i < inKey.length; i++){
			if (inKey[i] && !guesses[i]){
				temp = false;
				break;
			}
		}
		if (!complete){
			complete = temp;
			if (temp){
				win = true;
			}
		}
		if (numWrong == maxWrong){
			complete = true;
		}
		if (complete){
			printWin();
		}
	}
	
	/* prints the result at the end of each round
	 * - if the key is guessed: prints win message
	 * - if the key is not guessed: prints lose message and the correct answer */
	private static void printWin(){
		if (win){
			System.out.println("Congrats! You guessed it!");
		}
		else{
			System.out.println("Incorrect! You lose!");
			System.out.print("The correct answer was: ");
			for (char c: key){
				System.out.print(c);
			}
			System.out.println();
		}
	}
	
	/* prints the letter bank of all the letters that have been guessed
	 * if the correct key is guessed, the letter bank will update to include all the letters in the
	 * key as well as the letter bank's previous contents */
	private static void printGuessedLetters(){
		//TODO: pretty format
		System.out.print("Letter Bank: ");
		for (int i = 0; i < guesses.length; i++){
			if (guesses[i]){
				System.out.print(indexToChar(i) + " ");
			}
		}
		System.out.println();
		System.out.println("Limbs Remaining: " + (maxWrong-numWrong));
	}
	
	
	/* prints the hangman figure, depending on the difficulty level associated with the key
	 * easy: prints just the hangman's limbs
	 * medium: prints the hangman's limbs and a face
	 * hard: prints the gallows, the hangman's limb, and a face */
	private static void printMan(){
		switch(difficulty){
			case 0:
				printManEasy();
				break;
			case 1:
				printManMedium();
				break;
			case 2:
				printManHard();
				break;
		}
	}
	
	/* prints the hangman figure for keys of easy difficulty
	 * prints just the hangman's limbs --> player has a total of 6 wrong letter guesses allowed */
	private static void printManEasy(){
		String hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    \n⎮    \n⎮     \n⎮    \n⎮\n";
		switch(numWrong){
			case 0:
				break;
			case 1:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮    \n⎮     \n⎮    \n⎮\n";
				break;
			case 2:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮     ⎮\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 3:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮    \\⎮\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 4:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮    \\⎮/\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 5:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮    \\⎮/\n⎮     ⎮\n⎮    /\n⎮\n";
				break;
			case 6:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮    ( )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
		}
		System.out.println(hangman);
	}
	
	/* prints the hangman figure for keys of medium difficulty
	 * prints the hangman's limbs and a face --> player has a total of 9 wrong letter guesses allowed */
	private static void printManMedium(){
		String hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮        \n⎮     \n⎮      \n⎮    \n⎮\n";
		switch(numWrong){
			case 0:
				break;
			case 1:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮     \n⎮      \n⎮    \n⎮\n";
				break;
			case 2:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮     ⎮\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 3:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 4:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮    \n⎮\n";
				break;
			case 5:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \n⎮\n";
				break;
			case 6:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 7:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ  )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 8:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ ಠ)\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 9:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ_ಠ)\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
		}
		System.out.println(hangman);
	}
	
	/* prints the hangman figure for keys of hard difficulty
	 * prints the gallows, the hangman's limbs, and a face --> player has a total of 14 wrong letter guesses
	 * allowed */
	private static void printManHard(){
		String hangman = "     \n       \n         \n        \n       \n        \n \n";
		switch(numWrong){
			case 0:
				break;
			case 1:
				hangman = "     \n       \n         \n        \n       \n⎮       \n⎮\n";
				break;
			case 2:
				hangman = "     \n       \n         \n⎮       \n⎮      \n⎮       \n⎮\n";
				break;
			case 3:
				hangman = "     \n⎮      \n⎮        \n⎮       \n⎮      \n⎮       \n⎮\n";
				break;
			case 4:
				hangman = "+⏤⏤⏤+\n⎮      \n⎮        \n⎮       \n⎮      \n⎮       \n⎮\n";
				break;
			case 5:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮        \n⎮       \n⎮      \n⎮       \n⎮\n";
				break;
			case 6:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮       \n⎮      \n⎮       \n⎮\n";
				break;
			case 7:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮     ⎮ \n⎮     ⎮\n⎮       \n⎮\n";
				break;
			case 8:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮ \n⎮     ⎮\n⎮       \n⎮\n";
				break;
			case 9:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮       \n⎮\n";
				break;
			case 10:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮    /  \n⎮\n";
				break;
			case 11:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (   )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 12:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ  )\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 13:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ ಠ)\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
			case 14:
				hangman = "+⏤⏤⏤+\n⎮     ┼\n⎮   (ಠ_ಠ)\n⎮    \\⎮/\n⎮     ⎮\n⎮    / \\\n⎮\n";
				break;
		} 
		System.out.println(hangman);
	}
	
	/* prints the blanks in the key
	 * - if the char is not a letter: prints the char
	 * - if the char is a letter that has already been guessed: prints the letter, following the capitalization in key 
	 * - if the char is a letter that has not yet been guessed: prints underscore (_) symbol in its place */
	private static void printBlanks(){
		for (int i = 0; i < key.length; i++){
			// If the char is not a letter (ASCII index between 65-90 or 97-122 inclusive), print the char
			if (!validLetter(key[i])){
				System.out.print(key[i]);
			}
			else if (guesses[charToIndex(key[i])]){
				System.out.print(key[i]);
			}
			else{
				System.out.print("_");
			}
		}
		System.out.println("\n");
	}
	
	/* prompts whether to play the game again
	 * if:
	 * - "quit" was previously entered: prints thanks for playing message, terminates the game
	 * - "N"/"n" is entered: prints thanks for playing message, terminates the game
	 * - "Y"/"y" is entered: resets variables, starts a new round */
	private static void playAgain(){
		boolean good = false;
		String temp;
		if (quit){
			System.out.println("Thanks for playing!");
			good = true;
		}
		while (!good){
			System.out.print("Would you like to play again? Y/N: ");
			temp = sc.nextLine();
			if (temp.length() > 1 || temp.length() <= 0){
				System.out.println("Invalid response");
			}
			else if (temp.equals("N") || temp.equals("n")){
				play = false;
				System.out.println("Thanks for playing!");
				good = true;
			}
			else if (temp.equals("Y") || temp.equals("y")){
				complete = false;
				numWrong = 0;
				win = false;
				for (int i = 0; i < guesses.length; i++){
					guesses[i] = false;
					inKey[i] = false;
				}
				good = true;
			}
			else{
				System.out.println("Invalid response");
			}
		}
	}

	public static void main(String[] args) {
		do{
			System.out.print("Enter a key: ");
			key = sc.nextLine().toCharArray();
			initiateInKey();
			promptDifficulty();
			System.out.println("\n\n\n\n\n\n\n\n\n");
			printMan();
			printBlanks();
			while (!complete && play){
				promptGuess();
				printGuessedLetters();
				printMan();
				printBlanks();
				checkComplete();
			}
			playAgain();
		}
		while (play);
		sc.close();

	}

}
