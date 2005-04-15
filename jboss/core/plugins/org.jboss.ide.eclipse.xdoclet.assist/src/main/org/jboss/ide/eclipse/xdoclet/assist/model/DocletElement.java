/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.StringSubstitution;
import org.apache.oro.text.regex.Util;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionTree;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class DocletElement implements Serializable
{
   /** Description of the Field */
   protected String codename = null;
   /** Description of the Field */
   protected String conditionDescription;
   /** Description of the Field */
   protected SortedKeyTreeNode node;
   /** Description of the Field */
   protected DocletTree tree;
   private ConditionTree conditionTree;

   private String helpText;
   // The pattern to search a string like "${classname}HomeRemote" for ${classname}
   private final static String VALUE_PARSER_PATTERN = "\\$\\{\\w+\\}";//$NON-NLS-1$


   /**
    *Constructor for the DocletElement object
    *
    * @param node  Description of the Parameter
    * @param tree  Description of the Parameter
    */
   protected DocletElement(SortedKeyTreeNode node, DocletTree tree)
   {
      this.node = node;
      this.tree = tree;
      node.setObject(tree.getKey(), this);
      conditionTree = new ConditionTree(null);
   }


   /**
    * Adds a feature to the Child attribute of the DocletElement object
    *
    * @param name  The feature to be added to the Child attribute
    * @return      Description of the Return Value
    */
   public DocletElement addChild(String name)
   {
      SortedKeyTreeNode childNode = node.addChild(name);
      DocletElement child = new DocletElement(childNode, tree);
      return child;
   }


   /**
    * DocletElements are equals if there nodes are equals. The latter is determined by the key. This makes sense
    * as there must be no two DocletElements with the same key.
    *
    * @param obj  Description of the Parameter
    * @return     Description of the Return Value
    * @see        java.lang.Object#equals(Object)
    */
   public boolean equals(Object obj)
   {
      if (!(obj instanceof DocletElement))
      {
         return false;
      }
      DocletElement element = (DocletElement) obj;
      return this.node.equals(element.node);
   }


   /**
    * Gets the child attribute of the DocletElement object
    *
    * @param name  Description of the Parameter
    * @return      The child value
    */
   public DocletElement getChild(String name)
   {
      return (DocletElement) node.getChildObject(name, tree.getKey());
   }


   /**
    * Gets the child attribute of the DocletElement object
    *
    * @param name                    Description of the Parameter
    * @param member                  Description of the Parameter
    * @return                        The child value
    * @exception JavaModelException  Description of the Exception
    */
   public DocletElement getChild(String name, IMember member)
          throws JavaModelException
   {
      DocletElement child = getChild(name);
      if (child != null && child.getConditionTree().eval(member))
      {
         return child;
      }
      return null;
   }


   /**
    * Gets the childrenCount attribute of the DocletElement object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return getChildrenElements().length;
   }


   /**
    * Gets the childrenCount attribute of the DocletElement object
    *
    * @param member                  Description of the Parameter
    * @return                        The childrenCount value
    * @exception JavaModelException  Description of the Exception
    */
   public int getChildrenCount(IMember member) throws JavaModelException
   {
      return getChildrenElements(member).length;
   }


   /**
    * Returns the children.
    *
    * @param member                  Description of the Parameter
    * @return                        TreeMap
    * @exception JavaModelException  Description of the Exception
    */
   public DocletElement[] getChildrenElements(IMember member)
          throws JavaModelException
   {
      if (member == null)
      {
         return getChildrenElements();
      }
      ArrayList list = new ArrayList();
      DocletElement[] childrenDoclets = getChildrenElements();
      for (int i = 0; i < childrenDoclets.length; i++)
      {
         if (childrenDoclets[i].getConditionTree().eval(member))
         {
            list.add(childrenDoclets[i]);
         }
      }
      return (DocletElement[]) list.toArray(new DocletElement[list.size()]);
   }


   /**
    * Gets the childrenElements attribute of the DocletElement object
    *
    * @return   The childrenElements value
    */
   public DocletElement[] getChildrenElements()
   {
      return getChildrenElements(DocletElement.class.getName());
   }


   /**
    * Gets the childrenElements attribute of the DocletElement object
    *
    * @param key  Description of the Parameter
    * @return     The childrenElements value
    */
   public DocletElement[] getChildrenElements(String key)
   {
      List list = node.getChildrenObjects(key);
      return (DocletElement[]) list.toArray(new DocletElement[list.size()]);
   }


   /**
    * Gets the codeName attribute of the DocletElement object
    *
    * @return   The codeName value
    */
   public String getCodeName()
   {
      if (codename == null)
      {
         return getName();
      }
      return codename;
   }


   /**
    * Returns the conditionDescription.
    *
    * @return   String
    */
   public String getConditionDescription()
   {
      return conditionDescription;
   }


   /**
    * Returns the conditionTree.
    *
    * @return   ConditionTree
    */
   public ConditionTree getConditionTree()
   {
      return conditionTree;
   }


   /**
    * Returns the helpText.
    *
    * @return   String
    */
   public String getHelpText()
   {
      return helpText;
   }


   /**
    * Gets the name attribute of the DocletElement object
    *
    * @return   The name value
    */
   public String getName()
   {
      return node.getName();
   }


   /**
    * Returns the node.
    *
    * @return   SortedKeyTreeNode
    */
   public SortedKeyTreeNode getNode()
   {
      return node;
   }


   /**
    * Gets the parent attribute of the DocletElement object
    *
    * @return   The parent value
    */
   public DocletElement getParent()
   {
      if (node.getParentNode() != null)
      {
         return (DocletElement) node.getParentNode().getObject(
               DocletElement.class.getName());
      }
      return null;
   }


   /**
    * Gets the tree attribute of the DocletElement object
    *
    * @return   The tree value
    */
   public DocletTree getTree()
   {
      return tree;
   }


   /**
    * Gets the type attribute of the DocletElement object
    *
    * @return   The type value
    */
   public DocletType getType()
   {
      final int parentcount = node.getParentCount();
      switch (parentcount)
      {
         case 0:
            return DocletType.NAMESPACE;
         case 1:
            return DocletType.COMMAND;
         case 2:
            if (node.getAdditionalAttributes()
                  .get(IDocletConstants.ATTR_DISCRETE_VALUE_RANGE)
                  != null)
            {
               return DocletType.DISCRETE_ATTRIBUTE;
            }
            return DocletType.NON_DISCRETE_ATTRIBUTE;
         case 3:
            if (node.getAdditionalAttributes()
                  .get(IDocletConstants.ATTR_PARSING)
                  != null)
            {
               return DocletType.VALUE_WITH_VARIABLE;
            }
            return DocletType.VALUE_WITHOUT_VARIABLE;
         default:
            return null;
      }
   }


   /**
    * Description of the Method
    *
    * @param member                  Description of the Parameter
    * @return                        Description of the Return Value
    * @exception JavaModelException  Description of the Exception
    */
   public boolean hasChildren(IMember member) throws JavaModelException
   {
      return getChildrenElements(member).length > 0;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildren()
   {
      return getChildrenElements().length > 0;
   }


   /**
    * See equals
    *
    * @return   Description of the Return Value
    * @see      java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      return node.hashCode();
   }


   /**
    * Method replaceVariables.
    *
    * @param systemVariables  if null the non-parsed name is returned
    * @param store            Description of the Parameter
    * @return                 String the parsed name or if the systemVariables equals null or
    * the parsing attribute is not set to true the non parsed name.
    */
   public String replaceVariables(Map systemVariables, VariableStore store)
   {
      if (store == null)
      {
         throw new IllegalArgumentException();
      }
      String name = getCodeName();
      if ((node.getAdditionalAttributes().get(IDocletConstants.ATTR_PARSING)
            == null)
            || systemVariables == null)
      {
         return getCodeName();
      }

      // The pattern for the value-attribute name to get the variables used in there
      Perl5Matcher valueParserMatcher = new Perl5Matcher();
      PatternMatcherInput nameInput = new PatternMatcherInput(name);
      // The pattern that is defined for a variable and is applied to the value of
      // a system-variable
      Perl5Matcher variableMatcher = new Perl5Matcher();

      StringBuffer parsedName = new StringBuffer();
      // a variable use in the value-attribute name, i.e. classnameWithoutBean
      String variable;
      // i.e the value for classname might be AdressBean
      String systemVariableValue;
      // the result after parsing the variable, i.e. classnameWithoutBean to Adress
      // String variableValue;
      // the replacement for the value-attribute name after parsing
      String replacement;

      Variable variablePattern;
      String transformed = name;
      while (valueParserMatcher.contains(nameInput, PatternStore.getPattern(VALUE_PARSER_PATTERN)))
      {
         variable =
               valueParserMatcher.getMatch().group(0).substring(
               2,
               valueParserMatcher.getMatch().group(0).length() - 1);
         // if in the xml the variable used in the value-attribute name is defined in the
         // variables-section get the attributes for this variable otherwise it's null
         if ((variablePattern = store.getVariable(variable)) != null)
         {
            // if the system-variable for the variable-tag in the xml is passed by the client,
            // then get the value passed by the client for that variable otherwise it's null
            if ((systemVariableValue =
                  (String) systemVariables.get(variablePattern.getSystemVariable()))
                  != null)
            {
               // i.e. AdressBean to Adress
               if (variableMatcher.contains(
                     new PatternMatcherInput(systemVariableValue),
                     variablePattern.getCompiledPattern()))
               {
                  replacement = variableMatcher.getMatch().group(1);
               }
               else
               {
                  replacement = "";//$NON-NLS-1$
               }
               Util.substitute(
                     parsedName = new StringBuffer(),
                     valueParserMatcher,
                     PatternStore.getPattern(VALUE_PARSER_PATTERN),
                     new StringSubstitution(replacement),
                     transformed,
                     1);
               transformed = parsedName.toString();
            }
            else
            {
               Util.substitute(
                     parsedName = new StringBuffer(),
                     valueParserMatcher,
                     PatternStore.getPattern(VALUE_PARSER_PATTERN),
                     new StringSubstitution(""), //$NON-NLS-1$
               transformed,
                     1);
               transformed = parsedName.toString();
            }
         }
         else
         {
            Util.substitute(
                  parsedName = new StringBuffer(),
                  valueParserMatcher,
                  PatternStore.getPattern(VALUE_PARSER_PATTERN),
                  new StringSubstitution(""), //$NON-NLS-1$
            transformed,
                  1);
         }
         transformed = parsedName.toString();
      }
      return transformed;
   }


   /**
    * Sets the codename.
    *
    * @param codename  The codename to set
    */
   public void setCodename(String codename)
   {
      this.codename = codename;
   }


   /**
    * Sets the conditionDescription.
    *
    * @param conditionDescription  The conditionDescription to set
    */
   public void setConditionDescription(String conditionDescription)
   {
      this.conditionDescription = conditionDescription;
   }


   /**
    * Sets the helpText.
    *
    * @param helpText  The helpText to set
    */
   public void setHelpText(String helpText)
   {
      this.helpText = helpText;
   }


   /**
    * @return   Description of the Return Value
    * @see      java.lang.Object#toString()
    */
   public String toString()
   {
      return getName();
   }

}
