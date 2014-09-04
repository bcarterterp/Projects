
package mycc;

import java.util.BitSet;
import java.util.TreeSet;
import org.apache.bcel.generic.InstructionHandle;

//---------------------------------------------------------------------
//Basic Block

public class bBlock
{
	int               num;     // number for basic block
	int               len;     // number of instructions in basic block

	InstructionHandle first;   // first instruction in basic block
	InstructionHandle last;    // last instruction in basic block

	bBlock []         pred;    // predecessors
	bBlock []         succ;    // successors

	BitSet          in;        // 
	BitSet         out;        // 
	BitSet         gen;        // 
	BitSet        kill;        //

	int               currPred;     // iterator: number of current predecessor
	int               currSucc;     // iterator: number of current successor

	public bBlock()
	{
		num = 0;
		len = 0;
		first = null;
		last = null;
		succ = new bBlock[10];
		pred = new bBlock[10];
		in = new BitSet();
		out = new BitSet();
		gen = new BitSet();
		kill = new BitSet();

		for (int i = 0; i < succ.length ; i++)
			succ[i] = null;
	}

	public bBlock(InstructionHandle f, InstructionHandle l)
	{
		num = 0;
		len = 0;
		first = f;
		last = l;
		succ = new bBlock[10];
		pred = new bBlock[10];
		in = new BitSet();
		out = new BitSet();
		gen = new BitSet();
		kill = new BitSet();

		for (int i = 0; i < succ.length ; i++)
			succ[i] = null;
	}

	public void setNum(int n) { num = n; }
	public int getNum() { return num; }

	public void setLen(int n) { len = n; }
	public int getLen() { return len; }

	public void setFirst(InstructionHandle ih) { first = ih; }
	public void setLast(InstructionHandle ih) { last = ih; }

	public InstructionHandle getFirstInstr() { return first; }
	public InstructionHandle getLastInstr() { return last; }

	// add basic block as successor (will also add reverse edge)

	public void setSucc(bBlock bb)
	{
		int i;
		for (i = 0; i < succ.length ; i++)
		{
			if (succ[i] == bb)
				return;
		}
		for (i = 0; i < succ.length ; i++)
		{
			if (succ[i] == null)
			{
				succ[i] = bb;
				break;
			}
		}
		if (i == succ.length)
			System.out.print("ERROR: bBlock "+num+" too many successors "+i);

		bb.setPred(this);
	}

	// add basic block as predecessor (will also add reverse edge)

	public void setPred(bBlock bb)
	{
		int i;
		for (i = 0; i < pred.length ; i++)
		{
			if (pred[i] == bb)
				return;
		}
		for (i = 0; i < pred.length ; i++)
		{
			if (pred[i] == null)
			{
				pred[i] = bb;
				break;
			}
		}
		if (i == pred.length)
			System.out.print("ERROR: bBlock "+num+" too many predecessors "+i);
		bb.setSucc(this);
	}

	public bBlock getPred()     { currPred = 0; return pred[currPred++]; }
	public bBlock getPredNext() { return pred[currPred++]; }

	public bBlock getSucc()     { currSucc = 0; return succ[currSucc++]; }
	public bBlock getSuccNext() { return succ[currSucc++]; }

	public BitSet getIn() { return in ; }
	public BitSet getOut() { return out; }
	public BitSet getGen() { return gen; }
	public BitSet getKill() { return kill; }

	public void print()
	{
		int i;

		System.out.println("** Basic block: "+num+" **");

		System.out.print("  Length: "+len);

		System.out.print("  Successors: ");
		
		TreeSet<Integer> bb = new TreeSet<Integer>();
		
		for (i = 0; i < succ.length ; i++)
		{
			if (succ[i] != null)
			{
				bb.add(succ[i].getNum());
			}
		}

		// print successors in sorted order
		for (Integer elem : bb)
			System.out.print(" "+elem);

		bb.clear();
		
		System.out.print("  Predecessors:");
		for (i = 0; i < pred.length ; i++)
		{
			if (pred[i] != null)
			{
				bb.add(pred[i].getNum());
			}
		}
		
		// print predecessors in sorted order
		for (Integer elem : bb)
			System.out.print(" "+elem);

		System.out.println("");

		System.out.print("  GEN "+gen.toString());
		System.out.print("  KILL "+kill.toString());
		System.out.print("  IN "+in.toString());
		System.out.print("  OUT "+out.toString());
		System.out.println("");

		InstructionHandle ih = first;
		for (i = 0; i < len; i++)
		{
			System.out.println(">  "+ih.toString(false));
			ih = ih.getNext();
		}
	}
}

