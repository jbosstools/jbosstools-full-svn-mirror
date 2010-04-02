package org.hibernate.mediator.stubs;

import java.io.StringWriter;
import java.util.Properties;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.hbm2x.HibernateConfigurationExporter;

public class HibernateConfigurationExporterStub extends ExporterStub {
	public static final String CL = "org.hibernate.tool.hbm2x.HibernateConfigurationExporter"; //$NON-NLS-1$

	protected HibernateConfigurationExporter hibernateConfigurationExporter;

	protected HibernateConfigurationExporterStub(Object hibernateConfigurationExporter) {
		super(hibernateConfigurationExporter);
		if (hibernateConfigurationExporter == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.hibernateConfigurationExporter = (HibernateConfigurationExporter)hibernateConfigurationExporter;
	}
	
	public static HibernateConfigurationExporterStub newInstance() {
		return new HibernateConfigurationExporterStub(new HibernateConfigurationExporter());
	}

	public void setCustomProperties(Properties props) {
		hibernateConfigurationExporter.setCustomProperties(props);
	}

	public void setOutput(StringWriter stringWriter) {
		hibernateConfigurationExporter.setOutput(stringWriter);
	}
}
