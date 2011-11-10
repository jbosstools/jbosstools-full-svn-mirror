/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate3_5;

import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.ext.CompletionProposalsResult;
import org.hibernate.console.ext.HibernateException;
import org.hibernate.console.ext.HibernateExtension;
import org.hibernate.eclipse.console.ext.ConsoleExtension;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;

/**
 * @author Dmitry Geraskov
 *
 */
public class ConsoleExtension3_5 implements ConsoleExtension {
	
	private HibernateExtension3_5 hibernateExtension;
	
	public ConsoleExtension3_5(){}
	
	public void setHibernateException(HibernateExtension hibernateExtension){
		this.hibernateExtension = (HibernateExtension3_5) hibernateExtension;
	}

	@Override
	public CompletionProposalsResult hqlCodeComplete(String query, int currentOffset) {
		EclipseHQLCompletionRequestor requestor = new EclipseHQLCompletionRequestor();
		if (!hibernateExtension.hasConfiguration()){
			try {
				hibernateExtension.build();
				hibernateExtension.execute( new ExecutionContext.Command() {
			 		public Object execute() {
			 			if(hibernateExtension.hasConfiguration()) {
			 				hibernateExtension.getConfiguration().buildMappings();
				 		}
			 			return null;
			 		}
				});
			} catch (HibernateException e){
				//FIXME
				//String mess = NLS.bind(HibernateConsoleMessages.CompletionHelper_error_could_not_build_cc, consoleConfiguration.getName());
				//HibernateConsolePlugin.getDefault().logErrorMessage(mess, e);
			}
		}
		IHQLCodeAssist hqlEval = new HQLCodeAssist(hibernateExtension.getConfiguration());
		query = query.replace('\t', ' ');
		hqlEval.codeComplete(query, currentOffset, requestor);
		return new CompletionProposalsResult(requestor.getCompletionProposals(), requestor.getLastErrorMessage());
	}



}
