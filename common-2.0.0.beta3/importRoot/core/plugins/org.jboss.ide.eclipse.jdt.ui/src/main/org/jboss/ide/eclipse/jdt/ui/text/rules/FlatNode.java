/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.text.rules;


/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class FlatNode
{

   /** Flat length of the node */
   public int length;

   /** Flat offset of the node */
   public int offset;
   /** Content-type of the node */
   public final String type;


   /**
    *Constructor for the FlatNode object
    *
    * @param type  Description of the Parameter
    */
   public FlatNode(String type)
   {
      this.type = type;
   }


   /**
    * Checks whether the given offset is inside
    * of this position's text range.
    *
    * @param offset  the offset to check
    * @return        <code>true</code> if offset is inside of this position
    */
   public boolean includes(int offset)
   {
      return (this.offset <= offset && offset < this.offset + length);
   }


   /**
    * Checks whether the intersection of the given text range
    * and the text range represented by this position is empty
    * or not.
    *
    * @param offset  the offset of the range to check
    * @param length  the length of the range to check
    * @return        <code>true</code> if intersection is not empty
    */
   public boolean overlapsWith(int offset, int length)
   {
      int end = offset + length;
      int thisEnd = this.offset + this.length;

      if (length > 0)
      {
         if (this.length > 0)
         {
            return (this.offset < end && offset < thisEnd);
         }
         return (offset <= this.offset && this.offset < end);
      }

      if (this.length > 0)
      {
         return (this.offset <= offset && offset < thisEnd);
      }

      return (this.offset == offset);
   }


   /*
    * @see java.lang.Object#toString()
    */
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return "FlatNode[" + type + ", " + offset + ", " + length + "]";//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
}
