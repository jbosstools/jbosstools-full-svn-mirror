/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.model;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class DocletType
{
   private String name;
   /** Description of the Field */
   public final static DocletType COMMAND = new DocletType("command");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType DISCRETE_ATTRIBUTE = new DocletType("discrete_attribute");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType NAMESPACE = new DocletType("namespace");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType NON_DISCRETE_ATTRIBUTE = new DocletType("nondiscrete_attribute");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType ROOT = new DocletType("root");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType VALUE_WITHOUT_VARIABLE = new DocletType("value_without_variable");//$NON-NLS-1$
   /** Description of the Field */
   public final static DocletType VALUE_WITH_VARIABLE = new DocletType("value_with_variable");//$NON-NLS-1$


   /**
    *Constructor for the DocletType object
    *
    * @param name  Description of the Parameter
    */
   private DocletType(String name)
   {
      this.name = name;
   }


   /**
    * Gets the name attribute of the DocletType object
    *
    * @return   The name value
    */
   public String getName()
   {
      return name;
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public String toString()
   {
      return getName();
   }

}
