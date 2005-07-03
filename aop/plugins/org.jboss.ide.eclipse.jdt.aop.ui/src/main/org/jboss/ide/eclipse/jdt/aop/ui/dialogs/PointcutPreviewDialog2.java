package org.jboss.ide.eclipse.jdt.aop.ui.dialogs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.AopDescriptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Typedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.AopModelUtils;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces.AopUICompletionProcessor;
import org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces.PointcutPreviewAssistComposite;

/**
 * This class represents the second pointcut preview dialog.
 * Perhaps it's poorly named. It provides the controls 
 * to build a constructor pattern, a method pattern,
 * a field pattern, or a type pattern.
 * 
 * @author Rob Stryker
 */
public class PointcutPreviewDialog2 extends Dialog {

	
	protected String type;
	protected PointcutComposite content;
	protected String retVal;
	protected String expression;
	
	public PointcutPreviewDialog2 (String expression, Shell shell, String type)
	{
		super(shell);
		this.type = type;
		this.expression = expression;
	}


	
	protected Control createDialogArea (Composite parent)
	{
		getShell().setText("Edit Pointcut...");

		GridData mainData = new GridData();
		mainData.horizontalAlignment = GridData.FILL;
 		mainData.grabExcessHorizontalSpace = true;
		Composite main = new Composite (parent, SWT.NONE);
		main.setLayoutData(mainData);
		
		main.setLayout( new FormLayout());
		content = createComposite(main);
		FormData contentData = new FormData();
		contentData.left = new FormAttachment(0,5);
		contentData.right = new FormAttachment(0,450);
		contentData.top = new FormAttachment(0,5);
		if( content != null ) 
			((Composite)content).setLayoutData(contentData);

		return main;
	}
	
	
	protected PointcutComposite createComposite(Composite parent) {
		if( type.equals(PointcutPreviewAssistComposite.TYPE))
			//		type
			return new TypeComposite(parent, expression);
		
		if( type.equals(PointcutPreviewAssistComposite.CONSTRUCTOR))
			// constructor
			return new MemberComposite(parent, true, true, expression);
		
		if( type.equals(PointcutPreviewAssistComposite.METHOD)) 
			// method
			return new MemberComposite(parent, true, false, expression);
		
		if( type.equals(PointcutPreviewAssistComposite.FIELD)) 
			// field
			return new MemberComposite(parent, false, false, expression);

		return null;
	}

	/**
	 * Save the textbox so that after ok is clicked, a value can be obtained.
	 */
	public boolean close() {
		retVal = content.toString1();
		return super.close();
	}

	public String getString() {
		return retVal;
	}

	public interface PointcutComposite {
		// needed because for some reason, changing toString breaks it (?)
		public String toString1();
	}
	
	/**
	 * Used for parameters and exceptions, allows multiple classes to be selected
	 */
	public static class MultipleTypeComposite extends Composite implements PointcutComposite {
		private TypeComposite typeComposite;
		private Text collected;
		private Button add, clear, back;
		
		public MultipleTypeComposite( Composite parent, String label, String expression ) {
			this(parent, label, false, expression);
		}

		public MultipleTypeComposite( Composite parent, String label, boolean classOnly, String expression ) {
			super(parent, SWT.NONE);
			setLayout(new FormLayout());
			createMembers(label, classOnly);
			setMemberData();
			setVisible(true);
			addListeners();
			collected.setText(expression == null ? "" : expression);
		}
		
