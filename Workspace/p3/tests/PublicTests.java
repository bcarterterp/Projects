package tests;

import junit.framework.TestCase;

public class PublicTests extends TestCase {

    protected void setUp() {
		assertTrue("Failed to build mycc scanner/parser", TestMycc.buildParser());
    }
    
	public void testCodegen1() {
		assertTrue(TestMycc.testCodegen1());
	}
	
	public void testCodegen2() {
		assertTrue(TestMycc.testCodegen2());
	}
	
	public void testCodegen3() {
		assertTrue(TestMycc.testCodegen3());
	}
	
	public void testCodegen4() {
		assertTrue(TestMycc.testCodegen4());
	}
	
	public void testCodegen5() {
		assertTrue(TestMycc.testCodegen5());
	}
	
	public void testOpt1() {
		assertTrue(TestMycc.testOpt1());
	}
	
	public void testOpt2() {
		assertTrue(TestMycc.testOpt2());
	}
	
}
