/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat, Inc.
 *******************************************************************************/
package org.jboss.tools.cdi.text.ext.hyperlink.xpl;

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.SearchPattern;
import org.jboss.tools.cdi.text.ext.hyperlink.IInformationItem;

/**
 * Show hierarchy in light-weight control.
 *
 * @since 3.0
 */
public class HierarchyInformationControl extends AbstractInformationControl {
	private IHyperlink[] hyperlinks;

	private BeanTableLabelProvider fLabelProvider;

	public HierarchyInformationControl(Shell parent, String title, int shellStyle, int tableStyle, IHyperlink[] hyperlinks) {
		super(parent, shellStyle, tableStyle, IJavaEditorActionDefinitionIds.OPEN_HIERARCHY, true);
		this.hyperlinks = hyperlinks;
		setTitleText(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean hasHeader() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.text.JavaOutlineInformationControl#createTableViewer(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected TableViewer createTableViewer(Composite parent, int style) {
		Table table = new Table(parent, SWT.SINGLE | (style & ~SWT.MULTI));
		GridData gd= new GridData(GridData.FILL_BOTH);
		gd.heightHint= table.getItemHeight() * 12;
		table.setLayoutData(gd);

		TableViewer tableViewer= new TableViewer(table);
		
		tableViewer.addFilter(new BeanFilter());

		fLabelProvider= new BeanTableLabelProvider();

		tableViewer.setLabelProvider(fLabelProvider);

		return tableViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInput(Object information) {
		if(!(information instanceof IHyperlink[])){
			inputChanged(null, null);
			return;
		}
		
		hyperlinks = (IHyperlink[])information;

		BeanTableContentProvider contentProvider= new BeanTableContentProvider(hyperlinks);
		getTableViewer().setContentProvider(contentProvider);


		inputChanged(hyperlinks, hyperlinks[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getSelectedElement() {
		Object selectedElement= super.getSelectedElement();
		return selectedElement;
	}

	@Override
	protected String getId() {
		return "org.jboss.tools.cdi.text.ext.InformationControl";
	}
	
	public static class BeanTableContentProvider implements IStructuredContentProvider{
		private IHyperlink[] hyperlinks;
		
		public BeanTableContentProvider(IHyperlink[] beans){
			this.hyperlinks = beans;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return hyperlinks;
		}

	}
	
	public class BeanFilter extends ViewerFilter {
		SearchPattern patternMatcher = new SearchPattern();
		public boolean isConsistentItem(Object item) {
			return true;
		}

		public boolean select(Viewer viewer, Object parentElement,
	            Object element) {
			
			if (element instanceof IInformationItem) {
				String information = ((IInformationItem)element).getInformation();
				if(getFilterText().getText().isEmpty()){
					patternMatcher.setPattern("*");
				}else{
					patternMatcher.setPattern(getFilterText().getText());
				}
				return patternMatcher.matches(information);
			}else
				return true;
		}
	}
	
	static Color gray = new Color(null, 128, 128, 128);
	static Color black = new Color(null, 0, 0, 0);

	static Styler NAME_STYLE = new DefaultStyler(black, false);
	static Styler PACKAGE_STYLE = new DefaultStyler(gray, false);
	
	private static class DefaultStyler extends Styler {
		private final Color foreground;
		private final boolean italic;

		public DefaultStyler(Color foreground, boolean italic) {
			this.foreground = foreground;
			this.italic = italic;
		}

		public void applyStyles(TextStyle textStyle) {
			if (foreground != null) {
				textStyle.foreground = foreground;
			}
			if(italic) {
				textStyle.font = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
			}
		}
	}
	
	class BeanTableLabelProvider extends StyledCellLabelProvider implements DelegatingStyledCellLabelProvider.IStyledLabelProvider {
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString styledString = getStyledText(element);
			cell.setText(styledString.getString());
			cell.setStyleRanges(styledString.getStyleRanges());
			cell.setImage(getImage(element));

			super.update(cell);
		}

		public String getText(Object element) {
			return getStyledText(element).getString();
		}
		public StyledString getStyledText(Object element) {
			StyledString sb = new StyledString();
			if(element instanceof IHyperlink){
				if(element instanceof IInformationItem){
					String info = ((IInformationItem)element).getInformation();
					String qualifiedName = ((IInformationItem)element).getFullyQualifiedName();
					String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
					sb.append(info, NAME_STYLE);
					sb.append(" - ", PACKAGE_STYLE).append(packageName, PACKAGE_STYLE);
				}else{
					sb.append(((IHyperlink)element).getHyperlinkText(), NAME_STYLE);
				}
			}
			return sb;
		}

		public Image getImage(Object element) {
			if(element instanceof IInformationItem){
				return ((IInformationItem)element).getImage();
			}
			return null;
		}		
	}
}

