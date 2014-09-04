
package mycc;

import java.util.*;

public class astNodeList
{
   Vector       nodeList;
   int          nodeIndex;

   public astNodeList() { nodeList = new Vector(); }

   public void add(astNode tn) { nodeList.addElement(tn); }

   public boolean isEmpty() { return nodeList.isEmpty(); }

   public astNode getFirstNode()
   {
      nodeIndex = 0;

      if (isEmpty()) 
          return null;

      return (astNode) (nodeList.firstElement());
   }

   public astNode getNextNode()
   {
      nodeIndex++;

      if (nodeIndex < nodeList.size())
      {
         // System.out.println("Node # "+nodeIndex+" Size = "+nodeList.size());
         return (astNode) (nodeList.elementAt(nodeIndex));
      }

      return null;
   }

   public void print()
   {
      Enumeration       e = nodeList.elements();
      int               count = 0;

      while (e.hasMoreElements())
      {
         // System.out.println("printing node # " + count);

         count++;
         astNode an = (astNode) e.nextElement();

         if (an != null)
            an.print();
      }

   }

}




