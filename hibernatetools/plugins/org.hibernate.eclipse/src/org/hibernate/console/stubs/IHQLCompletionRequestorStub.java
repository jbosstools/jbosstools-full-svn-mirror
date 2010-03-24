package org.hibernate.console.stubs;

import org.hibernate.tool.ide.completion.IHQLCompletionRequestor;

public abstract class IHQLCompletionRequestorStub {

	protected IHQLCompletionRequestor hqlCompletionRequestor;
	
	protected IHQLCompletionRequestorStub(Object hqlCompletionRequestor) {
		
	}
	
	public abstract boolean accept(HQLCompletionProposalStub proposal);
	public abstract void completionFailure(String errorMessage);
}
