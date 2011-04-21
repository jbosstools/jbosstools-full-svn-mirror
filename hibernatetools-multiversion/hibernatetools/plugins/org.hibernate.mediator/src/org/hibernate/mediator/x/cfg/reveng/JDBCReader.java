package org.hibernate.mediator.x.cfg.reveng;

import java.util.List;
import java.util.Properties;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.cfg.Settings;

public class JDBCReader extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.JDBCReader"; //$NON-NLS-1$
	
	protected JDBCReader(Properties cfg, Settings settings, ReverseEngineeringStrategy revengStrategy) {
		super(create(cfg, settings, revengStrategy), CL);
	}
	
	private static Object create(Properties cfg, Settings settings, ReverseEngineeringStrategy revengStrategy) {
		Object jdbcReader = invokeStaticMethod("org.hibernate.cfg.JDBCReaderFactory",  //$NON-NLS-1$
			"newJDBCReader", cfg, settings, revengStrategy); //$NON-NLS-1$
		return jdbcReader;
	}
	
	public static JDBCReader newInstance(Properties cfg, Settings settings, ReverseEngineeringStrategy revengStrategy) {
		return new JDBCReader(cfg, settings, revengStrategy);
	}
	
	@SuppressWarnings("unchecked")
	public List readDatabaseSchema(DefaultDatabaseCollector dbs, String catalog, String schema, ProgressListener progress) {
		return (List)invoke(mn(), dbs, catalog, schema, progress.progressListener);
	}
}
