package org.jboss.ide.eclipse.as.ui.packages;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.JBossServerCore;
import org.jboss.ide.eclipse.as.core.model.PackagesListener;
import org.jboss.ide.eclipse.as.core.server.attributes.IDeployableServer;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.ui.actions.AbstractNodeActionDelegate;

public class PublishAction extends AbstractNodeActionDelegate {

	
	public PublishAction() {
	}

	public void run (IPackageNode node) {
		if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)	
		{
			IPackage pkg = (IPackage)node;
			String servers = node.getProperty(PackagesListener.DEPLOY_SERVERS);
			if( !new Boolean(pkg.getProperty(PackagesListener.DEPLOY_AFTER_BUILD)).booleanValue()  ||
					servers == null || "".equals(servers)) {
				servers = showSelectServersDialog(pkg);
			}
			PackagesListener.publish(pkg, servers, IServer.PUBLISH_FULL);
		}
	}
	
	public boolean isEnabledFor(IPackageNode node) {
		if (node.getNodeType() == IPackageNode.TYPE_PACKAGE
			|| node.getNodeType() == IPackageNode.TYPE_PACKAGE_REFERENCE)
		{
			IPackage pkg = (IPackage) node;
			if (pkg.isTopLevel())
			{
				return true;
			}
		}
		return false;
	}
	
	protected String showSelectServersDialog(IPackage node) {
		SelectServerWizard wiz = new SelectServerWizard(node);
		new WizardDialog(new Shell(), wiz).open();
		return wiz.getServers();
	}

	
	protected class SelectServerWizard extends Wizard {
		private SelectServerWizardPage page;
		private IPackage pack;
		protected SelectServerWizard(IPackage pack) {
			this.pack = pack;
		}
		public boolean performFinish() {
			System.out.println("servers: " + getServers());
			System.out.println("autodeploy: " + getAutoDeploy());
			System.out.println("always publish to these: " + getAlwaysPublish());
			pack.setProperty(PackagesListener.DEPLOY_SERVERS, getServers());
			pack.setProperty(PackagesListener.DEPLOY_AFTER_BUILD, getAutoDeploy());
			return true;
		}
		public void addPages() {
			page = new SelectServerWizardPage(pack);
			addPage(page);
		}

		protected String getServers() {
			return page.getServers();
		}
		protected String getAutoDeploy() {
			return page.getAutoDeploy();
		}
		protected String getAlwaysPublish() {
			return page.getAlwaysPublish();
		}
	}
	
	protected class SelectServerWizardPage extends WizardPage {
		protected IPackage pack;
		protected ListViewer viewer;
		protected Button autoDeploy, alwaysPublish;
		protected String viewerResult = "";
		protected String deployResult = Boolean.toString(false);
		protected String alwaysPublishResult = Boolean.toString(false);
		
		protected SelectServerWizardPage(IPackage pack) {
			super("Wizard Page Name");
			setDescription("Wizard Page Description");
			setTitle("Wizard Page Title");
			this.pack = pack;
		}

		public void createControl(Composite parent) {
			Composite mainComposite = new Composite(parent, SWT.NONE);
			mainComposite.setLayout(new FormLayout());
			mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			fillComposite(mainComposite);
			addListeners();
			setControl(mainComposite);
		}

		protected void fillComposite(Composite mainComposite) {
			viewer = new ListViewer(mainComposite);
			FormData viewerData = new FormData();
			viewerData.left = new FormAttachment(15,0);
			viewerData.right = new FormAttachment(85,0);
			viewerData.top = new FormAttachment(0,10);
			viewerData.bottom = new FormAttachment(80,0);
			viewer.getList().setLayoutData(viewerData);
			
			this.alwaysPublish = new Button(mainComposite, SWT.CHECK);
			FormData always = new FormData();
			always.left = new FormAttachment(15,0);
			always.top = new FormAttachment(viewer.getList(), 5);
			alwaysPublish.setLayoutData(always);
			alwaysPublish.setText("Always publish to these servers");
			
			autoDeploy = new Button(mainComposite, SWT.CHECK);
			FormData add = new FormData();
			add.left = new FormAttachment(15,0);
			add.top = new FormAttachment(alwaysPublish, 5);
			autoDeploy.setLayoutData(add);
			autoDeploy.setText("Auto-deploy to selected servers after builds");
			
			
//			this.alwaysPublish = new Button(mainComposite, SWT.CHECK);
//			FormData always = new FormData();
//			always.left = new FormAttachment(15,0);
//			always.top = new FormAttachment(viewer.getList(), 5);
//			alwaysPublish.setLayoutData(always);
//			
//			Label alwaysPublishLabel = new Label(mainComposite, SWT.NONE);
//			FormData apl = new FormData();
//			apl.top = new FormAttachment(viewer.getList(), 5);
//			apl.left = new FormAttachment(alwaysPublish, 5);
//			alwaysPublishLabel.setLayoutData(apl);
//			alwaysPublishLabel.setText("Always publish to these servers");
//
//			autoDeploy = new Button(mainComposite, SWT.CHECK);
//			FormData add = new FormData();
//			add.left = new FormAttachment(15,0);
//			add.top = new FormAttachment(alwaysPublish, 5);
//			autoDeploy.setLayoutData(add);
//			
//			Label autoDeployLabel = new Label(mainComposite, SWT.NONE);
//			FormData adl = new FormData();
//			adl.top = new FormAttachment(alwaysPublishLabel, 7);
//			adl.left = new FormAttachment(autoDeploy, 5);
//			autoDeployLabel.setLayoutData(adl);
//			autoDeployLabel.setText("Auto-deploy to selected servers after builds");
		}
		protected void addListeners() {
			autoDeploy.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e) {
					deployResult = Boolean.toString(autoDeploy.getSelection() && autoDeploy.getEnabled());
				}
			});
			viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					ISelection sel = viewer.getSelection();
					if( sel instanceof IStructuredSelection ) {
						IStructuredSelection sel2 = (IStructuredSelection)sel;
						Object[] os = sel2.toArray();
						String tmp = "";
						for( int i = 0; i < os.length; i++ ) {
							tmp += ((IDeployableServer)os[i]).getServer().getId() + ",";
						}
						viewerResult = tmp;
					}
				} 
			} );
			
			alwaysPublish.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				public void widgetSelected(SelectionEvent e) {
					autoDeploy.setEnabled(alwaysPublish.getSelection());
					deployResult = Boolean.toString(autoDeploy.getSelection() && autoDeploy.getEnabled());
					alwaysPublishResult = Boolean.toString(alwaysPublish.getSelection());
				} 
			});
			
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setLabelProvider(new PublishServerLabelProvider());
			viewer.setInput(JBossServerCore.getAllDeployableServers());
			autoDeploy.setEnabled(false);
		}
			
		protected String getServers() {
			return viewerResult;
		}
		protected String getAutoDeploy() {
			return deployResult;
		}
		protected String getAlwaysPublish() {
			return alwaysPublishResult;
		}
	}
	protected class PublishServerLabelProvider extends LabelProvider {
	    public Image getImage(Object element) {
	        return null;
	    }
	    public String getText(Object element) {
	    	if( element instanceof IDeployableServer ) {
	    		return ((IDeployableServer)element).getServer().getName();
	    	}
	        return element == null ? "" : element.toString();//$NON-NLS-1$
	    }

	}
}
