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
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Pointcut;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.PointcutPreviewDialog2;

/**
 * @author Rob Stryker
 */

/**
 * This is a composite class used in the PointcutPreviewDialog.
 * It consists of a group, containing 5 rows for possible 
 * pointcut expressions. 
 * 
 * @author Rob Stryker
 */
public class PointcutPreviewAssistComposite extends Composite
{
   /* The various pointcut expressions */
   public static final String NAMED_POINTCUT = "Named Pointcut";

   public static final String EXECUTION_METHOD = "execution(method)";

   public static final String EXECUTION_CONSTRUCTOR = "execution(constructor)";

   public static final String GET_FIELD = "get(field)";

   public static final String SET_FIELD = "set(field)";

   public static final String FIELD_FIELD = "field(field)";

   public static final String ALL_TYPE = "all(type)";

   public static final String CALL_METHOD = "call(method)";

   public static final String CALL_CONSTRUCTOR = "call(constructor)";

   public static final String WITHIN_TYPE = "within(type)";

   public static final String WITHINCODE_METHOD = "withincode(method)";

   public static final String WITHINCODE_CONSTRUCTOR = "withincode(constructor)";

   public static final String HAS_METHOD = "has(method)";

   public static final String HAS_CONSTRUCTOR = "has(constructor)";

   public static final String HASFIELD_FIELD = "hasfield(field)";

   public static final String CONJ_AND = "AND";

   public static final String CONJ_OR = "OR";

   //	public static final String CONJ_BLANK = "DONE";

   public static final String TYPE = "__TYPE__";

   public static final String METHOD = "__METHOD__";

   public static final String CONSTRUCTOR = "__CONSTRUCTOR__";

   public static final String FIELD = "__FIELD__";

   public static final String CLASS = "Class";

   public static final String ANNOTATION = "Annotation";

   public static final String INSTANCE_OF = "Instanceof";

   public static final String TYPEDEF = "Typedef";

   public static final String NAME = "Name";

   private static final int maxRows = 20;

   public static HashMap pointcutToComposite;

   public static HashMap pointcutExplanations;

   private String currentPointcutComposite;

   private Text explanationLabel;

   private Button addRowButton, removeRowButton;

   private final PointcutPreviewDialog dialog;

   private ScrolledComposite sc;

   private ArrayList rowList;

   private int lastVisible;

