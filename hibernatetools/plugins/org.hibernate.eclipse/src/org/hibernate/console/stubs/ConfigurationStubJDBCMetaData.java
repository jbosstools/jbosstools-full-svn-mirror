package org.hibernate.console.stubs;

import java.util.Properties;

import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;

public class ConfigurationStubJDBCMetaData extends ConfigurationStub {

	protected JDBCMetaDataConfiguration jdbcMetaDataConfiguration;
	
	protected ConfigurationStubJDBCMetaData(JDBCMetaDataConfiguration configuration) {
		super(configuration);
		jdbcMetaDataConfiguration = configuration;
	}

	public void setProperties(Properties properties) {
		jdbcMetaDataConfiguration.setProperties(properties);
	}

	public void setPreferBasicCompositeIds(boolean flag) {
		jdbcMetaDataConfiguration.setPreferBasicCompositeIds(flag);
	}

	public void setReverseEngineeringStrategy(ReverseEngineeringStrategy reverseEngineeringStrategy) {
		jdbcMetaDataConfiguration.setReverseEngineeringStrategy(reverseEngineeringStrategy);
	}

	public void readFromJDBC() {
		jdbcMetaDataConfiguration.readFromJDBC();
	}

}
