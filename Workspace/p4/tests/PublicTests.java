package tests;

import junit.framework.TestCase;

public class PublicTests extends TestCase {


	public void testCFG1() {
		assertTrue(TestMycc.testCFG1());	
	}
	public void testCFG2() {
		assertTrue(TestMycc.testCFG2());	
	}
	public void testLive1() {
		assertTrue(TestMycc.testLive1());	
	}
	public void testLive2() {
		assertTrue(TestMycc.testLive2());	
	}
	public void testLive3() {
		assertTrue(TestMycc.testLive3());	
	}
	public void testLive4() {
		assertTrue(TestMycc.testLive4());	
	}
	public void testInit1() {
		assertTrue(TestMycc.testInit1());	
	}
	public void testDead1() {
		assertTrue(TestMycc.testDead1());	
	}
}
