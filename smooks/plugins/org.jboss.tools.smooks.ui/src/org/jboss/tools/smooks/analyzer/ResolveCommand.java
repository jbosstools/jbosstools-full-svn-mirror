/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 *
 */
public class ResolveCommand {
	private SmooksConfigurationFileGenerateContext context;
	
	private String resolveDescription ;
	
	private Image image;

	public void setResolveDescription(String resolveDescription) {
		this.resolveDescription = resolveDescription;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ResolveCommand(SmooksConfigurationFileGenerateContext context){
		this.context = context;
	}
	
	public SmooksConfigurationFileGenerateContext getContext() {
		return context;
	}
	
	public void execute() throws Exception{
		
	}
	
	public Image getImage(){
		return image;
	}
	
	public String getResolveDescription(){
		return resolveDescription;
	}
	
}
