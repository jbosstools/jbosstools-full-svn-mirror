package org.jboss.ide.eclipse.as.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.corext.util.Resources;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.ui.internal.wizards.NewModelWizard;
import org.jboss.ide.eclipse.as.core.util.ASDebug;

public class NewMBeanWizard extends NewModelWizard implements INewWizard {

	private IStructuredSelection sel;
	private MBeanInterfacePage interfacePage;
	private MBeanPage mbeanPage;
	private NewFilePage newFilePage;
	private static String INTERFACE_NAME = "__INTERFACE_NAME__";

	public NewMBeanWizard() {
	}

    public void createPageControls(Composite pageContainer) {
    	super.createPageControls(pageContainer);
    	newFilePage.setVisible(false);
    }

	
	
	public boolean performFinish() {
		if( !canFinish() ) return false;
		try {
			interfacePage.createType(new NullProgressMonitor());
			mbeanPage.createType(new NullProgressMonitor());
			
			if( mbeanPage.shouldCreateDescriptor()) {
				newFilePage.setFileName(mbeanPage.getCreatedType().getElementName() + "-service.xml");
				
				IPath fullPath = newFilePage.getContainerFullPath();

				IPath newPath = new Path(fullPath.segment(0));

				// BLOCKING on eclipse bug 153135
//				IPath newPath = new Path(fullPath.segment(0)).append("META-INF");
//				newFilePage.setContainerFullPath(newPath);
				
				IFile newFile = newFilePage.createNewFile();
				createStubServiceDescriptor(newFile);
				ASDebug.p("create it", this);
			} else {
				ASDebug.p("DO NOT CREATE", this);
			}
		} catch( Throwable jme) {
			jme.printStackTrace();
		}
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		sel = selection;
	}

	public void addPages() {
		interfacePage = new MBeanInterfacePage();
		mbeanPage = new MBeanPage();
		addPage(interfacePage);
		addPage(mbeanPage);
		interfacePage.init(sel);
		mbeanPage.init(sel);
		
		
		newFilePage = new NewFilePage(sel);
		newFilePage.defaultName = "blah";
		Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
		String ext = "xml";
		newFilePage.defaultFileExtension = "."+ext; //$NON-NLS-1$
		newFilePage.filterExtensions = new String[] {"*.xml"};
		addPage(newFilePage);
		
	}
	
	private class MBeanInterfacePage extends NewTypeWizardPage {

		private StringDialogField fMBeanNameDialogField;
		private StringDialogField fMBeanInterfaceNameDialogField;
		private IStatus fMBeanNameStatus;
		
		
		public MBeanInterfacePage() {
			super(false, "New MBean Interface");
			
			setTitle("New MBean Interface"); 
			setDescription("Create the interface for the MBean"); 

			
			fMBeanNameDialogField= new StringDialogField();
			fMBeanNameDialogField.setDialogFieldListener(new MBeanPage1DialogFieldAdapter());
			fMBeanNameDialogField.setLabelText("MBean Name"); 

			fMBeanInterfaceNameDialogField= new StringDialogField();
			fMBeanInterfaceNameDialogField.setDialogFieldListener(new MBeanPage1DialogFieldAdapter());
			fMBeanInterfaceNameDialogField.setLabelText("Interface Name"); 
		}

		private class MBeanPage1DialogFieldAdapter implements IDialogFieldListener {
			public void dialogFieldChanged(DialogField field) {
				String fieldName = null;
				if( field == fMBeanNameDialogField ) {
					String txt = fMBeanNameDialogField.getText();
					fMBeanInterfaceNameDialogField.getTextControl(null).setText(txt + "MBean");
					fieldName = INTERFACE_NAME;
					fTypeNameStatus = typeNameChanged(getTypeName());
					fMBeanNameStatus = typeNameChanged(fMBeanNameDialogField.getText());
				}
				handleFieldChanged(fieldName);
				
			}
		}
		public void createControl(Composite parent) {
		      this.initializeDialogUnits(parent);

		      Composite composite = new Composite(parent, SWT.NONE);

		      int nColumns = 4;

		      GridLayout layout = new GridLayout();
		      layout.numColumns = nColumns;
		      composite.setLayout(layout);

		      this.createContainerControls(composite, nColumns);
		      this.createPackageControls(composite, nColumns);
		      this.createSeparator(composite, nColumns);
		      this.createMBeanNameControls(composite, nColumns);
		      this.createTypeNameControls(composite, nColumns);
		      this.createSuperClassControls(composite, nColumns);
		      this.createSuperInterfacesControls(composite, nColumns);
		      
		      fMBeanInterfaceNameDialogField.getTextControl(null).setEditable(false);

		      
		      this.setControl(composite);
		} 
		
