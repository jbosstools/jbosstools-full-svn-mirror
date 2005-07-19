/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICodeFormatter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.jdom.DOMFactory;
import org.eclipse.jdt.core.jdom.IDOMCompilationUnit;
import org.eclipse.jdt.core.jdom.IDOMField;
import org.eclipse.jdt.core.jdom.IDOMImport;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jdt.core.jdom.IDOMType;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.ITemplates;
import org.jboss.ide.eclipse.jdt.ui.wizards.util.FieldsUtil;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class DOMClassWizardPage extends ClassWizardPage implements IGenerationEngine
{
   private final DOMFactory factory = new DOMFactory();
   private final static String PAGE_NAME = DOMClassWizardPage.class.getName();


   /**
    *Constructor for the DOMClassWizardPage object
    *
    * @param isClass  Description of the Parameter
    * @param page     Description of the Parameter
    */
   public DOMClassWizardPage(boolean isClass, String page)
   {
      super(isClass, page);
   }


   /**
    * Description of the Method
    *
    * @param engine   Description of the Parameter
    * @param monitor  Description of the Parameter
    */
   public void generate(IGenerationEngine engine, IProgressMonitor monitor)
   {
      try
      {
         engine.generate(getCreatedType(), monitor);
      }
      catch (JavaModelException jme)
      {
         AbstractPlugin.logError("Unable to generate type", jme);//$NON-NLS-1$
      }
   }


   /**
    * Description of the Method
    *
    * @param type                    Description of the Parameter
    * @param monitor                 Description of the Parameter
    * @exception JavaModelException  Description of the Exception
    */
   public void generate(IType type, IProgressMonitor monitor)
      throws JavaModelException
   {
      ICompilationUnit cu = type.getCompilationUnit();
      if (cu.isWorkingCopy())
      {
         cu = (ICompilationUnit) cu.getOriginalElement();
      }

      IDOMCompilationUnit compUnit = getDOMFactory().createCompilationUnit(cu.getSource(), type.getElementName());
      IDOMType dType = (IDOMType) compUnit.getChild(type.getElementName());

      // Children may add their own content
      this.addContent(type, compUnit, dType, monitor);

      String newContents = compUnit.getContents();

      // Formatting
      ICodeFormatter formatter = ToolFactory.createCodeFormatter();
      newContents = formatter.format(newContents, 0, null, null);

      try
      {
         cu.getBuffer().setContents(newContents);
      }
      catch (Exception e)
      {
         AbstractPlugin.logError("Unable to set compilation unit content", e);//$NON-NLS-1$
      }

      // Save the buffer to the file.
      cu.save(monitor, true);
   }


   /**
    * Gets the createConstructors attribute of the DOMClassWizardPage object
    *
    * @return   The createConstructors value
    */
   public boolean isCreateConstructors()
   {
      return this.fMethodStubsButtons.isSelected(0);
   }


   /**
    * Gets the createInherited attribute of the DOMClassWizardPage object
    *
    * @return   The createInherited value
    */
   public boolean isCreateInherited()
   {
      return this.fMethodStubsButtons.isSelected(1);
   }


   /**
    * Adds a feature to the Content attribute of the DOMClassWizardPage object
    *
    * @param type      The feature to be added to the Content attribute
    * @param compUnit  The feature to be added to the Content attribute
    * @param dType     The feature to be added to the Content attribute
    * @param monitor   The feature to be added to the Content attribute
    */
   protected abstract void addContent(IType type, IDOMCompilationUnit compUnit, IDOMType dType, IProgressMonitor monitor);


   /**
    * Adds a feature to the Import attribute of the DOMClassWizardPage object
    *
    * @param unit  The feature to be added to the Import attribute
    * @param name  The feature to be added to the Import attribute
    */
   protected void addImport(IDOMCompilationUnit unit, String name)
   {
      IDOMNode node = unit.getFirstChild();
      IDOMNode previous = null;
      List imports = new ArrayList();

      // Get the first import node
      while (node != null && (node.getNodeType() != IDOMNode.IMPORT))
      {
         previous = node;
         node = node.getNextNode();
      }

      if (node != null)
      {
         // Get the last import node
         do
         {
            imports.add(node);
            node = node.getNextNode();
         } while (node != null && (node.getNodeType() == IDOMNode.IMPORT));
      }
      else
      {
         node = previous;
      }

      // Create a new import node
      IDOMImport newImportNode = getDOMFactory().createImport("import " + name + ";\n");//$NON-NLS-1$ //$NON-NLS-2$
      boolean found = false;
      for (int i = 0; i < imports.size(); i++)
      {
         IDOMImport importNode = (IDOMImport) imports.get(i);
         if (newImportNode.getName().equals(importNode.getName()))
         {
            found = true;
         }
      }
      if (!found)
      {
         node.insertSibling(newImportNode);
      }
   }


   /**
    * Description of the Method
    *
    * @param templates  Description of the Parameter
    * @param key        Description of the Parameter
    * @return           Description of the Return Value
    */
   protected IDOMField buildField(ITemplates templates, String key)
   {
      IDOMField field = getDOMFactory().createField(templates.getString(key + ".decl"));//$NON-NLS-1$
      field.setComment(templates.getString(key + ".comment"));//$NON-NLS-1$
      return field;
   }


   /**
    * Description of the Method
    *
    * @param templates   Description of the Parameter
    * @param key         Description of the Parameter
    * @param parameters  Description of the Parameter
    * @return            Description of the Return Value
    */
   protected IDOMField buildField(ITemplates templates, String key, Object[] parameters)
   {
      IDOMField field = getDOMFactory().createField(templates.getString(key + ".decl", parameters));//$NON-NLS-1$
      field.setComment(templates.getString(key + ".comment", parameters));//$NON-NLS-1$
      return field;
   }


   /**
    * Description of the Method
    *
    * @param templates  Description of the Parameter
    * @param key        Description of the Parameter
    * @return           Description of the Return Value
    */
   protected IDOMMethod buildMethod(ITemplates templates, String key)
   {
      IDOMMethod method = getDOMFactory().createMethod(templates.getString(key + ".body"));//$NON-NLS-1$
      method.setComment(templates.getString(key + ".comment"));//$NON-NLS-1$
      return method;
   }


   /**
    * Description of the Method
    *
    * @param templates   Description of the Parameter
    * @param key         Description of the Parameter
    * @param parameters  Description of the Parameter
    * @return            Description of the Return Value
    */
   protected IDOMMethod buildMethod(ITemplates templates, String key, Object[] parameters)
   {
      IDOMMethod method = getDOMFactory().createMethod(templates.getString(key + ".body", parameters));//$NON-NLS-1$
      method.setComment(templates.getString(key + ".comment", parameters));//$NON-NLS-1$
      return method;
   }


   /** Description of the Method */
   protected void createContent()
   {
      // Constructors and inherited must be left as is
      String[] buttonNames = new String[]{NewWizardMessages.NewClassWizardPage_methods_constructors, //$NON-NLS-1$ //$NON-NLS-2$
      NewWizardMessages.NewClassWizardPage_methods_inherited//$NON-NLS-1$
      };
      this.fMethodStubsButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK, buttonNames, 1);
      this.fMethodStubsButtons.setLabelText(NewWizardMessages.NewClassWizardPage_methods_label);//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createControls(Composite composite, int nColumns)
   {
      this.createContainerControls(composite, nColumns);
      this.createPackageControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createTypeNameControls(composite, nColumns);
      this.createModifierControls(composite, nColumns);
      this.createSuperClassControls(composite, nColumns);
      this.createSuperInterfacesControls(composite, nColumns);
      this.createSeparator(composite, nColumns);
      this.createMethodStubSelectionControls(composite, nColumns);
   }


   /**
    * Description of the Method
    *
    * @param composite  Description of the Parameter
    * @param nColumns   Description of the Parameter
    */
   protected void createMethodStubSelectionControls(Composite composite, int nColumns)
   {
      FieldsUtil.createSelectionButtonDialogFieldGroupControls(this.fMethodStubsButtons, composite, nColumns);
//      Control labelControl = fMethodStubsButtons.getLabelControl(composite);
//      LayoutUtil.setHorizontalSpan(labelControl, nColumns);
//
//      DialogField.createEmptySpace(composite);
//
//      Control buttonGroup = fMethodStubsButtons.getSelectionButtonsGroup(composite);
//      LayoutUtil.setHorizontalSpan(buttonGroup, nColumns - 1);
   }


   /**
    * Description of the Method
    *
    * @param newType            Description of the Parameter
    * @param imports            Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   protected void createTypeMembers(IType newType, ImportsManager imports, IProgressMonitor monitor)
      throws CoreException
   {
      this.createInheritedMethods(newType, isCreateConstructors(), isCreateInherited(), imports, monitor);
   }


   /**
    * Gets the dOMFactory attribute of the DOMClassWizardPage object
    *
    * @return   The dOMFactory value
    */
   protected DOMFactory getDOMFactory()
   {
      return this.factory;
   }


   /** Description of the Method */
   protected void initContent()
   {
      // Constructor is checked
      this.fMethodStubsButtons.setSelection(0, true);
      // Inherited is checked
      this.fMethodStubsButtons.setSelection(1, true);
   }
}
