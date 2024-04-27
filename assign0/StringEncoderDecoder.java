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

	HashMap<String, Integer> words_from_input;
	HashMap<String, Integer> common_words;

	public StringEncoderDecoder() {
		words_from_input = new HashMap<>(); // string = words, int = appearances
		common_words = new HashMap<>(); // string = words, int = appearances
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) {
		String[] words = input_names[0].split("[\\s,.!]+"); // regular expression
		StringBuilder output = new StringBuilder();
		char specialChar_1 = 222;
		char specialChar_2 = 223;
		
		countWordFrequency(words);
		createIndexMapping();

		for (String word : words) {
			if (common_words.containsKey(word)) {
				output.append((char) specialChar_1);
				output.append(common_words.get(word));
			} else {
				output.append((char) specialChar_2);
				output.append(word);
			}
		}
		
		
		output_names[0] = output.toString();
		System.out.println(output.toString());
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
			}
			else {
				break;
			}
		}
	}

	private void countWordFrequency(String[] words) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].length() >= 3) {
				// if the word is not there, add it
				if (!words_from_input.containsKey(words[i])) {
					words_from_input.put(words[i], 1);
				} else {
					// the word exists, increment appearance
					words_from_input.put(words[i], words_from_input.get(words[i]) + 1);
				}
			}
		}
	}

	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		String[] codedWords = output_names[0].split("[Þß]+"); // regular expression

		StringBuilder output = new StringBuilder();

		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(common_words.entrySet());
		list.sort(Map.Entry.comparingByValue());

		for (int i = 1; i < codedWords.length; i++) {
			String word = codedWords[i];
			try { // try parsing the coded word to int, if failed -> not in most common 200
				int value = Integer.parseInt(word);
				output.append(list.get(value-1).getKey());
			} catch (NumberFormatException e) {
				output.append(word);
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
