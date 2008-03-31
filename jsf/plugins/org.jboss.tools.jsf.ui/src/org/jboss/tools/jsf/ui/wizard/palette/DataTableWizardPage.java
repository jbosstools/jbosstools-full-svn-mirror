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
package org.jboss.tools.jsf.ui.wizard.palette;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.compare.Splitter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.jboss.tools.common.model.ui.attribute.XAttributeSupport;
import org.jboss.tools.common.model.ui.attribute.editor.IPropertyEditor;
import org.jboss.tools.common.model.ui.objecteditor.XChildrenEditor;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizard;
import org.jboss.tools.common.model.ui.wizards.query.list.AbstractListWizardView;

import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.XEntityDataImpl;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.AbstractTableHelper;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jsf.model.pv.JSFPromptingProvider;
import org.jboss.tools.common.model.ui.editors.dnd.*;
import org.jboss.tools.common.model.ui.editors.dnd.composite.*;
import org.jboss.tools.common.model.ui.editors.dnd.composite.TagAttributesComposite.AttributeDescriptorValue;

/**
 *  @author erick 
 */

public class DataTableWizardPage extends TagAttributesWizardPage {
	Properties properties;

	XAttributeSupport support = new XAttributeSupport();

	final XEntityData data;

	IDropWizardModel fWizardModel;

	XModelObject propertyListObject = PreferenceModelUtilities
			.getPreferenceModel().createModelObject("JSFDataTablePropertySet",
					null);

	XChildrenEditorImpl propertyListEditor = new XChildrenEditorImpl();
	
	private PropertyChangeListener pcl,mpcl;

	public void propertyChange(PropertyChangeEvent evt) {

	}

	public DataTableWizardPage() {
		data = XEntityDataImpl.create(new String[][] {
				{ "JSFDataTableWizard", "yes" }, { "var", "no" },
				{ "value", "no" }, { "value bean class", "no" } });
	}

	public void setProperties(Properties p) {
		properties = p;
	}

	public void createControl(Composite parent) {
		Composite maincomposite = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		maincomposite.setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH);
		maincomposite.setLayoutData(data);

		showAttributes(maincomposite);

