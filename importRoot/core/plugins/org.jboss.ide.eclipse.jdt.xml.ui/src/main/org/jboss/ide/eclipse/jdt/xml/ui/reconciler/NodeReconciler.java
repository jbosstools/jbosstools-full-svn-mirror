/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.reconciler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.ide.eclipse.jdt.xml.core.ns.Namespace;
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
public abstract class NodeReconciler implements IReconcilingStrategy, IDocumentListener
{
   /** Description of the Field */
   protected ArrayList added;
   /** Description of the Field */
   protected ArrayList deleted;
   /** Description of the Field */
   protected IDocument document;
   /** Description of the Field */
   protected Namespace dtdGrammar;
   /** Description of the Field */
   protected ITextEditor editor;
   /** Description of the Field */
   protected boolean firstTime = true;
   /** Description of the Field */
   protected Map namespaces;
   /** Description of the Field */
   protected XMLNode root;
   /** Description of the Field */
   protected boolean sendOnlyAdditions = false;
   /** Description of the Field */
   protected ArrayList storedPos;


   /**
    *Constructor for the NodeReconciler object
    *
    * @param editor  Description of the Parameter
    */
   public NodeReconciler(ITextEditor editor)
   {
      this.editor = editor;
      this.namespaces = new TreeMap();
   }


