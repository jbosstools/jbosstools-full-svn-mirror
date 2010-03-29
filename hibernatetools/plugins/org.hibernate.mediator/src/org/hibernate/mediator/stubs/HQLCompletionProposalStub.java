package org.hibernate.mediator.stubs;

import org.hibernate.tool.ide.completion.HQLCompletionProposal;

public class HQLCompletionProposalStub {
	
	public static final int ENTITY_NAME = HQLCompletionProposal.ENTITY_NAME;
	public static final int PROPERTY = HQLCompletionProposal.PROPERTY;
	public static final int KEYWORD = HQLCompletionProposal.KEYWORD;
	public static final int FUNCTION = HQLCompletionProposal.FUNCTION;
	public static final int ALIAS_REF = HQLCompletionProposal.ALIAS_REF;

	protected HQLCompletionProposal hqlCompletionProposal;

	protected HQLCompletionProposalStub(Object hqlCompletionProposal) {
		this.hqlCompletionProposal = (HQLCompletionProposal)hqlCompletionProposal;
	}

	public String getCompletion() {
		return hqlCompletionProposal.getCompletion();
	}

	public int getReplaceStart() {
		return hqlCompletionProposal.getReplaceStart();
	}

	public int getReplaceEnd() {
		return hqlCompletionProposal.getReplaceEnd();
	}

	public int getCompletionKind() {
		return hqlCompletionProposal.getCompletionKind();
	}

	public String getEntityName() {
		return hqlCompletionProposal.getEntityName();
	}

	public String getSimpleName() {
		return hqlCompletionProposal.getSimpleName();
	}

	public String getShortEntityName() {
		return hqlCompletionProposal.getShortEntityName();
	}

	public PropertyStub getProperty() {
		return new PropertyStub(hqlCompletionProposal.getProperty());
	}
}
