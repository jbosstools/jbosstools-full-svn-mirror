package org.jboss.ide.eclipse.packages.core.model.types;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.packages.core.model.IPackage;

/**
 * All package types should extend this base class
 * @author Marshall
 */
public abstract class AbstractPackageType implements IPackageType {

	protected String id;
	protected String label;
	
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
