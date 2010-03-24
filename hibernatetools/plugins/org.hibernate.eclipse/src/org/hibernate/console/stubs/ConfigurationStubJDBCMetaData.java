package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;
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

	public ArrayList<TableStub> getTableMappingsArr() {
		ArrayList<TableStub> arr = new ArrayList<TableStub>(); 
		Iterator<?> it = jdbcMetaDataConfiguration.getTableMappings();
		while (it.hasNext() ) {
			Object table = it.next();
			arr.add(new TableStub(table));
		}
		return arr;
	}

	public Iterator<TableStub> getTableMappingsIt() {
		return getTableMappingsArr().iterator();
	}

}
