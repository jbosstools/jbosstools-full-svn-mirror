package org.hibernate.mediator.x.tool.hbm2x;


public class GenericExporterStub extends ExporterStub {
	public static final String CL = "org.hibernate.tool.hbm2x.GenericExporter"; //$NON-NLS-1$

	protected GenericExporterStub(Object genericExporter) {
		super(genericExporter, CL);
	}

	public void setFilePattern(String property) {
		invoke(mn(), property);
	}

	public void setTemplateName(String property) {
		invoke(mn(), property);
	}

	public void setForEach(String property) {
		invoke(mn(), property);
	}

}
