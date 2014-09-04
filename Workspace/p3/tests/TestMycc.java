package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class TestMycc {

	private static Boolean built = false;

	public static void main(String[] args) {
		if (args[0].equals("testCodegen1")) {
			if (testCodegen1()) 
				System.exit(0);
		} 
		else if (args[0].equals("testCodegen2")) {
			if (testCodegen2()) 
				System.exit(0);
		} 
		else if (args[0].equals("testCodegen3")) {
			if (testCodegen3()) 
				System.exit(0);
		} 
		else if (args[0].equals("testCodegen4")) {
			if (testCodegen4()) 
				System.exit(0);
		} 
		else if (args[0].equals("testCodegen5")) {
			if (testCodegen5()) 
				System.exit(0);
		} 
		else if (args[0].equals("testOpt1")) {
			if (testOpt1()) 
				System.exit(0);
		} 
		else if (args[0].equals("testOpt2")) {
			if (testOpt2()) 
				System.exit(0);
		} 
		else 
			System.out.println("Usage: java TestMycc <testname>");

		System.exit(-1);

		/*
		Integer mode = Integer.valueOf(args[1]);
		if (tryTest(args[0], mode))
			System.exit(0);
		System.exit(-1);
		 */

	}

	private static void systemCmd(String cmd) 
	{ 
		// System.out.println("systemCmd: " + cmd);

		StringBuffer stringBuffer = new StringBuffer();
		try 
		{ 
			Process p=Runtime.getRuntime().exec(cmd); 
			p.waitFor(); 
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
			String line=reader.readLine(); 
			while(line!=null) 
			{ 
				System.out.println(line); 
				line=reader.readLine(); 
			} 

		} 
		catch (Exception e) {
			System.out.println("Exception: while executing systemCmd " + cmd);
			e.printStackTrace();
		}
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

	// mode 0 = optimization off, check only output
	// mode 1 = optimization on, also check AST

	public static boolean tryTest(String testName, int mode) {
		try{
			String arg[] = null;

			if (mode == 0) {
				arg = new String[1];
				arg[0] = testName + ".c";

			} 
			else if (mode == 1) {
				arg = new String[2];
				arg[0] = "-O";
				arg[1] = testName + ".c";

			}
			else {
				System.err.println("TRYTEST: unsupported mode " + mode);
				System.exit(1);
			}

			String logName = testName + ".log";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(logName), false));
			System.setOut(new PrintStream(flog));
			mycc.parser.main(arg);

			// System.out.println("** Decompiling " + testName + ".class **");
			// systemCmd("javap -c" + testName);

			System.out.println("** Executing " + testName + ".class **");
			systemCmd("java " + testName);

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
		
		System.err.println(firstFile + " : " + s1);
		System.err.println(secondFile + " : " + s2);
		
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
			else if (s.startsWith("** Executing")) {
				fileStage = 3;
			}

			// store parsed input, ignoring line number
			if (fileStage == 0) {			// input
				if (false) {
					int idx = s.indexOf(":");
					stringBuffer.append(s.substring(idx+1));
					stringBuffer.append("\n");
				}
			}
			else if (fileStage == 1) {		// symtab
				if (false)
					stringBuffer.append(s + "\n");
			}
			else if (fileStage == 2) {		// AST
				if (mode == 1)
					stringBuffer.append(s + "\n");
			}
			else if (fileStage == 3) {		// runtime output
				if ((mode == 0) || (mode == 1))
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
		return stringBuffer.toString();
	}

	public static boolean testCodegen1() {
		return tryTest("test01", 0) && tryTest("test02", 0);	
	}

	public static boolean testCodegen2() {
		return tryTest("test03", 0) && tryTest("test04", 0);	
	}

	public static boolean testCodegen3() {
		return tryTest("test05", 0) && tryTest("test06", 0);	
	}

	public static boolean testCodegen4() {
		return tryTest("test07", 0) && tryTest("test08", 0);	
	}

	public static boolean testCodegen5() {
		return tryTest("test09", 0) && tryTest("test10", 0);	
	}
	
	public static boolean testOpt1() {
		return tryTest("test09", 1);	
	}
	
	public static boolean testOpt2() {
		return tryTest("test10", 1);	
	}


}