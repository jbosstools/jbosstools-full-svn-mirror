/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.ui.util;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Simple sorter for viewer which compares the String representation
 * for both objects.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class StringViewSorter extends ViewerSorter
{
   /** Default constructor */
   public StringViewSorter()
   {
      super();
   }


   /**
    * Comparison of the String representation for both objects.
    *
    * @param viewer  The viewer that request the comparison
    * @param e1      First object
    * @param e2      Second object
    * @return        Return -1, 0 or 1 depending on the comparison result
    * @see           org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
    */
   public int compare(Viewer viewer, Object e1, Object e2)
   {
      String s1 = e1.toString();
      String s2 = e2.toString();
      return s1.compareTo(s2);
   }
}
