/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist.tags;


/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPTags extends AbstractRepository
{
   private static AbstractRepository instance = new JSPTags();


   /**Constructor for the JSPTags object */
   protected JSPTags() { }


   /**
    * Gets the instance attribute of the JSPTags class
    *
    * @return   The instance value
    */
   public static synchronized AbstractRepository getInstance()
   {
      return instance;
   }

}
