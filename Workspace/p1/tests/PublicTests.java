package tests;

import junit.framework.TestCase;

public class PublicTests extends TestCase {
	
	public void testDeclarations() {
		assertTrue("Failed to build mycc scanner/parser", TestUtils.buildParser());
		assertTrue(TestUtils.tryTest("test1"));
	}

	public void testControlStmts() {
		assertTrue(TestUtils.tryTest("test2"));
	}

	public void testFunctionCalls() {
		assertTrue(TestUtils.tryTest("test3"));
	}
	
	public void testArrays() {
		assertTrue(TestUtils.tryTest("test4"));
	}
	
	public void testParserErrors() {
		assertTrue(TestUtils.tryTest("test5"));
	}
}
