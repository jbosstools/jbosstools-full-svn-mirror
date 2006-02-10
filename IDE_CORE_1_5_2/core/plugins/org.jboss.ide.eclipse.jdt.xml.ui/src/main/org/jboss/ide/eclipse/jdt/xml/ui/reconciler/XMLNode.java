/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.xml.ui.reconciler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLNode extends TypedPosition implements IAdaptable, IWorkbenchAdapter, Comparable
{

   private boolean added = false;

   private List children = new ArrayList();

   private XMLNode correspondingNode = null;

   private IDocument document = null;

   private boolean modified = false;

   private XMLNode parent = null;

   /** Description of the Field */
   public final static int AFTER_ATTRIBUTE = 6;

   /** Description of the Field */
   public final static int AFTER_ATT_VALUE = 7;

   /** Description of the Field */
   public final static int ATTR = 1;

   /** Description of the Field */
   public final static int ATTRIBUTE = 4;

   /** Description of the Field */
   public final static int ATT_VALUE = 5;

   /** Description of the Field */
   public final static int DOUBLEQUOTE = 2;

   /** Description of the Field */
   public final static int SINGLEQUOTE = 3;

   /** Description of the Field */
   public final static int TAG = 0;

   /**
    *Constructor for the XMLNode object
    *
    * @param offset    Description of the Parameter
    * @param length    Description of the Parameter
    * @param type      Description of the Parameter
    * @param document  Description of the Parameter
    */
   public XMLNode(int offset, int length, String type, IDocument document)
   {
      super(offset, length, type);
      added = true;
      this.document = document;
   }

   /**
    *Constructor for the XMLNode object
    *
    * @param region  Description of the Parameter
    */
   public XMLNode(ITypedRegion region)
   {
      super(region);
      added = true;
   }

   /**
    * Adds a feature to the Child attribute of the XMLNode object
    *
    * @param child  The feature to be added to the Child attribute
    */
   public synchronized void addChild(XMLNode child)
   {
      for (int i = 0; i < children.size(); i++)
      {
         if (((XMLNode) children.get(i)).getOffset() > child.getOffset())
         {
            children.add(i, child);
            return;
         }
      }
      children.add(child);
   }

   /**
    * Description of the Method
    *
    * @param o  Description of the Parameter
    * @return   Description of the Return Value
    */
   public int compareTo(Object o)
   {
      XMLNode n = null;
      if (!(o instanceof XMLNode))
      {
         return 0;
      }
      n = (XMLNode) o;

      if (this == o)
      {
         return 0;
      }

      return (getOffset() > n.getOffset()) ? 1 : ((getOffset() < n.getOffset()) ? -1 : 0);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean containsOnlyWhitespaces()
   {
      String content = "";//$NON-NLS-1$

      try
      {
         content = document.get(getOffset(), getLength());
      }
      catch (BadLocationException e)
      {
         return true;
      }

      return (content.trim().length() == 0);
   }

   /**
    * Gets the adapter attribute of the XMLNode object
    *
    * @param adapter  Description of the Parameter
    * @return         The adapter value
    */
   public Object getAdapter(Class adapter)
   {
      if (adapter.equals(IWorkbenchAdapter.class))
      {
         return this;
      }

      return null;
   }

   /**
    * Gets the attributeAt attribute of the XMLNode object
    *
    * @param offset  Description of the Parameter
    * @return        The attributeAt value
    */
   public XMLNode getAttributeAt(int offset)
   {
      List attrs = getAttributes();

      for (Iterator it = attrs.iterator(); it.hasNext();)
      {
         XMLNode node = (XMLNode) it.next();

         if (node.getOffset() <= offset && offset <= node.getOffset() + node.getLength())
         {
            return node;
         }
      }

      return null;
   }

   /**
    * Gets the attributes attribute of the XMLNode object
    *
    * @return   The attributes value
    */
   public List getAttributes()
   {
      List attrs = new ArrayList();
      String content = null;
      int state = TAG;
      int start = -1;
      int startLength = 0;
      int endLength = 0;

      if (XMLPartitionScanner.XML_PI.equals(getType()))
      {
         startLength = 2;
         endLength = 2;
      }
      else if (XMLPartitionScanner.XML_DECL.equals(getType()))
      {
         startLength = 2;
         endLength = 1;
      }
      else if (XMLPartitionScanner.XML_TAG.equals(getType()))
      {
         startLength = 1;
         endLength = 1;
      }
      else if (XMLPartitionScanner.XML_EMPTY_TAG.equals(getType()))
      {
         startLength = 1;
         endLength = 2;
      }
      else
      {
         return attrs;
      }

      try
      {
         content = document.get(getOffset(), getLength());
      }
      catch (BadLocationException e)
      {
         e.printStackTrace();
         return attrs;
      }

      if (getName() == null)
      {
         return attrs;
      }

      for (int i = startLength + getName().length(); i < content.length() - endLength; i++)
      {
         char c = content.charAt(i);
         switch (c)
         {
            //            case '=':
            //                if (state == TAG) {
            //                    state = ATTR_VALUE;
            //                }
            //                break;
            //
            case '"' :
               if (state == DOUBLEQUOTE)
               {
                  attrs
                        .add(new XMLNode(getOffset() + start, i - start + 1, XMLPartitionScanner.XML_ATTRIBUTE,
                              document));
                  start = -1;
                  state = TAG;
               }
               else
               {
                  state = DOUBLEQUOTE;
               }
               break;
            case '\'' :
               if (state == SINGLEQUOTE)
               {
                  attrs
                        .add(new XMLNode(getOffset() + start, i - start + 1, XMLPartitionScanner.XML_ATTRIBUTE,
                              document));
                  start = -1;
                  state = TAG;
               }
               else
               {
                  state = SINGLEQUOTE;
               }
               break;
            default :
               if (!Character.isWhitespace(c) && state == TAG)
               {
                  start = i;
                  state = ATTR;
               }
         }
      }

      if (start != -1)
      {
         attrs.add(new XMLNode(getOffset() + start, content.length() - startLength - start,
               XMLPartitionScanner.XML_ATTRIBUTE, document));
      }

      return attrs;
   }

   /**
    * Gets the children attribute of the XMLNode object
    *
    * @return   The children value
    */
   public synchronized List getChildren()
   {
      return children;
   }

   /**
    * Gets the children attribute of the XMLNode object
    *
    * @param o  Description of the Parameter
    * @return   The children value
    */
   public Object[] getChildren(Object o)
   {
      if (o instanceof XMLNode)
      {
         List filteredChildren = new ArrayList();

         for (Iterator it = ((XMLNode) o).getChildren().iterator(); it.hasNext();)
         {
            XMLNode n = (XMLNode) it.next();

            if (!XMLPartitionScanner.XML_END_TAG.equals(n.getType())
                  && !XMLPartitionScanner.XML_END_DECL.equals(n.getType())
                  && (!XMLPartitionScanner.XML_TEXT.equals(n.getType()) || !n.containsOnlyWhitespaces()))
            {
               filteredChildren.add(n);
            }
         }

         if (XMLPartitionScanner.XML_TAG.equals(((XMLNode) o).getType())
               || XMLPartitionScanner.XML_PI.equals(((XMLNode) o).getType())
               || XMLPartitionScanner.XML_EMPTY_TAG.equals(((XMLNode) o).getType()))
         {
            filteredChildren.addAll(0, ((XMLNode) o).getAttributes());
         }

         return filteredChildren.toArray(new XMLNode[0]);
      }

      return null;
   }

   /**
    * Gets the childrenAfter attribute of the XMLNode object
    *
    * @param child  Description of the Parameter
    * @return       The childrenAfter value
    */
   public List getChildrenAfter(XMLNode child)
   {
      List result = new ArrayList();
      for (int i = 0; i < children.size(); i++)
      {
         if (((XMLNode) children.get(i)).getOffset() > child.getOffset())
         {
            result.add(child);
         }
      }

      return result;
   }

   /**
    * Gets the content attribute of the XMLNode object
    *
    * @return   The content value
    */
   public String getContent()
   {
      String content = "";//$NON-NLS-1$

      try
      {
         content = document.get(getOffset(), getLength());
      }
      catch (BadLocationException e)
      {
      }

      return content;
   }

   /**
    * Gets the contentFrom attribute of the XMLNode object
    *
    * @param from  Description of the Parameter
    * @return      The contentFrom value
    */
   public String getContentFrom(int from)
   {
      String content = "";//$NON-NLS-1$

      try
      {
         content = document.get(from, getOffset() - from + getLength() - 1);
      }
      catch (BadLocationException e)
      {
      }

      return content;
   }

   /**
    * Gets the contentTo attribute of the XMLNode object
    *
    * @param to  Description of the Parameter
    * @return    The contentTo value
    */
   public String getContentTo(int to)
   {
      String content = "";//$NON-NLS-1$

      if (to > getOffset())
      {
         try
         {
            content = document.get(getOffset(), to - getOffset());
         }
         catch (BadLocationException e)
         {
         }

         return content.substring(1);
      }
      return content;
   }

   /**
    * Gets the correspondingNode attribute of the XMLNode object
    *
    * @return   The correspondingNode value
    */
   public XMLNode getCorrespondingNode()
   {
      return correspondingNode == null ? this : correspondingNode;
   }

   /**
    * For !DOCTYPE:
    *
    * [28]  doctypedecl ::= '<!DOCTYPE' S Name (S ExternalID)? S?
    *                       ('[' (markupdecl | DeclSep)* ']' S?)? '>'
    * [28a] DeclSep     ::= PEReference | S
    * [29]  markupdecl  ::= elementdecl | AttlistDecl | EntityDecl | NotationDecl
    *                       | PI | Comment
    * [75]  ExternalID  ::= 'SYSTEM' S SystemLiteral
    *                       | 'PUBLIC' S PubidLiteral S SystemLiteral
    *
    * @return   The dTDLocation value
    */
   public String getDTDLocation()
   {
      //TODO: this must be changed to include inner DTDs
      String content = getContent();
      String location = null;
      int index = -1;
      int endIndex = -1;

      content = content.substring("<!DOCTYPE".length());//$NON-NLS-1$

      index = content.indexOf("SYSTEM");//$NON-NLS-1$

      if (index != -1)
      {
         index = content.indexOf("\"", index + "SYSTEM".length());//$NON-NLS-1$ //$NON-NLS-2$

         if (index != -1)
         {
            endIndex = content.indexOf("\"", index + 1);//$NON-NLS-1$
         }
         else
         {
            index = content.indexOf("'", index + "SYSTEM".length());//$NON-NLS-1$ //$NON-NLS-2$
            if (index == -1)
            {
               return null;
            }
            endIndex = content.indexOf("'", index + 1);//$NON-NLS-1$
         }
      }
      else
      {
         index = content.indexOf("PUBLIC");//$NON-NLS-1$
         if (index == -1)
         {
            return null;
         }
         index = content.indexOf("\"", index + "PUBLIC".length());//$NON-NLS-1$ //$NON-NLS-2$
         if (index != -1)
         {
            // skip public ID
            index = content.indexOf("\"", index + 1);//$NON-NLS-1$
         }
         else
         {
            index = content.indexOf("'", index + "PUBLIC".length());//$NON-NLS-1$ //$NON-NLS-2$
            if (index == -1)
            {
               return null;
            }
            // skip public ID
            index = content.indexOf("'", index + 1);//$NON-NLS-1$
         }
         index = content.indexOf("\"", index + 1);//$NON-NLS-1$
         if (index != -1)
         {
            endIndex = content.indexOf("\"", index + 1);//$NON-NLS-1$
         }
         else
         {
            index = content.indexOf("'", index + 1);//$NON-NLS-1$
            if (index != -1)
            {
               endIndex = content.indexOf("'", index + 1);//$NON-NLS-1$
            }
         }
      }
      if (index == -1 || endIndex == -1)
      {
         return null;
      }
      location = content.substring(index + 1, endIndex);

      return location;
   }

   /**
    * Gets the document attribute of the XMLNode object
    *
    * @return   The document value
    */
   public IDocument getDocument()
   {
      return document;
   }

   /**
    * Gets the imageDescriptor attribute of the XMLNode object
    *
    * @param object  Description of the Parameter
    * @return        The imageDescriptor value
    */
   public ImageDescriptor getImageDescriptor(Object object)
   {
      return null;
   }

   /**
    * Gets the label attribute of the XMLNode object
    *
    * @param o  Description of the Parameter
    * @return   The label value
    */
   public String getLabel(Object o)
   {
      return getName();
   }

   /**
    * Gets the name attribute of the XMLNode object
    *
    * @return   The name value
    */
   public String getName()
   {
      String name = "<unknown type>";//$NON-NLS-1$

      if (getType().equals(XMLPartitionScanner.XML_TEXT))
      {
         return "#TEXT";//$NON-NLS-1$
      }
      else if (getType().equals(XMLPartitionScanner.XML_TAG))
      {
         return getTagName();
      }
      else if (getType().equals(XMLPartitionScanner.XML_PI))
      {
         return getTagName();
      }
      else if (getType().equals(XMLPartitionScanner.XML_ATTRIBUTE))
      {
         return getAttributeName();
      }
      else if (getType().equals(XMLPartitionScanner.XML_COMMENT))
      {
         return "#COMMENT";//$NON-NLS-1$
      }
      else if (getType().equals(XMLPartitionScanner.XML_DECL))
      {
         return getTagName();
      }
      else if (getType().equals(XMLPartitionScanner.XML_END_TAG))
      {
         return getTagName();
      }
      else if (getType().equals(XMLPartitionScanner.XML_EMPTY_TAG))
      {
         return getTagName();
      }

      return name;
   }

   /**
    * Gets the parent attribute of the XMLNode object
    *
    * @return   The parent value
    */
   public XMLNode getParent()
   {
      return parent;
   }

   /**
    * Gets the parent attribute of the XMLNode object
    *
    * @param o  Description of the Parameter
    * @return   The parent value
    */
   public Object getParent(Object o)
   {
      return this;
   }

   /**
    * Gets the stateAt attribute of the XMLNode object
    *
    * @param offset  Description of the Parameter
    * @return        The stateAt value
    */
   public int getStateAt(int offset)
   {
      String content = null;
      int state = TAG;

      try
      {
         content = document.get(getOffset(), offset - getOffset());
      }
      catch (BadLocationException e)
      {
         e.printStackTrace();
         return TAG;
      }

      if (getName() == null)
      {
         return TAG;
      }

      for (int i = getName().length(); i < content.length(); i++)
      {
         char c = content.charAt(i);
         switch (c)
         {
            case '=' :
               if (state == AFTER_ATTRIBUTE || state == ATTRIBUTE)
               {
                  state = ATT_VALUE;
               }
               break;
            case '"' :
               if (state == DOUBLEQUOTE)
               {
                  state = AFTER_ATT_VALUE;
               }
               else
               {
                  state = DOUBLEQUOTE;
               }
               break;
            case '\'' :
               if (state == SINGLEQUOTE)
               {
                  state = AFTER_ATT_VALUE;
               }
               else
               {
                  state = SINGLEQUOTE;
               }
               break;
            default :
               if (Character.isWhitespace(c))
               {
                  switch (state)
                  {
                     case TAG :
                        state = ATTRIBUTE;
                        break;
                     case ATTR :
                        state = AFTER_ATTRIBUTE;
                        break;
                     case AFTER_ATT_VALUE :
                        state = ATTRIBUTE;
                        break;
                  }
               }
         }
      }

      return state;
   }

   /**
    * Gets the value attribute of the XMLNode object
    *
    * @return   The value value
    */
   public String getValue()
   {
      String name = "<unknown type>";//$NON-NLS-1$

      if (getType().equals(XMLPartitionScanner.XML_ATTRIBUTE))
      {
         return getAttributeValue();
      }

      if (getType().equals(XMLPartitionScanner.XML_TEXT))
      {
      }
      else if (getType().equals(XMLPartitionScanner.XML_TAG))
      {
         if (children.size() > 0 && ((XMLNode) children.get(0)).getType().equals(XMLPartitionScanner.XML_TEXT))
         {
            name = ((XMLNode) children.get(0)).getContent().trim();
         }
      }
      else if (getType().equals(XMLPartitionScanner.XML_PI))
      {
      }
      else if (getType().equals(XMLPartitionScanner.XML_ATTRIBUTE))
      {
         return getAttributeValue();
      }
      else if (getType().equals(XMLPartitionScanner.XML_COMMENT))
      {
      }
      else if (getType().equals(XMLPartitionScanner.XML_DECL))
      {
      }
      else if (getType().equals(XMLPartitionScanner.XML_END_TAG))
      {
      }
      else if (getType().equals(XMLPartitionScanner.XML_EMPTY_TAG))
      {
      }

      return name;
   }

   /**
    * Gets the added attribute of the XMLNode object
    *
    * @return   The added value
    */
   public boolean isAdded()
   {
      return added;
   }

   /**
    * Gets the modified attribute of the XMLNode object
    *
    * @return   The modified value
    */
   public boolean isModified()
   {
      return modified;
   }

   /**
    * Description of the Method
    *
    * @param child  Description of the Parameter
    */
   public synchronized void removeChild(XMLNode child)
   {
      children.remove(child);
   }

   /**
    * Sets the added attribute of the XMLNode object
    *
    * @param b  The new added value
    */
   public void setAdded(boolean b)
   {
      added = b;
   }

   /**
    * Sets the correspondingNode attribute of the XMLNode object
    *
    * @param node  The new correspondingNode value
    */
   public void setCorrespondingNode(XMLNode node)
   {
      correspondingNode = node;
   }

   /**
    * Sets the document attribute of the XMLNode object
    *
    * @param document  The new document value
    */
   public void setDocument(IDocument document)
   {
      this.document = document;
   }

   /**
    * Sets the length attribute of the XMLNode object
    *
    * @param length  The new length value
    */
   public void setLength(int length)
   {
      super.setLength(length);
      added = false;
      modified = true;
   }

   /**
    * Sets the modified attribute of the XMLNode object
    *
    * @param b  The new modified value
    */
   public void setModified(boolean b)
   {
      modified = b;
   }

   /**
    * Sets the offset attribute of the XMLNode object
    *
    * @param offset  The new offset value
    */
   public void setOffset(int offset)
   {
      super.setOffset(offset);
      added = false;
      modified = true;
   }

   /**
    * Sets the parent attribute of the XMLNode object
    *
    * @param node  The new parent value
    */
   public void setParent(XMLNode node)
   {
      parent = node;
      if (parent != null && !parent.getChildren().contains(this))
      {
         parent.addChild(this);
      }
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      String s = super.toString();
      s += "[";//$NON-NLS-1$
      s += "name=" + getName() + ";";//$NON-NLS-1$ //$NON-NLS-2$
      s += "type=" + getType() + ";";//$NON-NLS-1$ //$NON-NLS-2$
      s += "content=" + getContent() + ";";//$NON-NLS-1$ //$NON-NLS-2$
      s += "isDeleted=" + isDeleted() + ";";//$NON-NLS-1$ //$NON-NLS-2$
      s += "[" + getOffset() + "," + getLength() + "]";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      s += "]";//$NON-NLS-1$

      return s;
   }

   /**
    * Gets the attributeName attribute of the XMLNode object
    *
    * @return   The attributeName value
    */
   private String getAttributeName()
   {
      String content = null;
      int index = 0;

      try
      {
         content = document.get(getOffset(), getLength());
         index = content.indexOf("=");//$NON-NLS-1$
         if (index == -1)
         {
            index = content.indexOf("\"");//$NON-NLS-1$

            if (index == -1)
            {
               index = content.indexOf("'");//$NON-NLS-1$
               if (index == -1)
               {
                  index = content.length();
               }
            }
         }
      }
      catch (BadLocationException e)
      {
         e.printStackTrace();
      }

      return content.substring(0, index).trim();
   }

   /**
    * Gets the attributeValue attribute of the XMLNode object
    *
    * @return   The attributeValue value
    */
   private String getAttributeValue()
   {
      String content = null;
      int index = 0;

      try
      {
         content = document.get(getOffset(), getLength());
         index = content.indexOf("\"");//$NON-NLS-1$

         if (index == -1)
         {
            index = content.indexOf("'");//$NON-NLS-1$
            if (index == -1)
            {
               return "";//$NON-NLS-1$
            }
         }
      }
      catch (BadLocationException e)
      {
         e.printStackTrace();
      }

      content = content.substring(index).trim();

      return content.substring(1, content.length() - 1);
   }

   /**
    * Gets the tagName attribute of the XMLNode object
    *
    * @return   The tagName value
    */
   private String getTagName()
   {
      String content = null;
      String name = null;

      try
      {
         content = document.get(getOffset(), getLength());
      }
      catch (BadLocationException e)
      {
         return null;
      }

      StringTokenizer st = new StringTokenizer(content, " \t\n\r<>/");//$NON-NLS-1$

      if (st.hasMoreTokens())
      {
         name = st.nextToken();
      }

      if (name == null)
      {
         name = "";//$NON-NLS-1$
      }

      return name;
   }

}
