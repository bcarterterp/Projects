//--------- test nested scoping type errors ---------
int a, b;
void c ( int d )
{
    int e, f;
    if (1 == e) {
        int g,h;
        a = b + d + e + f + g + h + i;  // undeclared variable i
    } 
    else {
        int i,j,a,b;  
        a = b - d - e - f - i - j - g;    // undeclared variable g
    }
    a = b + d - e * f - e + h / b + a * a;   // undeclared variable h
}

int k ( )
{
    int l,m;
    if (1 == 2) {
        int n,o,a;
        a = b * l * m * i * n * o;  // undeclared variable i
    } 
    else {
        int p,q,b,k,l,m; 
	int a[4];
        a = b / l / m / p / q;    // misuse of a
    }
    return 1;
}
