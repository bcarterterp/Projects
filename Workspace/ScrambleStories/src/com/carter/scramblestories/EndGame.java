package com.carter.scramblestories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndGame extends Activity{
	
	String story,type[];
	String mode;
	TextView story_view,results;
	Intent game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_game_activity);
		
		story_view = (TextView) findViewById(R.id.story_text);
		results = (TextView) findViewById(R.id.results);
		game = getIntent();
		story = game.getExtras().getString("story");
		story_view.setText(story);
		mode = game.getExtras().getString("mode");
		
		if(mode.equalsIgnoreCase("mdevice")){
			mDeviceSetup();
		}else if(mode.equals("single")){
			sDeviceSetup();
		}
		
	}
	
	private void mDeviceSetup(){
		
		String players[] = game.getExtras().getStringArray("players");
		type = game.getExtras().getStringArray("types");
		String winner_name = game.getExtras().getString("winner");
		int winner_score = game.getExtras().getInt("winner_score");
		String words[][] = new String[players.length][];
		int word_scores[][] = new int[players.length][];
		
		for(int i=0;i<players.length;i++){
			words[i] = game.getExtras().getStringArray(players[i]);
			word_scores[i] = game.getExtras().getIntArray(players[i]+"scores");
		}
		
		StringBuilder builder = new StringBuilder();

		for(int i=0;i<type.length;i++){
			builder.append(type[i]+"\n"+"\n");
			for(int j=0;j<players.length;j++){
				
				builder.append("   "+players[j]+": "+words[j][i]+" - "+Integer.toString(word_scores[j][i])+" Points\n");
				
			}
			builder.append("\n");
		}
		builder.append("Winner: "+winner_name+" - "+winner_score+" Points");
		results.setText(builder.toString());
		
		
	}
	
	private void sDeviceSetup(){
		
		int score = game.getExtras().getInt("score");
		String words[] = game.getExtras().getStringArray("words");
		String type[] = game.getExtras().getStringArray("type");
		StringBuilder builder = new StringBuilder();
		builder.append("You Scored "+score+" Points!\nWords:\n");
		
		for(int i = 0;i<type.length;i++){
			builder.append("("+type[i]+") "+words[i]+"\n");
		}
		
		results.setText(builder.toString());
			
	}
	
	public void replay(View view){
		if(mode.equals("mdevice")){
			startActivity(new Intent(this, PassAndPlay.class));
		}else if(mode.equals("single")){
			startActivity(new Intent(this, SinglePlayer.class));
		}
	}
	
	public void home(View view){
		startActivity(new Intent(this, GameChooser.class));
	}
	
	@Override
	public void onBackPressed() {
	}

}
