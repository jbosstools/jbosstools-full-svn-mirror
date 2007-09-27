/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors;


/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IJSPSyntaxConstants
{
   String PREFIX = "jsp_";//$NON-NLS-1$

   /** The style key for plain text in JSP. */
   String JSP_DEFAULT = PREFIX + "default";//$NON-NLS-1$

   /** The style key for JSP brackets. */
   String JSP_BRACKET = PREFIX + "bracket";//$NON-NLS-1$

   /** The style key for JSP tag names. */
   String JSP_TAG = PREFIX + "tag";//$NON-NLS-1$

   /** The style key for JSP attribute names. */
   String JSP_ATT_NAME = PREFIX + "attribute";//$NON-NLS-1$

   /** The style key for JSP attribute values. */
   String JSP_ATT_VALUE = PREFIX + "attribute-value";//$NON-NLS-1$

   /** The style key for JSP comments. */
   String JSP_COMMENT = PREFIX + "comment";//$NON-NLS-1$

   /** The style key for JSP directives. */
   String JSP_DIRECTIVE = PREFIX + "directive";//$NON-NLS-1$
}
