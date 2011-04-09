/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.validator.rules;


/**
 * Dependency on the BPEL validation model only in here ...
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.bpel.model.Import;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.validator.helpers.DOMNodeAdapter;
import org.eclipse.bpel.validator.model.Filters;
import org.eclipse.bpel.validator.model.IConstants;
import org.eclipse.bpel.validator.model.IModelQueryLookups;
import org.eclipse.bpel.validator.model.INode;
import org.eclipse.bpel.validator.model.IProblem;
import org.eclipse.bpel.validator.model.ARule;
import org.eclipse.bpel.validator.model.NodeAttributeValueFilter;
import org.eclipse.emf.ecore.EObject;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;



/**
 * Validates Process nodes.
 * 
 *  
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Sep 14, 2006
 *
 */


@SuppressWarnings({"nls","boxing"})

public class ProcessValidator extends CValidator {
			
	String ncName ;
	
	protected String fExitStandardFault;

	protected String fSupressJoinFailure;
			
	protected List<INode> fStartActivities ;
	
	/** 
	 * Start the validation pass.
	 * 
	 * 
	 * @see org.eclipse.bpel.validator.model.Validator#start()
	 */
	
	@Override
	protected void start () {
		super.start();
		
		ncName = mNode.getAttribute( AT_NAME );
		fTypeToCheckList = new LinkedList<INode>();
		fStartActivities = new ArrayList<INode>();		
		setValue ("types.to.check",fTypeToCheckList);
		setValue ("start.activities", fStartActivities);		
	}
	
	/**
	 * Rule to check the name of the process. 
	 */
	
	@ARule(
		date = "9/14/2006",		
		desc = "Rule to check the name of the process",
		author = "michal.chmielewski@oracle.com"
	)	
		
	public void rule_CheckName_1 () {					
		checkNCName(mNode, ncName, AT_NAME );
	}
		
	
	
	/**
	 * Check if the expression language is acceptable by the model. 
	 */
	

	@ARule(
		author = "michal.chmielewski@oracle.com",
		desc = "Checks the expression language for support in the BPEL model",		
		date = "9/14/2006",
		sa = 4
	)
	
	public void rule_CheckExpressionLanguage_3 () {

		String value = mNode.getAttribute( AT_EXPRESSIONLANGUAGE );
		IProblem problem;
		
		if ( isEmpty(value) ) {
			value = IConstants.XMLNS_XPATH_EXPRESSION_LANGUAGE ;
		}
		setValue(AT_EXPRESSIONLANGUAGE, value);
		
		// Expression language not supported.
		if (mModelQuery.hasSupport(IModelQueryLookups.SUPPORT_EXPRESSION_LANGUAGE, value) == false) {
			problem = createError();
			problem.fill( "BPELC__UNSUPPORTED_XML_LANG",  //$NON-NLS-1$
					AT_EXPRESSIONLANGUAGE,value);
		}		
		
		setValue (AT_EXPRESSIONLANGUAGE,value);
	}
	
	
	/**
	 * Check if the query language is supported by the BPEL model.
	 *
	 */
	
	@ARule(
		author = "michal.chmielewski@oracle.com",
		desc = "Checks the query language for support in the BPEL model",		
		date = "9/14/2006",
		sa = 4
	)
		
	public void rule_CheckQueryLanguage_4 () {

		String value = mNode.getAttribute( AT_QUERYLANGUAGE );
		
		if ( isEmpty(value) ) {			
			value = IConstants.XMLNS_XPATH_EXPRESSION_LANGUAGE;
		}
		
		setValue (AT_QUERYLANGUAGE, value);
		
		IProblem problem;
		// Expression language not supported.
		if (mModelQuery.hasSupport(IModelQueryLookups.SUPPORT_QUERY_LANGUAGE, value) == false) {
			problem = createError();
			problem.fill( "BPELC__UNSUPPORTED_XML_LANG",  //$NON-NLS-1$
					AT_QUERYLANGUAGE,value);
		}			
		
	}
	

	/**
	 * Check exit on standard fault
	 *
	 */
	@ARule(
		sa = 0,
		desc = "Check exitOnStandardFault attribute setting",
		author = "michal.chmielewski@oracle.com",
		date = "01/10/2007"
	)
	
	public void rule_CheckExitOnStandardFault_10 () {
		
		fExitStandardFault = getAttribute(mNode, 
				AT_EXIT_ON_STANDARD_FAULT, 
				KIND_NODE, 
				Filters.BOOLEAN_FILTER, 
				false);
		
		if (isEmpty(fExitStandardFault)) {			
			fExitStandardFault = NO;
		} 
		
		setValue(AT_EXIT_ON_STANDARD_FAULT,fExitStandardFault);
	}	
	
