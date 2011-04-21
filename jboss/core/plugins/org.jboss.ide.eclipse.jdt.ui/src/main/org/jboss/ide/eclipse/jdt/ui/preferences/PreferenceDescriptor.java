/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.preferences;


/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Preference descriptor.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public final class PreferenceDescriptor
{
   /** Description of the Field */
   public final String key;

   /** Description of the Field */
   public final Type type;
   /** Description of the Field */
   public final static Type BOOLEAN = new Type();
   /** Description of the Field */
   public final static Type DOUBLE = new Type();
   /** Description of the Field */
   public final static Type FLOAT = new Type();
   /** Description of the Field */
   public final static Type INT = new Type();
   /** Description of the Field */
   public final static Type LONG = new Type();
   /** Description of the Field */
   public final static Type STRING = new Type();


   /**
    *Constructor for the PreferenceDescriptor object
    *
    * @param type  Description of the Parameter
    * @param key   Description of the Parameter
    */
   public PreferenceDescriptor(Type type, String key)
   {
      this.type = type;
      this.key = key;
   }


   /**
    * Description of the Class
    *
    * @author    Laurent Etiemble
    * @version   $Revision$
    */
   public final static class Type
   {
      /**Constructor for the Type object */
      Type() { }
   }
}
