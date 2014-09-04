// test control flow with IF and WHILE statements

void main ( )
{
   int i,n,x,y,z;

   n = 10;
   i = 1;

   printStr("Sum of odd numbers should be: 1 4 9 16 25 36 49 64 81");
   printLn();

   while ( i < n )
   {
      x = 1; 
      y = 0; 
      while ( x < (2*i) )
      {
          z = (x / 2);
          z = x - (2 * z);
          if ( z != 0 )
          {
              y = y + x;
          }
          x = x + 1;
      }
   
      printStr("Sum of odd numbers from 1 to ");
      z = x-1;
      printInt(z);
      printStr(" is ");
      printInt(y);
      printLn();
      i = i + 1;
   }
}


