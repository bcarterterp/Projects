// test arithmetic operators
int x,y;
void main ( )
{
   int a,b;
   x = 1;
   a = 2+x;
   y = -1+a*x;
   b = (3*y)-(x-a);
   printStr("Results should be: 1 3 2 8"); 
   printLn();

   printInt(x); 
   printStr(" ");
   printInt(a); 
   printStr(" ");
   printInt(y); 
   printStr(" ");
   printInt(b); 
   printLn();
}