	/**
	 * 
	 */
	@ARule(
		sa = 0,
		desc = "Check supressJoinFailure attribute setting",
		author = "michal.chmielewski@oracle.com",
		date = "01/10/2007"
	)			
	public void rule_CheckSuppressJoinFailre_11 () {
		fSupressJoinFailure = getAttribute(mNode, 
				AT_SUPPRESS_JOIN_FAILURE, 
				KIND_NODE, 
				Filters.BOOLEAN_FILTER, 
				false);
		
		if (isEmpty(fSupressJoinFailure)) {			
			fSupressJoinFailure = NO;
		}
		setValue (AT_SUPPRESS_JOIN_FAILURE,fSupressJoinFailure);
		
	}
	
	
	
	/**
	 * Check if process has start activity.
	 */
	

	@ARule(
		sa = 15,
		desc = "Verifies that a start activity has been found in the process",
		author = "michal.chmielewski@oracle.com",
		date = "02/01/2007",
		tag = "pass2",
		order = 1000
	)
	
	public void CheckIfProcessHasStartActivity () {
		
		if (fStartActivities.size() > 0)  {
			return ;
		}
		
		IProblem problem = createError();
		problem.fill("BPELC_PROCESS__NO_START",
				toString(mNode.nodeName()),
				mNode.getAttribute(AT_NAME)
		);
	}
	

	/**
	 * 
	 */
	@ARule(
		sa = 57,
		desc = "Check common correlation sets on all the start activities",
		author = "michal.chmielewski@oracle.com",
		date = "03/10/2007",
		tag = "pass2",
		order = 2000
	)
	public void CheckCorrelationSetsOnStartActivities () {
		// if 0 or 1, then it does not matter ...
		if (fStartActivities.size() < 2)  {
			return ;
		}		 
		
		/** Compute the common correlation sets (intersection) */
		
		List<INode> commonSets = null;
		 
		for(INode n : fStartActivities) {	
			
			// The correlation validator sets these. 
			List<INode> correlationSets = Collections.emptyList();
			if (containsValueKey(n,"correlation.sets")) {
				correlationSets = getValue(n,"correlation.sets",null);
			}
			
			// First time through the loop ?
			if (commonSets == null) {
				commonSets = new ArrayList<INode>( correlationSets );
				continue ;
			} 			
			 
			if (commonSets.size() > 0) {
				
				List<INode> intersection = new  ArrayList<INode>();			
				for(INode i1 : commonSets ) {
					for(INode i2 : correlationSets) {						
						if (i1.equals(i2)) {
							intersection.add(i2);
						}						
					}
				}
				
				commonSets.clear();
				commonSets.addAll(intersection);

			}
		}
		
		
		/** all start activities do *not* specify a correlation */
		if (commonSets == null) {
			return ;
		}
		
		IProblem problem;
		
		for(INode s : fStartActivities) {
			INode correlationsNode = mSelector.selectNode(s, ND_CORRELATIONS);
						
			if (commonSets.size () == 0 ) {
				problem = createError(s);
				problem.fill("BPELC_CORRELATION__COMMON",
						s.nodeName(),
						fStartActivities.size() 
						
				);
				
			} else {
				
				for ( INode correlationSet : commonSets ) {
					
					String name = correlationSet.getAttribute(AT_NAME);
					
					if (name == null) {
						continue;
					}
					
					INode correlationNode = mSelector.selectNode(correlationsNode, 
							ND_CORRELATION,
							new NodeAttributeValueFilter(AT_SET,name) );
					
					if ( isUndefined( correlationNode ) )  {
						continue;
					}
					
					if (JOIN.equals(correlationNode.getAttribute(AT_INITIATE)) == false) {
						problem = createError( correlationNode );
						problem.fill("BPELC_CORRELATION__JOIN",
								correlationNode.nodeName(),
								AT_INITIATE,
								JOIN );
					}								
								
				}
			}				
			
		}
	}
	
	
	
	/**
	 * Check the references types against the imports that we have.
	 *
	 */
	@ARule(
		sa = 14,
		desc = "Check if used types/elements/messages have conflicting definitions",
		author = "michal.chmielewski@oracle.com",
		date = "02/10/2007",
		tag = "pass2",
		order = 300
	)
	// https://issues.jboss.org/browse/JBIDE-8088
	// implemented missing code
	public void CheckReferencedTypes () {
		IProblem problem;
		
		for(INode node : fTypeToCheckList) {
			
			Process process = (Process) mModelQuery.lookupProcess(mNode);
			List<Import> conflicts = mModelQuery.findConflictingXSD(process, node);
			if (conflicts!=null) {
				
				String conflicting = null;
				for (int i=1; i<conflicts.size(); ++i) {
					if (conflicting == null)
						conflicting = conflicts.get(i).getLocation();
					else
						conflicting += ", " + conflicts.get(i).getLocation();
				}
				problem = createError(node);
				problem.fill("BPELC_XSD__CONFLICTING_DEFINITION",
						node.getAttribute(AT_NAME),
						conflicts.get(0).getLocation(),
						conflicting 
						
				);
				
			}
		}		
	}
	
	
	/** End of public rule methods.
	 * 
	 * Other methods are support methods for this class to perform its
	 * validation function.
	 * 
	 */

}
