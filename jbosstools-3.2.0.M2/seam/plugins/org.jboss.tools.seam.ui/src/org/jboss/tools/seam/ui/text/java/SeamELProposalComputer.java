/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.ui.text.java;

import org.jboss.tools.common.el.ui.ca.ELProposalProcessor;
import org.jboss.tools.common.el.ui.ca.JavaELProposalComputer;

/**
 * Custom Java Completion Proposal computer 
 * 
 * @author Jeremy
 */
public class SeamELProposalComputer extends JavaELProposalComputer {

	private final SeamELProposalProcessor fProcessor = new SeamELProposalProcessor();

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.common.el.ui.ca.JavaELProposalComputer#getELProcessor()
	 */
	@Override
	protected ELProposalProcessor getELProcessor() {
		return fProcessor;
	}
}