/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.jface.viewers.LabelProvider;
import org.jboss.tools.smooks.analyzer.NormalSmooksModelBuilder;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.ResourceType;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class ConfigurationViewerLabelProvider extends LabelProvider {
	public String getText(Object element) {
		if (element instanceof ResourceConfigType) {
//			if (SmooksModelUtils
//					.isBeanPopulatorResource((ResourceConfigType) element)) {
//				String selector = ((ResourceConfigType) element).getSelector();
//				if (selector == null)
//					selector = Messages
//							.getString("SmooksResourceConfigFormBlock.NULLString"); //$NON-NLS-1$
//				return Messages
//						.getString("SmooksResourceConfigFormBlock.BeanPopulator") + selector; //$NON-NLS-1$
//			}
			if (SmooksModelUtils
					.isDateTypeSelector((ResourceConfigType) element)) {
				String name = "Date Type"; //$NON-NLS-1$
				String selector = ((ResourceConfigType) element).getSelector();
				if (selector == null) {
					return name;
				}
				selector = selector.trim();
				if (selector.indexOf(":") != -1) {
					selector = selector.substring(selector.indexOf(":") + 1,
							selector.length());
				}
				return name + "(" + selector + ")";
			}
			if (SmooksModelUtils
					.isFilePathResourceConfig((ResourceConfigType) element)) {
				String selector = ((ResourceConfigType) element).getSelector();
				if (selector == null)
					selector = Messages
							.getString("SmooksResourceConfigFormBlock.NULLString"); //$NON-NLS-1$
				return Messages
						.getString("SmooksResourceConfigFormBlock.TemplateFile") + selector; //$NON-NLS-1$
			}
			if (SmooksModelUtils
					.isInnerFileContents((ResourceConfigType) element)) {
				ResourceType re = ((ResourceConfigType) element).getResource();
				String type = re.getType();
				String label = Messages
				.getString("SmooksResourceConfigFormBlock.TemplateFile") + "(" + type + ")";
				return label;
			}
			String s = ((ResourceConfigType) element).getSelector();
			if (s == null)
				s = Messages
						.getString("SmooksResourceConfigFormBlock.NULLString"); //$NON-NLS-1$
			return "ResourceConfig (" + s + ")"; //$NON-NLS-1$
		}
		return super.getText(element);
	}
}
