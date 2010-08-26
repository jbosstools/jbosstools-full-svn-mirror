package org.jboss.tools.smooks.model;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.jboss.tools.smooks.model.core.ICoreFactory;
import org.jboss.tools.smooks.model.core.IGlobalParams;
import org.jboss.tools.smooks.model.core.IParam;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IGlobalParams params = ICoreFactory.eINSTANCE.createGlobalParams();
		params.eAdapters().add(new EContentAdapter(){

			@Override
			public void notifyChanged(Notification notification) {
				System.out.println("change");
				super.notifyChanged(notification);
			}

			
			
		});
		IParam pp = ICoreFactory.eINSTANCE.createParam();
		params.getParams().add(pp);
		pp.setName("name");
	}

}
