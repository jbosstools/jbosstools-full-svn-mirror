/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.wizards.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;

/**
 * This wizard page configures the class loader of the cache
 * @author Gurkaner
 */
public class CacheLoaderConfigurationPage extends WizardPage
{

   private Label lblCacheLoaderClass;

   private Combo cmbCacheLoaderClass;

   private Button chkCacheLoaderShared;

   private Label lblCacheLoaderPreload;

   private Text txtCacheLoaderPreload;

   private Button chkCacheLoaderPassivation;

   private Button chkCacheLoaderFetchPersistentState;

   private Button chkCacheLoaderFetchTransientState;

   private Button chkCacheLoaderAsynchronous;

   /**CacheLoader Config for JDBC */
   private Group grpForJdbc;

   private Label lblTableName;

   private Text txtTableName;

   private Button chkTableCreate;

   private Button chkTableDrop;

   private Label lblFqnColumn;

   private Text txtFqnColumn;

   private Label lblFqnType;

   private Text txtFqnType;

   private Label lblNodeColumn;

   private Text txtNodeColumn;

   private Label lblNodeType;

   private Text txtNodeType;

   private Label lblParentColumn;

   private Text txtParentColumn;

   private Label lblDriver;

   private Combo cmbDriver;

   private Label lblDriverUrl;

   private Combo cmbDriverUrl;

   private Label lblUser;

   private Text txtUser;

   private Label lblPassword;

   private Text txtPassword;

   private Label lblDataSource;

   private Text txtDataSource;

   /**Cache Loader Config For File */
   private Label lblLocation;

   private Text txtLocation;

   /**
    * Constructor
    * @param name
    * @param title
    * @param imageDesc
    */
   public CacheLoaderConfigurationPage(String name, String title, ImageDescriptor imageDesc)
   {
      super(name, title, imageDesc);
      setDescription("Cache loader configuration informations");
   }//end of method

   /**
    * Creates the content of the wizard page
    * @param Parent of this page
    * @return none
    */
   public void createControl(Composite parent)
   {
      initializeContent(parent);

   }//end of method

