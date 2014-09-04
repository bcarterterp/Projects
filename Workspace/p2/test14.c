//------ test array type errors  -------

int a[4];

void
grammarTest() {
  int b,c,d[2];

  // test arrays
  c = b[3];		// misuse of b
  b[1] = 2;		// misuse of b
  b = 2*a[1 < b];	// illegal subscript
  c = a;		// misuse of a
  b = d(1);		// misuse of d
}

