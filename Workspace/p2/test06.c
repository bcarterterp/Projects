//--------- test forward declarations ---------

int x;
int f1 ( );
int f2 ( int p1 );
void f3 ( int p2 );
void f4 ( );

int f1 ( )             // match
{
    int x1; 
    x1 = 1; 
    return x1;
} 

int f2 ( int p3 )     // match
{ 
    int x2; 
    x2 = 2; 
    return x2;
} 

void f3 ( int p4 )     // match
{ 
    int x3; 
    x3 = 3; 
} 

void f4 ( )      // match
{ 
    int x4; 
    x4 = 4; 
} 

