/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

/**
 * @author Dart
 *
 */
public class Decorater {
	private String selector;

	public Decorater(String selector){
		setSelector(selector);
	}
	
	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}
}
