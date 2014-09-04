// test functions and parameter passing

int factorial ( int f )
{
   if ((f == 0) || (f == 1)) {
      return 1;
   }
   f = f * factorial(f - 1);
   return f;
}
 
void main ( ) 
{
   int z;

   printStr("Results should be : 6 720");
   printLn();

   z = factorial(3);
   printInt(z);
   printStr(" ");

   z = factorial(6);
   printInt(z);
   printLn();
}

