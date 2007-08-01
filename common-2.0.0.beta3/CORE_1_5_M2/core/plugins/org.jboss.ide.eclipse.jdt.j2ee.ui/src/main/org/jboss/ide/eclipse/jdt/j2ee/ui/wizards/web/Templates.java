/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.web;

import org.jboss.ide.eclipse.jdt.core.wizards.generation.AbstractTemplates;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;

/**
 * Generation templates accessor helper class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Templates extends AbstractTemplates
{
   /** Default Constructor */
   public Templates()
   {
      super(JDTJ2EECorePlugin.getDefault(), "web");//$NON-NLS-1$
   }
}
