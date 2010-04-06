package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class HQLCompletionProposalStub extends HObject {
	public static final String CL = "org.hibernate.tool.ide.completion.HQLCompletionProposal"; //$NON-NLS-1$

	public static final int ENTITY_NAME = (Integer)readStaticFieldValue(CL, "ENTITY_NAME"); //$NON-NLS-1$
	public static final int PROPERTY = (Integer)readStaticFieldValue(CL, "PROPERTY"); //$NON-NLS-1$
	public static final int KEYWORD = (Integer)readStaticFieldValue(CL, "KEYWORD"); //$NON-NLS-1$
	public static final int FUNCTION = (Integer)readStaticFieldValue(CL, "FUNCTION"); //$NON-NLS-1$
	public static final int ALIAS_REF = (Integer)readStaticFieldValue(CL, "ALIAS_REF"); //$NON-NLS-1$

	protected HQLCompletionProposalStub(Object hqlCompletionProposal) {
		super(hqlCompletionProposal, CL);
	}

	public String getCompletion() {
		return (String)invoke(mn());
	}

	public int getReplaceStart() {
		return (Integer)invoke(mn());
	}

	public int getReplaceEnd() {
		return (Integer)invoke(mn());
	}

	public int getCompletionKind() {
		return (Integer)invoke(mn());
	}

	public String getEntityName() {
		return (String)invoke(mn());
	}

	public String getSimpleName() {
		return (String)invoke(mn());
	}

	public String getShortEntityName() {
		return (String)invoke(mn());
	}

	public PropertyStub getProperty() {
		Object obj = invoke(mn());
		if (obj == null) {
			return null;
		}
		return new PropertyStub(obj);
	}
}
