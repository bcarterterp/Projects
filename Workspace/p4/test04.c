
//  Sieve of Erastothenes

int num[100];
int maxNum;

void main()
{
     int i, j;

     printStr("Result should be:");
     printLn();
     printStr("1 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97"); 
     printLn();

     maxNum = 100;

     i = 1;
     while ( i < maxNum )
     {
         num[i] = 0;
         i = i + 1;
     }
     
     j = 2;
     while ( j < maxNum )
     {
         i = j + j;
         while ( i < maxNum )
         {
             num[i] = num[i] + 1;
             i = i + j;
         }

         j = j + 1;
         while (( j < maxNum ) && ( num[j] > 0 ))
         {
             j = j + 1;
         }
     }

     i = 1;
     while ( i < maxNum )
     {
         if ( num[i] == 0 )
         {
             printInt(i);
             printStr(" ");
         }
         i = i + 1;
     }
     printLn();
     
     return;
}


