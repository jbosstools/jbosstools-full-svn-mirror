/**
 * 
 */
package org.jboss.tools.smooks.xml2xml;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.analyzer.AbstractAnalyzer;
import org.jboss.tools.smooks.analyzer.DesignTimeAnalyzeResult;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.analyzer.SmooksAnalyzerException;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author dart
 * 
 */
public class XML2XMLAnalyzer extends AbstractAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.analyzer.IMappingAnalyzer#analyzeMappingGraphModel(org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public void analyzeMappingGraphModel(
			SmooksConfigurationFileGenerateContext context)
			throws SmooksAnalyzerException {
		if (true) {
			Shell shell = context.getShell();
			MessageDialog.openWarning(shell, "Warning",
					"The xml2xml can't be generate to config file currently.");
			return;
		}
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
		return null;
	}

	public DesignTimeAnalyzeResult analyzeGraphModel(
			SmooksConfigurationFileGenerateContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