   public PointcutPreviewAssistComposite(Composite parent, PointcutPreviewDialog dialog)
   {

      super(parent, SWT.NONE);
      this.dialog = dialog;

      pointcutToComposite = new HashMap();
      pointcutExplanations = new HashMap();
      this.rowList = new ArrayList();
      this.lastVisible = -1;
      setLayout(new FormLayout());

      // Setting up the group
      Group group = new Group(this, SWT.NONE);
      group.setText("Build your expression");
      FormData groupData = new FormData();
      groupData.top = new FormAttachment(0, 0);
      groupData.bottom = new FormAttachment(0, 250);
      groupData.left = new FormAttachment(0, 0);
      groupData.right = new FormAttachment(100, 0);
      group.setLayoutData(groupData);
      group.setLayout(new FillLayout());

      /** TEST **/
      sc = new ScrolledComposite(group, SWT.H_SCROLL | SWT.V_SCROLL);

      // Create a child composite to hold the controls
      Composite child = new Composite(sc, SWT.NONE);
      child.setLayout(new FormLayout());

      // Create the buttons
      ExpressionRowComposite last = null;
      for (int i = 0; i < maxRows; i++)
      {
         last = createRow(child, last);
         rowList.add(last);
         if (i > 3)
         {
            last.setVisible(false);
         }
         else
         {
            int locY = last.getLocation().y;
            int sY = last.getSize().y;
            sc.setMinHeight(locY + sY);
            lastVisible = i;
         }
      }

      /*
       * // Set the absolute size of the child child.setSize(400, 400);
       */
      // Set the child as the scrolled content of the ScrolledComposite
      sc.setContent(child);

      // Expand both horizontally and vertically
      sc.setExpandHorizontal(true);
      sc.setExpandVertical(true);

      addRowButton = new Button(this, SWT.PUSH);
      FormData addData = new FormData();
      addData.left = new FormAttachment(0, 0);
      addData.top = new FormAttachment(group, 5);
      addData.bottom = new FormAttachment(100, 0);
      addRowButton.setLayoutData(addData);
      addRowButton.setText("Add New Row");
      addRowButton.setVisible(true);

      addRowButton.addSelectionListener(new SelectionListener()
      {

         public void widgetSelected(SelectionEvent e)
         {
            lastVisible++;
            boolean anyLeft = (lastVisible != PointcutPreviewAssistComposite.maxRows - 1);
            addRowButton.setEnabled(anyLeft);
            removeRowButton.setEnabled(true);

            ExpressionRowComposite tmp = (ExpressionRowComposite) rowList.get(lastVisible);
            tmp.setVisible(true);
            int locY = tmp.getLocation().y;
            int sY = tmp.getSize().y;
            sc.setMinHeight(locY + sY);
            updatePointcutTextBox();
         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

      });

      removeRowButton = new Button(this, SWT.PUSH);
      FormData removeData = new FormData();
      removeData.left = new FormAttachment(addRowButton, 5);
      removeData.top = new FormAttachment(group, 5);
      removeData.bottom = new FormAttachment(100, 0);
      removeRowButton.setLayoutData(removeData);
      removeRowButton.setText("Remove Last Row");
      removeRowButton.setVisible(true);

      removeRowButton.addSelectionListener(new SelectionListener()
      {

         public void widgetSelected(SelectionEvent e)
         {
            removeRowButton.setEnabled(lastVisible != 0);
            addRowButton.setEnabled(true);

            ExpressionRowComposite tmp = (ExpressionRowComposite) rowList.get(lastVisible);
            tmp.setVisible(false);
            int locY = tmp.getLocation().y;
            sc.setMinHeight(locY);
            lastVisible--;
            updatePointcutTextBox();
         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
            widgetSelected(e);
         }

      });

      fillPointcutCompositeMap();
      fillPointcutExplanationMap();
   }

   public int getLastVisible()
   {
      return lastVisible;
   }

   protected ExpressionRowComposite createRow(Composite parent, Control top)
   {
      ExpressionRowComposite c = new ExpressionRowComposite(parent, SWT.NONE, this, top);

      FormData cData = new FormData();
      if (top == null)
      {
         cData.top = new FormAttachment(0, 5);
      }
      else
      {
         cData.top = new FormAttachment(top, 5);
      }

      cData.left = new FormAttachment(0, 5);
      cData.right = new FormAttachment(100, -5);
      c.setLayoutData(cData);

      return c;
   }

   /**
    * Maps an item in the command combo to a required parameter type.
    * (Ex: execute(method) -> method,   get(field) -> field
    */
   protected void fillPointcutCompositeMap()
   {
      pointcutToComposite.put(EXECUTION_METHOD, METHOD);
      pointcutToComposite.put(EXECUTION_CONSTRUCTOR, CONSTRUCTOR);
      pointcutToComposite.put(GET_FIELD, FIELD);
      pointcutToComposite.put(SET_FIELD, FIELD);
      pointcutToComposite.put(FIELD_FIELD, FIELD);
      pointcutToComposite.put(ALL_TYPE, TYPE);
      pointcutToComposite.put(CALL_METHOD, METHOD);
      pointcutToComposite.put(CALL_CONSTRUCTOR, CONSTRUCTOR);
      pointcutToComposite.put(WITHIN_TYPE, TYPE);
      pointcutToComposite.put(WITHINCODE_METHOD, METHOD);
      pointcutToComposite.put(WITHINCODE_CONSTRUCTOR, CONSTRUCTOR);
      pointcutToComposite.put(HAS_METHOD, METHOD);
      pointcutToComposite.put(HAS_CONSTRUCTOR, CONSTRUCTOR);
      pointcutToComposite.put(HASFIELD_FIELD, FIELD);
   }

   /**
    * Maps an item in the commandcombo to a paragraph explaining its
    * usage from the manual.
    * 
    * TODO: Was used... perhaps use again?
    */
   private void fillPointcutExplanationMap()
   {
      pointcutExplanations
            .put(
                  EXECUTION_METHOD,
                  "execution is used to specify that you want an interception to happen whenever a method or constructor is called. The the first example of matches anytime a method is called, the second matches a constructor. System classes cannot be used within execution  expressions because it is impossible to instrument them.");
      pointcutExplanations
            .put(
                  EXECUTION_CONSTRUCTOR,
                  "execution is used to specify that you want an interception to happen whenever a method or constructor is called. The the first example of matches anytime a method is called, the second matches a constructor. System classes cannot be used within execution  expressions because it is impossible to instrument them.");
      pointcutExplanations
            .put(GET_FIELD,
                  "get is used to specify that you want an interception to happen when a specific field is accessed for a read.");
      pointcutExplanations
            .put(SET_FIELD,
                  "set is used to specify that you want an interception to happen when a specific field is accessed for a write.");
      pointcutExplanations
            .put(
                  FIELD_FIELD,
                  "field is used to specify that you want an interception to happen when a specific field is accessed for a read or a write.");
      pointcutExplanations
            .put(
                  ALL_TYPE,
                  "all is used to specify any constructor, method or field of a particular class will be intercepted. If an annotation is used, it matches the member's annotation, not the class's annotation.");
      pointcutExplanations
            .put(
                  CALL_METHOD,
                  "call is used to specify any constructor or method that you want intercepted. It is different than execution in that the interception happens at the caller side of things and the caller information is available within the Invocation object. call  can be used to intercept System classes because the bytecode weaving happens within the callers bytecode.");
      pointcutExplanations
            .put(
                  CALL_CONSTRUCTOR,
                  "call is used to specify any constructor or method that you want intercepted. It is different than execution in that the interception happens at the caller side of things and the caller information is available within the Invocation object. call  can be used to intercept System classes because the bytecode weaving happens within the callers bytecode.");
      pointcutExplanations.put(WITHIN_TYPE,
            "within matches any joinpoint (method or constructor call) within any code within a particular call.");
      pointcutExplanations.put(WITHINCODE_METHOD,
            "withincode matches any joinpoint (method or constructor call) within a particular method or constructor.");
      pointcutExplanations.put(WITHINCODE_CONSTRUCTOR,
            "withincode matches any joinpoint (method or constructor call) within a particular method or constructor.");
      pointcutExplanations
            .put(
                  HAS_METHOD,
                  "has is an additional requirement for matching. If a joinpoint is matched, its class must also have a constructor or method that matches the has expression.");
      pointcutExplanations
            .put(
                  HAS_CONSTRUCTOR,
                  "has is an additional requirement for matching. If a joinpoint is matched, its class must also have a constructor or method that matches the has expression.");
      pointcutExplanations
            .put(
                  HASFIELD_FIELD,
                  "hasfield is an additional requirement for matching. If a joinpoint is matched, its class must also have a field that matches the hasfield expression.");
   }

   /**
    * Allows one of the five rows to update the pointcut textbox when modified.
    */
   public void updatePointcutTextBox()
   {
      String ret = "";
      for (Iterator i = rowList.iterator(); i.hasNext();)
      {
         ExpressionRowComposite rowComp = (ExpressionRowComposite) i.next();
         ret += rowComp.toString();
      }
      dialog.setPointcutText(ret);
   }

   /**
    * One row representing one expression in a pointcut expression.
    * It consists of a conjunction (AND, OR), an expression command, 
    * and a button that will load the second dialog and allow for 
    * further assistance with creating a pointcut. 
    */
   protected static class ExpressionRowComposite extends Composite
   {

      protected Button modify;

      protected Combo commandCombo, conjunction, namedPointcutCombo;

      protected Text expression;

      protected boolean isConjunctive;

      protected PointcutPreviewAssistComposite comp;

      protected Control top;

      public ExpressionRowComposite(Composite parent, int style, PointcutPreviewAssistComposite comp, Control top)
      {
         super(parent, style);
         setLayout(new FormLayout());
         this.isConjunctive = top == null ? false : true;
         this.comp = comp;
         this.top = top;

         if (this.isConjunctive)
            conjunction = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
         commandCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
         namedPointcutCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
         modify = new Button(this, SWT.NONE);
         modify.setEnabled(false);
         expression = new Text(this, SWT.LEFT | SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
         expression.setEditable(false);

         loadLayoutData();
         fillCombos();
         addListeners();

         modify.setText("Modify");
      }

      /**
       * THis is all layout stuff.
       */
      protected void loadLayoutData()
      {
         int conjunctionEnds = 65;

         if (isConjunctive)
         {
            FormData conjunctionData = new FormData();
            conjunctionData.left = new FormAttachment(0, 5);
            conjunctionData.top = new FormAttachment(0, 5);
            conjunctionData.right = new FormAttachment(0, conjunctionEnds);
            conjunction.setLayoutData(conjunctionData);
         }

         FormData commandComboData = new FormData();
         commandComboData.left = new FormAttachment(0, conjunctionEnds + 10);
         commandComboData.right = new FormAttachment(modify, -5);
         commandComboData.top = new FormAttachment(0, 5);
         commandCombo.setLayoutData(commandComboData);

         FormData modifyData = new FormData();
         modifyData.right = new FormAttachment(100, -5);
         modifyData.left = new FormAttachment(100, -65);
         modifyData.top = new FormAttachment(0, 5);
         modify.setLayoutData(modifyData);

         FormData expressionData = new FormData();
         expressionData.left = new FormAttachment(0, conjunctionEnds + 10);
         expressionData.right = new FormAttachment(100, -5);
         expressionData.top = new FormAttachment(commandCombo, 5);
         expression.setLayoutData(expressionData);

         FormData namedPointcutData = new FormData();
         namedPointcutData.left = new FormAttachment(0, conjunctionEnds + 10);
         namedPointcutData.right = new FormAttachment(100, -5);
         namedPointcutData.top = new FormAttachment(commandCombo, 5);
         namedPointcutCombo.setLayoutData(namedPointcutData);
         namedPointcutCombo.setVisible(false);
      }

      /**
       * Simply fill a command combo
       */
      protected void fillCombos()
      {
         // Fill the command combo
         commandCombo.add(NAMED_POINTCUT);
         commandCombo.add(EXECUTION_METHOD);
         commandCombo.add(EXECUTION_CONSTRUCTOR);
         commandCombo.add(GET_FIELD);
         commandCombo.add(SET_FIELD);
         commandCombo.add(FIELD_FIELD);
         commandCombo.add(ALL_TYPE);
         commandCombo.add(CALL_METHOD);
         commandCombo.add(CALL_CONSTRUCTOR);
         commandCombo.add(WITHIN_TYPE);
         commandCombo.add(WITHINCODE_METHOD);
         commandCombo.add(WITHINCODE_CONSTRUCTOR);
         commandCombo.add(HAS_METHOD);
         commandCombo.add(HAS_CONSTRUCTOR);
         commandCombo.add(HASFIELD_FIELD);

         // Fill the named pointcut combo
         IJavaProject proj = AopCorePlugin.getCurrentJavaProject();
         AopDescriptor descriptor = AopCorePlugin.getDefault().getDefaultDescriptor(proj);

         List pointcuts = AopModelUtils.getPointcutsFromAop(descriptor.getAop());
         for (Iterator i = pointcuts.iterator(); i.hasNext();)
         {
            Pointcut pointcut = (Pointcut) i.next();
            namedPointcutCombo.add("[" + pointcut.getName() + "]  " + pointcut.getExpr());

         }

         if (isConjunctive)
         {
            //				conjunction.add(CONJ_BLANK);
            conjunction.add(CONJ_AND);
            conjunction.add(CONJ_OR);
            conjunction.select(0);
         }
      }

      /**
       * Clicking on the modify button will open a new dialog 
       * to access further controls for pointcut expression creation.
       */
      protected void addListeners()
      {
         modify.addSelectionListener(new SelectionListener()
         {

            public void widgetSelected(SelectionEvent e)
            {
               if (commandCombo.getSelectionIndex() == -1)
                  return;
               String s = commandCombo.getItem(commandCombo.getSelectionIndex());
               String compositeType = (String) pointcutToComposite.get(s);

               PointcutPreviewDialog2 dialog = new PointcutPreviewDialog2(expression.getText(), getShell(),
                     compositeType);
               int status = dialog.open();
               if (status == Window.OK)
               {
                  expression.setText(dialog.getString());
                  comp.updatePointcutTextBox();
               }
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
            }

         });
         commandCombo.addSelectionListener(new SelectionListener()
         {

            public void widgetSelected(SelectionEvent e)
            {
               Object selected = commandCombo.getItem(commandCombo.getSelectionIndex());

               // expression and modify are only 
               // shown when command is not named_pointcut
               expression.setVisible(!selected.equals(NAMED_POINTCUT));
               modify.setEnabled(!selected.equals(NAMED_POINTCUT));

               // pointcutcombo, the opposite
               namedPointcutCombo.setVisible(selected.equals(NAMED_POINTCUT));
               comp.updatePointcutTextBox();
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
            }

         });
         namedPointcutCombo.addSelectionListener(new SelectionListener()
         {

            public void widgetSelected(SelectionEvent e)
            {
               comp.updatePointcutTextBox();
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
               widgetSelected(e);
            }

         });

         if (isConjunctive)
         {
            conjunction.addSelectionListener(new SelectionListener()
            {

               public void widgetSelected(SelectionEvent e)
               {
                  int index = conjunction.getSelectionIndex();
                  if (index == -1)
                     return;

                  comp.updatePointcutTextBox();
               }

               public void widgetDefaultSelected(SelectionEvent e)
               {
                  widgetSelected(e);
               }

            });

         }
      }

      /**
       * Turns the controls into a usable portion of a pointcut expression.
       */
      public String toString()
      {
         if (!isVisible())
            return "";

         if (commandCombo.getSelectionIndex() == -1)
            return "";

         String conj = isConjunctive ? " " + conjunction.getItem(conjunction.getSelectionIndex()) + " " : "";
         String command = commandCombo.getItem(commandCombo.getSelectionIndex());
         int openParenIndex = command.indexOf("(");
         if (openParenIndex > -1)
         {
            String rawCommand = command.substring(0, command.indexOf("("));
            return conj + rawCommand + "(" + expression.getText() + ")";
         }
         else
         {
            // we dont have a command here... just a named pointcut
            int pointcutComboIndex = namedPointcutCombo.getSelectionIndex();
            if (pointcutComboIndex == -1)
               return "";
            String s = (String) namedPointcutCombo.getItem(pointcutComboIndex);

            return conj + s.substring(1, s.indexOf(']'));
         }
      }

   }

}
