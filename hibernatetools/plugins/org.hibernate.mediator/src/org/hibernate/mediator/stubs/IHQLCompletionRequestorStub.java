package org.hibernate.mediator.stubs;

import org.hibernate.tool.ide.completion.HQLCompletionProposal;
import org.hibernate.tool.ide.completion.IHQLCompletionRequestor;

public abstract class IHQLCompletionRequestorStub {
	public static final String CL = "org.hibernate.tool.ide.completion.HQLCompletionProposal"; //$NON-NLS-1$

	protected IHQLCompletionRequestor hqlCompletionRequestor;
	
	protected IHQLCompletionRequestorStub() {
		hqlCompletionRequestor = new IHQLCompletionRequestor() {

			public boolean accept(HQLCompletionProposal proposal) {
				return IHQLCompletionRequestorStub.this.accept(new HQLCompletionProposalStub(proposal));
			}

			public void completionFailure(String errorMessage) {
				IHQLCompletionRequestorStub.this.completionFailure(errorMessage);
			}
			
		};
	}
	
	public abstract boolean accept(HQLCompletionProposalStub proposal);
	public abstract void completionFailure(String errorMessage);
}
