package tests;

import junit.framework.TestCase;

public class StudentTests extends TestCase {
	
	public void testToy1() {
		assertTrue("Failed to build mycc scanner/parser", TestUtils.buildParser());
		assertTrue(TestUtils.tryTest("toy1"));
		}

	public void testToy2() {
		assertTrue(TestUtils.tryTest("toy2"));
	}
}

