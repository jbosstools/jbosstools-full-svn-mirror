/**
 * 
 */
package org.jboss.tools.smooks.editor;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.jboss.tools.smooks.model.SmooksModel;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 *
 */
public interface ISmooksModelProvider extends  IEditingDomainProvider{
	Model<SmooksModel> getSmooksModel();
}
