package org.jboss.tools.smooks.javabean.model;

import java.util.Collection;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.javabean.ui.JavaImageConstants;
import org.jboss.tools.smooks.ui.SmooksUIActivator;
import org.jboss.tools.smooks.utils.SmooksGraphConstants;

public class BeanlabelProvider extends LabelProvider {

	public Image getJavaObjectImage() {
		return SmooksUIActivator.getDefault().getImageRegistry().get(
				JavaImageConstants.IMAGE_JAVA_OBJECT);
	}

	public Image getJavaAttributeImage() {
		return SmooksUIActivator.getDefault().getImageRegistry().get(
				JavaImageConstants.IMAGE_JAVA_ATTRIBUTE);
	}

	public String getText(Object element) {
		if (element instanceof JavaBeanModel) {
			String name = ((JavaBeanModel) element).getName();
			if (name == null)
				name = "<nonamed>";

			Object error = ((JavaBeanModel) element).getError();
			if (error != null) {
				name = name + "    " + "<" + error.toString() + ">";
				return name;
			}

			Class typeRef = ((JavaBeanModel) element).getBeanClass();
			String typeStr = "";
			if (typeRef != null) {
				if (typeRef.isArray()) {
					typeRef = typeRef.getComponentType();
					typeStr = typeRef.getName() + "[]";
				} else {
					typeStr = typeRef.getName();
				}
			}
			if (!typeStr.equals("")) {
				name = name + "       " + typeStr;
			}

			return name;
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof JavaBeanModel) {
			Object error = ((JavaBeanModel) element).getError();
			if (error != null) {
				return SmooksUIActivator.getDefault().getImageRegistry().get(
						SmooksGraphConstants.IMAGE_ERROR);
			}
			if (((JavaBeanModel) element).isPrimitive()) {
				return this.getJavaAttributeImage();
			} else {
				Class typeRef = ((JavaBeanModel) element).getBeanClass();
				if (typeRef != null) {
					if (typeRef.isArray()) {
						return SmooksUIActivator.getDefault()
								.getImageRegistry().get(
										JavaImageConstants.IMAGE_JAVA_ARRAY);
					}
					if (Collection.class.isAssignableFrom(typeRef)) {
						return SmooksUIActivator.getDefault()
								.getImageRegistry()
								.get(JavaImageConstants.IMAGE_JAVA_COLLECTION);
					}
					if (typeRef.isInterface()) {
						return SmooksUIActivator.getDefault()
								.getImageRegistry()
								.get(JavaImageConstants.IMAGE_JAVA_INTERFACE);
					}
				}
				return this.getJavaObjectImage();
			}
		}
		return super.getImage(element);
	}
}