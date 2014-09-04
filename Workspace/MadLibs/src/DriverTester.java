
public class DriverTester {

	static int ASCII_OFFSET = 65;
	
	public static void main(String[] args) {
		StoryParser story = new StoryParser("The_Mean_Pirate");
		story.chooseWords(3);
		String all_word[] = story.getWords();
		String all_type[] = story.getWordTypes();
		String testWords[] = {"TACOBELL","BURGERKING","PIZZAHUT"};
		
		for(int i =0;i<all_word.length;i++){
			System.out.print(all_word[i]+"-");
			System.out.println(all_type[i]);
		}
		System.out.println(all_type.length);
		
		System.out.println(ShortStoryParser.swapWords("The_Mean_Pirate", all_word, testWords));
	}

}
