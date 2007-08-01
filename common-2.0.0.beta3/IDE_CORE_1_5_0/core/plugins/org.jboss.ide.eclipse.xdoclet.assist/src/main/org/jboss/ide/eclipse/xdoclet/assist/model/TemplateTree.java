/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.jface.util.ListenerList;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.ConditionTree;
import org.jboss.ide.eclipse.xdoclet.assist.model.conditions.IConditionTreeListener;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplateTree implements IConditionTreeListener, Serializable
{
   /** Description of the Field */
   protected String conditionDescription;
   /** Description of the Field */
   protected ConditionTree conditionTree;// Not for persistence
   /** Description of the Field */
   protected boolean deleted = false;
   /** Description of the Field */
   protected DocletTree docletTree;
   /** Description of the Field */
   protected String helptext;
   /** Description of the Field */
   protected long id;
   /** Description of the Field */
   protected SortedKeyTree keyTree;

   /** Description of the Field */
   protected ListenerList listenerList = new ListenerList();
   /** Description of the Field */
   protected String name;

   /** Description of the Field */
   protected TemplateElement rootElement;

   /** Description of the Field */
   protected final static int ADDED = 0;
   /** Description of the Field */
   protected final static int REMOVED = 1;
   /** Description of the Field */
   protected final static int TREE_CHANGED = 2;


   /**
    * Constructor for TemplateTree.
    *
    * @param name        Description of the Parameter
    * @param docletTree  Description of the Parameter
    * @param id          Description of the Parameter
    */
   public TemplateTree(String name, DocletTree docletTree, long id)
   {
      super();
      if (name == null || docletTree == null)
      {
         throw new IllegalArgumentException();
      }
      this.name = name;
      this.docletTree = docletTree;
      rootElement = new TemplateElement(docletTree.getRoot(), this);
      this.id = id;
      this.conditionTree = new ConditionTree(null);
      this.conditionTree.addConditionListener(this);
   }


   /**
    * Adds a feature to the Element attribute of the TemplateTree object
    *
    * @param docletElement  The feature to be added to the Element attribute
    * @return               Description of the Return Value
    */
   public TemplateElement addElement(DocletElement docletElement)
   {
      if (!docletTree.isNode(docletElement.getNode().getPath(), false))
      {
         throw new IllegalArgumentException();
      }

      // The attribute-value-relationship is 1:1. Therefore if a value is added remove any prexisting value
      if (docletElement.getType() == DocletType.VALUE_WITHOUT_VARIABLE
            || docletElement.getType() == DocletType.VALUE_WITH_VARIABLE)
      {
         TemplateElement parent =
               (TemplateElement) docletElement.getParent().getNode().getObject(
               getKey());
         if (parent != null)
         {
            // There can be only zero or one child
            if (parent.hasChildrenElements())
            {
               parent.getChildrenElements()[0].getNode().removeObject(getKey());
            }
         }
      }

      TemplateElement templateElement = new TemplateElement(docletElement, this);

      // if discrete attribute add value and vice versa

      if (docletElement.getType() == DocletType.DISCRETE_ATTRIBUTE)
      {
         //			new TemplateElement(
         //				docletElement.getChildrenElements(getContextTree())[0],
         //				this);
         new TemplateElement(docletElement.getChildrenElements()[0], this);
      }

      // Go through parent-hierarchy.
      // Find highest parent
      DocletElement parentDoclet = docletElement;
      TemplateElement parentTemplate = templateElement;

      while ((parentDoclet = parentDoclet.getParent()) != null
            && (parentTemplate = parentTemplate.getParent()) == null)
      {
         parentTemplate = new TemplateElement(parentDoclet, this);
      }

      fireChange(templateElement, ADDED);
      return templateElement;
   }


   /**
    * Method addTemplateTreeListener.
    *
    * @param templateTreeListener  The feature to be added to the TemplateTreeListener attribute
    */
   public void addTemplateTreeListener(ITemplateTreeListener templateTreeListener)
   {
      listenerList.add(templateTreeListener);
   }


   /**
    * Description of the Method
    *
    * @param element       Description of the Parameter
    * @param deleteNotAdd  Description of the Parameter
    */
   public void changeTemplateTree(Object element, boolean deleteNotAdd)
   {
      if (element instanceof TemplateElement)
      {
         if (deleteNotAdd)
         {
            removeTemplateElement(((TemplateElement) element).getDocletElement());
         }
         else
         {
            throw new IllegalArgumentException();
         }
      }
      else if (element instanceof DocletElement)
      {
         if (deleteNotAdd)
         {
            throw new IllegalArgumentException();
         }
         addElement((DocletElement) element);
      }//		else if (element instanceof ContextElement) {
      //			ContextElement contextElement = (ContextElement) element;
      //			if (deleteNotAdd)
      //				getContextTree().removeElement(contextElement);
      //			else
      //				getContextTree().addElement(contextElement);
      //		}
      else
      {
         throw new IllegalArgumentException();
      }
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.conditions.IConditionTreeListener#childAdded(java.util.EventObject)
    */
   public void childAdded(EventObject event) { }


   /**
    * Description of the Method
    *
    * @param parentElement            Description of the Parameter
    * @param onlyNotExistingElements  Description of the Parameter
    * @return                         Description of the Return Value
    */
   public Object[] computeChildren(
         Object parentElement,
         boolean onlyNotExistingElements)
   {
      ArrayList objects = new ArrayList();
      if (parentElement == null)
      {
         //			internalComputeChildren(
         //				objects,
         //				rootElement,
         //				getDocletTree().getChildrenElements(getContextTree()),
         //				onlyNotExistingElements);
         internalComputeChildren(
               objects,
               rootElement,
               getDocletTree().getChildrenElements(),
               onlyNotExistingElements);
      }
      else if (parentElement instanceof TemplateElement)
      {
         TemplateElement templateElement = (TemplateElement) parentElement;
         //			internalComputeChildren(
         //				objects,
         //				templateElement,
         //				templateElement.getDocletElement().getChildrenElements(
         //					getContextTree()),
         //				onlyNotExistingElements);
         internalComputeChildren(
               objects,
               templateElement,
               templateElement.getDocletElement().getChildrenElements(),
               onlyNotExistingElements);
      }
      // 		else if (parentElement instanceof ContextTree) {
      //			ContextTree contextTree = (ContextTree) parentElement;
      //			ContextElement[] contextElements = completeContextTree.getElements();
      //			for (int i = 0; i < contextElements.length; i++) {
      //				if (!onlyNotExistingElements
      //					|| !(contextTree.hasElement(contextElements[i])))
      //					objects.add(contextElements[i]);
      //			}
      //		} else if (!(parentElement instanceof ContextElement)) {
      //			throw new IllegalArgumentException();
      //		}
      return objects.toArray();
   }


   /** Description of the Method */
   public void deleteTree()
   {
      removeTemplateElements();
      rootElement.node.removeObject(getKey());
      deleted = true;
   }


   /**
    * Gets the allDocletElements attribute of the TemplateTree object
    *
    * @return   The allDocletElements value
    */
   public DocletElement[] getAllDocletElements()
   {
      ArrayList list = new ArrayList();
      getTemplateElements(rootElement, list);
      DocletElement[] docletElements = new DocletElement[list.size()];
      for (int i = 0; i < docletElements.length; i++)
      {
         docletElements[i] = ((TemplateElement) list.get(i)).getDocletElement();
      }
      return docletElements;
   }


   /**
    * Gets the child attribute of the TemplateTree object
    *
    * @param name  Description of the Parameter
    * @return      The child value
    */
   public TemplateElement getChild(String name)
   {
      return rootElement.getChild(name);
   }


   /**
    * Gets the childrenCount attribute of the TemplateTree object
    *
    * @return   The childrenCount value
    */
   public int getChildrenCount()
   {
      return rootElement.getChildrenCount();
   }

   // pass-through root

   /**
    * Gets the childrenElements attribute of the TemplateTree object
    *
    * @return   The childrenElements value
    */
   public TemplateElement[] getChildrenElements()
   {
      return rootElement.getChildrenElements();
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
    * Returns the docletTree.
    *
    * @return   DocletTree
    */
   public DocletTree getDocletTree()
   {
      return docletTree;
   }


   /**
    * Returns the helptext.
    *
    * @return   String
    */
   public String getHelptext()
   {
      return helptext;
   }


   /**
    * Returns the id.
    *
    * @return   long
    */
   public long getId()
   {
      return id;
   }


   /**
    * Gets the key attribute of the TemplateTree object
    *
    * @return   The key value
    */
   public String getKey()
   {
      return TemplateElement.class.getName() + id;
   }


   /**
    * Returns the name.
    *
    * @return   String
    */
   public String getName()
   {
      return name;
   }


   /**
    * Gets the root attribute of the TemplateTree object
    *
    * @return   The root value
    */
   public TemplateElement getRoot()
   {
      return rootElement;
   }


   /**
    * Gets the templateElement attribute of the TemplateTree object
    *
    * @param docletElement  Description of the Parameter
    * @return               The templateElement value
    */
   public TemplateElement getTemplateElement(DocletElement docletElement)
   {
      return (TemplateElement) docletElement.getNode().getObject(getKey());
   }


   /**
    * Gets the templateElements attribute of the TemplateTree object
    *
    * @param element  Description of the Parameter
    * @param list     Description of the Parameter
    */
   public void getTemplateElements(TemplateElement element, ArrayList list)
   {
      if (!element.hasChildrenElements())
      {
         return;
      }
      TemplateElement[] children = element.getChildrenElements();
      for (int i = 0; i < children.length; i++)
      {
         //			if (children[i].getDocletElement().belongsToContext(contextTree)) {
         //				list.add(element.getChildrenElements()[i]);
         //				getTemplateElements(
         //					element.getChildrenElements()[i],
         //					list,
         //					contextTree);
         //			}
         list.add(element.getChildrenElements()[i]);
         getTemplateElements(element.getChildrenElements()[i], list);
      }
      return;
   }


   /**
    * Gets the templateTreeListener attribute of the TemplateTree object
    *
    * @return   The templateTreeListener value
    */
   public ITemplateTreeListener[] getTemplateTreeListener()
   {
      return (ITemplateTreeListener[]) Arrays.asList(listenerList.getListeners())
            .toArray(new ITemplateTreeListener[listenerList.size()]);
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasChildrenElements()
   {
      return rootElement.hasChildrenElements();
   }


   /**
    * Returns the deleted.
    *
    * @return   boolean
    */
   public boolean isDeleted()
   {
      return deleted;
   }


   /**
    * Description of the Method
    *
    * @param docletElement  Description of the Parameter
    * @return               Description of the Return Value
    */
   public boolean removeTemplateElement(DocletElement docletElement)
   {
      if (docletElement == null
            || !docletTree.isNode(docletElement.getNode().getPath(), false))
      {
         throw new IllegalArgumentException();
      }
      if (getTemplateElement(docletElement) == null)
      {
         return false;
      }
      if (docletElement.getType() == DocletType.VALUE_WITHOUT_VARIABLE
            || docletElement.getType() == DocletType.VALUE_WITH_VARIABLE)
      {
         if (docletElement.getParent().getType() == DocletType.DISCRETE_ATTRIBUTE)
         {
            docletElement = docletElement.getParent();
         }
      }
      removeTemplateElements(getTemplateElement(docletElement));
      fireChange(getTemplateElement(docletElement), REMOVED);
      return true;
   }


   /** Description of the Method */
   public void removeTemplateElements()
   {
      removeTemplateElements(rootElement);
   }


   /**
    * Method removeTemplateTreeListener.
    *
    * @param templateTreeListener  Description of the Parameter
    */
   public void removeTemplateTreeListener(ITemplateTreeListener templateTreeListener)
   {
      listenerList.remove(templateTreeListener);
   }


   /**
    * @param event  Description of the Parameter
    * @see          org.jboss.ide.eclipse.xdoclet.model.conditions.IConditionTreeListener#removed(java.util.EventObject)
    */
   public void removed(EventObject event) { }


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
    * Sets the contextTree.
    *
    * @param conditionTree  The new conditionTree value
    */
   public void setConditionTree(ConditionTree conditionTree)
   {
      this.conditionTree.removeConditionListener(this);
      if (conditionTree == null)
      {
         conditionTree = new ConditionTree(null);
      }
      this.conditionTree = conditionTree;
      conditionTree.addConditionListener(this);
      adaptToContextTree();
   }


   /**
    * Sets the helptext.
    *
    * @param helptext  The helptext to set
    */
   public void setHelptext(String helptext)
   {
      this.helptext = helptext;
   }


   /**
    * Sets the name.
    *
    * @param name  The name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }


   /**
    * @return   Description of the Return Value
    * @see      java.lang.Object#toString()
    */
   public String toString()
   {
      return name;
   }


   /** Description of the Method */
   protected void adaptToContextTree()
   {
      ArrayList list = new ArrayList();
      getTemplateElements(rootElement, list);
      removeTemplateElements();
      for (Iterator iter = list.iterator(); iter.hasNext(); )
      {
         TemplateElement element = (TemplateElement) iter.next();
         this.addElement(element.getDocletElement());
      }
      fireChange(null, TREE_CHANGED);
   }


   /**
    * Description of the Method
    *
    * @param objects                  Description of the Parameter
    * @param templateElement          Description of the Parameter
    * @param docletElements           Description of the Parameter
    * @param onlyNotExistingElements  Description of the Parameter
    */
   protected void internalComputeChildren(
         ArrayList objects,
         TemplateElement templateElement,
         DocletElement[] docletElements,
         boolean onlyNotExistingElements)
   {
      for (int i = 0; i < docletElements.length; i++)
      {
         if (!onlyNotExistingElements
               || templateElement.getChild(docletElements[i].getName()) == null)
         {
            objects.add(docletElements[i]);
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    */
   protected void removeTemplateElements(TemplateElement element)
   {
      TemplateElement[] children = element.getChildrenElements();
      for (int i = 0; i < children.length; i++)
      {
         removeTemplateElements(children[i]);
      }
      element.getNode().removeObject(getKey());
      return;
   }


   /**
    * Description of the Method
    *
    * @param element  Description of the Parameter
    * @param type     Description of the Parameter
    */
   private void fireChange(TemplateElement element, int type)
   {
      Object[] listeners = listenerList.getListeners();
      if (listeners != null)
      {
         TemplateEvent event = new TemplateEvent(this, null, this, element);
         for (int i = 0; i < listeners.length; i++)
         {
            ITemplateTreeListener listener = (ITemplateTreeListener) listeners[i];
            switch (type)
            {
               case ADDED:
                  listener.elementAdded(event);
                  break;
               case REMOVED:
                  listener.elementRemoved(event);
                  break;
               case TREE_CHANGED:
                  listener.treeChanged(event);
                  break;
               default:
                  break;
            }
         }
      }
   }

}
