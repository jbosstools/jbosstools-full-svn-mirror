package org.jboss.tools.flow.editor.strategy;

import org.jboss.tools.flow.common.core.Container;

public interface AcceptsElementStrategy {
	
	void setContainer(Container container);

	boolean acceptsElement(Object element);
	
}
