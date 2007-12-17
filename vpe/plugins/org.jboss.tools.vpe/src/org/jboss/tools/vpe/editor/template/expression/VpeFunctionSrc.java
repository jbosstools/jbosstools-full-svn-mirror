/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.template.expression;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.jboss.tools.common.kb.AttributeDescriptor;
import org.jboss.tools.common.kb.AttributeValueDescriptor;
import org.jboss.tools.common.kb.ParamList;
import org.jboss.tools.common.kb.wtp.WtpKbConnector;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.IFilePathEncoder;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.css.ResourceReference;
import org.w3c.dom.Node;

public class VpeFunctionSrc extends VpeFunction {
    static final String IMG_UNRESOLVED = "unresolved.gif";
    static final String IMG_PREFIX = "file:///";

	public VpeValue exec(VpePageContext pageContext, Node sourceNode) {
		String tagValue = getParameter(0).exec(pageContext, sourceNode).stringValue();
		tagValue = processValue(pageContext, sourceNode, tagValue);
		
		// decode string from utf 
		try {
			tagValue = URLDecoder.decode(tagValue,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		IPath tagPath = new Path(tagValue);
		if (tagPath.isEmpty()) return new VpeValue(getUnresolved());

		String device = (tagPath.getDevice()==null?tagPath.segment(0):tagPath.getDevice());
		if (device != null && 
		        ("http:".equalsIgnoreCase(device) || "file:".equalsIgnoreCase(device))) return new VpeValue(tagValue);

		File locFile = tagPath.toFile();
		if (locFile.exists()) return new VpeValue(getPrefix() + locFile.getAbsolutePath()); 

		IEditorInput input = pageContext.getEditPart().getEditorInput();
	    IPath inputPath = getInputParentPath(input);
		IPath imgPath = null;
		if (input instanceof ILocationProvider) {
		    imgPath = inputPath.append(tagValue); 
		} else {
			IPath basePath = tagPath.isAbsolute() ? getRootPath(input) : inputPath;
		    if (basePath != null) {
		        imgPath = basePath.append(tagPath);
		    }
		}

		if (imgPath != null && imgPath.toFile().exists()) { 
		    return new VpeValue(getPrefix() + imgPath.toString());
		} else {
	        IFile file = null;
		    if (input instanceof IFileEditorInput) {
		        file = ((IFileEditorInput)input).getFile();
		    }

		    if (null != file) {
			    ResourceReference resourceReference = null;
				if ("/".equals(tagValue.substring(0, 1))) {
					resourceReference = pageContext.getRuntimeAbsoluteFolder(file);
					tagValue = tagValue.substring(1);
				} else {
					resourceReference = pageContext.getRuntimeRelativeFolder(file);
				}

				String location = null;
				if (resourceReference != null) {
					location = resourceReference.getLocation();
				}
				
				if (null == location && null != file.getLocation()) {
					location = file.getLocation().toFile().getParent();
				}

				if (null != location) {
					File f = new File(location + File.separator + tagValue);
					if (f.exists()) {
						return new VpeValue(getPrefix() + f.getPath());
					}
				}
		    }
		}

		return new VpeValue(getUnresolved());
	}

	protected IPath getInputParentPath(IEditorInput input) {
	    IPath inputPath = null;
		if (input instanceof ILocationProvider) {
		    inputPath = ((ILocationProvider)input).getPath(input);
		} else if (input instanceof IFileEditorInput) {
		    IFile inputFile = ((IFileEditorInput)input).getFile();
		    if (inputFile != null) {
			    inputPath = inputFile.getLocation();
		    }
		}
		if (inputPath != null && !inputPath.isEmpty()) {
		    inputPath = inputPath.removeLastSegments(1);
		}
	    return inputPath;
	}
	
	protected IPath getRootPath(IEditorInput input) {
	    IPath rootPath = null;
		if (input instanceof IFileEditorInput) {
		    IProject project = ((IFileEditorInput)input).getFile().getProject();
			if(project != null && project.isOpen()) {
				IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
				if (modelNature != null) {
					XModel model = modelNature.getModel();
					String rootPathStr = WebProject.getInstance(model).getWebRootLocation();
					if(rootPathStr!=null) {
						rootPath = new Path(rootPathStr);
					} else {
					    rootPath = project.getLocation();						
					}
				} else {
				    rootPath = project.getLocation();
				}
			}
		}
	    return rootPath;
	}

	protected String getUnresolved() {
	    return IMG_UNRESOLVED;
	}

	protected String getPrefix() {
	    return IMG_PREFIX;
	}
	
	String processValue(VpePageContext pageContext, Node sourceNode, String tagValue) {
		String attrName = null;
		if(getParameter(0) instanceof VpeAttributeOperand) {
			attrName = ((VpeAttributeOperand)getParameter(0)).getAttributeName();
		}
		String query = (attrName == null) ? null : "/" + sourceNode.getNodeName() + "@" + attrName;
				
		IDocument document = pageContext.getSourceBuilder().getStructuredTextViewer().getDocument();
		if(document == null || query == null) return tagValue;
		WtpKbConnector connector = pageContext.getConnector();
		try {
			AttributeDescriptor descriptor = connector.getAttributeInformation(query);
			if(descriptor == null) return tagValue;
			AttributeValueDescriptor[] ds = descriptor.getValueDesriptors();
			for (int i = 0; i < ds.length; i++) {
				if(!"file".equals(ds[i].getType())) continue;
				ParamList params = ds[i].getParams();
				String[] vs = params.getParamsValues(IFilePathEncoder.PATH_ADDITION);
				if(vs == null || vs.length == 0) continue;
				if(tagValue.startsWith(vs[0])) tagValue = tagValue.substring(vs[0].length());
			}
		} catch (Exception e) {
			VpePlugin.getPluginLog().logError(e);
		}
		return tagValue;
	}
	
}