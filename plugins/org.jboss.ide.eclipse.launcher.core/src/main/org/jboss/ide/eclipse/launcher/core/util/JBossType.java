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
 * @created   18 mai 2003
 */
public class JBossType extends NamedType
{
   /** Description of the Field */
   public final static JBossType JBoss_2_x = new JBossType("JBoss 2.x");//$NON-NLS-1$
   /** Description of the Field */
   public final static JBossType JBoss_3_0_X = new JBossType("JBoss 3.0.x");//$NON-NLS-1$
   /** Description of the Field */
   public final static JBossType JBoss_3_2_X = new JBossType("JBoss 3.2.x");//$NON-NLS-1$
   /** Description of the Field */
   public final static JBossType JBoss_4_0_X = new JBossType("JBoss 4.0.x");//$NON-NLS-1$


   /**
    *Constructor for the JBossType object
    *
    * @param name  Description of the Parameter
    */
   private JBossType(String name)
   {
      super(name);
   }
}
