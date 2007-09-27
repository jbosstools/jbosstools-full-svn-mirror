/*
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.ejb3.wizards.ui.wizards;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;
import org.eclipse.ui.help.WorkbenchHelp;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewMessageDrivenBeanWizardPage extends NewTypeWizardPage {
	private IStructuredSelection selection;
	
	public NewMessageDrivenBeanWizardPage ()
	{
		super (true, "New Message Driven Bean Page");
	}
	
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite composite= new Composite(parent, SWT.NONE);
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		composite.setLayout(layout);
		
		createContainerControls(composite, nColumns);	
		createEnclosingTypeControls(composite, nColumns);
		createSeparator(composite, nColumns);
		
		createPackageControls(composite, nColumns);
		createTypeNameControls(composite, nColumns);
		
		createSeparator(composite, nColumns);
		
		createModifierControls(composite, nColumns);
			
		createSuperClassControls(composite, nColumns);
		createSuperInterfacesControls(composite, nColumns);
		
		createSeparator(composite, nColumns);
		
		setControl(composite);
		
		
		Dialog.applyDialogFont(composite);
		WorkbenchHelp.setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
		
		ArrayList superInterfaces = new ArrayList();
		superInterfaces.add("javax.jms.MessageListener");
		setSuperInterfaces(superInterfaces, true);
	}
	
	public void init(IStructuredSelection selection) {
		this.selection = selection;
		
		IJavaElement element = getInitialJavaElement(selection);
		initContainerPage(element);
		initTypePage(element);
	}

	public void createType(IProgressMonitor monitor) throws CoreException,
		InterruptedException
	{
		super.createType(monitor);
		IType createdBeanType = getCreatedType();
		
		ICompilationUnit beanUnit = createdBeanType.getCompilationUnit();
		
		Document doc = new Document(beanUnit.getSource());
		
		ASTParser c = ASTParser.newParser(AST.JLS3);
		c.setSource(beanUnit.getSource().toCharArray());
		c.setResolveBindings(true);
		CompilationUnit beanAstUnit = (CompilationUnit) c.createAST(null);
		AST ast = beanAstUnit.getAST();
		beanAstUnit.recordModifications();
		
		ImportDeclaration importDecl = ast.newImportDeclaration();
		importDecl.setOnDemand(false);
		importDecl.setName(ast.newName(new String[] {"javax", "ejb", "MessageDriven"}));
		beanAstUnit.imports().add(importDecl);
		
		MarkerAnnotation sessionAnnotation = ast.newMarkerAnnotation();
		sessionAnnotation.setTypeName(ast.newSimpleName("MessageDriven"));
		TypeDeclaration type = (TypeDeclaration) beanAstUnit.types().get(0);
		type.modifiers().add(sessionAnnotation);
		
		TextEdit edit = beanAstUnit.rewrite(doc, null);
		try {
			UndoEdit undo = edit.apply(doc);
			String source = doc.get();
			beanUnit.getBuffer().setContents(source);
			beanUnit.getBuffer().save(monitor, true);
			
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	protected void createTypeMembers(IType newType, ImportsManager imports, IProgressMonitor monitor)
		throws CoreException
	{
		super.createTypeMembers(newType, imports, monitor);
		
		createInheritedMethods(newType, false, true, imports, new SubProgressMonitor(monitor, 1));
	}
}
