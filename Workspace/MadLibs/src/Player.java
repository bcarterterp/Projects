

public class Player {

	private String name;
	private int score;
	private char hand[];
	LetterBag bag;

	public Player(String name, LetterBag bag, int num) {

		score = 0;
		this.name = name;
		this.bag = bag;
		hand = this.bag.getTiles(num);

	}

	public char playTile(int num) {

		char return_char = hand[num];
		score += bag.getPoints(hand[num]);
		hand[num] = bag.getTile();
		return return_char;

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

}
