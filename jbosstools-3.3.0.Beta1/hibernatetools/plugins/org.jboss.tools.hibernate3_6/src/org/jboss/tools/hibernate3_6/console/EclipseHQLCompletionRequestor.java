/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate3_6.console;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.hibernate.tool.ide.completion.HQLCompletionProposal;
import org.hibernate.tool.ide.completion.IHQLCompletionRequestor;
import org.hibernate.util.xpl.StringHelper;

public class EclipseHQLCompletionRequestor implements IHQLCompletionRequestor {

	private final List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
	private String lastErrorMessage;
	private final int virtualOffset;
	
	public EclipseHQLCompletionRequestor() {
		virtualOffset = 0;
	}
	
	public EclipseHQLCompletionRequestor(int virtualOffset) {
		this.virtualOffset = virtualOffset;
	}

	public List<ICompletionProposal> getCompletionProposals() {		
		return result;
	}

	public boolean accept(HQLCompletionProposal proposal) {
		result.add(new CompletionProposal(proposal.getCompletion(), // replacementString 
										  proposal.getReplaceStart()+virtualOffset, // replacementOffset 
										  proposal.getReplaceEnd()-proposal.getReplaceStart(), // replacementLength
										  proposal.getCompletion().length(), // cursorPosition (relativeTo replacementStart)
										  getImage(proposal), 
										  getDisplayString(proposal), 
										  null, 
										  null));
		return true;
	}

	private String getDisplayString(HQLCompletionProposal proposal) {
		StringBuffer buf = new StringBuffer(proposal.getSimpleName());
		
		switch(proposal.getCompletionKind()) {
		case HQLCompletionProposal.ENTITY_NAME:
			if(proposal.getEntityName()!=null && 
					  !(proposal.getSimpleName().equals( proposal.getEntityName()))) {
				buf.append(" - "); //$NON-NLS-1$
				buf.append(StringHelper.qualifier( proposal.getEntityName() ));
			} else if(proposal.getShortEntityName()!=null &&
					!(proposal.getSimpleName().equals( proposal.getEntityName()))) {
				buf.append( " - " + proposal.getShortEntityName() ); //$NON-NLS-1$
			} 
			break;
		case HQLCompletionProposal.ALIAS_REF:
			if(proposal.getShortEntityName()!=null) {
				buf.append( " - " + proposal.getShortEntityName() ); //$NON-NLS-1$
			} else if(proposal.getEntityName()!=null) {
				buf.append( " - " + proposal.getEntityName() ); //$NON-NLS-1$
			}
			break;
		case HQLCompletionProposal.PROPERTY:
			if(proposal.getShortEntityName()!=null) {
				buf.append( " - " + proposal.getShortEntityName() ); //$NON-NLS-1$
			} else if(proposal.getEntityName()!=null) {
				if(proposal.getEntityName().indexOf( "." )>=0) { //$NON-NLS-1$
					buf.append( " - " + StringHelper.unqualify( proposal.getEntityName() )); //$NON-NLS-1$
				} else {
					buf.append( " - " + proposal.getEntityName() ); //$NON-NLS-1$
				}
			}
			break;
		case HQLCompletionProposal.KEYWORD:
			break;
		case HQLCompletionProposal.FUNCTION:
			break;
		default:
			
		}
		
		
		return buf.toString();
	}

	private Image getImage(HQLCompletionProposal proposal) {
		String key = null;
		
		switch(proposal.getCompletionKind()) {
		case HQLCompletionProposal.ENTITY_NAME:
		case HQLCompletionProposal.ALIAS_REF:
			key = ImageConstants.MAPPEDCLASS;
			break;
		case HQLCompletionProposal.PROPERTY:
			Property property = proposal.getProperty();
			if(property!=null) {
				if(property.getPersistentClass()!=null
						&& property.getPersistentClass().getIdentifierProperty()==property) {
						key = ImageConstants.IDPROPERTY;
				} else {
					key = getIconNameForValue(property.getValue());
				}
			} else {
				key = ImageConstants.PROPERTY;				
			}
			break;
		case HQLCompletionProposal.KEYWORD:
			key = null;
			break;
		case HQLCompletionProposal.FUNCTION:
			key = ImageConstants.FUNCTION;
			break;
		default:
			key = null;
		}
		
		return key==null?null:EclipseImages.getImage( key );
	}

	public void completionFailure(String errorMessage) {
		lastErrorMessage = errorMessage;		
	}
	
	public String getLastErrorMessage() {
		return lastErrorMessage;
	}
	
	public void clear() {
		result.clear();
		lastErrorMessage = null;
	}
	
	static private String getIconNameForValue(Value value) {
		String result;
		
		result = (String) value.accept(new IconNameValueVisitor());
		
		if(result==null) {
			result = ImageConstants.UNKNOWNPROPERTY;
		}
		return result;
	}

}
