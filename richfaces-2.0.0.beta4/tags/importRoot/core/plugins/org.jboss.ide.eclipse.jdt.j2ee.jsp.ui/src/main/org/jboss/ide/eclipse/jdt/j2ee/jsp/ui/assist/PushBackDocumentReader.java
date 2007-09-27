/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * Reader that can scan a Document forward and backward. It can also
 * skip whitespace character during scans.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class PushBackDocumentReader
{
   private int current = -1;
   private IDocument document = null;
   private int offset = -1;
   /** Description of the Field */
   public final static char EOS = (char) -1;


   /**
    *Constructor for the PushBackDocumentReader object
    *
    * @param document  Description of the Parameter
    * @param offset    Description of the Parameter
    */
   public PushBackDocumentReader(IDocument document, int offset)
   {
      if (offset > 0)
      {
         this.offset = offset;
         this.current = this.offset;
      }
      this.document = document;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public char readBackward()
   {
      if (this.current <= 0)
      {
         return EOS;
      }
      try
      {
         return (document.getChar(--this.current));
      }
      catch (BadLocationException e)
      {
         this.current++;
         return EOS;
      }
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public char readBackwardAndSkipSpaces()
   {
      char c = this.readBackward();
      while (c != EOS && Character.isWhitespace(c))
      {
         c = this.readBackward();
      }
      return c;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public char readForward()
   {
      if (this.current < document.getLength())
      {
         try
         {
            return (document.getChar(++this.current));
         }
         catch (BadLocationException ex)
         {
            this.current--;
            return EOS;
         }
      }
      return EOS;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public char readForwardAndSkipSpaces()
   {
      char c = this.readForward();
      while (c != EOS && Character.isWhitespace(c))
      {
         c = this.readForward();
      }
      return c;
   }


   /** Description of the Method */
   public void reset()
   {
      this.current = this.offset;
   }
}
