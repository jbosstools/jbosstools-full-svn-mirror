 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.common.el.core.resolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMAttr;
import org.jboss.tools.common.el.core.Activator;
import org.jboss.tools.common.el.core.model.ELExpression;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class helps to find var/value attributes in DOM tree.   
 * @author Alexey Kazakov
 */
public class ElVarSearcher {

	private final static String VAR_ATTRIBUTE_NAME = "var";
	private final static String VALUE_ATTRIBUTE_NAME = "value";

	private IFile file;
	private ELCompletionEngine engine;

	/**
	 * Constructor.
	 * @param project Seam project where we will look for vars.
	 * @param file File where we will look for vars.
	 * @param engine Competion Engine that we will use for resolving vars.
	 */
	public ElVarSearcher(IFile file, ELCompletionEngine engine) {
		this.file = file;
		this.engine = engine;
	}

	/**
	 * Constructor.
	 * @param project Seam project where we will look for vars.
	 * @param engine Competion Engine that we will use for resolving vars.
	 */
	public ElVarSearcher(ELCompletionEngine engine) {
		this(null, engine);
	}

	/**
	 * @param file File where we will look for vars.
	 */
	public void setFile(IFile file) {
		this.file = file;
	}

	/**
	 * @param viewer
	 * @param offset
	 * @return
	 */
	public static Node getNode(ITextViewer viewer, int offset) {
		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(viewer, offset);
		if(treeNode instanceof Node) {
			return (Node)treeNode;
		}
		return null;
	}

	/**
	 * @param viewer
	 * @param offset
	 * @return
	 */

	public static Node getNode(IFile file, int offset) {
		IndexedRegion treeNode = getNodeAt(file, offset);
		if(treeNode instanceof Node) {
			return (Node)treeNode;
		}
		return null;
	}

	/**
	 * Returns the closest IndexedRegion for the offset and viewer allowing
	 * for differences between viewer offsets and model positions. note: this
	 * method returns an IndexedRegion for read only
	 * 
	 * @param file
	 *            the file whose document is used to compute the proposals
	 * @param documentOffset
	 *            an offset within the document for which completions should
	 *            be computed
	 * @return an IndexedRegion
	 */

	public static IndexedRegion getNodeAt(IFile file, int documentOffset) {

		if (file == null)
			return null;

		IndexedRegion node = null;
		IModelManager mm = StructuredModelManager.getModelManager();
		IStructuredModel model = null;
		if (mm != null) {
			try {
				model = mm.getModelForRead(file);
			} catch (IOException e) {
				return null;
			} catch (CoreException e) {
				return null;
			}
		}
		try {
			if (model != null) {
				int lastOffset = documentOffset;
				node = model.getIndexedRegion(documentOffset);
				while (node == null && lastOffset >= 0) {
					lastOffset--;
					node = model.getIndexedRegion(lastOffset);
				}
			}
		} finally {
			if (model != null)
				model.releaseFromRead();
		}
		return node;
	}

	/**
	 * @param node
	 * @return All var/value that can be used in this position and null if can't find anyone.
	 */
	public List<Var> findAllVars(ITextViewer viewer, int offset) {
		Node node = getNode(viewer, offset);
		if(node!=null) {
			return findAllVars(node);
		}
		return null;
	}

	/**
	 * @param node
	 * @return All var/value that can be used in this position and null if can't find anyone.
	 */
	public List<Var> findAllVars(IFile file, int offset) {
		Node node = getNode(file, offset);
		if(node!=null) {
			return findAllVars(node);
		}
		return null;
	}

	/**
	 * @param node
	 * @return All var/value that can be used in node and null if can't find anyone.
	 */
	public List<Var> findAllVars(Node node) {
		ArrayList<Var> vars = null;
		Node parentNode = node.getParentNode();
		while(parentNode!=null) {
			Var var = findVar(parentNode);
			if(var!=null) {
				if(vars == null) {
					vars = new ArrayList<Var>();
				}
				vars.add(0, var);
			}
			parentNode = parentNode.getParentNode();
		}
		return vars;
	}

	/**
	 * @param node
	 * @return found var/value that can be used in this position and null if can't find anyone.
	 */
	public Var findVar(IFile file, int offset) {
		Node node = getNode(file, offset);
		if(node!=null) {
			return findVar(node);
		}
		return null;
	}

	/**
	 * Finds var/value attribute in node
	 * @param node
	 * @param vars
	 * @return found var/value or null
	 */
	public Var findVar(Node node) {
		if(node!=null && Node.ELEMENT_NODE == node.getNodeType()) {
			Element element = (Element)node;
			String var = element.getAttribute(VAR_ATTRIBUTE_NAME);
			if(var!=null) {
				int declOffset = 0;
				int declLength = 0;
				Node varAttr = element.getAttributeNode(VAR_ATTRIBUTE_NAME); 
				if (varAttr instanceof IDOMAttr) {
					int varNameStart = ((IDOMAttr)varAttr).getNameRegionStartOffset();
					int varNameEnd = ((IDOMAttr)varAttr).getNameRegionEndOffset();
					declOffset = varNameStart;
					declLength = varNameEnd - varNameStart;
				}
				var = var.trim();
				if(!"".equals(var)) {
					String value = element.getAttribute(VALUE_ATTRIBUTE_NAME);
					if(value!=null) {
						value = value.trim();
						Var newVar = new Var(engine.getParserFactory(), var, value, declOffset, declLength);
						if(newVar.getElToken()!=null) {
							return newVar;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Finds var in list of vars that is used in given EL.
	 * @param el EL without brackets.
	 * @param vars
	 * @param initializeNestedVars
	 * @return
	 */
	public Var findVarForEl(String el, List<Var> vars, boolean initializeNestedVars) {
		if(vars!=null) {
			ArrayList<Var> parentVars = new ArrayList<Var>();
			for (Var var : vars) {
				ELExpression token = var.getElToken();
				if(token!=null && !token.getText().endsWith(".")) {
					String varName = var.getName();
					if(el.startsWith(varName)) { //TODO it was el.equals(varName) and did not work. Why?
						if(var.getElToken()!=null && initializeNestedVars) {
							Var parentVar = findVarForEl(var.getElToken().getText(), parentVars, true);
							if(parentVar!=null) {
								ELExpression resolvedToken = parentVar.getResolvedElToken();
								if(resolvedToken==null && parentVar.getElToken()!=null) {
									try {
										// Initialize parent vars.
										engine.resolveELOperand(file, var.getElToken(), true, parentVars, this);
										resolvedToken = parentVar.getResolvedElToken();
									} catch (StringIndexOutOfBoundsException e) {
										Activator.getPluginLog().logError(e);
									} catch (BadLocationException e) {
										Activator.getPluginLog().logError(e);
									}
								}
								if(resolvedToken!=null) {
									String oldText = var.getElToken().getText();
									String newValue = "#{" + resolvedToken.getText() + oldText.substring(parentVar.getName().length()) + "}";
									var.value = newValue;
									var.elToken = var.parseEl(newValue);
								}
							}
						}
						return var;
					}
				}
				parentVars.add(var);
			}
		}
		return null;
	}

}
