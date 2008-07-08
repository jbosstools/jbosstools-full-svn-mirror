/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.jboss.ide.eclipse.archives.core.xpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.IVariableManager;

/**
 * Performs string substitution for context and value variables.
 */
public class StringSubstitutionEngineClone {
	
	// delimiters
	private static final String VARIABLE_START = "${"; //$NON-NLS-1$
	private static final char VARIABLE_END = '}'; 
//	private static final char VARIABLE_ARG = ':'; 
	// parsing states
	private static final int SCAN_FOR_START = 0;
	private static final int SCAN_FOR_END = 1;
	
	/**
	 * Resulting string
	 */
	private StringBuffer fResult;
	
	/**
	 * Whether substitutions were performed
	 */
	private boolean fSubs;
	
	/**
	 * Stack of variables to resolve
	 */
	private Stack fStack;
	
	class VariableReference {
		
		// the text inside the variable reference
		private StringBuffer fText;
		
		public VariableReference() {
			fText = new StringBuffer();
		}
		
		public void append(String text) {
			fText.append(text);
		}
		
		public String getText() {
			return fText.toString();
		}
	
	}
	
	/**
	 * Performs recursive string substitution and returns the resulting string.
	 * 
	 * @param expression expression to resolve
	 * @param reportUndefinedVariables whether to report undefined variables as an error
	 * @return the resulting string with all variables recursively
	 *  substituted
	 * @exception CoreException if unable to resolve a referenced variable or if a cycle exists
	 *  in referenced variables
	 */
	public String performStringSubstitution(String expression, boolean reportUndefinedVariables, IVariableManager manager ) throws CoreException {
		substitute(expression, reportUndefinedVariables,manager );
		List resolvedVariableSets = new ArrayList();
		while (fSubs) {
			HashSet resolved = substitute(fResult.toString(), reportUndefinedVariables,manager);			
			
			for(int i=resolvedVariableSets.size()-1; i>=0; i--) {
				
				HashSet prevSet = (HashSet)resolvedVariableSets.get(i);

				if (prevSet.equals(resolved)) {
					HashSet conflictingSet = new HashSet();
					for (; i<resolvedVariableSets.size(); i++)
						conflictingSet.addAll((HashSet)resolvedVariableSets.get(i));
					
					StringBuffer problemVariableList = new StringBuffer();
					for (Iterator it=conflictingSet.iterator(); it.hasNext(); ) {
						problemVariableList.append(it.next().toString());
						problemVariableList.append(", "); //$NON-NLS-1$
					}
					problemVariableList.setLength(problemVariableList.length()-2); //truncate the last ", "
					Status status = new Status(IStatus.ERROR, 
							ArchivesCore.PLUGIN_ID, 
							"Cycle found in variable replacement",
							null);
					throw new CoreException(status); 
				}				
			}		
			
			resolvedVariableSets.add(resolved);			
		}
		return fResult.toString();
	}
	
	/**
	 * Performs recursive string validation to ensure that all of the variables
	 * contained in the expression exist
	 * @param expression expression to validate
	 * @exception CoreException if a referenced variable does not exist or if a cycle exists
	 *  in referenced variables
	 */
	public void validateStringVariables(String expression, IVariableManager manager ) throws CoreException {
		performStringSubstitution(expression, true, manager );
	}
	
	/**
	 * Makes a substitution pass of the given expression returns a Set of the variables that were resolved in this
	 *  pass
	 *  
	 * @param expression source expression
	 * @param reportUndefinedVariables whether to report undefined variables as an error
	 * @exception CoreException if unable to resolve a variable
	 */
	private HashSet substitute(String expression, boolean reportUndefinedVariables, IVariableManager manager) throws CoreException {
		fResult = new StringBuffer(expression.length());
		fStack = new Stack();
		fSubs = false;
		
		HashSet resolvedVariables = new HashSet();

		int pos = 0;
		int state = SCAN_FOR_START;
		while (pos < expression.length()) {
			switch (state) {
				case SCAN_FOR_START:
					int start = expression.indexOf(VARIABLE_START, pos);
					if (start >= 0) {
						int length = start - pos;
						// copy non-variable text to the result
						if (length > 0) {
							fResult.append(expression.substring(pos, start));
						}
						pos = start + 2;
						state = SCAN_FOR_END;

						fStack.push(new VariableReference());						
					} else {
						// done - no more variables
						fResult.append(expression.substring(pos));
						pos = expression.length();
					}
					break;
				case SCAN_FOR_END:
					// be careful of nested variables
					start = expression.indexOf(VARIABLE_START, pos);
					int end = expression.indexOf(VARIABLE_END, pos);
					if (end < 0) {
						// variables are not completed
						VariableReference tos = (VariableReference)fStack.peek();
						tos.append(expression.substring(pos));
						pos = expression.length();
					} else {
						if (start >= 0 && start < end) {
							// start of a nested variable
							int length = start - pos;
							if (length > 0) {
								VariableReference tos = (VariableReference)fStack.peek();
								tos.append(expression.substring(pos, start));
							}
							pos = start + 2;
							fStack.push(new VariableReference());	
						} else {
							// end of variable reference
							VariableReference tos = (VariableReference)fStack.pop();
							String substring = expression.substring(pos, end);							
							tos.append(substring);
							resolvedVariables.add(substring);
							
							pos = end + 1;
							String value= resolve(tos, reportUndefinedVariables, manager);
							if (value == null) {
								value = ""; //$NON-NLS-1$
							}
							if (fStack.isEmpty()) {
								// append to result
								fResult.append(value);
								state = SCAN_FOR_START;
							} else {
								// append to previous variable
								tos = (VariableReference)fStack.peek();
								tos.append(value);
							}
						}
					}
					break;
			}
		}
		// process incomplete variable references
		while (!fStack.isEmpty()) {
			VariableReference tos = (VariableReference)fStack.pop();
			if (fStack.isEmpty()) {
				fResult.append(VARIABLE_START);
				fResult.append(tos.getText());
			} else {
				VariableReference var = (VariableReference)fStack.peek();
				var.append(VARIABLE_START);
				var.append(tos.getText());
			}
		}
		

		return resolvedVariables;
	}

	/**
	 * Resolve and return the value of the given variable reference,
	 * possibly <code>null</code>. 
	 * 
	 * @param var
	 * @param reportUndefinedVariables whether to report undefined variables as
	 *  an error
	 *  @param manager Someone to call back to for the variable's values
	 * @return variable value, possibly <code>null</code>
	 * @exception CoreException if unable to resolve a value
	 */
	private String resolve(VariableReference var, boolean reportUndefinedVariables, IVariableManager manager) throws CoreException {
		String text = var.getText();
		String name = null;
//		int pos = text.indexOf(VARIABLE_ARG);
//		String arg = null;
//		if (pos > 0) {
//			name = text.substring(0, pos);
//			pos++;
//			if (pos < text.length()) {
//				arg = text.substring(pos);
//			} 
//		} else {
//			name = text;
//		}
		name = text;
		if( !manager.containsVariable(name)) {
			if( reportUndefinedVariables )
				throw new CoreException(new Status(IStatus.ERROR, ArchivesCore.PLUGIN_ID, "Variable " + name + " undefined")); 
			return getOriginalVarText(var);
		}
		
		String ret = manager.getVariableValue(name);
		if(ret == null)
			return getOriginalVarText(var);
		return ret;
	}

	private String getOriginalVarText(VariableReference var) {
		StringBuffer res = new StringBuffer(var.getText());
		res.insert(0, VARIABLE_START);
		res.append(VARIABLE_END);
		return res.toString();
	}
}
