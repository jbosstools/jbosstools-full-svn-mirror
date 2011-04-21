/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text;

import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPScriptScanner;
import org.jboss.ide.eclipse.jdt.ui.text.rules.FlatNode;
import org.jboss.ide.eclipse.jdt.ui.text.rules.MultiViewPartitioner;
import org.jboss.ide.eclipse.jdt.ui.text.rules.ViewNode;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPScriptPartitioner extends MultiViewPartitioner
{
   /**
    *Constructor for the JSPScriptPartitioner object
    *
    * @param scanner  Description of the Parameter
    */
   public JSPScriptPartitioner(IPartitionTokenScanner scanner)
   {
      super(scanner);
   }


   /**
    * Description of the Method
    *
    * @param type    Description of the Parameter
    * @param offset  Description of the Parameter
    * @param length  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected FlatNode createNode(String type, int offset, int length)
   {
      if (type.equals(JSPScriptScanner.JSP_SCRIPT))
      {
         ViewNode node = new ViewNode(type);
         node.offset = offset;
         node.length = length;
         return node;
      }

      return super.createNode(type, offset, length);
   }

   /**
    * Description of the Method
    *
    * @param contentType  Description of the Parameter
    * @return             Description of the Return Value
    */
   protected IDocumentPartitioner createPartitioner(String contentType)
   {
      if (contentType == null)
      {
         return null;
      }

      if (contentType.equals(JSPScriptScanner.JSP_SCRIPT))
      {
         return JDTJ2EEJSPUIPlugin.getDefault().getJSPTextTools().getJavaTextTools().createDocumentPartitioner();
      }

      return null;
   }
}
