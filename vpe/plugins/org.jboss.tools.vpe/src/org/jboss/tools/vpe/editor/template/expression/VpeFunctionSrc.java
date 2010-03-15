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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.jst.jsp.contentassist.JspContentAssistProcessor;
import org.jboss.tools.jst.jsp.outline.ValueHelper;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageProcessor;
import org.jboss.tools.jst.web.kb.KbQuery.Type;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType;
import org.jboss.tools.jst.web.kb.internal.proposal.CustomProposalType.Param;
import org.jboss.tools.jst.web.kb.internal.taglib.CustomTagLibAttribute;
import org.jboss.tools.jst.web.kb.taglib.IAttribute;
import org.jboss.tools.jst.web.tld.IFilePathEncoder;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.jboss.tools.vpe.editor.util.ElService;
import org.jboss.tools.vpe.editor.util.Jsf2ResourceUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.w3c.dom.Node;

public class VpeFunctionSrc extends VpeFunction {
    static final String IMG_UNRESOLVED = "unresolved_image.gif"; //$NON-NLS-1$
    static final String IMG_PREFIX = "file:///"; //$NON-NLS-1$
//    private static final Pattern resourcePatternWithSinglCoat= Pattern.compile("[#\\$]\\{\\s*resource\\s*\\[\\s*'(.*)'\\s*\\]\\s*\\}"); //$NON-NLS-1$
//    private static final Pattern resourcePatternWithDoableCoat= Pattern.compile("[#\\$]\\{\\s*resource\\s*\\[\\s*\"(.*)\"\\s*\\]\\s*\\}"); //$NON-NLS-1$
    
    
    public VpeValue exec(VpePageContext pageContext, Node sourceNode) throws VpeExpressionException {
	String tagValue = getParameter(0).exec(pageContext, sourceNode)
		.stringValue();
//	tagValue = resolveEL(pageContext,tagValue);
	IFile iFile = VpeCreatorUtil.getFile(tagValue, pageContext);

	if (iFile != null) {
	    return new VpeValue(getPrefix()+iFile.getLocation().toString());
	}
	tagValue = processValue(pageContext, sourceNode, tagValue);

	// decode string from utf
	try {
	    tagValue = URLDecoder.decode(tagValue, "UTF-8"); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		
	    VpePlugin.getPluginLog().logError(e);
	} catch (IllegalArgumentException ex) {
		//if user enter invalid URL, for example " % sss", 
		//illegal argument exception will be throwed, 
		//brouser will not processed this url, so we just slow this exeption
		//You can check it's by inserting next code
		//<h:graphicImage value=" %= request.getContextPath()%/images/logos/banner.png"/>
		tagValue=""; //$NON-NLS-1$
	}

	IPath tagPath = new Path(tagValue);
	if (tagPath.isEmpty())
	    return new VpeValue(getUnresolved());

	String device = (tagPath.getDevice() == null ? tagPath.segment(0)
		: tagPath.getDevice());
	if (device != null
		&& ("http:".equalsIgnoreCase(device) //$NON-NLS-1$
			|| "file:".equalsIgnoreCase(device))) //$NON-NLS-1$
	    return new VpeValue(tagValue);

	File locFile = tagPath.toFile();
	if (locFile.exists())
	    return new VpeValue(getPrefix() + locFile.getAbsolutePath());

	IEditorInput input = pageContext.getEditPart().getEditorInput();
	IPath inputPath = getInputParentPath(input);
	IPath imgPath = null;
	if (input instanceof ILocationProvider) {
	    imgPath = inputPath.append(tagValue);
	} else {
	    IPath basePath = tagPath.isAbsolute() ? VpeStyleUtil.getRootPath(input)
		    : inputPath;
	    if (basePath != null) {
		imgPath = basePath.append(tagPath);
	    }
	}

	if (imgPath != null && imgPath.toFile().exists()) {
	    return new VpeValue(getPrefix() + imgPath.toString());
	} else {
	    IFile file = null;
	    if (input instanceof IFileEditorInput) {
		file = ((IFileEditorInput) input).getFile();
	    }

	    if (null != file) {
		ResourceReference resourceReference = null;
		if ("/".equals(tagValue.substring(0, 1))) { //$NON-NLS-1$
		    resourceReference = pageContext
			    .getRuntimeAbsoluteFolder(file);
		    tagValue = tagValue.substring(1);
		} else {
		    resourceReference = pageContext
			    .getRuntimeRelativeFolder(file);
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
	    inputPath = ((ILocationProvider) input).getPath(input);
	} else if (input instanceof IFileEditorInput) {
	    IFile inputFile = ((IFileEditorInput) input).getFile();
	    if (inputFile != null) {
		inputPath = inputFile.getLocation();
	    }
	}
	if (inputPath != null && !inputPath.isEmpty()) {
	    inputPath = inputPath.removeLastSegments(1);
	}
	return inputPath;
    }

    protected String getUnresolved() {
	return IMG_PREFIX + getAbsoluteResourcePath(IMG_UNRESOLVED).replace('\\', '/');
    }

    protected String getPrefix() {
	return IMG_PREFIX;
    }

    private String processValue(VpePageContext pageContext, Node sourceNode,
	    String tagValue) throws VpeExpressionException {
	String attrName = null;
	if (getParameter(0) instanceof VpeAttributeOperand) {
	    attrName = ((VpeAttributeOperand) getParameter(0)).getAttributeName();
	}
	
	String query = attrName == null
			? null
			: "/" + sourceNode.getNodeName() + "@" + attrName; //$NON-NLS-1$  //$NON-NLS-2$

	IDocument document = pageContext.getSourceBuilder().getStructuredTextViewer().getDocument();
	if (document == null || query == null) {
	    return tagValue;
	}
	
	ValueHelper valueHelper = new ValueHelper();
	JspContentAssistProcessor processor = valueHelper.createContentAssistProcessor();
	int offset = 0;
	if(sourceNode instanceof IndexedRegion) {
		offset = ((IndexedRegion)sourceNode).getStartOffset() + 1;
	}
	IPageContext pc = valueHelper.createPageContext(processor, offset);
	KbQuery kbQuery = createKbQuery(processor, sourceNode, offset, attrName);
	IAttribute[] as = PageProcessor.getInstance().getAttributes(kbQuery, pc);
	CustomTagLibAttribute a = null;
	for (IAttribute i: as) {
		if(i instanceof CustomTagLibAttribute) {
			a = (CustomTagLibAttribute)i;
			break;
		}
	}
	if(a != null) {
		CustomProposalType[] ds = a.getProposals();
	    for (int i = 0; i < ds.length; i++) {
			if (!"file".equals(ds[i].getType())) {//$NON-NLS-1$
			    continue;
			}
			Param[] ps = ds[i].getParams();
			for (Param p: ps) {
				if(IFilePathEncoder.PATH_ADDITION.equals(p.getName())) {
					String v = p.getValue();
					if(v != null && v.length() > 0 && tagValue.startsWith(v)) {
						tagValue = tagValue.substring(v.length());
					}
				}
			}
	    }
	}
	
//	WtpKbConnector connector = pageContext.getConnector();
//	try {
//	    AttributeDescriptor descriptor = connector.getAttributeInformation(query);
//	    if (descriptor == null) {
//		return tagValue;
//	    }
//	    
//	    AttributeValueDescriptor[] ds = descriptor.getValueDesriptors();
//	    for (int i = 0; i < ds.length; i++) {
//		if (!"file".equals(ds[i].getType())) {//$NON-NLS-1$
//		    continue;
//		}
//		
//		ParamList params = ds[i].getParams();
//		String[] vs = params.getParamsValues(IFilePathEncoder.PATH_ADDITION);
//		if (vs == null || vs.length == 0) {
//		    continue;
//		}
//		
//		if (tagValue.startsWith(vs[0])) {
//		    tagValue = tagValue.substring(vs[0].length());
//		}
//	    }
//	} catch (KbException e) {
//	    VpePlugin.getPluginLog().logError(e);
//	}
	
	return tagValue;
    }

    /*
     * temporary solution to solve #{facesContext.externalContext.requestContextPath}
     * JBIDE-1410
     */
//    protected String resolveEL(VpePageContext pageContext, String value) {
//        String resolvedValue = value.replaceFirst("^\\s*(\\#|\\$)\\{facesContext.externalContext.requestContextPath\\}", ""); //$NON-NLS-1$ //$NON-NLS-2$
//        
//        //fix for JBIDE-2550, author Maksim Areshkau
//        if(Jsf2ResourceUtil.isJSF2ResourceString(resolvedValue)){
//        	resolvedValue = Jsf2ResourceUtil.processCustomJSFAttributes(pageContext, resolvedValue);
//        }
//
//        //Fix for JBIDE-3030
//        if(pageContext.getVisualBuilder().getCurrentIncludeInfo()==null
//        		|| !(pageContext.getVisualBuilder().getCurrentIncludeInfo() instanceof IFile)){
//        	return resolvedValue;
//        }
//        final IFile file = (IFile) pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage();
//
//        resolvedValue = ElService.getInstance().replaceEl(file, resolvedValue);
//        return resolvedValue;
//    }
    
    public static String getAbsoluteResourcePath(String resourcePathInPlugin) {
	String pluginPath = VpePlugin.getPluginResourcePath();
	IPath pluginFile = new Path(pluginPath);
	File file = pluginFile.append(resourcePathInPlugin).toFile();
	if (file.exists()) {
	    return file.getAbsolutePath();
	} else {
	    throw new RuntimeException("Can't get path for " //$NON-NLS-1$
		    + resourcePathInPlugin);
	}
    }

	protected KbQuery createKbQuery(JspContentAssistProcessor processor, Node fNode, int offset, String attrName) {
		KbQuery kbQuery = new KbQuery();

		String[] parentTags = processor.getParentTags(false);
		parentTags = add(parentTags, fNode.getNodeName());
		String prefix = getPrefix(fNode);
		kbQuery.setPrefix(prefix);
		kbQuery.setUri(processor.getUri(prefix));
		kbQuery.setParentTags(parentTags);
		kbQuery.setParent(fNode.getNodeName());
		kbQuery.setMask(false); 
		kbQuery.setType(Type.ATTRIBUTE_NAME);
		kbQuery.setOffset(offset);
		kbQuery.setValue(attrName); 
		kbQuery.setStringQuery(attrName);
		
		return kbQuery;
	}

	private String[] add(String[] result, String v) {
		String[] result1 = new String[result.length + 1];
		System.arraycopy(result, 0, result1, 0, result.length);
		result1[result.length] = v;
		return result1;
	}
	private String getPrefix(Node fNode) {
		int i = fNode.getNodeName().indexOf(':');
		return i < 0 ? null : fNode.getNodeName().substring(0, i);
	}

}