   /**
    * Initialize the controls
    */
   protected void initializeContent(Composite parent)
   {
      Composite container = new Composite(parent, SWT.NONE);
      container.setLayout(new GridLayout(1, false));

      GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

      Group grpContainer = new Group(container, SWT.SHADOW_ETCHED_IN);
      grpContainer.setText("Cache Loader Configuration");
      grpContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
      grpContainer.setLayout(new GridLayout(2, false));

      GridData gridDataForCheck = new GridData(GridData.FILL_HORIZONTAL);
      gridDataForCheck.horizontalSpan = 2;

      lblCacheLoaderPreload = new Label(grpContainer, SWT.NONE);
      lblCacheLoaderPreload.setText("Cache Loader Preload:");
      txtCacheLoaderPreload = new Text(grpContainer, SWT.BORDER);
      txtCacheLoaderPreload.setText("/");
      txtCacheLoaderPreload.setLayoutData(gridData);

      chkCacheLoaderShared = new Button(grpContainer, SWT.CHECK);
      chkCacheLoaderShared.setText("Cache Loader Shared");
      chkCacheLoaderShared.setLayoutData(gridDataForCheck);

      chkCacheLoaderPassivation = new Button(grpContainer, SWT.CHECK);
      chkCacheLoaderPassivation.setText("Cache Loader Passivation");
      chkCacheLoaderPassivation.setLayoutData(gridDataForCheck);

      chkCacheLoaderFetchPersistentState = new Button(grpContainer, SWT.CHECK);
      chkCacheLoaderFetchPersistentState.setText("Cache Loader Persistent State");
      chkCacheLoaderFetchPersistentState.setLayoutData(gridDataForCheck);

      chkCacheLoaderFetchTransientState = new Button(grpContainer, SWT.CHECK);
      chkCacheLoaderFetchTransientState.setText("Cache Loader Transient State");
      chkCacheLoaderFetchTransientState.setLayoutData(gridDataForCheck);

      chkCacheLoaderAsynchronous = new Button(grpContainer, SWT.CHECK);
      chkCacheLoaderAsynchronous.setText("Cache Loader Asyncronous");
      chkCacheLoaderAsynchronous.setLayoutData(gridDataForCheck);

      lblCacheLoaderClass = new Label(grpContainer, SWT.NONE);
      lblCacheLoaderClass.setText("Cache Loader Class:");
      cmbCacheLoaderClass = new Combo(grpContainer, SWT.READ_ONLY);
      cmbCacheLoaderClass.setItems(ICacheConstants.CACHE_LOADER_CLASSES);
      cmbCacheLoaderClass.setText("");
      cmbCacheLoaderClass.setLayoutData(gridData);
      cmbCacheLoaderClass.addSelectionListener(new SelectionListener()
      {

         public void widgetSelected(SelectionEvent e)
         {
            if (((Combo) e.getSource()).getText().equals(ICacheConstants.CACHE_LOADER_CLASSES[0]))
            {
               lblLocation.setEnabled(false);
               txtLocation.setEnabled(false);
               grpForJdbc.setEnabled(true);
               Control[] childs = grpForJdbc.getChildren();
               for (int i = 0; i < childs.length; i++)
               {
                  childs[i].setEnabled(true);
               }

            }
            else
            {
               grpForJdbc.setEnabled(false);
               Control[] childs = grpForJdbc.getChildren();
               for (int i = 0; i < childs.length; i++)
               {
                  childs[i].setEnabled(false);
               }
               lblLocation.setEnabled(true);
               txtLocation.setEnabled(true);
            }
         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
            lblLocation.setEnabled(false);
            txtLocation.setEnabled(false);
            grpForJdbc.setEnabled(true);
            Control[] childs = grpForJdbc.getChildren();
            for (int i = 0; i < childs.length; i++)
            {
               childs[i].setEnabled(true);
            }
         }

      });

      lblLocation = new Label(grpContainer, SWT.NONE);
      lblLocation.setText("File Location:");
      txtLocation = new Text(grpContainer, SWT.BORDER);
      txtLocation.setLayoutData(gridData);
      lblLocation.setEnabled(false);
      txtLocation.setEnabled(false);

      GridData gDataForJdbc = new GridData(GridData.FILL_BOTH);
      gDataForJdbc.horizontalSpan = 2;

      grpForJdbc = new Group(grpContainer, SWT.SHADOW_ETCHED_IN);
      grpForJdbc.setText("JDBC Properties");
      grpForJdbc.setLayoutData(gDataForJdbc);
      grpForJdbc.setLayout(new GridLayout(2, false));

      lblTableName = new Label(grpForJdbc, SWT.NONE);
      lblTableName.setText("Table Name:");
      txtTableName = new Text(grpForJdbc, SWT.BORDER);
      txtTableName.setLayoutData(gridData);
      txtTableName.setText("jbosscache");

      chkTableCreate = new Button(grpForJdbc, SWT.CHECK);
      chkTableCreate.setText("Table Create");
      chkTableCreate.setLayoutData(gridDataForCheck);

      chkTableDrop = new Button(grpForJdbc, SWT.CHECK);
      chkTableDrop.setText("Table Drop");
      chkTableDrop.setLayoutData(gridDataForCheck);

      lblFqnColumn = new Label(grpForJdbc, SWT.NONE);
      lblFqnColumn.setText("Fqn Column:");
      txtFqnColumn = new Text(grpForJdbc, SWT.BORDER);
      txtFqnColumn.setLayoutData(gridData);
      txtFqnColumn.setText("fqn");

      lblFqnType = new Label(grpForJdbc, SWT.NONE);
      lblFqnType.setText("Fqn Type:");
      txtFqnType = new Text(grpForJdbc, SWT.BORDER);
      txtFqnType.setLayoutData(gridData);
      txtFqnType.setText("varchar(255)");

      lblNodeColumn = new Label(grpForJdbc, SWT.NONE);
      lblNodeColumn.setText("Node Column:");
      txtNodeColumn = new Text(grpForJdbc, SWT.BORDER);
      txtNodeColumn.setLayoutData(gridData);
      txtNodeColumn.setText("node");

      lblNodeType = new Label(grpForJdbc, SWT.NONE);
      lblNodeType.setText("Node Type:");
      txtNodeType = new Text(grpForJdbc, SWT.BORDER);
      txtNodeType.setLayoutData(gridData);
      txtNodeType.setText("blob");

      lblParentColumn = new Label(grpForJdbc, SWT.NONE);
      lblParentColumn.setText("Parent Column:");
      txtParentColumn = new Text(grpForJdbc, SWT.BORDER);
      txtParentColumn.setLayoutData(gridData);
      txtParentColumn.setText("parent");

      lblDataSource = new Label(grpForJdbc, SWT.NONE);
      lblDataSource.setText("Data Source:");
      txtDataSource = new Text(grpForJdbc, SWT.BORDER);
      txtDataSource.setLayoutData(gridData);
      txtDataSource.setText("java:/");

      lblDriver = new Label(grpForJdbc, SWT.NONE);
      lblDriver.setText("Driver Class:");
      cmbDriver = new Combo(grpForJdbc, SWT.READ_ONLY);
      cmbDriver.setItems(ICacheConstants.JDBC_DRIVERS);
      cmbDriver.select(0);
      cmbDriver.setLayoutData(gridData);
      cmbDriver.addSelectionListener(new SelectionListener()
      {

         public void widgetSelected(SelectionEvent e)
         {
            if (((Combo) e.getSource()).getText().equals(ICacheConstants.JDBC_DRIVERS[0]))
               cmbDriverUrl.select(0);
            else
               System.out.println("Another Driver Selection");

         }

         public void widgetDefaultSelected(SelectionEvent e)
         {
            if (((Combo) e.getSource()).getText().equals(ICacheConstants.JDBC_DRIVERS[0]))
               cmbDriverUrl.select(0);
         }

      });

      lblDriverUrl = new Label(grpForJdbc, SWT.NONE);
      lblDriverUrl.setText("Jdbc Url:");
      cmbDriverUrl = new Combo(grpForJdbc, SWT.NONE);
      cmbDriverUrl.setItems(ICacheConstants.JDBC_URL_PATTERN);
      cmbDriverUrl.select(0);
      cmbDriverUrl.setLayoutData(gridData);

      lblUser = new Label(grpForJdbc, SWT.NONE);
      lblUser.setText("User Name:");
      txtUser = new Text(grpForJdbc, SWT.BORDER);
      txtUser.setLayoutData(gridData);

      lblPassword = new Label(grpForJdbc, SWT.NONE);
      lblPassword.setText("Password:");
      txtPassword = new Text(grpForJdbc, SWT.BORDER | SWT.PASSWORD);
      txtPassword.setLayoutData(gridData);

      /*All controls in Jdbc disable by default */
      Control[] childs = grpForJdbc.getChildren();
      for (int i = 0; i < childs.length; i++)
      {
         childs[i].setEnabled(false);
      }

      setControl(container);
   }//end of method

