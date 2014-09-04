
package mycc;

import java.util.*; 

//---------------------------------------------------------------------
// Stores type and content of C-- expressions.  
/*
   nodeType:

	if Const.INTEGER
		expType = sym.INT or sym.BOOL
		val = value

	if Const.STRING
		expType = sym.VOID
		str = literal string

	if Const.ARRAY
		expType = sym.INT
		op1 = array subscript

	if Const.IDENTIFIER
		expType = sym.INT or sym.VOID
		ident = symTabEntry for identifier

	if Const.CALL
		expType = sym.INT or sym.VOID
		op1 = name of function
		op2 = function argument (may be null)

	if Const.INSTRUCTION
		expType = sym.INT, sym.VOID, or sym.BOOL
		opcode = code for instruction
		op1 = first operand
		op2 = 2nd operand
*/


public class expNode {

	int nodeType;      	// type of the node: 
                                //    Const.INSTRUCTION, Const.IDENTIFIER, 
				//    Const.INTEGER, Const.STRING, 
				//    Const.ARRAY, Const.CALL

	int expType;		// type of the variable: 
				//	sym.INT, sym.VOID, sym.BOOL
 
	int val;               // value of integer constant
	String str;            // content of literal string
	symTabEntry  ident;	// pointer to the symbol table entry

	int opcode;		// opcode of the instruction
	expNode op1;		// operand 1
	expNode op2;		// operand 2

	//----------------------------
	//----------------------------
	// constructors 

	//----------------------------
	// type, opcode, expNode, expNode param = binary instruction

	public expNode(int type, int op, expNode s1, expNode s2)
	{
		nodeType = Const.INSTRUCTION;
		expType = type;
		opcode = op;
		op1 = s1;
		op2 = s2;
		if (op == Const.CALL) nodeType = Const.CALL;
	}

	//----------------------------
	// type, opcode, expNode param = unary instruction

	public expNode(int type, int op, expNode s1)
	{
		nodeType = Const.INSTRUCTION;
		expType = type;
		opcode = op;
		op1 = s1;
		op2 = null;
		if (op == Const.CALL) nodeType = Const.CALL;
	}

	//----------------------------
	// type, opcode param = instruction

	public expNode(int type, int op)
	{
		nodeType = Const.INSTRUCTION;
		expType = type;
		opcode = op;
		op1 = null;
		op2 = null;
	}

	//----------------------------
	// Integer param = should be integer symbol 

	expNode(Integer n) 
	{ 
		nodeType = Const.INTEGER;
		expType = sym.INT; 
		val = n.intValue(); 
	}

	//----------------------------
	// boolean param = should be constant boolean 

	expNode(boolean b) 
	{ 
		nodeType = Const.INTEGER;
		expType = sym.BOOL; 
		val = b ? 1 : 0;
	}

	//----------------------------
	// String param = should be string symbol 

	expNode(String s) 
	{ 
		nodeType = Const.STRING;
		expType = sym.STRING_LITERAL; 
		str = s; 
	}

	//----------------------------
	// type, symTabEntry param = should be IDENTIFIER

	expNode(int t, symTabEntry s) 
	{ 
		nodeType = Const.IDENTIFIER;
		expType = t; 
		ident = s;
	}

	//----------------------------
	// int, symTabEntry, expNode param = should be ARRAY

	expNode(int t, symTabEntry id, expNode e1)
	{
		if ((id == null) || !id.isArray())
			parser.msg("illegal values for ARRAY identifier ");

		nodeType = Const.ARRAY;
		expType = t;
		ident = id;
		op1 = e1;
	}

	//----------------------------
	//----------------------------
	// access functions to return information about symbol

	int type() { return expType; }

	int getVal() { return val; }

	String getStr() { return str; }

	boolean isINT() 
		{ return (expType == sym.INT) ; }

	boolean isBOOL() 
		{ return (expType == sym.BOOL) ; }

	boolean isSTR() 
		{ return (expType == sym.STRING_LITERAL) ; }

