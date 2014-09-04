del Yylex.java parser.java sym.bat
java JLex.Main mycc.lex
ren mycc.lex.java mycc\Yylex.java
java java_cup.Main < mycc.cup
ren sym.java mycc\sym.java
ren parser.java mycc\parser.java
javac -d . mycc\*.java
java mycc.parser test01.c > test01.log
java mycc.parser test02.c > test02.log
java mycc.parser test03.c > test03.log
java mycc.parser test04.c > test04.log
java mycc.parser test05.c > test05.log
java mycc.parser test06.c > test06.log
java mycc.parser test11.c > test11.log
java mycc.parser test12.c > test12.log
java mycc.parser test13.c > test13.log
java mycc.parser test14.c > test14.log
java mycc.parser test15.c > test15.log
java mycc.parser test16.c > test16.log
