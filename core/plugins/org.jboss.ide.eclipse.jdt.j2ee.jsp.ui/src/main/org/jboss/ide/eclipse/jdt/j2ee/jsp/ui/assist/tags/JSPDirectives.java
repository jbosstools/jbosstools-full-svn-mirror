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
public class JSPDirectives extends AbstractRepository
{
   private static AbstractRepository instance = new JSPDirectives();


   /**Constructor for the JSPDirectives object */
   protected JSPDirectives() { }


   /**
    * Gets the instance attribute of the JSPDirectives class
    *
    * @return   The instance value
    */
   public static synchronized AbstractRepository getInstance()
   {
      return instance;
   }
}
