AUTHOR: Kylie Wu
CORE CONCEPTS: 3D arrays, hashtables, depth-first-search

This project searches for all possible words of a given length within a 3D word search that is denoted in graph.txt. This 3D word search layout is displayed at the beginning of the program run by printing a series of 2D arrays that represent the layer of letters at each depth of the layout. I used a 3D array of Strings to implement the layout of the letters in WordHunt. Word validity is checked against using largedictionary.txt.

To run the program, just run WordHunt.java. A hashtable for quick word lookup is created at this time using the largedictionary.txt file.

The program then asks the user for the length of the words to find. Given an int input, it will then print out all possible x-letter words it can find from the graph.

CLASS/METHODS:

WordHunt - Contains the main method and all other methods needed for this program to work.

findWord() - Works recursively to return all possible word (both valid and invalid). It works by taking the "part" of the word that has already been found and searching through all valid neighboring coordinates to find letters to add onto the part. If the part is at the desired word length, the word is checked for validity and printed if it is valid.

search() - Runs findWord() at each vertex of the layout. This ensures that every possible starting point is used in finding all possible valid words of the given length.

isValid() - To check validity, a hashcode value is made for the test word, using the same hash function that was used to make the dictionary lookup. The String at that hash value and in following indexes in the lookup table are compared to the test word to see if the test word exists within the dictionary.

THINGS TO NOTE:
The run time of finding words 7 letters and longer can be quite long. This is due to the exponential growth of possible word constructions, do to the increasing number of neighbor choices with each increase in word length. Finding words of lengths 1-6 will finish it a couple of minutes (less than 10).