// test control flow statements
int x,y;
void main ( )
{
   int a,b;
   x = 1;
   y = 2;
   a = 3;
   b = 4;

   printStr("Results should be : 2 7 1 2 3 4 ");
   printLn();

   if (x == 3) { 
       printStr("Error 1"); 
   }
   if (y != 2)  {
       printStr("Error 2"); 
   }
   else { 
       printInt(y); 
       printStr(" ");
   }
   if (1 == 1) {
       printInt( a + b ); 
       printStr(" ");
   }
   else { 
       printStr("Error 3"); 
       printStr(" ");
   }
   while (x != 5) {
       printInt(x);
       printStr(" ");
       x = x + 1;
   } 
   printLn(); 
}

