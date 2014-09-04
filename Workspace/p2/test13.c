//------ test functions & return type errors -------

int y;

int f1(int x)       
  { x = 1; return ; }		// return mismatch

void f2(int y) 
  { y = 2; return 5; }		// return mismatch

void f3() 
  { y = 3; return ; }

void f4() {
  int x,y,z;

  y = 0;
  f1(x);
  x = f1(y);
  f2();				// parameter mismatch
  x = f3();			// requires integer
}

void f4()  			// redeclaration of f4
  { y = 3; return ; }

