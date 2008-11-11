/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

/**
 * @author Dart
 *
 */
public interface ISaveListener {
	public void preSave(SaveResult result);
	
	public void endSave(SaveResult result);
}
