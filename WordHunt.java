/**
 * COMSW3137
 * Assignment 5 Problem 2
 * Kylie Wu
 * kcw2141
 *
 * Runs the entire word hunt
 */

import java.io.*;
import java.util.*;

public class WordHunt
{
    static String[][][] letters;
    static final String NUMBERS = "1234567890";
    static final int TABLE_SIZE = 304961; //304961 is the smallest prime after 2 * 152476
    static String[] hashtable;

    /**
     * Runs the WordHunt
     * @param args arguments from the command line (none used here)
     */
    public static void main(String args[])
    {
        hashtable = new String[TABLE_SIZE];

        // read in the letter location data
        readPuzzle();

        // obtain user input on word length and search accordingly
        Scanner kbInput = new Scanner(System.in);
        System.out.print("Input the length of the words you want to search for: ");
        int length = kbInput.nextInt();
        createHashTable();
        search(length);
    }

    /**
     * Reads in the letter location data for the word hunt puzzle
     */
    public static void readPuzzle()
    {
        letters = new String[3][3][2];

        try
        {
            Scanner reader = new Scanner(new File("graph.txt"));

            while(reader.hasNext())
            {
                String temp = reader.next();
                // if the String is not a number it is a character
                if (!NUMBERS.contains(temp))
                {
                    // find the coordinates of the letter/String
                    int x = reader.nextInt();
                    int y = reader.nextInt();
                    int z = reader.nextInt();
                    letters[x][y][z] = temp;
                }
            }

            // prints out the word hunt set up, in separate squares that represent the 2D arrays at each depth
            for (int k = 0; k < 2; k++)
            {
                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        System.out.print(letters[i][j][k] + " ");
                    }
                    System.out.print("\n");
                }
                System.out.print("\n");
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No such file.");
        }
    }

    /**
     * Searches and prints out all valid words of given length
     * @param len length of the words to find
     */
    public static void search(int len)
    {
        // initiate the search from every possible vertex
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 2; k++)
                {
                    findWord("", i, j, k, 0, len);
                }
            }
        }
    }

    /**
     * Method that actually finds the words and determines their validity
     * @param part String representing word fragment
     * @param x the x value of the location
     * @param y the y value of the location
     * @param z the z value of the location
     * @param currentLength current length of word fragment
     * @param maxLength the length of word that we want to find (from user input)
     */
    public static void findWord(String part, int x, int y, int z, int currentLength, int maxLength)
    {
        // add to part the letter at this location and increment the word's current length
        part += letters[x][y][z];
        currentLength += 1;

        // if we are at the desired length, check validity and print the word if it is valid
        if (currentLength == maxLength)
        {
            if (isValid(part))
            {
                System.out.println(part);
            }
            // don't do anything if the word isn't valid
        }
        else
        {
            // check if neighbors are within bounds and continue to search
            if (x - 1 >= 0)
            {
                findWord(part, x - 1, y, z, currentLength, maxLength);
            }
            if (x + 1 < 3)
            {
                findWord(part, x + 1, y, z, currentLength, maxLength);
            }
            if (y - 1 >= 0)
            {
                findWord(part, x, y - 1, z, currentLength, maxLength);
            }
            if (y + 1 < 3)
            {
                findWord(part, x, y + 1, z, currentLength, maxLength);
            }
            if (z - 1 >= 0)
            {
                findWord(part, x, y, z - 1, currentLength, maxLength);
            }
            if (z + 1 < 2)
            {
                findWord(part, x, y, z + 1, currentLength, maxLength);
            }
        }
    }

    /**
     * Creates hashtable to store all the words in the dictionary
     */
    public static void createHashTable()
    {
        try
        {
            Scanner fileReader = new Scanner(new File("largedictionary.txt"));

            // read in every word from the dictionary file
            while (fileReader.hasNext())
            {
                String theWord = fileReader.next();
                // create hash index for the word
                int index = toHash(theWord);

                // linear probing if the hashtable cell is occupied
                while (hashtable[index] != null)
                {
                    index += 1;

                    // if we somehow hit the end of the hashtable, go back to the beginning
                    if (index == TABLE_SIZE)
                    {
                        index = 0;
                    }
                }

                hashtable[index] = theWord;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
        }
    }

    /**
     * Creates hash value for a given String; algorithm borrowed from Weiss textbook
     * @param s word to be hashed
     * @return int hash value
     */
    public static int toHash(String s)
    {
        int value = 0;

        for (int i = 0; i < s.length(); i++)
        {
            value = 37 * value + s.charAt(i);
        }

        value = value % TABLE_SIZE;
        if (value < 0)
        {
            value += TABLE_SIZE;
        }

        return value;
    }

    /**
     * Checks if the word is valid
     * @param word String to be tested
     * @return true if String is valid, false otherwise
     */
    public static boolean isValid(String word)
    {
        int testIndex = toHash(word);

        // if nothing is at that hash value, the word isn't value
        if (hashtable[testIndex] == null)
        {
            return false;
        }

        // test word matches word in hash value, then the test word is valid
        if (hashtable[testIndex].equals(word))
        {
            return true;
        }
        else
        {
            // linearly probe to see if a match can be found
            int secondTestIndex = testIndex + 1;

            // if we somehow get to the end of the table, go to index zero
            if (secondTestIndex == TABLE_SIZE)
            {
                secondTestIndex = 0;
            }

            // probe until we go a full cycle back to where we started
            while (secondTestIndex != testIndex)
            {
                if (hashtable[secondTestIndex] != null)
                {
                    if (hashtable[secondTestIndex].equals(word))
                    {
                        return true;
                    }
                }
                secondTestIndex += 1;
                // if we somehow get to the end of the table, go to index zero
                if (secondTestIndex == TABLE_SIZE)
                {
                    secondTestIndex = 0;
                }
            }
        }

        // false if we check the entire hashtable and word is not there
        return false;
    }
}
