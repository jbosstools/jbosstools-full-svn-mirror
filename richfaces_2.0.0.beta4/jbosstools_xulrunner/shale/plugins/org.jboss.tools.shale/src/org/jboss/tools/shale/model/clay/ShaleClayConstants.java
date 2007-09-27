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
package org.jboss.tools.shale.model.clay;

import org.jboss.tools.jsf.web.ConfigFilesData;


public interface ShaleClayConstants {
    public String DOC_QUALIFIEDNAME = "view";
	public String DOC_PUBLICID = "-//Apache Software Foundation//DTD Shale Clay View Configuration 1.0//EN";
	public String DOC_EXTDTD = "http://struts.apache.org/dtds/shale-clay-config_1_0.dtd";
	
	public String ENT_SHALE_CLAY = "FileShaleClay";
	public String COMPONENT_ENTITY = "ShaleClayComponent";
	public String ATTRIBUTES_ENTITY = "ShaleClayAttributes";
	public String SYMBOLS_ENTITY = "ShaleClaySymbols";
	public String SET_ENTITY = "ShaleClaySet";
	public String CONVERTER_ENTITY = "ShaleClayConverter";
	public String VALIDATOR_FOLDER_ENTITY = "ShaleClayValidatorFolder";
	public String VALIDATOR_ENTITY = "ShaleClayValidator";
	public String ACTION_LISTENER_FOLDER_ENTITY = "ShaleClayActionListenerFolder";
	public String ACTION_LISTENER_ENTITY = "ShaleClayActionListener";
	public String VALUE_CHANGE_LISTENER_FOLDER_ENTITY = "ShaleClayValueListenerFolder";
	public String VALUE_CHANGE_LISTENER_ENTITY = "ShaleClayValueListener";
	public String ELEMENT_FOLDER_ENTITY = "ShaleClayElementFolder";
	public String ELEMENT_ENTITY = "ShaleClayElement";
	
    public String ELM_PROCESS = "process";
	public String ENT_PROCESS = "ShaleClayProcess";
	public String ENT_PROCESS_ITEM = "ShaleClayProcessItem";
	public String ENT_PROCESS_ITEM_OUTPUT = "ShaleClayProcessItemOutput";
	
	public String ATT_NAME = "name";
	public String ATT_JSFID = "jsf id";
	public String ATT_PATH = "path";
	public String ATT_TARGET = "target";
	public String ATT_EXTENDS = "extends";
	
	public ConfigFilesData REGISTRATION_DATA = new ConfigFilesData(
			"clay-config-files",
			new String[0]
	);
}
