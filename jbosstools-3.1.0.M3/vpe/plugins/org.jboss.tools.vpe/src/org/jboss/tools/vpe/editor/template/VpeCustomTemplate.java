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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.custom.CustomTLDReference;
import org.jboss.tools.vpe.editor.template.custom.VpeCustomStringStorage;
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

		IStorage sourceFileStorage = CustomTLDReference
				.getCustomElementPath(sourceNode, pageContext);

		if (sourceFileStorage instanceof IFile) {
			//add attributes to EL list
			IFile file =(IFile) sourceFileStorage;
			if (file.exists()) {
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
						return createCreationData(pageContext, sourceNode, file, document, visualDocument);
					}
				}
			}
		}else if(sourceFileStorage instanceof VpeCustomStringStorage) {
			VpeCustomStringStorage customStringStorage = (VpeCustomStringStorage) sourceFileStorage;
			if (!pageContext.getVisualBuilder().isFileInIncludeStack(
					customStringStorage)) {
				Document document = pageContext.getVisualBuilder()
						.getIncludeDocuments().get(customStringStorage);
				if (document == null) {
					document = VpeCreatorUtil.getDocumentForRead(customStringStorage.getContentString());
					if (document != null)
						pageContext.getVisualBuilder()
								.getIncludeDocuments().put(customStringStorage, document);
				}
				if (document != null) {
					return createCreationData(pageContext, sourceNode, customStringStorage, document, visualDocument);
				}
			}
		}
		VpeCreationData creationData = createStub(sourceNode.getNodeName(),
				visualDocument);
		creationData.setData(null);
		return creationData;
	}
	
	private VpeCreationData createCreationData(VpePageContext pageContext,Node sourceNode,
			IStorage storage,
			Document document, nsIDOMDocument visualDocument) {
		VpeCreationData creationData = createInclude(
				document, visualDocument);

			VpeCustomTemplate.addAttributesToELExcpressions(
										sourceNode, pageContext);
				creationData.setData(storage);
		pageContext.getVisualBuilder().pushIncludeStack(
				new VpeIncludeInfo((Element) sourceNode,
						storage, document));
		return creationData;
	}
	
	@Override
	public void validate(VpePageContext pageContext, Node sourceNode,
			nsIDOMDocument visualDocument, VpeCreationData data){
		NamedNodeMap attributesMap = sourceNode.getAttributes();
		for(int i=0;i<attributesMap.getLength();i++) {
			Attr attr = (Attr) attributesMap.item(i);
			pageContext.removeAttributeFromCustomElementMap(attr.getName());
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
	private static final void addAttributesToELExcpressions(
			final Node sourceNode, final VpePageContext vpePageContext){
		NamedNodeMap attributesMap = sourceNode.getAttributes();

		for(int i=0;i<attributesMap.getLength();i++) {
			Attr attr = (Attr) attributesMap.item(i);
			vpePageContext.addAttributeInCustomElementsMap(attr.getName(), attr.getValue());
		}
	}
	
	@Override
	public void beforeRemove(VpePageContext pageContext, Node sourceNode,
			nsIDOMNode visualNode, Object data) {
		IFile file = null;
		if(data instanceof IFile) {
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
}