		setControl(maincomposite);
		getSpecificWizard().getWizardModel().addPropertyChangeListener(
				IDropWizardModel.TAG_PROPOSAL, this);
		updateTitle();
		runValidation();
	}

	private TabItem general = null;

	private TabFolder tabs = null;

	private Splitter composite = null;

	public void showAttributes(Composite parent) {
		composite = new Splitter(parent, SWT.HORIZONTAL);

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		composite.setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(data);

		tabs = new TabFolder(composite, SWT.TOP);
		tabs.setLayoutData(data);
		general = new TabItem(tabs, SWT.NONE);

		general.setText(DropWizardMessages.General_Tab_Title);

		Composite generalTabContent = new Composite(tabs, SWT.NONE);

		general.setControl(fillGeneralOption(generalTabContent));

		TabItem advanced = new TabItem(tabs, SWT.NONE);
		advanced.setText(DropWizardMessages.Advanced_Tab_Title);
		TagAttributesComposite advancedTabContent = new TagAttributesComposite(
				tabs, SWT.NONE, getSpecificWizard().getWizardModel());
		advanced.setControl(advancedTabContent);
		
		tabs.addSelectionListener(advancedTabContent);
		
		setControl(composite);
		getSpecificWizard().getWizardModel().addPropertyChangeListener(
				IDropWizardModel.TAG_PROPOSAL, this);
		updateTitle();
		runValidation();
	}

	protected void updateTitle() {
		TagProposal tagProposal = (TagProposal) getDropWizardModel()
				.getTagProposal();
		StringBuffer titleText = new StringBuffer();
		titleText.append("<");
		if (!TagProposal.EMPTY_PREFIX.equals(tagProposal.getPrefix())) {
			titleText.append(tagProposal.getPrefix()).append(":");
		}
		titleText.append(tagProposal.getName()).append(">");
		setTitle(titleText.toString());
	}

	public Composite fillGeneralOption(Composite generalTabContent) {

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 3;
		generalTabContent.setLayout(layout);

		XModel model = getXModel();
		if(model == null) model = PreferenceModelUtilities.getPreferenceModel();
		support.init(model.getRoot(), data);
		Control c = support.createControl(generalTabContent);
		pcl = new PCL();
		support.addPropertyChangeListener(pcl);
		
		fWizardModel = getSpecificWizard().getWizardModel();
		mpcl = new MPCL();
		fWizardModel.addPropertyChangeListener(mpcl);		
		
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		c.setLayoutData(data);

		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		generalTabContent.setLayoutData(data);

		Label properties = new Label(generalTabContent, SWT.NONE);
		properties.setText("Properties");
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 3;
		properties.setLayoutData(data);

		propertyListEditor.setObject(propertyListObject);
		Control propertiesTable = propertyListEditor
				.createControl(generalTabContent);
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		propertiesTable.setLayoutData(data);

		return generalTabContent;
	}
	boolean flag = false;	

	class PCL implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			
			if (!flag) {
				flag = true;	
				if (IPropertyEditor.VALUE.equals(evt.getPropertyName())
						&& evt.getSource() == support.getPropertyEditorAdapterByName("value bean class")) {
					onValueChanged((String) evt.getOldValue(), (String) evt.getNewValue());					
				}
				
			setTagAttributesValues();
			flag = false;
			} 
		}
	}
	
	class MPCL implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			
			if (!flag) {
				flag = true;
				AttributeDescriptorValue[] value = fWizardModel.getAttributeValueDescriptors();
				for (int i = 0 ; i < 2; i++) { 
					if (value[i].getName().equals("value"))	{
						support.store();
						support.getPropertyEditorAdapterByName("value").setValue((String)value[i].getValue());												
					}
					if (value[i].getName().equals("var"))	{
						support.store();		
						support.getPropertyEditorAdapterByName("var").setValue((String)value[i].getValue());												
					}
				}				
				flag = false;
			} 							
		}
	}

	Map<String,String[]> propertiesHash = new HashMap<String,String[]>();

	void onValueChanged(String ov, String nv) {
		String[] vs = (String[]) propertiesHash.get(nv);
		if (vs == null)
			vs = new String[0];
		String[] ovs = getSelectedProperties();
		if (ov != null && ovs != null)
			propertiesHash.put(ov, ovs);
		setSelectedProperties(vs);	
			propertyListEditor.updateBar();
	}

	class XChildrenEditorImpl extends XChildrenEditor {
		protected AbstractTableHelper createHelper() {
			return new Helper();
		}

		protected String getAddActionPath() {
			return "CreateActions.AddProperties";
		}

		public void action(String command) {
			super.action(command);
		}

		protected boolean areUpDounActionsEnabled() {
			return true;
		}

		protected void add() {
			String[] ap = getAvailableProperties(data.getValue("value bean class"));
			String[] sp = getSelectedProperties();
			Properties p = new Properties();
			Set<String> set1 = new HashSet<String>(), set2 = new TreeSet<String>();
			for (int i = 0; i < sp.length; i++)
				set1.add(sp[i]);
			for (int i = 0; i < ap.length; i++)
				if (!set1.contains(ap[i]))
					set2.add(ap[i]);
			String[][] vs = new String[set1.size() + set2.size()][2];
			int k = 0;
			Iterator it = set1.iterator();
			for (int i = 0; i < set1.size(); i++) {
				vs[k][0] = it.next().toString();
				vs[k][1] = "no"; // "no" = selected
				++k;
			}
			it = set2.iterator();
			for (int i = 0; i < set2.size(); i++) {
				vs[k][0] = it.next().toString();
				vs[k][1] = "yes";
				++k;
			}

			p.put("data", vs);
			SelectPropertiesWizard w = new SelectPropertiesWizard();
			p.setProperty("title", "Bean Properties");			
			w.setObject(p);			
			int r = w.execute();
			if (r != 0)
				return;
			vs = (String[][]) p.get("data");
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < vs.length; i++) {
				if (vs[i][1].equals("no")) {
					list.add(vs[i][0]);
				}
			}
			sp = list.toArray(new String[0]);
			setSelectedProperties(sp);
		}
		
	protected void updateBar() {
			super.updateBar();
			String[] ap = getAvailableProperties(data.getValue("value bean class"));
			boolean addflag = false;			
			if (ap.length > 0) addflag = true;			
			bar.setEnabled(ADD, addflag);			
		}
	}

	private void setSelectedProperties(String[] s) {
		XModelObject[] cs = propertyListObject.getChildren();
		for (int i = 0; i < cs.length; i++)
			cs[i].removeFromParent();
		for (int i = 0; i < s.length; i++) {
			XModelObject c = propertyListObject.getModel().createModelObject(
					"JSFDataTableProperty", null);
			c.setAttributeValue("name", s[i]);
			propertyListObject.addChild(c);
		}
		propertyListEditor.update();
	}

	public String[] getSelectedProperties() {
		support.store();
		XModelObject[] cs = propertyListObject.getChildren();
		String[] s = new String[cs.length];
		for (int i = 0; i < cs.length; i++) {
			s[i] = cs[i].getAttributeValue("name");
		}
		return s;
	}

	private String[] getAvailableProperties(String value) {
		JSFPromptingProvider provider = new JSFPromptingProvider();

		XModel xModel = getXModel();

		return (String[]) provider.buildBeanProperties(xModel, value,
				null).toArray(new String[0]);
	}

	public void setTagAttributesValues() {
		support.store();	
		fWizardModel.setAttributeValue("value", data.getValue("value"));
		fWizardModel.setAttributeValue("var", data.getValue("var"));
	}

	public String getVar() {
		return data.getValue("var");
	}

	public String getValue() {
		return data.getValue("value");
	}
	
	private XModel getXModel() {
		IFile file = (IFile) properties.get("file");
		return file == null ? null :getXModel(file);
	}

	public static XModel getXModel(IFile file) {
		if (file == null)
			return null;
		XModelObject fs = EclipseResourceUtil.getObjectByResource(file);
		if (fs != null)
			return fs.getModel();
		IProject project = file.getProject();
		IModelNature nature = EclipseResourceUtil.getModelNature(project);
		return (nature == null) ? null : nature.getModel();
	}

	public void dispose() {
		if (pcl !=  null && support != null) {
			support.removePropertyChangeListener(pcl);
			pcl=null;
		}
		if (mpcl !=  null && fWizardModel != null) {
			fWizardModel.removePropertyChangeListener(mpcl);
			mpcl=null;
		}
		super.dispose();
	}

}

class Helper extends AbstractTableHelper {
	static String[] header = new String[] { "name" };

	public String[] getHeader() {
		return header;
	}
}

class SelectPropertiesWizard extends AbstractQueryWizard {
	public SelectPropertiesWizard() {
		setView(new SelectPropertiesWizardView());
	}
}

class SelectPropertiesWizardView extends AbstractListWizardView {

	protected String[] getActions() {
		return new String[] { "Select All", "Deselect All" };
	}

	protected void internalAction(String command) {
		if (command.equals("Select All")) {
			for (int i = 0; i < boxes.length; i++)
				if ("yes".equals(vs[i][1])) {
					boxes[i].setSelection(true);
					apply(i);
				}
		} else if (command.equals("Deselect All")) {
			for (int i = 0; i < boxes.length; i++)
				if ("no".equals(vs[i][1])) {
					boxes[i].setSelection(false);
					apply(i);
				}
		}
	}

}
