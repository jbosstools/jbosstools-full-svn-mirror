/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.as.ui.wizards;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wst.server.ui.wizard.WizardFragment;
import org.jboss.ide.eclipse.as.ui.JBossServerUISharedImages;

/**
 * @author Marshall
 */
public class JBossConfigurationTableViewer extends TableViewer
{
	//private String jbossHome;
	private String selectedConfiguration;
	private WizardFragment fragment;
	
	public JBossConfigurationTableViewer (Composite parent)
	{
		super (parent);
		init();
	}
	
	public JBossConfigurationTableViewer (Composite parent, int style)
	{
		super (parent, style);
		init();
	}
	
	public JBossConfigurationTableViewer (Table table)
	{
		super (table);
		init();
	}
	
	public void setJBossHome (String jbossHome)
	{
		//this.jbossHome = jbossHome;
		setInput(jbossHome);
	}
	
	public String getSelectedConfiguration ()
	{
		return selectedConfiguration;
	}
	
	public void setDefaultConfiguration (String defaultConfiguration)
	{
		int item = -1;
		TableItem items[] = getTable().getItems();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] != null && items[i].getText() != null && items[i].getText().equals(defaultConfiguration))
			{
				item = i;
				break;
			}
		}
		
		if (item != -1) {
			getTable().setSelection(item);
		}
		
		selectedConfiguration = defaultConfiguration;
	}
	
	private void init ()
	{
		ConfigurationProvider provider = new ConfigurationProvider();
		setContentProvider(provider);
		setLabelProvider(provider);
		getControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		getControl().setEnabled(false);
		addSelectionChangedListener(new ISelectionChangedListener()
		{
			public void selectionChanged(SelectionChangedEvent event)
			{
				configurationSelected();
			}
		});
	}
	
	protected String getCurrentlySelectedConfiguration ()
	{
		if (getSelection() instanceof IStructuredSelection)
		{
			IStructuredSelection selection = (IStructuredSelection) getSelection();
			return (String) selection.getFirstElement();	
		}
		
		return null;
	}
	
	protected void configurationSelected ()
	{
		selectedConfiguration = getCurrentlySelectedConfiguration();
		
		if (fragment != null)
		{
			fragment.updateChildFragments();
		}
	}
	
	protected class ConfigurationProvider implements IStructuredContentProvider, ILabelProvider
	{
		public void dispose() {
			// ignore
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// ignore
		}
		
		public Object[] getElements(Object inputElement)
		{
			ArrayList configList = new ArrayList();
			File serverDirectory = new File(inputElement.toString() + File.separator + "server");
			
			if (serverDirectory.exists())
			{
				
				File types[] = serverDirectory.listFiles();
				for (int i = 0; i < types.length; i++)
				{
					File serviceDescriptor = new File(
						types[i].getAbsolutePath() + File.separator +
						"conf" + File.separator + "jboss-service.xml");
					
					if (types[i].isDirectory() && serviceDescriptor.exists())
					{
						String configuration = types[i].getName();
						
						// Can't shutdown the minimal configuration -- hopefully we can find something a little less crude in the future.
						//if (!configuration.equals("minimal"))
							configList.add(configuration);
					}
				}
				
				if (configList.size() > 0)
				{
					getControl().setEnabled(true);
				}
			}
			
			return configList.toArray();
		}
		
		public void addListener(ILabelProviderListener listener) {
			// ignore
		}
		public boolean isLabelProperty(Object element, String property) { return false; }
		public void removeListener(ILabelProviderListener listener) {
			// ignore
		}
		
		public Image getImage(Object element)
		{
			return JBossServerUISharedImages.getImage(JBossServerUISharedImages.IMG_JBOSS_CONFIGURATION);
		}
		
		public String getText(Object element)
		{
			return (String) element;
		}
	}

	public void setWizardFragment(WizardFragment fragment) {
		this.fragment = fragment;
	}
}
