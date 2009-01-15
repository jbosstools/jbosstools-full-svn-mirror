/**
 * 
 */
package org.jboss.tools.smooks.test.java;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.model.AbstractResourceConfig;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.utils.UIUtils;

/**
 * @author Dart
 * 
 */
public class SelectorTester {

	/**
	 * 
	 * @param list
	 * @param source
	 * @param target
	 */
	public void validSmooksConfigFile(SmooksResourceListType list,
			IXMLStructuredObject source, IXMLStructuredObject target) {
		List<AbstractResourceConfig> resourceList = list
				.getAbstractResourceConfig();
		for (Iterator<AbstractResourceConfig> iterator = resourceList
				.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator
					.next();
			if (abstractResourceConfig instanceof ResourceConfigType) {
				ResourceConfigType config = (ResourceConfigType) abstractResourceConfig;
				ResourceType resource = config.getResource();
				if (resource != null) {
					if (JavaBeanAnalyzer.BEANPOPULATOR.equals(resource
							.getStringValue())) {
						IXMLStructuredObject model = UIUtils
								.localXMLNodeWithPath(config.getSelector(),
										source);
						Assert.assertNotNull(model);

						List bindingList = SmooksModelUtils
								.getBindingListFromResourceConfigType(config);

						for (Iterator iterator2 = bindingList.iterator(); iterator2
								.hasNext();) {
							AnyType binding = (AnyType) iterator2.next();
							String property = SmooksModelUtils
									.getAttributeValueFromAnyType(binding,
											SmooksModelUtils.ATTRIBUTE_PROPERTY);
							if (property != null) {
								property = property.trim();
							}
							String selector = SmooksModelUtils
									.getAttributeValueFromAnyType(binding,
											SmooksModelUtils.ATTRIBUTE_SELECTOR);
							if (selector != null) {
								selector = selector.trim();
							}

							if (selector.startsWith("${")) {
								continue;
							}

							IXMLStructuredObject childModel = UIUtils
									.localXMLNodeWithPath(config.getSelector(),
											source);
							Assert.assertNotNull(childModel);
						}
					}
				}
			}
		}
	}
}
