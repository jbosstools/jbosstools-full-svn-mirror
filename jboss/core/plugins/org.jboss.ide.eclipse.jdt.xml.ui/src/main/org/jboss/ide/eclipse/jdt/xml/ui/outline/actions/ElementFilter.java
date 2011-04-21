/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.outline.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.XMLNode;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * An <code>ElementFilter</code> is a <code>ViewerFilter</code> that
 * filters out XML elements of a specific type (e.g. attributes, processing
 * instructions, markup declarations.) from a <code>StructuredViewer</code>.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @see       org.xmen.ui.ITypeConstants
 */
public class ElementFilter extends ViewerFilter
{

   /** The XML element type to filter e.g. ITypeConstants.ATTR. */
   private String fType;


   /**
    * Creates an ElementFilter that can filter out a particular
    * type of XML element.
    *
    * @param type  Description of the Parameter
    * @see         org.xmen.ui.ITypeConstants
    */
   public ElementFilter(String type)
   {
      fType = type;
   }


   /**
    * Description of the Method
    *
    * @param viewer         Description of the Parameter
    * @param parentElement  Description of the Parameter
    * @param element        Description of the Parameter
    * @return               Description of the Return Value
    */
   public boolean select(Viewer viewer, Object parentElement, Object element)
   {
      // The next line is required otherwise we get a stack overflow if we
      // filter out the element that is currently selected or it's parent element.
      viewer.setSelection(StructuredSelection.EMPTY);

      return (element instanceof XMLNode) && !((XMLNode) element).getType().equals(fType);
   }

}
