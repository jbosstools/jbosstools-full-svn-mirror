package org.jboss.tools.flow.common.editor;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Common implementation of an outline page.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class OverviewOutlinePage extends Page implements IContentOutlinePage {

	private Canvas overview;
	private ScalableRootEditPart rootEditPart;
	private Thumbnail thumbnail;

	public OverviewOutlinePage(ScalableRootEditPart rootEditPart) {
		this.rootEditPart = rootEditPart;
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	public void createControl(Composite parent) {
		overview = new Canvas(parent, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(overview);
		thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
		thumbnail.setBorder(new MarginBorder(3));
		thumbnail.setSource(rootEditPart
				.getLayer(LayerConstants.PRINTABLE_LAYERS));
		lws.setContents(thumbnail);
	}

	public void dispose() {
		if (null != thumbnail) {
			thumbnail.deactivate();
		}
		super.dispose();
	}

	public Control getControl() {
		return overview;
	}

	public ISelection getSelection() {
		return StructuredSelection.EMPTY;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
	}

	public void setFocus() {
		if (getControl() != null) {
			getControl().setFocus();
		}
	}

	public void setSelection(ISelection selection) {
	}
}
