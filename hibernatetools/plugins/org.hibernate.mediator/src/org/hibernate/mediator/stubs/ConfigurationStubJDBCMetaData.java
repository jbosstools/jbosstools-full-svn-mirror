package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.hibernate.mediator.base.HObject;

public class ConfigurationStubJDBCMetaData extends ConfigurationStub {
	public static final String CL = "org.hibernate.cfg.JDBCMetaDataConfiguration"; //$NON-NLS-1$

	protected ConfigurationStubJDBCMetaData(Object configuration) {
		super(configuration, CL);
	}
	
	public static ConfigurationStubJDBCMetaData newInstance() {
		return new ConfigurationStubJDBCMetaData(HObject.newInstance(CL));
	}

	public ConfigurationStubJDBCMetaData setProperties(Properties properties) {
		Object tmp = invoke(mn(), properties);
		return (tmp == Obj() ? this : new ConfigurationStubJDBCMetaData(tmp));
	}

	public void setPreferBasicCompositeIds(boolean flag) {
		invoke(mn(), flag);
	}

	public void setReverseEngineeringStrategy(ReverseEngineeringStrategyStub reverseEngineeringStrategy) {
		invoke(mn(), reverseEngineeringStrategy);
	}

	public void readFromJDBC() {
		invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TableStub> getTableMappingsArr() {
		ArrayList<TableStub> arr = new ArrayList<TableStub>(); 
		Iterator it = (Iterator)invoke(mn());
		while (it.hasNext() ) {
			Object obj = it.next();
			if (obj != null) {
				arr.add(new TableStub(obj));
			}
		}
		return arr;
	}

	public Iterator<TableStub> getTableMappingsIt() {
		return getTableMappingsArr().iterator();
	}

}