		private void createMembers(String label, boolean classOnly) {
			typeComposite = new TypeComposite(this, true, false, label, classOnly, null);
			add = new Button(this, SWT.NONE);
			collected = new Text(this, SWT.LEFT | SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
			clear = new Button(this, SWT.NONE);
			back = new Button(this, SWT.NONE);
			add.setText("Add");
			clear.setText("Clear");
			back.setText("Back");
		}
		
		private void setMemberData() {

			
			FormData typeData = new FormData();
			typeData.top = new FormAttachment(0,5);
			typeData.left = new FormAttachment(0,0);
			typeData.right = new FormAttachment(add,-5);
			typeComposite.setLayoutData(typeData);
			
			FormData addData = new FormData();
			addData.right = new FormAttachment(100,-5);
			addData.bottom = new FormAttachment(clear,-5);
			addData.left = new FormAttachment(100,-40);
			add.setLayoutData(addData);
			
			FormData collectedData = new FormData();
			collectedData.left = new FormAttachment(0,5);
			collectedData.top = new FormAttachment(typeComposite,10);
			collectedData.right = new FormAttachment(back,-5);
			collected.setLayoutData(collectedData);
			
			FormData backData = new FormData();
			backData.left = new FormAttachment(100,-80);
			backData.right = new FormAttachment(clear,-5);
			backData.top = new FormAttachment(typeComposite,10);
			back.setLayoutData(backData);

			FormData clearData = new FormData();
			clearData.left = new FormAttachment(100,-40);
			clearData.right = new FormAttachment(100,-5);
			clearData.top = new FormAttachment(typeComposite,10);
			clear.setLayoutData(clearData);
			
		}
		
		public void addListeners() {
			add.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if( collected.getText() == "") {
						collected.setText(typeComposite.toString1());
					} else {
						collected.setText(collected.getText() + ", "
								+ typeComposite.toString1());
					}
					typeComposite.getPattern().setText("");
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
		
			clear.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					collected.setText("");
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});

