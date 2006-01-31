/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.Geometry;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.model.AdvisedCollector;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModel;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.ui.AopUiPlugin;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces.PointcutPreviewAssistComposite;

/**
 * @author Marshall
 */
public class PointcutPreviewDialog extends Dialog
{

   protected IJavaProject project;

   protected Text expressionText, nameText;

   protected Label messageImage, messageLabel, nameErrorImage, nameErrorLabel, expressionLabel, nameLabel;

   protected TableViewer expressionPreview;

   protected Button previewButton, wizardButton;

   protected String expressionString, name;

   protected ArrayList advisable;

   protected PointcutPreviewAssistComposite assistComposite;

   protected Composite main, errorComposite, nameErrorComposite;

   protected boolean named;

   public static final int NAMED = 0;

   public static final int UNNAMED = 1;

   protected boolean advanced;

   private ProgressBar previewProgress;

   // These are some booleans to determine if OK and PREVIEW should be enabled.
   protected boolean nameIsValid;

   protected boolean expressionCompiles;

   protected boolean successfulPreview; // not in use?

   public static final int PREVIEW_ID = -3000;

   public static final int WIZARD_ID = -3001;

   public PointcutPreviewDialog(String name, String pointcut, Shell shell, IJavaProject project, int named)
   {
      super(shell);
      this.named = named == PointcutPreviewDialog.NAMED ? true : false;
      this.project = project;
      this.expressionString = pointcut;
      this.name = name;
      this.advisable = new ArrayList();
      this.advanced = true;
      if (!this.named)
      {
         this.nameIsValid = true;
      }
      this.successfulPreview = false;
   }

   public String getExpression()
   {
      return expressionString;
   }

   public String getName()
   {
      return name;
   }

   protected void addText()
   {
      getShell().setText("Edit Pointcut...");
      expressionLabel.setText("Pointcut:");
   }

   protected Control createDialogArea(Composite parent)
   {

      GridData mainData = new GridData();
      mainData.horizontalAlignment = GridData.FILL;
      mainData.grabExcessHorizontalSpace = true;
      main = new Composite(parent, SWT.NONE);
      main.setLayoutData(mainData);

      main.setLayout(new FormLayout());

      if (this.named)
      {
         nameLabel = new Label(main, SWT.NONE);
         nameText = new Text(main, SWT.BORDER);
         nameErrorComposite = new Composite(main, SWT.NONE);
         nameErrorImage = new Label(nameErrorComposite, SWT.NONE);
         nameErrorLabel = new Label(nameErrorComposite, SWT.NONE);
      }

      expressionLabel = new Label(main, SWT.NONE);
      expressionText = new Text(main, SWT.BORDER);

      assistComposite = createOurAssistComposite();
      previewProgress = new ProgressBar(main, SWT.SMOOTH);
      errorComposite = new Composite(main, SWT.NONE);
      messageImage = new Label(errorComposite, SWT.NONE);
      messageLabel = new Label(errorComposite, SWT.NONE);

      setLayoutDatas();
      addListeners();
      addText();

      return main;
   }

   protected PointcutPreviewAssistComposite createOurAssistComposite()
   {
      return new PointcutPreviewAssistComposite(main, this);
   }

