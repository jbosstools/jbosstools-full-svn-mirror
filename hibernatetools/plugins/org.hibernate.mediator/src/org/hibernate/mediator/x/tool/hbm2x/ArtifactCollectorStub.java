package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;

import org.hibernate.mediator.base.HObject;

public class ArtifactCollectorStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.ArtifactCollector"; //$NON-NLS-1$

	protected ArtifactCollectorStub(Object artifactCollector) {
		super(artifactCollector, CL);
	}
	
	public static ArtifactCollectorStub newInstance() {
		return new ArtifactCollectorStub(newInstance(CL));
	}

	public File[] getFiles(String string) {
		return (File[])invoke(mn(), string);
	}

	public void formatFiles() {
		invoke(mn());
	}

}
