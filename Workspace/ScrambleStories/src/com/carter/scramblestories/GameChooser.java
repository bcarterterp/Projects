package com.carter.scramblestories;

import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GameChooser extends Activity{
	
	String tag = "Lifecycle";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_chooser);
		Log.d("HELP", "Testing");

		
	}
	
	public void singlePlayer(View view){	
		startActivity(new Intent(this,StorySelector.class).putExtra("type", "single"));	
	}
	
	public void mDevice(View view){
		startActivity(new Intent(this, NumberOfPlayers.class));
	}

	
	
	public void onSignInFailed() {
		// TODO Auto-generated method stub
	}

	
	
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
	}

}
