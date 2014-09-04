// test arrays
int a[5];

int foo ( int param )
{
   int b[10];
   int i;
   i = 1;
   while (i < 10) {
      b[i] = i;
      i = i+1;
   }
   b[0] = b[param-1] + b[param+1];
   return b[0]; 
}
 
void main ( ) 
{
   int c;
   printStr("Result should be: 8 10");
   printLn();

   c = a[0];
   a[1] = 4;
   a[2] = 5;
   a[0] = foo( a[1] );
   printInt(a[0]);
   printStr(" ");
   a[0] = foo( a[2] );
   printInt(a[0]);
   printLn();
}

