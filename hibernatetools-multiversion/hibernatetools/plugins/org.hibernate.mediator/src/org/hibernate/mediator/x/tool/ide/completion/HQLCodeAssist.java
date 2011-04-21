package org.hibernate.mediator.x.tool.ide.completion;

import org.hibernate.mediator.base.HObject;

public class HQLCodeAssist extends HObject {
	public static final String CL = "org.hibernate.tool.ide.completion.HQLCodeAssist"; //$NON-NLS-1$

	protected HQLCodeAssist(Object hqlCodeAssist) {
		super(hqlCodeAssist, CL);
	}

	public static HQLCodeAssist createHQLCodeAssist(Object configuration) {
		return new HQLCodeAssist(HObject.newInstance(CL, configuration));
	}

	public static HQLCodeAssist createHQLCodeAssist() {
		return new HQLCodeAssist(HObject.newInstance(CL, null));
	}

	public void codeComplete(String query, int position, IHQLCompletionRequestor requestor) {
		invoke(mn(), query, position, requestor.hqlCompletionRequestor);
	}
}
