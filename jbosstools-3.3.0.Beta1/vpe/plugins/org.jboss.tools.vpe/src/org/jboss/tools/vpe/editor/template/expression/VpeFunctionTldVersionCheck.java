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
package org.jboss.tools.vpe.editor.template.expression;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TLDVersionHelper;
import org.jboss.tools.jst.web.tld.TaglibData;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.util.XmlUtil;
import org.w3c.dom.Node;

/**
 * @author mareshkau
 * 
 * Class created for check tld version for template
 * here should be two numbers min and max tld version
 * Examples of using:
 * 	<vpe:tag name="f:testTldVersion" case-sensitive="yes">
 *		<vpe:if test="tld_version('min=0.0 max=1.2')" >
 *		<vpe:template children="yes" modify="yes">
 *			<div  style="background-color:red">
 *			Tld Check function
 *			</div>
 *		</vpe:template>
 *		</vpe:if>
 *	</vpe:tag>
 * 	<vpe:tag name="f:testTldVersion" case-sensitive="yes">
 *		<vpe:if test="tld_version('min=0.0')" >
 *		<vpe:template children="yes" modify="yes">
 *			<div  style="background-color:red">
 *			Tld Check function
 *			</div>
 *		</vpe:template>
 *		</vpe:if>
 *	</vpe:tag>
 * <vpe:tag name="f:testTldVersion" case-sensitive="yes">
 *		<vpe:if test="tld_version('max=1.2')" >
 *		<vpe:template children="yes" modify="yes">
 *			<div  style="background-color:red">
 *			Tld Check function
 *			</div>
 *		</vpe:template>
 *		</vpe:if>
 *	</vpe:tag>
 */
public class VpeFunctionTldVersionCheck extends VpeFunction{

	public static final String FUNCTION_NAME="tld_version"; //$NON-NLS-1$
	
	private static final String MIN_VERSION_KEYWORD="min"; //$NON-NLS-1$
	
	private static final String MAX_VERSION_KEYWORD="max";//$NON-NLS-1$
	
	public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
		//gets function parameter

			String tagValue = getParameter(0).exec(pageContext, sourceNode)
			.stringValue();
			
			double startValue = getStartVersion(tagValue);
			
			double endValue = getEndVersion(tagValue);
			
			if(sourceNode==null || sourceNode.getPrefix()==null) {
				 
				return new VpeValue(false);
			}
			
			List<TaglibData> taglibs = XmlUtil.getTaglibsForNode(sourceNode,pageContext);
			
			TaglibData sourceNodeTaglib = XmlUtil.getTaglibForPrefix(sourceNode.getPrefix(), taglibs);
			//this function works only for jsp files
			String  tldVersion = TLDVersionHelper.getTldVersion(sourceNodeTaglib.getUri(), sourceNodeTaglib.getPrefix(), 
								pageContext.getSourceBuilder().getStructuredTextViewer().getDocument());
			
			double tldVersionNumber;
			if(tldVersion!=null) {
				
				tldVersionNumber = stringVersionToDouble(tldVersion);
			
			} else {
				//here we getting tld version for xhtml files
				XModel xm = null;
				//fix for JBIDE-3385, mareshkau
				final IEditorInput editorInput = pageContext.getEditPart().getEditorInput();
				if(!(editorInput instanceof IFileEditorInput)){
					return new VpeValue(false);
				}
				IProject project = ((IFileEditorInput)editorInput).getFile().getProject();
				IModelNature mn = EclipseResourceUtil.getModelNature(project);
				if(mn!=null) {
					xm = mn.getModel();
				}
				tldVersion=WebProject.getTldVersion(sourceNodeTaglib.getUri(), 
						sourceNodeTaglib.getPrefix(),
						pageContext.getSourceBuilder().getStructuredTextViewer().getDocument(), 
						xm);
				if(tldVersion!=null) {
				
					tldVersionNumber = stringVersionToDouble(tldVersion);
				} else {
					// if the version of the TLD-file is not specified or the file is not found
					// then assume it is the last version
					tldVersionNumber = Double.MAX_VALUE;
				}
			}
			if((startValue<=tldVersionNumber)&&
					(endValue>=tldVersionNumber)) {
				
				return new VpeValue(true);
			} else {
				return new VpeValue(false);
			}

	}
	
	private double getStartVersion(String tagValue) {
		
		try {
			if(tagValue.indexOf(MIN_VERSION_KEYWORD)!=-1){
			    return stringVersionToDouble(tagValue.substring(tagValue.indexOf(MIN_VERSION_KEYWORD)+4));    
			} else {
				
				return (-1)*Double.MAX_VALUE;
			}
		} catch (NumberFormatException e) {
			
			VpePlugin.getPluginLog().logError(e);
		}
		return (-1)*Double.MAX_VALUE;
	}
	
	private double getEndVersion(String tagValue) {
		
		try{
			if(tagValue.indexOf(MAX_VERSION_KEYWORD)!=-1) {
			    return stringVersionToDouble(tagValue.substring(tagValue.indexOf(MAX_VERSION_KEYWORD)+4));    
			}else {
				return Double.MAX_VALUE;
			}
		} catch (NumberFormatException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return Double.MAX_VALUE;
	}
	
	/**
	 * Converts {@code sVersion} representing version of a library to
	 * {@code double} value. 
	 * 
	 * @deprecated it does not make difference between incremental versions
	 * like 1.2.0 and 1.2.1. This method have to be replaced by a more complicated
	 * version-comparator like [<a href="http://maven.apache.org/ref/current/maven-artifact/apidocs/org/apache/maven/artifact/versioning/package-summary.html">http://maven.apache.org/ref/current/maven-artifact/apidocs/org/apache/maven/artifact/versioning/package-summary.html</a>] 
	 */
	private double stringVersionToDouble(String sVersion) {
		String tokens[] = sVersion.split("\\."); //$NON-NLS-1$

		String parseableVersion;
		switch (tokens.length) {
		case 0:
			parseableVersion = "0"; //$NON-NLS-1$
			break;
		case 1:
			parseableVersion = tokens[0];
			break;
		case 2: default:
			parseableVersion = tokens[0] + '.' + tokens[1];
			break;
		}

		double dVersion;
		try {
			dVersion = Double.parseDouble(parseableVersion);
		} catch (NumberFormatException e) {
			VpePlugin.getPluginLog().logError(e);
			dVersion = 0.0;
		}

		return dVersion;
	}
}