			back.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					int last = collected.getText().lastIndexOf(",");
					if( last == -1 ) {
						collected.setText("");
					} else {
						collected.setText(collected.getText().substring(0, last));
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});

		}
		
		public String toString1() {
			return collected.getText();
		}
	}
	
	public static class TypeComposite extends Composite implements PointcutComposite {
		Composite c;
		private Combo patternType;
		private Combo typedefs;
		private boolean typedefsLoaded;
		private Text pattern;
		private String type, text;

		private Label categoryLabel;
		private AopUICompletionProcessor processor;
		
		/**
		 * Used by a composite containing ONLY a typecomposite.
		 */
		public TypeComposite(Composite parent, String expression ) {
			this(parent, false, false, "Type Pattern", expression);
		}

		/**
		 * Used by membertype for everything it seems.
		 */
		public TypeComposite(Composite parent, boolean enableBaseTypes, 
				boolean enableVoid, String labelString, String expression) {
			this(parent, enableBaseTypes, enableVoid, labelString, false, expression );
		}
		
		
		public TypeComposite(Composite parent, boolean enableBaseTypes, 
				boolean enableVoid, String labelString, boolean classOnly, String expression ) {
			super(parent, SWT.NONE);
			c = this;
			c.setLayout( new FormLayout());
			typedefsLoaded = false;
			
	
			
			categoryLabel = new Label(c, SWT.NONE );
			categoryLabel.setText(labelString);
			FormData labelData = new FormData();
			labelData.left = new FormAttachment(0,5);
			labelData.top = new FormAttachment(0,0);
			categoryLabel.setLayoutData(labelData);
	
			patternType = new Combo(c, SWT.READ_ONLY);
			pattern = new Text(c, SWT.LEFT | SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
			typedefs = new Combo(c, SWT.READ_ONLY);
			typedefs.setVisible(false);

			
			setWidgetDatas();
			fillCombos( classOnly );
			
			processor = new AopUICompletionProcessor(pattern); 

	
			patternType.addSelectionListener(new SelectionListener() {
	
				public void widgetSelected(SelectionEvent e) {
					String patternTypeString = patternType.getItem(patternType.getSelectionIndex());
					if( patternTypeString.equals(PointcutPreviewAssistComposite.TYPEDEF)) {
						if( !typedefsLoaded ) {
							lazyLoadTypedefs();
						}
						pattern.setVisible(false);
						typedefs.setVisible(true);
					} else {
						typedefs.setVisible(false);
						pattern.setVisible(true);
						updateProcessor(patternTypeString);
					}
				}
	
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				
			});
	
			patternType.select(0);
			updateProcessor(patternType.getItem(patternType.getSelectionIndex()));
			
			
			if( expression != null && expression != "") {
				loadExpressionIntoWidgets(expression.trim());
			}
		}
		
		private void updateProcessor(String pulldownString) {
			if( pulldownString.equals(PointcutPreviewAssistComposite.CLASS)) 
				processor.setType(AopUICompletionProcessor.CLAZZ);
			if( pulldownString.equals(PointcutPreviewAssistComposite.ANNOTATION)) 
				processor.setType(AopUICompletionProcessor.ANNOTATION);
			if( pulldownString.equals(PointcutPreviewAssistComposite.TYPEDEF)) 
				processor.setType(AopUICompletionProcessor.TYPEDEF);
			if( pulldownString.equals(PointcutPreviewAssistComposite.INSTANCE_OF)) 
				processor.setType(AopUICompletionProcessor.INSTANCEOF);
		}

		private void setWidgetDatas() {
			FormData comboData = new FormData();
			comboData.top = new FormAttachment(categoryLabel, 5);
			comboData.left = new FormAttachment(0,5);
			patternType.setLayoutData(comboData);
			
			pattern.setEditable(true);
			FormData patternData = new FormData();
			patternData.left = new FormAttachment(patternType,5);
			patternData.top = new FormAttachment(categoryLabel,5);
			patternData.right = new FormAttachment(100,-5);
			pattern.setLayoutData(patternData);

			FormData typedefData = new FormData();
			typedefData.left = new FormAttachment(patternType,5);
			typedefData.top = new FormAttachment(categoryLabel,5);
			typedefData.right = new FormAttachment(100,-5);
			typedefs.setLayoutData(typedefData);
			typedefs.setVisible(true);
		}
		
		private void fillCombos(boolean classOnly ) {
			patternType.add(PointcutPreviewAssistComposite.CLASS);
			patternType.add(PointcutPreviewAssistComposite.INSTANCE_OF);
			if( !classOnly ) { 
				patternType.add(PointcutPreviewAssistComposite.ANNOTATION);
				patternType.add(PointcutPreviewAssistComposite.TYPEDEF);
			}
		}
		
		private void lazyLoadTypedefs() {
			typedefsLoaded = true;
			IJavaProject proj = AopCorePlugin.getCurrentJavaProject();
			AopDescriptor aop = AopCorePlugin.getDefault().getDefaultDescriptor(proj);
			List list = AopModelUtils.getTypedefsFromAop(aop.getAop());
			for( Iterator i = list.iterator(); i.hasNext(); ) {
				Typedef def = (Typedef)i.next();
				typedefs.add("[" + def.getName() + "] " + def.getExpr());
			}

		}
		
		private void loadExpressionIntoWidgets(String expression) {
			if( expression.indexOf(" ") != -1) 
				expression = expression.substring(0, expression.indexOf(" "));
			
			
			if( expression.startsWith("@")) {
				type = PointcutPreviewAssistComposite.ANNOTATION;
				text = expression.substring(1);
			} else if( expression.startsWith("$instanceof")) {
				type = PointcutPreviewAssistComposite.INSTANCE_OF;
				text =  expression.substring(expression.indexOf("(") + 1, expression.length()-1);
			} else if( expression.startsWith("$typedef")) {
				type = PointcutPreviewAssistComposite.TYPEDEF;
				text =  expression.substring(expression.indexOf("(") + 1, expression.length()-1);
			} else if( expression.trim().indexOf(" ") == -1 ){
				type = PointcutPreviewAssistComposite.CLASS;
				text = expression;
			} else {
				return;
			}
			
			pattern.setText(text);
			String [] tmp = patternType.getItems();
			for( int i = 0; i < tmp.length; i++ ) {
				if( patternType.getItem(i).equals(type)) {
					patternType.select(i);
				}
			}

			
		}
		
		public Combo getCombo() {
			return patternType;
		}
		public Text getPattern() {
			return pattern;
		}
		public String toString1() {
			if( patternType.getItem(patternType.getSelectionIndex())
					.equals(PointcutPreviewAssistComposite.CLASS)) {
				return pattern.getText();
			}
			if( patternType.getItem(patternType.getSelectionIndex())
					.equals(PointcutPreviewAssistComposite.ANNOTATION)) {
				return "@" + pattern.getText();
			}
			if( patternType.getItem(patternType.getSelectionIndex())
					.equals(PointcutPreviewAssistComposite.INSTANCE_OF)) {
				return "$instanceof{" + pattern.getText() + "}";
			}
			if( patternType.getItem(patternType.getSelectionIndex())
					.equals(PointcutPreviewAssistComposite.TYPEDEF)) {
				int index = typedefs.getSelectionIndex();
				if( index == -1 ) return "";
				String s = typedefs.getItem(index);
				String typedefName = s.substring(1, s.indexOf(']'));
				return "$typedef{" + typedefName + "}";
			}
			// TODO: Throw exception?
			return "";
		}

	}

	/**
	 * Contains no content completion for patterns but does allow
	 * for completion for annotations.
	 */

	public static class NameComposite extends Composite implements PointcutComposite {
		private Combo combo1;
		private final Text annotPattern;
		private final Text namePattern;
		private final boolean constructor;

		public NameComposite(Composite parent, boolean cons, String labelString, String expression) {
			super(parent, SWT.NONE);
			setLayout( new FormLayout());
			this.constructor = cons;
	
			
			Label categoryLabel = new Label(this, SWT.NONE );
			categoryLabel.setText(labelString);
			FormData labelData = new FormData();
			labelData.left = new FormAttachment(0,5);
			labelData.top = new FormAttachment(0,0);
			categoryLabel.setLayoutData(labelData);
	
			final Combo combo = new Combo(this, SWT.READ_ONLY);
			combo1 = combo;
			combo.add(PointcutPreviewAssistComposite.NAME);
			combo.add(PointcutPreviewAssistComposite.ANNOTATION);
			
			FormData comboData = new FormData();
			comboData.top = new FormAttachment(categoryLabel, 5);
			comboData.left = new FormAttachment(0,5);
			combo.setLayoutData(comboData);
			
			annotPattern = new Text(this, SWT.LEFT | SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
			annotPattern.setEditable(true);
			FormData annotPatternData = new FormData();
			annotPatternData.left = new FormAttachment(combo,5);
			annotPatternData.top = new FormAttachment(categoryLabel,5);
			annotPatternData.right = new FormAttachment(100,-5);
			annotPattern.setLayoutData(annotPatternData);
	
			AopUICompletionProcessor processor = 
				new AopUICompletionProcessor(annotPattern);
			processor.setType(AopUICompletionProcessor.ANNOTATION);
			
			
			namePattern = new Text(this, SWT.LEFT | SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
			namePattern.setEditable(!constructor);
			FormData namePatternData = new FormData();
			namePatternData.left = new FormAttachment(combo,5);
			namePatternData.top = new FormAttachment(categoryLabel,5);
			namePatternData.right = new FormAttachment(100,-5);
			namePattern.setLayoutData(namePatternData);
			
	
			combo.addSelectionListener(new SelectionListener() {
	
				public void widgetSelected(SelectionEvent e) {
					if(combo.getItem(combo.getSelectionIndex())
							.equals(PointcutPreviewAssistComposite.NAME)) {
						annotPattern.setVisible(false);
						namePattern.setVisible(true);
					} else {
						namePattern.setVisible(false);
						annotPattern.setVisible(true);
					}
				}
	
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				
			});
	
			if( constructor ) {
				namePattern.setText("new");
				namePattern.setEditable(false);				
			}
			annotPattern.setVisible(false);

			combo.select(0);
			if( expression != null ) {
				if( expression.startsWith("@")) {
					annotPattern.setText(expression.substring(1));
					annotPattern.setVisible(true);
					namePattern.setVisible(false);
					combo.select(1);
				} else {
					namePattern.setText(expression);
				}
			}
		}
		
		public Combo getCombo() {
			return combo1;
		}
		public Text getPattern() {
			if( combo1.getItem(combo1.getSelectionIndex())
							.equals(PointcutPreviewAssistComposite.NAME)) {
				return namePattern;
			} else {
				return annotPattern;
			}
		}

		public String toString1() {
			if( combo1.getItem(combo1.getSelectionIndex())
					.equals(PointcutPreviewAssistComposite.ANNOTATION)) {
				return "@" + getPattern().getText();
			}
			return getPattern().getText();
		}

	}

	
	
//	/* Class to be used by any pointcut that requires a Type pattern */
//	public static class TypePatternModifyListener implements ModifyListener {
//		private IPackageFragment fragment = null;
//		private PointcutPreviewTypeCompletionProcessor completionProcessor;
//		private Text pattern;
//		private String text;
//		
//		private String typePulldown;
//		private boolean allowsStars;
//
//		public TypePatternModifyListener(Text pattern) {
//			this(pattern,false,false);
//		}
//		
//		public TypePatternModifyListener( Text pattern, boolean enableBaseTypes, boolean enableVoid) {
//			this(pattern, enableBaseTypes, enableVoid, true);
//		}
//		
//		public TypePatternModifyListener( Text pattern, boolean enableBaseTypes, 
//				boolean enableVoid, boolean addStars) {
//			this.pattern = pattern;
//			completionProcessor = new PointcutPreviewTypeCompletionProcessor(
//					new PointcutPreviewTypeCompletionProcessor.TypeCompletionRequestor(enableBaseTypes,enableVoid), addStars);
//			ControlContentAssistHelper.createTextContentAssistant(pattern, completionProcessor);
//			completionProcessor.setTextField(pattern);
//			try {
//				IPackageFragment[] fragments = AopCorePlugin.getCurrentJavaProject().getPackageFragments();
//				fragment = fragments[0];
//			} catch( Exception e ) {
//			}			
//		}
//		
//		
//
//		public void modifyText(ModifyEvent e) {
//			boolean changed = true;
//			if( pattern.getText().indexOf("@") != -1) {
//				int position = pattern.getCaretPosition();
//				pattern.setText(text);
//				pattern.setSelection(position-1,position-1);
//				changed = false;
//			}
//			if( !allowsStars && pattern.getText().indexOf("*") != -1) {
//				int position = pattern.getCaretPosition();
//				pattern.setText(text);
//				pattern.setSelection(position-1,position-1);
//				changed = false;
//			}
//			
//			if( changed ) {
//				text = pattern.getText();
//			}
//			completionProcessor.setExtendsCompletionContext( fragment );
//		}
//		
//		public void setTypePulldown( String s ) {
//			typePulldown = s;
//			if( s.equals(PointcutPreviewAssistComposite.CLASS )) {
//				allowsStars = true;;
//			} else {
//				allowsStars = false;
//				pattern.setText(pattern.getText().replace('*', '\0'));
//			}
//
//			completionProcessor.setType(s);
//		}
//		
//		
//	}
//	
	
	/**
	 * The class that creates controls to assist in producing field, method,
	 * or constructor pointcuts. Will make use of other classes, such as 
	 * MultipleTypeComposite, TypeComposite, and NameComposite
	 */
	public static class MemberComposite extends Composite implements PointcutComposite {
		private Combo permissionCombo;
		private Combo staticCombo;
		private TypeComposite type;
		private TypeComposite className;
		private MultipleTypeComposite params;
		private MultipleTypeComposite exceptions;
		private NameComposite memberName;
		private Label permissionLabel, staticLabel;
		

		private String typeExpr, classNameExpr, paramsExpr, 
			exceptionExpr, memberNameExpr, permissions, staticity;

		/**
		 * Are we any type of method? (a constructor is a type of method).
		 * !isMethod implies a field.
		 */
		private boolean isMethod;
		
		/**
		 * Are we a constructor? 
		 * if !isConstructor && isMethod, then we have a regular method.
		 * if !isConstructor && !isMethod, then we have a field. 
		 */
		private boolean isConstructor; 
		
		
		
		public MemberComposite(Composite parent, boolean isMethod,
				boolean isConstructor, String expression) {
			super(parent, SWT.NONE);
			this.isConstructor = isConstructor;
			this.isMethod = isMethod;

			try {
				parseFields(expression);
			} catch( Exception e ) {
				
			}

			setLayout( new FormLayout());
			createLabels();
			createAndFillCombos();
			createOtherLayoutItems();
			setMemberLayoutData();
		}
		
		private void createLabels() {
			permissionLabel = new Label(this, SWT.NONE);
			permissionLabel.setText("Access");
			staticLabel = new Label(this, SWT.NONE);
			staticLabel.setText("Static");
		}

		private void createAndFillCombos() {
			permissionCombo = new Combo(this, SWT.READ_ONLY);
			permissionCombo.add("any");
			permissionCombo.add("public ");
			permissionCombo.add("!public ");
			permissionCombo.add("private ");
			permissionCombo.add("!private ");
			
			
			staticCombo = new Combo(this, SWT.READ_ONLY);
			staticCombo.add("any");
			staticCombo.add("static ");
			staticCombo.add("!static ");
			
			permissionCombo.select(0);
			if( permissions != null ) {
				String [] tmp = permissionCombo.getItems();
				for( int i = 0; i < tmp.length; i++ ) {
					if( permissionCombo.getItem(i).equals(permissions)) {
						permissionCombo.select(i);
					}
				}
			}

			
			staticCombo.select(0);
			if( staticity != null ) {
				String[] tmp = staticCombo.getItems();
				for( int i = 0; i < tmp.length; i++ ) {
					if( staticCombo.getItem(i).equals(staticity)) {
						staticCombo.select(i);
					}
				}
			}
		}
		
		private void createOtherLayoutItems() {
			if( !isConstructor ) { 
				if( isMethod ) {
					type = new TypeComposite(this, true, true, 
							"Return Type Pattern (use * for any type)", typeExpr);

				} else {
					type = new TypeComposite(this, true, true, 
							"Member Type Pattern (use * for any type)", typeExpr);
				}
			} 

			className = new TypeComposite(this, false, false, 
			"Class Type Pattern (use * for any type)", classNameExpr);

	
			if(!isMethod) 
				memberName = new NameComposite(this, false, "Field pattern", memberNameExpr);
			else if( !isConstructor)
				memberName = new NameComposite(this, false, "Method pattern", memberNameExpr);
			else
				memberName = new NameComposite(this, true, "Constructor pattern", memberNameExpr);

			if( isMethod ) {
				params = new MultipleTypeComposite(this, "Parameters (Use .. for any, leave blank for none)", paramsExpr);
				exceptions = new MultipleTypeComposite(this, "Exceptions (leave blank for any)", true, exceptionExpr);
			}
		}
		private void setMemberLayoutData() {
			FormData permissionLabelData = new FormData();
			permissionLabelData.top = new FormAttachment(0,5);
			permissionLabelData.left = new FormAttachment(0,5);
			permissionLabel.setLayoutData(permissionLabelData);
			
			FormData permissionData = new FormData();
			permissionData.top = new FormAttachment(permissionLabel,5);
			permissionData.left = new FormAttachment(0,5);
			permissionCombo.setLayoutData(permissionData);
			
			FormData staticLabelData = new FormData();
			staticLabelData.top = new FormAttachment(0,5);
			staticLabelData.left = new FormAttachment(permissionCombo,5);
			staticLabel.setLayoutData(staticLabelData);
			
			FormData staticData = new FormData();
			staticData.top = new FormAttachment(staticLabel,5);
			staticData.left = new FormAttachment(permissionCombo, 5);
			staticCombo.setLayoutData(staticData);
			
			if( !isConstructor ) { 
				FormData returnTypeData = new FormData();
				returnTypeData.left = new FormAttachment(0,0);
				returnTypeData.top = new FormAttachment(permissionCombo,5);
				returnTypeData.right = new FormAttachment(100,-5);
				type.setLayoutData(returnTypeData);
				
				FormData classNameData = new FormData();
				classNameData.left = new FormAttachment(0,0);
				classNameData.top = new FormAttachment(type,5);
				classNameData.right = new FormAttachment(100,-5);
				className.setLayoutData(classNameData);
			} else {
				FormData classNameData = new FormData();
				classNameData.left = new FormAttachment(0,0);
				classNameData.top = new FormAttachment(permissionCombo,5);
				classNameData.right = new FormAttachment(100,-5);
				className.setLayoutData(classNameData);
			}
			
			FormData memberNameData = new FormData();
			memberNameData.left = new FormAttachment(0,0);
			memberNameData.right = new FormAttachment(100,-5);
			memberNameData.top = new FormAttachment(className,10);
			memberName.setLayoutData(memberNameData);
			
			
			if( isMethod ) {
				FormData paramsData = new FormData();
				paramsData.left = new FormAttachment(0,0);
				paramsData.top = new FormAttachment(memberName, 5);
				paramsData.right = new FormAttachment(100,-5);
				params.setLayoutData(paramsData);
				
				
				FormData exceptionsData = new FormData();
				exceptionsData.left = new FormAttachment(0,0);
				exceptionsData.top = new FormAttachment(params,5);
				exceptionsData.right = new FormAttachment(100,-5);
				exceptions.setLayoutData(exceptionsData);
			}
		}
		
		/**
		 * Used to default the fields to represent some string expression
		 * @param expression The expression
		 */
		public void parseFields( String expression ) {
			if( expression.equals("")) return;
			
			String firstToken;
			
			firstToken = expression.substring(0, expression.indexOf(" ") + 1);
			if( firstToken.indexOf("public") != -1 || firstToken.indexOf("private") != -1) {
				permissions = firstToken;
				expression = expression.substring(expression.indexOf(" ") + 1).trim();
			}
			
			firstToken = expression.substring(0, expression.indexOf(" ") + 1);
			if( firstToken.indexOf("static") != -1) {
				staticity = firstToken;
				expression = expression.substring(expression.indexOf(" ") + 1).trim();
			}
			
			if( !isConstructor ) {
				typeExpr = expression.substring(0, expression.indexOf(" "));
				expression = expression.substring(expression.indexOf(" ") + 1).trim();
			}
			
			classNameExpr = expression.substring(0, expression.indexOf("->"));
			expression = expression.substring(expression.indexOf("->") + 2);
			
			if( !isMethod ) {
				int paren = expression.indexOf("(");
				if( paren == -1 ) 
					memberNameExpr = expression;
				else 
					memberNameExpr = expression.substring(0, paren);
			} else {
				memberNameExpr = expression.substring(0, expression.indexOf("("));
				expression = expression.substring(expression.indexOf("(") + 1 );
				
				paramsExpr = expression.substring(0, expression.indexOf(")"));
				expression = expression.substring(expression.indexOf(")") + 1).trim();
				
				if( expression.startsWith("throws ")) {
					exceptionExpr = expression.substring( expression.indexOf(" ")+1);
				}
			}
		}
		
		
		public String toString1() {
			String retVal = "";
			String permission = permissionCombo.getSelectionIndex() < 0 ? "" :  
				permissionCombo.getItem(permissionCombo.getSelectionIndex()); 
			if( !permission.equals("any") && !permission.equals("")) {
				retVal += permission;
			}
			
			String staticS = staticCombo.getSelectionIndex() < 0 ? "" : 
				staticCombo.getItem(staticCombo.getSelectionIndex());
			if( !staticS.equals("any") && !staticS.equals("")) {
				retVal += staticS;
			}
			
			if( !isConstructor ) 
				retVal += type.toString1() + " ";
			retVal += className.toString1() + "->";
			retVal += memberName.toString1();
			if( isMethod ) {
				retVal += "(" + params.toString1() + ")";
				retVal += exceptions.toString1().equals("") ? "" : 
					" throws " + exceptions.toString1();
			}
			
			return retVal;
		}
	}
}
