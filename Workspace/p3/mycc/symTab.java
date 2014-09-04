
package mycc;

import java.util.*; 

//--------------------------------------------------------------------
// Symbol table for C-- compiler
//
//    Symbol table is a collection of symTabEntries, managed by a HashMap  
//
//    To implement nested lexical scoping, symbol tables have a pointer
//    to the parent symbol table (usually the global symbol table)

public class symTab
{
	TreeMap symMap;       // list of symbols in scope

	symTab parent;        // parent symbol table (enclosing scope)

	ArrayList children;   // children symbol tables (nested scopes)
				// except for function scopes
    
	//----------------------------------
	//----------------------------------
	// constructor

	symTab()
	{
		symMap = new TreeMap();
		parent = null;
		children = new ArrayList();
	}

	//----------------------------------
	//----------------------------------
	// methods 

	void setParent(symTab p) { parent = p; }

	symTab getParent() { return parent; }

	void addChild(symTab p) { children.add(p); }

	ArrayList getChildren() { return children; }

	// remove symTabEntry from symbol table, return false if fails
	boolean remove(symTabEntry p) 
	{ 
		if (symMap.remove(p.name()) == null)
			return false;
		return true;
	}

	// list of symTabEntries
	public Iterator getIter() { return symMap.values().iterator(); }

	// size of symbol table
	public int size() { return symMap.size(); }

	//----------------------------------
	// add symbol to symbol table

	void addSym(symTabEntry s)
	{
		// System.out.println("DEBUG: symtab adding "+s.name());

		if (symMap == null) {
			// System.out.println("ERROR: null symbolTable");
			symMap = new TreeMap();
		}

		if ( symMap.get(s.name()) != null)
		{
		// System.out.println("ERROR: multiple declaration of "+s.name());
		}
		else 
		{
			symMap.put(s.name(), s);
		}
	}

	//----------------------------------
	// look up symbol from symbol table

	// only look in current symbol table
	symTabEntry getLocalSym(String name)
	{
		symTabEntry s = (symTabEntry) symMap.get(name);
		return s;
	}

	// look in current symbol table and ancestors (enclosing scopes)
	symTabEntry getSym(String name)
	{
		symTabEntry s = (symTabEntry) symMap.get(name);

		if ((s == null) && (parent != null)) {
			s = parent.getSym(name);
		}

		return s;
	}

	//----------------------------------
	// print out symbols in a symbol table

	void print()
	{
		for (Iterator i = symMap.values().iterator(); i.hasNext(); )
		{
			symTabEntry s = (symTabEntry) i.next();

			System.out.print(s.name());

			if (s.getType() == sym.INT)
				System.out.print(", int");
			else if (s.getType() == sym.VOID)
				System.out.print(", void");

			if (s.isArray())
				System.out.print(", array");

			if (s.isFunc()) {
				System.out.print(", function");
				symTabEntry p = s.getParam();
				if (p == null) {
					System.out.print(", param = <void>");
				}
				else {
					System.out.print(", param = ");
					if (p.getType() == sym.INT)
						System.out.print("int");
					else if (p.getType() == sym.VOID)
						System.out.print("void");
					else if (p.getType() == sym.STRING_LITERAL)
						System.out.print("String");
				}
			}

			System.out.println("");
		}

		// print out nested scopes

		for (Iterator i = children.iterator(); i.hasNext(); )
		{
			System.out.println("{");
			symTab st = (symTab) i.next();
			st.print();
			System.out.println("}");
		}
	}


	//----------------------------------
	// print out all symbols 

	void list()
	{
		System.out.println("** Global Variables **");
		print();

		for (Iterator i = symMap.values().iterator(); i.hasNext(); )
		{
			symTabEntry s = (symTabEntry) i.next();

			if (s.isFunc()) {
				String nm = s.name();

			  if (	!nm.equals("printStr") && 
				!nm.equals("printInt") &&
				!nm.equals("printLn")) {

				System.out.print("** Local Variables in ");
				System.out.print(nm);
				System.out.println("() **");
				symTab st = s.getSymTab();
				if (st != null)
					st.print();
			  }
			}
		}
	}

}
