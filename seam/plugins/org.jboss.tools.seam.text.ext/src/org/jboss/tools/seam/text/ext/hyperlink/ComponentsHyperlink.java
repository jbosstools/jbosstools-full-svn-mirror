/*******************************************************************************
 * Copyright (c) 2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.text.ext.hyperlink;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.text.ext.hyperlink.AbstractHyperlink;
import org.jboss.tools.seam.core.SeamProjectsSet;
import org.jboss.tools.seam.text.ext.SeamExtPlugin;
import org.w3c.dom.Node;

public class ComponentsHyperlink extends AbstractHyperlink {
	private String hyperlinkText = "";

	private String partitionType = null;

	@Override
	protected IRegion doGetHyperlinkRegion(int offset) {
		Node node = ComponentsHyperlinkPartitioner.getNode(getDocument(),
				offset);
		partitionType = ComponentsHyperlinkPartitioner.getType(node);
		if (partitionType == null)
			return null;

		IndexedRegion text = (IndexedRegion) node;

		int regLength = text.getLength();
		int regOffset = text.getStartOffset();

		Region region = new Region(regOffset, regLength);
		return region;
	}

	@Override
	protected void doHyperlink(IRegion region) {
		if (region == null)
			return;

		try {
			IDocument document = getDocument();
			hyperlinkText = document
					.get(region.getOffset(), region.getLength());
		} catch (BadLocationException ex) {
			SeamExtPlugin.getPluginLog().logError(ex);
		}

		if (partitionType == ComponentsHyperlinkPartitioner.BPM_DEFINITION_PARTITION)
			doBpmDefinitionHyperlink(region);
		else if (partitionType == ComponentsHyperlinkPartitioner.DROOLS_RULE_PARTITION)
			doDroolsRuleHyperlink(region);

	}

	private void doDroolsRuleHyperlink(IRegion region) {
		IProject project = getProject();
		IResource[] sources = EclipseResourceUtil.getJavaSourceRoots(project);

		for (IResource resource : sources) {
			String path = resource.getFullPath().removeFirstSegments(1)
					+ hyperlinkText;
			IFile file = project.getFile(path);
			if (file.exists()) {
				openFileInEditor(file);
				return;
			}
		}
	}

	private void doBpmDefinitionHyperlink(IRegion region) {
		IFile file = findDefinitionFile();
		if (file != null)
			openFileInEditor(file);
	}
	
	private IFile findDefinitionFile(){
		IFile file;
		SeamProjectsSet projectsSet = SeamProjectsSet.create(getProject());
		
		IContainer webContent = projectsSet.getDefaultViewsFolder();
		
		if(webContent != null){
			file = webContent.getFile(new Path(hyperlinkText));
			if(file != null && file.exists())
				return file;
		}
		
		IContainer earContent = projectsSet.getDefaultEarViewsFolder();
		
		if(earContent != null){
			file = earContent.getFile(new Path(hyperlinkText));
			if(file != null && file.exists())
				return file;
		}
		
		IContainer ejbSource = projectsSet.getDefaultEjbSourceFolder();
		
		if(ejbSource != null){
			file = ejbSource.getFile(new Path(hyperlinkText));
			if(file != null && file.exists())
				return file;
		}
		
		return null;
	}

	private IProject getProject() {
		IFile documentFile = getFile();
		if (documentFile == null || !documentFile.isAccessible())
			return null;

		IProject project = documentFile.getProject();

		return project;
	}

	@Override
	public String getHyperlinkText() {
		return hyperlinkText;
	}

}
