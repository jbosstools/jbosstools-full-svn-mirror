package org.hibernate.mediator.stubs;

import java.io.File;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.Exporter;

public class ExporterStub {

	protected Exporter exporter;

	protected ExporterStub(Object exporter) {
		this.exporter = (Exporter)exporter;
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void setConfiguration(Configuration configuration) {
		// TODO Auto-generated method stub
		
	}

	public void setProperties(Properties props) {
		// TODO Auto-generated method stub
		
	}

	public void setArtifactCollector(ArtifactCollectorStub collector) {
		// TODO Auto-generated method stub
		
	}

	public void setOutputDirectory(File file) {
		// TODO Auto-generated method stub
		
	}

	public void setTemplatePath(String[] array) {
		// TODO Auto-generated method stub
		
	}

}
