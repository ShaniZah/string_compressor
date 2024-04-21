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

	public StringEncoderDecoder() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Compress(String[] input_names, String[] output_names) {
		char specialChar = 222;
		HashMap<String, Integer> hash = new HashMap<>(); // string = words, int = appearances
		String[] words = input_names[0].split("[\\s,.]+"); // regular expression
		for (int i = 0; i < words.length; i++) {
			if (words[i].length() >= 3) {
				// if the word is not there, add it
				if (!hash.containsKey(words[i])) {
					hash.put(words[i], 1);
				} else {
					// the word exists, increment appearance
					hash.put(words[i], hash.get(words[i]) + 1);
				}
			}
		}
		// now we sort the map to have the words with the most appearances.
		// convert map to list
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(hash.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
		HashMap<String, Integer> sortedmap = new HashMap<>(); // string = words, int = appearances

		// insert to map with encoding
		for (int i = 0; i < list.size(); i++) {
			if (i < 200) {
				sortedmap.put(list.get(i).getKey(), i+1);
			}
			else {
				sortedmap.put(list.get(i).getKey(), list.get(i).getValue());
			}
		}
		

	}

	@Override
	public void Decompress(String[] input_names, String[] output_names) {
		// TODO Auto-generated method stub

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
