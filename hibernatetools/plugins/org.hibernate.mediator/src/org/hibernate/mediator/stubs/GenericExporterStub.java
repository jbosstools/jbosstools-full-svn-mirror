package org.hibernate.mediator.stubs;

import org.hibernate.tool.hbm2x.GenericExporter;

public class GenericExporterStub extends ExporterStub {
	protected GenericExporter genericExporter;

	protected GenericExporterStub(Object genericExporter) {
		super(genericExporter);
		this.genericExporter = (GenericExporter)genericExporter;
	}

	public void setFilePattern(String property) {
		genericExporter.setFilePattern(property);
	}

	public void setTemplateName(String property) {
		genericExporter.setTemplateName(property);
	}

	public void setForEach(String property) {
		genericExporter.setForEach(property);
	}

}
