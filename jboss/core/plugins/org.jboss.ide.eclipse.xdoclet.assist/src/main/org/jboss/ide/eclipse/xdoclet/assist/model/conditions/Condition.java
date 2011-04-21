/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Base class for conditions. Inspired by the
 * xtags.condition package from Aslak Hellesøy of the
 * XDoclet team
 *
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public abstract class Condition implements Serializable
{
   /** Description of the Field */
   protected IMember cachedMember;
   /** Description of the Field */
   protected boolean cachedResult;
   /** Description of the Field */
   protected int maximumNumberOfChildren = 0;
   /** Description of the Field */
   protected Condition parent;
   /** Description of the Field */
   protected ConditionTree tree;

   private final ArrayList childConditions = new ArrayList();
   /** Description of the Field */
   public static int UNLIMITED_NUMBER_OF_CHILDREN = -1;

   /** for debugging */
   private static String indent = "                                  ";//$NON-NLS-1$


   /**Constructor for the Condition object */
   public Condition()
   {
      super();
   }


   /**
    * Adds a feature to the ChildCondition attribute of the Condition object
    *
    * @param condition               The feature to be added to the ChildCondition attribute
    * @exception ConditionException  Description of the Exception
    */
   public void addChildCondition(Condition condition) throws ConditionException
   {
      if (condition == null)
      {
         throw new IllegalArgumentException("Can't add null to condition " + condition);//$NON-NLS-1$
      }
      if (getChildConditionsCount() == getMaximumNumberOfChildren())
      {
         throw new ToManyChildrenConditionsException("Attention: Can't add " + condition + " to Condition " + this);//$NON-NLS-1$ //$NON-NLS-2$
      }
      childConditions.add(condition);
      condition.setParent(this);
      condition.setTree(getTree());
      if (tree != null)
      {
         tree.fireChange(ConditionTree.CHILD_ADDED);
      }
   }


   /**
    * @param member                  Description of the Parameter
    * @return                        boolean
    * @exception JavaModelException  Description of the Exception
    */
   public boolean eval(IMember member) throws JavaModelException
   {
      if (member == null)
      {
         return true;
      }
//		if (member.equals(cachedMember)) {
//			System.out.println("cach");
//			return cachedResult;
//		}
//		cachedMember = member;
      return (cachedResult = evalInternal(member));
   }


   /**
    * Gets the childConditions attribute of the Condition object
    *
    * @return   The childConditions value
    */
   public List getChildConditions()
   {
      return childConditions;
   }


   /**
    * Gets the childConditionsCount attribute of the Condition object
    *
    * @return   The childConditionsCount value
    */
   public int getChildConditionsCount()
   {
      return childConditions.size();
   }


   /**
    * Returns the maximumNumberOfChildren.
    *
    * @return   int
    */
   public int getMaximumNumberOfChildren()
   {
      return maximumNumberOfChildren;
   }


   /**
    * Returns the parent.
    *
    * @return   Object
    */
   public Condition getParent()
   {
      return parent;
   }


   /**
    * Returns the tree.
    *
    * @return   ConditionTree
    */
   public ConditionTree getTree()
   {
      return tree;
   }


   /**
    * Description of the Method
    *
    * @param out          Description of the Parameter
    * @param indentCount  Description of the Parameter
    */
   public void print(java.io.PrintStream out, int indentCount)
   {
      out.print(indent.substring(0, indentCount));
      out.println(toString());

      Iterator conditions = getChildConditions().iterator();

      while (conditions.hasNext())
      {
         Condition subCondition = (Condition) conditions.next();

         subCondition.print(out, indentCount + 2);
      }
   }


   /** Description of the Method */
   public void remove()
   {
      if (tree != null)
      {
         tree.fireChange(ConditionTree.REMOVED);
      }
      if (parent != null)
      {
         parent.getChildConditions().remove(this);
      }
   }


   /**
    * Sets the maximumNumberOfChildren.
    *
    * @param maximumNumberOfChildren  The maximumNumberOfChildren to set
    */
   public void setMaximumNumberOfChildren(int maximumNumberOfChildren)
   {
      this.maximumNumberOfChildren = maximumNumberOfChildren;
   }


   /**
    * Sets the parent.
    *
    * @param parent  The parent to set
    */
   public void setParent(Condition parent)
   {
      this.parent = parent;
   }


   /**
    * Sets the tree.
    *
    * @param tree  The tree to set
    */
   public void setTree(ConditionTree tree)
   {
      this.tree = tree;
   }


   /**
    * Method evalInternal.
    *
    * @param member                  Description of the Parameter
    * @return                        boolean
    * @exception JavaModelException  Description of the Exception
    */
   protected abstract boolean evalInternal(IMember member)
          throws JavaModelException;

}
