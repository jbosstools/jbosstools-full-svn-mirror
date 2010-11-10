/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.bpel.runtimes.module;

import org.eclipse.bpel.runtimes.module.BPELModuleFactoryDelegate;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.model.ModuleFactoryDelegate;

/**
 * The purpose of this class is to redirect requests to locate an
 * already-migrated project module. For example, if a project 
 * previously was of module-type jbt.bpel.module and has since been 
 * migrated properly, old servers may still search for a jbt.bpel.module 
 * module ID.
 * 
 * This factory does not create any modules, but rather only returns
 * new modules from the eclipse bpel factory when the server is requesting
 * a legacy id. 
 */
public class JBTBPELModuleFactoryDelegate  extends ModuleFactoryDelegate {
	public static final String FACTORY_ID = "org.jboss.tools.bpel.runtimes.module.moduleFactory";
	public static final String LEGACY_MODULE_TYPE = "jbt.bpel.module";
	
	public JBTBPELModuleFactoryDelegate() {
		super();
	}

	@Override
	public ModuleDelegate getModuleDelegate(IModule module) {
		return BPELModuleFactoryDelegate.factoryInstance().getModuleDelegate(module);
	}

	@Override
	public IModule[] getModules() {
		return new IModule[]{};
	}
	
	@Override
	public IModule findModule(String id) {
		return BPELModuleFactoryDelegate.factoryInstance().findModule(id);
	}
}