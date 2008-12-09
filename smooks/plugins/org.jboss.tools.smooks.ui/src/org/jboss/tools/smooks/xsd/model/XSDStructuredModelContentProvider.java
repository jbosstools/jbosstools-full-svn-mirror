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
package org.jboss.tools.smooks.xsd.model;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.xsd.provider.XSDItemProviderAdapterFactory;


/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 24, 2008
 */
public class XSDStructuredModelContentProvider extends AdapterFactoryContentProvider {
	public XSDStructuredModelContentProvider() {
		super(new XSDItemProviderAdapterFactory());
	}
}
