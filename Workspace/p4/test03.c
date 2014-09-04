
// test control flow with WHILE statements

void main ( )
{
   int i,j,k,m;

   i = 1;
   j = 2; 
   k = 3; 
   m = 4;

   while ( i < 10 )
   {
      k = i;
      m = k;
      i = i + 1;
      j = k + m; 
   }

   while ( i > 0 )
   {
      j = j + m; 
      i = i - 2;
   }

   m = i + j + k + m;

   printStr("Result (should be 81) = ");
   printInt(m);
   printLn();
}

