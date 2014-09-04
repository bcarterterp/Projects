package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class TestMycc {

	private static Boolean built = false;

	public static void main(String[] args) {
		if (args[0].equals("testSymtab1")) {
			if (testSymtab1()) 
				System.exit(0);
		} else if (args[0].equals("testSymtab2")) {
			if (testSymtab2()) 
				System.exit(0); 
		} else if (args[0].equals("testTypeCorrect")) {
			if (testTypeCorrect()) 
				System.exit(0); 
		} else if (args[0].equals("testTypeError1")) {
			if (testTypeError1()) 
				System.exit(0); 
		} else if (args[0].equals("testTypeError2")) {
			if (testTypeError2()) 
				System.exit(0); 
		} else if (args[0].equals("testTypeError3")) {
			if (testTypeError3()) 
				System.exit(0); 
		} else if (args[0].equals("testAST1")) {
			if (testAST1()) 
				System.exit(0); 
		} else if (args[0].equals("testAST2")) {
			if (testAST2()) 
				System.exit(0); 
		} else 
			System.out.println("Usage: java TestMycc <testname>");

		System.exit(-1);
		
		/*
		Integer mode = Integer.valueOf(args[1]);
		if (tryTest(args[0], mode))
			System.exit(0);
		System.exit(-1);
		*/
		
	}

	private static void moveFile(String fromFile, String toFile) {
		File f = new File(fromFile); 
		f.renameTo(new File(toFile));
	}

	private static void deleteFile(String fileName) {

		// A File object to represent the filename
		File f = new File(fileName);

		// Make sure the file or directory exists and isn't write protected
		if (!f.exists())
			return;
		// throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);

		if (!f.canWrite())
			throw new IllegalArgumentException("Delete: write protected: "
					+ fileName);

		// If it is a directory, make sure it is empty
		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException(
						"Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = f.delete();

		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");
	}

	public static synchronized boolean buildParser() {

		try {
			String arg[] = new String[1];

			if (built == true)
				return true;

			built = true;

			// remove from mycc directory Yylex.java, parser.java, and sym.java
			deleteFile("mycc/Yylex.java");
			deleteFile("mycc/parser.java");
			deleteFile("mycc/sym.java");

			// java JLex.Main mycc.lex
			arg[0] = "mycc.lex";
			JLex.Main.main(arg);

			// move mycc.lex.java to mycc directory as Yylex.java
			moveFile("mycc.lex.java", "mycc/Yylex.java");

			// java java_cup.Main mycc.cup
			arg[0] = "mycc.cup";
			java_cup.Main.main(arg);	

			// mv parser.java & sym.java to mycc directory
			moveFile("parser.java", "mycc/parser.java");
			moveFile("sym.java", "mycc/sym.java");
		}
		catch (Exception e) {
			System.out.println("Exception: while creating parser ");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// mode 0 = check everything
	// mode 1 = check only symbol table
	// mode 2 = check only AST
	// mode 3 = check only type errors

	public static boolean tryTest(String testName, int mode) {
		try{
			String arg[] = new String[1];
			arg[0] = testName + ".c";

			String logName = testName + ".log";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(logName), false));
			System.setOut(new PrintStream(flog));
			mycc.parser.main(arg);
			flog.flush();
			flog.close();
			return compareParseOutput(logName,testName +".out",mode);

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	// compare two files, ignoring spaces and newlines
	private static boolean compareParseOutput(String firstFile, String secondFile, int mode) throws IOException {
		String s1 = filterString(readParseOutput(firstFile,mode));
		String s2 = filterString(readParseOutput(secondFile,mode));
		// System.err.println(firstFile + " : " + s1);
		// System.err.println(secondFile + " : " + s2);
		return s1.equals(s2);
	}

	// read file in as a String & process any syntax errors found
	private static String readParseOutput(String fileName, int mode) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer errorBuffer = new StringBuffer();
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Scanner fileScanner = new Scanner(bufferedReader);
		int fileStage = 0;

		// Take out line # for syntax errors and compare separately at end
		while (fileScanner.hasNextLine())  {
			String s = fileScanner.nextLine();

			if (s.equals("** Global Variables **")) {
				fileStage = 1;
			}
			// Beginning of AST info
			else if (s.startsWith("TREE_PROC")) {
				fileStage = 2;
			}

			// store body of error messages in errorBuffer
			if (s.startsWith("ERROR")) {
				int idx = s.indexOf(")");
				String errMsg = s.substring(idx+2);
				// only look for the following error messages
				if (errMsg.equals("unterminated string") 
						|| errMsg.equals("illegal expression")
						|| errMsg.equals("illegal variable declaration")
						|| errMsg.equals("missing semicolon")
				)
				{
					System.err.println("Found Syntax Error: " + errMsg);
					// ignore syntax errors
					// errorBuffer.append(errMsg);
					// errorBuffer.append("\n");
				}
				else if (errMsg.startsWith("multiple declaration of")
						|| errMsg.startsWith("forward decl mismatch")
						|| errMsg.startsWith("return mismatch")
						|| errMsg.startsWith("assignment type mismatch")
						|| errMsg.startsWith("undefined variable")
						|| errMsg.startsWith("misuse of")																																			|| errMsg.startsWith("")
						|| errMsg.startsWith("illegal subscript")
						|| errMsg.startsWith("parameter mismatch")
						|| errMsg.startsWith("undefined function")
						|| errMsg.startsWith("operand type mismatch")
						|| errMsg.startsWith("requires integer")	
						|| errMsg.startsWith("requires boolean")
				){
					errorBuffer.append(errMsg);
					errorBuffer.append("\n");
				}
			}
			// unexpected syntax error 
			else if (s.startsWith("Syntax error")) {
				System.err.println("Found Syntax Error");
			}

			// store parsed input, ignoring line number
			else if (fileStage == 0) {
				if (mode == 0) {
					int idx = s.indexOf(":");
					stringBuffer.append(s.substring(idx+1));
					stringBuffer.append("\n");
				}
			}
			else if (fileStage == 1) {
				if ((mode == 0) || (mode == 1))
					stringBuffer.append(s + "\n");
			}
			else if (fileStage == 2) {
				if ((mode == 0) || (mode == 2))
					stringBuffer.append(s + "\n");
			}
		}
		fileScanner.close();
		String result;
		if (mode != 3)
			result = stringBuffer.toString() + "\n";
		else 
			result = errorBuffer.toString() + "\n"; 
		return result;
	}

	private static String filterString(String input) {
		Scanner s = new Scanner(input);
		StringBuffer stringBuffer = new StringBuffer();
		while (s.hasNext()) {
			stringBuffer.append(s.next()+" ");
		}
		s.close();
		return stringBuffer.toString();
	}

	public static boolean testSymtab1() {
		return tryTest("test01", 1) 
		&& tryTest("test02", 1) 
		&& tryTest("test03", 1);
	}

	public static boolean testSymtab2() {
		return tryTest("test04", 1) 
		&& tryTest("test05", 1) 
		&& tryTest("test06", 1);
	}

	public static boolean testTypeCorrect() {
		return tryTest("test01", 3) 
		&& tryTest("test02", 3) 
		&& tryTest("test03", 3)
		&& tryTest("test04", 3) 
		&& tryTest("test05", 3) 
		&& tryTest("test06", 3);
	}

	public static boolean testTypeError1() {
		return tryTest("test11", 3) 
		&& tryTest("test12", 3);
	}

	public static boolean testTypeError2() {
		return tryTest("test13", 3) 
		&& tryTest("test14", 3);
	}

	public static boolean testTypeError3() {
		return tryTest("test15", 3) 
		&& tryTest("test16", 3);
	}

	public static boolean testAST1() {
		return tryTest("test01", 2) 
		&& tryTest("test02", 2) 
		&& tryTest("test03", 2);
	}

	public static boolean testAST2() {
		return tryTest("test04", 2) 
		&& tryTest("test05", 2) 
		&& tryTest("test06", 2);
	}
}