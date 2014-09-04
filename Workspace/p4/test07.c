
// test dead code elimination

void main ( )
{
   int x,y,z;

   x = 1;              // dead
   y = 2;
   z = 3 + y;          // dead
   x = 5 + y;
   y = 6;              // dead
   z = 7;
   x = 8 + x;
   y = 9;              // dead
   z = z + x;

   printStr("Result (should be 22) = ");
   printInt(z);
   printLn();
}

