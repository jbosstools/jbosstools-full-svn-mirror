/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

/**
 * @author root
 * 
 */
public interface IValidatable {
	public void setWarning(Object warning);

	public Object getWarning();

	public void setError(Object error);

	public Object getError();
}
