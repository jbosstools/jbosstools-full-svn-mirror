package org.hibernate.mediator.stubs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.cfg.Environment;

public class EnvironmentStub {
	public static final String CL = "org.hibernate.cfg.Environment"; //$NON-NLS-1$

	public static final String DIALECT = Environment.DIALECT;
	public static final String DEFAULT_CATALOG = Environment.DEFAULT_CATALOG;
	public static final String DEFAULT_SCHEMA = Environment.DEFAULT_SCHEMA;
	public static final String DRIVER = Environment.DRIVER;
	public static final String PASS = Environment.PASS;
	public static final String SESSION_FACTORY_NAME = Environment.SESSION_FACTORY_NAME;
	public static final String URL = Environment.URL;
	public static final String USER = Environment.USER;
	public static final String HBM2DDL_AUTO = Environment.HBM2DDL_AUTO;
	public static final String DATASOURCE = Environment.DATASOURCE;

	@SuppressWarnings("unchecked")
	public static String[] extractHibernateProperties() {
		try {
			// TODO: extract property names from the Environment class in the users hibernate
			// configuration.
			Class cl = Environment.class;
			List<String> names = new ArrayList<String>();
			Field[] fields = cl.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(String.class)) {
					String str = (String) field.get(cl);
					if (str.startsWith("hibernate.")) { //$NON-NLS-1$
						names.add(str);
					}
				}
			}
			String[] propertyNames = (String[]) names.toArray(new String[names.size()]);
			Arrays.sort(propertyNames);
			return propertyNames;
		} catch (IllegalAccessException iae) {
			// ignore
			return new String[0];
		}
	}
}
