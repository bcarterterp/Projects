
package mycc;

import java.util.*; 

//----------------------------------------------------------------
// Symbol Table Entries
//     entries contain actual info of interest for each symbol

class symTabEntry {

	//-----------------------
	// fields for storing information about symbol

	String var_name;		// name of the variable   
	int var_type;			// type of the variable  

					// if array variable    
	boolean is_array;		// flag for array variable
	int num_elem;			// number of elements    
					// element type in var_type

					// if function variable 
	boolean is_func;		// flag for function    
	boolean is_forward;		// flag for forward declaration
	symTabEntry param;		// 0-1 formal parameters
	symTab sym_tab;			// local symbol table   
					// return type in var_type


	int cp_index;			// index in constant pool
	int global;			// store as global or local

	//-----------------------
	// constructor (name, type)

	symTabEntry(String s, int t)
	{
		var_name = new String(s);
		var_type = t;
		num_elem = 0;
		param = null;
		sym_tab = null;
		cp_index = -1;
		global = -1;
		is_forward = false;
	}

	//-----------------------
	// routines to add information about symbol

	void setSymTab(symTab s) 
	{ 
		sym_tab = s;       // save ref to symbol table
	}

	void setArray(int n) { is_array = true; num_elem = n; }

	void setFunc() { is_func = true; param = null; }

	void setParam(symTabEntry p) 
	{
		is_func = true;
		param = p;

		// System.out.println("DEBUG: Adding param to "+var_name);
		// if (p != null) { System.out.println("DEBUG: Param="+p.name()); }
	}

	void setForward() { is_forward = true; }
	void unsetForward() { is_forward = false; }

	void setIndex(int i) { cp_index = i; }

	void setGlobal() { global = 1; }

	void setLocal() { global = 0; }

	void setType(int type) { var_type = type; }

	//-----------------------
	// query routines to get information about symbol

	String name() { return var_name; }

	boolean isArray() { return is_array; }

	int getArraySize() { return num_elem; }

	boolean isFunc() { return is_func; }

	boolean isForward() { return is_forward; }

	int getType() { return var_type; }

	symTabEntry getParam() { return param; }

	symTab getSymTab() { return sym_tab; }

	int getIndex() { return cp_index; }

	boolean isGlobal() { return (global == 1) ; }

	boolean isLocal() { return (global == 0) ; }

}

