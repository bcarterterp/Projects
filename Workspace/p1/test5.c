
//-------- test parser errors --------

int x, y  z;          // illegal variable declaration
int foo () 
{
    int i, j , k;
    i = j * ( 5 + ) ; // illegal expression
    i = 4;
    i - (1 * 2);      // illegal statement
    j = 5;
}

