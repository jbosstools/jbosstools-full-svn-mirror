package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;
import java.util.Properties;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.cfg.Configuration;

public class Exporter extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.Exporter"; //$NON-NLS-1$

	protected Exporter(Object exporter) {
		super(exporter, CL);
	}

	protected Exporter(Object exporter, String cn) {
		super(exporter, cn);
	}

	public void start() {
		invoke(mn());
	}

	public void setConfiguration(Configuration configuration) {
		invoke(mn(), configuration);
	}

	public void setProperties(Properties props) {
		invoke(mn(), props);
	}

	public void setArtifactCollector(ArtifactCollector collector) {
		invoke(mn(), collector);
	}

	public void setOutputDirectory(File file) {
		invoke(mn(), file);
	}

	public void setTemplatePath(String[] array) {
		invoke(mn(), array);
	}

}
