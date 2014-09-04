package tests;

import junit.framework.TestCase;

public class StudentTests extends TestCase {

    protected void setUp() {
		assertTrue("Failed to build mycc scanner/parser", TestMycc.buildParser());
    }

	public void testMyTest() {
		// put test here
	}
}
