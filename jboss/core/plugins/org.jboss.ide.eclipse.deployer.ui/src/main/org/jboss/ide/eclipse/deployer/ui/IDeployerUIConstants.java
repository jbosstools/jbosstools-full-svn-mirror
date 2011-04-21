/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui;

import org.eclipse.core.runtime.QualifiedName;


/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface IDeployerUIConstants
{
   /** Description of the Field */
   public final static QualifiedName QNAME_TARGET = new QualifiedName("org.jboss.ide.eclipse.deployer.ui", "target");//$NON-NLS-1$ //$NON-NLS-2$
   /** Description of the Field */
   public final static QualifiedName QNAME_DEPLOYED = new QualifiedName("org.jboss.ide.eclipse.deployer.ui", "deployed");//$NON-NLS-1$ //$NON-NLS-2$

   /** Description of the Field */
   public final static String IMG_OBJS_FILESYSTEM = "IMG_OBJS_FILESYSTEM";//$NON-NLS-1$
   /** Description of the Field */
   public final static String IMG_OBJS_LOCAL_DEPLOYER = "IMG_OBJS_LOCAL_DEPLOYER";//$NON-NLS-1$

   /** Description of the Field */
   public final static String IMG_OVR_DEPLOYED = "IMG_OVR_DEPLOYED";//$NON-NLS-1$
}

