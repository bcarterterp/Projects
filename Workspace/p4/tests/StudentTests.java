package tests;

import junit.framework.TestCase;

import org.apache.bcel.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;
import org.apache.bcel.Repository;
import org.apache.bcel.util.*;


public class StudentTests extends TestCase {

	public void test1() {
		assertTrue("Failed to build mycc scanner/parser", TestMycc.buildParser());
		TestMycc.tryTest("test01", 1);	
	}
	
	public void test2() {
		TestMycc.tryTest("test02", 1);	
	}
	
	public void test3() {
		TestMycc.tryTest("test03", 1);	
	}
	
	public void test4() {
		TestMycc.tryTest("test04", 1);	
	}
	
	public void test5() {
		TestMycc.tryTest("test05", 1);	
	}
	
	public void test6() {
		TestMycc.tryTest("test06", 1);	
	}
	
	public void test7() {
		TestMycc.tryTest("test07", 1);	
	}
	
}