   /**
    * Adds a feature to the AttributeTo attribute of the NodeReconciler object
    *
    * @param name  The feature to be added to the AttributeTo attribute
    * @param to    The feature to be added to the AttributeTo attribute
    */
   public void addAttributeTo(String name, XMLNode to)
   {
      try
      {
         sendOnlyAdditions = true;
         document.replace(to.getOffset() + to.getLength() - 1, 0, " " + name + "=\"\"");//$NON-NLS-1$ //$NON-NLS-2$
      }
      catch (BadLocationException e)
      {
         e.printStackTrace();
      }
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @param to    Description of the Parameter
    */
   public void appendNewNodeTo(String name, XMLNode to)
   {
      try
      {
         sendOnlyAdditions = true;
         document.replace(to.getOffset() + to.getLength(), 0, "<" + name + "/>");//$NON-NLS-1$ //$NON-NLS-2$
      }
      catch (BadLocationException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param doc  Description of the Parameter
    */
   public void createTree(IDocument doc)
   {
      document = doc;
      try
      {
         Position[] pos = this.getPositions();
         Arrays.sort(pos,
            new Comparator()
            {
               public int compare(Object o1, Object o2)
               {
                  int offset1 = ((XMLNode) o1).getOffset();
                  int offset2 = ((XMLNode) o2).getOffset();
                  return (offset1 > offset2) ? 1 : ((offset1 < offset2) ? -1 : 0);
               }
            });
         root = new XMLNode(0, 0, "/", doc);//$NON-NLS-1$
         storedPos = new ArrayList();
         root.setParent(null);
         for (int i = 0; i < pos.length; i++)
         {
            storedPos.add(pos[i]);
            ((XMLNode) pos[i]).setAdded(false);
         }
         added = (ArrayList) storedPos.clone();
         deleted = new ArrayList();
         fix(pos, 0, root);
         firstTime = false;
      }
      catch (BadPositionCategoryException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void documentAboutToBeChanged(DocumentEvent event)
   {
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   public void documentChanged(DocumentEvent event)
   {
      IDocument doc = event.getDocument();
      document = doc;
      XMLNode firstAdded = null;
      try
      {
         Position[] pos = this.getPositions();
         Arrays.sort(pos,
            new Comparator()
            {
               public int compare(Object o1, Object o2)
               {
                  int offset1 = ((XMLNode) o1).getOffset();
                  int offset2 = ((XMLNode) o2).getOffset();
                  return (offset1 > offset2) ? 1 : ((offset1 < offset2) ? -1 : 0);
               }
            });
         if (root == null)
         {
            System.out.println("root is null!");//$NON-NLS-1$
         }
         else
         {
            if (deleted == null)
            {
               deleted = new ArrayList();
            }
            else
            {
               deleted.clear();
            }
            for (int i = 0; i < storedPos.size(); i++)
            {
               if (((XMLNode) storedPos.get(i)).isDeleted())
               {
                  XMLNode n = (XMLNode) storedPos.get(i);
                  deleted.add(n);
               }
            }
            this.updateTree(deleted);
            if (added == null)
            {
               added = new ArrayList();
            }
            else
            {
               added.clear();
            }
            boolean fixed = false;
            for (int i = 0; i < pos.length; i++)
            {
               XMLNode n = (XMLNode) pos[i];
               if (n.isAdded() || n.isModified())
               {
                  if (n.isAdded())
                  {
                     storedPos.add(n);
                  }
                  n.setAdded(false);
                  n.setModified(false);
                  if (!fixed)
                  {
                     firstAdded = n;
                     if (i == 0)
                     {
                        fix(pos, i, root);
                     }
                     else
                     {
                        XMLNode prev = (XMLNode) pos[i - 1];

                        if (XMLPartitionScanner.XML_TAG.equals(prev.getType()))
                        {
                           fix(pos, i, prev);
                        }
                        else
                        {
                           fix(pos, i, prev.getParent());
                        }
                     }
                     if (XMLPartitionScanner.XML_DECL.equals(n.getType()))
                     {
                        if (n.getName().equals("!DOCTYPE")//$NON-NLS-1$
                                )
                        {
                           String dtdLocation = n.getDTDLocation();
                           if (dtdLocation != null)
                           {
                              dtdGrammar = new Namespace(null, null, dtdLocation);
                              if (!dtdGrammar.readDTD(dtdLocation))
                              {
                                 dtdGrammar = null;
                              }
                              else
                              {
                                 //editor.setDTD(dtdGrammar);
                              }
                           }
                        }
                        else if (n.getParent() != null && XMLPartitionScanner.XML_START_DECL.equals(n.getParent().getType()))
                        {
                           if (n.getParent().getCorrespondingNode() != n.getParent())
                           {
                              XMLNode start = n.getParent();
                              XMLNode end = n.getParent().getCorrespondingNode();

                              try
                              {
                                 dtdGrammar = Namespace.createDTD(doc.get(start.getOffset(), end.getOffset() + end.getLength() - start.getOffset()));
                              }
                              catch (BadLocationException e1)
                              {
                                 e1.printStackTrace();
                              }
                           }
                        }
                     }
                     else
                     {
                        fixed = true;
                     }
                  }
               }
            }
         }

         this.update();

         sendOnlyAdditions = false;
      }
      catch (BadPositionCategoryException e)
      {
         // Do nothing
      }
   }


   /**
    * Gets the dTDGrammar attribute of the NodeReconciler object
    *
    * @return   The dTDGrammar value
    */
   public Namespace getDTDGrammar()
   {
      return dtdGrammar;
   }


   /**
    * Gets the namespaces attribute of the NodeReconciler object
    *
    * @return   The namespaces value
    */
   public Map getNamespaces()
   {
      return namespaces;
   }


   /**
    * Gets the root attribute of the NodeReconciler object
    *
    * @return   The root value
    */
   public XMLNode getRoot()
   {
      return root;
   }


   /**
    * Gets the storedPos attribute of the NodeReconciler object
    *
    * @return   The storedPos value
    */
   public ArrayList getStoredPos()
   {
      return storedPos;
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @param node  Description of the Parameter
    */
   public void insertTagAfter(String name, XMLNode node)
   {
      int offset = node.getOffset() + node.getLength() + 1;

      try
      {
         document.replace(offset, 0, "<" + name + "/>");//$NON-NLS-1$ //$NON-NLS-2$
         sendOnlyAdditions = true;
      }
      catch (BadLocationException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @param to    Description of the Parameter
    */
   public void prependNewNodeTo(String name, XMLNode to)
   {
      try
      {
         sendOnlyAdditions = true;
         document.replace(to.getOffset(), 0, "<" + name + "/>");//$NON-NLS-1$ //$NON-NLS-2$
      }
      catch (BadLocationException e)
      {
         // Do nothing
      }
   }


   /**
    * Description of the Method
    *
    * @param dirtyRegion  Description of the Parameter
    * @param subRegion    Description of the Parameter
    */
   public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion)
   {
   }


   /**
    * Description of the Method
    *
    * @param partition  Description of the Parameter
    */
   public void reconcile(IRegion partition)
   {
      this.documentChanged(null);
   }


   /**
    * Sets the document attribute of the NodeReconciler object
    *
    * @param document  The new document value
    */
   public void setDocument(IDocument document)
   {
      this.document = document;
   }


   /**
    * Sets the root attribute of the NodeReconciler object
    *
    * @param node  The new root value
    */
   public void setRoot(XMLNode node)
   {
      root = node;
   }


   /**
    * Description of the Method
    *
    * @param deleted  Description of the Parameter
    */
   public void updateTree(List deleted)
   {
      for (Iterator it = deleted.iterator(); it.hasNext(); )
      {
         XMLNode node = (XMLNode) it.next();

         if (node.getType().equals(XMLPartitionScanner.XML_DECL))
         {
            if (node.getName() != null && node.getName().equals("!DOCTYPE")//$NON-NLS-1$
                    )
            {
               dtdGrammar = null;
            }
         }
         if (node.getParent() != null)
         {
            node.getParent().removeChild(node);
            node.setParent(null);
         }
         else
         {
            //System.out.println("parent not set!" + node.getType() + "|" + node.getName() + "|" + node);
         }
         storedPos.remove(node);
      }
   }


   /**
    * Description of the Method
    *
    * @param pos     Description of the Parameter
    * @param start   Description of the Parameter
    * @param parent  Description of the Parameter
    */
   protected void fix(Position[] pos, int start, XMLNode parent)
   {
      if (parent == null)
      {
         parent = root;
      }

      for (int i = start; i < pos.length; i++)
      {
         XMLNode n = (XMLNode) pos[i];

         if (n.isDeleted)
         {
            continue;
         }

         if (!XMLPartitionScanner.XML_END_TAG.equals(n.getType()) && !XMLPartitionScanner.XML_END_DECL.equals(n.getType()))
         {
            if (n.getParent() != parent)
            {
               if (n.getParent() != null)
               {
                  n.getParent().removeChild(n);
               }
               n.setParent(parent);
            }
         }
         if (XMLPartitionScanner.XML_TAG.equals(n.getType()))
         {
            List attrs = n.getAttributes();
            n.setCorrespondingNode(n);
            for (Iterator it = attrs.iterator(); it.hasNext(); )
            {
               XMLNode element = (XMLNode) it.next();
               String name = element.getName();
               if (name.indexOf("xmlns") != -1 //$NON-NLS-1$
                       )
               {
                  String value = element.getValue();
                  int index = name.indexOf(":");//$NON-NLS-1$
                  String prefix = null;
                  Namespace ns = null;

                  if (index == -1)
                  {
                     prefix = Namespace.DEFAULTNAMESPACE;
                  }
                  else
                  {
                     prefix = name.substring(index + 1);
                  }

                  ns = (Namespace) namespaces.get(prefix);

                  if (ns == null || !ns.getUri().equals(value))
                  {
                     ns = new Namespace(prefix, value, null);
                     if (!firstTime && false)
                     {
                        //if (!firstTime && XMenPlugin.getDefault().getPreferenceStore().getBoolean("LoadSchemasOnlyOnSave")) {
                        namespaces.put(prefix, ns);
                     }
                     else
                     {
                        ns.readSchema(value);
                        namespaces.put(prefix, ns);
                     }
                  }
               }
               else if ("xsi:schemaLocation".equals(name)//$NON-NLS-1$
                       )
               {
                  String value = element.getValue();
                  String uri = null;
                  String file = null;
                  Namespace found = null;

                  int space = value.indexOf(' ');
                  if (space != -1)
                  {
                     uri = value.substring(0, space);
                     file = value.substring(space + 1);
                     Iterator iter = namespaces.values().iterator();
                     while (iter.hasNext())
                     {
                        Namespace ns = (Namespace) iter.next();
                        if (uri.equals(ns.getUri()))
                        {
                           found = ns;
                           break;
                        }
                     }
                  }
                  else
                  {
                     found = (Namespace) namespaces.get(Namespace.DEFAULTNAMESPACE);
                     file = value;
                  }

                  if (found != null && file != null)
                  {
                     found.readSchema(file);
                  }
               }
               else if ("xsi:noNamespaceSchemaLocation".equals(name)//$NON-NLS-1$
                       )
               {
                  String value = element.getValue();

                  Namespace ns = (Namespace) namespaces.get(Namespace.DEFAULTNAMESPACE);
                  if (ns != null)
                  {
                     ns.readSchema(value);
                  }
               }
            }
            parent = n;
         }
         else if (XMLPartitionScanner.XML_END_TAG.equals(n.getType()))
         {
            n.setCorrespondingNode(n);
            if (parent != null)
            {
               XMLNode newParent = parent;
               while (!n.getName().equals(newParent.getName()) && newParent != root && newParent != null)
               {
                  newParent = newParent.getParent();
               }
               if (newParent != root && newParent != null)
               {
                  parent = newParent;
                  n.setCorrespondingNode(parent);
                  parent.setCorrespondingNode(n);
                  if (n.getParent() != parent.getParent())
                  {
                     if (n.getParent() != null)
                     {
                        n.getParent().removeChild(n);
                     }
                     n.setParent(parent.getParent());
                  }
                  parent = parent.getParent();
               }
               else
               {
                  n.setParent(parent);
               }
            }
         }
         else if (XMLPartitionScanner.XML_DECL.equals(n.getType()))
         {
            if (n.getName() != null && n.getName().equals("!DOCTYPE")//$NON-NLS-1$
                    )
            {
               String dtdLocation = n.getDTDLocation();
               if (dtdLocation != null)
               {
                  dtdGrammar = new Namespace(null, null, dtdLocation);
                  if (!dtdGrammar.readDTD(dtdLocation))
                  {
                     dtdGrammar = null;
                  }
                  else
                  {
                     //editor.setDTD(dtdGrammar);
                  }
               }
            }
         }
         else if (XMLPartitionScanner.XML_START_DECL.equals(n.getType()))
         {
            n.setCorrespondingNode(null);
            parent = n;
         }
         else if (XMLPartitionScanner.XML_END_DECL.equals(n.getType()))
         {
            n.setCorrespondingNode(null);
            if (parent != null)
            {
               XMLNode newParent = parent;
               while (!XMLPartitionScanner.XML_START_DECL.equals(newParent.getType()) && newParent != root && newParent != null)
               {
                  newParent = newParent.getParent();
               }
               if (newParent != root && newParent != null)
               {
                  parent = newParent;
                  n.setCorrespondingNode(parent);
                  parent.setCorrespondingNode(n);
                  try
                  {
                     dtdGrammar = Namespace.createDTD(document.get(parent.getOffset(), n.getOffset() + n.getLength() - parent.getOffset()));
                  }
                  catch (BadLocationException e1)
                  {
                     e1.printStackTrace();
                  }

                  if (n.getParent() != parent.getParent())
                  {
                     if (n.getParent() != null)
                     {
                        n.getParent().removeChild(n);
                     }
                     n.setParent(parent.getParent());
                  }
                  parent = parent.getParent();
               }
               else
               {
                  n.setParent(parent);
               }
            }
         }
      }
   }


   /**
    * Gets the positionCategory attribute of the NodeReconciler object
    *
    * @return   The positionCategory value
    */
   protected abstract String getPositionCategory();


   /**
    * Gets the positions attribute of the NodeReconciler object
    *
    * @return                                  The positions value
    * @exception BadPositionCategoryException  Description of the Exception
    */
   protected Position[] getPositions()
      throws BadPositionCategoryException
   {
      return this.document.getPositions(this.getPositionCategory());
   }


   /** Description of the Method */
   protected abstract void update();

}
