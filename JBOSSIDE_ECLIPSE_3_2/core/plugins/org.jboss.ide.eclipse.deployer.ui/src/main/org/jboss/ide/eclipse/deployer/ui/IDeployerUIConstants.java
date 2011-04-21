/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
