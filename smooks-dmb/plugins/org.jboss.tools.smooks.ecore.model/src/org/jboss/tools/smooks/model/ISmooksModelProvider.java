/**
 * 
 */
package org.jboss.tools.smooks.model;

import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.milyn.javabean.dynamic.Model;

/**
 * @author Dart
 *
 */
public interface ISmooksModelProvider extends  IEditingDomainProvider{
	Model<SmooksModel> getSmooksModel();
	
	String getInputType();
}
