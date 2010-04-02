package org.hibernate.mediator.stubs;

import java.io.File;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.hbm2x.ArtifactCollector;

public class ArtifactCollectorStub {
	public static final String CL = "org.hibernate.tool.hbm2x.ArtifactCollector"; //$NON-NLS-1$

	protected ArtifactCollector artifactCollector;

	protected ArtifactCollectorStub(Object artifactCollector) {
		if (artifactCollector == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.artifactCollector = (ArtifactCollector)artifactCollector;
	}
	
	public static ArtifactCollectorStub newInstance() {
		return new ArtifactCollectorStub(new ArtifactCollector());
	}

	public File[] getFiles(String string) {
		return artifactCollector.getFiles(string);
	}

	public void formatFiles() {
		artifactCollector.formatFiles();
	}

}
