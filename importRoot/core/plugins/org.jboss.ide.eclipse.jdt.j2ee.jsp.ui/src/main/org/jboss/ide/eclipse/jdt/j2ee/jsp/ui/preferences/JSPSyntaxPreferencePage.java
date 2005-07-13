/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.preferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.WorkbenchChainedTextFontFieldEditor;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.IJSPSyntaxConstants;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.JSPConfiguration;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.JSPTextTools;
import org.jboss.ide.eclipse.jdt.ui.editors.ColorEditor;
import org.jboss.ide.eclipse.jdt.ui.preferences.ITextStylePreferences;
import org.jboss.ide.eclipse.jdt.ui.preferences.OverlayPreferenceStore;
import org.jboss.ide.eclipse.jdt.ui.preferences.PreferenceDescriptor;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * The JSPSyntaxPreferencePage is a preference page that
 * handles setting the colors used by the JSP editors.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPSyntaxPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

   ColorEditor bgColorEditor;
   Button bgCustom;

   Button bgDefault;

   List colors;
   Button fgBold;

   ColorEditor fgColorEditor;

   // REVISIT hack: java-like editor behavoir
   Button fgLineNumbers;
   Button fgSpacesForTabs;

   OverlayPreferenceStore overlay;

   SourceViewer preview;

   private Color bgColor;
   private Text fgTabWidth;

   private JSPTextTools jspTextTools;
   /** Description of the Field */
   public final PreferenceDescriptor[] fKeys = new PreferenceDescriptor[]{
      new PreferenceDescriptor(PreferenceDescriptor.BOOLEAN, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_DEFAULT + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_DEFAULT + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_TAG + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_TAG + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_NAME + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_NAME + ITextStylePreferences.SUFFIX_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_NAME + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_VALUE + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_VALUE + ITextStylePreferences.SUFFIX_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_ATT_VALUE + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_COMMENT + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_COMMENT + ITextStylePreferences.SUFFIX_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_COMMENT + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_DIRECTIVE + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_DIRECTIVE + ITextStylePreferences.SUFFIX_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_DIRECTIVE + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_BRACKET + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_BRACKET + ITextStylePreferences.SUFFIX_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IJSPSyntaxConstants.JSP_BRACKET + ITextStylePreferences.SUFFIX_STYLE),
   // REVISIT hack: java-like editor behavoir
      new PreferenceDescriptor(PreferenceDescriptor.BOOLEAN, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER),
      new PreferenceDescriptor(PreferenceDescriptor.BOOLEAN, PreferenceConstants.EDITOR_SPACES_FOR_TABS),
      new PreferenceDescriptor(PreferenceDescriptor.INT, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER_COLOR),
      new PreferenceDescriptor(PreferenceDescriptor.BOOLEAN, AbstractDecoratedTextEditorPreferenceConstants.EDITOR_OVERVIEW_RULER),};

   final String[][] fSyntaxColorListModel = new String[][]{{JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.others"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_DEFAULT}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.Tag"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_TAG}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.AttName"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_ATT_NAME}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.AttValue"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_ATT_VALUE}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.Comment"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_COMMENT}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.Directive"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_DIRECTIVE}, {JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.Bracket"), //$NON-NLS-1$
   IJSPSyntaxConstants.JSP_BRACKET},};


   /** Constructor for JSPColorPreferencePage. */
   public JSPSyntaxPreferencePage()
   {
      setDescription(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.description"));//$NON-NLS-1$

      setPreferenceStore(JDTJ2EEJSPUIPlugin.getDefault().getPreferenceStore());

      overlay = new OverlayPreferenceStore(getPreferenceStore(), fKeys);
   }


   /*
    * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
    */
   /** Description of the Method */
   public void dispose()
   {
      if (jspTextTools != null)
      {
         jspTextTools.dispose();
         jspTextTools = null;
      }

      if (overlay != null)
      {
         overlay.stop();
         overlay = null;
      }

      super.dispose();
   }


   /*
    * @see IWorkbenchPreferencePage#init(IWorkbench)
    */
   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    */
   public void init(IWorkbench workbench)
   {
   }


   /*
    * @see org.eclipse.jface.preference.IPreferencePage#performOk()
    */
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performOk()
   {
      overlay.propagate();
      JDTJ2EEJSPUIPlugin.getDefault().savePluginPreferences();

      return true;
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createContents(Composite parent)
   {
      overlay.load();
      overlay.start();

      Composite colorComposite = new Composite(parent, SWT.NULL);
      colorComposite.setLayout(new GridLayout());

      Group backgroundComposite = new Group(colorComposite, SWT.SHADOW_ETCHED_IN);
      backgroundComposite.setLayout(new RowLayout());
      backgroundComposite.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.backgroundColor"));//$NON-NLS-1$

      SelectionListener backgroundSelectionListener =
         new SelectionListener()
         {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               boolean custom = bgCustom.getSelection();
               bgColorEditor.getButton().setEnabled(custom);

               overlay.setValue(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, !custom);
            }
         };

      bgDefault = new Button(backgroundComposite, SWT.RADIO | SWT.LEFT);
      bgDefault.addSelectionListener(backgroundSelectionListener);
      bgDefault.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.systemDefault"));//$NON-NLS-1$

      bgCustom = new Button(backgroundComposite, SWT.RADIO | SWT.LEFT);
      bgCustom.addSelectionListener(backgroundSelectionListener);
      bgCustom.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.custom"));//$NON-NLS-1$

      bgColorEditor = new ColorEditor(backgroundComposite);

      Label label = new Label(colorComposite, SWT.LEFT);
      label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.foreground"));//$NON-NLS-1$

      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      layout.marginHeight = 0;
      layout.marginWidth = 0;

      GridData gd = new GridData(GridData.FILL_BOTH);

      Composite editorComposite = new Composite(colorComposite, SWT.NONE);
      editorComposite.setLayout(layout);
      editorComposite.setLayoutData(gd);

      gd = new GridData(GridData.FILL_BOTH);
      gd.heightHint = convertHeightInCharsToPixels(5);

      colors = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
      colors.setLayoutData(gd);

      layout = new GridLayout();
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      layout.numColumns = 2;

      Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
      stylesComposite.setLayout(layout);
      stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

      gd = new GridData();
      gd.horizontalAlignment = GridData.BEGINNING;

      label = new Label(stylesComposite, SWT.LEFT);
      label.setLayoutData(gd);
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.color"));//$NON-NLS-1$

      fgColorEditor = new ColorEditor(stylesComposite);

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      Button fgColorButton = fgColorEditor.getButton();
      fgColorButton.setLayoutData(gd);

      gd = new GridData();
      gd.horizontalAlignment = GridData.BEGINNING;

      label = new Label(stylesComposite, SWT.LEFT);
      label.setLayoutData(gd);
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.bold"));//$NON-NLS-1$

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      fgBold = new Button(stylesComposite, SWT.CHECK);
      fgBold.setLayoutData(gd);

      // REVISIT hack: java-like editor behavoir
      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      label = new Label(stylesComposite, SWT.LEFT);
      label.setLayoutData(gd);
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.showLineNumbers"));//$NON-NLS-1$

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;
      fgLineNumbers = new Button(stylesComposite, SWT.CHECK);
      fgLineNumbers.setLayoutData(gd);

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      label = new Label(stylesComposite, SWT.LEFT);
      label.setLayoutData(gd);
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.displayedTabWidth"));//$NON-NLS-1$

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      fgTabWidth = new Text(stylesComposite, SWT.BORDER | SWT.SINGLE);
      fgTabWidth.setLayoutData(gd);
      fgTabWidth.setTextLimit(4);

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;

      label = new Label(stylesComposite, SWT.LEFT);
      label.setLayoutData(gd);
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.insertSpaceForTabs"));//$NON-NLS-1$

      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;
      fgSpacesForTabs = new Button(stylesComposite, SWT.CHECK);
      fgSpacesForTabs.setLayoutData(gd);
      // end hack: java-like editor behavoir

      label = new Label(colorComposite, SWT.LEFT);
      label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      label.setText(JDTJ2EEJSPUIMessages.getString("JspSyntaxPreferencePage.preview"));//$NON-NLS-1$

      gd = new GridData(GridData.FILL_BOTH);
      gd.widthHint = convertWidthInCharsToPixels(20);
      gd.heightHint = convertHeightInCharsToPixels(5);

      Control previewer = createPreviewer(colorComposite);
      previewer.setLayoutData(gd);

      colors.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               handleSyntaxColorListSelection();
            }
         });

      bgColorEditor.getButton().addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               PreferenceConverter.setValue(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, bgColorEditor.getColorValue());
            }
         });

      fgColorButton.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               int i = colors.getSelectionIndex();

               String key = fSyntaxColorListModel[i][1];

               PreferenceConverter.setValue(overlay, key + ITextStylePreferences.SUFFIX_FOREGROUND, fgColorEditor.getColorValue());
            }
         });

      fgBold.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               int i = colors.getSelectionIndex();

               String key = fSyntaxColorListModel[i][1];

               String value = (fgBold.getSelection()) ? ITextStylePreferences.STYLE_BOLD : ITextStylePreferences.STYLE_NORMAL;

               overlay.setValue(key + ITextStylePreferences.SUFFIX_STYLE, value);
            }
         });

      // REVISIT hack: java-like editor behavoir
      fgLineNumbers.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               overlay.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER, fgLineNumbers.getSelection());
            }
         });

      fgSpacesForTabs.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               overlay.setValue(PreferenceConstants.EDITOR_SPACES_FOR_TABS, fgSpacesForTabs.getSelection());
            }
         });

      fgTabWidth.addModifyListener(
         new ModifyListener()
         {
            public void modifyText(ModifyEvent e)
            {
               String number = ((Text) e.widget).getText();
               try
               {
                  int value = Integer.parseInt(number);
                  if (value > 0)
                  {
                     overlay.setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH, value);
                  }
               }
               catch (NumberFormatException ex)
               {
                  // ignore
               }
            }
         });
      // end hack: java-like editor behavoir

      initialize();

      return colorComposite;
   }


   /*
    * @see PreferencePage#performDefaults()
    */
   /** Description of the Method */
   protected void performDefaults()
   {
      overlay.loadDefaults();
      //initializeFields();
      handleSyntaxColorListSelection();

      super.performDefaults();

      preview.invalidateTextPresentation();
   }


   /** Description of the Method */
   void handleSyntaxColorListSelection()
   {
      int i = colors.getSelectionIndex();

      String key = fSyntaxColorListModel[i][1];

      RGB rgb = PreferenceConverter.getColor(overlay, key + ITextStylePreferences.SUFFIX_FOREGROUND);

      fgColorEditor.setColorValue(rgb);

      // REVISIT
      fgBold.setSelection(overlay.getString(key + ITextStylePreferences.SUFFIX_STYLE).indexOf(ITextStylePreferences.STYLE_BOLD) >= 0);

      // REVISIT hack: java-like editor behavoir
      fgLineNumbers.setSelection(overlay.getBoolean(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER));

      fgSpacesForTabs.setSelection(overlay.getBoolean(PreferenceConstants.EDITOR_SPACES_FOR_TABS));

      // REVISIT string?
      fgTabWidth.setText("" + overlay.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH));//$NON-NLS-1$
   }


   /**
    * Initializes the given viewer's colors.
    *
    * @param viewer  the viewer to be initialized
    */
   void initializeViewerColors(ISourceViewer viewer)
   {
      if (overlay != null)
      {
         StyledText styledText = viewer.getTextWidget();

         // ---------- background color ----------------------
         Color color = null;
         if (!overlay.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT))
         {
            color = createColor(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, styledText.getDisplay());
         }

         styledText.setBackground(color);

         if (bgColor != null)
         {
            bgColor.dispose();
         }

         bgColor = color;
      }
   }


   /**
    * Creates a color from the information stored in the given
    * preference store. Returns <code>null</code> if there is no
    * such information available.
    *
    * @param store    Description of the Parameter
    * @param key      Description of the Parameter
    * @param display  Description of the Parameter
    * @return         Description of the Return Value
    */
   private Color createColor(IPreferenceStore store, String key, Display display)
   {
      RGB rgb = null;

      if (store.contains(key))
      {
         if (store.isDefault(key))
         {
            rgb = PreferenceConverter.getDefaultColor(store, key);
         }
         else
         {
            rgb = PreferenceConverter.getColor(store, key);
         }

         if (rgb != null)
         {
            return new Color(display, rgb);
         }
      }

      return null;
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   private Control createPreviewer(Composite parent)
   {
      jspTextTools = new JSPTextTools(overlay);

      preview = new SourceViewer(parent, null, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

      preview.configure(new JSPConfiguration(jspTextTools, null));
      preview.getTextWidget().setFont(JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT));
      preview.setEditable(false);

      initializeViewerColors(preview);

      String content = loadPreviewContentFromFile("preview.jsp");//$NON-NLS-1$
      IDocument document = new Document(content);

      IDocumentPartitioner partitioner = jspTextTools.createJSPPartitioner();

      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);

      preview.setDocument(document);

      overlay.addPropertyChangeListener(
         new IPropertyChangeListener()
         {
            public void propertyChange(PropertyChangeEvent event)
            {
               String p = event.getProperty();
               if (p.equals(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND) || p.equals(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT))
               {
                  initializeViewerColors(preview);
               }

               preview.invalidateTextPresentation();
            }
         });

      return preview.getControl();
   }


   /** */
   private void initialize()
   {
      initializeFields();

      for (int i = 0; i < fSyntaxColorListModel.length; i++)
      {
         colors.add(fSyntaxColorListModel[i][0]);
      }

      colors.getDisplay().asyncExec(
         new Runnable()
         {
            public void run()
            {
               colors.select(0);
               handleSyntaxColorListSelection();
            }
         });
   }


   /** Description of the Method */
   private void initializeFields()
   {
      RGB rgb = PreferenceConverter.getColor(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
      bgColorEditor.setColorValue(rgb);

      boolean def = overlay.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT);
      bgDefault.setSelection(def);
      bgCustom.setSelection(!def);
      bgColorEditor.getButton().setEnabled(!def);
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   private String loadPreviewContentFromFile(String filename)
   {
      StringBuffer string = new StringBuffer(512);

      try
      {
         char[] buf = new char[512];
         BufferedReader reader = new BufferedReader(new InputStreamReader(JSPSyntaxPreferencePage.class.getResourceAsStream(filename)));

         try
         {
            while (true)
            {
               int n = reader.read(buf);
               if (n < 0)
               {
                  break;
               }

               string.append(buf, 0, n);
            }
         }
         finally
         {
            reader.close();
         }
      }
      catch (IOException e)
      {
      }

      return string.toString();
   }


   /**
    * Description of the Method
    *
    * @param store  Description of the Parameter
    */
   public static void initDefaults(IPreferenceStore store)
   {
      PreferenceConverter.setDefault(store, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND)
         .getRGB());

      store.setDefault(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT, true);

      PreferenceConverter.setDefault(store, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND)
         .getRGB());

      store.setDefault(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, true);

      WorkbenchChainedTextFontFieldEditor.startPropagate(store, JFaceResources.TEXT_FONT);

      //		store.setDefault(
      //			DefaultSourceViewerConfiguration.PREFERENCE_TAB_WIDTH, 4);

      setDefault(store, IJSPSyntaxConstants.JSP_DEFAULT, "0,0,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$

      setDefault(store, IJSPSyntaxConstants.JSP_COMMENT, "127,0,0", "255,255,191", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$ //$NON-NLS-2$

      setDefault(store, IJSPSyntaxConstants.JSP_DIRECTIVE, "127,0,0", "255,255,191", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$ //$NON-NLS-2$

      setDefault(store, IJSPSyntaxConstants.JSP_BRACKET, "127,0,0", "255,255,191", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$ //$NON-NLS-2$

      setDefault(store, IJSPSyntaxConstants.JSP_TAG, "0,0,191", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$

      // REVISIT: directive/tag attributes
      setDefault(store, IJSPSyntaxConstants.JSP_ATT_NAME, "0,191,0", "255,255,191", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$ //$NON-NLS-2$

      setDefault(store, IJSPSyntaxConstants.JSP_ATT_VALUE, "0,0,191", "255,255,191", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$ //$NON-NLS-2$

      // REVISIT hack: java-like editor behavoir
      store.setDefault(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER, false);
      store.setDefault(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER_COLOR, "0,0,0");//$NON-NLS-1$
      store.setDefault(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH, 4);
      store.setDefault(PreferenceConstants.EDITOR_SPACES_FOR_TABS, false);
   }


   /**
    * Sets the default attribute of the JSPSyntaxPreferencePage class
    *
    * @param store     The new default value
    * @param constant  The new default value
    * @param color     The new default value
    * @param style     The new default value
    */
   private static void setDefault(IPreferenceStore store, String constant, String color, String style)
   {
      store.setDefault(constant + ITextStylePreferences.SUFFIX_FOREGROUND, color);

      store.setDefault(constant + ITextStylePreferences.SUFFIX_STYLE, style);
   }


   /**
    * Sets the default attribute of the JSPSyntaxPreferencePage class
    *
    * @param store       The new default value
    * @param constant    The new default value
    * @param foreground  The new default value
    * @param background  The new default value
    * @param style       The new default value
    */
   private static void setDefault(IPreferenceStore store, String constant, String foreground, String background, String style)
   {
      store.setDefault(constant + ITextStylePreferences.SUFFIX_FOREGROUND, foreground);

      store.setDefault(constant + ITextStylePreferences.SUFFIX_BACKGROUND, background);

      store.setDefault(constant + ITextStylePreferences.SUFFIX_STYLE, style);
   }
}
