/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Saparates content of JSP script from enclosing brackets.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPScriptScanner implements IPartitionTokenScanner
{

   private int begin;

   // Brackets: <%! <%= <%   %>

   private IDocument document;
   private int end;
   private int length;

   private int offset;

   private int position;

   private Map tokens = new HashMap();

   /** Description of the Field */
   public final static String JSP_BRACKET = "jsp_bracket";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_SCRIPT = "jsp_script";//$NON-NLS-1$


   /**
    * Gets the tokenLength attribute of the JSPScriptScanner object
    *
    * @return   The tokenLength value
    */
   public int getTokenLength()
   {
      return length;
   }


   /**
    * Gets the tokenOffset attribute of the JSPScriptScanner object
    *
    * @return   The tokenOffset value
    */
   public int getTokenOffset()
   {
      return offset;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public IToken nextToken()
   {
      offset += length;

      if (position == 0)
      {
         if (end <= 2)
         {
            position = end;// <%
         }
         else
         {
            position = 2;

            try
            {
               int ch = document.getChar(2);
               if (ch == '!' || ch == '=')
               {
                  position = 3;
               }
            }
            catch (BadLocationException e)
            {
            }
         }

         if (end == position + 2)
         {
            try
            {
               if (document.get(position, 2).equals("%>")//$NON-NLS-1$
               )
               {
                  position += 2;
               }
            }
            catch (BadLocationException e)
            {
            }
         }

         return getToken(JSP_BRACKET);
      }

      if (position == end)
      {
         return getToken(null);
      }

      int p = end - 2;
      if (p < position)
      {
         position = end;
         return getToken(JSP_SCRIPT);
      }

      try
      {
         if (position == p)
         {
            position = end;

            if (document.get(p, 2).equals("%>")//$NON-NLS-1$
            )
            {
               return getToken(JSP_BRACKET);
            }

            return getToken(JSP_SCRIPT);
         }

         position = p;
         if (!document.get(p, 2).equals("%>")//$NON-NLS-1$
         )
         {
            position = end;
         }
      }
      catch (BadLocationException e)
      {
      }

      return getToken(JSP_SCRIPT);
   }


   /**
    * Sets the partialRange attribute of the JSPScriptScanner object
    *
    * @param document         The new partialRange value
    * @param offset           The new partialRange value
    * @param length           The new partialRange value
    * @param contentType      The new partialRange value
    * @param partitionOffset  The new partialRange value
    */
   public void setPartialRange(
      IDocument document, int offset, int length,
      String contentType, int partitionOffset
      )
   {
      setRange(document, partitionOffset, length);
   }


   /**
    * Sets the range attribute of the JSPScriptScanner object
    *
    * @param document  The new range value
    * @param offset    The new range value
    * @param length    The new range value
    */
   public void setRange(IDocument document, int offset, int length)
   {
      this.document = document;
      this.begin = offset;
      this.end = Math.min(offset + length, document.getLength());

      this.offset = offset;
      this.position = offset;
      this.length = 0;
   }


   /**
    * Gets the token attribute of the JSPScriptScanner object
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
         token = new Token(type);
         tokens.put(type, token);
      }

      return token;
   }
}
