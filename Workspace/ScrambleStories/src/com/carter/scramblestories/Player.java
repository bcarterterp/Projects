package com.carter.scramblestories;

public class Player{

	private String name;
	private int score;
	private char hand[];
	LetterBag bag;

	public Player(String name, LetterBag bag) {

		score = 0;
		this.name = name;
		this.bag = bag;
		hand = this.bag.getGameHand();

	}
	
	public Player(String name, int score, char hand[], LetterBag bag){
		this.score = score;
		this.name = name;
		this.hand = hand;
		this.bag = bag;
	}

	public void playTile(int num) {

		hand[num] = 48;
		
		if(num < 5){
			hand[num] = bag.getNewCons(hand);
		}else{
			hand[num] = bag.getNewVowel(hand);
		}

	}
	
	public void setScore(int new_score){
		score = new_score;
	}

	public int score() {
		return score;
	}

	public String getName() {
		return name;
	}
	
	public String getHand(){
		return new String(hand);
	}
	public void setTile(int index, char letter){
		hand[index] = letter;
		
	}
	
	public char getTile(int index){
		if (hand[index] == '['){
			
			return ' ';
			
		}else{
			
			return hand[index];
			
		}
	}
	
	public CharSequence[] handArray(){
		
		String return_hand[] = new String[hand.length];
		
		for(int i = 0;i<hand.length;i++){
			if(hand[i] == '['){
				return_hand[i] = "Blank";
			}else{
			return_hand[i] = Character.toString(hand[i]);
			}
		}
		
		return return_hand;
	}
	
	public char[] basic_hand(){
		return hand;
	}
	
	public char replaceTile(int index){
		
		hand[index] = bag.replaceTile(hand[index]);
		if(hand[index] == '['){
			return ' ';
		}
		return hand[index];
		
	}


}
