package org.hibernate.mediator.x.tool.hbm2x;

import java.io.File;
import java.util.Properties;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.cfg.ConfigurationStub;

public class ExporterStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2x.Exporter"; //$NON-NLS-1$

	protected ExporterStub(Object exporter) {
		super(exporter, CL);
	}

	protected ExporterStub(Object exporter, String cn) {
		super(exporter, cn);
	}

	public void start() {
		invoke(mn());
	}

	public void setConfiguration(ConfigurationStub configuration) {
		invoke(mn(), configuration);
	}

	public void setProperties(Properties props) {
		invoke(mn(), props);
	}

	public void setArtifactCollector(ArtifactCollectorStub collector) {
		invoke(mn(), collector);
	}

	public void setOutputDirectory(File file) {
		invoke(mn(), file);
	}

	public void setTemplatePath(String[] array) {
		invoke(mn(), array);
	}

}
