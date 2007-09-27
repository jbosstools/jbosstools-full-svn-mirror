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
package org.jboss.ide.eclipse.jdt.xml.ui.text.scanners;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.rules.XMLTagsRule;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLPartitionScanner extends RuleBasedPartitionScanner
{
   /** Description of the Field */
   public final static String DTD_CONDITIONAL = "__dtd_conditional";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DTD_INTERNAL = "__dtd_internal";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DTD_INTERNAL_COMMENT = "__dtd_internal_comment";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DTD_INTERNAL_DECL = "__dtd_internal_declaration";//$NON-NLS-1$

   /** Description of the Field */
   public final static String DTD_INTERNAL_PI = "__dtd_internal_pi";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_ATTRIBUTE = "__xml_attribute";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_CDATA = "__xml_cdata";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_COMMENT = "__xml_comment";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_DECL = "__xml_declaration";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_EMPTY_TAG = "__xml_empty_tag";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_END_DECL = "__xml_end_declaration";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_END_TAG = "__xml_end_tag";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_PI = "__xml_processing_instruction";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_START_DECL = "__xml_start_declaration";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_TAG = "__xml_tag";//$NON-NLS-1$

   /** Description of the Field */
   public final static String XML_TEXT = "__xml_text";//$NON-NLS-1$

   /**Constructor for the XMLPartitionScanner object */
   public XMLPartitionScanner()
   {
      this.setPredicateRules(new IPredicateRule[]
      {new XMLTagsRule()});
   }

   /**
    * Gets the continuationPartition attribute of the XMLPartitionScanner object
    *
    * @param document  Description of the Parameter
    * @param offset    Description of the Parameter
    * @return          The continuationPartition value
    */
   private boolean isContinuationPartition(IDocument document, int offset)
   {
      try
      {
         String type = document.getContentType(offset - 1);

         if (type != IDocument.DEFAULT_CONTENT_TYPE)
         {
            return true;
         }
      }
      catch (BadLocationException e)
      {
      }

      return false;
   }
}
