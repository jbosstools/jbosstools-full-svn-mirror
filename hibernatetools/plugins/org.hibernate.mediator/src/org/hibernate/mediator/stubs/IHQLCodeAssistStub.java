package org.hibernate.mediator.stubs;

import org.hibernate.mediator.Messages;
import org.hibernate.tool.ide.completion.HQLCodeAssist;

public class IHQLCodeAssistStub {
	public static final String CL = "org.hibernate.tool.ide.completion.HQLCodeAssist"; //$NON-NLS-1$

	protected HQLCodeAssist hqlCodeAssist;

	protected IHQLCodeAssistStub(Object hqlCodeAssist) {
		if (hqlCodeAssist == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.hqlCodeAssist = (HQLCodeAssist)hqlCodeAssist;
	}

	public static IHQLCodeAssistStub createHQLCodeAssist() {
		return new IHQLCodeAssistStub(new HQLCodeAssist(null));
	}

	public void codeComplete(String query, int position, IHQLCompletionRequestorStub requestor) {
		hqlCodeAssist.codeComplete(query, position, requestor.hqlCompletionRequestor);
	}
}
