/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui.action;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.smooks.javabean.model.JavaBeanModel;
import org.jboss.tools.smooks.ui.popup.SmooksAction;

/**
 * @author Dart
 *
 */
public class JavaBeanModelAction extends SmooksAction{
	public List getJavaBeanModelList(){
		return ((IStructuredSelection)getSelection()).toList();
	}
	
	public JavaBeanModel getFirstJavaBeanModel(){
		Object element = ((IStructuredSelection)getSelection()).getFirstElement();
		if(element instanceof JavaBeanModel){
			return (JavaBeanModel)element;
		}
		return null;
	}
}
