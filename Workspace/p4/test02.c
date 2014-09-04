
// test control flow with IF statements

void main ( )
{
		int a,b,c,x;

		x = 0;
		a = 1;
		b = 2;
		c = 3;

		if ( x != 0 )
		{
			a = a + 1;
			b = 5 - c;
		}
		else
		{
			a = 6 + b;
			b = 3 - c;
		}

		if ( x < 2 )
		{
			b = 5 + c;
		}
		else
		{
			a = 6 + b;
		}

		x = a+b+c;

		printStr("Result (should be 19) = ");
		printInt( x );
		printLn();
}
