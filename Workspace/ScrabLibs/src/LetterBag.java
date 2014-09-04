

import java.util.Random;

public class LetterBag {

	private int tiles;
	private int tile_set[];
	private int tile_points[];
	private static int TILE_TYPES = 27;
	private static int ASCII_OFFSET = 65;
	private Random randomTile = new Random();

	public LetterBag() {

		tile_set = new int[TILE_TYPES];
		tile_points = new int[TILE_TYPES];
		setTiles();
		setPoints();
		numTiles();

	}
	
	public LetterBag(int tile_s[],int tile_p[], int num_tiles){
		tile_set = tile_s;
		tile_points = tile_p;
		tiles = num_tiles;
	}

	private void numTiles() {

		for (int i = 0; i < TILE_TYPES; i++) {
			tiles += tile_set[i];
		}
	}

	private void setTiles() {

		tile_set[0] = 9; // A
		tile_set[1] = 2; // B
		tile_set[2] = 2; // C
		tile_set[3] = 4; // D
		tile_set[4] = 12; // E
		tile_set[5] = 2; // F
		tile_set[6] = 3; // G
		tile_set[7] = 2; // H
		tile_set[8] = 9; // I
		tile_set[9] = 1; // J
		tile_set[10] = 1; // K
		tile_set[11] = 4; // L
		tile_set[12] = 2; // M
		tile_set[13] = 6; // N
		tile_set[14] = 8; // O
		tile_set[15] = 2; // P
		tile_set[16] = 1; // Q
		tile_set[17] = 6; // R
		tile_set[18] = 4; // S
		tile_set[19] = 6; // T
		tile_set[20] = 4; // U
		tile_set[21] = 2; // V
		tile_set[22] = 2; // W
		tile_set[23] = 1; // X
		tile_set[24] = 2; // Y
		tile_set[25] = 1; // Z
		tile_set[26] = 2; // Space

	}

	public void setPoints() {

		tile_points[0] = 1; // A
		tile_points[1] = 3; // B
		tile_points[2] = 3; // C
		tile_points[3] = 2; // D
		tile_points[4] = 1; // E
		tile_points[5] = 4; // F
		tile_points[6] = 2; // G
		tile_points[7] = 4; // H
		tile_points[8] = 1; // I
		tile_points[9] = 8; // J
		tile_points[10] = 5; // K
		tile_points[11] = 1; // L
		tile_points[12] = 3; // M
		tile_points[13] = 1; // N
		tile_points[14] = 1; // O
		tile_points[15] = 3; // P
		tile_points[16] = 10; // Q
		tile_points[17] = 1; // R
		tile_points[18] = 1; // S
		tile_points[19] = 1; // T
		tile_points[20] = 1; // U
		tile_points[21] = 4; // V
		tile_points[22] = 4; // W
		tile_points[23] = 8; // X
		tile_points[24] = 4; // Y
		tile_points[25] = 10; // Z
		tile_points[26] = 0; // Space

	}

	public char getTile() {

		tiles--;
		int tile = randomTile.nextInt(TILE_TYPES);

		while (tile_set[tile] <= 0) {

			tile = randomTile.nextInt(TILE_TYPES);

		}

		tile_set[tile]--;
		char return_char[] = Character.toChars(tile + ASCII_OFFSET);
		return return_char[0];
	}

	public char replaceTile(int old_tile) {

		tile_set[old_tile-ASCII_OFFSET]++;
		return getTile();

	}
	public char[] replaceTiles(char old_tiles[]) {

		for (int i = 0; i < old_tiles.length; i++) {

			tile_set[Character.getNumericValue(old_tiles[i]-ASCII_OFFSET)]++;

		}

		return getTiles(old_tiles.length);

	}

	public char[] getTiles(int num) {

		char tiles[] = new char[num];

		for (int i = 0; i < num; i++) {

			tiles[i] = getTile();

		}

		return tiles;

	}

	public int totalTiles() {
		return tiles;
	}

	public void displayTiles() {

		for (int i = 0; i < 26; i++) {

			System.out.print(Character.toChars(i + ASCII_OFFSET));
			System.out.println(" = " + tile_set[i]);

		}

		System.out.println("Blanks = " + tile_set[26]);

	}

	public int getPoints(int num) {

		return tile_points[num-ASCII_OFFSET];

	}
	
	public char getCons(){
		
		int tile = randomTile.nextInt(TILE_TYPES);
		char letter = (char) (tile+ASCII_OFFSET);

		while (tile_set[tile] <= 0 || isVowel(letter)) {

			tile = randomTile.nextInt(TILE_TYPES);
			letter = (char) (tile+ASCII_OFFSET);

		}

		char return_char[] = Character.toChars(tile + ASCII_OFFSET);
		return return_char[0];
		
	}
	
	public char getVowel(){
		
		int tile = randomTile.nextInt(TILE_TYPES);
		char letter = (char) (tile+ASCII_OFFSET);

		while (tile_set[tile] <= 0 || !isVowel(letter)) {
			tile = randomTile.nextInt(TILE_TYPES);
			letter = (char) (tile+ASCII_OFFSET);

		}

		char return_char[] = Character.toChars(tile + ASCII_OFFSET);
		return return_char[0];
		
	}
	
	public boolean isVowel(char letter){
		if(letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U' || letter == 'Y'){
			return true;
		}else{
			return false;
		}
	}

	public char[] getGameHand(){
		
		char hand[] = new char[7];
		hand[0] = getCons();
		char letter = getCons();
		for(int i = 1;i<hand.length-2;i++){
			
			while(!isUniq(hand, letter)){
				letter = getCons();
			}
			
			hand[i] = letter;
			
		}
		
		hand[5] = getVowel();
		letter = getVowel();
		while(!isUniq(hand, letter)){
			letter = getVowel();
		}
		hand[6] = letter;
		
		return hand;
		
	}
	
	private boolean isUniq(char hand[],char letter){
		
		for(int i = 0;i<hand.length;i++){
			
			if(hand[i] == letter){
				return false;
			}
			
		}
		
		tiles--;
		tile_set[letter-ASCII_OFFSET]--;
		return true;
		
	}
	
	public char getNewCons(char hand[]){
		
		char letter = getCons();
		
		while(!isUniq(hand,letter)){
			letter = getCons();
		}
		
		return letter;
		
	}
	
	public char getNewVowel(char hand[]){
		
		char letter = getVowel();
		
		while(!isUniq(hand,letter)){
			letter = getVowel();
		}
		
		return letter;
		
	}
	
	public String[] getAlphabet(){
		
		String alpha[] = new String[26];
		String word = "";
		char letter;
		for(int i = 0;i<26;i++){
			letter = (char) (i+ASCII_OFFSET);
			alpha[i] = word+letter;
		}
		
		return alpha;
		
	}
	
	public int[] getTileSet(){
		return tile_set;
	}
	
	public int[] getTilePoints(){
		return tile_points;
	}
	
	public int getNumTiles(){
		return tiles;
	}
	
}
