/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.text.rules;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.jboss.ide.eclipse.jdt.xml.ui.text.scanners.XMLPartitionScanner;

/*
 * This file contains materials derived from the
 * XMen project. License can be found at :
 * http://www.eclipse.org/legal/cpl-v10.html
 */
/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLTagsRule implements IPredicateRule
{
   /** Description of the Field */
   public final static IToken COMMENT = new Token(XMLPartitionScanner.XML_COMMENT);
   /** Description of the Field */
   public final static IToken DECLARATION = new Token(XMLPartitionScanner.XML_DECL);
   /** Description of the Field */
   public final static IToken EMPTYTAG = new Token(XMLPartitionScanner.XML_EMPTY_TAG);
   /** Description of the Field */
   public final static IToken ENDTAG = new Token(XMLPartitionScanner.XML_END_TAG);
   /** Description of the Field */
   public final static IToken END_DECLARATION = new Token(XMLPartitionScanner.XML_END_DECL);
   /** Description of the Field */
   public final static IToken PI = new Token(XMLPartitionScanner.XML_PI);
   /** Description of the Field */
   public final static IToken START_DECLARATION = new Token(XMLPartitionScanner.XML_START_DECL);
   /** Description of the Field */
   public final static IToken TAG = new Token(XMLPartitionScanner.XML_TAG);
   //public static final IToken TEXT = new Token(XMLPartitionScanner.XML_TEXT);
   /** Description of the Field */
   public final static IToken TEXT = new Token(IDocument.DEFAULT_CONTENT_TYPE);


   /**
    * Description of the Method
    *
    * @param scanner  Description of the Parameter
    * @return         Description of the Return Value
    */
   public IToken evaluate(ICharacterScanner scanner)
   {
      IToken result = Token.EOF;
      int c = scanner.read();

      if (c == -1)
      {
         return Token.EOF;
      }

      if (c == ']')
      {
         c = scanner.read();

         if (c == '>')
         {
            return END_DECLARATION;
         }

         while (c != -1 && c != '<' && c != ']')
         {
            c = scanner.read();
         }
         scanner.unread();

         return TEXT;
      }
      else if (c != '<')
      {
         while (c != -1 && c != '<' && c != ']')
         {
            c = scanner.read();
         }

         scanner.unread();

         return TEXT;
      }
      else
      {
         result = TAG;
         c = scanner.read();

         switch (c)
         {
            case '!':
               result = DECLARATION;
               c = scanner.read();
               if (c == '-')
               {
                  c = scanner.read();
                  if (c == '-')
                  {
                     c = scanner.read();
                     result = COMMENT;
                     c = scanTo(scanner, "-->", false);//$NON-NLS-1$
                  }
                  else
                  {
                     c = findFirstOf(scanner, '>', '[', true);

                     if (c == '>')
                     {
                        return DECLARATION;
                     }
                     return START_DECLARATION;
                  }
               }
               else
               {
                  scanner.unread();
                  if (isNext(scanner, "[CDATA[")//$NON-NLS-1$
                          )
                  {

                     result = TEXT;
                     c = scanTo(scanner, "]]>", false);//$NON-NLS-1$
                  }
                  else
                  {
                     c = findFirstOf(scanner, '>', '[', true);

                     if (c == '>')
                     {
                        return DECLARATION;
                     }
                     return START_DECLARATION;
                  }
               }
               break;
            case '?':
               result = PI;
               c = scanTo(scanner, "?>", false);//$NON-NLS-1$
               break;
            case '>':
               break;
            case '/':
               result = ENDTAG;
               c = scanTo(scanner, ">", true);//$NON-NLS-1$
               break;
            default:
               c = scanTo(scanner, ">", true);//$NON-NLS-1$
               if (c != -1)
               {
                  scanner.unread();
                  scanner.unread();
                  if (scanner.read() == '/')
                  {
                     result = EMPTYTAG;
                  }
                  scanner.read();
               }
               break;
         }
         //            if (c == -1) {
         //                return Token.EOF;
         //            }
      }

      return result;
   }


   /*
    * (non-Javadoc)
    * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner, boolean)
    */
   /**
    * Description of the Method
    *
    * @param scanner  Description of the Parameter
    * @param resume   Description of the Parameter
    * @return         Description of the Return Value
    */
   public IToken evaluate(ICharacterScanner scanner, boolean resume)
   {
      return evaluate(scanner);
   }


   /*
    * (non-Javadoc)
    * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
    */
   /**
    * Gets the successToken attribute of the XMLTagsRule object
    *
    * @return   The successToken value
    */
   public IToken getSuccessToken()
   {
      return Token.EOF;//TEXT;//DECLARATION;
   }


   /**
    * Description of the Method
    *
    * @param scanner       Description of the Parameter
    * @param one           Description of the Parameter
    * @param other         Description of the Parameter
    * @param quoteEscapes  Description of the Parameter
    * @return              Description of the Return Value
    */
   private int findFirstOf(ICharacterScanner scanner, char one, char other, boolean quoteEscapes)
   {
      int c;
      boolean inSingleQuote = false;
      boolean inDoubleQuote = false;

      do
      {
         c = scanner.read();
         if (c == '"' && !inSingleQuote)
         {
            inDoubleQuote = !inDoubleQuote;
         }
         else if (c == '\'' && !inDoubleQuote)
         {
            inSingleQuote = !inSingleQuote;
         }
         else if (!inSingleQuote && !inDoubleQuote)
         {
            if (c == one)
            {
               return c;
            }
            else if (c == other)
            {
               return c;
            }
         }
      } while (c != -1);

      return c;
   }


   /**
    * @param scanner
    * @param s        Description of the Parameter
    * @return
    */
   private boolean isNext(ICharacterScanner scanner, String s)
   {
      int pos = 0;

      while (pos < s.length())
      {
         int c = scanner.read();
         if (c != s.charAt(pos))
         {
            for (int i = 0; i < pos; i++)
            {
               scanner.unread();
            }
            return false;
         }
         pos++;
      }

      return true;
   }


   /**
    * Description of the Method
    *
    * @param scanner       Description of the Parameter
    * @param end           Description of the Parameter
    * @param quoteEscapes  Description of the Parameter
    * @return              Description of the Return Value
    */
   private int scanTo(ICharacterScanner scanner, String end, boolean quoteEscapes)
   {
      int c;
      int i = 0;
      boolean inSingleQuote = false;
      boolean inDoubleQuote = false;

      do
      {
         c = scanner.read();
         if (c == '"' && !inSingleQuote && quoteEscapes)
         {
            inDoubleQuote = !inDoubleQuote;
            i = 0;
         }
         else if (c == '\'' && !inDoubleQuote && quoteEscapes)
         {
            inSingleQuote = !inSingleQuote;
            i = 0;
         }
         else if (!inSingleQuote && !inDoubleQuote)
         {
            if (c == end.charAt(i))
            {
               i++;
            }
            else if (i > 0)
            {
               i = 0;
            }
         }
         if (i >= end.length())
         {
            return c;
         }
      } while (c != -1);

      return c;
   }

}