   private void setLayoutDatas()
   {
      assistComposite.setVisible(false);
      previewProgress.setVisible(false);
      messageImage.setVisible(false);
      messageLabel.setText("                                                             "
            + "                                                                        ");
      if (expressionString != null)
         expressionText.setText(expressionString);
      if (name != null && name != "")
      {
         nameText.setText(name);
         nameText.setEnabled(false);
         nameIsValid = true;
         nameErrorComposite.setVisible(false);
      }

      if (!this.named)
      {

         FormData pointcutLabelData = new FormData();
         pointcutLabelData.left = new FormAttachment(0, 10);
         pointcutLabelData.top = new FormAttachment(0, 10);
         //pointcutLabelData.right = new FormAttachment(0,60);
         expressionLabel.setLayoutData(pointcutLabelData);

         FormData pointcutTextData = new FormData();
         pointcutTextData.top = new FormAttachment(0, 5);
         pointcutTextData.left = new FormAttachment(expressionLabel, 10);
         pointcutTextData.right = new FormAttachment(100, -10);
         expressionText.setLayoutData(pointcutTextData);
      }
      else
      {
         nameErrorImage.setVisible(false);
         nameErrorLabel.setVisible(false);
         nameErrorImage
               .setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
         nameErrorLabel.setText("Name already in use");
         nameLabel.setText("Name:");

         FormData nameLabelData = new FormData();
         nameLabelData.left = new FormAttachment(0, 10);
         nameLabelData.top = new FormAttachment(0, 10);
         nameLabelData.right = new FormAttachment(0, 60);
         nameLabel.setLayoutData(nameLabelData);

         FormData nameTextData = new FormData();
         nameTextData.top = new FormAttachment(0, 5);
         nameTextData.left = new FormAttachment(nameLabel, 10);
         nameTextData.right = new FormAttachment(nameLabel, 250);
         nameText.setLayoutData(nameTextData);

         FormData nameErrorCompositeData = new FormData();
         nameErrorCompositeData.left = new FormAttachment(nameText, 10);
         nameErrorCompositeData.right = new FormAttachment(100, -10);
         nameErrorCompositeData.bottom = new FormAttachment(expressionText, -5);
         nameErrorComposite.setLayoutData(nameErrorCompositeData);

         nameErrorComposite.setLayout(new FormLayout());

         FormData nameErrorLabelData = new FormData();
         nameErrorLabelData.left = new FormAttachment(nameErrorImage, 5);
         nameErrorLabelData.top = new FormAttachment(0, 1);
         nameErrorLabel.setLayoutData(nameErrorLabelData);

         FormData nameErrorImageData = new FormData();
         nameErrorImageData.left = new FormAttachment(0, 5);
         nameErrorImage.setLayoutData(nameErrorImageData);
         // pointcut stuff is a bit lower now.

         FormData pointcutLabelData = new FormData();
         pointcutLabelData.left = new FormAttachment(0, 10);
         pointcutLabelData.top = new FormAttachment(nameLabel, 10);
         pointcutLabelData.right = new FormAttachment(0, 60);
         expressionLabel.setLayoutData(pointcutLabelData);

         FormData pointcutTextData = new FormData();
         pointcutTextData.top = new FormAttachment(nameLabel, 5);
         pointcutTextData.left = new FormAttachment(expressionLabel, 10);
         pointcutTextData.right = new FormAttachment(100, -10);
         expressionText.setLayoutData(pointcutTextData);

      }

      // Everything else should be independent.

      FormData assistCompositeData = new FormData();
      assistCompositeData.top = new FormAttachment(expressionLabel, 10);
      assistCompositeData.bottom = new FormAttachment(expressionLabel, 12);
      assistCompositeData.left = new FormAttachment(0, 5);
      assistCompositeData.right = new FormAttachment(100, -10);
      assistComposite.setLayoutData(assistCompositeData);

      FormData previewProgressData = new FormData();
      previewProgressData.left = new FormAttachment(0, 10);
      previewProgressData.right = new FormAttachment(100, -5);
      previewProgressData.top = new FormAttachment(assistComposite, 10);

      previewProgress.setLayoutData(previewProgressData);

      FormData errorCompositeData = new FormData();
      errorCompositeData.left = new FormAttachment(0, 10);
      errorCompositeData.right = new FormAttachment(100, -5);
      errorCompositeData.top = new FormAttachment(previewProgress, 5);
      errorCompositeData.bottom = new FormAttachment(previewProgress, 50);

      errorComposite.setLayoutData(errorCompositeData);

      errorComposite.setLayout(new GridLayout(2, false));

      messageImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
      messageLabel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

      FormData pointcutPreviewData = new FormData();
      pointcutPreviewData.left = new FormAttachment(0, 5);
      pointcutPreviewData.top = new FormAttachment(errorComposite, 10);
      pointcutPreviewData.right = new FormAttachment(100, -5);
      expressionPreview = new TableViewer(main, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
      expressionPreview.getTable().setEnabled(false);

      AdvisedLabelProvider provider = new AdvisedLabelProvider();
      expressionPreview.setLabelProvider(provider);
      expressionPreview.setContentProvider(provider);
      expressionPreview.getTable().setLayoutData(pointcutPreviewData);
      expressionPreview.setInput(advisable);
   }

   protected void addListeners()
   {
      expressionText.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            expressionString = expressionText.getText();
            String localName = named ? name : expressionString;

            if (expressionString == "")
            {
               expressionCompiles = false;
               redrawButtons();
               return;
            }

            try
            {
               PointcutExpression expr = new PointcutExpression(localName, expressionString);
               expressionCompiles = true;
               redrawButtons();
               clearError();
            }
            catch (Throwable thr)
            {
               // Most will be parse errors (simple exceptions.)
               // Some will be a TokenMgrError.
               expressionCompiles = false;
               redrawButtons();
               showError(thr);
            }
         }
      });

      if (this.named)
      {
         nameText.addModifyListener(new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               name = nameText.getText();
               Pointcut pointcut = AspectManager.instance().getPointcut(name);
               nameErrorImage.setVisible(pointcut != null);
               nameErrorLabel.setVisible(pointcut != null);
               nameErrorComposite.redraw();

               if (pointcut == null || name == "")
               {
                  nameIsValid = pointcut == null;
               }
               redrawButtons();
            }
         });
      }

   }

   protected void redrawButtons()
   {
      if (expressionCompiles == false || nameIsValid == false)
      {
         getButton(IDialogConstants.OK_ID).setEnabled(false);
         previewButton.setEnabled(false);
         //		} 
         //		else if( successfulPreview == false ) {
         //			getButton(IDialogConstants.OK_ID).setEnabled(false);
         //			previewButton.setEnabled(true);			
      }
      else
      {
         getButton(IDialogConstants.OK_ID).setEnabled(true);
         previewButton.setEnabled(true);
      }
      //		System.out.println("expression compiles: " + expressionCompiles + ", name is valid: " + nameIsValid);

   }

   protected void createButtonsForButtonBar(Composite parent)
   {
      super.createButtonsForButtonBar(parent);

      previewButton = createButton(parent, PREVIEW_ID, "Preview >>", false);
      previewButton.setText("Preview >>");
      setButtonLayoutData(previewButton);

      wizardButton = createButton(parent, WIZARD_ID, "Wizard", false);
      wizardButton.setText("Open Wizard");
      setButtonLayoutData(wizardButton);
      addSelectionListenersForButtonBar();
   }

   protected void addSelectionListenersForButtonBar()
   {
      previewButton.addSelectionListener(new SelectionListener()
      {
         boolean on = false;

         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            previewPressed();
         }
      });
      wizardButton.addSelectionListener(new SelectionListener()
      {
         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

         public void widgetSelected(SelectionEvent e)
         {
            morePressed();
         }
      });
   }

   protected void morePressed()
   {
      boolean closing = assistComposite.isVisible();
      //int height = assistComposite.getBounds().height;

      FormData assistCompositeData = new FormData();
      assistCompositeData.top = new FormAttachment(expressionLabel, 10);
      assistCompositeData.left = new FormAttachment(0, 5);
      assistCompositeData.right = new FormAttachment(100, -10);
      if (closing)
      {
         assistCompositeData.bottom = new FormAttachment(expressionLabel, 12);
         wizardButton.setText("Open Wizard");
      }
      else
      {
         assistCompositeData.bottom = new FormAttachment(expressionLabel, 300);
         wizardButton.setText("Close Wizard");
      }
      assistComposite.setLayoutData(assistCompositeData);

      assistComposite.setVisible(!assistComposite.isVisible()); // invert visiblilty
      resizeMyself();
   }

   protected void resizeMyself()
   {

      Point size = getInitialSize();
      Point location = getShell().getLocation();

      getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
   }

   protected Point getInitialLocation(Point initialSize)
   {
      Composite parent = getShell().getParent();

      Monitor monitor = getShell().getDisplay().getPrimaryMonitor();
      if (parent != null)
      {
         monitor = parent.getMonitor();
      }

      Rectangle monitorBounds = monitor.getClientArea();
      Point centerPoint;
      if (parent != null)
      {
         centerPoint = Geometry.centerPoint(parent.getBounds());
      }
      else
      {
         centerPoint = Geometry.centerPoint(monitorBounds);
      }

      return new Point(centerPoint.x - (initialSize.x / 2), 100);
   }

   protected Point getInitialSize()
   {
      Point p = super.getInitialSize();
      p.x = 451;
      return p;
   }

   protected void previewPressed()
   {
      try
      {
         //clearError();
         expressionString = expressionText.getText();

         JDTPointcutExpression expression = new JDTPointcutExpression(new PointcutExpression(null, expressionString));
         //AopModel.instance().findAllAdvised(AopModel.instance().getRegisteredTypes(), expression, new PreviewCollector(), new NullProgressMonitor());
         AopModel.instance().findAllAdvised(AopModel.instance().getRegisteredTypesAsITypes(), expression,
               new PreviewCollector("pointcut"), new NullProgressMonitor());
      }
      catch (ParseException e)
      {
         showError(e);
      }
      catch (RuntimeException e)
      {
         showError(e);
      }
      catch (Exception e)
      {
         showError(e);
      }
      catch (Throwable t)
      {
         showError(t);
      }
   }

   protected class AdvisedLabelProvider extends LabelProvider implements IStructuredContentProvider
   {
      private JavaUILabelProvider javaUILabelProviderDelegate;

      public AdvisedLabelProvider()
      {
         javaUILabelProviderDelegate = new JavaUILabelProvider();
      }

      public Image getImage(Object element)
      {
         IAopAdvised advised = (IAopAdvised) element;
         return javaUILabelProviderDelegate.getImage(advised.getAdvisedElement());
      }

      public String getText(Object element)
      {
         IAopAdvised advised = (IAopAdvised) element;

         return AopUiPlugin.getDefault().getTreeLabel(advised.getAdvisedElement());
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public Object[] getElements(Object inputElement)
      {
         if (inputElement instanceof List)
         {
            return ((List) inputElement).toArray();
         }
         else
            return new Object[0];
      }
   }

   protected void clearError()
   {
      messageImage.setVisible(false);
      messageLabel.setVisible(false);
   }

   protected void showError(Throwable error)
   {
      messageImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
      messageImage.setVisible(true);
      messageImage.redraw();
      //error.printStackTrace();
      showMessage(error.getMessage(), false);
   }

   protected void showSuccess(String message)
   {
      messageImage.setImage(Display.getDefault().getSystemImage(SWT.ICON_INFORMATION));
      messageImage.redraw();
      showMessage(message, true);
   }

   protected void showMessage(String message, boolean success)
   {

      messageImage.setVisible(true);
      messageLabel.setVisible(true);

      messageLabel.setText(message != null ? message : "");
      messageLabel.setToolTipText(message != null ? message : "");
      getButton(OK).setEnabled(success);
      previewProgress.setVisible(false);

      messageLabel.getParent().getParent().redraw();
      expressionPreview.setInput(advisable);
      resizeMyself();
   }

   protected class PreviewCollector extends AdvisedCollector
   {
      private String category;

      public PreviewCollector(String category)
      {
         this.category = category;
      }

      public void beginTask(String typeName, final int numberOfAdvised)
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               previewProgress.setVisible(true);
               previewProgress.setMinimum(0);
               previewProgress.setMaximum(numberOfAdvised);
               previewProgress.setSelection(0);
               previewProgress.setSize(previewProgress.getSize().x, 10);
               advisable.clear();

            }
         });
      }

      public void worked(final int work)
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               previewProgress.setSelection(previewProgress.getSelection() + work);
            }
         });
      }

      public void collectAdvised(final IAopAdvised advised)
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               advisable.add(advised);
            }
         });
      }

      public void done()
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               expressionPreview.setInput(advisable);
               showSuccess("Acceptable " + category + " expression advising " + advisable.size() + " elements");
            }
         });
      }

      public void handleException(final Exception e)
      {
         Display.getDefault().asyncExec(new Runnable()
         {
            public void run()
            {
               showError(e);
            }
         });
      }
   }

   public void setPointcutText(String expression)
   {
      expressionText.setText(expression);
   }
}
