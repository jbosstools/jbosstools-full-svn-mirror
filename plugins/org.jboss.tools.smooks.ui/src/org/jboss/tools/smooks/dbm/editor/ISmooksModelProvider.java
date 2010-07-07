/**
 * 
 */
package org.jboss.tools.smooks.dbm.editor;

import org.jboss.tools.smooks.model.SmooksModel;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 *
 */
public interface ISmooksModelProvider {
	Model<SmooksModel> getSmooksModel();
}
