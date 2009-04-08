/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.java2xml.analyzer;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.SourceModel;
import org.jboss.tools.smooks.ui.gef.model.TargetModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * 
 * @author <a href="mailto:dpeng@redhat.com">Dart Peng</a>
 * @Date 2008-9-22
 */
public class Java2XMLAnalyzer extends AbstractAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeMappingGraphModel(org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException {
		// TODO will modify soon
		if(true){
			Shell shell = context.getShell();
			MessageDialog.openWarning(shell, Messages.Java2XMLAnalyzer_Warning, Messages.Java2XMLAnalyzer_CantGenerateConfig); 
			return;
		}
		
		GraphRootModel rootModel = context.getGraphicalRootModel();
		List<SourceModel> sourceModelList = rootModel.loadSourceModelList();
		List<TargetModel> targetModelList = rootModel.loadTargetModelList();

		// Element rootElement = DocumentHelper.createElement(name);
		TagObject root = null;
		for (Iterator iterator = targetModelList.iterator(); iterator.hasNext();) {
			TargetModel targetModel = (TargetModel) iterator.next();
			AbstractXMLObject tag = (AbstractXMLObject) targetModel
					.getReferenceEntityModel();
			root = findTheRootTagObject(tag);
			break;
		}

		if (root == null)
			throw new SmooksAnalyzerException(Messages.Java2XMLAnalyzer_CantFindRoot); 
		Document document = DocumentHelper.createDocument();
		Element rootElement = generateXMLContents(rootModel, root);
		document.setRootElement(rootElement);

		String string = document.asXML();

		ResourceConfigType resource = SmooksFactory.eINSTANCE
				.createResourceConfigType();
		ParamType param = SmooksFactory.eINSTANCE.createParamType();
		resource.getParam().add(param);
		SmooksModelUtils.appendTextToSmooksType(param, string);
		context.getGeneratorResourceList().add(resource);
		System.out.println(string);
	}

	protected Element generateXMLContents(GraphRootModel graph, TagObject tag) {
		Element element = DocumentHelper.createElement(tag.getName());
		AbstractStructuredDataModel dataModel = UIUtils.findGraphModel(graph,
				tag);
		generateElementAttribute(graph, element, tag.getProperties());
		List children = tag.getXMLNodeChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			TagObject child = (TagObject) iterator.next();
			element.add(generateXMLContents(graph, child));
		}

		if (dataModel != null) {
			LineConnectionModel connection = UIUtils
					.getFirstTargetModelViaConnection(dataModel);
			if (connection != null) {
				SourceModel source = (SourceModel) connection.getSource();
				element
						.add(DocumentHelper
								.createCDATA(getTheJavaBeanString((JavaBeanModel) source
										.getReferenceEntityModel())));
			}
		}

		return element;
	}

	protected void generateElementAttribute(GraphRootModel graph,
			Element element, List<TagPropertyObject> properties) {
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator
					.next();

			AbstractStructuredDataModel dataModel = UIUtils.findGraphModel(
					graph, tagPropertyObject);
			LineConnectionModel connect = UIUtils
					.getFirstTargetModelViaConnection(dataModel);
			if (connect == null)
				continue;

			SourceModel source = (SourceModel) connect.getSource();

			element.addAttribute(tagPropertyObject.getName(),
					getTheJavaBeanString((JavaBeanModel) source
							.getReferenceEntityModel()));
		}
	}

	protected TagObject findTheRootTagObject(AbstractXMLObject obj) {
		AbstractXMLObject parent = obj.getParent();
		if (parent instanceof TagList && obj instanceof TagObject)
			return (TagObject) obj;
		return (TagObject) findTheRootTagObject(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeMappingSmooksModel(org.jboss.tools.smooks.model.SmooksResourceListType,
	 *      java.lang.Object, java.lang.Object)
	 */
	public MappingResourceConfigList analyzeMappingSmooksModel(
			SmooksResourceListType listType, Object sourceObject,
			Object targetObject) {
		throw new RuntimeException(Messages.Java2XMLAnalyzer_DontSupportJ2X); 
	}

	protected String getTheJavaBeanString(JavaBeanModel currentModel) {
		String name = currentModel.getName();
		JavaBeanModel parent = currentModel.getParent();
		while (parent != null ) {
			String pn = parent.getName();
			if (pn != null) {
				name = parent.getName() + "." + name; //$NON-NLS-1$
			}
			parent = parent.getParent();
		}
		return "${" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public DesignTimeAnalyzeResult[] analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