	public int nodeType() { return nodeType; }

	public symTabEntry getSymbol()
	{
		if ((nodeType == Const.IDENTIFIER) ||
			(nodeType == Const.ARRAY))
			return ident;

		parser.msg("expNode is not an Identifier ");
		return null;
	}

   public expNode getOp1() { return op1; }
   public expNode getOp2() { return op2; }
   public int getOpcode() { return opcode; }

   public boolean isSYMBOL()
   {
      if ((nodeType == Const.IDENTIFIER) ||
          (nodeType == Const.ARRAY))
         return true;

      return false;
   }

   public boolean isINSTR()
   {
      if (nodeType == Const.INSTRUCTION)
         return true;

      return false;
   }

   public boolean isConst()
   {
      if (nodeType == Const.INTEGER)
         return true;

      return false;
   }

   public void print()
   {
      switch (nodeType)
      {
         case Const.INTEGER:
            System.out.print(val);
            break;

         case Const.STRING:
            System.out.print("\"");
            System.out.print(str);
            System.out.print("\"");
            break;

         case Const.IDENTIFIER:
            System.out.print(ident.name());
            break;

         case Const.ARRAY:
            System.out.print(ident.name() + "[");
            op1.print();
            System.out.print("]");
            break;

         case Const.CALL:
            System.out.print("CALL ");
            op1.print();
            System.out.print("(");
            if (op2 != null)
               op2.print();
            System.out.println(")");
            break;

         case Const.INSTRUCTION:
            printInstr();
            break;

         default:
            System.out.println("ERROR: Unknown expNode type = "+nodeType);
            break;
      }
   }
		
   public void printInstr()
   {
      switch (opcode)
      {
         case sym.ASSIGN:
            op1.print();
            System.out.print(" = ");
            op2.print();
            System.out.println();
            break;

         case sym.RETURN:
            System.out.print(" RETURN ");
            if (op1 != null)
               op1.print();
            System.out.println();
            break;

         case sym.PLUS:
            System.out.print("(");
            op1.print();
            System.out.print(" PLUS ");
            op2.print();
            System.out.print(")");
            break;

         case sym.MUL:
            System.out.print("(");
            op1.print();
            System.out.print(" MUL ");
            op2.print();
            System.out.print(")");
            break;

         case sym.MINUS:
            System.out.print("(");
            if (op2 != null) {
                op1.print();
                System.out.print(" MINUS ");
                op2.print();
            }
            else {
                System.out.print(" MINUS ");
                op1.print();
            }
            System.out.print(")");
            break;

         case sym.DIV:
            System.out.print("(");
            op1.print();
            System.out.print(" DIV ");
            op2.print();
            System.out.print(")");
            break;

         case sym.GR:
            System.out.print("(");
            op1.print();
            System.out.print(" GR ");
            op2.print();
            System.out.print(")");
            break;

         case sym.LESS:
            System.out.print("(");
            op1.print();
            System.out.print(" LESS ");
            op2.print();
            System.out.print(")");
            break;

         case sym.AND_OP:
            System.out.print("(");
            op1.print();
            System.out.print(" AND_OP ");
            op2.print();
            System.out.print(")");
            break;

         case sym.OR_OP:
            System.out.print("(");
            op1.print();
            System.out.print(" OR_OP ");
            op2.print();
            System.out.print(")");
            break;

         case sym.NE_OP:
            System.out.print("(");
            op1.print();
            System.out.print(" NE_OP ");
            op2.print();
            System.out.print(")");
            break;

         case sym.EQ_OP:
            System.out.print("(");
            op1.print();
            System.out.print(" EQ_OP ");
            op2.print();
            System.out.print(")");
            break;

         case sym.NOT_OP:
            System.out.print("( NOT_OP ");
            op1.print();
            System.out.print(")");
            break;

         default:
            System.out.println("ERROR: Unknown expNode Instr code = "+opcode);
            break;
      }
   }

}

