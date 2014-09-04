del Yylex.java parser.java sym.bat
java JLex.Main mycc.lex
ren mycc.lex.java mycc\Yylex.java
java java_cup.Main < mycc.cup
ren sym.java mycc\sym.java
ren parser.java mycc\parser.java
javac -d . mycc\*.java
java mycc.parser test1.c > test1.log
java mycc.parser test2.c > test2.log
java mycc.parser test3.c > test3.log
java mycc.parser test4.c > test4.log
java mycc.parser test5.c > test5.log

