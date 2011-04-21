/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.custom.CustomTLDReference;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * 
 */
public class VpeCustomTemplate extends VpeIncludeTemplate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.vpe.editor.template.VpeIncludeTemplate#create(org.jboss
	 * .tools.vpe.editor.context.VpePageContext, org.w3c.dom.Node,
	 * org.mozilla.interfaces.nsIDOMDocument)
	 */
	@Override
	public VpeCreationData create(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument) {

		IPath pathToFile = CustomTLDReference
				.getCustomElementPath(sourceNode, pageContext);
		if (pathToFile != null) {
			//add attributes to EL list

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
					pathToFile);
			if (file != null && file.exists()) {
				if (!pageContext.getVisualBuilder().isFileInIncludeStack(
						file)) {
					Document document = pageContext.getVisualBuilder()
							.getIncludeDocuments().get(file);
					if (document == null) {
						document = VpeCreatorUtil.getDocumentForRead(file);
						if (document != null)
							pageContext.getVisualBuilder()
									.getIncludeDocuments().put(file, document);
					}
					if (document != null) {
						VpeCreationData creationData = createInclude(
								document, visualDocument);
						ResourceReference [] oldResourceReferences 
								= VpeCustomTemplate
										.addAttributesToELExcpressions(
												sourceNode, file);
						creationData.setData(new TransferObject(
								oldResourceReferences, file));
						pageContext.getVisualBuilder().pushIncludeStack(
								new VpeIncludeInfo((Element) sourceNode,
										file, document));
						return creationData;
					}
				}
			}
		}

		VpeCreationData creationData = createStub(sourceNode.getNodeName(),
				visualDocument);
		creationData.setData(null);
		return creationData;
	}	
	@Override
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data){
		if(data.getData() instanceof TransferObject) {
			TransferObject trObject = (TransferObject) data.getData();
			ELReferenceList.getInstance().setAllResources(
					trObject.getCustomFile(), trObject.getResourceReferebces());
			data.setData(trObject.getCustomFile());
		}
		
		super.validate(pageContext, sourceNode, visualDocument, data);
	}

	/**
	 * Temparary add to attribute for custom el expressions
	 * @param pageContext Page Context
	 * @param sourceNode source Node
	 * @param processedFile processed File
	 * @return resourceReferences - unchanged resource references
	 */
	private static final ResourceReference[] addAttributesToELExcpressions(
			final Node sourceNode, final IFile processedFile){
				
		//obtain old resource references for this file
		ResourceReference[] resourceReferences = ELReferenceList.getInstance()
				.getAllResources(processedFile);	
		//obtain attribute resource references for file
		NamedNodeMap attributesMap = sourceNode.getAttributes();
		List<ResourceReference>  addedAttrToElExpressions 
				= new ArrayList<ResourceReference>();
		for(int i=0;i<attributesMap.getLength();i++) {
			Attr attr = (Attr) attributesMap.item(i);
			//adds attribute if such attribute not exists for page
				ResourceReference resRef = new ResourceReference(attr.getName(),
						ResourceReference.FILE_SCOPE);
				resRef.setProperties(attr.getValue());
				addedAttrToElExpressions.add(resRef);
		}
		ELReferenceList.getInstance().setAllResources(processedFile,
				addedAttrToElExpressions.toArray(new ResourceReference[0]));
		return resourceReferences;
	}
	
	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		IFile file = null;
		if(data instanceof TransferObject) {
			file = ((TransferObject)data).getCustomFile();
		} else if(data instanceof IFile) {
			file = (IFile) data;
		}
		super.beforeRemove(pageContext, sourceNode, visualNode, file);
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeTemplate#getSourceRegionForOpenOn(org.mozilla.interfaces.nsIDOMNode)
	 *
	 */
	/**
	 * @author mareshkau
	 */
	@Override
	public IRegion getSourceRegionForOpenOn(VpePageContext pageContext, Node sourceNode ,nsIDOMNode domNode) {
			int offset = NodesManagingUtil.getStartOffsetNode(sourceNode);
			//calculate openOnPosition,prefixLengght+>+":"
			offset+=sourceNode.getPrefix().length()+1+1;
			return new Region(offset, 0); 
	}

	/**
	 * Object which used to passing argument throw template event processing,
	 * restore el expressions after processing custom template
	 * 
	 * @author mareshkau
	 *
	 */
	private class TransferObject {
		private ResourceReference [] resourceReferebces;
		private IFile customFile;
		
		/**
		 * @param resourceReferebces
		 * @param customFile
		 */
		public TransferObject(ResourceReference[] resourceReferebces,
				IFile customFile) {
			this.resourceReferebces = resourceReferebces;
			this.customFile = customFile;
		}
		/**
		 * @return the resourceReferebces
		 */
		public ResourceReference[] getResourceReferebces() {
			return this.resourceReferebces;
		}
		/**
		 * @return the customFile
		 */
		public IFile getCustomFile() {
			return this.customFile;
		}
	}
}
