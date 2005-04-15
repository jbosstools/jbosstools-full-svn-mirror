/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.launcher.ui.util;

import java.util.Comparator;

import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   18 mai 2003
 */
public class SortedLaunchConfigurationComparator implements Comparator
{
   /**
    * @param o1  Description of the Parameter
    * @param o2  Description of the Parameter
    * @return    Description of the Return Value
    * @see       java.util.Comparator#compare(Object, Object)
    */
   public int compare(Object o1, Object o2)
   {
      return ServerLaunchUIUtil.getName((ILaunchConfiguration) o1).compareTo(ServerLaunchUIUtil.getName((ILaunchConfiguration) o2));
   }
}
