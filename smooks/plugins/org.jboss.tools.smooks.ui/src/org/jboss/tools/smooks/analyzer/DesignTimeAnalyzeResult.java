/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dart
 *
 */
public class DesignTimeAnalyzeResult {
	protected String errorMessage;
	protected String warningMessage;
	protected List<ResolveCommand> resolveCommandList = new ArrayList<ResolveCommand>();
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getWarningMessage() {
		return warningMessage;
	}
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	
	public void addResolveCommand(ResolveCommand command){
		this.resolveCommandList.add(command);
	}
	
	public void removeResolveCommand(ResolveCommand command){
		this.resolveCommandList.remove(command);
	}
	
	public List<ResolveCommand> getResolveProblem(){
		return resolveCommandList;
	}
	
	
}
