/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.core.wizards.generation;


/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface ITemplates
{
   /**
    * Gets the string attribute of the ITemplates object
    *
    * @param key  Description of the Parameter
    * @return     The string value
    */
   public String getString(String key);


   /**
    * Gets the string attribute of the ITemplates object
    *
    * @param key         Description of the Parameter
    * @param parameters  Description of the Parameter
    * @return            The string value
    */
   public String getString(String key, Object[] parameters);
}
