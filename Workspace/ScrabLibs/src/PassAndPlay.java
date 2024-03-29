

import java.util.ArrayList;

import brandon.carter.scrablibs.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PassAndPlay extends Activity{
	
	Player players[];
	LetterBag bag = new LetterBag();
	LinearLayout layout;
	TextView textview,word_type,score_view;
	Button tiles[], repick;
	String user_word;
	WordCheck checker = new WordCheck();
	int curr_type, word_score,wild_one_index[],
	wild_two_index[],repicked_times,people,curr_player,player_word_score[][];
	String type[],words_to_swap[],old_words[],player_word[][];
	StoryParser story;
	boolean wild_one[],wild_two[];
	String names[];
	Intent done;
	
	String update_output = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		layout = (LinearLayout) findViewById(R.id.linlayout);
		textview = (TextView) findViewById(R.id.textView1);
		word_type = (TextView) findViewById(R.id.wordType);
		score_view = (TextView) findViewById(R.id.score);
		repick = (Button) findViewById(R.id.rePick);
		done = new Intent(this, EndGame.class);
		done.putExtra("mode", "mdevice");
		tiles = new Button[7];
		repick.setOnClickListener(repick());
		
		if(savedInstanceState != null){
			
			int tile_type[] = savedInstanceState.getIntArray("bag_tile_type");
			int tile_points[] = savedInstanceState.getIntArray("bag_tile_points");
			int num_tiles = savedInstanceState.getInt("bag_number_tiles");
			bag = new LetterBag(tile_type,tile_points,num_tiles);
			
			int num_lines = savedInstanceState.getInt("num_lines");
			String lines[][] = new String[num_lines][];
			for(int i = 0;i < num_lines;i++){
				lines[i] = savedInstanceState.getStringArray("line"+i);
				
			}
			story = new StoryParser(lines);
			
			curr_type = savedInstanceState.getInt("curr_type");
			curr_player = savedInstanceState.getInt("curr_player");
			repicked_times = savedInstanceState.getInt("repicks");
			type = savedInstanceState.getStringArray("match_words");
			words_to_swap = savedInstanceState.getStringArray("words_to_swap");
			old_words = savedInstanceState.getStringArray("old_words");
			wild_one = savedInstanceState.getBooleanArray("wild_one");
			wild_two = savedInstanceState.getBooleanArray("wild_two");
			wild_one_index = savedInstanceState.getIntArray("wild_one_index");
			wild_two_index = savedInstanceState.getIntArray("wild_two_index");
			
			names = savedInstanceState.getStringArray("names");
			people = savedInstanceState.getInt("people");
			players = new Player[people];
			player_word = new String[people][];
			player_word_score = new int[people][];
			String line = "";
			for(int i = 0;i<people;i++){
				
				
				player_word[i] = savedInstanceState.getStringArray("player"+i+"word");
				player_word_score[i] = savedInstanceState.getIntArray("player"+i+"score");
				if(player_word[i] == null || player_word_score == null){
					update_output = "FAILED";
				}
				for(int j = 0;j<player_word[i].length;j++){
					line+="Player: "+names[i]+" "+player_word[i][j]+"("+player_word_score[i][j]+") type: "+type[j]+"\n";
				}
				
				players[i] = new Player(names[i],0,savedInstanceState.getCharArray("player"+i+"hand"),bag);
				
			}
			line+="total types: "+type.length;
			textview.setText(line);
			
		}else{
			
			Intent settings = getIntent();
			
			int num_lines = settings.getExtras().getInt("num_lines");
			String lines[][] = new String[num_lines][];
			for(int i = 0;i < num_lines;i++){
				lines[i] = settings.getExtras().getStringArray("line"+i);
				
			}
			
			story = new StoryParser(lines);
			type = settings.getExtras().getStringArray("match_words");
			old_words = settings.getExtras().getStringArray("old_words");
			words_to_swap = new String[type.length];
			people = settings.getExtras().getInt("players");
			players = new Player[people];
			wild_one = new boolean[people];
			wild_two = new boolean[people];
			wild_one_index = new int[people];
			wild_two_index = new int[people];
			repicked_times = 0;
			player_word = new String[people][type.length];
			player_word_score = new int[people][type.length];
			curr_player = 0;
			names = new String[people];
			
			for(int i = 0;i<people;i++){
				
				names[i] ="Player "+(i+1);
				players[i] = new Player(names[i],bag);
				wild_one[i] = false;
				wild_two[i] = false;
				update_output = update_output+players[i].getHand()+"\n";
				
			}
		}
		
		setup();

	}
	
	public void setup(){
		
		user_word = "";
		for(int i=0;i<7;i++){
			tiles[i] = new Button(this);
			if(players[curr_player].getTile(i) == ' '){
				tiles[i].setOnClickListener(clickedBlank(i));
			}else{
				tiles[i].setOnClickListener(clickedTile(i));
			}
			layout.addView(tiles[i]);
			String tile_name = getTileString(players[curr_player].getTile(i));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[i].setBackgroundResource(imageResource);
		}
		
		word_type.setText(type[curr_type]);
		word_score = 0;
		score_view.setText(Integer.toString(word_score));
		//textview.setText(update_output);
		
	}
	
	public void showTiles(){
		for(int i=0;i<7;i++){
			if(players[curr_player].getTile(i) == ' '){
				tiles[i].setOnClickListener(clickedBlank(i));
			}else{
				tiles[i].setOnClickListener(clickedTile(i));
			}
			String tile_name = getTileString(players[curr_player].getTile(i));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[i].setBackgroundResource(imageResource);
		}
	}
	
	public void createWord(View v){
		if(user_word.equalsIgnoreCase("")){
			textview.setText("NO WORD MADE!!!");
		}else if(checker.checkWord(type[curr_type], user_word)){
			textview.setText("IT'S A WORD!!!");
			player_word_score[curr_player][curr_type] = word_score;
			word_score = 0;
			score_view.setText(user_word+" played! Score: "+players[curr_player].score());
			player_word[curr_player][curr_type] = user_word;
			changeTiles(user_word.toUpperCase());
			user_word = "";
			repicked_times = 0;
			if(curr_player == people-1){
				curr_player = 0;
				curr_type++;
			}else{
				curr_player++;
			}
			textview.setText(Integer.toString(curr_player));
			if(curr_type<type.length){
				word_type.setText(type[curr_type]);
				showTiles();
			}else{
				chooseWords();
				story.swapIntoStory(words_to_swap, old_words);
				textview.setText(story.getStoryString());
				score_view.setText(Integer.toString(players[curr_player].score()));
				startScoreScreen();
			}
		}else{
			textview.setText("TRY AGAIN!");
			user_word = "";
			word_score = 0;
			score_view.setText("0");
		}
		
	}
	
	public void changeTiles(String word){
		
		if(wild_one[curr_player]){
			players[curr_player].setTile(wild_one_index[curr_player], '[');
		}
		if(wild_two[curr_player]){
			players[curr_player].setTile(wild_two_index[curr_player], '[');
		}
		
		for(int i = 0;i<players[curr_player].getHand().length();i++){
			
			if(word.indexOf(players[curr_player].getTile(i)) >= 0){
				word.replace(players[curr_player].getTile(i), '*');
				players[curr_player].playTile(i);
			}
			
		}
		
		if(wild_one[curr_player]){
			players[curr_player].playTile(wild_one_index[curr_player]);
			wild_one[curr_player] = false;
		}
		if(wild_one[curr_player]){
			players[curr_player].playTile(wild_two_index[curr_player]);
			wild_two[curr_player] = false;
		}
		showTiles();
	}
	
	public void clearWord(View view){
		word_score = 0;
		if(wild_one[curr_player]){
			wild_one[curr_player] = false;
			players[curr_player].setTile(wild_one_index[curr_player], '[');
			String tile_name = getTileString(players[curr_player].getTile(wild_one_index[curr_player]));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[wild_one_index[curr_player]].setBackgroundResource(imageResource);
			tiles[wild_one_index[curr_player]].setOnClickListener(clickedBlank(wild_one_index[curr_player]));
		}
		if(wild_two[curr_player]){
			wild_two[curr_player] = false;
			players[curr_player].setTile(wild_one_index[curr_player], '[');
			String tile_name = getTileString(players[curr_player].getTile(wild_two_index[curr_player]));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[wild_two_index[curr_player]].setBackgroundResource(imageResource);
			tiles[wild_two_index[curr_player]].setOnClickListener(clickedBlank(wild_two_index[curr_player]));
		}
		user_word = "";
		score_view.setText(Integer.toString(word_score));
		textview.setText(null);
	}
	
	public void removeLast(View v){
		
		if (!user_word.equalsIgnoreCase("")){
			
			word_score = word_score-bag.getPoints(Character.toUpperCase(user_word.charAt(user_word.length()-1)));
			user_word = user_word.substring(0, user_word.length()-1);
			textview.setText(user_word);
			score_view.setText(Integer.toString(word_score));
			
		}
		
	}
	
	private View.OnClickListener clickedTile(final int index){

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				user_word = user_word + players[curr_player].getTile(index);
				user_word = user_word.toLowerCase();
				textview.setText(user_word);
				word_score += bag.getPoints(players[curr_player].getTile(index));
				score_view.setText(Integer.toString(word_score));
				
			}
			
		
		};
		
	}
	
	private View.OnClickListener clickedBlank(final int index){
		
		return new View.OnClickListener() {
			AlertDialog tileDialog;
			@Override
			public void onClick(View arg0) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
				builder.setTitle(R.string.choose_tile);
				builder.setSingleChoiceItems(bag.getAlphabet(), -1, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						tileDialog.dismiss();
						changeWildTile(index, which+65);
						
					}
				});
				
				tileDialog = builder.create();
				tileDialog.show();
			}
			
		};
		
	}
	
	private void changeWildTile(int index, int letter){
		
		if(!wild_one[curr_player]){
			
			wild_one[curr_player] = true;
			wild_one_index[curr_player]= index;
			
		}else{
			
			wild_two[curr_player] = true;
			wild_two_index[curr_player] = index;
			
		}
		
		char tile = (char) letter;
		
		tiles[index].setOnClickListener(clickedTile(index));
		players[curr_player].setTile(index, tile);
		int imageResource = getResources().getIdentifier(getTileString(tile), "drawable", getPackageName());
		tiles[index].setBackgroundResource(imageResource);
		
	}
	
	private String getTileString(char letter){
		
		if(letter == ' '){
			return "tile_blank";
		}else{
			return "tile_"+Character.toLowerCase(letter);
		}
		
	}
	
