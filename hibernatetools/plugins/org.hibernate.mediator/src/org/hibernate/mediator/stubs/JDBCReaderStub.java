package org.hibernate.mediator.stubs;

import java.util.List;
import java.util.Properties;

import org.hibernate.mediator.base.HObject;

public class JDBCReaderStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.JDBCReader"; //$NON-NLS-1$
	
	protected JDBCReaderStub(Properties cfg, SettingsStub settings, ReverseEngineeringStrategyStub revengStrategy) {
		super(create(cfg, settings, revengStrategy), CL);
	}
	
	private static Object create(Properties cfg, SettingsStub settings, ReverseEngineeringStrategyStub revengStrategy) {
		Object jdbcReader = invokeStaticMethod("org.hibernate.cfg.JDBCReaderFactory",  //$NON-NLS-1$
			"newJDBCReader", cfg, settings, revengStrategy); //$NON-NLS-1$
		return jdbcReader;
	}
	
	public static JDBCReaderStub newInstance(Properties cfg, SettingsStub settings, ReverseEngineeringStrategyStub revengStrategy) {
		return new JDBCReaderStub(cfg, settings, revengStrategy);
	}
	
	@SuppressWarnings("unchecked")
	public List readDatabaseSchema(DefaultDatabaseCollectorStub dbs, String catalog, String schema, ProgressListenerStub progress) {
		return (List)invoke(mn(), dbs, catalog, schema, progress.progressListener);
	}
}
