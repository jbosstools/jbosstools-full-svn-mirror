package org.jboss.tools.bpmn2.process.diagram.sheet;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class Bpmn2PropertyPageAdapterFactoryContentProvider extends AdapterFactoryContentProvider {

	public Bpmn2PropertyPageAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	public Object [] getElements(Object object) {
		return new Object[] { object };
	}

}
