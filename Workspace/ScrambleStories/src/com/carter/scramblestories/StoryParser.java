package com.carter.scramblestories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class StoryParser {

	private String file_name;
	private String lines[][];
	private int line_count;
	private int possible_words;
	private WordCheck checker;
	private String all_words[];
	private String all_words_type[];
	private String game_words[];
	private String game_words_type[];
	private int total_words;

	public StoryParser(String story) {

		file_name = "/resource/" + story + ".txt";
		checker = new WordCheck();
		checkStory(file_name);
		parseStory(file_name);

		if (all_words.length > total_words) {
			trimArray();
		}

	}
	
	public StoryParser(String lines[][]){
		
		this.lines = lines;
		
	}

	private void checkStory(String story) {

		line_count = 0;
		possible_words = 0;
		total_words = 0;

		InputStream stream = WordCheck.class.getResourceAsStream(story);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;

		try {

			while ((line = br.readLine()) != null) {

				possible_words += possibleSwaps(line.split(" "));
				line_count++;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		lines = new String[line_count][];
		all_words = new String[possible_words];
		all_words_type = new String[possible_words];

	}

	private void parseStory(String story) {

		InputStream stream = WordCheck.class.getResourceAsStream(story);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;
		int line_index = 0;

		try {

			while ((line = br.readLine()) != null) {

				lines[line_index] = line.split(" ");
				getAllWords(lines[line_index]);
				line_index++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String skimWord(String word) {

		char actual_word[] = new char[word.length()];

		int letter_index = 0;

		for (int i = 0; i < word.length(); i++) {

			if (!(word.charAt(i) == '"' || word.charAt(i) == '.'
					|| word.charAt(i) == ',' || word.charAt(i) == '!'
					|| word.charAt(i) == '?' || word.charAt(i) == '\0')) {

				actual_word[letter_index] = word.charAt(i);
				letter_index++;

			}

		}

		return new String(actual_word).trim().toLowerCase();

	}

	private void getAllWords(String story[]) {

		for (int i = 0; i < story.length; i++) {

			if (story[i].indexOf('\'') < 0 && !story[i].equals("")) {

				String skimmed_word = skimWord(story[i]);

				int matches = 0;
				String flag = "";

				if (checker.checkWord("nouns", skimmed_word)) {
					flag = "nouns";
					matches++;
				}
				if (checker.checkWord("verbs", skimmed_word)) {
					flag = "verbs";
					matches++;
				}
				if (checker.checkWord("adj", skimmed_word)) {
					flag = "adj";
					matches++;
				}
				if (checker.checkWord("adv", skimmed_word)) {
					flag = "adv";
					matches++;
				}

				if (matches == 1 && !duplicate(skimmed_word, all_words)) {
					all_words[total_words] = skimmed_word;
					all_words_type[total_words] = flag;
					total_words++;
				}

			}

		}

	}

	private int possibleSwaps(String story[]) {

		int possible = 0;

		for (int i = 0; i < story.length; i++) {

			if (story[i].indexOf('\'') < 0 && !story[i].equals("")) {

				String skimmed_word = skimWord(story[i]);

				int matches = 0;

				if (checker.checkWord("nouns", skimmed_word)) {
					matches++;
				}
				if (checker.checkWord("verbs", skimmed_word)) {
					matches++;
				}
				if (checker.checkWord("adj", skimmed_word)) {
					matches++;
				}
				if (checker.checkWord("adv", skimmed_word)) {
					matches++;
				}

				if (matches == 1) {
					possible++;
				}

			}

		}

		return possible;

	}

	private boolean duplicate(String word, String list[]) {

		for (int i = 0; i < list.length; i++) {

			if (list[i] != null && list[i].equals(word)) {

				return true;

			}

		}

		return false;

	}

	public void allWordsPrintout() {

		for (int i = 0; i < all_words.length; i++) {

			System.out.println(all_words[i] + "-" + all_words_type[i]);

		}

	}

	private String replaceWord(String orig, String swap) {

		int new_word_index = 0;
		char new_word[] = new char[swap.length() + 4];

		if (orig.startsWith("\"")) {

			new_word[0] = '\"';
			new_word_index++;

		}

		if (Character.isUpperCase(orig.charAt(new_word_index))) {

			Character.toUpperCase(swap.charAt(0));

		}

		for (int i = 0; i < swap.length(); i++) {

			new_word[new_word_index] = swap.charAt(i);
			new_word_index++;

		}

		if (orig.indexOf('.') > 0) {

			new_word[new_word_index] = '.';
			new_word_index++;

		}
		if (orig.indexOf('?') > 0) {

			new_word[new_word_index] = '?';
			new_word_index++;

		}
		if (orig.indexOf('!') > 0) {

			new_word[new_word_index] = '!';
			new_word_index++;

		}
		if (orig.indexOf(',') > 0) {

			new_word[new_word_index] = ',';
			new_word_index++;

		}

		if (orig.endsWith("\"")) {

			new_word[new_word_index] = '"';
			new_word_index++;

		}

		return new String(new_word).trim();

	}

	public void swapIntoStory(String replace[], String list[]) {

		for (int i = 0; i < lines.length; i++) {

			for (int j = 0; j < lines[i].length; j++) {

				for (int k = 0; k < list.length; k++) {

					if (skimWord(lines[i][j]).equals(list[k])) {

						lines[i][j] = replaceWord(lines[i][j], replace[k]);

					}

				}

			}

		}

	}

	public void printStory() {

		for (int i = 0; i < lines.length; i++) {

			for (int j = 0; j < lines[i].length; j++) {

				System.out.print(lines[i][j] + " ");

			}

			System.out.println();
		}

	}
	
	public String getStoryString(){
		StringBuilder newString = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {

			for (int j = 0; j < lines[i].length; j++) {

				newString.append(lines[i][j] + " ");

			}

			newString.append("\n");
		}
		
		return newString.toString();
		
	}

	public String[] getWords() {
		return game_words;
	}

	public String[] getWordTypes() {
		return game_words_type;
	}

	public void trimArray() {
		String newArray[] = new String[total_words];
		for (int i = 0; i < total_words; i++) {
			newArray[i] = all_words[i];
		}

		all_words = newArray;
	}
	
	public String[][] getLines(){
		return lines;
	}

	public void chooseWords(int max) {

		game_words = new String[max];
		game_words_type = new String[max];

		if(max>total_words){
			max = total_words;
		}
		
		game_words = new String[max];
		game_words_type = new String[max];

		int chosen = 1;
		Random random = new Random();
		int index = random.nextInt(total_words);
		String word = all_words[index];
		game_words[0] = word;
		game_words_type[0] = all_words_type[index];
		
		for(int i = chosen;i<max;i++){
			
			while(duplicate(word, game_words)){
				index = random.nextInt(total_words);
				word = all_words[index];
					
			}
						
			game_words[chosen] = word;
			game_words_type[chosen] = all_words_type[index];
			chosen++;	
		
		}

	}
}
