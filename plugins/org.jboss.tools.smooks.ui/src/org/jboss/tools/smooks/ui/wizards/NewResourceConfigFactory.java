/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.wizards;

import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;

/**
 * @author Dart Peng<br>
 *         Date : Sep 18, 2008
 */
public class NewResourceConfigFactory implements INewResourceConfigFactory {
	
	private static NewResourceConfigFactory instance = null;

	private NewResourceConfigFactory(){
		
	}
	
	public static synchronized NewResourceConfigFactory getInstance() {
		if(instance == null) instance = new NewResourceConfigFactory();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.ui.INewResourceConfigFactory#createNewResourceConfig(java.lang.String)
	 */
	public ResourceConfigType createNewResourceConfig(NewResourceConfigKey key) {
		if (SmooksModelConstants.BEAN_POPULATOR.equals(key.getId())) {
			ResourceConfigType config = SmooksFactory.eINSTANCE
					.createResourceConfigType();
			ResourceType resource = SmooksFactory.eINSTANCE
					.createResourceType();
			config.setResource(resource);
			resource.setValue(SmooksModelConstants.BEAN_POPULATOR);

			ParamType idParmType = SmooksFactory.eINSTANCE.createParamType();
			ParamType classParmType = SmooksFactory.eINSTANCE.createParamType();
			ParamType bindingParmType = SmooksFactory.eINSTANCE
					.createParamType();
			idParmType.setName(SmooksModelConstants.BEAN_ID);
			classParmType.setName(SmooksModelConstants.BEAN_CLASS);

			config.getParam().add(idParmType);
			config.getParam().add(classParmType);
			config.getParam().add(bindingParmType);
			return config;
		}
		if (SmooksModelConstants.DATE_DECODER.equals(key.getId())) {
			ResourceConfigType config = SmooksFactory.eINSTANCE
					.createResourceConfigType();
			ResourceType resource = SmooksFactory.eINSTANCE
					.createResourceType();
			config.setResource(resource);

			resource.setValue(SmooksModelConstants.DATE_DECODER);

			ParamType formate = SmooksFactory.eINSTANCE.createParamType();
			ParamType language = SmooksFactory.eINSTANCE.createParamType();
			ParamType contry = SmooksFactory.eINSTANCE.createParamType();
			formate.setName(SmooksModelConstants.FORMATE);
			language.setName(SmooksModelConstants.LOCALE_LANGUAGE);
			contry.setName(SmooksModelConstants.LOCALE_CONTRY);

			config.getParam().add(formate);
			config.getParam().add(language);
			config.getParam().add(contry);

			return config;
		}
		return null;
	}

	public NewResourceConfigKey[] getAllIDs() {
		NewResourceConfigKey bean = new NewResourceConfigKey();
		bean.setId(SmooksModelConstants.BEAN_POPULATOR);
		bean.setName("BeanPopulator");

		NewResourceConfigKey date = new NewResourceConfigKey();
		date.setId(SmooksModelConstants.DATE_DECODER);
		date.setName("Date Decoder");
		return new NewResourceConfigKey[] { bean, date };
	}

}
