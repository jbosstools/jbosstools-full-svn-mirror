/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.completion;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 * @todo      Javadoc to complete
 */
public class Property
{
   private String name;
   private String value;


   /**
    *Constructor for the Property object
    *
    * @param name   Description of the Parameter
    * @param value  Description of the Parameter
    */
   public Property(String name, String value)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("Name of Property must not be null");//$NON-NLS-1$
      }
      this.name = name;
      this.value = value;
   }


   /**
    * Gets the name attribute of the Property object
    *
    * @return   The name value
    */
   public String getName()
   {
      return name;
   }


   /**
    * Gets the value attribute of the Property object
    *
    * @return   The value value
    */
   public String getValue()
   {
      return value;
   }


   /**
    * Sets the value attribute of the Property object
    *
    * @param value  The new value value
    */
   public void setValue(String value)
   {
      this.value = value;
   }
}
