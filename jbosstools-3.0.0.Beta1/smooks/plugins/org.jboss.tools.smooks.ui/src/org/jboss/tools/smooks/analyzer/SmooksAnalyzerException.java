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
package org.jboss.tools.smooks.analyzer;

/**
 * @author Dart Peng
 * @Date Aug 19, 2008
 */
public class SmooksAnalyzerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7107932620709349894L;
	
	
	public SmooksAnalyzerException(String errorMessage){
		super(errorMessage);
	}
}
