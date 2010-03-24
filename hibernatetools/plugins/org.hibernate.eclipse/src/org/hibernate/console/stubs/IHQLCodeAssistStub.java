package org.hibernate.console.stubs;

import org.hibernate.tool.ide.completion.HQLCodeAssist;

public class IHQLCodeAssistStub {
	protected HQLCodeAssist hqlCodeAssist;

	protected IHQLCodeAssistStub(Object hqlCodeAssist) {
		this.hqlCodeAssist = (HQLCodeAssist)hqlCodeAssist;
	}

	public static IHQLCodeAssistStub createHQLCodeAssist() {
		return new IHQLCodeAssistStub(new HQLCodeAssist(null));
	}

	public void codeComplete(String query, int position, IHQLCompletionRequestorStub requestor) {
		hqlCodeAssist.codeComplete(query, position, requestor.hqlCompletionRequestor);
	}
}
