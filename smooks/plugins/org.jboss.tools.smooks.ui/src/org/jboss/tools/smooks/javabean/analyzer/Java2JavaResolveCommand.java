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
public class Java2JavaResolveCommand extends ResolveCommand {

	private JavaBeanModel sourceModel;
	
	private JavaBeanModel targetModel;
	
	
	
	public JavaBeanModel getSourceModel() {
		return sourceModel;
	}

	public void setSourceModel(JavaBeanModel sourceModel) {
		this.sourceModel = sourceModel;
	}

	public JavaBeanModel getTargetModel() {
		return targetModel;
	}

	public void setTargetModel(JavaBeanModel targetModel) {
		this.targetModel = targetModel;
	}

	public Java2JavaResolveCommand(
			SmooksConfigurationFileGenerateContext context) {
		super(context);
	}

	@Override
	public void execute() throws Exception {
		
		SmooksConfigurationFileGenerateContext context = getContext();
		if(context == null) throw new RuntimeException("Smooks generated context is NULL");
		
	}
}
