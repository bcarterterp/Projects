//--------- test forward declaration type errors ---------
int x;
int f1 ( );
int f2 ( int p1 );
void f3 ( int p2 );
void f4 ( );

int f1 ( int z )             // forward decl mismatch f1
{
    int x1; 
    x1 = 1; 
    return x1;
} 

void f2 ( int z )     //  forward decl mismatch f2
{ 
    int x2; 
    x2 = 2; 
    return x2;		// return mismatch
} 

int f3 ( int p4 )       //  forward decl mismatch f3
{ 
    int x3; 
    x3 = 3; 
    return x3;
} 

int f4 ( )      // forward decl mismatch f4
{ 
    int x4; 
    x4 = 4; 
    return;  	// return mismatch
} 
