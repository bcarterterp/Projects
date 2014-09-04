# compile & build mycc

all:
	java JLex.Main mycc.lex
	mv -f mycc.lex.java mycc/Yylex.java
	java java_cup.Main mycc.cup
	mv -f sym.java mycc
	mv -f parser.java mycc
	javac -g -d . mycc/*.java
	
clean:
	rm -f *~ *.log

unix:
	dos2unix *.c
	dos2unix *.out
	dos2unix */*.java
	dos2unix mycc.lex
	dos2unix mycc.cup

runMycc:
	java mycc.parser toy1.c > toy1.log
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

runPublicTests:
	java tests/TestMycc testAST1
	java tests/TestMycc testAST2
	java tests/TestMycc testSymtab1
	java tests/TestMycc testSymtab2
	java tests/TestMycc testTypeCorrect
	java tests/TestMycc testTypeError1
	java tests/TestMycc testTypeError2
	java tests/TestMycc testTypeError3

