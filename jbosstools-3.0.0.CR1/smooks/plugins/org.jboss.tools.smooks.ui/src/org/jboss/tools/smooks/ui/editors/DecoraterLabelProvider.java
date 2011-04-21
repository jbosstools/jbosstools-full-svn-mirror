/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Dart
 *
 */
public class DecoraterLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if(element instanceof List){
			if(!((List)element).isEmpty()){
				Object obj = ((List)element).get(0);
				if(obj instanceof DateDecorater){
					return "Date decorater list";
				}
			}
			return "Unknown decorater list";
		}
		if(element instanceof Decorater){
			return ((Decorater)element).getSelector();
		}
		return super.getText(element);
	}

}
