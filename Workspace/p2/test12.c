//------ test relational & logical expression errors  -------

void
main() {
  int x,y,z;

  // test control flow
  if (x - 1)        // requires boolean
    { z = 1; }
  else
    { z = 2; }

  while (x < 6) 
    { x = x + 1; }

  // test relational & logical expressions

  if ( (x == y) && (1 * z) )  // requires boolean
    { z = 5; }

  if ( (x == y) == 1 )  // requires boolean
    { z = 5; }

  if ( 1 != (x < 4) )  // requires integer
    { z = 5; }

  if ( !(x != z) ) 
    { y = y > x; }    // requires integer
}

