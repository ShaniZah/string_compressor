package assign0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Assignment 0
 * Submitted by: 
 * Shani Zahavi 	ID# 211305271
 * Leon Petrov 	ID# 314821521
 */

import base.Compressor;

public class StringEncoderDecoder implements Compressor {

	private HashMap<String, Integer> words_count_map;
	private HashMap<String, Integer> most_common_words;
	private ArrayList<String> allWords;

	public StringEncoderDecoder() {
		words_count_map = new HashMap<>(); // string = words, int = appearances
		most_common_words = new HashMap<>(); // string = words, int = coded value
		allWords = new ArrayList<>();
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) {

		// split all words frm their punctuation chars and count their occurances
		String[] words = input_names[0].split(" "); // initial split
		countWordFrequency(words);
		// count and append coded index to the 200 most common words
		createIndexMapping();

		output_names[0] = buildCompressedString().toString();
		System.out.println(output_names[0]);
	}

	// seperate words from punctuation chars
	// count how many times each word appeares in the string
	private void countWordFrequency(String[] words) {
		for (int i = 0; i < words.length; i++) {
			String subWord = words[i];
			int endIndex = 0;
			// add spaces that were lost during the initial split
			if (subWord.equals(""))
				allWords.add(" ");
			// check if word ends with char that is not alphabet
			else if (subWord.length() != 0 && !Character.isLetter(subWord.charAt(subWord.length() - 1))) {
				for (int j = 0; j < subWord.length(); j++) {
					// go through word and find index of first non abc char
					if (!Character.isLetter(subWord.charAt(j))) {
						if (j == 0) {
							endIndex = subWord.length(); // the whole word is non abc
							break;
						} else {
							endIndex = j; // first index of the non abc part of the word
							break;
						}
					}
				}
				allWords.add(subWord.substring(0, endIndex)); // the word itself
				allWords.add(subWord.substring(endIndex)); // the tailling non abc chars
			} else {
				allWords.add(subWord);
			}
		}
		// fill map of all the words(key) and count them(value)
		for (int i = 0; i < allWords.size(); i++) {
			// the word is at least 3 chars that are abc
			if (allWords.get(i).length() >= 3 && Character.isLetter(allWords.get(i).charAt(0))) {
				// if the word is not there, add it
				if (!words_count_map.containsKey(allWords.get(i))) {
					words_count_map.put(allWords.get(i), 1);
				} else {
					// the word exists in the map, increment occurence count
					words_count_map.put(allWords.get(i), words_count_map.get(allWords.get(i)) + 1);
				}
			}
		}
	}

	// give the most common 200 words their coded word
	private void createIndexMapping() {
		// sort the map to have the words with the most appearances be at the top of
		// map.
		// convert map to list
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(words_count_map.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

		// insert to map with encoding
		for (int i = 0; i < list.size() && i < 200; i++) {
			most_common_words.put(list.get(i).getKey(), i + 1);
		}
	}

	// construct the compressed output
	private StringBuilder buildCompressedString() {
		StringBuilder output = new StringBuilder();
		char specialChar_1 = 222; // Þ
		char specialChar_2 = 223; // ß
		for (int i = 0; i < allWords.size(); i++) {
			String word = allWords.get(i);
			if (most_common_words.containsKey(word)) {
				output.append((char) specialChar_1);
				output.append(most_common_words.get(word));
			} else {
				output.append((char) specialChar_2);
				output.append(word);
			}
		}
		return output;
	}

	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		// convert map to list for easy access of a key by value
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(most_common_words.entrySet());
		String[] codedWords = output_names[0].split("[Þß]+");
		StringBuilder output = new StringBuilder();
		boolean nextIsLetter = true; // flag
		
		for (int i = 1; i < codedWords.length; i++) {
			String word = codedWords[i];
			if (i + 1 < codedWords.length)
				nextIsLetter = nextCharIsLetter(codedWords[i + 1], list);
			try { // try parsing the string to int, if failed -> not coded word
				int value = Integer.parseInt(word);
				output.append(list.get(value - 1).getKey());
				if (nextIsLetter)
					output.append(" ");

			} catch (NumberFormatException e) {
				output.append(word); // the word is not coded and can be appended as is
				if (nextIsLetter)
					output.append(" ");
			}
		}
		System.out.println(output_names[0]);
	}

	// check if the next char is a letter from the abc
	private boolean nextCharIsLetter(String str, ArrayList<Map.Entry<String, Integer>> list) {
		try { // try parsing the word to int, if failed -> not a coded word
			int value = Integer.parseInt(str);
			str = list.get(value - 1).getKey();
			if (!Character.isLetter(str.charAt(0)))
				return false;

		} catch (NumberFormatException e) {
			if (!Character.isLetter(str.charAt(0)))
				return false;
		}
		return true; // the next char is abc letter
	}

	@Override
	public byte[] CompressWithArray(String[] input_names, String[] output_names) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] DecompressWithArray(String[] input_names, String[] output_names) {
		// TODO Auto-generated method stub
		return null;
	}

}
