/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.core.util;


/**
 * Name/Value pair holder for general purpose
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NameValuePair
{
   private String name = null;
   private String value = null;


   /**
    * Constructor
    *
    * @param name  Name for the pair
    */
   public NameValuePair(String name)
   {
      this.name = name;
      this.value = "";//$NON-NLS-1$
   }


   /**
    *Constructor for the NameValuePair object
    *
    * @param name   Description of the Parameter
    * @param value  Description of the Parameter
    */
   public NameValuePair(String name, String value)
   {
      this.name = name;
      this.value = value;
   }


   /**
    * @return   The pair name
    */
   public String getName()
   {
      return this.name;
   }


   /**
    * @return   The pair value
    */
   public String getValue()
   {
      return this.value;
   }


   /**
    * Sets the pair name
    *
    * @param name  The new name
    */
   public void setName(String name)
   {
      this.name = name;
   }


   /**
    * Sets the pair value
    *
    * @param value  The new value
    */
   public void setValue(String value)
   {
      this.value = value;
   }


   /**
    * toString implementation
    *
    * @return   The String representation
    */
   public String toString()
   {
      return name + ":" + value;//$NON-NLS-1$
   }
}
