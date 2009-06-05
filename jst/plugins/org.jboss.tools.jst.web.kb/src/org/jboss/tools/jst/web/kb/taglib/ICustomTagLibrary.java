/******************************************************************************* 
 * Copyright (c) 2009 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.taglib;

import java.util.Set;

import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;

/**
 * @author Alexey Kazakov
 */
public interface ICustomTagLibrary extends ITagLibrary {

	/**
	 * @param query
	 * @param context
	 * @return Components with "extended" flag. See org.jboss.tools.jst.web.kb.taglib.ICustomTagLibComponent#isExtended()
	 */
	Set<ICustomTagLibComponent> getExtendedComponents(KbQuery query, IPageContext context);
}