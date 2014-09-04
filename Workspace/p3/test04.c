// test boolean expressions 
int x,y;
void main ( )
{
   // int a,b;
   x = 1;
   y = 2;

   printStr("Results should be : 1 3 -1 ");
   printLn();

   if (x < 3) { 
      printInt(x); 
      printStr(" ");
   }
   else {
      printStr("Error 1"); 
   }
   if ((x == 1) && (y == 2)) {   // True && True
      printInt(x+y); 
      printStr(" ");
   }
   if ((x == 2) || (y > 1)) {    // False || TRUE
      printInt(x-y); 
      printStr(" ");
   }
  printLn(); 
}
