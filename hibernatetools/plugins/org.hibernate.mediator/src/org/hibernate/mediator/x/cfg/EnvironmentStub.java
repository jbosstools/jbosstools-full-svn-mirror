package org.hibernate.mediator.x.cfg;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.stubs.util.ReflectHelper;

public class EnvironmentStub {
	public static final String CL = "org.hibernate.cfg.Environment"; //$NON-NLS-1$

	public static final String DIALECT = (String)HObject.readStaticFieldValue(CL, "DIALECT"); //$NON-NLS-1$
	public static final String DEFAULT_CATALOG = (String)HObject.readStaticFieldValue(CL, "DEFAULT_CATALOG"); //$NON-NLS-1$
	public static final String DEFAULT_SCHEMA = (String)HObject.readStaticFieldValue(CL, "DEFAULT_SCHEMA"); //$NON-NLS-1$
	public static final String DRIVER = (String)HObject.readStaticFieldValue(CL, "DRIVER"); //$NON-NLS-1$
	public static final String PASS = (String)HObject.readStaticFieldValue(CL, "PASS"); //$NON-NLS-1$
	public static final String SESSION_FACTORY_NAME = (String)HObject.readStaticFieldValue(CL, "SESSION_FACTORY_NAME"); //$NON-NLS-1$
	public static final String URL = (String)HObject.readStaticFieldValue(CL, "URL"); //$NON-NLS-1$
	public static final String USER = (String)HObject.readStaticFieldValue(CL, "USER"); //$NON-NLS-1$
	public static final String HBM2DDL_AUTO = (String)HObject.readStaticFieldValue(CL, "HBM2DDL_AUTO"); //$NON-NLS-1$
	public static final String DATASOURCE = (String)HObject.readStaticFieldValue(CL, "DATASOURCE"); //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	public static String[] extractHibernateProperties() {
		try {
			// TODO: extract property names from the Environment class in the users hibernate
			// configuration.
			Class cl = ReflectHelper.classForName(CL);
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
		} catch (ClassNotFoundException e) {
			// ignore
			return new String[0];
		} catch (IllegalAccessException iae) {
			// ignore
			return new String[0];
		}
	}
}
