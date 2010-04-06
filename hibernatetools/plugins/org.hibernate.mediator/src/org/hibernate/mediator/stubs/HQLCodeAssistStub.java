package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class HQLCodeAssistStub extends HObject {
	public static final String CL = "org.hibernate.tool.ide.completion.HQLCodeAssist"; //$NON-NLS-1$

	protected HQLCodeAssistStub(Object hqlCodeAssist) {
		super(hqlCodeAssist, CL);
	}

	public static HQLCodeAssistStub createHQLCodeAssist(Object configuration) {
		return new HQLCodeAssistStub(HObject.newInstance(CL, configuration));
	}

	public static HQLCodeAssistStub createHQLCodeAssist() {
		return new HQLCodeAssistStub(HObject.newInstance(CL, null));
	}

	public void codeComplete(String query, int position, IHQLCompletionRequestorStub requestor) {
		invoke(mn(), query, position, requestor);
	}
}
