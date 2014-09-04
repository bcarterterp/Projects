//------ test functions & return statements -------
int y;
int f1()       
  { return 1; }

void f2(int y) 
  { y = 2; }

void f3() 
  { y = 3; return; }

void
main() {
  int x,y,z;

  // test function calls
  y = 0;
  f1();
  x = 2 + f1();
  f2(x);
  f3();

  printInt(x);
  printStr(" ");
  printInt(y);
  printStr(" Done!");
  printLn();
}

