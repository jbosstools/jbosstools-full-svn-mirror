package org.hibernate.mediator.stubs;

import java.io.File;
import java.util.Properties;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.hbm2x.Exporter;

public class ExporterStub {

	protected Exporter exporter;

	protected ExporterStub(Object exporter) {
		if (exporter == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.exporter = (Exporter)exporter;
	}

	public void start() {
		exporter.start();
	}

	public void setConfiguration(ConfigurationStub configuration) {
		exporter.setConfiguration(configuration.configuration);
	}

	public void setProperties(Properties props) {
		exporter.setProperties(props);
	}

	public void setArtifactCollector(ArtifactCollectorStub collector) {
		exporter.setArtifactCollector(collector.artifactCollector);
	}

	public void setOutputDirectory(File file) {
		exporter.setOutputDirectory(file);
	}

	public void setTemplatePath(String[] array) {
		exporter.setTemplatePath(array);
	}

}
