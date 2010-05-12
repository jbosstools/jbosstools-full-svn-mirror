package org.hibernate.mediator.x.tool.hbm2x;

public class ExporterFactory {
	@SuppressWarnings("unchecked")
	public static Exporter createExporterStub(Object exporter) {
		if (exporter == null) {
			return null;
		}
		final Class cl = exporter.getClass();
		if (0 == HibernateConfigurationExporter.CL.compareTo(cl.getName())) {
			return new HibernateConfigurationExporter(exporter);
		} else if (0 == GenericExporter.CL.compareTo(cl.getName())) {
			return new GenericExporter(exporter);
		}
		return new GenericExporter(exporter);
	}
}
