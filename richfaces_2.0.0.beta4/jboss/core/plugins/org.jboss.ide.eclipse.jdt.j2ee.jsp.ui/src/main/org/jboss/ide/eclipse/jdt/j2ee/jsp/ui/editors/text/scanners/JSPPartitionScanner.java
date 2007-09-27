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
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPPartitionScanner implements IPartitionTokenScanner
{
   private int begin;

   private IDocument document;
   private int end;
   private int length;

   private int offset;

   private int position;
   private int state;

   private Map tokens = new HashMap();

   /** Description of the Field */
   public final static String JSP_COMMENT = "__jsp_comment";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_DECLARATION = "__jsp_declaration";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_DIRECTIVE = "__jsp_directive";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_EXPRESSION = "__jsp_expression";//$NON-NLS-1$
   /** Description of the Field */
   public final static String JSP_SCRIPLET = "__jsp_scriplet";//$NON-NLS-1$

   /** Description of the Field */
   public final static int STATE_DEFAULT = 0;
   /** Description of the Field */
   public final static int STATE_SCRIPT = 2;
   /** Description of the Field */
   public final static int STATE_TAG = 1;


   /**Constructor for the JSPPartitionScanner object */
   public JSPPartitionScanner() { }


   /**
    * Gets the tokenLength attribute of the JSPPartitionScanner object
    *
    * @return   The tokenLength value
    */
   public int getTokenLength()
   {
      return length;
   }


   /**
    * Gets the tokenOffset attribute of the JSPPartitionScanner object
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

      /*
       * switch (state) {
       * case STATE_TAG:
       * return nextTagToken();
       * }
       */
      switch (read())
      {
         case ICharacterScanner.EOF:
            state = STATE_DEFAULT;
            return getToken(null);
         case '<':
            switch (read())
            {
               case ICharacterScanner.EOF:
                  state = STATE_DEFAULT;
                  return getToken(null);
               case '%':// <%SCRIPLET <%@DIRECTIVE <%!DECLARATION <%=EXPRESSION <%--COMMENT
                  switch (read())
                  {
                     case ICharacterScanner.EOF:
                        state = STATE_DEFAULT;
                        return getToken(JSP_SCRIPLET);
                     case '-':// <%-  <%--COMMENT
                        switch (read())
                        {
                           case ICharacterScanner.EOF:
                           case '-':// <%--
                              return nextCommentToken();
                        }

                        break;
                     case '@':// <%@
                        return nextJSPToken(JSP_DIRECTIVE);
                     case '!':// <%!
                        return nextJSPToken(JSP_DECLARATION);
                     case '=':// <%=
                        return nextJSPToken(JSP_EXPRESSION);
                  }

                  return nextJSPToken(JSP_SCRIPLET);
            }

            unread();
      }

      loop :
      while (true)
      {
         switch (read())
         {
            case ICharacterScanner.EOF:
               state = STATE_DEFAULT;
               return getToken(null);
            case '<':
               switch (read())
               {
                  case ICharacterScanner.EOF:
                     state = STATE_DEFAULT;
                     return getToken(null);
                  case '%':
                     unread();
                     break;
                  case '<':
                     unread();

                  default:
                     continue loop;
               }

               unread();

               state = STATE_DEFAULT;
               return getToken(null);
         }
      }
   }


   /*
    * @see org.eclipse.jface.text.rules.IPartitionTokenScanner
    */
   /**
    * Sets the partialRange attribute of the JSPPartitionScanner object
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
      state = STATE_DEFAULT;
      setRange(document, partitionOffset, length);
   }


   /**
    * Sets the range attribute of the JSPPartitionScanner object
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
    * Gets the token attribute of the JSPPartitionScanner object
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


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   private IToken nextCommentToken()
   {
      int ch = read();
      loop :
      while (true)
      {
         switch (ch)
         {
            case ICharacterScanner.EOF:
               break loop;
            case '-':// -  --%>
               ch = read();
               switch (ch)
               {
                  case ICharacterScanner.EOF:
                     break loop;
                  case '-':// --  --%>
                     ch = read();
                     switch (ch)
                     {
                        case ICharacterScanner.EOF:
                           break loop;
                        case '%':// --%  --%>
                           ch = read();
                           switch (ch)
                           {
                              case ICharacterScanner.EOF:
                              case '>':
                                 break loop;
                           }

                           continue loop;
                        case '-':// ---  ---%>
                           unread();
                           continue loop;
                     }

                     ch = read();
                     continue loop;
               }
         }

         ch = read();
      }

      return getToken(JSP_COMMENT);
   }


   /**
    * Description of the Method
    *
    * @param token  Description of the Parameter
    * @return       Description of the Return Value
    */
   private IToken nextJSPToken(String token)
   {
      int ch = read();
      while (true)
      {
         switch (ch)
         {
            case ICharacterScanner.EOF:
               state = STATE_DEFAULT;
               return getToken(token);
            case '%':
               ch = read();
               switch (ch)
               {
                  case ICharacterScanner.EOF:
                  case '>':
                     state = STATE_DEFAULT;
                     return getToken(token);
                  case '%':
                     continue;
               }
         }

         ch = read();
      }
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   private int read()
   {
      if (position >= end)
      {
         return ICharacterScanner.EOF;
      }

      try
      {
         return document.getChar(position++);
      }
      catch (BadLocationException e)
      {
         --position;
         return ICharacterScanner.EOF;
      }
   }


   /** Description of the Method */
   private void unread()
   {
      --position;
   }
}
