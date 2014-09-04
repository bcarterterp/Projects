// test basic variable assignments
int x,y;
void main ( )
{
   int a,b;
   x = 1;
   a = 2;
   y = a;
   b = x;
   printStr("Results should be: 1 2 2 1"); 
   printLn();
   printInt(x); 
   printStr(" ");
   printInt(y); 
   printStr(" ");
   printInt(a); 
   printStr(" ");
   printInt(b); 
   printLn();
}
