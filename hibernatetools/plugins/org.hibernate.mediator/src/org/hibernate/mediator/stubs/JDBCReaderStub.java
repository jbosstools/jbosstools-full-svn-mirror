package org.hibernate.mediator.stubs;

import java.util.List;
import java.util.Properties;

import org.hibernate.cfg.JDBCReaderFactory;
import org.hibernate.cfg.reveng.JDBCReader;

public class JDBCReaderStub {
	public static final String CL = "org.hibernate.cfg.reveng.JDBCReader"; //$NON-NLS-1$

	protected JDBCReader jdbcReader;
	
	protected JDBCReaderStub(Properties cfg, SettingsStub settings, ReverseEngineeringStrategyStub revengStrategy) {
		jdbcReader = JDBCReaderFactory.newJDBCReader(cfg, settings.settings, revengStrategy.reverseEngineeringStrategy);
	}
	
	public static JDBCReaderStub newInstance(Properties cfg, SettingsStub settings, ReverseEngineeringStrategyStub revengStrategy) {
		return new JDBCReaderStub(cfg, settings, revengStrategy);
	}
	
	@SuppressWarnings("unchecked")
	public List readDatabaseSchema(DefaultDatabaseCollectorStub dbs, String catalog, String schema, ProgressListenerStub progress) {
		return jdbcReader.readDatabaseSchema(dbs.defaultDatabaseCollector, catalog, schema, progress.progressListener);
	}
}
