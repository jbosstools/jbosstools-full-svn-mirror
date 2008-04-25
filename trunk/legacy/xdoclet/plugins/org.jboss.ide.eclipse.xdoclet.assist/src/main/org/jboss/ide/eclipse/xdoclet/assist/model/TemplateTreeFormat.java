/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.util.HashMap;

/**
 * @author    Hans Dockter
 * @version   $Revision: 1420 $
 * @created   17 mai 2003
 */
public class TemplateTreeFormat
{
   /** Description of the Field */
   protected boolean createCommentBlock = false;

   /** Description of the Field */
   protected String indent = "";//$NON-NLS-1$

   /** Description of the Field */
   protected boolean indentFirstLine = true;

   /** Description of the Field */
   protected TemplateTree tree;

   /** Description of the Field */
   protected VariableStore variableStore;

   /** Description of the Field */
   protected HashMap variables;

   /** Description of the Field */
   public static String COMMENT_BLOCK_FOOTER = "*/";//$NON-NLS-1$

   /** Description of the Field */
   public static String COMMENT_BLOCK_HEADER = "/**";//$NON-NLS-1$

   /** Description of the Field */
   public static char DEFAULT_NAMESPACE_SEPARATOR = '.';//$NON-NLS-1$

   /** Description of the Field */
   public static char NAMESPACE_MARKER = '@';//$NON-NLS-1$

   /** Description of the Field */
   public static String NON_DISCRETE_DEFAULT_VALUE = "value";//$NON-NLS-1$

   /** Description of the Field */
   public final static String NEW_LINE = System.getProperty("line.separator");//$NON-NLS-1$

   /**Constructor for the TemplateTreeFormat object */
   public TemplateTreeFormat()
   {
      super();
   }

   /**
    * Method TemplateTreeFormat.
    *
    * @param tree
    */
   public TemplateTreeFormat(TemplateTree tree)
   {
      super();
      setTree(tree);
   }

   /**
    * @return   String
    */
   public String format()
   {
      StringBuffer text = new StringBuffer();
      if (createCommentBlock)
      {
         String additionalIndent = "";//$NON-NLS-1$
         if (indentFirstLine)
         {
            text.append(indent);
            additionalIndent = " ";//$NON-NLS-1$
         }
         text.append(COMMENT_BLOCK_HEADER);
         text.append(NEW_LINE);
         appendTemplateText(text, indent + additionalIndent, true);
         text.append(NEW_LINE);
         text.append(indent + additionalIndent);
         text.append(COMMENT_BLOCK_FOOTER);
      }
      else
      {
         appendTemplateText(text, indent, indentFirstLine);
      }
      return text.toString();
   }

   /**
    * Returns the indent.
    *
    * @return   int
    */
   public String getIndent()
   {
      return indent;
   }

   /**
    * Returns the tree.
    *
    * @return   TemplateTree
    */
   public TemplateTree getTree()
   {
      return tree;
   }

   /**
    * Returns the variableStore.
    *
    * @return   VariableStore
    */
   public VariableStore getVariableStore()
   {
      return variableStore;
   }

   /**
    * Returns the variables.
    *
    * @return   HashMap
    */
   public HashMap getVariables()
   {
      return variables;
   }

   /**
    * Returns the createCommentBlock.
    *
    * @return   boolean
    */
   public boolean isCreateCommentBlock()
   {
      return createCommentBlock;
   }

   /**
    * Returns the indentFirstLine.
    *
    * @return   boolean
    */
   public boolean isIndentFirstLine()
   {
      return indentFirstLine;
   }

   /**
    * Sets the createCommentBlock.
    *
    * @param createCommentBlock  The createCommentBlock to set
    */
   public void setCreateCommentBlock(boolean createCommentBlock)
   {
      this.createCommentBlock = createCommentBlock;
   }

   /**
    * Sets the indent.
    *
    * @param indent  The indent to set
    */
   public void setIndent(String indent)
   {
      this.indent = indent;
   }

