
//------ test functions -------

int f1()       
  { return 1; }

void f2(int y) 
  { y = 1; }

void
grammarTest() {
  int x,y,z;

  // test function calls
  x = f1();
  f2(x);

  printInt(x);
  printStr("Done!);
  printLn();

}

