

import brandon.carter.scrablibs.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StorySelector extends Activity{
	
	Intent startGame;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.story_selector_activity);
		
		Intent game_type = getIntent();
		
		if(game_type.getExtras().getString("type").equals("single")){
			startGame = new Intent(this,SinglePlayer.class);
		}else if(game_type.getExtras().getString("type").equals("pass")){
			startGame = new Intent(this,PassAndPlay.class);
			startGame.putExtra("players", game_type.getExtras().getInt("players"));
		}
		
		
	}
	
	public void playTester(View view){
		
		setUpStory("tester", 5);
		
	}
	
	public void playMeanPirate(View view){
		
		setUpStory("The_Mean_Pirate", 10);
		
	} 

	public void playRidingHoodOne(View view){
	
		setUpStory("Red_Riding_Hood_Part1", 15);
		
	}

	public void playRidingHoodTwo(View view){
	
		setUpStory("Red_Riding_Hood_Part2", 15);
	
	}
	
	public void setUpStory(final String story, final int num_words){
		
		final ProgressDialog working = ProgressDialog.show(StorySelector.this,"Please Wait", "Setting up game!", true);
		working.setCancelable(true);
		new Thread(new Runnable(){

			@Override
			public void run() {
				
				StoryParser create_story = new StoryParser(story);
				create_story.chooseWords(num_words);
				
				String lines[][] = create_story.getLines();
				for(int i = 0;i<lines.length;i++){
					startGame.putExtra("line"+i, lines[i]);
				}
				startGame.putExtra("num_lines", lines.length);
				startGame.putExtra("match_words", create_story.getWordTypes());
				startGame.putExtra("old_words", create_story.getWords());
				startActivity(startGame);
				
			}
			
		}).start();
			
		
		
		
	}

}
