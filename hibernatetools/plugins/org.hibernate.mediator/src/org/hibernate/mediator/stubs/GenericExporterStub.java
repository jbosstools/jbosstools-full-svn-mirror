package org.hibernate.mediator.stubs;

import org.hibernate.tool.hbm2x.GenericExporter;

public class GenericExporterStub extends ExporterStub {
	protected GenericExporter genericExporter;

	protected GenericExporterStub(Object genericExporter) {
		super(genericExporter);
		this.genericExporter = (GenericExporter)genericExporter;
	}

	public void setFilePattern(String property) {
		// TODO Auto-generated method stub
		
	}

	public void setTemplateName(String property) {
		// TODO Auto-generated method stub
		
	}

	public void setForEach(String property) {
		// TODO Auto-generated method stub
		
	}

}
