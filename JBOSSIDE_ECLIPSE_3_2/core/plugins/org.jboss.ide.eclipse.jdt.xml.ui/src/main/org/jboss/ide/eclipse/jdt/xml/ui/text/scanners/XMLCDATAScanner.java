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

import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.IXMLSyntaxConstants;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLCDATAScanner implements ITokenScanner
{

   private int begin;

   private IDocument document;

   private int end;

   private int length;

   private int offset;

   private int position;

   private Map tokens;

   /**
    *Constructor for the XMLCDATAScanner object
    *
    * @param tokens  Description of the Parameter
    */
   public XMLCDATAScanner(Map tokens)
   {
      this.tokens = tokens;
   }

   /*
    * @see org.eclipse.jface.text.rules.ITokenScanner#getTokenLength()
    */
   /**
    * Gets the tokenLength attribute of the XMLCDATAScanner object
    *
    * @return   The tokenLength value
    */
   public int getTokenLength()
   {
      return this.length;
   }

   /*
    * @see org.eclipse.jface.text.rules.ITokenScanner#getTokenOffset()
    */
   /**
    * Gets the tokenOffset attribute of the XMLCDATAScanner object
    *
    * @return   The tokenOffset value
    */
   public int getTokenOffset()
   {
      return this.offset;
   }

   /*
    * @see org.eclipse.jface.text.rules.ITokenScanner#nextToken()
    */
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public IToken nextToken()
   {
      offset += length;

      if (position == begin)
      {
         position += 3;// <![

         try
         {
            if (document.get(position, 6).equals("CDATA[")//$NON-NLS-1$
            )
            {
               position += 6;
            }
         }
         catch (BadLocationException e)
         {
         }

         return this.getToken(IXMLSyntaxConstants.XML_CDATA);
      }

      if (position == end)
      {
         return this.getToken(null);
      }

      try
      {
         int p = end - 3;
         if (document.get(p, 3).equals("]]>")//$NON-NLS-1$
         )
         {
            if (position == p)
            {
               position = end;
               return this.getToken(IXMLSyntaxConstants.XML_CDATA);
            }

            position = p;
         }
         else
         {
            position = end;
         }
      }
      catch (BadLocationException e)
      {
      }

      return this.getToken(IXMLSyntaxConstants.XML_DEFAULT);
   }

   /*
    * @see org.eclipse.jface.text.rules.ITokenScanner#setRange(IDocument, int, int)
    */
   /**
    * Sets the range attribute of the XMLCDATAScanner object
    *
    * @param document  The new range value
    * @param offset    The new range value
    * @param length    The new range value
    */
   public void setRange(IDocument document, int offset, int length)
   {
      this.document = document;

      this.begin = offset;
      this.end = offset + length;

      this.offset = offset;
      this.position = offset;
      this.length = 0;
   }

   /**
    * Gets the token attribute of the XMLCDATAScanner object
    *
    * @param type  Description of the Parameter
    * @return      The token value
    */
   private IToken getToken(String type)
   {
      length = position - offset;

      if (length == 0)
      {
         return Token.EOF;
      }

      if (type == null)
      {
         return Token.UNDEFINED;
      }

      IToken token = (IToken) tokens.get(type);
      if (token == null)
      {
         return Token.UNDEFINED;
      }

      return token;
   }
}
