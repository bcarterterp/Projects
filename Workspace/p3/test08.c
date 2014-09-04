//  Test function calls

int stack[100];
int top;

// Stack API

void initStack()
{
    top = -1;
}

int pop()
{
    int x;

    if (top == -1) 
    {
        printStr("ERROR: pop() empty stack");
        printLn();
        return 0;
    }

    x = stack[top];
    top = top - 1;
    return x;
}

void push( int x )
{
    top = top + 1;
    if (top == 100) 
    {
        printStr("ERROR: push() full stack");
        printLn();
        return;
    }

    stack[top] = x;
    return;
}

void iadd()
{
    int x, y;
    y = pop();
    x = pop();
    push( x + y );
}

void isub()
{
    int x, y;
    y = pop();
    x = pop();
    push( x - y );
}

// Main routine

void main()
{
     initStack();

     printStr("Result should be: -2 4"); 
     printLn();

     push(5);
     push(4);
     push(3);
     iadd();
     isub();
     printInt(pop());
     printLn();

     push(105);
     push(104);
     push(103);
     push(102);
     iadd();
     isub();
     iadd();
     printInt(pop());
     printLn();

     return;
}

