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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.common.kb.wtp.TLDVersionHelper;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
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
			
			double tldVersionNumber =0;
			if(tldVersion!=null) {
				
				tldVersionNumber = Double.parseDouble(tldVersion);
			
			} else {
				//here we getting tld version for xhtml files
				XModel xm = null;
				IProject project = ((IFileEditorInput)pageContext.getEditPart().getEditorInput()).getFile().getProject();
				IModelNature mn = EclipseResourceUtil.getModelNature(project);
				if(mn!=null) {
					xm = mn.getModel();
				}
				tldVersion=WebProject.getTldVersion(sourceNodeTaglib.getUri(), 
						sourceNodeTaglib.getPrefix(),
						pageContext.getSourceBuilder().getStructuredTextViewer().getDocument(), 
						xm);
				if(tldVersion!=null) {
				
					tldVersionNumber = Double.parseDouble(tldVersion);
				} else {
					tldVersionNumber = 0;
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
			    return Double.parseDouble(tagValue.substring(tagValue.indexOf(MIN_VERSION_KEYWORD)+4));    
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
			    return Double.parseDouble(tagValue.substring(tagValue.indexOf(MAX_VERSION_KEYWORD)+4));    
			}else {
				return Double.MAX_VALUE;
			}
		} catch (NumberFormatException e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return Double.MAX_VALUE;
	}
}
