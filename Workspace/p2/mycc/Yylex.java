package mycc;
import  java.lang.System;
import  java.lang.String;
import  java_cup.runtime.Symbol;


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	public final int YYEOF = -1;

	static String buf;
	static int lineno;
	// keeps track of current line number
	public static int pos() { return lineno; }
	// copies current token to buffer, output at newline 
	public void List() 
	{ 
		if (buf == null) 
			buf = new String("");
		buf = buf.concat(new String(yytext())); 
	}
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_END,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_END,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"15:9,38,39,15:2,40,15:18,38,32,36,15:3,24,15,5,6,4,2,21,3,15,14,35:10,15,1," +
"22,9,23,15:2,37:26,19,15,20,15:3,34,37:2,18,25,13,37,29,10,37:2,26,37,11,17" +
",37:2,30,27,12,31,16,28,37:3,7,33,8,15:2,0,41")[0];

	private int yy_rmap[] = unpackFromString(1,65,
"0,1:9,2,3,4,1:6,5,6,1:3,6,7,1:5,6:6,8,6,9,10,11,12,13,8,14,15,16,17,18,19,2" +
"0,21,22,23,24,25,26,27,28,29,30,31,32,33")[0];

	private int yy_nxt[][] = unpackFromString(34,42,
"1,2,3,4,5,6,7,8,9,10,11,38,55,61,12,13,56,38:2,14,15,16,17,18,39,57,38:2,62" +
",38,64,38,19,42,38,20,44,38,21,22,-1,1,-1:51,23,-1:42,38,41,38,24,-1:2,38:3" +
",-1:6,38:7,-1:2,38:2,-1,38,-1:18,25,-1:36,27,-1:42,38:4,-1:2,38:3,-1:6,38:7" +
",-1:2,38:2,-1,38,-1:5,25:38,-1:4,37:35,29,37:2,30,40,30,-1:24,26,-1:56,30,-" +
"1:12,38:2,31,38,-1:2,38:3,-1:6,38:7,-1:2,38:2,-1,38,-1:37,28,-1:18,38:4,-1:" +
"2,38:3,-1:6,32,38:6,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:2,33,-1:6,38:7,-1:2," +
"38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,34,38:6,-1:2,38:2,-1,38,-1:14,38:4,-1:" +
"2,38:3,-1:6,35,38:6,-1:2,38:2,-1,38,-1:14,38,36,38:2,-1:2,38:3,-1:6,38:7,-1" +
":2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:6,43,-1:2,38:2,-1,38,-1:14,45,38" +
":3,-1:2,38:3,-1:6,38:7,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:2,46,38" +
":4,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:2,43,38:4,-1:2,38:2,-1,38,-" +
"1:14,38:4,-1:2,38:3,-1:6,38,47,38:5,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1" +
":6,38:5,48,38,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:5,49,38,-1:2,38:" +
"2,-1,38,-1:14,38:4,-1:2,38,50,38,-1:6,38:7,-1:2,38:2,-1,38,-1:14,38:4,-1:2," +
"38:3,-1:6,38,51,38:5,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38,52,38:5,-" +
"1:2,38:2,-1,38,-1:14,53,38:3,-1:2,38:3,-1:6,38:7,-1:2,38:2,-1,38,-1:14,38:4" +
",-1:2,38:3,-1:6,38:6,54,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:7,-1:2" +
",58,38,-1,38,-1:14,38:4,-1:2,38:3,-1:6,38:4,59,38:2,-1:2,38:2,-1,38,-1:14,3" +
"8:2,60,38,-1:2,38:3,-1:6,38:7,-1:2,38:2,-1,38,-1:14,38:4,-1:2,38:3,-1:6,63," +
"38:6,-1:2,38:2,-1,38,-1:4");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

    return (new Symbol(sym.EOF));
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ List(); return new Symbol(sym.SEMI); }
					case -3:
						break;
					case 3:
						{ List(); return new Symbol(sym.PLUS); }
					case -4:
						break;
					case 4:
						{ List(); return new Symbol(sym.MINUS); }
					case -5:
						break;
					case 5:
						{ List(); return new Symbol(sym.MUL); }
					case -6:
						break;
					case 6:
						{ List(); return new Symbol(sym.LPAREN); }
					case -7:
						break;
					case 7:
						{ List(); return new Symbol(sym.RPAREN); }
					case -8:
						break;
					case 8:
						{ List(); return new Symbol(sym.LCURL ); }
					case -9:
						break;
					case 9:
						{ List(); return new Symbol(sym.RCURL ); }
					case -10:
						break;
					case 10:
						{ List(); return new Symbol(sym.ASSIGN ); }
					case -11:
						break;
					case 11:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -12:
						break;
					case 12:
						{ List(); return new Symbol(sym.DIV); }
					case -13:
						break;
					case 13:
						{ List(); parser.msg("illegal character "+yytext()); }
					case -14:
						break;
					case 14:
						{List(); return new Symbol(sym.LBRACKET); }
					case -15:
						break;
					case 15:
						{List(); return new Symbol(sym.RBRACKET); }
					case -16:
						break;
					case 16:
						{List(); return new Symbol(sym.COMMA); }
					case -17:
						break;
					case 17:
						{List(); return new Symbol(sym.LESS); }
					case -18:
						break;
					case 18:
						{List(); return new Symbol(sym.GR); }
					case -19:
						break;
					case 19:
						{List(); return new Symbol(sym.NOT_OP); }
					case -20:
						break;
					case 20:
						{ List(); 
		String s = new String(yytext());
		Integer i = new Integer(s);
		return new Symbol(sym.CONSTANT, i); }
					case -21:
						break;
					case 21:
						{ List(); }
					case -22:
						break;
					case 22:
						{ if (buf == null) buf = new String("");
		System.out.println(++lineno+": "+buf); 
		buf = new String(""); }
					case -23:
						break;
					case 23:
						{ List(); return new Symbol(sym.EQ_OP); }
					case -24:
						break;
					case 24:
						{ List(); return new Symbol(sym.IF ); }
					case -25:
						break;
					case 25:
						{List();}
					case -26:
						break;
					case 26:
						{List(); return new Symbol(sym.AND_OP); }
					case -27:
						break;
					case 27:
						{List(); return new Symbol(sym.NE_OP); }
					case -28:
						break;
					case 28:
						{List(); return new Symbol(sym.OR_OP); }
					case -29:
						break;
					case 29:
						{ List();	     
		String s = new String(yytext());
		s = s.substring(1,s.length()-1);
		return new Symbol(sym.STRING_LITERAL, s); }
					case -30:
						break;
					case 30:
						{List(); parser.msg("unterminated string");}
					case -31:
						break;
					case 31:
						{ List(); return new Symbol(sym.INT ); }
					case -32:
						break;
					case 32:
						{List(); return new Symbol(sym.BOOL); }
					case -33:
						break;
					case 33:
						{List(); return new Symbol(sym.VOID); }
					case -34:
						break;
					case 34:
						{List(); return new Symbol(sym.ELSE); }
					case -35:
						break;
					case 35:
						{List(); return new Symbol(sym.WHILE); }
					case -36:
						break;
					case 36:
						{List(); return new Symbol(sym.RETURN); }
					case -37:
						break;
					case 38:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -38:
						break;
					case 39:
						{ List(); parser.msg("illegal character "+yytext()); }
					case -39:
						break;
					case 40:
						{List(); parser.msg("unterminated string");}
					case -40:
						break;
					case 41:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -41:
						break;
					case 42:
						{ List(); parser.msg("illegal character "+yytext()); }
					case -42:
						break;
					case 43:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -43:
						break;
					case 44:
						{ List(); parser.msg("illegal character "+yytext()); }
					case -44:
						break;
					case 45:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -45:
						break;
					case 46:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -46:
						break;
					case 47:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -47:
						break;
					case 48:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -48:
						break;
					case 49:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -49:
						break;
					case 50:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -50:
						break;
					case 51:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -51:
						break;
					case 52:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -52:
						break;
					case 53:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -53:
						break;
					case 54:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -54:
						break;
					case 55:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -55:
						break;
					case 56:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -56:
						break;
					case 57:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -57:
						break;
					case 58:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -58:
						break;
					case 59:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -59:
						break;
					case 60:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -60:
						break;
					case 61:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -61:
						break;
					case 62:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -62:
						break;
					case 63:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -63:
						break;
					case 64:
						{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }
					case -64:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
