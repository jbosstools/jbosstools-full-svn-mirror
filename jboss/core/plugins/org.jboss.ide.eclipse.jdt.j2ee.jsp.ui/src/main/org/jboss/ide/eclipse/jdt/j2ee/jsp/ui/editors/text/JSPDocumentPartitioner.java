/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPPartitionScanner;
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
public class JSPDocumentPartitioner extends MultiViewPartitioner
{
   private String fPositionCategory;
   private IPartitionTokenScanner scriptScanner;

   /** Description of the Field */
   public final static String CONTENT_TYPES_CATEGORY = "__jsp_content_types_category";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_SCRIPLET_CODE = "__jsp_scriplet_code";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_TEMPLATE_DATA = "__jsp_template_data";//$NON-NLS-1$


   /**
    *Constructor for the JSPDocumentPartitioner object
    *
    * @param scanner        Description of the Parameter
    * @param scriptScanner  Description of the Parameter
    */
   public JSPDocumentPartitioner(IPartitionTokenScanner scanner, IPartitionTokenScanner scriptScanner)
   {
      super(scanner);

      this.scriptScanner = scriptScanner;
      fPositionCategory = CONTENT_TYPES_CATEGORY;
   }


   /**
    * Gets the managingPositionCategories attribute of the JSPDocumentPartitioner object
    *
    * @return   The managingPositionCategories value
    */
   public String[] getManagingPositionCategories()
   {
      return new String[]{fPositionCategory};
   }


   /**
    * Gets the positionCategory attribute of the JSPDocumentPartitioner object
    *
    * @return   The positionCategory value
    */
   public String getPositionCategory()
   {
      return this.getManagingPositionCategories()[0];
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
      if (type.equals(JSPPartitionScanner.JSP_DECLARATION) || type.equals(JSPPartitionScanner.JSP_EXPRESSION)
         || type.equals(JSPPartitionScanner.JSP_SCRIPLET))
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
         return JDTJ2EEJSPUIPlugin.getDefault().getJSPTextTools().getXMLTextTools().createXMLPartitioner();
      }

      if (contentType.equals(JSPPartitionScanner.JSP_DECLARATION) || contentType.equals(JSPPartitionScanner.JSP_EXPRESSION)
         || contentType.equals(JSPPartitionScanner.JSP_SCRIPLET))
      {
         return new JSPScriptPartitioner(scriptScanner);
      }

      return null;
   }


   /**
    * Gets the contentType attribute of the JSPDocumentPartitioner object
    *
    * @param parent  Description of the Parameter
    * @param view    Description of the Parameter
    * @return        The contentType value
    */
   protected String getContentType(String parent, String view)
   {
      if (parent == null)
      {
         if (view == IDocument.DEFAULT_CONTENT_TYPE)
         {
            return JSP_TEMPLATE_DATA;
         }
      }
      else
      {
         if (view == IDocument.DEFAULT_CONTENT_TYPE)
         {
            return JSP_SCRIPLET_CODE;
         }
      }

      return super.getContentType(parent, view);
   }
}