   public boolean canFlipToNextPage()
   {
      return false;
   }

   public Button getChkCacheLoaderAsynchronous()
   {
      return chkCacheLoaderAsynchronous;
   }

   public void setChkCacheLoaderAsynchronous(Button chkCacheLoaderAsynchronous)
   {
      this.chkCacheLoaderAsynchronous = chkCacheLoaderAsynchronous;
   }

   public Button getChkCacheLoaderFetchPersistentState()
   {
      return chkCacheLoaderFetchPersistentState;
   }

   public void setChkCacheLoaderFetchPersistentState(Button chkCacheLoaderFetchPersistentState)
   {
      this.chkCacheLoaderFetchPersistentState = chkCacheLoaderFetchPersistentState;
   }

   public Button getChkCacheLoaderFetchTransientState()
   {
      return chkCacheLoaderFetchTransientState;
   }

   public void setChkCacheLoaderFetchTransientState(Button chkCacheLoaderFetchTransientState)
   {
      this.chkCacheLoaderFetchTransientState = chkCacheLoaderFetchTransientState;
   }

   public Button getChkCacheLoaderPassivation()
   {
      return chkCacheLoaderPassivation;
   }

   public void setChkCacheLoaderPassivation(Button chkCacheLoaderPassivation)
   {
      this.chkCacheLoaderPassivation = chkCacheLoaderPassivation;
   }

   public Button getChkCacheLoaderShared()
   {
      return chkCacheLoaderShared;
   }

   public void setChkCacheLoaderShared(Button chkCacheLoaderShared)
   {
      this.chkCacheLoaderShared = chkCacheLoaderShared;
   }

   public Button getChkTableCreate()
   {
      return chkTableCreate;
   }

   public void setChkTableCreate(Button chkTableCreate)
   {
      this.chkTableCreate = chkTableCreate;
   }

   public Button getChkTableDrop()
   {
      return chkTableDrop;
   }

   public void setChkTableDrop(Button chkTableDrop)
   {
      this.chkTableDrop = chkTableDrop;
   }

   public Combo getCmbCacheLoaderClass()
   {
      return cmbCacheLoaderClass;
   }

   public void setCmbCacheLoaderClass(Combo cmbCacheLoaderClass)
   {
      this.cmbCacheLoaderClass = cmbCacheLoaderClass;
   }

   public Combo getCmbDriver()
   {
      return cmbDriver;
   }

   public void setCmbDriver(Combo cmbDriver)
   {
      this.cmbDriver = cmbDriver;
   }

   public Combo getCmbDriverUrl()
   {
      return cmbDriverUrl;
   }

   public void setCmbDriverUrl(Combo cmbDriverUrl)
   {
      this.cmbDriverUrl = cmbDriverUrl;
   }

   public Group getGrpForJdbc()
   {
      return grpForJdbc;
   }

   public void setGrpForJdbc(Group grpForJdbc)
   {
      this.grpForJdbc = grpForJdbc;
   }

   public Label getLblCacheLoaderClass()
   {
      return lblCacheLoaderClass;
   }

   public void setLblCacheLoaderClass(Label lblCacheLoaderClass)
   {
      this.lblCacheLoaderClass = lblCacheLoaderClass;
   }

