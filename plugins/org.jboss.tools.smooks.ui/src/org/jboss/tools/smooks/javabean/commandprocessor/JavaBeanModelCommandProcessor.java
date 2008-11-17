/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.javabean.commandprocessor;

import org.eclipse.emf.common.command.Command;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class JavaBeanModelCommandProcessor implements ICommandProcessor {

	public void processEMFCommand(Command emfCommand) {
	}

	public void processGEFCommand(org.eclipse.gef.commands.Command gefCommand) {
		if (CreateConnectionCommand.class.isAssignableFrom(gefCommand
				.getClass())) {
			CreateConnectionCommand command = (CreateConnectionCommand) gefCommand;
			if (command.getSource() != null && command.getTarget() != null) {
				Object m = command.getTarget();
				Object s = command.getSource();
				if (m instanceof AbstractStructuredDataModel && s instanceof AbstractStructuredDataModel) {
					Object source = ((AbstractStructuredDataModel)s).getReferenceEntityModel();
					Object t = ((AbstractStructuredDataModel)m).getReferenceEntityModel();
					if (!UIUtils.isInstanceCreatingConnection(source,t)) {
						if (t instanceof JavaBeanModel) {
							Class clazz = ((JavaBeanModel) t).getBeanClass();
							if (clazz != null && clazz != String.class) {
								PropertyModel property = new PropertyModel();
								property.setName("type");
								property.setValue(getTypeString(clazz));
								command.addPropertyModel(property);
							}
						}
					}
				}
			}
		}
	}

	public static String getTypeString(Class clazz) {
		if(clazz.isPrimitive()){
			return getPrimitiveTypeString(clazz);
		}
		String name = clazz.getSimpleName();
		return name;
	}
	
	public static String getPrimitiveTypeString(Class clazz){
		String name = clazz.getName();
		if("int".equalsIgnoreCase(name)) return "Integer";
		if("double".equalsIgnoreCase(name)) return "Double";
		if("float".equalsIgnoreCase(name)) return "Float";
		if("long".equalsIgnoreCase(name)) return "Long";
		if("boolean".equalsIgnoreCase(name)) return "Boolean";
		return "";
	}

}
