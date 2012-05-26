/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.options;

import java.util.Properties;

import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelConstants;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelFactory;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.ModelFeatureFactory;

public class PreferenceModelUtilities {
	static final String ENT_OPTION_ROOT = "OptionRoot"; //$NON-NLS-1$

	private static class PreferenceModelHolder {
		public static XModel preferenceModel;

		static {
				String f = ModelPlugin.getDefault().getStateLocation().toString();
				Properties p = new Properties();
				p.setProperty(XModelConstants.WORKSPACE, f);
				preferenceModel = createPreferenceModel(p);
				ServiceDialog d = createServiceDialog();
				if(d != null) {
					d.setModel(preferenceModel);
					preferenceModel.setService(d);
				}
		}

		private static ServiceDialog createServiceDialog() {
			return (ServiceDialog)ModelFeatureFactory.getInstance().createFeatureInstance("org.jboss.tools.common.model.ui.wizards.one.ServiceDialogImpl"); //$NON-NLS-1$
		}
		
	}
	
	public static XModel getPreferenceModel() {
		return PreferenceModelHolder.preferenceModel;
	}
	
	public static XModel createPreferenceModel(Properties p) {
		p.putAll(System.getProperties());
		p.setProperty(XModelObjectConstants.PROP_ROOT_ENTITY, ENT_OPTION_ROOT);
		return XModelFactory.getModel(p);
	}
	
	public static void initPreferenceValue(XModel initialModel, Preference preference)
	throws XModelException {
		String value = preference.getValue(); 
		if (value == null || "".equals(value)) //$NON-NLS-1$
		{
			XModelObject object = initialModel.getByPath(preference.getModelPath());
			if (object != null)
			{
				String newValue = object.getAttributeValue(preference.getName());
				if (newValue != null && !newValue.equals(value))
					preference.setValue(newValue);
			}
		}
	}
}
