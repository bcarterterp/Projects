//------ test declaration & expression errors  -------
int i;
int i1;

void
main() {

  // test declarations
  int x,y,z,i;
  int a,b,c;
  int y;      // redeclaration of y

  // test arithmetic expressions
  a = 1+2*b;
  c = b/(4 < 5);  // requires integer
  x = -1-a;
  z = -x+d-z;     // undeclared variable d
}
