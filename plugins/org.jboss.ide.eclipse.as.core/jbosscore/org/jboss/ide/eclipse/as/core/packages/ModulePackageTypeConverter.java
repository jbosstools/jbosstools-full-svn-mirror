/**
 * JBoss, a Division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.as.core.packages;

import org.eclipse.wst.server.core.IModule;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class ModulePackageTypeConverter {
	public static final String WAR_PACKAGE_TYPE = "org.jboss.ide.eclipse.as.core.packages.warPackage";
	public static final String EAR_PACKAGE_TYPE = "org.jboss.ide.eclipse.as.core.packages.earPackage";
	public static IPackageType getPackageTypeFor(IModule module) {
		String modType = module.getModuleType().getId();
		if("jst.web".equals(modType)) {
			return PackagesCore.getPackageType(WAR_PACKAGE_TYPE);
		} else if("jst.ear".equals(modType)) {
			return PackagesCore.getPackageType(EAR_PACKAGE_TYPE);
		}

		return null;
	}
}