		public String getTypeName() {
			return fMBeanInterfaceNameDialogField.getText();
		}

		
		protected void createTypeNameControls(Composite composite, int nColumns) {
			fMBeanInterfaceNameDialogField.doFillIntoGrid(composite, nColumns - 1);
			DialogField.createEmptySpace(composite);
			
			Text text= fMBeanInterfaceNameDialogField.getTextControl(null);
			LayoutUtil.setWidthHint(text, getMaxFieldWidth());
			TextFieldNavigationHandler.install(text);
		}

		
		protected void createMBeanNameControls(Composite composite, int nColumns) {
			fMBeanNameDialogField.doFillIntoGrid(composite, nColumns - 1);
			DialogField.createEmptySpace(composite);
			
			Text text= fMBeanNameDialogField.getTextControl(null);
			LayoutUtil.setWidthHint(text, getMaxFieldWidth());
			TextFieldNavigationHandler.install(text);
		}


		public void init(IStructuredSelection selection) {
			IJavaElement jelem= getInitialJavaElement(selection);
			initContainerPage(jelem);
			initTypePage(jelem);
		}
		
		protected void handleFieldChanged(String fieldName) {
			super.handleFieldChanged(fieldName);
			
			if( fieldName == INTERFACE_NAME) {
				mbeanPage.setMBeanName(fMBeanNameDialogField.getText());
			}
			doStatusUpdate();
		}

		// ------ validation --------
		private void doStatusUpdate() {
			// status of all used components
			IStatus[] status= new IStatus[] {
				fContainerStatus,
				fTypeNameStatus,
				fMBeanNameStatus,
				fSuperClassStatus,
				fSuperInterfacesStatus
			};
			
			// the mode severe status will be displayed and the OK button enabled/disabled.
			updateStatus(status);
		}

		
		protected IStatus typeNameChanged(String typeNameWithParameters) {
			StatusInfo status= new StatusInfo();
			IType currType = null;


			// must not be empty
			if (typeNameWithParameters.length() == 0) {
				status.setError(NewWizardMessages.NewTypeWizardPage_error_EnterTypeName); 
				return status;
			}
			
			String typeName= getTypeNameWithoutParameters(typeNameWithParameters);
			if (typeName.indexOf('.') != -1) {
				status.setError(NewWizardMessages.NewTypeWizardPage_error_QualifiedName); 
				return status;
			}
			IStatus val= JavaConventions.validateJavaTypeName(typeName);
			if (val.getSeverity() == IStatus.ERROR) {
				status.setError(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidTypeName, val.getMessage())); 
				return status;
			} else if (val.getSeverity() == IStatus.WARNING) {
				status.setWarning(Messages.format(NewWizardMessages.NewTypeWizardPage_warning_TypeNameDiscouraged, val.getMessage())); 
				// continue checking
			}		

