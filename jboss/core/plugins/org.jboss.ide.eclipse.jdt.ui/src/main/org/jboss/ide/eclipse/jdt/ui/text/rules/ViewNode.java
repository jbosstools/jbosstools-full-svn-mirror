/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;


/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ViewNode extends FlatNode
{
   /** Inner view of the document */
   public InnerDocumentView view;


   /**
    *Constructor for the ViewNode object
    *
    * @param type  Description of the Parameter
    */
   public ViewNode(String type)
   {
      super(type);
   }


   /*
    * @see java.lang.Object#toString()
    */
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return "ViewNode[" + type + ", " + offset + ", " + length + "]";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
}
