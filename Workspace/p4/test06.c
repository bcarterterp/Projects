
// test uninitialized variable detection (program will not run)

void main ( )
{
		int a,b,c,d,e,f,x;
		
		if ( x != 0 )
		{
			a = 4;
			c = 5 - d;
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
			d = 6 + b;
		}

		f = a + 2;
		a = b + c;
		c = e + d;
		b = f + 1;
		e = a - 3;

		x = a + b + c + d + e + f;
		printInt(x);
}
