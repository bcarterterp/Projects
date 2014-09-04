package tests;

import junit.framework.TestCase;

public class PublicTests extends TestCase {

    protected void setUp() {
		assertTrue("Failed to build mycc scanner/parser", TestMycc.buildParser());
    }

	public void testSymtab1() {
		assertTrue(TestMycc.testSymtab1());
	}
	
	public void testSymtab2() {
		assertTrue(TestMycc.testSymtab2());
	}
	
	public void testAST1() {
		assertTrue(TestMycc.testAST1());
	}
	
	public void testAST2() {
		assertTrue(TestMycc.testAST2());
	}
	
	public void testTypeCorrect() {
		assertTrue(TestMycc.testTypeCorrect());
	}
	
	public void testTypeError1() {
		assertTrue(TestMycc.testTypeError1());
	}
	
	public void testTypeError2() {
		assertTrue(TestMycc.testTypeError2());
	}
	
	public void testTypeError3() {
		assertTrue(TestMycc.testTypeError3());
	}

}
