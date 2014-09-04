

import brandon.carter.scrablibs.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NumberOfPlayers extends Activity{
	
	@Override
	public void onCreate(Bundle saved){
		
		super.onCreate(saved);
		setContentView(R.layout.number_players);
		
	}
	
	public void selectTwo(View view){
		
		Intent startGame = new Intent(this,StorySelector.class);
		startGame.putExtra("players", 2);
		startGame.putExtra("type", "pass");
		startActivity(startGame);
		
	}
	
	public void selectThree(View view){
		
		Intent startGame = new Intent(this,StorySelector.class);
		startGame.putExtra("players", 3);
		startGame.putExtra("type", "pass");
		startActivity(startGame);
		
	}
	
	public void selectFour(View view){
	
		Intent startGame = new Intent(this,StorySelector.class);
		startGame.putExtra("players", 4);
		startGame.putExtra("type", "pass");
		startActivity(startGame);
	
	}

}
