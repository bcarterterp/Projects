package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

import mycc.ClassFile;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;

public class TestMycc implements Constants {

	private static Boolean built = false;

	public static void main(String[] args) {

		if (args[0].equals("testCFG1")) {
			if (testCFG1()) 
				System.exit(0);
		} 
		else if (args[0].equals("testCFG2")) {
			if (testCFG2()) 
				System.exit(0);
		}
		else if (args[0].equals("testLive1")) {
			if (testLive1()) 
				System.exit(0);
		}
		else if (args[0].equals("testLive2")) {
			if (testLive2()) 
				System.exit(0);
		}
		else if (args[0].equals("testLive3")) {
			if (testLive3()) 
				System.exit(0);
		}
		else if (args[0].equals("testLive4")) {
			if (testLive4()) 
				System.exit(0);
		}
		else if (args[0].equals("testInit1")) {
			if (testInit1()) 
				System.exit(0);
		}
		else if (args[0].equals("testDead1")) {
			if (testDead1()) 
				System.exit(0);
		}
		else 
			System.out.println("Usage: java TestMycc <testname>");

		System.exit(-1);
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

			return true;

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

	// read file in as a String & process 
	// mode 0 = cfg
	// mode 1 = live variables
	// mode 2 = uninitialized vars
	// mode 3 = dead code
	private static String readParseOutput(String fileName, int mode) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer errorBuffer = new StringBuffer();
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Scanner fileScanner = new Scanner(bufferedReader);
		int fileStage = 0;

		while (fileScanner.hasNextLine())  {
			String s = fileScanner.nextLine();

			if (s.equals("** Basic block: 0 **")) {
				fileStage = 0;
			}
			// Beginning of Live Range info
			else if (s.startsWith("<<---- Live Ranges ---->>")) {
				fileStage = 1;
			}
			// uninitialized variable warning
			else if (s.startsWith("WARNING: possible uninitialized local variable index")) {
				fileStage = 2;
			}
			else if (s.startsWith("<<---- Optimized code ---->>")) {
				fileStage = 3;
			}

			// store instruction, ignoring instruction number
			if (fileStage == 0) {		// CFG, only look at pred/succ info
				if (mode == 0 && s.contains("Successors")) {
					stringBuffer.append(s + "\n");
				}
			}
			else if (fileStage == 1) {		// Live range, ignore instruction
				if (mode > 0) {
					int idx = s.indexOf(":");
					if (idx > 3)
						stringBuffer.append(s.substring(0, idx-3));
					else
						stringBuffer.append(s);
					stringBuffer.append("\n");
				}
			}
			else if (fileStage == 2) {		// Look at all warnings
				if (mode == 2 && s.contains("uninitialized")) {
					stringBuffer.append(s + "\n");
				}
			}
			else if (fileStage == 3) {			// Opt code, ignore instruction #
				if (mode == 3) {
					int idx = s.indexOf(":");
					stringBuffer.append(s.substring(idx+1));
					stringBuffer.append("\n");
				}
			}
		}
		fileScanner.close();
		String result;
		result = stringBuffer.toString() + "\n";
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

	public static boolean optTest(String testName, int mode) {
		try{
			String logName = testName + "Opt.log";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(logName), false));
			System.setOut(new PrintStream(flog));
			test07();
			flog.flush();
			flog.close();
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static void test01() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });
		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_2 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_4 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_6 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_9 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_12 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 6));
		InstructionHandle ih_15 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 7));
		InstructionHandle ih_18 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_22 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_26 = il.append(_factory.createLoad(Type.INT, 5));
		il.append(_factory.createLoad(Type.INT, 7));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_32 = il.append(_factory.createLoad(Type.INT, 6));
		il.append(new PUSH(_cp, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));

		// InstructionHandle ih_37 = il.append(new IINC(1, -3));
		InstructionHandle ih_37 = il.append(new PUSH(_cp, -3));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_40 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 5));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 6));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 7));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_58 = il.append(new PUSH(_cp, "Result (should be 8) = "));
		il.append(_factory.createInvoke("test01", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_63 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createInvoke("test01", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_67 = il.append(_factory.createInvoke("test01", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_70 = il.append(_factory.createReturn(Type.VOID));
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);
	}

	public static void test02() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, 0));
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_3 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_5 = il.append(new PUSH(_cp, 2));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_7 = il.append(new PUSH(_cp, 3));
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_9 = il.append(_factory.createLoad(Type.INT, 4));
		BranchInstruction ifeq_11 = _factory.createBranchInstruction(Constants.IFEQ, null);
		il.append(ifeq_11);

		// InstructionHandle ih_14 = il.append(new IINC(1, 1));
		InstructionHandle ih_14 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_17 = il.append(new PUSH(_cp, 5));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 2));
		BranchInstruction goto_21 = _factory.createBranchInstruction(Constants.GOTO, null);
		il.append(goto_21);
		InstructionHandle ih_24 = il.append(new PUSH(_cp, 6));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_29 = il.append(new PUSH(_cp, 3));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_33 = il.append(_factory.createLoad(Type.INT, 4));
		il.append(new PUSH(_cp, 2));
		BranchInstruction if_icmpge_36 = _factory.createBranchInstruction(Constants.IF_ICMPGE, null);
		il.append(if_icmpge_36);
		InstructionHandle ih_39 = il.append(new PUSH(_cp, 5));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));
		BranchInstruction goto_43 = _factory.createBranchInstruction(Constants.GOTO, null);
		il.append(goto_43);
		InstructionHandle ih_46 = il.append(new PUSH(_cp, 6));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_51 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_58 = il.append(new PUSH(_cp, "Result (should be 19) = "));
		il.append(_factory.createInvoke("test02", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_63 = il.append(_factory.createLoad(Type.INT, 4));
		il.append(_factory.createInvoke("test02", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_68 = il.append(_factory.createInvoke("test02", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_71 = il.append(_factory.createReturn(Type.VOID));
		ifeq_11.setTarget(ih_24);
		goto_21.setTarget(ih_33);
		if_icmpge_36.setTarget(ih_46);
		goto_43.setTarget(ih_51);
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);
	}

	public static void test03() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_2 = il.append(new PUSH(_cp, 2));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_4 = il.append(new PUSH(_cp, 3));
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_6 = il.append(new PUSH(_cp, 4));
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_9;
		BranchInstruction goto_9 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_9 = il.append(goto_9);
		InstructionHandle ih_12 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_14 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(_factory.createStore(Type.INT, 4));

		// InstructionHandle ih_17 = il.append(new IINC(1, 1));
		InstructionHandle ih_17 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_20 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_25 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(new PUSH(_cp, 10));
		BranchInstruction if_icmplt_28 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_12);
		il.append(if_icmplt_28);
		InstructionHandle ih_31;
		BranchInstruction goto_31 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_31 = il.append(goto_31);
		InstructionHandle ih_34 = il.append(_factory.createLoad(Type.INT, 2));
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));

		// InstructionHandle ih_39 = il.append(new IINC(1, -2));
		InstructionHandle ih_39 = il.append(new PUSH(_cp, -2));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_42 = il.append(_factory.createLoad(Type.INT, 1));
		BranchInstruction ifgt_43 = _factory.createBranchInstruction(Constants.IFGT, ih_34);
		il.append(ifgt_43);
		InstructionHandle ih_46 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_56 = il.append(new PUSH(_cp, "Result (should be 81) = "));
		il.append(_factory.createInvoke("test03", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_61 = il.append(_factory.createLoad(Type.INT, 4));
		il.append(_factory.createInvoke("test03", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_66 = il.append(_factory.createInvoke("test03", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_69 = il.append(_factory.createReturn(Type.VOID));
		goto_9.setTarget(ih_25);
		goto_31.setTarget(ih_42);
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);
	}


	public static void test04() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, "Result should be:"));
		il.append(_factory.createInvoke("test04", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_5 = il.append(_factory.createInvoke("test04", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_8 = il.append(new PUSH(_cp, "1 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97"));
		il.append(_factory.createInvoke("test04", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_13 = il.append(_factory.createInvoke("test04", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_16 = il.append(new PUSH(_cp, 100));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.PUTSTATIC));
		InstructionHandle ih_21 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_23;
		BranchInstruction goto_23 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_23 = il.append(goto_23);
		InstructionHandle ih_26 = il.append(_factory.createFieldAccess("test04", "num", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(new PUSH(_cp, 0));
		il.append(InstructionConstants.IASTORE);

		//InstructionHandle ih_32 = il.append(new IINC(1, 1));
		InstructionHandle ih_32 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_35 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.GETSTATIC));
		BranchInstruction if_icmplt_39 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_26);
		il.append(if_icmplt_39);
		InstructionHandle ih_42 = il.append(new PUSH(_cp, 2));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_44;
		BranchInstruction goto_44 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_44 = il.append(goto_44);
		InstructionHandle ih_47 = il.append(_factory.createLoad(Type.INT, 2));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_51;
		BranchInstruction goto_51 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_51 = il.append(goto_51);
		InstructionHandle ih_54 = il.append(_factory.createFieldAccess("test04", "num", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createFieldAccess("test04", "num", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IALOAD);
		il.append(new PUSH(_cp, 1));
		il.append(InstructionConstants.IADD);
		il.append(InstructionConstants.IASTORE);
		InstructionHandle ih_66 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_70 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.GETSTATIC));
		BranchInstruction if_icmplt_74 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_54);
		il.append(if_icmplt_74);

		//InstructionHandle ih_77 = il.append(new IINC(2, 1));
		InstructionHandle ih_77 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));

		InstructionHandle ih_80;
		BranchInstruction goto_80 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_80 = il.append(goto_80);

		//InstructionHandle ih_83 = il.append(new IINC(2, 1));
		InstructionHandle ih_83 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));

		InstructionHandle ih_86 = il.append(_factory.createLoad(Type.INT, 2));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.GETSTATIC));
		BranchInstruction if_icmpge_90 = _factory.createBranchInstruction(Constants.IF_ICMPGE, null);
		il.append(if_icmpge_90);
		il.append(_factory.createFieldAccess("test04", "num", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IALOAD);
		BranchInstruction ifgt_98 = _factory.createBranchInstruction(Constants.IFGT, ih_83);
		il.append(ifgt_98);
		InstructionHandle ih_101 = il.append(_factory.createLoad(Type.INT, 2));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.GETSTATIC));
		BranchInstruction if_icmplt_105 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_47);
		il.append(if_icmplt_105);
		InstructionHandle ih_108 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_110;
		BranchInstruction goto_110 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_110 = il.append(goto_110);
		InstructionHandle ih_113 = il.append(_factory.createFieldAccess("test04", "num", new ArrayType(Type.INT, 1), Constants.GETSTATIC));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IALOAD);
		BranchInstruction ifne_118 = _factory.createBranchInstruction(Constants.IFNE, null);
		il.append(ifne_118);
		InstructionHandle ih_121 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createInvoke("test04", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_125 = il.append(new PUSH(_cp, " "));
		il.append(_factory.createInvoke("test04", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));

		// InstructionHandle ih_130 = il.append(new IINC(1, 1));
		InstructionHandle ih_130 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_133 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createFieldAccess("test04", "maxNum", Type.INT, Constants.GETSTATIC));
		BranchInstruction if_icmplt_137 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_113);
		il.append(if_icmplt_137);
		InstructionHandle ih_140 = il.append(_factory.createInvoke("test04", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_143 = il.append(_factory.createReturn(Type.VOID));
		goto_23.setTarget(ih_35);
		goto_44.setTarget(ih_101);
		goto_51.setTarget(ih_70);
		goto_80.setTarget(ih_86);
		if_icmpge_90.setTarget(ih_101);
		goto_110.setTarget(ih_133);
		ifne_118.setTarget(ih_130);
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);

	}


	public static void test05() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, 10));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_3 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_5 = il.append(new PUSH(_cp, "Sum of odd numbers should be: 1 4 9 16 25 36 49 64 81"));
		il.append(_factory.createInvoke("test05", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_10 = il.append(_factory.createInvoke("test05", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_13;
		BranchInstruction goto_13 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_13 = il.append(goto_13);
		InstructionHandle ih_16 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_18 = il.append(new PUSH(_cp, 0));
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_21;
		BranchInstruction goto_21 = _factory.createBranchInstruction(Constants.GOTO, null);
		ih_21 = il.append(goto_21);
		InstructionHandle ih_24 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(new PUSH(_cp, 2));
		il.append(InstructionConstants.IDIV);
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_29 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(new PUSH(_cp, 2));
		il.append(_factory.createLoad(Type.INT, 5));
		il.append(InstructionConstants.IMUL);
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_37 = il.append(_factory.createLoad(Type.INT, 5));
		BranchInstruction ifeq_39 = _factory.createBranchInstruction(Constants.IFEQ, null);
		il.append(ifeq_39);
		InstructionHandle ih_42 = il.append(_factory.createLoad(Type.INT, 4));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 4));

		// InstructionHandle ih_48 = il.append(new IINC(3, 1));
		InstructionHandle ih_48 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 3));

		InstructionHandle ih_51 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(new PUSH(_cp, 2));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IMUL);
		BranchInstruction if_icmplt_55 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_24);
		il.append(if_icmplt_55);
		InstructionHandle ih_58 = il.append(new PUSH(_cp, "Sum of odd numbers from 1 to "));
		il.append(_factory.createInvoke("test05", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_63 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(new PUSH(_cp, 1));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_68 = il.append(_factory.createLoad(Type.INT, 5));
		il.append(_factory.createInvoke("test05", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_73 = il.append(new PUSH(_cp, " is "));
		il.append(_factory.createInvoke("test05", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_78 = il.append(_factory.createLoad(Type.INT, 4));
		il.append(_factory.createInvoke("test05", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_83 = il.append(_factory.createInvoke("test05", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));

		// InstructionHandle ih_86 = il.append(new IINC(1, 1));
		InstructionHandle ih_86 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_89 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		BranchInstruction if_icmplt_91 = _factory.createBranchInstruction(Constants.IF_ICMPLT, ih_16);
		il.append(if_icmplt_91);
		InstructionHandle ih_94 = il.append(_factory.createReturn(Type.VOID));
		goto_13.setTarget(ih_89);
		goto_21.setTarget(ih_51);
		ifeq_39.setTarget(ih_48);
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);

	}


	public static void test06() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		/*
		InstructionHandle ih_0 = il.append(new PUSH(_cp, 0));
		il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 7));
		InstructionHandle ih_4 = il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 6));
		InstructionHandle ih_7 = il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_10 = il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_13 = il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_15 = il.append(InstructionConstants.DUP);
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_17 = il.append(_factory.createStore(Type.INT, 1));
		 */

		InstructionHandle ih_18 = il.append(_factory.createLoad(Type.INT, 7));
		BranchInstruction ifeq_20 = _factory.createBranchInstruction(Constants.IFEQ, null);
		il.append(ifeq_20);
		InstructionHandle ih_23 = il.append(new PUSH(_cp, 4));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_25 = il.append(new PUSH(_cp, 5));
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 3));
		BranchInstruction goto_30 = _factory.createBranchInstruction(Constants.GOTO, null);
		il.append(goto_30);
		InstructionHandle ih_33 = il.append(new PUSH(_cp, 6));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_38 = il.append(new PUSH(_cp, 3));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_42 = il.append(_factory.createLoad(Type.INT, 7));
		il.append(new PUSH(_cp, 2));
		BranchInstruction if_icmpge_45 = _factory.createBranchInstruction(Constants.IF_ICMPGE, null);
		il.append(if_icmpge_45);
		InstructionHandle ih_48 = il.append(new PUSH(_cp, 5));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));
		BranchInstruction goto_52 = _factory.createBranchInstruction(Constants.GOTO, null);
		il.append(goto_52);
		InstructionHandle ih_55 = il.append(new PUSH(_cp, 6));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 4));
		InstructionHandle ih_61 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(new PUSH(_cp, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 6));
		InstructionHandle ih_66 = il.append(_factory.createLoad(Type.INT, 2));
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_70 = il.append(_factory.createLoad(Type.INT, 5));
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_76 = il.append(_factory.createLoad(Type.INT, 6));
		il.append(new PUSH(_cp, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_81 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(new PUSH(_cp, 3));
		il.append(InstructionConstants.ISUB);
		il.append(_factory.createStore(Type.INT, 5));
		InstructionHandle ih_86 = il.append(_factory.createLoad(Type.INT, 1));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 3));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 4));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 5));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createLoad(Type.INT, 6));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 7));
		InstructionHandle ih_102 = il.append(_factory.createLoad(Type.INT, 7));
		il.append(_factory.createInvoke("test06", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_107 = il.append(_factory.createReturn(Type.VOID));
		ifeq_20.setTarget(ih_33);
		goto_30.setTarget(ih_42);
		if_icmpge_45.setTarget(ih_55);
		goto_52.setTarget(ih_61);
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);


	}


	public static void test07() {
		InstructionFactory _factory;
		ConstantPoolGen    _cp;
		ClassGen           _cg;

		_cg = new ClassGen("myTest", "java.lang.Object", "test01.java", ACC_PUBLIC | ACC_SUPER, new String[] {  });

		_cp = _cg.getConstantPool();
		_factory = new InstructionFactory(_cg, _cp);

		InstructionList il = new InstructionList();
		MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.VOID, new Type[] { new ArrayType(Type.STRING, 1) }, new String[] { "arg0" }, "main", "test01", il, _cp);

		InstructionHandle ih_0 = il.append(new PUSH(_cp, 1));
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_2 = il.append(new PUSH(_cp, 2));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_4 = il.append(new PUSH(_cp, 3));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_8 = il.append(new PUSH(_cp, 5));
		il.append(_factory.createLoad(Type.INT, 2));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));
		InstructionHandle ih_12 = il.append(new PUSH(_cp, 6));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_15 = il.append(new PUSH(_cp, 7));
		il.append(_factory.createStore(Type.INT, 3));

		// InstructionHandle ih_18 = il.append(new IINC(1, 8));
		InstructionHandle ih_18 = il.append(new PUSH(_cp, 8));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 1));

		InstructionHandle ih_21 = il.append(new PUSH(_cp, 9));
		il.append(_factory.createStore(Type.INT, 2));
		InstructionHandle ih_24 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(_factory.createLoad(Type.INT, 1));
		il.append(InstructionConstants.IADD);
		il.append(_factory.createStore(Type.INT, 3));
		InstructionHandle ih_28 = il.append(new PUSH(_cp, "Result (should be 22) = "));
		il.append(_factory.createInvoke("test07", "printStr", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESTATIC));
		InstructionHandle ih_33 = il.append(_factory.createLoad(Type.INT, 3));
		il.append(_factory.createInvoke("test07", "printInt", Type.VOID, new Type[] { Type.INT }, Constants.INVOKESTATIC));
		InstructionHandle ih_37 = il.append(_factory.createInvoke("test07", "printLn", Type.VOID, Type.NO_ARGS, Constants.INVOKESTATIC));
		InstructionHandle ih_40 = il.append(_factory.createReturn(Type.VOID));
		method.setMaxStack();
		method.setMaxLocals();
		_cg.addMethod(method.getMethod());

		ClassFile mycc = new ClassFile("myTest");
		ClassFile.optimizeCode(il);

		System.out.println();
		System.out.println("<<---- Optimized code ---->>");

		// print out instructions
		InstructionHandle ih = il.getStart();

		while (ih != null) 
		{
			System.out.println(ih.toString(false));
			ih = ih.getNext();
		}
	}

	public static boolean testCFG1() {
		try {

			String testName = "opt01";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test01();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;

			testName = "opt02";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test02();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;

			testName = "opt03";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test03();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testCFG2() {
		try {

			String testName = "opt04";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test04();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;

			testName = "opt05";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test05();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;

			testName = "opt06";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test06();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;

			testName = "opt07";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test07();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",0) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testLive1() {
		try {

			String testName = "opt01";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test01();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			return true;
		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testLive2() {
		try {

			String testName = "opt02";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test02();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testLive3() {
		try {

			String testName = "opt03";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test03();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;

			testName = "opt05";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test05();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testLive4() {
		try {

			String testName = "opt04";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test04();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			testName = "opt06";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test06();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			testName = "opt07";
			flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test07();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",1) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testInit1() {
		try {

			String testName = "opt06";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test06();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",2) == false)
				return false;
			return true;

		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

	public static boolean testDead1() {
		try {

			String testName = "opt07";
			PrintStream flog = new PrintStream(new FileOutputStream(new File(testName+".log"), false));
			System.setOut(new PrintStream(flog));
			test07();
			flog.flush();
			flog.close();
			if (compareParseOutput(testName+".log",testName +".out",3) == false)
				return false;
			return true;
		} catch (Exception e) {
			System.err.println("Caught exception: " + e);
			e.printStackTrace();
			System.err.println();
		}
		return false;
	}

}
