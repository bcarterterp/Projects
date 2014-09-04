// peephole optimizations - constant relations, simplify booleans, IF stmts
int x,y;
void main ( )
{
   int a,b;
   x = 1;
   y = 2;
   a = 3;
   b = 4;

   printStr("Results should be : a c e ");
   printLn();

   if (3 == 3) { 
       printStr("a ");
   }
   
   if (1 > 2)  { 
       printStr("b ");
   } else {
       printStr("c ");
   }
   
   if ((1 > 2) && (a < b)) {
       printStr("d ");
   }
   
   if ((1 > 2) || (a < b)) {
       printStr("e ");
   }
   printLn();
}

