
package mycc;

import  java.lang.System;
import  java.lang.String;
import  java_cup.runtime.Symbol;

%%
%notunix
%cup
%yyeof
%eofval{
    return (new Symbol(sym.EOF));
%eofval} 
   
 
%{
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

%}

%%

";" { List(); return new Symbol(sym.SEMI); }

"+" { List(); return new Symbol(sym.PLUS); }

"-" { List(); return new Symbol(sym.MINUS); }

"*" { List(); return new Symbol(sym.MUL); }

"(" { List(); return new Symbol(sym.LPAREN); }

")" { List(); return new Symbol(sym.RPAREN); }

"{" { List(); return new Symbol(sym.LCURL ); }

"}" { List(); return new Symbol(sym.RCURL ); }

"=" { List(); return new Symbol(sym.ASSIGN ); }

"==" { List(); return new Symbol(sym.EQ_OP); }

"int" { List(); return new Symbol(sym.INT ); }

"if" { List(); return new Symbol(sym.IF ); }

"/" { List(); return new Symbol(sym.DIV); }

[/][/](.)* {List();}

"void" {List(); return new Symbol(sym.VOID); }

"(" {List(); return new Symbol(sym.LPAREN); }

")" {List(); return new Symbol(sym.RPAREN); }

"[" {List(); return new Symbol(sym.LBRACKET); }

"]" {List(); return new Symbol(sym.RBRACKET); }

"{" {List(); return new Symbol(sym.LCURL); }

"}" {List(); return new Symbol(sym.RCURL); }

"," {List(); return new Symbol(sym.COMMA); }

"<" {List(); return new Symbol(sym.LESS); }

">" {List(); return new Symbol(sym.GR); }

"&&" {List(); return new Symbol(sym.AND_OP); }

"else" {List(); return new Symbol(sym.ELSE); }

"while" {List(); return new Symbol(sym.WHILE); }

"return" {List(); return new Symbol(sym.RETURN); }

"!=" {List(); return new Symbol(sym.NE_OP); }

"!" {List(); return new Symbol(sym.NOT_OP); }	

"||" {List(); return new Symbol(sym.OR_OP); }

"true"|"false" {List(); return new Symbol(sym.BOOL); }

([0-9]+)
	{ List(); 
		String s = new String(yytext());
		Integer i = new Integer(s);
		return new Symbol(sym.CONSTANT, i); }

\"[^\"\r\n]*[\"]
	{ List();	     
		String s = new String(yytext());
		s = s.substring(1,s.length()-1);
		return new Symbol(sym.STRING_LITERAL, s); }

(([a-z])|([0-9])|([A-Z]))+
	{  List(); 
		String s = new String(yytext());
		return new Symbol(sym.IDENTIFIER, s); }

[ \t] { List(); }

\n { if (buf == null) buf = new String("");
		System.out.println(++lineno+": "+buf); 
		buf = new String(""); }
		
\"[^\"\r\n]*$ {List(); parser.msg("unterminated string");}

. { List(); parser.msg("illegal character "+yytext()); }

