package org.jboss.tools.bpel.runtimes.module;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleFolder;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.ModuleDelegate;
import org.eclipse.wst.server.core.util.ModuleFile;
import org.eclipse.wst.server.core.util.ModuleFolder;
import org.eclipse.wst.server.core.util.ProjectModule;
import org.jboss.ide.eclipse.as.wtp.core.modules.JBTProjectModuleDelegate;
import org.jboss.tools.bpel.runtimes.IBPELModuleFacetConstants;
import org.jboss.tools.bpel.runtimes.RuntimesPlugin;

public class BPELModuleDelegate extends JBTProjectModuleDelegate {

	public BPELModuleDelegate(IProject project) {
		super(project);
	}

	@Override
	protected String getFactoryId() {
		return BPELModuleFactoryDelegate.FACTORY_ID;
	}

	public IModule[] getModules() {
		return new IModule[]{};
	}

	public boolean isBinary() {
		return false;
	}
}
