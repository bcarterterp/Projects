
// test data flow with straight-line code

void main ( )
{
 		int a,b,c,d,e,f,g;

		a = 1;
		b = 1;
		c = 1;
		d = 1;
		e = 1;
		f = 1;
		g = 1;
		a = a + b;
		a = a + c;
		c = e + g;
		b = f + 1;
		a = a - 3;
		a = a + b + c + d + e + f + g;

		printStr("Result (should be 8) = ");
		printInt(a);
		printLn();
}
