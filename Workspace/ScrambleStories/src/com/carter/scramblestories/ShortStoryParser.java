package com.carter.scramblestories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShortStoryParser {
	
	public static String swapWords(String story,String prev[],String rep[]) {

		InputStream stream = WordCheck.class.getResourceAsStream(story);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;
		String retStory = "";

		try {

			while ((line = br.readLine()) != null) {

				for(int i=0;i<prev.length;i++){
					retStory.concat(line.replace(prev[i], rep[i]));
				}
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retStory;

	}

}
