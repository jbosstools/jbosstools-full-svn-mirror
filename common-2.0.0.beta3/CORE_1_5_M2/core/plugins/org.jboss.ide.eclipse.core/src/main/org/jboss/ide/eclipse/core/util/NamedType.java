/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.core.util;


/**
 * General purpose class for unsafe named type enumeration
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class NamedType
{
   private String name;


   /**
    * Constructor for a Named yype object
    *
    * @param name  Name of the type
    */
   protected NamedType(String name)
   {
      this.name = name;
   }


   /**
    * Implementation of equals method.
    * To be equal, must be of the same class and
    * have the same name
    *
    * @param obj  Object to test equality
    * @return     True if Object is equal
    */
   public boolean equals(Object obj)
   {
      return (this.getClass().equals(obj.getClass()) && this.name.equals(((NamedType) obj).getName()));
   }


   /**
    * Gets the name of the type
    *
    * @return   The name of the type
    */
   public String getName()
   {
      return this.name;
   }


   /**
    * Implementation of the hashCode method.
    *
    * @return   Hash code of the named type
    */
   public int hashCode()
   {
      return this.name.hashCode();
   }


   /**
    * Gets the String representation
    *
    * @return   The String representation
    */
   public String toString()
   {
      return this.name;
   }
}
