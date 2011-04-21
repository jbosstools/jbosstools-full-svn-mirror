/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.Collection;

import org.jboss.tools.smooks.model.ParamType;

/**
 * @author Dart
 *
 */
public interface ParamaterChangeLitener {
	public void paramaterAdded(ParamType param);
	
	public void paramaterRemoved(Collection<ParamType> params);
	
	public void paramaterChanged(ParamType param);
}
