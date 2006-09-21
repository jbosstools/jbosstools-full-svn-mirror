package org.jboss.ide.eclipse.jbosscache.editors;

import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;

public class AdapterFactoryProviderForCacheXML extends AdapterFactoryProviderForXML {
	
	public boolean isFor(IDocumentTypeHandler contentTypeDescription) {
		return (contentTypeDescription instanceof ModelHandlerForCacheXML);
	}


}
