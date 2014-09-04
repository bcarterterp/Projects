// peephole optimizations - constant arithmetic & algebraic simplification

void main ( )
{
   int x,z;
   z = 0;
   x = 2;

   printStr("Results should be : 10");
   printLn();

   z = z+(1+2);
   z = z+(3*4-5);
   printInt(z);
   printLn();

   printStr("Results should be : 14");
   printLn();

   z = z+(x+0);
   z = z+(0+1*x);
   printInt(z);
   printLn();
}
