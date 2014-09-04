

import java.util.ArrayList;

import brandon.carter.scrablibs.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SinglePlayer extends Activity {

	LetterBag bag;
	Player p1;
	LinearLayout layout;
	TextView textview,word_type,score_view;
	Button tiles[], repick;
	String user_word;
	WordCheck checker = new WordCheck();
	int curr_type, word_score,wild_one_index,wild_two_index,repicked_times;
	String match_words[],words_to_swap[],old_words[];
	StoryParser story;
	boolean wild_one,wild_two,repicked;
	Intent done;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		layout = (LinearLayout) findViewById(R.id.linlayout);
		textview = (TextView) findViewById(R.id.textView1);
		word_type = (TextView) findViewById(R.id.wordType);
		score_view = (TextView) findViewById(R.id.score);
		repick = (Button) findViewById(R.id.rePick);
		
		if(savedInstanceState != null){
			int tile_type[] = savedInstanceState.getIntArray("bag_tile_type");
			int tile_points[] = savedInstanceState.getIntArray("bag_tile_points");
			int num_tiles = savedInstanceState.getInt("bag_number_tiles");
			bag = new LetterBag(tile_type,tile_points,num_tiles);
			int score = savedInstanceState.getInt("score");
			char hand[] = savedInstanceState.getCharArray("hand");
			String name = savedInstanceState.getString("name");
			p1 = new Player(name, score, hand, bag);
			int num_lines = savedInstanceState.getInt("num_lines");
			String lines[][] = new String[num_lines][];
			
			for(int i = 0;i < num_lines;i++){
				lines[i] = savedInstanceState.getStringArray("line"+i);
				
			}
			story = new StoryParser(lines);
			curr_type = savedInstanceState.getInt("curr_type");
			match_words = savedInstanceState.getStringArray("match_words");
			words_to_swap = savedInstanceState.getStringArray("words_to_swap");
			old_words = savedInstanceState.getStringArray("old_words");
			repicked_times = savedInstanceState.getInt("repick");
			
		}else{
			
			Intent settings = getIntent();
			
			int num_lines = settings.getExtras().getInt("num_lines");
			String lines[][] = new String[num_lines][];
			for(int i = 0;i < num_lines;i++){
				lines[i] = settings.getExtras().getStringArray("line"+i);
				
			}
			
			bag = new LetterBag();
			p1 = new Player("Player",bag);
			story = new StoryParser(lines);
			match_words = settings.getExtras().getStringArray("match_words");
			old_words = settings.getExtras().getStringArray("old_words");
			words_to_swap = new String[match_words.length];
			curr_type = 0;
			repicked_times = 0;
		}

		done = new Intent(this, EndGame.class);
		tiles = new Button[7];
		repick.setOnClickListener(repick());
		repicked = false;
		
		setInit();			
		
	}
	
	private String getTileString(char letter){
		
		if(letter == ' '){
			return "tile_blank";
		}else{
			return "tile_"+Character.toLowerCase(letter);
		}
		
	}
	
	private void setInit(){
		user_word = "";
		wild_one = false;
		wild_two = false;
		for(int i=0;i<7;i++){
			tiles[i] = new Button(this);
			if(p1.getTile(i) == ' '){
				tiles[i].setOnClickListener(clickedBlank(i));
			}else{
				tiles[i].setOnClickListener(clickedTile(i));
			}
			layout.addView(tiles[i]);
			String tile_name = getTileString(p1.getTile(i));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[i].setBackgroundResource(imageResource);
		}
		word_type.setText(match_words[curr_type]);
		word_score = 0;
		score_view.setText(Integer.toString(word_score));
	}
	
	private View.OnClickListener clickedTile(final int index){

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				user_word = user_word + p1.getTile(index);
				user_word = user_word.toLowerCase();
				textview.setText(user_word);
				word_score += bag.getPoints(p1.getTile(index));
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
		
		if(!wild_one){
			
			wild_one = true;
			wild_one_index = index;
			
		}else{
			
			wild_two = true;
			wild_two_index = index;
			
		}
		
		char tile = (char) letter;
		
		tiles[index].setOnClickListener(clickedTile(index));
		p1.setTile(index, tile);
		int imageResource = getResources().getIdentifier(getTileString(tile), "drawable", getPackageName());
		tiles[index].setBackgroundResource(imageResource);
		
		
	}

	public void createWord(View v){
		if(user_word.equalsIgnoreCase("")){
			textview.setText("NO WORD MADE!!!");
		}else if(checker.checkWord(match_words[curr_type], user_word)){
			p1.setScore(p1.score()+word_score);
			word_score = 0;
			score_view.setText(user_word+" played! Score: "+p1.score());
			words_to_swap[curr_type] = user_word;
			changeTiles(user_word.toUpperCase());
			user_word = "";
			curr_type++;
			repicked = false;
			repicked_times = 0;
			if(curr_type<match_words.length){
				word_type.setText(match_words[curr_type]);
			}else{
				story.swapIntoStory(words_to_swap, old_words);
				textview.setText(story.getStoryString());
				score_view.setText(Integer.toString(p1.score()));
				done.putExtra("story", story.getStoryString());
				done.putExtra("score",p1.score());
				done.putExtra("mode", "single");
				done.putExtra("words", words_to_swap);
				done.putExtra("type", match_words); 
				startActivity(done);
			}
		}else{
			textview.setText("TRY AGAIN!");
			user_word = "";
			word_score = 0;
			score_view.setText("0");
		}
		
	}
	
	public void changeTiles(String word){
		
		if(wild_one){
			p1.setTile(wild_one_index, '[');
		}
		if(wild_two){
			p1.setTile(wild_two_index, '[');
		}
		
		for(int i = 0;i<p1.getHand().length();i++){
			
			if(word.indexOf(p1.getTile(i)) >= 0){
				word.replace(p1.getTile(i), '*');
				p1.playTile(i);
			}
			
		}
		
		if(wild_one){
			p1.playTile(wild_one_index);
			wild_one = false;
		}
		if(wild_one){
			p1.playTile(wild_two_index);
			wild_two = false;
		}
		showTiles();
	}
	
	public void showTiles(){
		for(int i=0;i<7;i++){
			if(p1.getTile(i) == ' '){
				tiles[i].setOnClickListener(clickedBlank(i));
			}else{
				tiles[i].setOnClickListener(clickedTile(i));
			}
			String tile_name = getTileString(p1.getTile(i));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[i].setBackgroundResource(imageResource);
		}
	}
	
	public void removeLast(View v){
		
		if (!user_word.equalsIgnoreCase("")){
			
			word_score = word_score-bag.getPoints(Character.toUpperCase(user_word.charAt(user_word.length()-1)));
			user_word = user_word.substring(0, user_word.length()-1);
			textview.setText(user_word);
			score_view.setText(Integer.toString(word_score));
			
		}
		
	}
	
	public void clearWord(View view){
		word_score = 0;
		if(wild_one){
			wild_one = false;
			p1.setTile(wild_one_index, '[');
			String tile_name = getTileString(p1.getTile(wild_one_index));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[wild_one_index].setBackgroundResource(imageResource);
			tiles[wild_one_index].setOnClickListener(clickedBlank(wild_one_index));
		}
		if(wild_two){
			wild_two = false;
			p1.setTile(wild_one_index, '[');
			String tile_name = getTileString(p1.getTile(wild_two_index));
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[wild_two_index].setBackgroundResource(imageResource);
			tiles[wild_two_index].setOnClickListener(clickedBlank(wild_two_index));
		}
		user_word = "";
		score_view.setText(Integer.toString(word_score));
		textview.setText(null);
	}
	
	private void replaceTiles(ArrayList<Integer> list){
		for(Integer x:list){
			char tile_letter = p1.replaceTile(x);
			String tile_name = getTileString(tile_letter);
			int imageResource = getResources().getIdentifier(tile_name, "drawable", getPackageName());
			tiles[x].setBackgroundResource(imageResource);
			if(p1.getTile(x) == ' '){
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
				score_view.setText("Score: "+p1.score());
				words_to_swap[curr_type] = user_word;
				user_word = "";
				curr_type++;
				showTiles();
				if(curr_type<match_words.length){
					word_type.setText(match_words[curr_type]);
				}else{
					story.swapIntoStory(words_to_swap, old_words);
					textview.setText(story.getStoryString());
					score_view.setText(Integer.toString(p1.score()));
					done.putExtra("story", story.getStoryString());
					done.putExtra("score",p1.score());
					done.putExtra("mode", "single");
					done.putExtra("words", words_to_swap);
					done.putExtra("type", match_words);
					startActivity(done);
				}
				
				repick.setText("Repick");
				repicked = false;
				repicked_times = 0;
				repick.setOnClickListener(repick());
				
			}
		};
	
	}
	
	public View.OnClickListener repick(){
		
		return new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				
				final ArrayList<Integer> tile_index = new ArrayList<Integer>();

				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle("Select Tiles to Replace ("+(repicked_times+1)+"/3)");
				builder.setMultiChoiceItems(p1.handArray(), null, new DialogInterface.OnMultiChoiceClickListener(){
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
						repicked = true;
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
	
	@Override
	public void onSaveInstanceState(Bundle saved) {
	 
		saved.putInt("score", p1.score());
		saved.putCharArray("hand", p1.basic_hand());
		saved.putString("name", p1.getName());
		saved.putIntArray("bag_tile_type", bag.getTileSet());
		saved.putIntArray("bag_tile_points", bag.getTilePoints());
		saved.putInt("bag_number_tiles", bag.getNumTiles());
		saved.putInt("curr_type", curr_type);
		saved.putStringArray("match_words", match_words);
		saved.putStringArray("old_words", old_words);
		saved.putStringArray("words_to_swap", words_to_swap);
		saved.putInt("repick", repicked_times);
		
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
