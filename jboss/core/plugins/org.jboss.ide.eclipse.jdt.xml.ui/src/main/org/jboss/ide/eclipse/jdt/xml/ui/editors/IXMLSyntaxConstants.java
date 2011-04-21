/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.editors;


/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IXMLSyntaxConstants
{
   /** Note: This constant is for internal use only. Clients should not use this constant. The prefix all color constants start with. */
   String PREFIX = "xml_";//$NON-NLS-1$

   /** The style key for XML text. */
   String XML_DEFAULT = PREFIX + "text";//$NON-NLS-1$

   /** The style key for XML tag names. */
   String XML_TAG = PREFIX + "tag";//$NON-NLS-1$

   /** The style key for XML attribute names. */
   String XML_ATT_NAME = PREFIX + "attribute";//$NON-NLS-1$

   /** The style key for XML attribute values. */
   String XML_ATT_VALUE = PREFIX + "string";//$NON-NLS-1$

   /** The style key for XML entities. */
   String XML_ENTITY = PREFIX + "entity";//$NON-NLS-1$

   /** The style key for XML processing instructions. */
   String XML_PI = PREFIX + "processing_instruction";//$NON-NLS-1$

   /** The style key for XML CDATA sections. */
   String XML_CDATA = PREFIX + "cdata";//$NON-NLS-1$

   /** The style key for XML comments. */
   String XML_COMMENT = PREFIX + "comment";//$NON-NLS-1$

   /** The style key for XML declaration. */
   String XML_DECL = PREFIX + "declaration";//$NON-NLS-1$

   /** The style key for external DTD conditional sections. */
   String DTD_CONDITIONAL = PREFIX + "conditional";//$NON-NLS-1$
}
