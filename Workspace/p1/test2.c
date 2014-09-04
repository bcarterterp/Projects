
//------ test legal statements -------

void
grammarTest() {
  int x,y,z;

  // test control flow
  if (x == 1) 
    { z = 1; }
  else
    { z = 2; }

  while (x < 6) 
    { x = x + 1; }

  // test relational & logical expressions

  if ( (x == y) && (1 > z) )
    { z = 5; }

  if ( !(x != z) )
    { y = 2; }
}

