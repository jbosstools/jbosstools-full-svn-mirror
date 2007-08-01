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
package org.jboss.ide.eclipse.jdt.xml.ui.text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.jboss.ide.eclipse.jdt.ui.editors.I18NDocumentProvider;
import org.jboss.ide.eclipse.jdt.ui.text.rules.WhitespaceDetector;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class AbstractDocumentProvider extends I18NDocumentProvider
{
   /** Description of the Field */
   protected IWhitespaceDetector detector = new WhitespaceDetector();

   /**
    * Try to guess the encoding of the given stream.
    *
    * @param in               The stream to test
    * @return                 The declaredEncoding value
    * @exception IOException  Description of the Exception
    */
   public String getDeclaredEncoding(InputStream in) throws IOException
   {
      if (!in.markSupported())
      {
         in = new BufferedInputStream(in, 512);
      }

      in.mark(512);
      String encoding = super.getDeclaredEncoding(in);
      if (encoding != null)
      {
         return encoding;
      }
      in.reset();

      // check Prolog-Start <?xml
      if (!this.skipXMLDecl(in))
      {
         return null;
      }

      // detect 'encoding'
      this.skipEncoding(in);

      // read encoding
      int delimiter;

      while (true)
      {
         int ch = in.read();
         if (ch < 0)
         {
            return null;
         }

         if (this.detector.isWhitespace((char) ch))
         {
            continue;
         }

         if (ch == '"' || ch == '\'')
         {
            delimiter = ch;
            break;
         }

         return null;
      }

      StringBuffer buf = new StringBuffer();

      while (true)
      {
         int ch = in.read();
         if (ch < 0)
         {
            return null;
         }

         if (ch == delimiter)
         {
            break;
         }

         buf.append((char) ch);
      }

      return buf.toString();
   }

   /**
    * Default encoding is UTF-8
    *
    * @return   The defaultEncoding value
    */
   public String getDefaultEncoding()
   {
      return "UTF-8";//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param in               Description of the Parameter
    * @return                 Description of the Return Value
    * @exception IOException  Description of the Exception
    */
   private boolean skipEncoding(InputStream in) throws IOException
   {
      int ch = in.read();

      boolean whitespace = false;

      while (true)
      {
         if (ch < 0)
         {
            return false;
         }

         if (detector.isWhitespace((char) ch))
         {
            ch = in.read();
            whitespace = true;
            continue;
         }

         if (ch == '?' || ch == '<')
         {
            return false;
         }

         if (ch != 'e')
         {
            ch = in.read();
            whitespace = false;
            continue;
         }

         if (!whitespace)
         {
            ch = in.read();
            continue;
         }

         if ((ch = in.read()) == 'n' && (ch = in.read()) == 'c' && (ch = in.read()) == 'o' && (ch = in.read()) == 'd'
               && (ch = in.read()) == 'i' && (ch = in.read()) == 'n' && (ch = in.read()) == 'g')
         {
            break;
         }

         whitespace = false;
      }

      // '='
      while (true)
      {
         ch = in.read();
         if (ch < 0)
         {
            return false;
         }

         if (detector.isWhitespace((char) ch))
         {
            continue;
         }

         if (ch == '=')
         {
            break;
         }

         return false;
      }

      return true;
   }

   /**
    * Description of the Method
    *
    * @param in               Description of the Parameter
    * @return                 Description of the Return Value
    * @exception IOException  Description of the Exception
    */
   private boolean skipXMLDecl(InputStream in) throws IOException
   {
      int ch = in.read();
      if (ch != '<')
      {
         return false;
      }

      ch = in.read();
      if (ch != '?')
      {
         return false;
      }

      ch = in.read();
      if (ch != 'x')
      {
         return false;
      }

      ch = in.read();
      if (ch != 'm')
      {
         return false;
      }

      ch = in.read();
      if (ch != 'l')
      {
         return false;
      }

      return true;
   }
}
