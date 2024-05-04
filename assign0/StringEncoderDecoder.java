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

	private HashMap<String, Integer> wordsCountMap;
	private HashMap<String, Integer> wordsToCodeMap; // most common 200 words with code
	private ArrayList<String> allWords;

	public StringEncoderDecoder() {
		wordsCountMap = new HashMap<>();
		wordsToCodeMap = new HashMap<>();
		allWords = new ArrayList<>();
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) {
		// split all words frm their punctuation chars and count their occurances
		String[] words = input_names[0].split(" "); // initial split
		countWordFrequency(words);
		fillWordsCountMap();
		// count and append coded index to the 200 most common words
		buildWordsToCodeMap();

		output_names[0] = buildCompressedString().toString();
	}

	// seperate words from punctuation chars
	private void countWordFrequency(String[] words) {
		for (int i = 0; i < words.length; i++) {
			String currentWord = words[i];
			int endIndex = 0;
			// add spaces that were lost during the initial split
			if (currentWord.equals(""))
				allWords.add(" ");
			// check if word ends with char that is not alphabet
			else if (currentWord.length() != 0 && !Character.isLetter(currentWord.charAt(currentWord.length() - 1))) {
				for (int j = 0; j < currentWord.length(); j++) {
					// go through word and find index of first non abc char
					if (!Character.isLetter(currentWord.charAt(j))) {
						if (j == 0) {
							endIndex = currentWord.length(); // the whole word is non abc
							break;
						} else {
							endIndex = j; // first index of the non abc part of the word
							break;
						}
					}
				}
				allWords.add(currentWord.substring(0, endIndex)); // the word itself
				allWords.add(currentWord.substring(endIndex)); // the tailling non abc chars
			} else {
				allWords.add(currentWord);
			}
		}
	}

	// fill map of all the words(key) and count them(value)
	private void fillWordsCountMap() {
		// put in function
		for (int i = 0; i < allWords.size(); i++) {
			// the word is at least 3 chars that are abc
			if (allWords.get(i).length() >= 3 && Character.isLetter(allWords.get(i).charAt(0))) {
				// if the word is not there, add it
				if (!wordsCountMap.containsKey(allWords.get(i))) {
					wordsCountMap.put(allWords.get(i), 1);
				} else {
					// the word exists in the map, increment occurence count
					wordsCountMap.put(allWords.get(i), wordsCountMap.get(allWords.get(i)) + 1);
				}
			}
		}
	}

	// give the most common 200 words their coded value
	private void buildWordsToCodeMap() {
		// sort the map to have the words with the most occurences be at the top
		// convert map to list for easy access and sorting
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(wordsCountMap.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

		// insert to map with encoding
		for (int i = 0; i < list.size() && i < 200; i++) {
			wordsToCodeMap.put(list.get(i).getKey(), i + 1);
		}
	}

	// construct the compressed output
	private StringBuilder buildCompressedString() {
		StringBuilder output = new StringBuilder();
		char specialChar_1 = 222; // Þ
		char specialChar_2 = 223; // ß
		for (int i = 0; i < allWords.size(); i++) {
			String word = allWords.get(i);
			if (wordsToCodeMap.containsKey(word)) {
				output.append((char) specialChar_1);
				output.append(wordsToCodeMap.get(word));
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
		ArrayList<Map.Entry<String, Integer>> common_words_list = new ArrayList<>(wordsToCodeMap.entrySet());
		common_words_list.sort(Map.Entry.comparingByValue());

		String[] codedWords = output_names[0].split("[Þß]+");
		StringBuilder output = new StringBuilder();
		boolean nextIsLetter = true; // flag

		for (int i = 1; i < codedWords.length; i++) {
			String word = codedWords[i];
			if (i + 1 < codedWords.length)
				nextIsLetter = nextCharIsLetter(codedWords[i + 1], common_words_list);
			try { // try parsing the string to int, if failed -> not coded word
				int value = Integer.parseInt(word);
				output.append(common_words_list.get(value - 1).getKey());
				if (nextIsLetter)
					output.append(" ");

			} catch (NumberFormatException e) {
				output.append(word); // the word is not coded and can be appended as is
				if (nextIsLetter)
					output.append(" ");
			}
		}
		output_names[0] = output.toString();
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
