package org.jboss.tools.bpmn2.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.ResourceHandler;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.jboss.tools.bpmn2.gmf.notation.BpmnNotationFactory;

public class Bpmn2ResourceFactoryImpl extends org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl {
	
	private ResourceHandler resourceHandler = new Bpmn2ResourceHandlerImpl();

    @Override
    public Resource createResource(URI uri) {
    	XMLResource result = (XMLResource)super.createResource(uri);
    	result.getDefaultLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);
    	result.getDefaultSaveOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);
        return result;
    }
    
    class Bpmn2ResourceHandlerImpl implements ResourceHandler {

		@Override
		public void preLoad(XMLResource resource, InputStream inputStream,
				Map<?, ?> options) {
		}

		@Override
		public void postLoad(XMLResource resource, InputStream inputStream,
				Map<?, ?> options) {
			TreeIterator<EObject> contents = resource.getAllContents();
			ArrayList<EObject> viewObjects = new ArrayList<EObject>();
			while (contents.hasNext()) {
				EObject obj = contents.next();
				if (obj instanceof BPMNDiagram) {
					viewObjects.add(createDiagram((BPMNDiagram)obj));
				}
			}
			for (EObject viewObject : viewObjects) {
				resource.getContents().add(viewObject);
			}
		}
		
		private Diagram createDiagram(BPMNDiagram bpmnDiagram) {
			return BpmnNotationFactory.INSTANCE.createDiagram(bpmnDiagram);
		}

		@Override
		public void preSave(XMLResource resource, OutputStream outputStream,
				Map<?, ?> options) {
		}
		
		private View getParentView(View candidate, View view) {
			for (Object child : candidate.getVisibleChildren()) {
				if (child == view) {
					return candidate;
				} else {
					return getParentView((View)child, view);
				}
			}
			return null;
		}

		@Override
		public void postSave(XMLResource resource, OutputStream outputStream,
				Map<?, ?> options) {
		}
		
    	
    }
    

    private void prepareSave(Resource resource) {
        EObject cur;
        for (Iterator<EObject> iter = resource.getAllContents(); iter.hasNext();) {
            cur = iter.next();

            EStructuralFeature idAttr = cur.eClass().getEIDAttribute();
            if (idAttr != null && !cur.eIsSet(idAttr)) {
                cur.eSet(idAttr, EcoreUtil.generateUUID());
            }
        }
    }
}
