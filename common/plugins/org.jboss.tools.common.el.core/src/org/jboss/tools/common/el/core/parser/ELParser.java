/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.el.core.parser;

import java.util.List;

import org.jboss.tools.common.el.core.model.ELModel;

/**
 * 
 * @author V. Kabanovich
 *
 */
public interface ELParser {

	public ELModel parse(String source);

	public List<SyntaxError> getSyntaxErrors();

}