public View.OnClickListener repick(){
		
		return new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				
				final ArrayList<Integer> tile_index = new ArrayList<Integer>();

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle("Select Tiles to Replace ("+(repicked_times+1)+"/3)");
				builder.setMultiChoiceItems(players[curr_player].handArray(), null, new DialogInterface.OnMultiChoiceClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which,boolean isChecked){
						
						if(isChecked){
							tile_index.add(which);
						}else if(tile_index.contains(which)){
							tile_index.remove(Integer.valueOf(which));
						}
						
					}
				});
				builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						
						dialog.dismiss();
						clearWord(v);
						replaceTiles(tile_index);
						repicked_times++;
						if(repicked_times == 3){
							repick.setText("Pass");
							repick.setOnClickListener(pass());
						}
						
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						
						dialog.dismiss();
						
					}
				});
				
				AlertDialog replaceDialog = builder.create();
				replaceDialog.show();
				
			}
		};
	}

	private void replaceTiles(ArrayList<Integer> list){
		for(Integer x:list){
			char tile_letter = players[curr_player].replaceTile(x);
			String tile_name = getTileString(tile_letter);
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[x].setBackgroundResource(imageResource);
			if(players[curr_player].getTile(x) == ' '){
				tiles[x].setOnClickListener(clickedBlank(x));
			}else{
				tiles[x].setOnClickListener(clickedTile(x));
			}
		
		}
	
	}
	
	public View.OnClickListener pass(){
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				user_word = old_words[curr_type];
				word_score = 0;
				score_view.setText("No Points Scored");
				player_word[curr_player][curr_type] = user_word;
				player_word_score[curr_player][curr_type] = 0;
				user_word = "";
				if(curr_player == people-1){
					curr_player = 0;
					curr_type++;
				}else{
					curr_player++;
				}
				if(curr_type<type.length){
					word_type.setText(type[curr_type]);
					showTiles();
				}else{
					chooseWords();
					story.swapIntoStory(words_to_swap, old_words);
					textview.setText(story.getStoryString());
					score_view.setText(Integer.toString(players[curr_player].score()));
					startScoreScreen();
				}
				
				repick.setText("Repick");
				repicked_times = 0;
				repick.setOnClickListener(repick());
				
			}
		};
	}
	
	private void chooseWords(){
		
		for(int i = 0;i<type.length;i++){
			
			int score = 0;
			int round_winner = 0;
			for(int j = 0;j<people;j++){
				
				if(player_word_score[j][i] >= score){
					
					score = player_word_score[j][i];
					words_to_swap[i] = player_word[j][i];
					round_winner = j;
					
				}
				
			}
			players[round_winner].setScore(players[round_winner].score()+player_word_score[round_winner][i]);
			
		}
		
		int winner = -1;
		
		for(int i = 0;i<people;i++){
	
			if(players[i].score()>0){
				winner = i;
			}
		
		}
		
		if(winner == -1){
			done.putExtra("winner", "Tie");
		}else{
			done.putExtra("winner", players[winner].getName());
			done.putExtra("winner_score", players[winner].score());
		}
	}
	
	private void startScoreScreen(){
		String player_names[] = new String[people];
		int final_scores[] = new int[people];
		
		for(int person = 0;person<people;person++){
			
			player_names[person] = players[person].getName();
			done.putExtra(player_names[person], player_word[person]);
			done.putExtra(player_names[person]+"scores", player_word_score[person]);
			final_scores[person] = players[person].score();
			
		}
		done.putExtra("story", story.getStoryString());
		done.putExtra("players",player_names);
		done.putExtra("scores", final_scores);
		done.putExtra("types", type);
		startActivity(done);
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle saved) {
		
		 
		saved.putIntArray("bag_tile_type", bag.getTileSet());
		saved.putIntArray("bag_tile_points", bag.getTilePoints());
		saved.putInt("bag_number_tiles", bag.getNumTiles());
		
		saved.putInt("people", people);
		saved.putInt("curr_type", curr_type);
		saved.putInt("curr_player", curr_player);
		saved.putStringArray("match_words", type);
		saved.putStringArray("old_words", old_words);
		saved.putInt("repicks", repicked_times);
		saved.putStringArray("words_to_swap", words_to_swap);
		saved.putBooleanArray("wild_one", wild_one);
		saved.putIntArray("wild_one_index", wild_one_index);
		saved.putBooleanArray("wild_two", wild_two);
		saved.putIntArray("wild_two_index", wild_two_index);
		saved.putStringArray("names", names);
		
		for(int i = 0;i<people;i++){
	
			saved.putIntArray("player"+i+"score", player_word_score[i]);
			saved.putStringArray("player"+i+"word", player_word[i]);
			saved.putCharArray("player"+i+"hand", players[i].basic_hand());
			
		}
		
		String lines[][] = story.getLines();
		for(int i = 0;i<lines.length;i++){
			saved.putStringArray("line"+i, lines[i]);
		}
		saved.putInt("num_lines", lines.length);
		
	    super.onSaveInstanceState(saved);
	    
	}
	
	@Override
	public void onBackPressed() {
	}

}
