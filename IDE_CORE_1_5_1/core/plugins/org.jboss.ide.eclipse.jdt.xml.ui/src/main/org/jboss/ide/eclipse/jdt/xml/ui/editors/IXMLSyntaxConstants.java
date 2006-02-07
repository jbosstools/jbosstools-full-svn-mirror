/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
