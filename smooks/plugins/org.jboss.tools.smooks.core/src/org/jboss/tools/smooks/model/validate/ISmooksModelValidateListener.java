package org.jboss.tools.smooks.model.validate;

import org.eclipse.emf.common.util.Diagnostic;

public interface ISmooksModelValidateListener {
	void validateStart();
	void validateEnd(Diagnostic diagnosticResult);
}
