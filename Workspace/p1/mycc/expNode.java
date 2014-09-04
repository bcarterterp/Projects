
package mycc;

import java.util.*; 

//---------------------------------------------------------------------
// Stores type and content of C-- expressions.  
//     For integer constants, stores its value
//     For string literals, stores string as String object

class expNode {

	int exp_type;          // type of the variable, sym.* 
	int val;               // value of integer constant
	String str;            // content of literal string

	//----------------------------
	//----------------------------
	// constructors 

	//----------------------------
	// no param = void symbol

	expNode() { exp_type = sym.VOID; }

	//----------------------------
	// int param = symbol type

	expNode(int t) { exp_type = t; }

	//----------------------------
	// Integer param = should be integer symbol 

	expNode(int t, Integer n) 
	{ 
		if (t == sym.INT) {
			exp_type = t; 
			val = n.intValue(); 
		}
		else {
			parser.msg("illegal expNode for int");
		}
	}

	//----------------------------
	// String param = should be string symbol 

	expNode(int t, String s) 
		{ 
			if (t == sym.STRING_LITERAL) {
				exp_type = t; 
				str = s; 
			}
			else {
				parser.msg("illegal expNode for str");
			}
		}

	//----------------------------
	//----------------------------
	// access functions to return information about symbol

	int type() { return exp_type; }

	int val() { return val; }

	String str() { return str; }

	boolean isINT() 
		{ if (exp_type == sym.INT) return true; else return false ; }

	boolean isBOOL() 
		{ if (exp_type == sym.BOOL) return true; else return false ; }

	boolean isSTR() 
		{ if (exp_type == sym.STRING_LITERAL) return true; 
			else return false ; }
}


