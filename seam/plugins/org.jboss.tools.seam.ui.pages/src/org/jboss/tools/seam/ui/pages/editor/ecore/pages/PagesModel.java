package org.jboss.tools.seam.ui.pages.editor.ecore.pages;


/** 
 * @author daniel
 * 
 * Pages.xml model interface
 * 
 * @model
 */
public interface PagesModel extends PagesElement {

	public PagesElement findElement(Object data);

	public void load();

}
