package org.hibernate.console.stubs;

import java.util.List;
import java.util.Properties;

import org.hibernate.cfg.JDBCReaderFactory;
import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.ProgressListener;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;

public class JDBCReaderStub {
	
	protected JDBCReader jdbcReader;
	
	protected JDBCReaderStub(Properties cfg, SettingsStub settings, ReverseEngineeringStrategy revengStrategy) {
		jdbcReader = JDBCReaderFactory.newJDBCReader(cfg, settings.settings, revengStrategy);
	}
	
	public static JDBCReaderStub newInstance(Properties cfg, SettingsStub settings, ReverseEngineeringStrategy revengStrategy) {
		return new JDBCReaderStub(cfg, settings, revengStrategy);
	}
	
	@SuppressWarnings("unchecked")
	public List readDatabaseSchema(DefaultDatabaseCollectorStub dbs, String catalog, String schema, ProgressListener progress) {
		return jdbcReader.readDatabaseSchema(dbs.defaultDatabaseCollector, catalog, schema, progress);
	}
}
