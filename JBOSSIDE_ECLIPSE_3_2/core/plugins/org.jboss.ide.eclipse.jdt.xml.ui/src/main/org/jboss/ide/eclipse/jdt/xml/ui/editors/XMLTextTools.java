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

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.jboss.ide.eclipse.jdt.ui.text.AbstractTextTools;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.DeclScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.SingleTokenScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.TextScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLCDATAScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLTagScanner;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @version   $Revision$
 */
public class XMLTextTools extends AbstractTextTools
{

   /** The DTD text scanner */
   private TextScanner dtdTextScanner;

   /** The XML attributes scanner */
   private TextScanner xmlAttributeScanner;

   /** The XML CDATA sections scanner */
   private XMLCDATAScanner xmlCDATAScanner;

   /** The XML comments scanner */
   private SingleTokenScanner xmlCommentScanner;

   /** The XML declarations scanner */
   private DeclScanner xmlDeclScanner;

   /** The XML processing instructions scanner */
   private SingleTokenScanner xmlPIScanner;

   /** The XML partitions scanner */
   private XMLPartitionScanner xmlPartitionScanner;

   /** The XML tags scanner */
   private XMLTagScanner xmlTagScanner;

   /** The XML text scanner */
   private TextScanner xmlTextScanner;

   /** Text Attributes for XML editors */
   private final static String[] TOKENS =
   {//
   IXMLSyntaxConstants.XML_DEFAULT, //
         IXMLSyntaxConstants.XML_TAG, //
         IXMLSyntaxConstants.XML_ATT_NAME, //
         IXMLSyntaxConstants.XML_ATT_VALUE, //
         IXMLSyntaxConstants.XML_ENTITY, //
         IXMLSyntaxConstants.XML_PI, //
         IXMLSyntaxConstants.XML_CDATA, IXMLSyntaxConstants.XML_COMMENT, //
         IXMLSyntaxConstants.XML_DECL,//
   };

   /** Content types for XML editors */
   private final static String[] TYPES =
   {//
   XMLPartitionScanner.XML_ATTRIBUTE, //
         XMLPartitionScanner.XML_CDATA, //
         XMLPartitionScanner.XML_COMMENT, //
         XMLPartitionScanner.XML_DECL, //
         XMLPartitionScanner.XML_EMPTY_TAG, //
         XMLPartitionScanner.XML_END_DECL, //
         XMLPartitionScanner.XML_END_TAG, //
         XMLPartitionScanner.XML_PI, //
         XMLPartitionScanner.XML_START_DECL, //
         XMLPartitionScanner.XML_TAG, //
         XMLPartitionScanner.XML_TEXT, //
         XMLPartitionScanner.DTD_INTERNAL, //
         XMLPartitionScanner.DTD_INTERNAL_PI, //
         XMLPartitionScanner.DTD_INTERNAL_COMMENT, //
         XMLPartitionScanner.DTD_INTERNAL_DECL,//
   };

   /**
    * Creates a new XML text tools collection.
    *
    * @param store  Description of the Parameter
    */
   public XMLTextTools(IPreferenceStore store)
   {
      super(store, TOKENS);

      Map tokens = this.getTokens();

      xmlPartitionScanner = new XMLPartitionScanner();
      xmlTextScanner = new TextScanner(tokens, '&', IXMLSyntaxConstants.XML_DEFAULT);
      dtdTextScanner = new TextScanner(tokens, '%', IXMLSyntaxConstants.XML_DEFAULT);
      xmlPIScanner = new SingleTokenScanner(tokens, IXMLSyntaxConstants.XML_PI);
      xmlCommentScanner = new SingleTokenScanner(tokens, IXMLSyntaxConstants.XML_COMMENT);
      xmlDeclScanner = new DeclScanner(tokens);
      xmlTagScanner = new XMLTagScanner(tokens);
      xmlAttributeScanner = new TextScanner(tokens, '&', IXMLSyntaxConstants.XML_ATT_VALUE);
      xmlCDATAScanner = new XMLCDATAScanner(tokens);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public IDocumentPartitioner createXMLPartitioner()
   {
      return new XMLDocumentPartitioner(xmlPartitionScanner, TYPES);
   }

   /**
    * Returns a scanner which is configured to scan DTD text.
    *
    * @return   an DTD text scanner
    */
   public RuleBasedScanner getDTDTextScanner()
   {
      return dtdTextScanner;
   }

   /**
    * Gets the preferenceStore attribute of the XMLTextTools object
    *
    * @return   The preferenceStore value
    */
   public IPreferenceStore getPreferenceStore()
   {
      return this.store;
   }

   /**
    * Returns a scanner which is configured to scan XML tag attributes.
    *
    * @return   an XML tag attribute scanner
    */
   public RuleBasedScanner getXMLAttributeScanner()
   {
      return xmlAttributeScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML CDATA sections.
    *
    * @return   an XML CDATA section scanner
    */
   public ITokenScanner getXMLCDATAScanner()
   {
      return xmlCDATAScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML comments.
    *
    * @return   an XML comment scanner
    */
   public RuleBasedScanner getXMLCommentScanner()
   {
      return xmlCommentScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML declarations.
    *
    * @return   an XML declaration scanner
    */
   public RuleBasedScanner getXMLDeclScanner()
   {
      return xmlDeclScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML
    * processing instructions.
    *
    * @return   an XML processing instruction scanner
    */
   public RuleBasedScanner getXMLPIScanner()
   {
      return xmlPIScanner;
   }

   /**
    * Gets the xMLPartitionScanner attribute of the XMLTextTools object
    *
    * @return   The xMLPartitionScanner value
    */
   public IPartitionTokenScanner getXMLPartitionScanner()
   {
      return xmlPartitionScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML tags.
    *
    * @return   an XML tag scanner
    */
   public RuleBasedScanner getXMLTagScanner()
   {
      return xmlTagScanner;
   }

   /**
    * Returns a scanner which is configured to scan XML text.
    *
    * @return   an XML text scanner
    */
   public RuleBasedScanner getXMLTextScanner()
   {
      return xmlTextScanner;
   }

}
