/**
 * 
 */
package org.jboss.ide.eclipse.jsr88deployer.ui.dialogs;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jsr88deployer.core.PreferenceStoreUtil;
import org.jboss.ide.eclipse.jsr88deployer.core.utils.StringProperties;
import org.jboss.ide.eclipse.jsr88deployer.ui.preferences.Deploy88PreferencePage;

/**
 * @author Rob Stryker
 */
public class DeployAssocDialog extends Dialog {

	private Label nameLabel, jarLabel, targetLabel, configLabel;
	private Combo jarCombo, targetCombo, configCombo;
	private Text nameText;
	private HashMap targetsMap;
	private DeployAssocPojo pojo;
	private DeployAssocPojo pojoParam;
	
	private boolean createOrEdit;
	
	public static final boolean CREATE = true;
	public static final boolean EDIT = false;
	
	public DeployAssocDialog( Shell shell, DeployAssocPojo pojo ) {
		this(shell);
		this.pojoParam = pojo;
		createOrEdit = EDIT;
	}

	public DeployAssocDialog(Shell shell) {
		super(shell);
		targetsMap = new HashMap();
		createOrEdit = CREATE;
		pojo = new DeployAssocPojo();
	}

	protected Control createDialogArea(Composite parent) { 
		
		parent.getShell().setText("Create a deploy association");
		
		Composite superGridComposite = (Composite)super.createDialogArea(parent);
		
		Composite main = new Composite(superGridComposite, SWT.NONE);
		
		main.setLayout(new FormLayout());
		
		createWidgets(main);
		setWidgetText();
		setWidgetData();
		addWidgetListeners();
		
		if( createOrEdit == EDIT ) 
			fillWidgetsWithValues();
		
		return superGridComposite;
	}
	
	private void fillWidgetsWithValues() {
		String name = pojoParam.getName();
		String jar = pojoParam.getJar();
		String target = pojoParam.getTarget();
		String config = pojoParam.getConfig();
		
		int tmp;

		nameText.setText(name);
		if( (tmp = getIndex(jarCombo, jar)) != -1 ) 			jarCombo.select(tmp);
		if( (tmp = getIndex(targetCombo, target)) != -1 ) 		targetCombo.select(tmp);
		resetConfigCombo();
		if( (tmp = getIndex(configCombo, config)) != -1 ) 		configCombo.select(tmp);
		
	}
	
	private int getIndex( Combo c, String s ) {
		String[] strings = c.getItems();
		for( int i = 0; i < strings.length; i++ ) {
			if( strings[i].equals(s)) 
				return i;
		}
		return -1;
	}
	
	
	
	public void createWidgets(Composite main) {
		nameLabel = new Label(main, SWT.NONE);
		jarLabel = new Label(main, SWT.NONE);
		targetLabel = new Label(main, SWT.NONE);
		configLabel = new Label(main, SWT.NONE);
		
		nameText = new Text(main, SWT.BORDER);
		jarCombo = new Combo(main, SWT.READ_ONLY);
		targetCombo = new Combo(main, SWT.READ_ONLY);
		configCombo = new Combo(main, SWT.READ_ONLY);
		
	}
	
	public void setWidgetText() {
		nameLabel.setText("Association Name:");
		jarLabel.setText("Vendor Jar:");
		targetLabel.setText("Target URI: ");
		configLabel.setText("Configuration File: ");
		
		// Jar Combo
		Object[] jars = PreferenceStoreUtil
		.loadIncrementalPreferences(Deploy88PreferencePage.JAR_PREFIX);
		for( int i = 0; i < jars.length; i++ ) {
			jarCombo.add(jars[i].toString());
		}
		
		Object[] targetObjects = PreferenceStoreUtil.
		loadIncrementalPreferences(Deploy88PreferencePage.TARGET_PREFIX);
		for( int i = 0; i < targetObjects.length; i++ ) {
			if( targetObjects[i] instanceof String ) {
				String tmp = (String)targetObjects[i];
				try {
					StringProperties pair = new StringProperties(tmp);
					// TODO USE CONSTANTS
					targetCombo.add(pair.getPiece(0));
					targetsMap.put(pair.getPiece(0), pair);
				} catch( Exception e ) {
				}
			}
		}

	}
	