			// must not exist
			IPackageFragment pack= getPackageFragment();
			if (pack != null) {
				ICompilationUnit cu= pack.getCompilationUnit(getCompilationUnitName(typeName));
				currType= cu.getType(typeName);
				IResource resource= cu.getResource();

				if (resource.exists()) {
					status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeNameExists + "(" + typeName + ")"); 
					return status;
				}
				URI location= resource.getLocationURI();
				if (location != null) {
					try {
						IFileStore store= EFS.getStore(location);
						if (store.fetchInfo().exists()) {
							status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeNameExistsDifferentCase); 
							return status;
						}
					} catch (CoreException e) {
						status.setError(Messages.format(
							NewWizardMessages.NewTypeWizardPage_error_uri_location_unkown, 
							Resources.getLocationString(resource)));
					}
				}
			}
			
			if (typeNameWithParameters != typeName) {
				IPackageFragmentRoot root= getPackageFragmentRoot();
				if (root != null) {
					if (!JavaModelUtil.is50OrHigher(root.getJavaProject())) {
						status.setError(NewWizardMessages.NewTypeWizardPage_error_TypeParameters); 
						return status;
					}
					String typeDeclaration= "class " + typeNameWithParameters + " {}"; //$NON-NLS-1$//$NON-NLS-2$
					ASTParser parser= ASTParser.newParser(AST.JLS3);
					parser.setSource(typeDeclaration.toCharArray());
					parser.setProject(root.getJavaProject());
					CompilationUnit compilationUnit= (CompilationUnit) parser.createAST(null);
					IProblem[] problems= compilationUnit.getProblems();
					if (problems.length > 0) {
						status.setError(Messages.format(NewWizardMessages.NewTypeWizardPage_error_InvalidTypeName, problems[0].getMessage())); 
						return status;
					}
				}
			}
			return status;
		}
		
		private String getTypeNameWithoutParameters(String typeNameWithParameters) {
			int angleBracketOffset= typeNameWithParameters.indexOf('<');
			if (angleBracketOffset == -1) {
				return typeNameWithParameters;
			} else {
				return typeNameWithParameters.substring(0, angleBracketOffset);
			}
		}

	
	}
	
	private class MBeanPage extends NewTypeWizardPage {

		private StringDialogField fMBeanNameDialogField;
		private SelectionButtonDialogField fDescriptorDialogField;
		public MBeanPage() {
			super(true, "MBean Class");
			
			setTitle("New MBean Class"); 
			setDescription("Create the concrete MBean Class"); 

			MBeanPage2DialogFieldAdapter adapter = new MBeanPage2DialogFieldAdapter();
			fMBeanNameDialogField= new StringDialogField();
			fMBeanNameDialogField.setDialogFieldListener(adapter);
			fMBeanNameDialogField.setLabelText("MBean Name"); 
			
			fDescriptorDialogField = new SelectionButtonDialogField(SWT.CHECK);
			fDescriptorDialogField.setDialogFieldListener(adapter);
			fDescriptorDialogField.setLabelText("Create -service.xml file?");
		}

		private class MBeanPage2DialogFieldAdapter implements IDialogFieldListener {
			public void dialogFieldChanged(DialogField field) {
				//ASDebug.p("field changed: " + field, this);
			}
		}

		public void createControl(Composite parent) {
		      this.initializeDialogUnits(parent);

		      Composite composite = new Composite(parent, SWT.NONE);

		      int nColumns = 4;

		      GridLayout layout = new GridLayout();
		      layout.numColumns = nColumns;
		      composite.setLayout(layout);

		      this.createContainerControls(composite, nColumns);
		      this.createPackageControls(composite, nColumns);
		      this.createSeparator(composite, nColumns);
		      this.createMBeanTypeNameControls(composite, nColumns);
		      this.createSuperClassControls(composite, nColumns);
		      this.createSuperInterfacesControls(composite, nColumns);
		      this.createSeparator(composite, nColumns);
		      this.createDescriptorControls(composite, nColumns);
		      
		      fMBeanNameDialogField.getTextControl(null).setEditable(false);

		      this.setControl(composite);
		}
		
		public List getSuperInterfaces() {
			List interfaces = super.getSuperInterfaces();
			if( interfacePage.getCreatedType() != null ) {
				IType t = interfacePage.getCreatedType();
				interfaces.add(t.getFullyQualifiedName());
			}
			return interfaces;
		}

		protected void createDescriptorControls(Composite composite, int nColumns) {
			fDescriptorDialogField.doFillIntoGrid(composite, nColumns);
		}
		
		protected void createMBeanTypeNameControls(Composite composite, int nColumns) {
			fMBeanNameDialogField.doFillIntoGrid(composite, nColumns - 1);
			DialogField.createEmptySpace(composite);
			
			Text text= fMBeanNameDialogField.getTextControl(null);
			LayoutUtil.setWidthHint(text, getMaxFieldWidth());
			TextFieldNavigationHandler.install(text);
		}

		
		public String getTypeName() {
			return fMBeanNameDialogField.getText();
		}

		public void setMBeanName(String s) {
			fMBeanNameDialogField.getTextControl(null).setText(s);
		}
		
		public void init(IStructuredSelection selection) {
			IJavaElement jelem= getInitialJavaElement(selection);
			initContainerPage(jelem);
			initTypePage(jelem);
		}
		
		protected void createTypeMembers(IType type, ImportsManager imports, IProgressMonitor monitor) throws CoreException {
			createInheritedMethods(type, true, true, imports, new SubProgressMonitor(monitor, 1));
		}

		public boolean shouldCreateDescriptor() {
			return fDescriptorDialogField.isSelected();
		}
		
		public IWizardPage getNextPage() {
			return null;
		}
	}
	
	
	private void createStubServiceDescriptor(IFile newFile) throws Exception {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			String charSet = getUserPreferredCharset();

			PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charSet));
			writer.println("<?xml version=\"1.0\" encoding=\"" + charSet + "\"?>"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.println("<server>");
			writer.println("\t<mbean code=\"" + mbeanPage.getCreatedType().getFullyQualifiedName() + "\" name=\"your.domain:key=value\">");
			writer.println("\t</mbean>");
			writer.println("</server>");
			writer.flush();
			outputStream.close();

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			newFile.setContents(inputStream, true, true, null);
			inputStream.close();
	}
	
	private String getUserPreferredCharset() {
		Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
		String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
		return charSet;
	}


}
