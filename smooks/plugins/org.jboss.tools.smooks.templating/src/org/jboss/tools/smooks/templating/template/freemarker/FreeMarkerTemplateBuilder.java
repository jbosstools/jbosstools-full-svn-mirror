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
package org.jboss.tools.smooks.templating.template.freemarker;

import org.jboss.tools.smooks.templating.model.ModelBuilder;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.jboss.tools.smooks.templating.template.TemplateBuilder;

/**
 * Abstract FreeMarker template builder.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public abstract class FreeMarkerTemplateBuilder extends TemplateBuilder {

	private boolean nodeModelSource = false;
	
	public FreeMarkerTemplateBuilder(ModelBuilder modelBuilder) throws ModelBuilderException {
		super(modelBuilder);
	}

	public boolean isNodeModelSource() {
		return nodeModelSource;
	}

	public void setNodeModelSource(boolean nodeModelSource) {
		this.nodeModelSource = nodeModelSource;
	}
}