   /**
    * Sets the indentFirstLine.
    *
    * @param indentFirstLine  The indentFirstLine to set
    */
   public void setIndentFirstLine(boolean indentFirstLine)
   {
      this.indentFirstLine = indentFirstLine;
   }

   /**
    * Sets the tree.
    *
    * @param tree  The tree to set
    */
   public void setTree(TemplateTree tree)
   {
      this.tree = tree;
   }

   /**
    * Sets the variableStore.
    *
    * @param variableStore  The variableStore to set
    */
   public void setVariableStore(VariableStore variableStore)
   {
      this.variableStore = variableStore;
   }

   /**
    * Sets the variables.
    *
    * @param variables  The variables to set
    */
   public void setVariables(HashMap variables)
   {
      this.variables = variables;
   }

   /**
    * Description of the Method
    *
    * @param indent  Description of the Parameter
    * @param text    Description of the Parameter
    */
   protected void appendPrefix(String indent, StringBuffer text)
   {
      text.append(indent);
      text.append("*");//$NON-NLS-1$
      appendSpaces(text, 1);
   }

   /**
    * Description of the Method
    *
    * @param buffer  Description of the Parameter
    * @param n       Description of the Parameter
    */
   protected void appendSpaces(StringBuffer buffer, int n)
   {
      for (int i = 0; i < n; i++)
      {
         buffer.append(' ');
      }
   }

   /**
    * Description of the Method
    *
    * @param text             Description of the Parameter
    * @param indent           Description of the Parameter
    * @param indentFirstLine  Description of the Parameter
    * @return                 Description of the Return Value
    */
   protected String appendTemplateText(StringBuffer text, String indent, boolean indentFirstLine)
   {
      TemplateElement namespace;
      TemplateElement command;
      TemplateElement attribute;
      // TemplateElement value;
      int internalIndent;
      for (int i = 0; i < tree.getChildrenCount(); i++)
      {
         namespace = tree.getChildrenElements()[i];
         if (namespace.getChildrenCount() == 0)
         {
            if (i > 0 || indentFirstLine)
            {
               appendPrefix(indent, text);
            }
            text.append(namespace.getDocletElement().getCodeName());
            text.append(DEFAULT_NAMESPACE_SEPARATOR);
         }
         for (int j = 0; j < namespace.getChildrenCount(); j++)
         {
            command = namespace.getChildrenElements()[j];
            if (i > 0 || j > 0)
            {
               text.append(NEW_LINE);
            }
            if (i > 0 || j > 0 || indentFirstLine)
            {
               appendPrefix(indent, text);
            }
            text.append(NAMESPACE_MARKER);
            text.append(namespace.getDocletElement().getCodeName());
            text.append("" //$NON-NLS-1$
                  + command.getDocletElement().getNode().getAdditionalAttributes().get(
                        IDocletConstants.ATTR_NAMESPACE_SEPARATOR));
            text.append(command.getDocletElement().getCodeName());
            appendSpaces(text, 1);
            // i.e. '@ejb:bean '
            internalIndent = namespace.getDocletElement().getCodeName().length()
                  + command.getDocletElement().getCodeName().length() + 3;
            for (int k = 0; k < command.getChildrenCount(); k++)
            {
               attribute = command.getChildrenElements()[k];
               if (k > 0)
               {
                  text.append(NEW_LINE);
                  appendPrefix(indent, text);
                  appendSpaces(text, internalIndent);
               }
               text.append(attribute.getDocletElement().getCodeName());
               text.append('=');
               text.append('"');
               if (attribute.hasChildrenElements())
               {
                  text.append(attribute.getChildrenElements()[0].getDocletElement().replaceVariables(variables,
                        variableStore));
               }
               else
               {
                  text.append(NON_DISCRETE_DEFAULT_VALUE);
               }
               text.append('"');
            }
         }
      }
      return text.toString();
   }

}
