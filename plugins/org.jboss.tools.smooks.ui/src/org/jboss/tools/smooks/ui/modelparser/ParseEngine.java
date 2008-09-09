package org.jboss.tools.smooks.ui.modelparser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;

/**
 * 
 * @author Dart Peng
 * 
 */
public class ParseEngine {

	/**
	 * 
	 * @param provider
	 * @param contents
	 * @param parser
	 * @return
	 */
	public List<AbstractStructuredDataModel> parseModelElements(
			Object contents, ITreeContentProvider provider,
			IStructuredModelParser parser) {
		List<AbstractStructuredDataModel> list = new ArrayList<AbstractStructuredDataModel>();
		Object[] objects = provider.getElements(contents);
		for (int i = 0; i < objects.length; i++) {
			Object obj = objects[i];
			list.add(parseModel(obj, provider, parser));
		}
		return list;
	}

	/**
	 * 
	 * @param model
	 * @param provider
	 * @param parser
	 * @return
	 */
	public AbstractStructuredDataModel parseModel(Object model,
			ITreeContentProvider provider, IStructuredModelParser parser) {
		AbstractStructuredDataModel adm = parser.parse(model);
		if(adm == null) return null;
		if (provider.hasChildren(model)) {
			Object[] array = provider.getChildren(model);
			for (int j = 0; j < array.length; j++) {
				Object child = array[j];
				adm.addChild(parseModel(child, provider, parser));
			}
		}

		return adm;
	}

}
