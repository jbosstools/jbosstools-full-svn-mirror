package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;

import org.hibernate.mediator.base.HObject;

public class ArtifactCollector extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.ArtifactCollector"; //$NON-NLS-1$

	protected ArtifactCollector(Object artifactCollector) {
		super(artifactCollector, CL);
	}
	
	public static ArtifactCollector newInstance() {
		return new ArtifactCollector(newInstance(CL));
	}

	public File[] getFiles(String string) {
		return (File[])invoke(mn(), string);
	}

	public void formatFiles() {
		invoke(mn());
	}

}
