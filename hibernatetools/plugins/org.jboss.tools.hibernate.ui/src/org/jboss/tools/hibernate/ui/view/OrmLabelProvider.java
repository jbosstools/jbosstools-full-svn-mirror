/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.stubs.ColumnStub;
import org.hibernate.console.stubs.ConfigurationStub;
import org.hibernate.console.stubs.DialectStub;
import org.hibernate.console.stubs.EnvironmentStub;
import org.hibernate.console.stubs.MappingStub;
import org.hibernate.console.stubs.PropertyStub;
import org.hibernate.console.stubs.RootClassStub;
import org.hibernate.eclipse.console.HibernateConsolePlugin;

/**
 *
 */
public class OrmLabelProvider extends LabelProvider implements IColorProvider, IFontProvider {

	private Map<ImageDescriptor, Image> imageCache = new HashMap<ImageDescriptor, Image>(25);
	
	protected String consoleConfigName;
	protected MappingStub mapping = null;
	protected DialectStub dialect = null;

	public OrmLabelProvider() {
	}

	public OrmLabelProvider(String consoleConfigName) {
		super();
		setConsoleConfigName(consoleConfigName);
	}

	public void setConsoleConfigName(String consoleConfigName) {
		if (this.consoleConfigName == consoleConfigName) {
			return;
		}
		this.consoleConfigName = consoleConfigName;
		mapping = null;
		dialect = null;
	}

	protected ConfigurationStub getConfig() {
		final ConsoleConfiguration consoleConfig = getConsoleConfig();
		if (consoleConfig != null) {
			ConfigurationStub config = consoleConfig.getConfiguration();
			if (config == null) {
				try {
    				consoleConfig.build();
    				consoleConfig.buildMappings();
				} catch (RuntimeException he) {
					// here just ignore this
				}
				config = consoleConfig.getConfiguration();
			}
			return config;
		}
		return null;
	}

	protected ConsoleConfiguration getConsoleConfig() {
		final KnownConfigurations knownConfigurations = KnownConfigurations.getInstance();
		ConsoleConfiguration consoleConfig = knownConfigurations.find(consoleConfigName);
		return consoleConfig;
	}

	@Override
	public Image getImage(Object element) {
		ImageDescriptor descriptor = OrmImageMap.getImageDescriptor(element);
		if (descriptor == null) {
			return null;
		}
		Image image = imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}

	@Override
	public String getText(Object obj) {
		if (obj instanceof ColumnStub) {
			updateColumnSqlType((ColumnStub)obj);
		}
		return OrmLabelMap.getLabel(obj);
	}

	public void dispose() {
		for (Iterator<Image> i = imageCache.values().iterator(); i.hasNext();) {
			i.next().dispose();
		}
		imageCache.clear();
	}

	public Color getForeground(Object element) {
		if (element instanceof RootClassStub) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
		} else if (element instanceof PropertyStub) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
		}
		return null;
	}

	public Color getBackground(Object element) {
		return null;
	}

	public Font getFont(Object element) {
		//return JFaceResources.getFontRegistry().getBold(JFaceResources.getTextFont().getFontData()[0].getName());
		return null;
	}
	
	/**
	 * For correct label creation should update column sql type.
	 * @param column
	 * @return
	 */
	public boolean updateColumnSqlType(final ColumnStub column) {
		String sqlType = column.getSqlType();
		if (sqlType != null) {
			return false;
		}
		final ConfigurationStub config = getConfig();
		if (mapping == null && config != null) {
			mapping = config.buildMapping();
		}
		if (dialect == null && config != null) {
			final String dialectName = config.getProperty(EnvironmentStub.DIALECT);
			if (dialectName != null) {
				try {
					dialect = DialectStub.newInstance(dialectName);
				} catch (InstantiationException e) {
					HibernateConsolePlugin.getDefault().logErrorMessage("Exception: ", e); //$NON-NLS-1$
				} catch (IllegalAccessException e) {
					HibernateConsolePlugin.getDefault().logErrorMessage("Exception: ", e); //$NON-NLS-1$
				} catch (ClassNotFoundException e) {
					HibernateConsolePlugin.getDefault().logErrorMessage("Exception: ", e); //$NON-NLS-1$
				}
			}
		}
		if (dialect != null) {
			try {
				sqlType = column.getSqlType(dialect, mapping);
			} catch (Exception ex) {
				// ignore it
			}
		}
		if (sqlType != null) {
			column.setSqlType(sqlType);
			return true;
		}
		return false;
	}

}