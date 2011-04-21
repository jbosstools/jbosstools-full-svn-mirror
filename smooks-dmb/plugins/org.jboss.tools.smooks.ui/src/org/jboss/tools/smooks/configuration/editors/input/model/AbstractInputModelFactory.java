/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input.model;

import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.configuration.editors.input.InputModelFactory;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.GlobalParams;
import org.jboss.tools.smooks.model.core.IParam;

/**
 * Abstract {@link InputModelFactory}.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
abstract class AbstractInputModelFactory implements InputModelFactory {

	protected IParam getInputSourceParam(String inputType, SmooksModel smooksConfigModel) {
		GlobalParams globalParams = smooksConfigModel.getParams();
		IParam inputSource = globalParams.getParam(inputType, SmooksModelUtils.INPUT_ACTIVE_TYPE);
		
		if(inputSource == null) {
			inputSource = globalParams.getParam(inputType);
		}
		
		return inputSource;
	}
}
