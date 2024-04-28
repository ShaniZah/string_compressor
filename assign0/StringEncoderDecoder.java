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

	private HashMap<String, Integer> words_from_input;
	private HashMap<String, Integer> common_words;
	private ArrayList<String> allWords;

	public StringEncoderDecoder() {
		words_from_input = new HashMap<>(); // string = words, int = appearances
		common_words = new HashMap<>(); // string = words, int = appearances
		allWords = new ArrayList<>();
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) {
		String[] words = input_names[0].split(" "); // initial split		

		countWordFrequency(words);
		createIndexMapping();
		output_names[0] = buildCompressedString().toString();
		
		System.out.println(output_names[0].toString());
	}
	
	// construct the compressed output
	private StringBuilder buildCompressedString() {
		StringBuilder output = new StringBuilder();
		char specialChar_1 = 222;
		char specialChar_2 = 223;
		for (int i = 0; i < allWords.size(); i++) {
			String word = allWords.get(i);
			if (common_words.containsKey(word)) {
				output.append((char) specialChar_1);
				output.append(common_words.get(word));
			} else {
				output.append((char) specialChar_2);
				output.append(word);
			}
		}
		return output;
	}

	private void createIndexMapping() {
		// sort the map to have the words with the most appearances.
		// convert map to list
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(words_from_input.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

		// insert to map with encoding
		for (int i = 0; i < list.size(); i++) {
			if (i < 200) {
				common_words.put(list.get(i).getKey(), i + 1);
			} else {
				break;
			}
		}
	}

	// count how many times each word appeares in the string
	private void countWordFrequency(String[] words) {
		String subWord;
		for (int i = 0; i < words.length; i++) {
			subWord = words[i];
			int endIndex = 0;
			// check if word ends with char that is not alphabet
			if (subWord.length()!=0 && !Character.isLetter(subWord.charAt(subWord.length() - 1))) {
				for (int j = 0; i < subWord.length(); j++) {
					if (!Character.isLetter(subWord.charAt(j))) {
						endIndex = j; //the index+1 of the non abc part of the word
						break;
					}
				}
				allWords.add(subWord.substring(0, endIndex)); // the word itself
				allWords.add(subWord.substring(endIndex)); // the tailling non abc chars
			} else {
				allWords.add(subWord);
			}
		}
		// fill map of all the words and count them 
		for (int i = 0; i < allWords.size(); i++) {
			if (allWords.get(i).length() >= 3) {
				// if the word is not there, add it
				if (!words_from_input.containsKey(allWords.get(i))) {
					words_from_input.put(allWords.get(i), 1);
				} else {
					// the word exists, increment occurence
					words_from_input.put(allWords.get(i), words_from_input.get(allWords.get(i)) + 1);
				}
			}
		}
	}

	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		String[] codedWords = output_names[0].split("[Þß]+"); // split string by the special chars
		StringBuilder output = new StringBuilder();
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(common_words.entrySet());
		//list.sort(Map.Entry.comparingByValue());

		for (int i = 1; i < codedWords.length; i++) {
			String word = codedWords[i];
			try { // try parsing the coded word to int, if failed -> not in most common 200
				int value = Integer.parseInt(word);
				output.append(list.get(value - 1).getKey());
			} catch (NumberFormatException e) {
				output.append(word); // the word is not coded and can be appended as is
			}
			output.append(" ");
		}
		System.out.println(output.toString());
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
