package tests;

import junit.framework.TestCase;

public class StudentTests extends TestCase {
	
    protected void setUp() {
		assertTrue("Failed to build mycc scanner/parser", TestMycc.buildParser());
    }

	public void testToy1() {
		TestMycc.tryTest("toy1", 0);
	}

	public void testToy2() {
		TestMycc.tryTest("toy2", 0);
	}

	public void testMyTest() {
		// put test here
	}
	
}
