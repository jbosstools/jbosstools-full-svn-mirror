package org.hibernate.eclipse.mapper.editors;

import org.eclipse.ui.PartInitException;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.hibernate.eclipse.mapper.MapperPlugin;
import org.hibernate.eclipse.mapper.editors.reveng.HibernateConfigurationForm;

public class HibernateCfgXmlEditor extends XMLMultiPageEditorPart {

	private HibernateConfigurationForm configurationForm;
	private int configurationPageNo;
	private StructuredTextEditor sourcePage;
	
	public HibernateCfgXmlEditor() {
		super();
	}

	protected void createPages() {
		try {
			addFormPage();
			super.createPages();
			initSourcePage();
		} catch (PartInitException pe) {
			MapperPlugin.getDefault().getLogger().logException(
					"Could not create form part for hibernate.cfg.xml editor", pe );
		}
	}

	private void initSourcePage() {
		int pageCount = getPageCount();
		for (int i = 0; i < pageCount; i++) {
			if ( getEditor( i ) instanceof StructuredTextEditor ) {
				sourcePage = (StructuredTextEditor) getEditor( i );							
			}
		}
		
		configurationForm.setModel(getStructuredModel());
	}

	private void addFormPage() throws PartInitException {
		configurationForm = new HibernateConfigurationForm();
        configurationForm.createPartControl(getContainer());
        configurationPageNo = addPage(configurationForm.getControl());
        setPageText(configurationPageNo, "Configuration");
        setActivePage( 0 );
	}
	
	IStructuredModel getStructuredModel() {
		//TODO:how to get it without usage of deprecated methods ?
		return sourcePage.getModel();
	}
	
}
