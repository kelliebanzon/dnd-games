package hangman;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	static boolean play = true;
	static boolean[] guesses = new boolean[128];
	static boolean complete = false;
	static boolean win = false;
	static boolean quit = false;
	static Scanner sc = new Scanner(System.in);
	static char[] key;
	
	private static void promptGuess(){
		String temp;
		char lowercase, uppercase;
		boolean good = false;
		while (!good){
			System.out.print("Guess: ");
			temp = sc.nextLine();
			if (temp.length() <= 0){
				System.out.println("Not a valid guess.");
			}
			else if (temp.length() > 1){
				if (temp.toLowerCase().equals("quit")){
					play = false;
					quit = true;
					good = true;
				}
				else{
					checkComplete(temp);
					good = true;
				}
			}
			else{
				lowercase = temp.toLowerCase().charAt(0);
				uppercase = temp.toUpperCase().charAt(0);
				if (guesses[(int)lowercase]){
					System.out.println("You already guessed that letter!");
				}
				else{
					guesses[(int)lowercase] = true;
					guesses[(int)uppercase] = true;
					good = true;
				}
			}
		}
	}
	
	private static void checkComplete(String guess){
		char[] temp = guess.toCharArray();
		if (Arrays.equals(temp, key)){
			win = true;
		}
		complete = true;
		checkWin();
	}
	
	private static void checkComplete(){
		boolean temp = true;
		for (int i = 0; i < key.length; i++){
			if (!guesses[(int)key[i]]){
				temp = false;
			}
		}
		if (!complete){
			complete = temp;
			if (temp){
				win = true;
			}
		}
		if (complete){
			checkWin();
		}
	}
		
	public static void checkWin(){
		if (win){
			System.out.println("Congrats! You guessed it!");
		}
		else{
			System.out.println("Incorrect! You lose!");
		}
	}
	
	private static void printGuessedLetters(){
		//TODO: pretty format
		System.out.print("Letter Bank: ");
		for (int i = 65; i < 91; i++){
			if (guesses[i]){
				System.out.print((char)i + " ");
			}
		}
		System.out.println();
	}
	
	private static void printMan(){
		//TODO: print dude
		System.out.print("");
	}
	
	private static void printBlanks(){
		for (int i = 0; i < key.length; i++){
			if (!(((int)key[i] >= 65 && (int)key[i] <= 90) || ((int)key[i] >= 97 && (int)key[i] <= 122))){
				System.out.print(key[i]);
			}
			else if (guesses[(int)key[i]]){
				System.out.print(key[i]);
			}
			else{
				System.out.print("?");
			}
		}
		System.out.println("\n");
	}
	
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
