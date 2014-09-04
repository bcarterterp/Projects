

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShortStoryParser {
	
	public static String swapWords(String story,String prev[],String rep[]) {

		story = "/resource/" + story + ".txt";
		InputStream stream = WordCheck.class.getResourceAsStream(story);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;
		String retStory = "";

		try {

			while ((line = br.readLine()) != null) {

				String newLine = "";
				for(int i=0;i<prev.length;i++){
					newLine = line.replace(prev[i], rep[i]);
				}
				retStory = retStory.concat(newLine);
				retStory = retStory +"\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retStory;

	}

}