	public void setWidgetData() {

		FormData nameLabelData = new FormData();
		nameLabelData.left = new FormAttachment(0,5);
		nameLabelData.top = new FormAttachment(0,5);
		nameLabel.setLayoutData(nameLabelData);

		FormData nameTextData = new FormData();
		nameTextData.top = new FormAttachment(nameLabel,5);
		nameTextData.left = new FormAttachment(0, 5);
		nameTextData.right = new FormAttachment(100,-5);
		nameText.setLayoutData(nameTextData);

		
		
		FormData jarLabelData = new FormData();
		jarLabelData.left = new FormAttachment(0,5);
		jarLabelData.top = new FormAttachment(nameText,10);
		jarLabel.setLayoutData(jarLabelData);
		
		FormData jarComboData = new FormData();
		jarComboData.top = new FormAttachment(jarLabel,5);
		jarComboData.left = new FormAttachment(0, 5);
		jarComboData.right = new FormAttachment(100, -5);
		jarCombo.setLayoutData(jarComboData);

		
		
		
		FormData targetLabelData = new FormData();
		targetLabelData.left = new FormAttachment(0,5);
		targetLabelData.top = new FormAttachment(jarCombo,10);
		targetLabel.setLayoutData(targetLabelData);
		
		FormData targetComboData = new FormData();
		targetComboData.top = new FormAttachment(targetLabel,5);
		targetComboData.left = new FormAttachment(0, 5);
		targetComboData.right = new FormAttachment(100, -5);
		targetCombo.setLayoutData(targetComboData);

		
		
		FormData configLabelData = new FormData();
		configLabelData.left = new FormAttachment(0,5);
		configLabelData.top = new FormAttachment(targetCombo,10);
		configLabel.setLayoutData(configLabelData);
		

		FormData configComboData = new FormData();
		configComboData.top = new FormAttachment(configLabel,5);
		configComboData.left = new FormAttachment(0, 5);
		configComboData.right = new FormAttachment(100, -5);
		configCombo.setLayoutData(configComboData);		
		
	}
	
	public void addWidgetListeners() {
		jarCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				resetConfigCombo();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			} 
			
		} );
		
		
		ModifyListener tmpListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateStrings();
			} 
		};
		
		nameText.addModifyListener(tmpListener);
		configCombo.addModifyListener(tmpListener);
		jarCombo.addModifyListener(tmpListener);
		targetCombo.addModifyListener(tmpListener);
		
	}
	
//	public boolean isCreateOrEdit() {
//		return createOrEdit;
//	}

	public DeployAssocPojo getPojo() {
		return pojo;
	}


	public void resetConfigCombo() {
		configCombo.removeAll();
		String text = jarCombo.getText();
		Object[] configs = 
			PreferenceStoreUtil.loadIncrementalPreferences(
					Deploy88PreferencePage.CONFIG_PREFIX + text);
		for( int i = 0; i < configs.length; i++ ) {
			configCombo.add(configs[i].toString());
		}

	}
	public void updateStrings() {
		pojo.setName( nameText.getText());
		pojo.setConfig( configCombo.getText());
		pojo.setJar( jarCombo.getText());
		pojo.setTarget(targetCombo.getText());
	}

	public static class DeployAssocPojo {
		private String name, jar, target, config;
		public DeployAssocPojo() {
			name = jar = target = config = "";
		}
		
		public DeployAssocPojo(String string) {
			this();
			String[] arr = string.split("\n");
			if( arr.length == 4 ) {
				setName(arr[0]);
				setJar(arr[1]);
				setTarget(arr[2]);
				setConfig(arr[3]);
			}
		}
		

		public String getConfig() {
			return config;
		}

		public String getJar() {
			return jar;
		}

		public String getName() {
			return name;
		}

		public String getTarget() {
			return target;
		}

		public void setConfig(String config) {
			this.config = config;
		}

		public void setJar(String jar) {
			this.jar = jar;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setTarget(String target) {
			this.target = target;
		}
		
		public String toString() {
			return name + "\n" + jar + "\n" + target + "\n" + config;
		}
		

	}


}