   public Label getLblCacheLoaderPreload()
   {
      return lblCacheLoaderPreload;
   }

   public void setLblCacheLoaderPreload(Label lblCacheLoaderPreload)
   {
      this.lblCacheLoaderPreload = lblCacheLoaderPreload;
   }

   public Label getLblDataSource()
   {
      return lblDataSource;
   }

   public void setLblDataSource(Label lblDataSource)
   {
      this.lblDataSource = lblDataSource;
   }

   public Label getLblDriver()
   {
      return lblDriver;
   }

   public void setLblDriver(Label lblDriver)
   {
      this.lblDriver = lblDriver;
   }

   public Label getLblDriverUrl()
   {
      return lblDriverUrl;
   }

   public void setLblDriverUrl(Label lblDriverUrl)
   {
      this.lblDriverUrl = lblDriverUrl;
   }

   public Label getLblFqnColumn()
   {
      return lblFqnColumn;
   }

   public void setLblFqnColumn(Label lblFqnColumn)
   {
      this.lblFqnColumn = lblFqnColumn;
   }

   public Label getLblFqnType()
   {
      return lblFqnType;
   }

   public void setLblFqnType(Label lblFqnType)
   {
      this.lblFqnType = lblFqnType;
   }

   public Label getLblLocation()
   {
      return lblLocation;
   }

   public void setLblLocation(Label lblLocation)
   {
      this.lblLocation = lblLocation;
   }

   public Label getLblNodeColumn()
   {
      return lblNodeColumn;
   }

   public void setLblNodeColumn(Label lblNodeColumn)
   {
      this.lblNodeColumn = lblNodeColumn;
   }

   public Label getLblNodeType()
   {
      return lblNodeType;
   }

   public void setLblNodeType(Label lblNodeType)
   {
      this.lblNodeType = lblNodeType;
   }

   public Label getLblParentColumn()
   {
      return lblParentColumn;
   }

   public void setLblParentColumn(Label lblParentColumn)
   {
      this.lblParentColumn = lblParentColumn;
   }

   public Label getLblPassword()
   {
      return lblPassword;
   }

   public void setLblPassword(Label lblPassword)
   {
      this.lblPassword = lblPassword;
   }

   public Label getLblTableName()
   {
      return lblTableName;
   }

   public void setLblTableName(Label lblTableName)
   {
      this.lblTableName = lblTableName;
   }

   public Label getLblUser()
   {
      return lblUser;
   }

   public void setLblUser(Label lblUser)
   {
      this.lblUser = lblUser;
   }

   public Text getTxtCacheLoaderPreload()
   {
      return txtCacheLoaderPreload;
   }

   public void setTxtCacheLoaderPreload(Text txtCacheLoaderPreload)
   {
      this.txtCacheLoaderPreload = txtCacheLoaderPreload;
   }

   public Text getTxtDataSource()
   {
      return txtDataSource;
   }

   public void setTxtDataSource(Text txtDataSource)
   {
      this.txtDataSource = txtDataSource;
   }

   public Text getTxtFqnColumn()
   {
      return txtFqnColumn;
   }

   public void setTxtFqnColumn(Text txtFqnColumn)
   {
      this.txtFqnColumn = txtFqnColumn;
   }

   public Text getTxtFqnType()
   {
      return txtFqnType;
   }

   public void setTxtFqnType(Text txtFqnType)
   {
      this.txtFqnType = txtFqnType;
   }

   public Text getTxtLocation()
   {
      return txtLocation;
   }

   public void setTxtLocation(Text txtLocation)
   {
      this.txtLocation = txtLocation;
   }

   public Text getTxtNodeColumn()
   {
      return txtNodeColumn;
   }

   public void setTxtNodeColumn(Text txtNodeColumn)
   {
      this.txtNodeColumn = txtNodeColumn;
   }

   public Text getTxtNodeType()
   {
      return txtNodeType;
   }

   public void setTxtNodeType(Text txtNodeType)
   {
      this.txtNodeType = txtNodeType;
   }

   public Text getTxtParentColumn()
   {
      return txtParentColumn;
   }

   public void setTxtParentColumn(Text txtParentColumn)
   {
      this.txtParentColumn = txtParentColumn;
   }

   public Text getTxtPassword()
   {
      return txtPassword;
   }

   public void setTxtPassword(Text txtPassword)
   {
      this.txtPassword = txtPassword;
   }

   public Text getTxtTableName()
   {
      return txtTableName;
   }

   public void setTxtTableName(Text txtTableName)
   {
      this.txtTableName = txtTableName;
   }

   public Text getTxtUser()
   {
      return txtUser;
   }

   public void setTxtUser(Text txtUser)
   {
      this.txtUser = txtUser;
   }
}//end of class