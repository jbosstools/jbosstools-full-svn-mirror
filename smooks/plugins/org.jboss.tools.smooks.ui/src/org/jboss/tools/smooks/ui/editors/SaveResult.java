/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import org.eclipse.ui.IEditorPart;

/**
 * @author Dart
 *
 */
public class SaveResult {
	private Throwable error;
	
	private IEditorPart sourceEdtior;

	public IEditorPart getSourceEdtior() {
		return sourceEdtior;
	}

	public void setSourceEdtior(IEditorPart sourceEdtior) {
		this.sourceEdtior = sourceEdtior;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}
	
}
