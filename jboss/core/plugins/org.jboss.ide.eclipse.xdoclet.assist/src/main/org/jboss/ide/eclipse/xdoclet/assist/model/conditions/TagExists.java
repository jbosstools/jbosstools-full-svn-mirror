/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model.conditions;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TagExists extends Condition
{
   /** tag to check */
   private final String tagName;


   /**
    * Constructs a condition that checks to see if the specified tag exists within
    * an XProgramElement.
    *
    * @param tagName  the name of the tag to check for
    */
   public TagExists(String tagName)
   {
      this.tagName = tagName;
   }


   /**
    * check whether condition holds
    *
    * @param member  Description of the Parameter
    * @return        whether condition holds
    */
   public boolean evalInternal(IMember member)
   {
      // We need improved eclipse javadoc capabilities to implement that
      // return xprogramelement.getDoc().hasTag( tagName );

      try
      {
         String source = member.getSource();
         return (source.indexOf(this.tagName) >= 0);
      }
      catch (JavaModelException jme)
      {
         AbstractPlugin.log(jme);
      }
      return false;
   }


   /**
    * Describe what the method does
    *
    * @return   Describe the return value
    */
   public String toString()
   {
      return getClass().getName() + " : " + "@" + tagName;//$NON-NLS-1$ //$NON-NLS-2$
   }

}
