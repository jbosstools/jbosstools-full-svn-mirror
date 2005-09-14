package org.hibernate.eclipse.mapper.registry;

import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;
import org.hibernate.eclipse.mapper.modelhandler.ModelHandlerForHBMXML;


public class AdapterFactoryProviderForHBMXML extends AdapterFactoryProviderForXML {
	
	/*
	 * @see AdapterFactoryProvider#isFor(ContentTypeDescription)
	 */
	public boolean isFor(IDocumentTypeHandler contentTypeDescription) {
		if(contentTypeDescription.getId().equals(ModelHandlerForHBMXML.ModelHandlerID)) {
			return true;
		} else {
			return false;
		}
	}

}
