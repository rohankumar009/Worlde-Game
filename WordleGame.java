import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class WordleGame {
    
    // A STRING ARRAY THAT STORES ALL THE AVAILABLE WORDS FOR THE GAME- THERE WASN NO SPECIFICATION IN THE PROJECT WRITE UP ON HOW MANY WORDS I NEEDED TO USE SO I MADE A BUNCH FOR PRACTICAL
    private static final String[] WORDS = {"WHICH", "THERE", "MOLLY", "HOLLY", "WHERE", "RIGHT", "MINNE", "HELLO", "JELLO", "APPLE","UNDER", "MIGHT", "WHILE", "HOUSE", "WORLD", "BELOW", "ASKED", "GOING", "LARGE", "UNTIL", "ALONG", "SHALL", "BEING", "OFTEN", "EARTH", "BEGAN", "SINCE"};
    
    // THE TOTAL MAX NUMBER OF GUESSES ALLOWED FOR THE GAME
    private static final int MAX_GUESSES = 6;
    private static final String FILENAME = "/Users/rohankumar/Desktop/CSCI 2081/Worlde/src/Usedwords.txt"; // PUT THE USED WORDS ON DISPLAY HERE
    private static Set<String> usedWords = new HashSet<>(); // WHERE MY USED WORDS WOULD BE STORED
    private static String secretWord;
    private static final String BG_GREEN = "\u001b[42m"; // FOUND THIS ON A STACKOVERFLOW PAGE AS RECOMMENDED BY A TA
    private static final String BG_YELLOW = "\u001b[43m";
    private static final String RESET = "\u001b[0m";
    
    public static void main(String[] args) {
        // THIS WILL SELECT A RANDOM WORD FROM MY HASHSET AND THE PROGRAM WILL BE ABLE TO USE WORDS WITH REPEATING LETTERS AS I AM ABLE TO HANDLE THE LOCATION OF EACH LETTER INDIVIDUALLY
        Random random = new Random();
        String secretWord = WORDS[random.nextInt(WORDS.length)];
        System.out.println("The secret word ONLY has " + secretWord.length() + " letters.");
    
        do { // THIS WILL BE ABLE OT MAKE SURE THE WORD THAT IS BEING GUESSED IN THE GAME HAS 5 LETTERS- NO MORE NO LESS
            secretWord = WORDS[random.nextInt(WORDS.length)];
        } while (secretWord.length() < 5);
        
        // THIS IS COMMENTED OUT AS IN THE REAL GAME YOU CANNOT SEE THE OFFICIAL ANSWER UNTIL THE GAME IS OVER BY WINNER OR LOSER
        // THE COMMENT BELOW HELPED ME TEST MY CODE AS I DID NOT WANT TO PLAY MY OWN GAME TO FIND THE ANSWER AS IT IS BEING RANDOMIZED
         System.out.println("Secret word: " + secretWord);
        
        // READING THE USED WORDS FROM FILE
        try (BufferedReader br = new BufferedReader(new FileReader("Usedwords.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                usedWords.add(line.toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading used words file: " + e.getMessage());
        }
        
        // THE PROMPT TO MAKE THE USER TO GUESS A WORD
        Scanner scanner = new Scanner(System.in);
        int guessesLeft = MAX_GUESSES;
        String guess;
        boolean hasWon = false;
    
        // LOOP TO RUN THE GAME UNTIL THE PLAYER WINS OR RUNS OUT OF GUESSES
        while (guessesLeft > 0 && !hasWon) {
            System.out.printf("Guess a word (%d guesses left): ", guessesLeft);
            guess = scanner.next().toUpperCase();
            if (usedWords.contains(guess)) {
                System.out.println("You have already guessed that word. Try again.");
                continue;
            }
            usedWords.add(guess);
            System.out.println("Previous guesses: " + usedWords);
            if (guess.equals(secretWord)) {
                hasWon = true;
            } if (guess.length() != 5) {
                System.out.println("Invalid");
                guessesLeft--;
            } else {
                guessesLeft--;
                System.out.println("Incorrect! Try again.");
                
                // THIS WILL CHECK FOR SIMILAR LETTERS
                char[] guessLetters = guess.toCharArray();
                char[] secretLetters = secretWord.toCharArray();
                for (int i = 0; i < guessLetters.length; i++) {
                    if (guessLetters[i] == secretLetters[i]) {
                        System.out.print(BG_GREEN + guessLetters[i] + RESET);
                    } else if (new String(secretLetters).contains(String.valueOf(guessLetters[i]))) {
                        System.out.print(BG_YELLOW + guessLetters[i] + RESET);
                    } else {
                        System.out.print(guessLetters[i]);
                    }
                }
                System.out.println();
            }
        }
        
        
        // PRINTING THE USED WORDS INTO THE OUTPUT TEXT FILE WHICH UPDATES THE WORDS THAT HAVE BEEN USED ALREADY
        try {
            FileWriter writer = new FileWriter(new File(FILENAME), true);
            for (String word : usedWords) {
                writer.write(word + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
        if (hasWon) {
            System.out.println("You have solved the wordle!");
            System.out.println("The secret word was: " + secretWord);
        } else {
            System.out.println("Sorry you lose.");
            System.out.println("The secret word was: " + secretWord);
        }
    }
}
