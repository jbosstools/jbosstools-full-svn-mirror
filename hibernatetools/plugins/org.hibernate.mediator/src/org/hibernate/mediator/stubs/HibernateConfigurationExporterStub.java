package org.hibernate.mediator.stubs;

import java.io.StringWriter;
import java.util.Properties;

import org.hibernate.tool.hbm2x.HibernateConfigurationExporter;

public class HibernateConfigurationExporterStub extends ExporterStub {
	protected HibernateConfigurationExporter hibernateConfigurationExporter;

	protected HibernateConfigurationExporterStub(Object hibernateConfigurationExporter) {
		super(hibernateConfigurationExporter);
		this.hibernateConfigurationExporter = (HibernateConfigurationExporter)hibernateConfigurationExporter;
	}
	
	public static HibernateConfigurationExporterStub newInstance() {
		return new HibernateConfigurationExporterStub(new HibernateConfigurationExporter());
	}

	public void setCustomProperties(Properties props) {
		// TODO Auto-generated method stub
		
	}

	public void setOutput(StringWriter stringWriter) {
		// TODO Auto-generated method stub
		
	}
}
