/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.css.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSNode;
import org.jboss.tools.jst.css.messages.CSSUIMessages;
import org.jboss.tools.jst.jsp.outline.cssdialog.common.Constants;
import org.w3c.dom.css.CSSStyleRule;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public class CSSPreview extends ViewPart implements ISelectionListener {

	private Browser browser;

	private Text previewText;

	private String previewContent = CSSUIMessages.CSSPreview_DefaultBrowserText;

	private Map<String, String> styleAttributes = new HashMap<String, String>();

	// private String location;
	// private String selectorName;

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		site.getWorkbenchWindow().getSelectionService()
				.addPostSelectionListener(this);
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removePostSelectionListener(this);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		final SashForm previewComposite = new SashForm(parent, SWT.None);
		previewComposite.setLayout(new GridLayout());
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		previewComposite.setLayoutData(gridData);

		browser = new Browser(previewComposite, SWT.BORDER | SWT.MOZILLA);
		browser.setText(generateBrowserPage());
		browser.setLayoutData(gridData);
		browser.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if (e.widget == browser) {
					browser.setEnabled(false);
					previewComposite.setMaximizedControl(previewText);
					previewText.setFocus();
				}
			}
		});

		previewText = new Text(previewComposite, SWT.NONE | SWT.H_SCROLL);
		previewText.setLayoutData(gridData);
		previewText.setText(getPreviewContent());
		previewText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (e.widget == previewText) {
					String text = previewText.getText();
					if (text == null || text.equals(Constants.EMPTY)) {
						setPreviewContent(CSSUIMessages.CSSPreview_DefaultBrowserText);
					} else {
						setPreviewContent(text);
					}

					browser.setEnabled(true);
					browser.setText(generateBrowserPage());
					previewComposite.setMaximizedControl(browser);
				}
			}
		});

		previewComposite.setMaximizedControl(browser);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		browser.setFocus();
	}

	/**
	 * /** Method is used to build html body that is appropriate to browse.
	 * 
	 * @return String html text representation
	 */
	public String generateBrowserPage() {

		StringBuffer html = new StringBuffer(Constants.OPEN_DIV_TAG);

		for (Map.Entry<String, String> styleItem : styleAttributes.entrySet()) {

			html.append(styleItem.getKey() + Constants.COLON
					+ styleItem.getValue() + Constants.SEMICOLON);
		}

		html.append("\">" + getPreviewContent() + Constants.CLOSE_DIV_TAG); //$NON-NLS-1$

		return html.toString();
	}

	public String getPreviewContent() {
		return previewContent;
	}

	public void setPreviewContent(String previewContent) {
		this.previewContent = previewContent;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof ICSSNode) {

				CSSStyleRule styleRule = CSSViewUtil
						.getStyleRule((ICSSNode) structuredSelection
								.getFirstElement());

				if (styleRule != null) {
					styleAttributes = CSSViewUtil.getStyleAttributes(styleRule);

				} else {
					styleAttributes.clear();
				}

			}
			updateBrowser();
		}

	}

	public void updateBrowser() {
		browser.setText(generateBrowserPage());
	}

}
