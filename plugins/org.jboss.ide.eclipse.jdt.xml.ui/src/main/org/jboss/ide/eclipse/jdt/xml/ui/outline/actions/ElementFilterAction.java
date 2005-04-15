/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.outline.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 *	This class is designed to simplify applying and removing filters from a
 *	structured viewer.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ElementFilterAction extends Action
{
   private ViewerFilter fFilter;

   private StructuredViewer fViewer;


   /**
    *Constructor for the ElementFilterAction object
    *
    * @param viewer  Description of the Parameter
    * @param filter  Description of the Parameter
    * @param text    Description of the Parameter
    */
   public ElementFilterAction(StructuredViewer viewer, ViewerFilter filter, String text)
   {
      super(text, AS_CHECK_BOX);
      fViewer = viewer;
      fFilter = filter;
   }


   /** Main processing method for the ElementFilterAction object */
   public void run()
   {
      if (isChecked())
      {
         fViewer.addFilter(fFilter);
      }
      else
      {
         fViewer.removeFilter(fFilter);
      }
   }

}
