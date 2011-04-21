/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal.context.java;

import org.eclipse.jpt.core.context.java.JavaGenerator;

/**
 * @author Dmitry Geraskov
 *
 */
public interface JavaGenericGenerator extends JavaGenerator, GenericGenerator {
	
	void initialize(GenericGeneratorAnnotation generator);
	
	void update(GenericGeneratorAnnotation generator);

}

