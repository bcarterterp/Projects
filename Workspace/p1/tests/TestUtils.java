package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class TestUtils {

	public static void main(String[] args) {
		buildParser();
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
	
	public static boolean tryTest(String testName) {
		try{
			String arg[] = new String[1];
			arg[0] = testName + ".c";
			
			String logName = testName + ".log";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(logName), false));
			System.setOut(new PrintStream(flog));
			mycc.parser.main(arg);
			flog.flush();
			flog.close();
			return compareParseOutput(logName,testName +".out");

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	// compare two files, ignoring spaces and newlines
	private static boolean compareParseOutput(String firstFile, String secondFile) throws IOException {
		String s1 = filterString(readParseOutput(firstFile));
		String s2 = filterString(readParseOutput(secondFile));
		// System.err.println(firstFile + " : " + s1);
		// System.err.println(secondFile + " : " + s2);
		return s1.equals(s2);
	}

	// read file in as a String & process any syntax errors found
	private static String readParseOutput(String fileName) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer errorBuffer = new StringBuffer();
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Scanner fileScanner = new Scanner(bufferedReader);
		
		// Take out line # for syntax errors and compare separately at end
		while (fileScanner.hasNextLine())  {
			String s = fileScanner.nextLine();
			// store body of error message
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
					errorBuffer.append(errMsg);
					errorBuffer.append("\n");
				}
			}
			// ignore syntax error warnings
			else if (s.startsWith("Syntax error")) {
				
			}
			// store parsed input, ignoring line number
			else {
				int idx = s.indexOf(":");
				stringBuffer.append(s.substring(idx+1));
				stringBuffer.append("\n");
			}
		}
		fileScanner.close();
		return stringBuffer.toString() + "\n" + errorBuffer.toString() + "\n";
	}

	private static String filterString(String input) {
		Scanner s = new Scanner(input);
		StringBuffer stringBuffer = new StringBuffer();
		while (s.hasNext()) {
			stringBuffer.append(s.next()+" ");
		}
		return stringBuffer.toString();
	}


}