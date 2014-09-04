//--------- test arithmetic expressions ---------
int a, b;
void c ( int d )
{
    int e, f;
    if (1 == 2) {
        int g,h;
        a = b + d + e + f + g + h;
    } 
    else {
        int i,j,b;  
        a = b - d - e - f - i - j;
    }
    a = b + d - e * f - e + d / b + a * a - b * d * e;
    a = a+a+a-a-a*a*a*a/a/a-a+a*a/a*a;
}

int k ( )
{
    int l,m;
    if (1 == 2) {
        int n,o;
        a = b * l * m * n * o;
    } 
    else {
        int p,q;  
        a = b / l / m / p / q;
    }
    return (b + a * l - (m / b * m * b + a) + b * m - a);
}
