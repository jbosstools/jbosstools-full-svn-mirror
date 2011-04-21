/**
 * 
 */
package org.jboss.tools.smooks.javabean.analyzer;

import org.jboss.tools.smooks.analyzer.ResolveCommand;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 *
 */
public class JavaModelResolveCommand extends ResolveCommand {
	
	private String instanceName = null;
	
	private JavaBeanModel javaBean;

	public JavaBeanModel getJavaBean() {
		return javaBean;
	}

	public void setJavaBean(JavaBeanModel javaBean) {
		this.javaBean = javaBean;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public JavaModelResolveCommand(
			SmooksConfigurationFileGenerateContext context) {
		super(context);
	}

	@Override
	public void execute() throws Exception {
		getJavaBean().setBeanClassString(instanceName);
	}
}
