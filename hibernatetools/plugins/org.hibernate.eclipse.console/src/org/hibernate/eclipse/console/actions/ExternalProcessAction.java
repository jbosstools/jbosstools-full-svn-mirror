package org.hibernate.eclipse.console.actions;

import org.eclipse.jface.viewers.StructuredViewer;
import org.hibernate.console.ConsoleConfiguration;

public class ExternalProcessAction extends ConsoleConfigurationBasedAction {

	public static final String EXTERNALPROCESS_ACTIONID = "actionid.externalprocess"; //$NON-NLS-1$

	private StructuredViewer viewer;

	/**
	 * @param text
	 */
	protected ExternalProcessAction(String text) {
		super(text);
		setId(EXTERNALPROCESS_ACTIONID);
	}

	/**
	 * @param selectionProvider
	 */
	public ExternalProcessAction(StructuredViewer selectionProvider) {
		super("Run ExternalProcess");
		this.viewer = selectionProvider;
		setId(EXTERNALPROCESS_ACTIONID);
	}

	public void doRun() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.hibernate.eclipse.console.actions.SessionFactoryBasedAction#updateState(org.hibernate.console.ConsoleConfiguration)
	 */
	protected boolean updateState(ConsoleConfiguration consoleConfiguration) {
		return consoleConfiguration.hasConfiguration();
	}
}
