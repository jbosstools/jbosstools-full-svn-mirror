package org.jboss.tools.smooks.javabean.model;

import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.modelparser.IStructuredModelParser;

/**
 * 
 * @author Dart Peng
 * 
 */
public class JavaBeanParser implements IStructuredModelParser {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.ui.modelparser.IStructuredModelParser#parse(java
	 * .lang.Object)
	 */
	public AbstractStructuredDataModel parse(Object customModel) {
		if (customModel instanceof JavaBeanModel) {
			AbstractStructuredDataModel model = null;
			String name = ((JavaBeanModel) customModel).getName();

			if (((JavaBeanModel) customModel).isRootClassModel()) {
				model = new JavaStructuredDataModel();
				((JavaStructuredDataModel) model).setLabelName(name);
			} else {
				model = new JavabeanStructuredContentsModel();
				((JavabeanStructuredContentsModel) model).setName(name);
				((JavabeanStructuredContentsModel) model).setTypeString(((JavaBeanModel) customModel).getBeanClass().getName());
			}
			model.setReferenceEntityModel(customModel);
			return model;
		}
		return null;
	}

}
