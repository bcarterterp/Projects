
package mycc;

import java.util.*;

//---------------------------------------------------------------------
// Stores type and content of C-- abstract syntax trees.
/*
   treeType:

	if TREE_INSTR
		treeType = Const.TREE_INSTR
		expNode = instruction

	if TREE_IF
		treeType = Const.TREE_IF
		treeExp = condition
		node1 = if block statement
		node2 = else block statement

	if TREE_WHILE
		treeType = Const.TREE_WHILE
		treeExp = condition
		node1 = loop body 

	if TREE_BLK
		treeType = Const.TREE_BLK
		nlist = list of block statements 

	if TREE_PROC
		treeType = Const.TREE_PROC
		treeExp = name of function
		node1 = block statement body
*/

public class astNode
{
   int          treeType;       // type of the tree
   expNode      treeExp;        // useful for if, while, instructions 
   astNode      node1;          // useful for if and while 
   astNode      node2;          // useful for else 
   astNodeList  nlist;          // useful for block stmt 

   public astNode()
   {
	treeType = 0;
	treeExp = null;
	node1 = null;
	node2 = null;
   }

   public void genInstr(expNode in)
   {
        treeType = Const.TREE_INSTR;
	treeExp = in;
   }

   public void genIf(expNode e1, astNode n1)
   {
         treeType = Const.TREE_IF;
         treeExp = e1;
         node1 = n1;
   }

   public void genIfElse(expNode e1, astNode n1, astNode n2)
   {
         treeType = Const.TREE_IF;
         treeExp = e1;
         node1 = n1;
         node2 = n2;
   }

   public void genWhile(expNode e1, astNode n1)
   {
         treeType = Const.TREE_WHILE;
         treeExp = e1;
         node1 = n1;
   }

   public void genProc(expNode e1, astNode n1)
   {
         treeType = Const.TREE_PROC;
         treeExp = e1;
         node1 = n1;
   }

   public void genBlk(astNodeList l1)
   {
         treeType = Const.TREE_BLK;
         nlist = l1;
   }

   public boolean isTreeInstr() { return (treeType == Const.TREE_INSTR); }
   public boolean isTreeIf() { return (treeType == Const.TREE_IF); }
   public boolean isTreeWhile() { return (treeType == Const.TREE_WHILE); }
   public boolean isTreeBlk() { return (treeType == Const.TREE_BLK); }

   public expNode getInstr() { return treeExp; }
   public expNode getCondition() { return treeExp; }
   public astNode getNode1() { return node1; }
   public astNode getNode2() { return node2; }
   public astNodeList getList()  { return nlist; }

   public void print()
   {
	switch (treeType)
	{
	case Const.TREE_INSTR:
            System.out.print("    TREE_INSTR: ");
            treeExp.print();
            break;

	case Const.TREE_IF:
            System.out.print("  TREE_IF: ");
            System.out.print("CONDITION: ");
            treeExp.print();
            System.out.println();
            node1.print();
            if (node2 != null)
            {
               System.out.println("  ELSE: ");
               node2.print();
            }
            System.out.println("  END_IF ");
            break;

	case Const.TREE_WHILE:
            System.out.print("  TREE_WHILE: ");
            System.out.print("CONDITION: ");
            treeExp.print();
            System.out.println();
            node1.print();
            System.out.println("  END_WHILE ");
            break;

	case Const.TREE_PROC:
            System.out.print("TREE_PROC: ");
            treeExp.print();
            System.out.println();
            node1.print();
            System.out.println("END_PROC");
            System.out.println();
            break;

	case Const.TREE_BLK:
            System.out.println("{");
            nlist.print();
            System.out.println("}");
            break;

	default:
            System.out.println("ERROR: Unknown astNode type = "+treeType);
            break;
	}
   }
}

