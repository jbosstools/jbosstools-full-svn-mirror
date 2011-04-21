/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.core.util;

import org.jboss.ide.eclipse.core.util.NamedType;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class LaunchType extends NamedType
{
   /** Description of the Field */
   public final static LaunchType SHUTDOWN = new LaunchType("shutdown");//$NON-NLS-1$
   /** Description of the Field */
   public final static LaunchType START = new LaunchType("start");//$NON-NLS-1$


   /**
    *Constructor for the LaunchStatus object
    *
    * @param name  Description of the Parameter
    */
   private LaunchType(String name)
   {
      super(name);
   }
}
