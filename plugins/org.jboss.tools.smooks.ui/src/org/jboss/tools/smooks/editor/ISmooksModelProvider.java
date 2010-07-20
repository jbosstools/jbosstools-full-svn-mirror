/**
 * 
 */
package org.jboss.tools.smooks.editor;

import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.command.SmooksCommandStack;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 *
 */
public interface ISmooksModelProvider {
	Model<SmooksModel> getSmooksModel();
	
	SmooksCommandStack getCommandStack();
}
